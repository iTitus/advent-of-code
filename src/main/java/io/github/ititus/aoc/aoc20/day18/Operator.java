package io.github.ititus.aoc.aoc20.day18;

import it.unimi.dsi.fastutil.longs.LongStack;

public enum Operator {

    OPEN,
    CLOSE,
    SUM,
    PRODUCT;

    public static Operator get(String token) {
        if (token.length() != 1) {
            return null;
        }

        return get(token.charAt(0));
    }

    public static Operator get(char token) {
        return switch (token) {
            case '(' -> OPEN;
            case ')' -> CLOSE;
            case '+' -> SUM;
            case '*' -> PRODUCT;
            default -> null;
        };
    }

    public boolean isBracket() {
        return switch (this) {
            case OPEN, CLOSE -> true;
            default -> false;
        };
    }


    public long apply(LongStack parameters) {
        if (isBracket()) {
            throw new RuntimeException();
        }

        long b = parameters.popLong();
        long a = parameters.popLong();

        return switch (this) {
            case SUM -> a + b;
            case PRODUCT -> a * b;
            default -> throw new RuntimeException();
        };
    }

    public int precedence(boolean part2) {
        if (isBracket()) {
            throw new RuntimeException();
        }

        return (part2 && this == SUM) ? 1 : 0;
    }

    public boolean isLeftAssociative() {
        return true;
    }
}
