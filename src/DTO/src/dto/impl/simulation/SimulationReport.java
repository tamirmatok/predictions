package dto.impl.simulation;

import engine.definition.entity.EntityDefinition;
import engine.definition.property.api.PropertyDefinition;
import engine.definition.world.api.WorldDefinition;
import engine.execution.instance.enitty.EntityInstance;
import engine.execution.instance.enitty.manager.EntityInstanceManager;

import java.util.ArrayList;
import java.time.LocalDateTime;

public class SimulationReport {
    private static int counter = 1;
    private final int simulationId = counter++;
    private final String timestamp;
    private String causeOfTermination;
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


    public void setInitialEntityReports(WorldDefinition worldDefinition){
        for (EntityDefinition entityDefinition : worldDefinition.getEntityDefinitions().values()) {
            ArrayList<String> propertyNames = new ArrayList<>();
            for (PropertyDefinition propertyDefinition: entityDefinition.getProps()){
                propertyNames.add(propertyDefinition.getName());
            }
            EntityReport entityReport = new EntityReport(entityDefinition.getName(), entityDefinition.getPopulation(), propertyNames);
            this.addEntityReport(entityReport);
        }
    }

    public void setFinalEntityReport(EntityInstanceManager entityInstanceManager, String causeOfTermination) {

        this.causeOfTermination = causeOfTermination;
        for (EntityInstance entityInstance: entityInstanceManager.getEntityInstances()) {
            EntityDefinition entityDefinition = entityInstance.getEntityDefinition();
            for (EntityReport entityReport: this.entitiesReport){
                if (entityReport.getEntityName().equals(entityDefinition.getName())){
                    entityReport.setFinalPopulation(entityDefinition.getPopulation());
                }
                for (PropertyDefinition propertyDefinition: entityDefinition.getProps()){
                    String propertyName = propertyDefinition.getName();
                    String propertyValue = entityInstance.getPropertyByName(propertyName).getValue().toString();
                    entityReport.reportPropertyValue(propertyName, propertyValue);
                }
            }
        }
    }


    public String getCauseOfTermination(){
        return causeOfTermination;
    }


}
