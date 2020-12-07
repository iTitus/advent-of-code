package io.github.ititus.aoc.aoc20.day07;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.FastUtilStreams;
import it.unimi.dsi.fastutil.objects.Object2IntMap;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

@Aoc(year = 2020, day = 7)
public class Day07 implements AocSolution {

    private static final String GOLD = "shiny gold";

    private BagRules rules;

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
                .collect(BagRules.collector());

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
                .collect(BagRules.collector());

        System.out.println("part2 (expected 126): " + part2());
    }

    @Override
    public void readInput(AocInput input) {
        try (Stream<String> stream = input.lines()) {
            rules = stream
                    .collect(BagRules.collector());
        }
    }

    @Override
    public Object part1() {
        Set<String> ancestors = rules.getAncestors(GOLD);
        return ancestors.size();
    }

    @Override
    public Object part2() {
        Object2IntMap<String> children = rules.getChildren(GOLD);
        return FastUtilStreams.stream(children.values())
                .sum();
    }
}
