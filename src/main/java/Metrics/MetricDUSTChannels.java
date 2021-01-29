package Metrics;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Metrics-DUST-Channels")
@XmlAccessorType(XmlAccessType.FIELD)
public class MetricDUSTChannels
{

    public String channelName = "null";
    public int totalSizeOfAllMessagesSend = 0;
    public int totalMessagescounter = 0;
    public int messagesPerSecond = 0;
    public int loadPerSecond = 0;

    public MetricDUSTChannels() throws JAXBException
    {

    }

    public MetricDUSTChannels(String channelName, int totalSizeOfAllMessagesSend, int totalMessagescounter, int messagesPerSecond, int loadPerSecond) {
        this.channelName = channelName;
        this.totalSizeOfAllMessagesSend = totalSizeOfAllMessagesSend;
        this.totalMessagescounter = totalMessagescounter;
        this.messagesPerSecond = messagesPerSecond;
        this.loadPerSecond = loadPerSecond;
    }
}
