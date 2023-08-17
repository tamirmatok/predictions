package dto.impl.simulation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Simulation {
    private static int counter = 0;

    private final int simulationId = counter++;
    private final String timestamp;
    private final ArrayList<String> operations;
    private final ArrayList<EntityReport> entityReports;

    public Simulation() {
        this.timestamp = LocalDateTime.now().toString();
        this.operations = new ArrayList<String>();
        this.entityReports = new ArrayList<EntityReport>();
    }

    public int getSimulationId() {
        return simulationId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public ArrayList<String> getOperations() {
        return operations;
    }

    public ArrayList<EntityReport> getEntityReports() {
        return entityReports;
    }

    public void addOperation(String operation) {
        operations.add(operation);
    }

    public void addEntityReport(EntityReport entityReport) {
        entityReports.add(entityReport);
    }
}
