package engine.definition.entity;

import engine.definition.property.api.PropertyDefinition;

import java.util.List;

public interface EntityDefinition {
    String getName();
    int getPopulation();
    List<PropertyDefinition> getProps();

    void setPopulation(int population);
}
