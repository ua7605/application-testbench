package Application;

import Timer.TimerForMeasurements;
import monitor.MonitorMemory;
import monitor.MonitorProcessor;
import monitor.MonitorLinks;


/**
 * De Application klasse is de klasse waarin de application gaan worden gedraaid (application zullen draaien in docker).
 * Om de application te laten lopen moet de run methode worden opgeroepen en deze zal in
 */
public class Application extends Thread {

    public static boolean isAppRunning;
    public MonitorMemory monitorMemory;
    public MonitorProcessor monitorProcessor;
    public MonitorLinks monitorLinks;
    public int halve_second = 500;
    public int second = 1000;
    public int twosecond = 2000;
    public TimerForMeasurements timer;

    public Application(MonitorMemory monitorMemory, MonitorProcessor monitorProcessor, MonitorLinks monitorLinks) {
        isAppRunning = false;
        this.monitorMemory = monitorMemory;
        this.monitorProcessor = monitorProcessor;
        this.monitorLinks = monitorLinks;
        this.monitorMemory.setIsAppRunning(isAppRunning);
        this.monitorProcessor.setIsAppRunning(isAppRunning);
        this.monitorLinks.setIsAppRunning(isAppRunning);
        timer = new TimerForMeasurements(second, this.monitorLinks, this.monitorProcessor, this.monitorMemory);

    }

    public static boolean getIsAppRunning(){
        return isAppRunning;
    }

    //TODO: hierin moet de applicatie worden in opgestart. Om vervolgens hierin metingen te doen. De run method zal vanuit Mian worden opgeroepen!
    //TODO: Wanneer de applicatie wordt gestart moet dit ook gecommuniceerd worden met de monitors: OK
    @Override
    public void run() {
        super.run();
        isAppRunning = true; // Deze globale variable moet op true komen te staan wanneer effectief de applicatie aan het lopen is.

        letTestBechKnowThatisAppRunning(true);

        timer.Measure();

        // Hiermee worden de monitors op de hoogte gesteld dat de app gestart is. Dit is zeer belangerijk!

        // Dit zou normaal het nodige zijn om een docker te kunnen optstarten.
        //ProcessBuilder docker = new ProcessBuilder("docker","run",...NOG TE BEPALEN....)
        //docker.inheritIO();
        //docker.start(); // Dit zal starten in een thread waardoor het dit programma gewoon door zal lopen.




        // Op het einde wanneer de applicatie is gestopt dan laten we dit ook weten aan de testbech

        letTestBechKnowThatisAppRunning(false);

    }


    public void letTestBechKnowThatisAppRunning(Boolean isRunning){
        monitorMemory.setIsAppRunning(true);
        monitorProcessor.setIsAppRunning(true);
        monitorLinks.setIsAppRunning(true);
    }



}
