package Metrics;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Metrics-Memory")
@XmlAccessorType(XmlAccessType.FIELD)
public class Metricmemory {

    public long availableMemorySize;
    public long totalMemorySize;
    public long usedMemorySize;
    public long energyConsumption;
    public boolean isAppRunning;
    public int ID_Of_MemoryMeasurement;
    public String nameOfDevice;

    public Metricmemory(String nameOfDevice,int ID,boolean isAppRunning, long availableMemorySize, long totalMemorySize, long usedMemorySize, long energyConsumption)
    {
        this.nameOfDevice = nameOfDevice;
        this.ID_Of_MemoryMeasurement = ID;
        this.isAppRunning = isAppRunning;
        this.availableMemorySize = availableMemorySize;
        this.totalMemorySize = totalMemorySize;
        this.usedMemorySize = usedMemorySize;
        this.energyConsumption = energyConsumption;
    }

    private Metricmemory() throws JAXBException
    {
    }

    public int getID_Of_MemoryMeasurement()
    {
        return ID_Of_MemoryMeasurement;
    }
}
