package engine.execution.instance.world.impl;

import engine.definition.entity.EntityDefinition;
import engine.definition.environment.api.EnvVariablesManager;
import engine.definition.property.api.PropertyDefinition;
import engine.execution.instance.enitty.manager.EntityInstanceManager;
import engine.execution.instance.enitty.manager.EntityInstanceManagerImpl;
import engine.execution.instance.environment.api.ActiveEnvironment;
import engine.execution.instance.environment.impl.ActiveEnvironmentImpl;
import engine.execution.instance.world.api.WorldInstanceInterface;
import engine.schema.generated.*;
import engine.definition.world.api.WorldDefinition;


public class WorldInstance implements WorldInstanceInterface {

    private final WorldDefinition worldDefinition;
    private final EntityInstanceManager entityInstanceManager;
    private final ActiveEnvironment activeEnvironment;

    public WorldInstance(WorldDefinition worldDefinition) {
        this.worldDefinition = worldDefinition;
        this.entityInstanceManager = new EntityInstanceManagerImpl();
        this.activeEnvironment = new ActiveEnvironmentImpl();
    }

    public void setWorldInstance() {
        this.setEntitiesInstances();
        this.setActiveEnvironment();
    }

    public void setEntitiesInstances() {
        for (EntityDefinition entityDefinition : this.worldDefinition.getEntityDefinitions().values()) {
            this.entityInstanceManager.create(entityDefinition);
        }
    }

    public void setActiveEnvironment(){
        EnvVariablesManager envVariablesManager = this.worldDefinition.getEnvVariablesManager();
        for (PropertyDefinition propertyDefinition : envVariablesManager.getEnvVariables()) {
            //TODO: CHECK WHAT ARE THE CONDIITONS FOR THE ENVIRONMENT
            // COLLECT VALUE FROM THE USER
        }

    }
//
//    public void setRules(PRDRules prdRules) {
//        EntityInstanceManager entityInstanceManager = this.worldDefinition.getEntityInstanceManager();
//        for (PRDRule prdRule : prdRules.getPRDRule()) {
//            for (PRDAction prdAction : prdRule.getPRDActions().getPRDAction()) {
//                String entityName = prdAction.getEntity();
//                EntityDefinition entityDefinion = entityInstanceManager.getInstanceByName(entityName).getEntityDefinition();
//                if (entityDefinion == null) {
//                    throw new IllegalArgumentException(" rule " + prdRule.getName() + " has no entity named " + entityName);
//                }
//                this.worldDefinition.getRules().add(JaxbConverter.convertRule(prdRule, entityDefinion));
//            }
//        }
//
//    }

    public void setTermination(PRDTermination prdTermination) {

    }

    @Override
    public void run() {

    }

    @Override
    public void stop() {

    }

}
