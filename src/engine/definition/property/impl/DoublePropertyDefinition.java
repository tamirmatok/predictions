package engine.definition.property.impl;

import engine.definition.property.api.AbstractPropertyDefinition;
import engine.definition.property.api.PropertyType;
import engine.definition.value.generator.api.ValueGenerator;

public class DoublePropertyDefinition extends AbstractPropertyDefinition<Double> {

    public DoublePropertyDefinition(String name, ValueGenerator<Double> valueGenerator) {
        super(name, PropertyType.FLOAT, valueGenerator);
    }

}