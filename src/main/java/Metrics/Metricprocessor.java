package Metrics;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Metrics-Processor")
@XmlAccessorType(XmlAccessType.FIELD)
public class Metricprocessor
{

    public long availableProcessingPower;
    public int totalCores;
    public long processorEnergyConsumption;
    public long totalProcessingPower;
    public long usedProcessingPower;
    public boolean isAppRunning;
    public int ID_Of_ProcessorMeasurement;
    public String nameOfDevice;

    public Metricprocessor(String nameOfDevice ,int ID, boolean isAppRunning, long availableProcessingPower, int totalCores, long processorEnergyConsumption, long totalProcessingPower, long usedProcessingPower)
    {
        this.nameOfDevice = nameOfDevice;
        this.isAppRunning = isAppRunning;
        ID_Of_ProcessorMeasurement = ID;
        this.availableProcessingPower = availableProcessingPower;
        this.totalCores = totalCores;
        this.processorEnergyConsumption = processorEnergyConsumption;
        this.totalProcessingPower = totalProcessingPower;
        this.usedProcessingPower = usedProcessingPower;
    }

    protected Metricprocessor() throws JAXBException
    {
    }
}
