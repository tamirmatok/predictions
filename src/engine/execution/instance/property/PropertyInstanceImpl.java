package engine.execution.instance.property;

import engine.definition.property.api.PropertyDefinition;
import engine.definition.property.api.PropertyType;

public class PropertyInstanceImpl implements PropertyInstance {

    private PropertyDefinition propertyDefinition;
    private Object value;

    public PropertyInstanceImpl(PropertyDefinition propertyDefinition, Object value) {
        this.propertyDefinition = propertyDefinition;
        // TODO validation !!!!!!
//        this.validateValueType(value);
//        this.validateValueRange(value);
        this.value = value;
    }

    private void validateValueRange(Object value) {
    }

    private void validateValueType(Object value) {
        PropertyType propertyType = this.propertyDefinition.getType();
        if (propertyType == PropertyType.DECIMAL) {
            if (!(value instanceof Integer)) {
                throw new IllegalArgumentException("Value " + value + " is not of type " + propertyType);
            }
        } else if (propertyType == PropertyType.FLOAT) {
            if (!(value instanceof Double)) {
                throw new IllegalArgumentException("Value " + value + " is not of type " + propertyType);
            }
        } else if (propertyType == PropertyType.STRING) {
            if (!(value instanceof String)) {
                throw new IllegalArgumentException("Value " + value + " is not of type " + propertyType);
            }
        } else if (propertyType == PropertyType.BOOLEAN) {
            if (!(value instanceof Boolean)) {
                throw new IllegalArgumentException("Value " + value + " is not of type " + propertyType);
            }
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
