package engine.execution.instance.world.impl;

import engine.definition.entity.EntityDefinition;
import engine.definition.property.api.PropertyDefinition;
import engine.execution.instance.enitty.EntityInstance;
import engine.execution.instance.enitty.manager.EntityInstanceManager;
import engine.execution.instance.enitty.manager.EntityInstanceManagerImpl;
import engine.execution.instance.environment.api.ActiveEnvironment;
import engine.execution.instance.environment.impl.ActiveEnvironmentImpl;
import engine.execution.instance.grid.Grid;
import engine.execution.instance.property.PropertyInstanceImpl;
import engine.execution.instance.termination.impl.Termination;
import engine.rule.Rule;
import engine.definition.world.api.WorldDefinition;

import java.util.ArrayList;
import java.util.HashMap;


public class WorldInstance{

    private final WorldDefinition worldDefinition;
    private EntityInstanceManager entityInstanceManager;
    private ActiveEnvironment activeEnvironment;
    private final ArrayList<Rule> rules;
    private final Termination termination;
    private final Grid grid;


    public WorldInstance(WorldDefinition worldDefinition) {
        this.worldDefinition = worldDefinition;
        this.entityInstanceManager = new EntityInstanceManagerImpl();
        this.activeEnvironment = new ActiveEnvironmentImpl();
        this.rules = this.worldDefinition.getRules();
        this.termination = this.worldDefinition.getTermination();
        this.grid = new Grid(worldDefinition.getGridDefinition());
    }


    public void setEntityInstances(HashMap<String, String> entityNameToEntityPopulation) {
        for (String entityName : entityNameToEntityPopulation.keySet()) {
            try{
                Integer entityPopulation = Integer.parseInt(entityNameToEntityPopulation.get(entityName));
                EntityDefinition entityDefinition = this.worldDefinition.getEntityDefinitions().get(entityName);
                entityDefinition.setPopulation(entityPopulation);
                for (int i = 0; i < entityPopulation; i++) {
                    EntityInstance entityInstance = entityInstanceManager.create(entityDefinition);
                    this.grid.setRandom(entityInstance);
                }
            }
            catch (NumberFormatException e){
                this.entityInstanceManager = new EntityInstanceManagerImpl();
                throw new IllegalArgumentException("Population of entity " + entityName + " is not a valid number");
            }
        }
    }

    public void setEnvProperties(HashMap<String, String> envPropertyNameToEnvPropertyValue){
        for (String envPropertyName : envPropertyNameToEnvPropertyValue.keySet()) {
            String envPropertyValue = envPropertyNameToEnvPropertyValue.get(envPropertyName);
            this.setEnvVariable(envPropertyName, envPropertyValue);
        }
    }



    public void setEnvVariable(String envVariableName, Object value) {
        PropertyDefinition propertyDefinition = this.worldDefinition.getEnvVariables().get(envVariableName);
        if (value == null) {
            activeEnvironment.addPropertyInstance(new PropertyInstanceImpl(propertyDefinition, propertyDefinition.generateValue()));
        } else {
            activeEnvironment.addPropertyInstance(new PropertyInstanceImpl(propertyDefinition, value));
        }
    }

    public ActiveEnvironment getActiveEnvironment(){
        return activeEnvironment;
    }


    public void resetActiveEnvironment(){
        this.activeEnvironment = new ActiveEnvironmentImpl();
    }


    public WorldDefinition getWorldDefinition() {
        return worldDefinition;
    }

    public ArrayList<Rule> getRules() {
        return rules;
    }


    public EntityInstanceManager getEntityInstanceManager(){
        return entityInstanceManager;
    }

    public Termination getTermination() {
        return termination;
    }

    public Grid getGrid() {
        return grid;
    }

}
