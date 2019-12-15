package io.github.ititus.aoc.aoc19.day13;

import java.util.Arrays;

public enum ArcadeTile {

    EMPTY(' '),
    WALL('█'),
    BLOCK('#'),
    HORIZONTAL_PADDLE('-'),
    BALL('o');

    private static final ArcadeTile[] VALUES;

    static {
        ArcadeTile[] values = values();
        VALUES = Arrays.copyOf(values, values.length);
    }

    private final char renderChar;

    ArcadeTile(char renderChar) {
        this.renderChar = renderChar;
    }

    public static ArcadeTile get(int index) {
        return VALUES[index];
    }

    public char getRenderChar() {
        return renderChar;
    }
}
