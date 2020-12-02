package io.github.ititus.aoc.aoc20.day02;

import java.util.regex.Matcher;

public class PasswordPolicy {

    private final Range range;
    private final char letter;

    private PasswordPolicy(Range range, char letter) {
        this.range = range;
        this.letter = letter;
    }

    public static PasswordPolicy of(Matcher m) {
        return new PasswordPolicy(
                Range.of(m),
                m.group("letter").charAt(0)
        );
    }

    public boolean isValidPart1(String password) {
        return range.contains(password.chars().filter(c -> c == letter).count());
    }

    public boolean isValidPart2(String password) {
        return password.charAt(range.getMin() - 1) == letter ^ password.charAt(range.getMax() - 1) == letter;
    }
}
