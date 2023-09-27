package engine.simulation.manager;

import dto.impl.simulation.SimulationDTO;
import dto.impl.simulation.SimulationExecutionDetails;
import engine.definition.world.api.WorldDefinition;
import engine.simulation.runner.impl.SimulationRunner;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationExecutionManager {
    private final ExecutorService threadPool;
    HashMap<Integer, SimulationRunner> simulationIdToSimulationRunner;
    HashMap<Integer, SimulationExecutionDetails> simulationIdToSimulationExecutionDetails;
    private int queueSize;
    private static final int count=0;

    public SimulationExecutionManager(int numThreads) {
        queueSize = numThreads;
        this.threadPool = Executors.newFixedThreadPool(numThreads);
        this.simulationIdToSimulationRunner = new HashMap<Integer, SimulationRunner>();
        this.simulationIdToSimulationExecutionDetails = new HashMap<Integer, SimulationExecutionDetails>();
    }
    public SimulationDTO startNewSimulation(WorldDefinition worldDefinition, HashMap<String, String> envPropertyNameToEnvPropertyValue, HashMap<String, String> entityNameToEntityPopulation) {
        try {
            SimulationRunner simulationRunner = new SimulationRunner(worldDefinition, envPropertyNameToEnvPropertyValue, entityNameToEntityPopulation);
            int simulationId = simulationRunner.getSimulationExecutionDetails().getSimulationId();
            this.simulationIdToSimulationRunner.put(simulationId, simulationRunner);
            this.simulationIdToSimulationExecutionDetails.put(simulationId, simulationRunner.getSimulationExecutionDetails());
            this.threadPool.execute(simulationRunner);
            return new SimulationDTO(true, simulationRunner.getSimulationExecutionDetails());
        } catch (Exception e) {
            SimulationDTO simulationDTO = new SimulationDTO(false, new SimulationExecutionDetails());
            simulationDTO.setErrorMessage(e.getMessage());
            return simulationDTO;
        }
    }

    public HashMap<Integer, SimulationExecutionDetails> getSimulationIdToSimulationExecutionDetails() {
        return simulationIdToSimulationExecutionDetails;
    }

    public void pauseSimulation(Integer simulationId) {
        simulationIdToSimulationRunner.get(simulationId).pauseSimulation();
    }

    public void resumeSimulation(Integer simulationId) {
        simulationIdToSimulationRunner.get(simulationId).resumeSimulation();
    }

    public void terminateSimulation(Integer simulationId) {
        simulationIdToSimulationRunner.get(simulationId).terminateSimulation();
    }
}
