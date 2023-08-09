package engine.execution.instance.enitty.manager;

import engine.definition.entity.EntityDefinition;
import engine.execution.instance.enitty.EntityInstance;

import java.util.List;

public interface EntityInstanceManager {

    EntityInstance create(EntityDefinition entityDefinition);
    List<EntityInstance> getInstances();

    void killEntity(int id);
}
