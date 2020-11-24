package io.github.ititus.aoc.aoc19.day13;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;

import java.math.BigInteger;

@Aoc(year = 2019, day = 13)
public final class Day13 implements AocSolution {

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
        ArcadeMachine arcade1 = new ArcadeMachine(memory, false);
        arcade1.run();
        return arcade1.getCount(ArcadeTile.BLOCK);
    }

    @Override
    public Object part2() {
        ArcadeMachine arcade2 = new ArcadeMachine(memory, true);
        arcade2.run();
        return arcade2.getScore();
    }
}
