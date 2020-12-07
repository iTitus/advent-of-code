package io.github.ititus.aoc.aoc20.day07;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BagColor {

    private static final Pattern P = Pattern.compile("^(?<colormod>[a-z]+) (?<color>[a-z]+)$");

    private final String modifier;
    private final String color;

    private BagColor(String modifier, String color) {
        this.modifier = modifier;
        this.color = color;
    }

    public static BagColor parse(Matcher m) {
        String modifier = m.group("colormod");
        String color = m.group("color");
        if (modifier.isBlank() || color.isBlank()) {
            throw new RuntimeException();
        }

        return new BagColor(modifier, color);
    }

    public static BagColor of(String bagColor) {
        Matcher m = P.matcher(bagColor);
        if (!m.matches()) {
            throw new RuntimeException();
        }

        return parse(m);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BagColor)) {
            return false;
        }
        BagColor bagColor = (BagColor) o;
        return modifier.equals(bagColor.modifier) && color.equals(bagColor.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modifier, color);
    }
}
