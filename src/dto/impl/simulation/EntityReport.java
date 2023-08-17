package dto.impl.simulation;

import java.util.ArrayList;
import java.util.HashMap;

public class EntityReport {
    String entityName;
    int initialPopulation;
    int finalPopulation;
    HashMap<String,String> propertyNameToPropertyValue;

    public EntityReport(String entityName, int initialPopulation) {
        this.entityName = entityName;
        this.initialPopulation = initialPopulation;
        this.propertyNameToPropertyValue = new HashMap<String,String>();
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

    public void setFinalPopulation(int finalPopulation) {
        this.finalPopulation = finalPopulation;
    }

    public HashMap<String,String> getPropertyNameToPropertyValue() {
        return propertyNameToPropertyValue;
    }

    public void addProperty(String propertyName, String propertyValue) {
        propertyNameToPropertyValue.put(propertyName, propertyValue);
    }

}
