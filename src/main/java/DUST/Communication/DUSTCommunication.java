package DUST.Communication;

import be.uantwerpen.idlab.dust.core.Core;
import java.util.function.Consumer;

public class DUSTCommunication implements ICommunication {

    private String configFilePath;
    public String blockName;
    private Core dustCore;
    private Thread coreThread;


    public DUSTCommunication(String configFilePath, String modulePath, String blockName)
    {

        this.blockName = blockName;

        this.configFilePath = configFilePath;

        // initialises the core with the given block name and the directory where the modules are located (default "../modules")dust
        this.dustCore = new Core(blockName,modulePath);

        // load the core, this includes reading the libraries in the modules directory to check addons and transports are available
        this.dustCore.setup();

        // Wraps the given file in a configuration object
        this.dustCore.setConfigurationFile(configFilePath);

        // connects all channels
        this.dustCore.connect();

        this.coreThread = this.dustCore.cycleForever();

    }

    @Override
    public void registerListener(String channelName, Consumer<byte[]> consumer)
    {
        this.dustCore.registerListener(channelName, consumer);
    }

    @Override
    public void run()
    {
    }

    @Override
    public void publish(String topic, byte[] message)
    {
        this.dustCore.publish(topic,message);
    }

    public String getBlockName()
    {
        return blockName;
    }
}
