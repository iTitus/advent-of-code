package io.github.ititus.aoc.aoc20.day16;

import java.util.regex.Matcher;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.stream;

public class Ticket {

    private final int[] properties;

    private Ticket(int... properties) {
        this.properties = properties;
    }

    public static Ticket of(int expectedProperties, Matcher m) {
        int[] properties = new int[expectedProperties];
        for (int i = 0; i < expectedProperties; i++) {
            properties[i] = parseInt(m.group("property" + i));
        }

        return new Ticket(properties);
    }

    public IntStream streamProperties() {
        return stream(properties);
    }

    public int getProperty(int i) {
        return properties[i];
    }
}
