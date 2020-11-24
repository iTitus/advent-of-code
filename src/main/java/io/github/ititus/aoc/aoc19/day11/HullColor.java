package io.github.ititus.aoc.aoc19.day11;

public enum HullColor {

    BLACK(0, '\u2588'),
    WHITE(1, '\u2591');

    private final int index;
    private final char renderChar;

    HullColor(int index, char renderChar) {
        this.index = index;
        this.renderChar = renderChar;
    }

    public int getIndex() {
        return index;
    }

    public char getRenderChar() {
        return renderChar;
    }
}
