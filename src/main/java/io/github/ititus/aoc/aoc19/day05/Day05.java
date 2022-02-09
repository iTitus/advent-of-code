package io.github.ititus.aoc.aoc19.day05;

import io.github.ititus.aoc.aoc19.IntComputer;
import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.commons.math.number.BigIntegerMath;

import java.math.BigInteger;


@Aoc(year = 2019, day = 5)
public final class Day05 implements AocSolution {

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
        return IntComputer.runGetLastOutput(BigInteger.ONE, memory);
    }

    @Override
    public Object part2() {
        return IntComputer.runGetOutput(BigIntegerMath.of(5), memory);
    }
}
