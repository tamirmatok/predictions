package engine.simulation.runner.impl;

import dto.impl.simulation.SimulationExecutionDetails;
import engine.execution.instance.termination.impl.Termination;

public class SimulationController {

    private long startTime;
    private long pausedTime;
    private int countTicks;
    private boolean paused;
    private boolean terminated;
    private String simulationStatus = "Pending";
    private SimulationExecutionDetails simulationExecutionDetails;


    public SimulationController(SimulationExecutionDetails simulationExecutionDetails) {
        this.startTime = 0;
        this.pausedTime = 0;
        this.countTicks = 0;
        this.paused = false;
        this.terminated = false;
        this.simulationExecutionDetails = simulationExecutionDetails;
    }

    public synchronized void terminateSimulation(boolean byUser) {
        simulationExecutionDetails.setSimulationStatus("Terminated");
        if (byUser) {
            simulationExecutionDetails.setCauseOfTermination("By user");
        }
        terminated = true;
    }

    public synchronized void pauseSimulation() {
        simulationExecutionDetails.setSimulationStatus("Paused");
        pausedTime = System.currentTimeMillis();
        paused = true;
    }

    public String getSimulationStatus() {
        return simulationStatus;
    }

    public synchronized void resumeSimulation() {
        simulationExecutionDetails.setSimulationStatus("Running");
        paused = false;
        startTime += System.currentTimeMillis() - pausedTime;
        notify(); // Notify waiting threads
    }

    public synchronized void startSimulation(){
        simulationStatus = "Running";
        simulationExecutionDetails.setSimulationStatus("Running");
        startTime = System.currentTimeMillis();
    }

    public synchronized void waitForResume() throws InterruptedException {
        while (paused) {
            simulationExecutionDetails.setSimulationStatus("Paused");
            wait(); // Wait until the simulation is resumed
        }

    }
    public void increaseTick() {
        this.countTicks++;
    }
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getCurrentTick() {
        return countTicks;
    }
    public long getSecondsCollapsed() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }
    public boolean isFinish(Termination termination) {
        if (!termination.isByUser() && !terminated) {
            long currentTime = System.currentTimeMillis();
            long elapsedMilliseconds = currentTime - startTime;
            int elapsedSeconds = (int) (elapsedMilliseconds / 1000);
            if (countTicks > termination.getByTicks()) {
                terminated = true;
                simulationExecutionDetails.setCauseOfTermination("Tick count exceeded " + termination.getByTicks() + " ticks");
            }
            else if (elapsedSeconds > termination.getBySeconds()) {
                terminated = true;
                simulationExecutionDetails.setCauseOfTermination("Time exceeded " + termination.getBySeconds() + " seconds");
            }
        }
        return terminated;
    }
    public double getSimulationPercents(Termination termination) {
        if (!termination.isByUser()) {
            long currentTime = System.currentTimeMillis();
            long elapsedMilliseconds = currentTime - startTime;
            int elapsedSeconds = (int) (elapsedMilliseconds / 1000);
            return (Math.max(((double) countTicks) / termination.getByTicks(), ((double) elapsedSeconds) / termination.getBySeconds()))*100;
        }
        else {
            if(terminated) {
                return 100;
            }
            else {
                return 0;
            }
        }

    }
}
