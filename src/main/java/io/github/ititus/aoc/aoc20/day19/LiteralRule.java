package io.github.ititus.aoc.aoc20.day19;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public class LiteralRule implements Rule {

    private final String literal;

    public LiteralRule(String literal) {
        this.literal = literal;
    }

    @Override
    public String toRegex(Int2ObjectMap<Rule> rules) {
        return literal;
    }
}
