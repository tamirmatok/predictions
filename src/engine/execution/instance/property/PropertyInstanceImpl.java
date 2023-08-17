package engine.execution.instance.property;

import engine.definition.property.api.PropertyDefinition;
import engine.definition.property.api.PropertyType;
import engine.definition.value.generator.api.ValueGenerator;

public class PropertyInstanceImpl implements PropertyInstance {

    private PropertyDefinition propertyDefinition;
    private Object value;

    public PropertyInstanceImpl(PropertyDefinition propertyDefinition, Object value) {
        this.propertyDefinition = propertyDefinition;
        this.validateValueType(value);
        this.validateValueRange(value);
    }

    private void validateValueRange(Object value) {
        ValueGenerator valueGenerator = this.propertyDefinition.getValueGenerator();
        PropertyType propertyType = this.propertyDefinition.getType();
        if (valueGenerator.hasRange()) {
            switch(propertyType){
                case DECIMAL:
                    Integer min = (Integer) propertyDefinition.getValueGenerator().getFrom();
                    Integer max = (Integer) propertyDefinition.getValueGenerator().getTo();
                    Integer val = Integer.parseInt(value.toString());
                    if (val < min || val > max) {
                        throw new IllegalArgumentException("Value " + value + " is not in range [" + min + ", " + max + "]");
                    }
                    break;
                case FLOAT:
                    Float minD = (Float) propertyDefinition.getValueGenerator().getFrom();
                    Float maxD = (Float) propertyDefinition.getValueGenerator().getTo();
                    Float valf = Float.parseFloat(value.toString());
                    if (valf < minD || valf > maxD) {
                        throw new IllegalArgumentException("Value " + value + " is not in range [" + minD + ", " + maxD + "]");
                    }
                    break;
            }
        }
    }

    private void validateValueType(Object value) {
        PropertyType propertyType = this.propertyDefinition.getType();
        try {
            if (propertyType == PropertyType.DECIMAL) {
                this.value = Integer.parseInt(value.toString());
            } else if (propertyType == PropertyType.BOOLEAN) {
                this.value = Boolean.parseBoolean(value.toString());
            } else if (propertyType == PropertyType.FLOAT) {
                this.value = Double.parseDouble(value.toString());
            } else if (propertyType == PropertyType.STRING) {
                this.value = value.toString();
            }
        }
        catch(Exception e){
            throw new IllegalArgumentException("Value " + value + " is not " + propertyType + " type");
        }
    }

    @Override
    public PropertyDefinition getPropertyDefinition() {
        return propertyDefinition;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void updateValue(Object val) {
        this.value = val;
    }

}
