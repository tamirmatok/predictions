package engine.definition.value.generator.api;

public interface ValueGenerator<T> {
    T generateValue();

    T getFrom();

    T getTo();

    boolean hasRange();

}
