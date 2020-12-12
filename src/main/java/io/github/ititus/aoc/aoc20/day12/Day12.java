package io.github.ititus.aoc.aoc20.day12;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;

import java.util.List;

@Aoc(year = 2020, day = 12)
public class Day12 implements AocSolution {

    private List<String> lines;

    @Override
    public void executeTests() {
        readInput(new AocStringInput("""
                F10
                N3
                F7
                R90
                F11
                """));
        System.out.println("Part 1 (expected 25): " + part1());
        System.out.println("Part 2 (expected 286): " + part2());
    }

    @Override
    public void readInput(AocInput input) {
        lines = input.readAllLines();
    }

    @Override
    public Object part1() {
        Ship ship = new Ship();
        ship.execute(lines, false);
        return ship.getManhattanDistance();
    }

    @Override
    public Object part2() {
        Ship ship = new Ship();
        ship.execute(lines, true);
        return ship.getManhattanDistance();
    }
}
