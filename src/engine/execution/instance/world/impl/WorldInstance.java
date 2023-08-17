package engine.execution.instance.world.impl;

import dto.impl.MessageDTO;
import engine.definition.entity.EntityDefinition;
import engine.definition.environment.api.EnvVariablesManager;
import engine.definition.property.api.PropertyDefinition;
import engine.definition.property.api.PropertyType;
import engine.execution.context.Context;
import engine.execution.context.ContextImpl;
import engine.execution.instance.enitty.EntityInstance;
import engine.execution.instance.enitty.manager.EntityInstanceManager;
import engine.execution.instance.enitty.manager.EntityInstanceManagerImpl;
import engine.execution.instance.environment.api.ActiveEnvironment;
import engine.execution.instance.environment.impl.ActiveEnvironmentImpl;
import engine.execution.instance.property.PropertyInstance;
import engine.execution.instance.property.PropertyInstanceImpl;
import engine.execution.instance.termination.impl.Termination;
import engine.execution.instance.world.api.WorldInstanceInterface;
import engine.rule.Rule;
import engine.schema.generated.*;
import engine.definition.world.api.WorldDefinition;
import engine.system.functions.SystemFunctions;

import java.util.ArrayList;


public class WorldInstance implements WorldInstanceInterface {

    private final WorldDefinition worldDefinition;
    private final EntityInstanceManager entityInstanceManager;
    private final ActiveEnvironment activeEnvironment;
    private ArrayList<Rule> rules;
    private Termination termination;

    public WorldInstance(WorldDefinition worldDefinition) {
        this.worldDefinition = worldDefinition;
        this.entityInstanceManager = new EntityInstanceManagerImpl();
        this.activeEnvironment = new ActiveEnvironmentImpl();
        this.rules = new ArrayList<Rule>();
        this.termination = new Termination();
    }

    public void setWorldInstance() {
        this.setEntitiesInstances();
        this.rules = this.worldDefinition.getRules();
        this.termination = this.worldDefinition.getTermination();

    }

    public void setEntitiesInstances() {
        for (EntityDefinition entityDefinition : this.worldDefinition.getEntityDefinitions().values()) {
            for(int i=0; i < entityDefinition.getPopulation(); i++)
                entityInstanceManager.create(entityDefinition);
        }
    }


    public MessageDTO setEnvVariable(String envVariableName, Object value){
        try {
            PropertyDefinition propertyDefinition = this.worldDefinition.getEnvVariablesManager().getEnvVariable(envVariableName);
            PropertyInstance propertyInstance = new PropertyInstanceImpl(propertyDefinition, value);
            activeEnvironment.addPropertyInstance(propertyInstance);
        }
        catch (Exception e){
            return new MessageDTO(false, e.getMessage());
        }

        return new MessageDTO(true, "Env variable " + envVariableName + " set to " + value + " successfully !\n");
    }


    @Override
    public MessageDTO run() {
        setWorldInstance();

        return null;
    }

    @Override
    public void stop() {

    }

}
