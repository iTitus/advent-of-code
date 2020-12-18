package io.github.ititus.aoc.aoc20.day18;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;

import java.util.List;

@Aoc(year = 2020, day = 18)
public class Day18 implements AocSolution {

    private List<String> lines;

    @Override
    public void executeTests() {
        readInput(new AocStringInput("1 + 2 * 3 + 4 * 5 + 6"));
        System.out.println("Part 1 (expected 71): " + part1());

        readInput(new AocStringInput("2 * 3 + (4 * 5)"));
        System.out.println("Part 1 (expected 26): " + part1());

        readInput(new AocStringInput("5 + (8 * 3 + 9 + 3 * 4 * 3)"));
        System.out.println("Part 1 (expected 437): " + part1());

        readInput(new AocStringInput("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))"));
        System.out.println("Part 1 (expected 12240): " + part1());

        readInput(new AocStringInput("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"));
        System.out.println("Part 1 (expected 13632): " + part1());

        readInput(new AocStringInput("1 + 2 * 3 + 4 * 5 + 6"));
        System.out.println("Part 2 (expected 231): " + part2());

        readInput(new AocStringInput("1 + (2 * 3) + (4 * (5 + 6))"));
        System.out.println("Part 2 (expected 51): " + part2());

        readInput(new AocStringInput("2 * 3 + (4 * 5)"));
        System.out.println("Part 2 (expected 46): " + part2());

        readInput(new AocStringInput("5 + (8 * 3 + 9 + 3 * 4 * 3)"));
        System.out.println("Part 2 (expected 1445): " + part2());

        readInput(new AocStringInput("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))"));
        System.out.println("Part 2 (expected 669060): " + part2());

        readInput(new AocStringInput("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"));
        System.out.println("Part 2 (expected 23340): " + part2());
    }

    @Override
    public void readInput(AocInput input) {
        lines = input.readAllLines();
    }

    @Override
    public Object part1() {
        return lines.stream()
                .mapToLong(s -> ExpressionEvaluator.evaluate(s, false))
                .sum();
    }

    @Override
    public Object part2() {
        return lines.stream()
                .mapToLong(s -> ExpressionEvaluator.evaluate(s, true))
                .sum();
    }
}
