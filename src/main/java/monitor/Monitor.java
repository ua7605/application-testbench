package monitor;

import be.uantwerpen.idlab.dust.coordinator.HardwareMonitor.HardwareMonitor;
import be.uantwerpen.idlab.dust.coordinator.HardwareMonitor.OSHI.OSHIHardwareFactory;
import org.json.JSONObject;
import org.json.XML;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

import java.io.*;

//TODO: Het type device er nog uithalen

public class Monitor
{

    public HardwareMonitor hardwareMonitor;
    public static String xmlFilePathMemory = "MetricsMemory.xml";
    public static String xmlFilePathProcessor = "MetricsProcessor.xml";
    public static String xmlFilePathLinks = "MetricsLinks.xml";
    public static String OutputMetricsLinks = "OutputMetricsLinks.xml";
    public static String OutputMetricsProcessor = "OutputMetricsProcessor.xml";
    public static String OutputMetricsProcessorJSON = "OutputProcessorMemory.json";
    public static String OutputMetricsMemory = "OutputMetricsMemory.xml";
    public static String OutputMetricsMemoryJSON = "OutputMetricsMemory.json";
    public static String OutputMetricsLinksJSON = "OutputMetricsLinks.json";
    public static String outputMetricsDUSTChannels = "OutputDUSTChannels.xml";
    public static String outputMetricsDUSTChannelsjson = "OutputDUSTChannels.json";

    protected String MBps = " MBp/s";
    protected String GBs = " Gb/s";
    protected String ms = " ms";
    protected String GB = " GB";
    protected int ID;
    public SystemInfo systemInfo;
    public String nameOfDevice;
    public String typeOfDevice;
    public static HardwareAbstractionLayer hardwareAbstractionLayer;

    protected boolean isAppRunning = false;

    //TODO: De monitor zou moeten weten wanneer de applicatie is gestart.: ok

    public Monitor()
    {

        hardwareMonitor = new HardwareMonitor(new OSHIHardwareFactory());
        hardwareMonitor.measure();
        ID = 0;
        systemInfo = new SystemInfo();
        hardwareAbstractionLayer = systemInfo.getHardware();
        typeOfDevice = systemInfo.getOperatingSystem().getManufacturer(); // Hierdoor gaat er bij de metingen gezien kunnen worden op welk device het werd uitgevoerd.
        nameOfDevice = systemInfo.getHardware().getComputerSystem().getModel();
    }

    public boolean getIsAppRunning()
    {
        return isAppRunning;
    }

    public void setIsAppRunning(boolean isRunning)
    {
        isAppRunning = isRunning;
    }

    protected Double byte_to_GB(long _byte)
    {
        Double gb = (_byte/1024/1024) * Math.pow(10,-3);
        return gb;
    }

    protected Double byte_to_MBps(long _byte)
    {
        return (_byte/1024/1024) * Math.pow(10,-2);
    }

    public HardwareMonitor monitor()
    {
        return hardwareMonitor;
    }

    protected void measure()
    {
        hardwareMonitor.measure();
    }


    public static void convertXmlToJson(String pathToXMLFile, String pahtToJSONfile)
    {

        int PRETTY_FACTOR=4;
        try {
            File xmlFile = new File(pathToXMLFile);
            InputStream inputStream = new FileInputStream(xmlFile);
            StringBuilder builder = new StringBuilder();
            int ptr;
            while ((ptr = inputStream.read()) != -1) {
                builder.append((char) ptr);
            }

            String xml = builder.toString();
            JSONObject jsonObj = XML.toJSONObject(xml);
            //System.out.print(jsonObj);
            FileWriter fileWriter = new FileWriter(pahtToJSONfile);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(jsonObj.toString(PRETTY_FACTOR));
            bufferedWriter.close();
        } catch (IOException ex) {
            System.out.println(
                    "Error writing to file '" + pahtToJSONfile + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
