package Output;

public class Output {

    // Links
    public String linkName;
    public long linkusedBandwith;
    public long linktotalAvailableBandwidth;
    public double linklatency;
    public boolean isAppRunning;
    public int ID_Of_LinkMeasurement;

    // Memory
    public long availableMemorySize;
    public long totalMemorySize;
    public long usedMemorySize;
    public long memoryenergyConsumption;
    public int ID_Of_MemoryMeasurement;

    // Processor
    public long availableProcessingPower;
    public int processortotalCores;
    public long processorEnergyConsumption;
    public long totalProcessingPower;
    public long usedProcessingPower;
    public int ID_Of_ProcessorMeasurement;


    public Output(String linkName, long linkusedBandwith, long linktotalAvailableBandwidth, double linklatency, boolean isAppRunning, int ID_Of_LinkMeasurement, long availableMemorySize, long totalMemorySize, long usedMemorySize, long memoryenergyConsumption, int ID_Of_MemoryMeasurement, long availableProcessingPower, int processortotalCores, long processorEnergyConsumption, long totalProcessingPower, long usedProcessingPower, int ID_Of_ProcessorMeasurement) {
        this.linkName = linkName;
        this.linkusedBandwith = linkusedBandwith;
        this.linktotalAvailableBandwidth = linktotalAvailableBandwidth;
        this.linklatency = linklatency;
        this.isAppRunning = isAppRunning;
        this.ID_Of_LinkMeasurement = ID_Of_LinkMeasurement;
        this.availableMemorySize = availableMemorySize;
        this.totalMemorySize = totalMemorySize;
        this.usedMemorySize = usedMemorySize;
        this.memoryenergyConsumption = memoryenergyConsumption;
        this.ID_Of_MemoryMeasurement = ID_Of_MemoryMeasurement;
        this.availableProcessingPower = availableProcessingPower;
        this.processortotalCores = processortotalCores;
        this.processorEnergyConsumption = processorEnergyConsumption;
        this.totalProcessingPower = totalProcessingPower;
        this.usedProcessingPower = usedProcessingPower;
        this.ID_Of_ProcessorMeasurement = ID_Of_ProcessorMeasurement;
    }








}
