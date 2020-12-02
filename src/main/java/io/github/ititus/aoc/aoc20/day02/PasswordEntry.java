package io.github.ititus.aoc.aoc20.day02;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordEntry {

    private static final Pattern PATTERN = Pattern.compile(
            "^(?<min>\\d+)-(?<max>\\d+) (?<letter>[a-z]): (?<password>[a-z]+)$");

    private final PasswordPolicy policy;
    private final String password;

    private PasswordEntry(PasswordPolicy policy, String password) {
        this.policy = policy;
        this.password = password;
    }

    public static PasswordEntry of(String input) {
        Matcher m = PATTERN.matcher(input);
        if (!m.matches()) {
            throw new IllegalArgumentException();
        }

        return new PasswordEntry(
                PasswordPolicy.of(m),
                m.group("password")
        );
    }

    public boolean isValidPart1() {
        return policy.isValidPart1(password);
    }

    public boolean isValidPart2() {
        return policy.isValidPart2(password);
    }
}
