package engine.execution.instance.enitty.manager;

import engine.action.impl.SecondaryEntityDefinition;
import engine.definition.entity.EntityDefinition;
import engine.execution.context.Context;
import engine.execution.instance.enitty.EntityInstance;
import engine.execution.instance.environment.api.ActiveEnvironment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface EntityInstanceManager {

    EntityInstance create(EntityDefinition entityDefinition);
    ArrayList<EntityInstance> getEntityInstances();

    ArrayList<EntityInstance> getInstancesByDefinition(EntityDefinition entityDefinition);
    EntityInstance getInstanceByName(String name);

    void killEntity(int id);
    ArrayList<EntityInstance> getSecondaryEntityInstances(SecondaryEntityDefinition secondaryEntityDefinition, ActiveEnvironment activeEnvironment);

    EntityDefinition getEntityDefinitionByName(String name);

    EntityInstance getEntityInstanceById(int id);

    HashMap<String, Integer> getEntityNameToEntityCount();

    HashMap<String, EntityDefinition> getEntityNameToEntityDefinition();
}
