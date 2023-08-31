package engine.execution.instance.world.impl;

import dto.impl.MessageDTO;
import engine.definition.entity.EntityDefinition;
import engine.definition.property.api.PropertyDefinition;
import engine.execution.instance.enitty.manager.EntityInstanceManager;
import engine.execution.instance.enitty.manager.EntityInstanceManagerImpl;
import engine.execution.instance.environment.api.ActiveEnvironment;
import engine.execution.instance.environment.impl.ActiveEnvironmentImpl;
import engine.execution.instance.grid.Grid;
import engine.execution.instance.property.PropertyInstanceImpl;
import engine.execution.instance.termination.impl.Termination;
import engine.factory.impl.JaxbConverter;
import engine.rule.Rule;
import engine.definition.world.api.WorldDefinition;
import engine.schema.generated.PRDEntity;
import engine.schema.generated.PRDEnvProperty;
import engine.schema.generated.PRDRule;
import engine.schema.generated.PRDWorld;

import java.util.ArrayList;


public class WorldInstance{

    private final WorldDefinition worldDefinition;
    private final EntityInstanceManager entityInstanceManager;
    private ActiveEnvironment activeEnvironment;
    private ArrayList<Rule> rules;
    private Termination termination;
    private Grid grid;


    public WorldInstance(WorldDefinition worldDefinition) {
        this.worldDefinition = worldDefinition;
        this.entityInstanceManager = new EntityInstanceManagerImpl();
        this.activeEnvironment = new ActiveEnvironmentImpl();
        this.rules = new ArrayList<Rule>();
        this.termination = new Termination();
        this.loadWorldDefintion();
    }

    private void loadWorldDefintion() {
        for (EntityDefinition entityDefinition: worldDefinition.getEntityDefinitions().values()){
            for (int i = 0; i < entityDefinition.getPopulation(); i++)
                entityInstanceManager.create(entityDefinition);
        }
        //TODO : THINK WHEN AND HOW WE SHOULD LOAD THE ENV VARIABLES
//        for (PropertyDefinition propertyDefinition: worldDefinition.getEnvVariables().values()){
//            activeEnvironment.addPropertyInstance(new PropertyInstanceImpl(propertyDefinition, propertyDefinition.generateValue()));
//        }

        this.rules = this.worldDefinition.getRules();
        this.termination = this.worldDefinition.getTermination();
        this.grid = new Grid(this.worldDefinition.getGridDefinition());

    }



    public void setWorldInstance(ActiveEnvironment activeEnvironment) {
        this.activeEnvironment = activeEnvironment;
        this.setEntitiesInstances();
        this.rules = this.worldDefinition.getRules();
        this.termination = this.worldDefinition.getTermination();
    }

    public void setEntitiesInstances() {
        for (EntityDefinition entityDefinition : this.worldDefinition.getEntityDefinitions().values()) {
            for (int i = 0; i < entityDefinition.getPopulation(); i++)
                entityInstanceManager.create(entityDefinition);
        }
    }


//    public MessageDTO setEnvVariable(String envVariableName, Object value) {
//        try {
//            PropertyDefinition propertyDefinition = this.worldDefinition.getEnvVariablesManager().getEnvVariable(envVariableName);
//            activeEnvironment.addPropertyInstance(new PropertyInstanceImpl(propertyDefinition, value));
//        } catch (Exception e) {
//            return new MessageDTO(false, e.getMessage());
//        }
//        return new MessageDTO(true, "Env variable " + envVariableName + " set to " + value + " successfully !\n");
//    }


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

}
