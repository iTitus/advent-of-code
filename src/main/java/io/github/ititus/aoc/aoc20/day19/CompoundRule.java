package io.github.ititus.aoc.aoc20.day19;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import static java.util.Arrays.stream;

public class CompoundRule implements Rule {

    private final int[] rules;

    private CompoundRule(int... rules) {
        this.rules = rules;
    }

    public static CompoundRule of(String rule) {
        String[] split = rule.split(" ");
        int[] rules = stream(split)
                .filter(s -> !s.isBlank())
                .mapToInt(Integer::parseInt)
                .toArray();
        return new CompoundRule(rules);
    }

    @Override
    public String toRegex(Int2ObjectMap<Rule> rules) {
        if (this.rules.length == 0) {
            return "";
        } else if (this.rules.length == 1) {
            return rules.get(this.rules[0]).toRegex(rules);
        }

        StringBuilder b = new StringBuilder();
        for (int rule : this.rules) {
            b.append(rules.get(rule).toRegex(rules));
        }
        return b.toString();
    }

    public int[] getRules() {
        return rules;
    }
}
