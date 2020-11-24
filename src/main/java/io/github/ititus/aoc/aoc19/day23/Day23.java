package io.github.ititus.aoc.aoc19.day23;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;

import java.math.BigInteger;

@Aoc(year = 2019, day = 23, skip = true)
public final class Day23 implements AocSolution {

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
        /*Network n = new Network(50, false, memory);
        n.start();*/
        return "Currently disabled";
    }

    @Override
    public Object part2() {
        Network n2 = new Network(50, true, memory);
        n2.start();
        return null;
    }
}
