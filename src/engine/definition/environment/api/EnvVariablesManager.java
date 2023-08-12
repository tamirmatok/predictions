package engine.definition.environment.api;

import engine.definition.property.api.PropertyDefinition;
import engine.execution.instance.environment.api.ActiveEnvironment;

import java.util.Collection;

public interface EnvVariablesManager {
    void addEnvironmentVariable(PropertyDefinition propertyDefinition);
    ActiveEnvironment createActiveEnvironment();
    Collection<PropertyDefinition> getEnvVariables();

    public PropertyDefinition getEnvVariable(String name);
}
