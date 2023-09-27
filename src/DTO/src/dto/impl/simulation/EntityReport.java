package dto.impl.simulation;

import java.util.ArrayList;

public class EntityReport {
    String entityName;
    int population;
    ArrayList<Integer> tickPopulationCounts;
    ArrayList<PropertyValueCount> propertyValueCounts;

    public EntityReport(String entityName, int population, ArrayList<String> propertyNames) {
        this.entityName = entityName;
        this.population = population;
        this.propertyValueCounts = new ArrayList<PropertyValueCount>();
        this.tickPopulationCounts = new ArrayList<Integer>();
        this.setInitialCounts(propertyNames);
    }

    public void setInitialCounts(ArrayList<String> propertyNames) {
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
    public ArrayList<PropertyValueCount> getPropertyValueCounts() {
        return propertyValueCounts;
    }
    public void setPopulation(int population) {
        this.population = population;
    }
    public int getPopulation() {
        return population;
    }
    public void setTickPopulation() {
        this.tickPopulationCounts.add(population);
    }
    public ArrayList<Integer> getTickPopulationCounts() {
        return tickPopulationCounts;
    }

    public void resetPropertyValueCount(){
        this.propertyValueCounts = new ArrayList<PropertyValueCount>();
    }
}
