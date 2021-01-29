package monitor;

import Lists.ListMemoryMeasures;
import Metrics.Metricmemory;
import be.uantwerpen.idlab.dust.coordinator.DeviceModels.Memory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


//TODO: Voeg de mogelijkheid toe om de metingen te onderscheiden wanneer een applicatie aan het lopen is en wanneer niet.

public class MonitorMemory extends Monitor
{

    protected Memory monitorMemory;
    protected List<Metricmemory> listMemoryMeasures;

    protected List<Metricmemory> listMemoryMeasuresAppNotRunning;
    protected List<Metricmemory> listMemoryMeasuresAppIsRunning;

    protected ListMemoryMeasures listMeasure;

    public MonitorMemory()
    {

        monitorMemory = hardwareMonitor.getMemory();
        hardwareMonitor.getTransceivers();
        listMemoryMeasures = new ArrayList<Metricmemory>();
        listMeasure = new ListMemoryMeasures();

        listMemoryMeasuresAppNotRunning = new ArrayList<Metricmemory>();
        listMemoryMeasuresAppIsRunning = new ArrayList<Metricmemory>();

    }
    protected Memory getMemoryMonitor()
    {
        return monitorMemory;
    }

    protected long getAvailableMemorySize()
    {
        return monitorMemory.getAvailableMemorySize();
    }

    protected long getTotalMemorySize()
    {
        return monitorMemory.getTotalMemorySize();
    }

    protected long getUsedMemorySize()
    {
        return monitorMemory.getUsedMemorySize();
    }

    protected long getEnergyConsumption()
    {
        return monitorMemory.getEnergyConsumption();
    }

    public void Measure() throws JAXBException
    {
        hardwareMonitor.measure();
        long availableMem = hardwareAbstractionLayer.getMemory().getAvailable();
        long totalmemory = hardwareAbstractionLayer.getMemory().getTotal();
        long usedMemory = totalmemory - availableMem;
        Metricmemory metric = new Metricmemory(nameOfDevice,ID, isAppRunning, availableMem,totalmemory,usedMemory,getEnergyConsumption());
        listMemoryMeasures.add(metric);
        listMeasure.setListMemoryMeasures(listMemoryMeasures);

        if (!listMeasure.getListMemoryMeasures().isEmpty())
        {
            marshall(listMeasure);
        }

        if(getIsAppRunning())
        {
            listMemoryMeasuresAppIsRunning.add(metric);
        }
        else {
            listMemoryMeasuresAppNotRunning.add(metric);
        }
        ID++; // Nodig zodat we de metingen kunnen bijhouden.

    }

    public void determineWorstCaseMetrics()
    {
        TreeMap<Long, Metricmemory > treeMap = new TreeMap<Long, Metricmemory>();
        TreeMap<Long, Metricmemory> treeMapBeforeAppRunned = new TreeMap<Long, Metricmemory>();
        long groosteWaardeVoorUsedMemorySize = 0;
        long kleinsteWaardeVoorUsedMemorySize = 0;
        long result_total_used_memory_size_of_the_application = 0;
        Metricmemory obj = null;

        for (Metricmemory index: listMemoryMeasuresAppIsRunning)
        {
            treeMap.put(index.usedMemorySize, index);
        }

        if (!treeMap.isEmpty())
        {
            Map.Entry<Long, Metricmemory>  key = treeMap.lastEntry();
            groosteWaardeVoorUsedMemorySize = key.getValue().usedMemorySize;
            obj = key.getValue();

        }
        else {
            System.out.println("Er zijn geen meetingen gedaan wanneer de applicatie draaide!");
        }


        for (Metricmemory index: listMemoryMeasuresAppNotRunning)
        {
            treeMapBeforeAppRunned.put(index.usedMemorySize, index);
        }

        if (!treeMapBeforeAppRunned.isEmpty())
        {
            Map.Entry<Long, Metricmemory> key = treeMapBeforeAppRunned.firstEntry();
            kleinsteWaardeVoorUsedMemorySize = key.getValue().usedMemorySize;
        }
        else {
            System.out.println("Er werden geen meetingen gedaan voor dat de applicatie werd opgestart!");
        }

        result_total_used_memory_size_of_the_application = groosteWaardeVoorUsedMemorySize - kleinsteWaardeVoorUsedMemorySize;


        assert obj != null;
        try {
            marshallOutputFile(new Metricmemory(obj.nameOfDevice,obj.ID_Of_MemoryMeasurement, obj.isAppRunning, obj.availableMemorySize, obj.totalMemorySize, result_total_used_memory_size_of_the_application, obj.energyConsumption));
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        convertXmlToJson(OutputMetricsMemory,OutputMetricsMemoryJSON);
    }

    protected void marshall(ListMemoryMeasures listMeasure) throws JAXBException
    {
        /******************** Marshalling *****************************/
        JAXBContext jaxbContext = JAXBContext.newInstance(ListMemoryMeasures.class, Metricmemory.class);

        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        //jaxbMarshaller.marshal(memoryObj, System.out);
        jaxbMarshaller.marshal(listMeasure, new File(xmlFilePathMemory));
    }

    protected void marshallOutputFile(Metricmemory metricmemory) throws JAXBException
    {
        /******************** Marshalling *****************************/
        JAXBContext jaxbContext = JAXBContext.newInstance(Metricmemory.class);

        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        //jaxbMarshaller.marshal(memoryObj, System.out);
        jaxbMarshaller.marshal(metricmemory, new File(OutputMetricsMemory));
    }

}
