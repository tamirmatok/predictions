package engine.definition.world.api;

import engine.definition.entity.EntityDefinition;
import engine.definition.environment.api.EnvVariablesManager;
import engine.definition.grid.GridDefinition;
import engine.definition.property.api.PropertyDefinition;
import engine.execution.instance.termination.impl.Termination;
import engine.rule.Rule;
import engine.schema.generated.PRDWorld;

import java.util.ArrayList;
import java.util.HashMap;

public interface WorldDefinition {

    void loadWorldDefinition(PRDWorld prdWorld);

    GridDefinition getGridDefinition();
    HashMap<String, EntityDefinition> getEntityDefinitions();
    HashMap<String, PropertyDefinition> getEnvVariables();
    void addEntityDefinition(EntityDefinition entityDefinition);
    void addRule(Rule rule);

    ArrayList<Rule> getRules();
    public Termination getTermination();

}
