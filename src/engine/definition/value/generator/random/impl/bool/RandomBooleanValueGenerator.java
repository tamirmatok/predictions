package engine.definition.value.generator.random.impl.bool;

import engine.definition.value.generator.random.api.AbstractRandomValueGenerator;

public class RandomBooleanValueGenerator extends AbstractRandomValueGenerator<Boolean> {

    @Override
    public Boolean generateValue() {
        return random.nextDouble() < 0.5;
    }

    @Override
    public Boolean getFrom() {
        return null;
    }

    @Override
    public Boolean getTo() {
        return null;
    }

    @Override
    public boolean hasRange() {
        return false;
    }
}
