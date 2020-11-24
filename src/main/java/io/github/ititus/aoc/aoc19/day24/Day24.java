package io.github.ititus.aoc.aoc19.day24;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;

import java.util.List;

@Aoc(year = 2019, day = 24)
public final class Day24 implements AocSolution {

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
        ErisBugs eb1 = new ErisBugs(false, lines);
        return eb1.stepUntilRepeat();
    }

    @Override
    public Object part2() {
        ErisBugs eb2 = new ErisBugs(true, lines);
        return eb2.step(200);
    }
}
