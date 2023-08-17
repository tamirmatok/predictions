package dto.impl.simulation;

import java.util.ArrayList;

public class EntityReport {
    String entityName;
    int initialPopulation;
    int finalPopulation;
    ArrayList<String> propertyNames;

    public EntityReport(String entityName, int initialPopulation, int finalPopulation, ArrayList<String> propertyNames) {
        this.entityName = entityName;
        this.initialPopulation = initialPopulation;
        this.finalPopulation = finalPopulation;
        this.propertyNames = propertyNames;
    }

    public String getEntityName() {
        return entityName;
    }

    public int getInitialPopulation() {
        return initialPopulation;
    }

    public int getFinalPopulation() {
        return finalPopulation;
    }

    public ArrayList<String> getPropertyNames() {
        return propertyNames;
    }

}
