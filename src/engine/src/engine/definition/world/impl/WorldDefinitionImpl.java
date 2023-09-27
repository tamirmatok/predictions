package engine.definition.world.impl;

import engine.definition.grid.GridDefinition;
import engine.definition.entity.EntityDefinition;
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
    private final HashMap<String, PropertyDefinition> envPropertyNameToEnvPropertyDefinition;
    private final ArrayList<Rule> rules;
    private Termination termination;
    private GridDefinition gridDefinition;


    public WorldDefinitionImpl(PRDWorld prdWorld) {
        this.entityDefinitions = new HashMap<String, EntityDefinition>();
        this.envPropertyNameToEnvPropertyDefinition = new HashMap<String, PropertyDefinition>();
        this.rules = new ArrayList<>();
        this.loadWorldDefinition(prdWorld);
    }

    @Override
    public void loadWorldDefinition(PRDWorld prdWorld) {
        for (PRDEntity prdEntity : prdWorld.getPRDEntities().getPRDEntity()) {
            EntityDefinition entityDefinition = JaxbConverter.convertEntity(prdEntity);
            this.addEntityDefinition(entityDefinition);
        }
        for (PRDEnvProperty prdEnvProperty : prdWorld.getPRDEnvironment().getPRDEnvProperty()) {
            if (envPropertyNameToEnvPropertyDefinition.containsKey(prdEnvProperty.getPRDName())) {
                throw new IllegalArgumentException("Environment variable with name " + prdEnvProperty.getPRDName() + " already exists");
            } else {
                PropertyDefinition propertyDefinition = JaxbConverter.convertEnvProperty(prdEnvProperty);
                envPropertyNameToEnvPropertyDefinition.put(propertyDefinition.getName(), propertyDefinition);
            }
        }
        for (PRDRule prdRule : prdWorld.getPRDRules().getPRDRule()) {
            Rule rule = JaxbConverter.convertRule(prdRule, this.entityDefinitions);
            this.addRule(rule);
        }
        this.gridDefinition = JaxbConverter.convertGrid(prdWorld.getPRDGrid());
        this.termination = JaxbConverter.convertTermination(prdWorld.getPRDTermination());

    }
    public HashMap<String, EntityDefinition> getEntityDefinitions() {
        return entityDefinitions;
    }

    public HashMap<String, PropertyDefinition> getEnvVariables() {
        return envPropertyNameToEnvPropertyDefinition;
    }

    public ArrayList<Rule> getRules() {
        return rules;
    }

    public void addEntityDefinition(EntityDefinition entityDefinition) {
        entityDefinitions.put(entityDefinition.getName(), entityDefinition);
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public Termination getTermination() {
        return this.termination;
    }

    public GridDefinition getGridDefinition() {
        return this.gridDefinition;
    }

}
