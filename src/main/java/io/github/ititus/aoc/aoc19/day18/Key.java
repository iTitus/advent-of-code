package io.github.ititus.aoc.aoc19.day18;

import io.github.ititus.commons.math.vector.Vec2i;

public class Key {

    private final char keyChar;
    private final Vec2i keyPos;
    private Door door;

    public Key(char keyChar, Vec2i keyPos) {
        this.keyChar = keyChar;
        this.keyPos = keyPos;
    }

    public char getKeyChar() {
        return keyChar;
    }

    public Vec2i getKeyPos() {
        return keyPos;
    }

    public Door getDoor() {
        return door;
    }

    public void setDoor(Door door) {
        if (this.door != null) {
            throw new RuntimeException();
        }
        this.door = door;
    }

    @Override
    public String toString() {
        return "Key{keyChar=" + keyChar + ", keyPos=" + keyPos + '}';
    }
}
