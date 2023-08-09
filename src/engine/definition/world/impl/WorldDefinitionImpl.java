package engine.definition.world.impl;

import engine.definition.world.api.WorldDefinition;
import engine.execution.instance.enitty.manager.EntityInstanceManager;
import engine.execution.instance.enitty.manager.EntityInstanceManagerImpl;

public class WorldDefinitionImpl implements WorldDefinition {
    EntityInstanceManager entityInstanceManager;

    public WorldDefinitionImpl() {
        this.entityInstanceManager = new EntityInstanceManagerImpl();
    }

    @Override
    public EntityInstanceManager getEntityInstanceManager() {

        return this.entityInstanceManager;
    }
}
