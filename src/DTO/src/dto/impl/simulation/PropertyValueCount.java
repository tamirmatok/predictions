package dto.impl.simulation;

import java.util.HashMap;

public class PropertyValueCount {

    String propertyName;
    HashMap<String, Integer> propertyValueToCount;

    public PropertyValueCount(String propertyName){
        this.propertyName = propertyName;
        this.propertyValueToCount = new HashMap<String, Integer>();
    }

    public void addValue(String value){
        propertyValueToCount.merge(value, 1, Integer::sum);
    }

    public HashMap<String, Integer> getPropertyValueCount(){
        return propertyValueToCount;
    }

    public String getPropertyName(){
        return this.propertyName;
    }
}
