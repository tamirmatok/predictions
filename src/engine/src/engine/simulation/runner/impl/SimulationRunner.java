package engine.simulation.runner.impl;

import dto.impl.simulation.SimulationExecutionDetails;
import engine.action.api.Action;
import engine.definition.entity.EntityDefinition;
import engine.definition.world.api.WorldDefinition;
import engine.execution.context.Context;
import engine.execution.context.ContextImpl;
import engine.execution.instance.enitty.EntityInstance;
import engine.execution.instance.enitty.manager.EntityInstanceManager;
import engine.execution.instance.environment.api.ActiveEnvironment;
import engine.execution.instance.termination.impl.Termination;
import engine.execution.instance.world.impl.WorldInstance;
import engine.rule.Rule;

import java.util.ArrayList;
import java.util.HashMap;

public class SimulationRunner implements Runnable {

    private final WorldInstance worldInstance;
    HashMap<String, String> envPropertyNameToEnvPropertyValue = new HashMap<>();
    HashMap<String, String> entityNameToEntityPopulation = new HashMap<>();
    private final SimulationExecutionDetails simulationExecutionDetails;
    private final SimulationController simulationController;


    public SimulationRunner(WorldDefinition worldDefinition, HashMap<String, String> envPropertyNameToEnvPropertyValue, HashMap<String, String> entityNameToEntityPopulation) {
        this.simulationExecutionDetails = new SimulationExecutionDetails();
        this.simulationController = new SimulationController(simulationExecutionDetails);
        this.worldInstance = new WorldInstance(worldDefinition);
        this.validateEntityPopulation(entityNameToEntityPopulation);
        this.entityNameToEntityPopulation = entityNameToEntityPopulation;
        this.envPropertyNameToEnvPropertyValue = envPropertyNameToEnvPropertyValue;
        worldInstance.setEnvProperties(envPropertyNameToEnvPropertyValue);
        worldInstance.setEntityInstances(entityNameToEntityPopulation);
        this.simulationExecutionDetails.setEntities(worldDefinition);
    }

    private void validateEntityPopulation(HashMap<String, String> entityNameToEntityPopulation) {
        int gridSize = worldInstance.getGrid().getNumRows() * worldInstance.getGrid().getNumCols();
        int totalPopulation = 0;
        for (String entityName : entityNameToEntityPopulation.keySet()) {
            try {
                int entityPopulation = Integer.parseInt(entityNameToEntityPopulation.get(entityName));
                totalPopulation += entityPopulation;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Population of entity " + entityName + " is not a valid number");
            }
        }
        if (totalPopulation > gridSize) {
            throw new IllegalArgumentException("Total population of entities - " + totalPopulation + " is greater than the grid size - " + gridSize + "");
        }
    }

    @Override
    public void run() {
        simulationController.setStartTime(System.currentTimeMillis());
        simulationController.startSimulation();
        EntityInstanceManager entityInstanceManager = worldInstance.getEntityInstanceManager();
        ActiveEnvironment activeEnvironment = worldInstance.getActiveEnvironment();
        try {
            do {
                // wait in case of simulation paused set to true
                try {
                    simulationController.waitForResume();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupted status
                    break; // Exit the thread if interrupted
                }

                try {
                    Thread.sleep(120);
                } catch (InterruptedException e) {
                    // Handle the InterruptedException if needed
                    e.printStackTrace();
                }
                this.updateSimulationExecutionDetails();
                ArrayList<Rule> rulesOnRun = new ArrayList<Rule>();
                ArrayList<Action> actionsOnRun = new ArrayList<Action>();

                // move entities
                worldInstance.getGrid().moveEntities();

                // Phase 1: Process rules
                for (Rule rule : worldInstance.getRules()) {
                    if (rule.getActivation().isActive(simulationController.getCurrentTick())) {
                        rulesOnRun.add(rule);
                    }
                }
                // Phase 2: Process actions within rules
                for (Rule rule : rulesOnRun) {
                    actionsOnRun.addAll(rule.getActionsToPerform());
                }

                // Phase 3: Invoke actions on context entities
                for (Action action : actionsOnRun) {
                    EntityDefinition contextEntity = action.getMainContextEntity();
                    for (EntityInstance entity : entityInstanceManager.getInstancesByDefinition(contextEntity)) {
                        ArrayList<EntityInstance> secondaryEntityInstances = null;
                        if (action.hasSecondaryEntity()) {
                            secondaryEntityInstances = entityInstanceManager.getSecondaryEntityInstances(action.getSecondaryEntityDefinition(), activeEnvironment);
                        }
                        Context context = new ContextImpl(entity, secondaryEntityInstances, entityInstanceManager, activeEnvironment, worldInstance.getGrid());
                        context.setCurrentTick(simulationController.getCurrentTick());
                        action.invoke(context);
                    }
                }
                simulationController.increaseTick();
            } while (!simulationController.isFinish(worldInstance.getTermination()));

            this.simulationController.terminateSimulation(false);

        } catch (Exception e) {
            simulationExecutionDetails.setSimulationStatus("Terminated");
            simulationExecutionDetails.setCauseOfTermination("Engine Failure - " + e.getMessage());
        }
    }

    private void updateSimulationExecutionDetails() {
        this.simulationExecutionDetails.setCurrentTick(simulationController.getCurrentTick());
        this.simulationExecutionDetails.setCurrentSecond(Long.toString(simulationController.getSecondsCollapsed()));
        this.simulationExecutionDetails.updateEntitiesReports(worldInstance.getEntityInstanceManager());
        this.simulationExecutionDetails.setSimulationStatus(simulationController.getSimulationStatus());
        this.simulationExecutionDetails.setSimulationPercents(simulationController.getSimulationPercents(worldInstance.getTermination()));
    }

    public SimulationExecutionDetails getSimulationExecutionDetails() {
        return simulationExecutionDetails;
    }
    public void pauseSimulation() {
        this.simulationController.pauseSimulation();
    }

    public void resumeSimulation() {
        this.simulationController.resumeSimulation();
    }

    public void terminateSimulation() {
       simulationController.terminateSimulation(true);
    }
}
