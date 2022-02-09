package io.github.ititus.aoc.aoc19.day18;

import io.github.ititus.commons.math.vector.Vec2i;

public class Door {

    private final char doorChar;
    private final Vec2i doorPos;
    private Key key;

    public Door(char doorChar, Vec2i doorPos) {
        this.doorChar = doorChar;
        this.doorPos = doorPos;
    }

    public char getDoorChar() {
        return doorChar;
    }

    public Vec2i getDoorPos() {
        return doorPos;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        if (this.key != null) {
            throw new RuntimeException();
        }
        this.key = key;
    }

    @Override
    public String toString() {
        return "Door{doorChar=" + doorChar + ", doorPos=" + doorPos + '}';
    }
}
