package engine.execution.instance.enitty;

import engine.definition.entity.EntityDefinition;
import engine.execution.instance.property.PropertyInstance;

import java.util.HashMap;

public interface EntityInstance {
    int getId();
    PropertyInstance getPropertyByName(String name);
    void addPropertyInstance(PropertyInstance propertyInstance);

    EntityDefinition getEntityDefinition();
    HashMap<String, PropertyInstance> getProperties();
}
