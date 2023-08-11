package engine.definition.value.generator.random.impl.string;

import engine.definition.value.generator.random.api.AbstractRandomValueGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RandomStringValueGenerater extends AbstractRandomValueGenerator<String> {

    private final int from = 1;
    private final int to = 50;

    private int length;

    private final List<Character> validCharacters;


    public RandomStringValueGenerater() {
        this.validCharacters = generateOptionCharacters();
        this.length = this.generateLength();
    }

    public int generateLength() {
        return random.nextInt(to - from) + from;
    }

    private List<Character> generateOptionCharacters() {
        List<Character> characters = new ArrayList<>(Arrays.asList('!', '?', ',', '_', '-', '.', '(', ')'));

        for (char c = 'a'; c <= 'z'; c++) {
            characters.add(c);
        }

        for (char c = 'A'; c <= 'Z'; c++) {
            characters.add(c);
        }

        for (char c = '0'; c <= '9'; c++) {
            characters.add(c);
        }

        return characters;
    }


    public String generateValue() {
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(validCharacters.size());
            randomString.append(validCharacters.get(randomIndex));
        }
        return randomString.toString();
    }
}
