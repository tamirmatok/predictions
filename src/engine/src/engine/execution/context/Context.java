package engine.execution.context;

import engine.execution.instance.enitty.EntityInstance;
import engine.execution.instance.enitty.manager.EntityInstanceManager;
import engine.execution.instance.environment.api.ActiveEnvironment;
import engine.execution.instance.property.PropertyInstance;

public interface Context {
    EntityInstance getPrimaryEntityInstance();
    void removeEntity(EntityInstance entityInstance);
    PropertyInstance getEnvironmentVariable(String name);
   EntityInstanceManager getEntityInstanceManager();

   ActiveEnvironment getActiveEnvironment();
}
