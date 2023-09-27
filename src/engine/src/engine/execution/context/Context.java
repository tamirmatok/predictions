package engine.execution.context;

import engine.execution.instance.enitty.EntityInstance;
import engine.execution.instance.enitty.manager.EntityInstanceManager;
import engine.execution.instance.environment.api.ActiveEnvironment;
import engine.execution.instance.grid.Grid;
import engine.execution.instance.property.PropertyInstance;

import java.util.ArrayList;

public interface Context {
   EntityInstance getPrimaryEntityInstance();
    void removeEntity(EntityInstance entityInstance);
    PropertyInstance getEnvironmentVariable(String name);
   EntityInstanceManager getEntityInstanceManager();

   ActiveEnvironment getActiveEnvironment();
   ArrayList<EntityInstance> getSecondaryEntityInstances();
   Grid getGrid();
   int getCurrentTick();

    void setCurrentTick(int tick);
    void setRootContexts (ArrayList<Context> rootContexts);
    ArrayList<Context> getRootContexts();
    EntityInstance getInstanceByName(String name);
}
