package io.github.ititus.aoc.aoc21.day01;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

@Aoc(year = 2021, day = 1)
public class Day01 implements AocSolution {

    private IntList input;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        this.input = input.readAllLinesAsInt();
    }

    @Override
    public Object part1() {
        IntList differences = new IntArrayList(input.size() - 1);
        for (int i = 1; i < input.size(); i++) {
            int prev = input.getInt(i - 1);
            int cur = input.getInt(i);
            differences.add(cur - prev);
        }

        return differences.intStream().filter(i -> i > 0).count();
    }

    @Override
    public Object part2() {
        IntList differences = new IntArrayList(input.size() - 3);
        for (int i = 3; i < input.size(); i++) {
            int prev = input.getInt(i - 3);
            int cur = input.getInt(i);
            differences.add(cur - prev);
        }

        return differences.intStream().filter(i -> i > 0).count();
    }
}
