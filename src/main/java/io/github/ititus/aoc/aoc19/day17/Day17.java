package io.github.ititus.aoc.aoc19.day17;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;

@Aoc(year = 2019, day = 17, skip = true)
public final class Day17 implements AocSolution {

    private AsciiRobot robot;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        robot = new AsciiRobot(input.readAsIntCodeMemory());
        robot.run();
    }

    @Override
    public Object part1() {
        return robot.sumAlignmentParameters();
    }

    @Override
    public Object part2() {
        return robot.getDustCleaned();
    }
}
