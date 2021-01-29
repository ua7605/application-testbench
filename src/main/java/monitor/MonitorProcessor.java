package monitor;


import Lists.ListProcessorMeasure;
import Metrics.Metricprocessor;
import be.uantwerpen.idlab.dust.coordinator.DeviceModels.Processor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MonitorProcessor extends Monitor {

    protected Processor processor;
    protected List<Metricprocessor> listProcessorMeasurements;
    protected ListProcessorMeasure listProcessorMeasure;


    protected List<Metricprocessor> listProcessorMeasurementsAppIsNotRunning;
    protected List<Metricprocessor> listProcessorMeasurementsAppIsRunning;


    public MonitorProcessor()
    {
        processor = hardwareMonitor.getProcessor();
        listProcessorMeasurements = new ArrayList<Metricprocessor>();
        listProcessorMeasure = new ListProcessorMeasure();

        listProcessorMeasurementsAppIsRunning = new ArrayList<Metricprocessor>();
        listProcessorMeasurementsAppIsNotRunning = new ArrayList<Metricprocessor>();
    }

    protected long getAvailableProcessingPower()
    {
        return processor.getAvailableProcessingPower();
    }

    protected int getcores()
    {
        return processor.getCores();
    }

    protected long getProcessorEnergyConsumption()
    {
        return processor.getProcessorEnergyConsumption();
    }

    protected long getTotalProcessingPower()
    {
        return processor.getTotalProcessingPower();
    }

    protected long getUsedProcessingPower()
    {
        return processor.getUsedProcessingPower();
    }

    public void Measure() throws JAXBException
    {
        measure();

        int coreCount = hardwareAbstractionLayer.getProcessor().getLogicalProcessorCount();
        int usedProcessingPower = (int) (hardwareAbstractionLayer.getProcessor().getSystemLoadAverage(1)[0] * 100);

        int totalProcessingPower = coreCount * 100;
        int availableProcessingPower = totalProcessingPower - usedProcessingPower;

        Metricprocessor metricprocessor = new Metricprocessor(nameOfDevice,ID, isAppRunning, availableProcessingPower, coreCount, getProcessorEnergyConsumption(), totalProcessingPower, usedProcessingPower);

        listProcessorMeasurements.add(metricprocessor);
        listProcessorMeasure.setMetricprocessorList(listProcessorMeasurements);

        if (!listProcessorMeasure.getMetricprocessorList().isEmpty())
        {
            marshall(listProcessorMeasure);
        }

        if (isAppRunning)
        {
            listProcessorMeasurementsAppIsRunning.add(metricprocessor);
        }
        else {
            listProcessorMeasurementsAppIsNotRunning.add(metricprocessor);
        }
        ID++;
    }


    public void determineWorstCaseMetrics() throws JAXBException
    {
        TreeMap<Long, Metricprocessor> treeMap = new TreeMap<Long, Metricprocessor>();
        TreeMap<Long, Metricprocessor> treeMapBeforeAppRunned = new TreeMap<Long, Metricprocessor>();
        long biggestValueForUsedProcessingPower = 0;
        long smallestValueForUsedProcessingPower = 0;
        long result_total_used_processing_power_of_the_application = 0;
        Metricprocessor obj = null;

        for (Metricprocessor index: listProcessorMeasurementsAppIsRunning)
        {
            treeMap.put(index.usedProcessingPower, index);
        }

        if (!treeMap.isEmpty())
        {
            Map.Entry<Long, Metricprocessor>  key = treeMap.lastEntry();
            biggestValueForUsedProcessingPower = key.getValue().usedProcessingPower;
            obj = key.getValue();

        }
        else {
            System.out.println("Er zijn geen meetingen gedaan wanneer de applicatie draaide!");
        }


        for (Metricprocessor index: listProcessorMeasurementsAppIsNotRunning)
        {
            treeMapBeforeAppRunned.put(index.usedProcessingPower, index);
        }

        if (!treeMapBeforeAppRunned.isEmpty())
        {
            Map.Entry<Long, Metricprocessor> key = treeMapBeforeAppRunned.firstEntry();
            smallestValueForUsedProcessingPower = key.getValue().usedProcessingPower;
        }
        else {
            System.out.println("Er werden geen meetingen gedaan voor dat de applicatie werd opgestart!");
        }

        result_total_used_processing_power_of_the_application = biggestValueForUsedProcessingPower - smallestValueForUsedProcessingPower;


        assert obj != null;
        marshalloutput(new Metricprocessor(obj.nameOfDevice,obj.ID_Of_ProcessorMeasurement,obj.isAppRunning,obj.availableProcessingPower,obj.totalCores,obj.processorEnergyConsumption,obj.totalProcessingPower,result_total_used_processing_power_of_the_application));

        convertXmlToJson(OutputMetricsProcessor,OutputMetricsProcessorJSON);
    }


    protected void marshall(ListProcessorMeasure listProcessorMeasure) throws JAXBException
    {
        /******************** Marshalling *****************************/
        JAXBContext jaxbContext = JAXBContext.newInstance(ListProcessorMeasure.class, Metricprocessor.class);

        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        //jaxbMarshaller.marshal(memoryObj, System.out);
        jaxbMarshaller.marshal(listProcessorMeasure, new File(xmlFilePathProcessor));
    }

    protected void marshalloutput(Metricprocessor metricprocessor) throws JAXBException
    {
        /******************** Marshalling *****************************/
        JAXBContext jaxbContext = JAXBContext.newInstance(Metricprocessor.class);

        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        //jaxbMarshaller.marshal(memoryObj, System.out);
        jaxbMarshaller.marshal(metricprocessor, new File(OutputMetricsProcessor));
    }
}
