package Lists;

import Metrics.Metriclinks;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
@XmlRootElement(name="list-Total")
public class ListLinks {
    List<Metriclinks> listLinks;

    public ListLinks()
    {
    }

    public List<Metriclinks>getListLinks()
    {
        return listLinks;
    }

    public void setListLinks(List<Metriclinks> list)
    {
        listLinks = list;
    }
}
