package TestbenchController;

import Application.Application;
import DUST.Communication.CommunicationHandler;
import DUST.Communication.ICommunication;
import Timer.TimerForMeasurements;
import monitor.Monitor;
import monitor.MonitorLinks;
import monitor.MonitorMemory;
import monitor.MonitorProcessor;
import org.apache.commons.io.FileUtils;
import javax.xml.bind.JAXBException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public abstract class Controller {


    private final String agentName;
    protected ICommunication DUSTcommunication;
    protected CommunicationHandler communicationHandler;

    private MonitorMemory monitorMemory;
    private MonitorProcessor monitorProcessor;
    private MonitorLinks monitorLinks;

    private Application application;
    protected TimerForMeasurements timer;

    protected appstate state;
    protected static String homePath;
    protected boolean isTestbenchRunning = true;
    protected int MeasureEvery;
    private Boolean userinput = true;
    private String keyphrase;
    private int totalTimeTestbenchNeedsToRun;
    private int TWOSECONDS = 2000;
    protected static String outputFilePaht;
    public static ArrayList<String> csvFiles;
    public enum appstate{
        beforeAppisRunning,
        waitingOnUserInput,
        appisRunning
    }
    enum phrase{
        firtstime,
        errorphrase
    }


    protected Controller(String agentName, String localSoftwareFolder, ICommunication DUSTcommunication, int MeasureEvery, String phrases, int totalTimeTestbenchNeedsToRun)
    {
        this.agentName = agentName;

        this.keyphrase = phrases;

        this.monitorMemory = new MonitorMemory();

        this.monitorProcessor = new MonitorProcessor();

        this.monitorLinks = new MonitorLinks();

        //this.application = new Application(this.monitorMemory, this.monitorProcessor, this.monitorLinks);

        this.state = appstate.beforeAppisRunning;

        this.MeasureEvery = MeasureEvery;

        this.totalTimeTestbenchNeedsToRun = totalTimeTestbenchNeedsToRun/2;

        this.timer = new TimerForMeasurements(this.MeasureEvery,monitorLinks,monitorProcessor,monitorMemory);

    }

    public MonitorMemory getMonitorMemory() {
        return monitorMemory;
    }

    public MonitorProcessor getMonitorProcessor() {
        return monitorProcessor;
    }

    public MonitorLinks getMonitorLinks() {
        return monitorLinks;
    }

    protected static void makeDirForOutPutFiles()
    {
        homePath = System.getProperty("user.home");
        homePath = homePath+"/TestBenchFiles";
        try {
            removeDir(homePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        checkDirectory(homePath);
        setLoggerFiles();
        setOutputFiles();
        makelogdir();
    }

    protected static void setLoggerFiles()
    {
        String logFilesPaht = homePath+"/logFiles";
        checkDirectory(logFilesPaht);

        Monitor.xmlFilePathMemory = logFilesPaht+"/MetricsMemory.xml";
        makeFiles(Monitor.xmlFilePathMemory);

        Monitor.xmlFilePathProcessor = logFilesPaht+"/MetricsProcessor.xml";
        makeFiles(Monitor.xmlFilePathProcessor);

        Monitor.xmlFilePathLinks = logFilesPaht+"/MetricsLinks.xml";
        makeFiles(Monitor.xmlFilePathLinks);

    }

    protected static void setOutputFiles()
    {
        outputFilePaht = homePath+"/outputFiles";
        checkDirectory(outputFilePaht);

        Monitor.OutputMetricsLinks = outputFilePaht+"/OutputMetricsLinks.xml";
        makeFiles(Monitor.OutputMetricsLinks);

        Monitor.OutputMetricsProcessor = outputFilePaht+"/OutputMetricsProcessor.xml";
        makeFiles(Monitor.OutputMetricsProcessor);

        Monitor.OutputMetricsProcessorJSON = outputFilePaht+"/Processor.json";
        makeFiles(Monitor.OutputMetricsProcessorJSON);

        Monitor.OutputMetricsMemory = outputFilePaht+"/OutputMetricsMemory.xml";
        makeFiles(Monitor.OutputMetricsMemory);

        Monitor.OutputMetricsMemoryJSON = outputFilePaht+"/Memory.json";
        makeFiles(Monitor.OutputMetricsMemoryJSON);

        Monitor.OutputMetricsLinksJSON = outputFilePaht+"/PhysicalLinks.json";
        makeFiles(Monitor.OutputMetricsLinksJSON);

        Monitor.outputMetricsDUSTChannels = outputFilePaht+"/OutputDUSTChannels.xml";
        makeFiles(Monitor.outputMetricsDUSTChannels);

        Monitor.outputMetricsDUSTChannelsjson = outputFilePaht+"/DUSTChannels.json";
        makeFiles(Monitor.outputMetricsDUSTChannelsjson);
    }
    protected static void checkDirectory(String directory)
    {

        File directoryFile = new File(directory);
        if (!directoryFile.exists())
        {
            boolean added = directoryFile.mkdirs();
            //System.out.println("Volgende directory is aangemaakt: "+directory);
        }
    }

    protected static void makelogdir(){
        String currentdir = System.getProperty("user.dir");
        currentdir = currentdir+"/results";
        checkDirectory(currentdir);

        makeFiles(currentdir+"/monitor-memory.csv");
        makeFiles(currentdir+"/monitor-transceiver.csv");
        makeFiles(currentdir+"/monitor-processor.csv");
    }

    protected static void makeFiles(String directory)
    {

        File file = new File(directory);
        try
        {
            new FileWriter(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    protected static void removeDir(String directory) throws IOException {

        File directoryFile = new File(directory);
        if (directoryFile.exists())
        {
            // First, remove files from into the folder
            FileUtils.cleanDirectory(directoryFile);

            // Then, remove the folder
            FileUtils.deleteDirectory(directoryFile);
        }
    }
    public void startTestbench() throws JAXBException, InterruptedException
    {
        this.timer.Measure();
        this.isTestbenchRunning = true;
        Scanner userInput = new Scanner(System.in);
        int teller = 0;


        while (isTestbenchRunning)
        {
            switch (state)
            {
                case beforeAppisRunning:

                    if(teller == 5)
                    {
                        this.userinput = false;
                        this.timer.stop();
                        String inputuser = getUserphrase(userInput, phrase.firtstime);
                        if (inputuser.equals(this.keyphrase))
                        {
                            this.startMonitor();
                            teller = 5;
                            break;
                        }else{
                            while (!userinput){
                                String userinputerror = getUserphrase(userInput, phrase.errorphrase);
                                if (userinputerror.equals(this.keyphrase)){
                                    this.userinput = true;
                                    this.startMonitor();
                                    teller = 5;
                                    break;
                                }else {
                                    this.userinput = false;
                                }
                            }
                        }
                    }

                case appisRunning:

                    if (teller >= this.totalTimeTestbenchNeedsToRun)
                    {
                        isTestbenchRunning = false;
                        this.monitorLinks.determineWorstCaseMetrics();
                        this.monitorMemory.determineWorstCaseMetrics();
                        this.monitorProcessor.determineWorstCaseMetrics();
                        this.timer.stop();
                        break;
                    }
            }
            sleep(TWOSECONDS);
            teller++;
            if(teller >= this.totalTimeTestbenchNeedsToRun+1){
                teller = 100;
            }
        }

    }

    private void startMonitor()
    {
        this.userinput = true;
        System.out.println("The Testbench will calculate the metrics of the application ...");
        this.timer = new TimerForMeasurements(this.MeasureEvery,monitorLinks,monitorProcessor,monitorMemory);
        this.timer.Measure();
        this.monitorLinks.setIsAppRunning(true);
        this.monitorMemory.setIsAppRunning(true);
        this.monitorProcessor.setIsAppRunning(true);
        this.state = appstate.appisRunning;

    }

    private String getUserphrase(Scanner userInput,phrase state)
    {
        switch (state)
        {
            case firtstime:
                System.out.println("==========================================================================>Please start the application that has to be tested<==========================================================================");
                System.out.println("When the application that needs to be tested has started please type in the command line: '"+this.keyphrase +"'");
                break;
            case errorphrase:
                System.out.println("Sorry, you made a typo please type: '"+this.keyphrase +"'");
                break;

        }
        return userInput.nextLine();
    }



    public void startTestbenchOLD() throws JAXBException, InterruptedException
    {

        this.timer.Measure();
        this.isTestbenchRunning = true;
        int teller = 0;

        while (isTestbenchRunning){

            System.out.println("Teller: "+teller);
            if(teller ==5){
                this.monitorLinks.setIsAppRunning(true);
                this.monitorMemory.setIsAppRunning(true);
                this.monitorProcessor.setIsAppRunning(true);
                System.out.println("===============================================>Start nu de applicatie op!!!!<===================================================================================================");
            }
            if (teller >= 30){
                isTestbenchRunning = false;
                this.monitorLinks.determineWorstCaseMetrics();
                this.monitorMemory.determineWorstCaseMetrics();
                this.monitorProcessor.determineWorstCaseMetrics();
                this.timer.stop();
            }
            sleep(TWOSECONDS);
            teller++;
        }
    }









}
