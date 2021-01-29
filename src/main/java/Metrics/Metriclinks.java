package Metrics;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Metrics-NICs")
@XmlAccessorType(XmlAccessType.FIELD)
public class Metriclinks {
    public String linkName;
    public long usedBandwith;
    public long totalAvailableBandwidth;
    public double latency;
    public boolean isAppRunning;
    public int ID_Of_LinkMeasurement;
    public String nameOfDevice;



    protected Metriclinks() throws JAXBException {

    }

    public Metriclinks(String nameOfDevice ,int ID, boolean isAppRunning, String linkName, long usedBandwith, long totalAvailableBandwidth, double latency)
    {
        this.nameOfDevice = nameOfDevice;
        ID_Of_LinkMeasurement = ID;
        this.isAppRunning = isAppRunning;
        this.linkName = linkName;
        this.usedBandwith = usedBandwith;
        this.totalAvailableBandwidth = totalAvailableBandwidth;
        this.latency = latency;
    }

    public String getLinkName()
    {
        return linkName;
    }

    public long getUsedBandwith()
    {
        return usedBandwith;
    }

    public long getTotalAvailableBandwidth()
    {
        return totalAvailableBandwidth;
    }

    public double getLatency()
    {
        return latency;
    }

    public void printLinkMetrics()
    {
        System.out.println("");
        System.out.println("Link stats of link: "+linkName);
        System.out.println("Total availableBandwith: "+totalAvailableBandwidth);
        System.out.println("Used bandwith: "+usedBandwith);
        System.out.println("Latency: "+latency);
    }
}
