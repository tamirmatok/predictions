package engine.definition.world.impl;

import engine.definition.entity.EntityDefinition;
import engine.definition.environment.api.EnvVariablesManager;
import engine.definition.environment.impl.EnvVariableManagerImpl;
import engine.definition.property.api.PropertyDefinition;
import engine.definition.world.api.WorldDefinition;
import engine.execution.instance.termination.impl.Termination;
import engine.factory.impl.JaxbConverter;
import engine.rule.Rule;
import engine.schema.generated.*;

import java.util.ArrayList;
import java.util.HashMap;

public class WorldDefinitionImpl implements WorldDefinition {
    private HashMap<String, EntityDefinition> entityDefinitions;
    private EnvVariablesManager envVariableManager;
    private ArrayList<Rule> rules;

    private Termination termination;

    public WorldDefinitionImpl() {
        this.entityDefinitions = new HashMap<String, EntityDefinition>();
        this.envVariableManager = new EnvVariableManagerImpl();
        this.rules = new ArrayList<>();
        this.termination = null;
    }


    private void resetWorld() {
        this.entityDefinitions = new HashMap<String, EntityDefinition>();
        this.envVariableManager = new EnvVariableManagerImpl();
        this.rules = new ArrayList<>();
        this.termination = null;
    }


    //TODO: remove load defintion and the validation funciton from here
    @Override
    public void loadWorldDefintion(PRDWorld prdWorld) {
        this.resetWorld();
        for (PRDEntity prdEntity : prdWorld.getPRDEntities().getPRDEntity()) {
            EntityDefinition entityDefinition = JaxbConverter.convertEntity(prdEntity);
            this.addEntityDefinition(entityDefinition);
        }
        for (PRDEnvProperty prdEnvProperty : prdWorld.getPRDEvironment().getPRDEnvProperty()) {
            if (envVariableManager.getEnvVariable(prdEnvProperty.getPRDName()) != null) {
                throw new IllegalArgumentException("Environment variable with name " + prdEnvProperty.getPRDName() + " already exists");
            }
            PropertyDefinition propertyDefinition = JaxbConverter.convertEnvProperty(prdEnvProperty);
            this.addEnvPropertyDefinition(propertyDefinition);
        }
        for (PRDRule prdRule : prdWorld.getPRDRules().getPRDRule()) {
            validateRuleActions(prdWorld.getPRDEntities(), prdRule);
            Rule rule = JaxbConverter.convertRule(prdRule, this.entityDefinitions);
            this.addRule(rule);
        }
        this.termination = JaxbConverter.convertTermination(prdWorld.getPRDTermination());
    }


    public void validateRuleActions(PRDEntities prdEntities, PRDRule prdRule) {
        for (PRDAction prdAction : prdRule.getPRDActions().getPRDAction()) {
            boolean entityFound = false;
            for (PRDEntity prdEntity : prdEntities.getPRDEntity()) {
                if (prdEntity.getName().equals(prdAction.getEntity())) {
                    entityFound = true;
                }
                if (entityFound){
                    boolean propertyFound = false;
                    String propertyName = prdAction.getProperty();
                    if (propertyName != null){
                        for (PRDProperty prdProperty: prdEntity.getPRDProperties().getPRDProperty()){
                            if (prdProperty.getPRDName().equals(propertyName)){
                                propertyFound = true;
                            }
                        }
                        if (!propertyFound){
                            throw new IllegalArgumentException("Rule " + prdRule.getName() + " -   un exist property '"+ propertyName + "'" + "for entity '" + prdEntity.getName());
                        }
                    }

                }
            }
            if (!entityFound) {
                throw new IllegalArgumentException("Rule '" + prdRule.getName() + "' contain non exist entity '" + prdAction.getEntity() + "'");
            }
        }
    }


    public HashMap<String, EntityDefinition> getEntityDefinitions() {
        return entityDefinitions;
    }

    public EnvVariablesManager getEnvVariablesManager() {
        return envVariableManager;
    }

    public ArrayList<Rule> getRules() {
        return rules;
    }

    public void addEntityDefinition(EntityDefinition entityDefinition) {
        entityDefinitions.put(entityDefinition.getName(), entityDefinition);
    }

    public void addEnvPropertyDefinition(PropertyDefinition propertyDefinition) {
        envVariableManager.addEnvironmentVariable(propertyDefinition);
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public Termination getTermination() {
        return this.termination;
    }

    @Override
    public String toString() {
        return "World Definition:\n" +
                "Entity Definitions:" + entityDefinitions + "\n" +
                "Env Variables:" + envVariableManager + "\n" +
                "Rules: " + rules + "\n" +
                "Termination = " + termination + "\n";
    }

}
