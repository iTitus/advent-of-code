package io.github.ititus.aoc.aoc19.day15;

import java.util.Arrays;

public enum MovementStatus {

    BLOCKED,
    FREE,
    OXYGEN;

    private static final MovementStatus[] VALUES;

    static {
        MovementStatus[] values = values();
        VALUES = Arrays.copyOf(values, values.length);
    }

    public static MovementStatus get(int index) {
        return VALUES[index];
    }
}
