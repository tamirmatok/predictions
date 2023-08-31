package engine.simulator.impl;

import dto.impl.simulation.SimulationReport;
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
import engine.simulator.api.SimulatorInterface;

import java.util.ArrayList;

public class Simulator implements SimulatorInterface {

    private int countTick;
    private long startTime;
    private final WorldInstance worldInstance;

    public Simulator(WorldInstance worldInstance) {
        this.countTick = 0;
        this.worldInstance = worldInstance;
        this.startTime = 0;
    }

    @Override
    public SimulationReport runSimulation() {
        this.startTime = System.currentTimeMillis();
        WorldDefinition worldDefinition = worldInstance.getWorldDefinition();
        EntityInstanceManager entityInstanceManager = worldInstance.getEntityInstanceManager();
        ActiveEnvironment activeEnvironment = worldInstance.getActiveEnvironment();
        SimulationReport simulationReport = new SimulationReport();
        simulationReport.setInitialEntityReports(worldDefinition);
        resetTick();
        do {
            ArrayList<Rule> rulesOnRun = new ArrayList<Rule>();
            ArrayList<Action> actionsOnRun = new ArrayList<Action>();

            // Phase 1: Process rules
            for (Rule rule : worldInstance.getRules()) {
                if (rule.getActivation().isActive(countTick)) {
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
                    Context context = new ContextImpl(entity, entityInstanceManager, activeEnvironment);
                    action.invoke(context);
                }
            }
            increaseTick();
        } while (isFinish(worldInstance.getTermination()) == null);

        String terminationCause = isFinish(worldInstance.getTermination());
        simulationReport.setFinalEntityReport(entityInstanceManager, terminationCause);
        return simulationReport;
    }


    private String isFinish(Termination termination) {
        long currentTime = System.currentTimeMillis();
        long elapsedMilliseconds = currentTime - startTime;
        int elapsedSeconds = (int) (elapsedMilliseconds / 1000);
        if (countTick > termination.getByTicks()) {
            return ("Termination Condition by ticks = " + termination.getByTicks() + " reach to end.");
        } else if ( elapsedSeconds >= termination.getBySeconds()) {
            return ("Termination Condition by seconds = " + termination.getBySeconds() + " reach to end.");
        }
        return null;
    }


    private void resetTick() {
        this.countTick = 1;
    }

    private void increaseTick() {
        this.countTick++;
    }


}
