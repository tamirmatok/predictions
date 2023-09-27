package engine.execution.context;

import engine.execution.instance.enitty.EntityInstance;
import engine.execution.instance.enitty.manager.EntityInstanceManager;
import engine.execution.instance.environment.api.ActiveEnvironment;
import engine.execution.instance.grid.Grid;
import engine.execution.instance.property.PropertyInstance;

import java.util.ArrayList;

public class ContextImpl implements Context {
    private final EntityInstance primaryEntityInstance;
    private final ArrayList<EntityInstance> secondaryEntityInstances;
    private final EntityInstanceManager entityInstanceManager;
    private final ActiveEnvironment activeEnvironment;
    private final Grid grid;
    private ArrayList<Context> rootContexts = new ArrayList<>();
    private int currentTick = 0;
    public ContextImpl(EntityInstance primaryEntityInstances, ArrayList<EntityInstance> secondaryEntityInstances, EntityInstanceManager entityInstanceManager, ActiveEnvironment activeEnvironment, Grid grid){
        rootContexts.add(this);
        this.primaryEntityInstance = primaryEntityInstances;
        this.entityInstanceManager = entityInstanceManager;
        this.activeEnvironment = activeEnvironment;
        this.grid = grid;
        if (secondaryEntityInstances == null){
            this.secondaryEntityInstances = new ArrayList<>();
        }
        else {
            this.secondaryEntityInstances = secondaryEntityInstances;
        }
    }

    public void setRootContexts(ArrayList<Context> previousContexts){
        rootContexts = previousContexts;
        rootContexts.add(this);
    }

    public ArrayList<Context> getRootContexts(){
        return rootContexts;
    }

    @Override
    public EntityInstance getPrimaryEntityInstance() {
        return primaryEntityInstance;
    }

    @Override
    public void removeEntity(EntityInstance entityInstance) {
        entityInstanceManager.killEntity(entityInstance.getId());
    }

    @Override
    public PropertyInstance getEnvironmentVariable(String name) {
        return activeEnvironment.getProperty(name);
    }


    public EntityInstanceManager getEntityInstanceManager() {
        return entityInstanceManager;
    }

    @Override
    public ActiveEnvironment getActiveEnvironment() {
        return activeEnvironment;
    }

    @Override
    public ArrayList<EntityInstance> getSecondaryEntityInstances() {
        return secondaryEntityInstances;
    }

    @Override
    public Grid getGrid() {
        return grid;
    }

    @Override
    public int getCurrentTick() {
        return currentTick;
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }

    public EntityInstance getInstanceByName(String name){
        if (primaryEntityInstance.getEntityDefinition().getName().equals(name)){
            return primaryEntityInstance;
        }
        else{
            for (EntityInstance entityInstance : secondaryEntityInstances){
                if (entityInstance.getEntityDefinition().getName().equals(name)){
                    return entityInstance;
                }
            }
            return null;
        }
    }
}
