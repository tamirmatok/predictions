package engine.definition.value.generator.fixed;

import engine.definition.value.generator.api.ValueGenerator;

public class FixedValueGenerator<T> implements ValueGenerator<T> {

    private final T fixedValue;

    private  T from;

    private  T to;

    private boolean isRangeSet = false;

    public FixedValueGenerator(T fixedValue) {
        this.fixedValue = fixedValue;
    }

    public FixedValueGenerator(T fixedValue, T from, T to) {
        this.fixedValue = fixedValue;
        this.from = from;
        this.to = to;
        isRangeSet = true;
    }

    @Override
    public T generateValue() {
        if(isRangeSet) {
            validateFixedValueInRange();
        }
        return fixedValue;
    }

    public void validateFixedValueInRange() {
        if (fixedValue instanceof Integer) {
            Integer integerFixedValue = (Integer) fixedValue;
            int integerFrom = (Integer) from;
            int integerTo = (Integer) to;
            if (integerFixedValue > integerTo || integerFixedValue < integerFrom) {
                throw new IllegalArgumentException("Fixed value is not in range");
            }
        }
        else if (fixedValue instanceof Float) {
            Float floatFixedValue = (Float) fixedValue;
            float floatFrom = (Float) from;
            float floatTo = (Float) to;
            if (floatFixedValue > floatTo || floatFixedValue < floatFrom) {
                throw new IllegalArgumentException("Fixed value is not in range");
            }
        }
    }
}
