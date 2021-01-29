package DUST.Communication;

import java.util.function.Consumer;

public interface ICommunication {

    void registerListener(String channelName, Consumer<byte []> consumer);

    void run();

    void publish(String topic, byte[] message);

}
