package engine.definition.value.generator.api;

import engine.definition.value.generator.fixed.FixedValueGenerator;
import engine.definition.value.generator.random.impl.bool.RandomBooleanValueGenerator;
import engine.definition.value.generator.random.impl.numeric.RandomFloatValueGenerator;
import engine.definition.value.generator.random.impl.numeric.RandomIntegerValueGenerator;
import engine.definition.value.generator.random.impl.string.RandomStringValueGenerater;

public interface ValueGeneratorFactory {

    static <T> ValueGenerator<T> createFixed(T value) {
        return new FixedValueGenerator<>(value);
    }

    static <T> ValueGenerator<T> createFixed(T value, T from, T to) {
        return new FixedValueGenerator<>(value, from, to);
    }

    static ValueGenerator<Boolean> createRandomBoolean() {
        return new RandomBooleanValueGenerator();
    }

    static ValueGenerator<Integer> createRandomInteger(Integer from, Integer to) {
        return new RandomIntegerValueGenerator(from, to);
    }

    static ValueGenerator<Float> createRandomFloat(Float from, Float to) {
        return new RandomFloatValueGenerator(from, to);
    }

    static ValueGenerator<String> createRandomString() {
        return new RandomStringValueGenerater();
    }

}
