package dto.impl.simulation;

import engine.definition.entity.EntityDefinition;
import engine.definition.property.api.PropertyDefinition;
import engine.definition.world.api.WorldDefinition;
import engine.execution.instance.enitty.EntityInstance;
import engine.execution.instance.enitty.manager.EntityInstanceManager;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.HashMap;

public class SimulationExecutionDetails {
    private static int counter = 1;
    private final int simulationId = counter++;
    private final ArrayList<EntityReport> entitiesReport;
    private final String timestamp;
    private String causeOfTermination;
    private String currentSecond;
    private int currentTick;
    String simulationStatus;
    double simulationPercents;

    public SimulationExecutionDetails() {
        this.timestamp = LocalDateTime.now().toString();
        this.entitiesReport = new ArrayList<>();
        this.simulationPercents = 0;
        this.simulationStatus = "Pending";
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


    public void setEntities(WorldDefinition worldDefinition) {
        for (EntityDefinition entityDefinition : worldDefinition.getEntityDefinitions().values()) {
            ArrayList<String> propertyNames = new ArrayList<>();
            for (PropertyDefinition propertyDefinition : entityDefinition.getProps()) {
                propertyNames.add(propertyDefinition.getName());
            }
            EntityReport entityReport = new EntityReport(entityDefinition.getName(), entityDefinition.getPopulation(), propertyNames);
            this.addEntityReport(entityReport);
        }
    }

    public void updateEntitiesReports(EntityInstanceManager entityInstanceManager) {
        for (EntityReport entityReport : this.entitiesReport) {
            EntityDefinition entityDefinition = entityInstanceManager.getEntityDefinitionByName(entityReport.getEntityName());
            ArrayList<String> propertyNames = new ArrayList<>();
            for (PropertyDefinition propertyDefinition : entityDefinition.getProps()) {
                propertyNames.add(propertyDefinition.getName());
            }
            entityReport.setPopulation(entityInstanceManager.getInstancesByDefinition(entityDefinition).size());
            entityReport.setTickPopulation();
            entityReport.resetPropertyValueCount();
            entityReport.setInitialCounts(propertyNames);
            for(EntityInstance entityinstance: entityInstanceManager.getInstancesByDefinition(entityDefinition)){
                if (entityinstance.getEntityDefinition().getName().equals(entityReport.getEntityName())){
                    for (PropertyDefinition propertyDefinition : entityDefinition.getProps()) {
                        String propertyName = propertyDefinition.getName();
                        String propertyValue = entityinstance.getPropertyByName(propertyName).getValue().toString();
                        entityReport.reportPropertyValue(propertyName, propertyValue);
                    }
                }
            }
        }
    }

    public String getCauseOfTermination() {
        return causeOfTermination;
    }

    public void setCauseOfTermination(String causeOfTermination) {
        this.causeOfTermination = causeOfTermination;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }

    public String getCurrentSecond() {
        return currentSecond;
    }

    public void setCurrentSecond(String currentSecond) {
        this.currentSecond = currentSecond;
    }

    public String getSimulationStatus() {
        return simulationStatus;
    }

    public Double getSimulationPercents() {
        return simulationPercents;
    }

    public void setSimulationPercents(double simulationPercents) {
        this.simulationPercents = simulationPercents;
    }

    public void setSimulationStatus(String simulationStatus) {
        this.simulationStatus = simulationStatus;
    }

    public void updateEntityReportTickPopulations(String entityName) {
        for (EntityReport entityReport : this.entitiesReport) {
            if (entityReport.getEntityName().equals(entityName)) {
                entityReport.setTickPopulation();
            }
        }
    }



}
