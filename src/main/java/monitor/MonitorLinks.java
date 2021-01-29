package monitor;

import Lists.ListLinks;
import Metrics.Metriclinks;
import be.uantwerpen.idlab.dust.coordinator.DeviceModels.Transceiver;
import be.uantwerpen.idlab.dust.coordinator.NetworkMonitor.ControlLink;
import be.uantwerpen.idlab.dust.coordinator.NetworkMonitor.LinkMonitor;
import be.uantwerpen.idlab.dust.coordinator.NetworkMonitor.NetworkController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MonitorLinks extends Monitor{

    public List<Transceiver> listTransceivers;
    public LinkMonitor linkMonitor;
    public NetworkController networkController;
    public List<Metriclinks> listLinks;
    public ListLinks lijstMetLinks;

    private List<Metriclinks> listOutputFile;

    public ListLinks listLinksForOutput;

    public List<Metriclinks> listLinksAppIsNotRunning; // Hierin zullen al de metrics komen te staan van metigen die uitgevoed zijn voor dat de applicatie werd gestart
    public List<Metriclinks> listLinksAppIsRunning;

    private long lowestBWBeforeAppIsRunning = 0;
    private final boolean getlowest = true;
    private final boolean getHighest = false;


    enum metricoflinks{
        bandwith,
        latency
    }

    public MonitorLinks()
    {

        listTransceivers = hardwareMonitor.getTransceivers();
        networkController = new NetworkController(hardwareMonitor);
        networkController.runRoutingMonitor();
        networkController.runNetworkMonitors();
        listLinks = new ArrayList<Metriclinks>();
        lijstMetLinks = new ListLinks();
        listLinksAppIsRunning = new ArrayList<Metriclinks>();
        listLinksAppIsNotRunning = new ArrayList<Metriclinks>();

        listOutputFile = new ArrayList<Metriclinks>();

        listLinksForOutput = new ListLinks();
    }

    public void printTransievers(){
        for (Transceiver index: listTransceivers){
            System.out.println(index.getInterfaceName());
        }
    }

    public void Measure() throws JAXBException
    {
        measure();
        networkController.runRoutingMonitor();
        networkController.runNetworkMonitors();
        int aantalLinks = networkController.getPhysicalControlLinks().size();

        for(ControlLink link: networkController.getPhysicalControlLinks())
        {

            linkMonitor = link.getLinkMonitor();
            linkMonitor.measure();

            String linkName = link.getSourceInterface().getInterfaceName();
            long usedBandwith = link.getLinkMonitor().getUsedBandwidth();


            long totalAvailableBandwidth = linkMonitor.getTotalAvailableBandwidth();

            double latency = link.getLinkMonitor().getLatency();

            Metriclinks aLink = new Metriclinks(nameOfDevice, ID, isAppRunning, linkName, usedBandwith, totalAvailableBandwidth, latency);

            listLinks.add(aLink);
            lijstMetLinks.setListLinks(listLinks);

            if (isAppRunning)
            {
                listLinksAppIsRunning.add(aLink);
            }
            else {
                listLinksAppIsNotRunning.add(aLink);
            }
            ID++;

        }
        if (!listLinks.isEmpty())
        {
            marshall(lijstMetLinks);
        }
    }


    public void determineWorstCaseMetrics() throws JAXBException
    {

        for (ControlLink namesLinks: networkController.getPhysicalControlLinks())
        {
            String linkName = namesLinks.getSourceInterface().getInterfaceName();

            ArrayList<Metriclinks> listLinkBeforeAppRunned = new ArrayList<Metriclinks>();
            ArrayList<Metriclinks> listLinkWhenAppIsRunning = new ArrayList<Metriclinks>();

            for(Metriclinks index : listLinksAppIsNotRunning)
            {
                if(index.getLinkName().equals(namesLinks.getSourceInterface().getInterfaceName()))
                {
                    listLinkBeforeAppRunned.add(index);
                }
            }

            for (Metriclinks index: listLinksAppIsRunning)
            {
                if (index.getLinkName().equals(namesLinks.getSourceInterface().getInterfaceName()))
                {
                    listLinkWhenAppIsRunning.add(index);
                }
            }

            Metriclinks metricLowestValue = algorithmToGetValue(listLinkBeforeAppRunned, getlowest,metricoflinks.latency);
            Metriclinks metricHigestWhenAppRunned = algorithmToGetValue(listLinkWhenAppIsRunning, getHighest,metricoflinks.latency);

            Metriclinks metriclinksLowBW = algorithmToGetValue(listLinkBeforeAppRunned,getlowest,metricoflinks.bandwith);
            Metriclinks metricLinksHighBWapprun = algorithmToGetValue(listLinkWhenAppIsRunning,getHighest,metricoflinks.bandwith);

            long totalUsedBandwith = metricLinksHighBWapprun.getUsedBandwith() - metriclinksLowBW.getUsedBandwith();
            double totalLatencyConsumed = metricHigestWhenAppRunned.getLatency() - metricLowestValue.getLatency();


            listOutputFile.add(new Metriclinks(nameOfDevice, metricLinksHighBWapprun.ID_Of_LinkMeasurement,metricLinksHighBWapprun.isAppRunning,metricHigestWhenAppRunned.linkName,totalUsedBandwith,metricLowestValue.totalAvailableBandwidth,totalLatencyConsumed));
            listLinksForOutput.setListLinks(listOutputFile);

            System.out.println();
        }
        if (!listOutputFile.isEmpty())
        {
            marshallOutPutFile(listLinksForOutput);
        }

        convertXmlToJson(OutputMetricsLinks,OutputMetricsLinksJSON);
    }

    //public List<Metriclinks> getListOfNicsWithTheSameName(List<ControlLink> ){


    //}

    protected static Metriclinks algorithmToGetValue(List<Metriclinks> list, boolean isLowest, metricoflinks select)
    {

        Metriclinks obj = null;

        switch (select){
            case bandwith:
                TreeMap<Long,Metriclinks> treeMap = new TreeMap<Long, Metriclinks>();

                for (Metriclinks index: list){
                    treeMap.put(index.getUsedBandwith(),index);
                }
                if (!treeMap.isEmpty()){

                    if (isLowest){
                        Map.Entry<Long, Metriclinks> lowestMeasure = treeMap.firstEntry();
                        obj = new Metriclinks(lowestMeasure.getValue().nameOfDevice,lowestMeasure.getValue().ID_Of_LinkMeasurement,lowestMeasure.getValue().isAppRunning,lowestMeasure.getValue().linkName,lowestMeasure.getValue().getUsedBandwith(),lowestMeasure.getValue().totalAvailableBandwidth,lowestMeasure.getValue().latency);
                    }
                    else {
                        Map.Entry<Long, Metriclinks> key = treeMap.lastEntry();
                        obj = new Metriclinks(key.getValue().nameOfDevice, key.getValue().ID_Of_LinkMeasurement,key.getValue().isAppRunning,key.getValue().linkName,key.getValue().getUsedBandwith(),key.getValue().totalAvailableBandwidth,key.getValue().latency);
                    }
                }
                break;
            case latency:
                TreeMap<Double,Metriclinks> treeMapLatency = new TreeMap<Double, Metriclinks>();

                for (Metriclinks index: list){
                    treeMapLatency.put(index.getLatency(),index);
                }
                if (!treeMapLatency.isEmpty()){

                    if (isLowest){
                        Map.Entry<Double, Metriclinks> key = treeMapLatency.firstEntry();
                        obj = new Metriclinks(key.getValue().nameOfDevice,key.getValue().ID_Of_LinkMeasurement,key.getValue().isAppRunning,key.getValue().linkName,key.getValue().getUsedBandwith(),key.getValue().totalAvailableBandwidth,key.getValue().latency);

                    }
                    else {
                        Map.Entry<Double, Metriclinks> key = treeMapLatency.lastEntry();
                        obj = new Metriclinks(key.getValue().nameOfDevice,key.getValue().ID_Of_LinkMeasurement,key.getValue().isAppRunning,key.getValue().linkName,key.getValue().getUsedBandwith(),key.getValue().totalAvailableBandwidth,key.getValue().latency);
                    }
                }
        }
        return obj;
    }

    public List<Metriclinks> getListLinks()
    {
        return listLinks;
    }



    protected void marshall(ListLinks listLinks) throws JAXBException
    {
        /******************** Marshalling *****************************/
        JAXBContext jaxbContext = JAXBContext.newInstance(ListLinks.class, Metriclinks.class);

        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        //jaxbMarshaller.marshal(memoryObj, System.out);
        jaxbMarshaller.marshal(listLinks, new File(xmlFilePathLinks));
    }


    protected void marshallOutPutFile(ListLinks listLinks) throws JAXBException
    {
        /******************** Marshalling *****************************/
        JAXBContext jaxbContext = JAXBContext.newInstance(ListLinks.class, Metriclinks.class);

        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(listLinks, new File(OutputMetricsLinks));
    }

}
