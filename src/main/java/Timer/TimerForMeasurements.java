package Timer;

import monitor.MonitorLinks;
import monitor.MonitorMemory;
import monitor.MonitorProcessor;

import javax.xml.bind.JAXBException;
import java.util.Timer;
import java.util.TimerTask;

public class TimerForMeasurements {

    protected int measure_every;
    protected Timer timer;
    protected MonitorLinks monitorLinks;
    protected MonitorProcessor monitorProcessor;
    protected MonitorMemory monitorMemory;



    public TimerForMeasurements(int measure_every, MonitorLinks monitorLinks, MonitorProcessor monitorProcessor, MonitorMemory monitorMemory)
    {
        this.monitorLinks = monitorLinks;
        this.monitorProcessor = monitorProcessor;
        this.monitorMemory = monitorMemory;
        this.measure_every = measure_every;
        timer = new Timer();
    }

    public void Measure()
    {

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    monitorLinks.Measure();
                } catch (JAXBException e) {
                    e.printStackTrace();
                }
                try {
                    monitorMemory.Measure();
                } catch (JAXBException e) {
                    e.printStackTrace();
                }
                try {
                    monitorProcessor.Measure();
                } catch (JAXBException e) {
                    e.printStackTrace();
                }
            }
        },0,measure_every);
    }

    public void stop()
    {
        timer.cancel();
        timer.purge();
        //System.exit(0);
    }

}
