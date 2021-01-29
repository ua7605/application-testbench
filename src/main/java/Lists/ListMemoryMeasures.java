package Lists;

import Metrics.Metricmemory;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
@XmlRootElement(name="lists-Total-memory-Measures")
public class ListMemoryMeasures {
    List<Metricmemory> listMemoryMeasures;

    public ListMemoryMeasures()
    {
    }

    public List<Metricmemory> getListMemoryMeasures()
    {
        return listMemoryMeasures;
    }

    public void setListMemoryMeasures(List<Metricmemory> list)
    {
        listMemoryMeasures = list;
    }

}
