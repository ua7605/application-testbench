package Lists;

import Metrics.Metricprocessor;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
@XmlRootElement(name="lists-Total-Processor-Measures")
public class ListProcessorMeasure {

    List<Metricprocessor> metricprocessorList;

    public ListProcessorMeasure()
    {
    }

    public List<Metricprocessor> getMetricprocessorList()
    {
        return metricprocessorList;
    }

    public void setMetricprocessorList(List<Metricprocessor> metricprocessorList)
    {
        this.metricprocessorList = metricprocessorList;
    }
}
