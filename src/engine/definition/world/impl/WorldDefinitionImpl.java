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
    private final HashMap<String, EntityDefinition> entityDefinitions;
    private final EnvVariablesManager envVariableManager;
    private final ArrayList<Rule> rules;

    private Termination termination;

    public WorldDefinitionImpl() {
        this.entityDefinitions = new HashMap<String, EntityDefinition>();
        this.envVariableManager = new EnvVariableManagerImpl();
        this.rules = new ArrayList<>();
        this.termination = null;
    }

    @Override
    public void loadWorldDefintion(PRDWorld prdWorld) {
        for (PRDEntity prdEntity : prdWorld.getPRDEntities().getPRDEntity()) {
            EntityDefinition entityDefinition = JaxbConverter.convertEntity(prdEntity);
            this.addEntityDefinition(entityDefinition);
        }
        for (PRDEnvProperty prdEnvProperty : prdWorld.getPRDEvironment().getPRDEnvProperty()) {
            if (envVariableManager.getEnvVariable(prdEnvProperty.getPRDName()) != null) {
                throw new RuntimeException("Environment variable with name " + prdEnvProperty.getPRDName() + " already exists");
            }
            PropertyDefinition propertyDefinition = JaxbConverter.convertEnvProperty(prdEnvProperty);
            this.addEnvPropertyDefinition(propertyDefinition);
        }
        for (PRDRule prdRule : prdWorld.getPRDRules().getPRDRule()) {
            Rule rule = JaxbConverter.convertRule(prdRule, this.entityDefinitions);
            this.addRule(rule);
        }
        this.termination = JaxbConverter.convertTermination(prdWorld.getPRDTermination());
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
        entityDefinitions.put(entityDefinition.getName(),entityDefinition);
    }

    public void addEnvPropertyDefinition(PropertyDefinition propertyDefinition) {
        envVariableManager.addEnvironmentVariable(propertyDefinition);
    }

    public void addRule(Rule rule) {
        rules.add(rule);
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
