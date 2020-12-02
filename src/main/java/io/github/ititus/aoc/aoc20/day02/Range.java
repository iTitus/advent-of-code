package io.github.ititus.aoc.aoc20.day02;

import java.util.regex.Matcher;

public class Range {

    private final int min, max;

    private Range(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public static Range of(Matcher m) {
        return new Range(
                Integer.parseInt(m.group("min")),
                Integer.parseInt(m.group("max"))
        );
    }

    public boolean contains(int n) {
        return min <= n && n <= max;
    }

    public boolean contains(long n) {
        return min <= n && n <= max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}