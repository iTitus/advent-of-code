package io.github.ititus.aoc.aoc19.day25;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;

import java.math.BigInteger;

@Aoc(year = 2019, day = 25, skip = true)
public final class Day25 implements AocSolution {

    private BigInteger[] memory;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        memory = input.readAsIntCodeMemory();
    }

    @Override
    public Object part1() {
        Droid d = new Droid(memory);
        d.run();
        return null;
    }

    @Override
    public Object part2() {
        return "Free!";
    }
}
