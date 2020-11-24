package io.github.ititus.aoc.aoc19.day02;

import io.github.ititus.aoc.aoc19.IntComputer;
import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.math.number.BigIntegerMath;

import java.math.BigInteger;
import java.util.Arrays;

@Aoc(year = 2019, day = 2)
public final class Day02 implements AocSolution {

    private BigInteger[] memory;

    private static BigInteger run(BigInteger[] memory, int noun, int verb) {
        memory = Arrays.copyOf(memory, memory.length);
        memory[1] = BigIntegerMath.of(noun);
        memory[2] = BigIntegerMath.of(verb);
        return IntComputer.runGetFirst(memory);
    }

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        memory = input.readAsIntCodeMemory();
    }

    @Override
    public Object part1() {
        return run(memory, 12, 2);
    }

    @Override
    public Object part2() {
        BigInteger result = BigIntegerMath.of(19690720);
        for (int noun = 0; noun < 100; noun++) {
            for (int verb = 0; verb < 100; verb++) {
                if (result.equals(run(memory, noun, verb))) {
                    return 100 * noun + verb;
                }
            }
        }

        return null;
    }
}
