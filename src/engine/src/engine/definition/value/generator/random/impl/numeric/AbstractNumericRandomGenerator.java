package engine.definition.value.generator.random.impl.numeric;

import engine.definition.value.generator.random.api.AbstractRandomValueGenerator;

public abstract class AbstractNumericRandomGenerator<T> extends AbstractRandomValueGenerator<T> {

    protected final T from;
    protected final T to;

    protected AbstractNumericRandomGenerator(T from, T to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public T getFrom() {
        return from;
    }

    @Override
    public T getTo() {
        return to;
    }

    @Override
    public boolean hasRange() {
        return true;
    }

}
