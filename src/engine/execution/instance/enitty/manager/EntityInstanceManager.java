package engine.execution.instance.enitty.manager;

import engine.definition.entity.EntityDefinition;
import engine.execution.instance.enitty.EntityInstance;

import java.util.List;

public interface EntityInstanceManager {

    EntityInstance create(EntityDefinition entityDefinition);
    List<EntityInstance> getInstances();

    List<EntityInstance> getInstancesByDefinition(EntityDefinition entityDefinition);
    EntityInstance getInstanceByName(String name);

    void killEntity(int id);
}
