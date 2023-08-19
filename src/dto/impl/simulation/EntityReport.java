package dto.impl.simulation;

import java.util.ArrayList;
import java.util.HashMap;

public class EntityReport {
    String entityName;
    int initialPopulation;
    int finalPopulation;
    ArrayList<PropertyValueCount> propertyValueCounts;

    public EntityReport(String entityName, int initialPopulation, ArrayList<String> propertyNames) {
        this.entityName = entityName;
        this.initialPopulation = initialPopulation;
        this.propertyValueCounts = new ArrayList<PropertyValueCount>();
        this.setCounts(propertyNames);
    }



    private void setCounts(ArrayList<String> propertyNames) {
        for (String propertyName: propertyNames){
            PropertyValueCount propertyValueCount = new PropertyValueCount(propertyName);
            propertyValueCounts.add(propertyValueCount);
        }
    }

    public void reportPropertyValue(String propertyName, String value){
        for (PropertyValueCount propertyValueCount: propertyValueCounts){
            if (propertyValueCount.getPropertyName().equals(propertyName)){
                propertyValueCount.addValue(value);
            }
        }
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

    public ArrayList<PropertyValueCount> getPropertyValueCounts() {
        return propertyValueCounts;
    }


}
