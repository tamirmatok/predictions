package engine.definition.world.api;

import engine.definition.entity.EntityDefinition;
import engine.definition.environment.api.EnvVariablesManager;
import engine.definition.property.api.PropertyDefinition;
import engine.execution.instance.termination.impl.Termination;
import engine.rule.Rule;
import engine.schema.generated.PRDWorld;

import java.util.ArrayList;
import java.util.HashMap;

public interface WorldDefinition {

    void loadWorldDefintion(PRDWorld prdWorld);
    HashMap<String, EntityDefinition> getEntityDefinitions();
    EnvVariablesManager getEnvVariablesManager();
    void addEntityDefinition(EntityDefinition entityDefinition);
    void addEnvPropertyDefinition(PropertyDefinition propertyDefinition);
    void addRule(Rule rule);

    ArrayList<Rule> getRules();
    public Termination getTermination();

}
