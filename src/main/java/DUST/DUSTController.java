package DUST;
import TestbenchController.Controller;
import DUST.Communication.CommunicationHandler;
import DUST.Communication.DUSTCommunication;
import DUST.Communication.ICommunication;
import DUST.ConfigurationFiles.ConfigFile;
import DUST.ConfigurationFiles.DUSTBlockChannels;
import DUST.ConfigurationFiles.JSON;
import Lists.ListDUSTChannels;
import Metrics.MetricDUSTChannels;
import monitor.Monitor;
import org.json.simple.parser.ParseException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DUSTController extends Controller
{
    public ArrayList<CommunicationHandler> listDUSTchannelSUBs = new ArrayList<>();

    private List<MetricDUSTChannels> listDUSTOutputFile;

    public ListDUSTChannels listDUSTLinksForOutput;

    public static DUSTController createADUSTAgent(String tomlConfigFilepath) throws IOException, ParseException
    {
        makeDirForOutPutFiles();

        ConfigFile.initDustConfigurationFile(tomlConfigFilepath);

        ConfigFile configFile = ConfigFile.getDustConfigurationFileInstance();

        JSON.initializeDUSTChannelFile(configFile.getString("Agent.pathFile_toDUSTConfig"));

        DUSTBlockChannels dustBlockChannels = DUSTBlockChannels.getInstance();

        ICommunication dustCommunicationCore = new DUSTCommunication(configFile.getString("Agent.config_file_path"), configFile.getString("Agent.module_path"), dustBlockChannels.getBlockName());

        return new DUSTController(configFile.getString("Agent.Name"),configFile.getString("Agent.LocalSoftwareFolder"),dustCommunicationCore,doubleToInt(configFile.getDouble("Agent.doAmeasurementEveryInMicroSeconds",10000.0)), configFile.getString("Agent.startPhraseToStartMonitoring","yes"), doubleToInt(configFile.getDouble("Agent.totalTimeOfTheMeasurementInSeconds",60.0)));
    }

    protected DUSTController(String agentName, String localSoftwareFolder, ICommunication communication, int MeasureEvery, String phrase, int totalTimeTestbenchNeedsToRun)
    {
        super(agentName,localSoftwareFolder,communication,MeasureEvery, phrase, totalTimeTestbenchNeedsToRun);

        this.DUSTcommunication = communication;

        DUSTBlockChannels dustBlockChannels = DUSTBlockChannels.getInstance();

        this.listDUSTOutputFile = new ArrayList<MetricDUSTChannels>();

        this.listDUSTLinksForOutput = new ListDUSTChannels();

        for (String channelName : dustBlockChannels.getChannelNameList())
        {
            CommunicationHandler channel = new CommunicationHandler(this, this.DUSTcommunication,channelName);
            listDUSTchannelSUBs.add(channel);
        }

    }

    public ArrayList<CommunicationHandler> getListDUSTchannelSUBs()
    {
        return listDUSTchannelSUBs;
    }

    public void getTotalAmountOfSendMessagesPerDUSTChannel() throws JAXBException
    {
        System.out.println("");
        for (CommunicationHandler aDUSTChannel: getListDUSTchannelSUBs())
        {
            listDUSTOutputFile.add(new MetricDUSTChannels(aDUSTChannel.getChannelName(),aDUSTChannel.getTotalSizeOfAllMessagesSend(), aDUSTChannel.getTotalMessagescounter(), aDUSTChannel.getTotalMessagescounter()/doubleToInt(ConfigFile.getDustConfigurationFileInstance().getDouble("Agent.totalTimeOfTheMeasurementInSeconds")), aDUSTChannel.getTotalSizeOfAllMessagesSend()/doubleToInt(ConfigFile.getDustConfigurationFileInstance().getDouble("Agent.totalTimeOfTheMeasurementInSeconds"))));
            listDUSTLinksForOutput.setListDUSTLinks(listDUSTOutputFile);
            System.out.println("Op Channel: "+aDUSTChannel.getChannelName()+" are: "+aDUSTChannel.getTotalMessagescounter()+" Messages in total send");
        }
        if (!listDUSTOutputFile.isEmpty())
        {
            marshallOutPutFile(listDUSTLinksForOutput);
            Monitor.convertXmlToJson(Monitor.outputMetricsDUSTChannels, Monitor.outputMetricsDUSTChannelsjson);
        }
        System.out.println("OutputFiles can be found at: "+outputFilePaht);
    }

    @Override
    public void startTestbench() throws JAXBException, InterruptedException
    {
        super.startTestbench();
        if (!isTestbenchRunning)
        {
            this.getTotalAmountOfSendMessagesPerDUSTChannel();
            System.exit(0);
        }
    }

    private static int doubleToInt(double DoubleValue)
    {
        return (int) Math.round(DoubleValue);
    }







    // Old neglect it will not wait on the user.
    @Override
    public void startTestbenchOLD() throws JAXBException, InterruptedException
    {
        super.startTestbenchOLD();
        if (!isTestbenchRunning)
        {
            this.getTotalAmountOfSendMessagesPerDUSTChannel();
            System.exit(0);
        }
    }

    protected void marshallOutPutFile(ListDUSTChannels listDUSTChannels) throws JAXBException
    {
        /******************** Marshalling *****************************/
        JAXBContext jaxbContext = JAXBContext.newInstance(ListDUSTChannels.class, MetricDUSTChannels.class);

        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(listDUSTChannels, new File(Monitor.outputMetricsDUSTChannels));
    }
}

