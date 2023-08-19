package engine.definition.property.api;

import engine.definition.value.generator.api.ValueGenerator;

public interface PropertyDefinition {
    String getName();
    PropertyType getType();
    Object generateValue();
    ValueGenerator getValueGenerator();

}