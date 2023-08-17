package dto.impl.simulation;

import engine.execution.instance.enitty.manager.EntityInstanceManager;
import engine.execution.instance.world.impl.WorldInstance;

import java.util.ArrayList;
import java.time.LocalDateTime;

public class SimulationReport {
    private static int counter = 0;
    private final int simulationId = counter++;
    private final String timestamp;

    private final ArrayList<EntityReport> entitiesReport;

    public SimulationReport() {
        this.timestamp = LocalDateTime.now().toString();
        this.entitiesReport = new ArrayList<EntityReport>();
    }

    public int getSimulationId() {
        return simulationId;
    }

    public String getTimestamp() {
        return timestamp;
    }


    public ArrayList<EntityReport> getEntityReports() {
        return entitiesReport;
    }


    public void addEntityReport(EntityReport entityReport) {
        entitiesReport.add(entityReport);
    }

}
