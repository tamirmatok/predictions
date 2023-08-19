package engine.execution.instance.enitty;

import engine.definition.entity.EntityDefinition;
import engine.execution.instance.property.PropertyInstance;

import java.util.HashMap;
import java.util.Map;

public class EntityInstanceImpl implements EntityInstance {

    private final EntityDefinition entityDefinition;
    private final int id;
    private Map<String, PropertyInstance> properties;

    public EntityInstanceImpl(EntityDefinition entityDefinition, int id) {
        this.entityDefinition = entityDefinition;
        this.id = id;
        properties = new HashMap<>();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public PropertyInstance getPropertyByName(String name) {
        if (!properties.containsKey(name)) {
            return null;
        }

        return properties.get(name);
    }

    @Override
    public void addPropertyInstance(PropertyInstance propertyInstance) {
        properties.put(propertyInstance.getPropertyDefinition().getName(), propertyInstance);
    }

    @Override
    public EntityDefinition getEntityDefinition() {
        return this.entityDefinition;
    }

    public HashMap<String, PropertyInstance> getProperties() {
        return (HashMap<String, PropertyInstance>) properties;
    }
}
