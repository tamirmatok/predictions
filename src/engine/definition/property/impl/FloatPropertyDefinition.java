package engine.definition.property.impl;

import engine.definition.property.api.AbstractPropertyDefinition;
import engine.definition.property.api.PropertyType;
import engine.definition.value.generator.api.ValueGenerator;

public class FloatPropertyDefinition extends AbstractPropertyDefinition<Float> {

    public FloatPropertyDefinition(String name, ValueGenerator<Float> valueGenerator) {
        super(name, PropertyType.FLOAT, valueGenerator);
    }

}