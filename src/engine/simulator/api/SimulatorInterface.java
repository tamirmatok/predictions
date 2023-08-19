package engine.simulator.api;

import dto.impl.simulation.SimulationReport;
import engine.execution.instance.world.impl.WorldInstance;

public interface SimulatorInterface {
    SimulationReport runSimulation();
}
