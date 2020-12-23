package io.github.ititus.aoc.aoc20.day23;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;

@Aoc(year = 2020, day = 23)
public class Day23 implements AocSolution {

    private String input;

    @Override
    public void executeTests() {
        readInput(new AocStringInput("389125467"));
        System.out.println("Part 1 (expected 67384529): " + part1());
        System.out.println("Part 1 (expected 149245887792): " + part2());
    }

    @Override
    public void readInput(AocInput input) {
        this.input = input.readString().trim();
    }

    @Override
    public Object part1() {
        Cups cups = new Cups(input, false);
        return cups.play(100);
    }

    @Override
    public Object part2() {
        Cups cups = new Cups(input, true);
        return cups.play(10_000_000);
    }
}
