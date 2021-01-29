package monitor;

import be.uantwerpen.idlab.dust.coordinator.HardwareMonitor.HardwareMonitor;

import be.uantwerpen.idlab.dust.coordinator.HardwareMonitor.OSHI.OSHIHardwareFactory;
import be.uantwerpen.idlab.dust.coordinator.NetworkMonitor.Iperf.IperfClient;
import be.uantwerpen.idlab.dust.coordinator.NetworkMonitor.Iperf.IperfServer;
import be.uantwerpen.idlab.dust.coordinator.NetworkMonitor.RouteMonitor;

import java.io.File;
import java.text.DecimalFormat;


public class DevMonitor implements Runnable {
    //private final Logger logger = (Logger) LoggerFactory.getLogger(DeviceMonitor.class);
    private static HardwareMonitor hardwareMonitor;
    public static DecimalFormat decimalFormat;
    //private NetworkController networkController;

    public DevMonitor() {
        createDir();
        decimalFormat = new DecimalFormat("0.00");
        hardwareMonitor = new HardwareMonitor(new OSHIHardwareFactory());
        hardwareMonitor.measure();
        System.out.println("used Memory: "+hardwareMonitor.getMemory().getUsedMemorySize());
        System.out.println("Used memory converted: "+hardwareMonitor.getMemory().getUsedMemorySize()/1024/1024);
        //HardwareFactory factory = new OSHIHardwareFactory();
        //this.hardwareMonitor = new HardwareMonitor(factory);
        //this.networkController = new NetworkController(hardwareMonitor);
        // networkController.runRoutingMonitor();
    }

    public void testHarwareMonitor()
    {
        hardwareMonitor.measure();
        hardwareMonitor.getHardwareMessage();
        System.out.println(hardwareMonitor.getMemory().getUsedMemorySize() * Math.pow(10,-6) );//Output is in kB hardwareMonitor.getMemory().getUsedMemorySize()

    }

    private static void tesDeviceMemory(){
        //Memory memoryController = new Memory();
    }

    private static void testRouteManager()
    {
        RouteMonitor monitor = new RouteMonitor();
        monitor.readAll();
        monitor.printTable();
    }

    private static void testIperfManager() throws InterruptedException
    {
        IperfServer iperfServer = new IperfServer();
        Thread.sleep(100);
        IperfClient iperfClient = new IperfClient("localhost");
        iperfServer.checkErrors();
        iperfServer.shutdown();
    }

    private void createDir(){
        String dir = "results";
        File file = new File(dir);

        if(file.mkdir()){
            System.out.println("Directory 'resluts' created succesvol");
        }
    }

    public void run() {
        try {
            long currentTime = System.currentTimeMillis();
            hardwareMonitor.measure();
            long nextTime = System.currentTimeMillis();
        }catch (Throwable t){
            System.out.println("Exception");
        }

    }
}
