package io.github.ititus.aoc.aoc20.day19;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import static java.util.Arrays.stream;

public class OrRule implements Rule {

    private final Rule[] rules;

    private OrRule(Rule... rules) {
        this.rules = rules;
    }

    public static Rule of(String rule) {
        String[] split = rule.split("\\|");
        Rule[] rules = stream(split)
                .filter(s -> !s.isBlank())
                .map(CompoundRule::of)
                .toArray(Rule[]::new);
        if (rules.length == 0) {
            return null;
        } else if (rules.length == 1) {
            return rules[0];
        }
        return new OrRule(rules);
    }

    @Override
    public String toRegex(Int2ObjectMap<Rule> rules) {
        StringBuilder b = new StringBuilder("(?:");
        for (Rule rule : this.rules) {
            b.append(rule.toRegex(rules)).append("|");
        }
        b.setLength(b.length() - 1);
        return b.append(')').toString();
    }
}
