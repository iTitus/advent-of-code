package io.github.ititus.aoc.aoc20.day07;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class BagRules {

    private final Map<String, Object2IntMap<String>> rules;

    private BagRules(Map<String, Object2IntMap<String>> rules) {
        this.rules = rules;
    }

    public static Collector<String, BagRulesAccumulator, BagRules> collector() {
        return Collector.of(
                BagRulesAccumulator::new,
                BagRulesAccumulator::accumulate,
                BagRulesAccumulator::combine,
                BagRulesAccumulator::finish
        );
    }

    public Set<String> getAncestors(String color) {
        Set<String> ancestors = rules.entrySet().stream()
                .filter(e -> e.getValue().containsKey(color))
                .map(Map.Entry::getKey)
                .collect(toSet());

        if (!ancestors.isEmpty()) {
            List<String> indirectAncestors = ancestors.stream()
                    .flatMap(c -> getAncestors(c).stream())
                    .collect(toList());
            ancestors.addAll(indirectAncestors);
        }

        return ancestors;
    }

    public Object2IntMap<String> getChildren(String color) {
        Object2IntMap<String> children = new Object2IntOpenHashMap<>();
        rules.get(color)
                .forEach((c, n) -> children.mergeInt(c, n, Integer::sum));

        if (!children.isEmpty()) {
            List<ObjectIntPair<String>> indirectChildren = children.object2IntEntrySet().stream()
                    .flatMap(
                            c -> getChildren(c.getKey()).object2IntEntrySet().stream()
                                    .map(p -> ObjectIntPair.of(p.getKey(), c.getIntValue() * p.getIntValue()))
                    )
                    .collect(toList());
            indirectChildren.forEach(c -> children.mergeInt(c.key(), c.valueInt(), Integer::sum));
        }

        return children;
    }

    public static class BagRulesAccumulator {

        private static final Pattern P1 = Pattern.compile(
                "^(?<color>[a-z]+ [a-z]+) bags contain (?<rest>.*)\\.$"
        );
        private static final Pattern P2 = Pattern.compile(", ", Pattern.LITERAL);
        private static final Pattern P3 = Pattern.compile(
                "^(?<amount>\\d+) (?<color>[a-z]+ [a-z]+) bags?$"
        );

        private final Map<String, Object2IntMap<String>> rules = new HashMap<>();

        private void accumulate(String line) {
            Matcher m = P1.matcher(line);
            if (!m.matches()) {
                throw new RuntimeException();
            }

            String inColor = m.group("color");
            Object2IntMap<String> out;

            String rest = m.group("rest");
            if ("no other bags".equals(rest)) {
                out = Object2IntMaps.emptyMap();
            } else {
                out = new Object2IntOpenHashMap<>();
                for (String s : P2.split(rest)) {
                    m = P3.matcher(s);
                    if (!m.matches()) {
                        throw new RuntimeException();
                    }

                    String outColor = m.group("color");
                    int amount = Integer.parseInt(m.group("amount"));
                    if (amount <= 0) {
                        throw new RuntimeException();
                    }
                    if (out.put(outColor, amount) != 0) {
                        throw new RuntimeException();
                    }
                }
            }

            if (rules.put(inColor, out) != null) {
                throw new RuntimeException();
            }
        }

        private BagRulesAccumulator combine(BagRulesAccumulator other) {
            throw new UnsupportedOperationException();
        }

        private BagRules finish() {
            return new BagRules(rules);
        }
    }
}
