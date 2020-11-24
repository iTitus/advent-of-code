package io.github.ititus.aoc.aoc19.day20;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;

import java.util.List;

@Aoc(year = 2019, day = 20)
public final class Day20 implements AocSolution {

    private List<String> lines;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        lines = input.readAllLines();
    }

    @Override
    public Object part1() {
        PlutoMaze pm = new PlutoMaze(false, lines);
        return pm.findShortestPath("AA", "ZZ");
    }

    @Override
    public Object part2() {
        PlutoMaze pm = new PlutoMaze(true, lines);
        return pm.findShortestPath("AA", "ZZ");
    }
}
