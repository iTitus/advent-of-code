package io.github.ititus.aoc.aoc20.day11;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;

import java.util.List;

@Aoc(year = 2020, day = 11)
public class Day11 implements AocSolution {

    private List<String> lines;

    @Override
    public void executeTests() {
        readInput(new AocStringInput("""
                L.LL.LL.LL
                LLLLLLL.LL
                L.L.L..L..
                LLLL.LL.LL
                L.LL.LL.LL
                L.LLLLL.LL
                ..L.L.....
                LLLLLLLLLL
                L.LLLLLL.L
                L.LLLLL.LL
                """));
        System.out.println("Part 1 (expected 37): " + part1());
        System.out.println("Part 2 (expected 26): " + part2());
    }

    @Override
    public void readInput(AocInput input) {
        lines = input.readAllLines();
    }

    @Override
    public Object part1() {
        SeatGrid grid = new SeatGrid(lines);
        return grid.run(false);
    }

    @Override
    public Object part2() {
        SeatGrid grid = new SeatGrid(lines);
        return grid.run(true);
    }
}
