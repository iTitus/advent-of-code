package io.github.ititus.aoc.aoc19.day08;

public enum SpaceEncodedColor {

    BLACK(false, ' '),
    WHITE(false, '█'),
    TRANSPARENT(true, '\0');

    private final boolean transparent;
    private final char renderChar;

    SpaceEncodedColor(boolean transparent, char renderChar) {
        this.transparent = transparent;
        this.renderChar = renderChar;
    }

    public static SpaceEncodedColor get(int index) {
        switch (index) {
            case 0:
                return BLACK;
            case 1:
                return WHITE;
            case 2:
                return TRANSPARENT;
            default:
                throw new IllegalArgumentException();
        }
    }

    public boolean isTransparent() {
        return transparent;
    }

    public char getRenderChar() {
        if (renderChar == '\0') {
            throw new RuntimeException();
        }

        return renderChar;
    }
}