package io.github.ititus.aoc.aoc20.day17;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;

import java.util.List;

@Aoc(year = 2020, day = 17)
public class Day17 implements AocSolution {

    private List<String> lines;

    @Override
    public void executeTests() {
        readInput(new AocStringInput("""
                .#.
                ..#
                ###
                """));
        System.out.println("Part 1 (expected 112): " + part1());
        System.out.println("Part 2 (expected 848): " + part2());
    }

    @Override
    public void readInput(AocInput input) {
        lines = input.readAllLines();
    }

    @Override
    public Object part1() {
        ConwayCube cc = new ConwayCube(lines, false);
        return cc.run(6);
    }

    @Override
    public Object part2() {
        ConwayCube cc = new ConwayCube(lines, true);
        return cc.run(6);
    }
}
