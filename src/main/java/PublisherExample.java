import be.uantwerpen.idlab.dust.core.Core;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PublisherExample {

    public static void main(String[] args) throws InterruptedException {
        Core core = new Core("publish-block","/home/vincentcharpentier/project/application-testbench/modules");

        core.setup();
        core.setConfigurationFile("/home/vincentcharpentier/project/application-testbench/configuration.json");
        core.connect();
        Thread t = core.cycleForever();

        TimeUnit.SECONDS.sleep(1);

        byte[] payload = {0x01, 0x02, 0x03};
        for (int i=0;i < 10; i++){
            core.publish("publish-tcp", payload);

            TimeUnit.SECONDS.sleep(1);
        }
        core.disconnect();

        core.cycleStop();

        // wait until cycle has stopped
        t.join();
    }

}
