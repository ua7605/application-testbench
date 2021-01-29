package Lists;

import Metrics.MetricDUSTChannels;
import Metrics.Metriclinks;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="ALL-DUST-Channels")
public class ListDUSTChannels
{
    List<MetricDUSTChannels> listDUSTLinks;

    public ListDUSTChannels() {
    }

    public List<MetricDUSTChannels> getListDUSTLinks()
    {
        return listDUSTLinks;
    }

    public void setListDUSTLinks(List<MetricDUSTChannels> list)
    {
        listDUSTLinks = list;
    }
}
