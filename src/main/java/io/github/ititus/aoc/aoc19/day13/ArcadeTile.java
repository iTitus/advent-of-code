package io.github.ititus.aoc.aoc19.day13;

import java.util.Arrays;

public enum ArcadeTile {

    EMPTY(0, ' '),
    WALL(1, '█'),
    BLOCK(2, '#'),
    HORZONTAL_PADDLE(3, '-'),
    BALL(4, 'o');

    private static final ArcadeTile[] VALUES;

    static {
        ArcadeTile[] values = values();
        VALUES = Arrays.copyOf(values, values.length);
    }

    private final int index;
    private final char renderChar;

    ArcadeTile(int index, char renderChar) {
        this.index = index;
        this.renderChar = renderChar;
    }

    public static ArcadeTile get(int index) {
        return VALUES[index];
    }

    public int getIndex() {
        return index;
    }

    public char getRenderChar() {
        return renderChar;
    }
}
