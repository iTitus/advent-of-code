package io.github.ititus.aoc.aoc19.day01;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.FastUtilStreams;
import it.unimi.dsi.fastutil.ints.IntList;

@Aoc(year = 2019, day = 1)
public final class Day01 implements AocSolution {

    private IntList moduleMasses;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        moduleMasses = input.readAllLinesAsInt();
    }

    @Override
    public Object part1() {
        return FastUtilStreams.stream(moduleMasses)
                .map(m -> m / 3)
                .map(m -> m - 2)
                .sum();
    }

    @Override
    public Object part2() {
        return FastUtilStreams.stream(moduleMasses)
                .map(m -> {
                    int fuel = 0;
                    int massToBeFueled = m;
                    while (massToBeFueled >= 9) { // (8 / 3) - 2 = 0, (9 / 3) - 2 = 1
                        massToBeFueled = (massToBeFueled / 3) - 2;
                        fuel += massToBeFueled;
                    }
                    return fuel;
                })
                .sum();
    }
}
