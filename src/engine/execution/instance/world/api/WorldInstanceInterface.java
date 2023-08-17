package engine.execution.instance.world.api;

import dto.impl.MessageDTO;
import dto.impl.simulation.SimulationDTO;
import dto.impl.simulation.SimulationReport;
import engine.definition.environment.api.EnvVariablesManager;
import engine.execution.instance.enitty.manager.EntityInstanceManager;
import engine.rule.Rule;
import engine.schema.generated.PRDWorld;

import java.util.ArrayList;

public interface WorldInstanceInterface {
    void stop();

    MessageDTO runSimulation();

    void setInitialSimulationReport(SimulationReport simulation);
}
