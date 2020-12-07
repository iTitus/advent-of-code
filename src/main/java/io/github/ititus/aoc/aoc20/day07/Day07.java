package io.github.ititus.aoc.aoc20.day07;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.data.Pair;
import io.github.ititus.math.number.BigIntegerMath;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Aoc(year = 2020, day = 7)
public class Day07 implements AocSolution {

    private static final BagColor GOLD = BagColor.of("shiny gold");

    private List<BagRule> rules;

    private static Set<BagColor> getAncestors(List<BagRule> rules, BagColor color) {
        Set<BagColor> ancestors = rules.stream()
                .filter(r -> r.outputContains(color))
                .map(BagRule::getIn)
                .collect(toSet());

        ancestors.addAll(
                ancestors.stream()
                        .flatMap(c -> getAncestors(rules, c).stream())
                        .collect(toList())
        );

        return ancestors;
    }

    private static Map<BagColor, BigInteger> getChildren(List<BagRule> rules, BagColor color) {
        Map<BagColor, BigInteger> children = new HashMap<>();
        rules.stream()
                .filter(r -> r.inputMatches(color))
                .flatMap(r -> r.getOut().object2IntEntrySet().stream())
                .map(e -> Pair.of(e.getKey(), BigIntegerMath.of(e.getIntValue())))
                .forEach(p -> children.merge(p.getA(), p.getB(), BigInteger::add));

        List<Pair<BagColor, BigInteger>> indirectChildren = children.entrySet().stream()
                .flatMap(
                        c -> getChildren(rules, c.getKey()).entrySet().stream()
                                .map(p -> Pair.of(p.getKey(), c.getValue().multiply(p.getValue())))
                )
                .collect(toList());
        indirectChildren.forEach(c -> children.merge(c.getA(), c.getB(), BigInteger::add));

        return children;
    }

    @Override
    public void executeTests() {
        String lines = """
                light red bags contain 1 bright white bag, 2 muted yellow bags.
                dark orange bags contain 3 bright white bags, 4 muted yellow bags.
                bright white bags contain 1 shiny gold bag.
                muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
                shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
                dark olive bags contain 3 faded blue bags, 4 dotted black bags.
                vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
                faded blue bags contain no other bags.
                dotted black bags contain no other bags.
                """;
        rules = Arrays.stream(lines.split("\n"))
                .map(BagRule::parse)
                .collect(toList());

        System.out.println("part2 (expected 4): " + part1());
        System.out.println("part2 (expected 32): " + part2());

        lines = """
                shiny gold bags contain 2 dark red bags.
                dark red bags contain 2 dark orange bags.
                dark orange bags contain 2 dark yellow bags.
                dark yellow bags contain 2 dark green bags.
                dark green bags contain 2 dark blue bags.
                dark blue bags contain 2 dark violet bags.
                dark violet bags contain no other bags.
                """;
        rules = Arrays.stream(lines.split("\n"))
                .map(BagRule::parse)
                .collect(toList());

        System.out.println("part2 (expected 126): " + part2());
    }

    @Override
    public void readInput(AocInput input) {
        try (Stream<String> stream = input.lines()) {
            rules = stream
                    .map(BagRule::parse)
                    .collect(toList());
        }
    }

    @Override
    public Object part1() {
        Set<BagColor> ancestors = getAncestors(rules, GOLD);
        return ancestors.size();
    }

    @Override
    public Object part2() {
        Map<BagColor, BigInteger> children = getChildren(rules, GOLD);
        return children.values().stream()
                .reduce(BigInteger.ZERO, BigInteger::add);
    }
}
