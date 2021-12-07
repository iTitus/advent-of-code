package io.github.ititus.aoc.aoc21.day07;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntUnaryOperator;

import java.util.Arrays;
import java.util.stream.IntStream;

@Aoc(year = 2021, day = 7)
public class Day07 implements AocSolution {

    IntList positions;

    private static int distance(int a, int b) {
        return Math.abs(b - a);
    }

    private static IntUnaryOperator simpleDistanceTo(int b) {
        return a -> distance(a, b);
    }

    private static IntUnaryOperator advancedDistanceTo(int b) {
        return a -> {
            int n = distance(a, b);
            return (n * (n + 1)) / 2;
        };
    }

    @Override
    public void executeTests() {
        AocInput input = new AocStringInput("16,1,2,0,4,2,7,1,2,14");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());
    }

    @Override
    public void readInput(AocInput input) {
        positions = Arrays.stream(input.readString().split(",".trim()))
                .map(String::strip)
                .mapToInt(Integer::parseInt)
                .sorted()
                .collect(IntArrayList::new, IntList::add, IntList::addAll);
    }

    @Override
    public Object part1() {
        // the cheapest will always be the median, because the median will minimize the mean absolute error
        int median;
        boolean medianRounded;
        if (positions.size() % 2 == 0) {
            int l = positions.getInt(positions.size() / 2 - 1);
            int r = positions.getInt(positions.size() / 2);
            if (l == r) {
                median = l;
                medianRounded = false;
            } else {
                int sum = l + r;
                median = sum / 2;
                medianRounded = sum % 2 != 0;
            }
        } else {
            median = positions.getInt(positions.size() / 2);
            medianRounded = false;
        }

        return IntStream.rangeClosed(median, medianRounded ? median + 1 : median)
                .mapToObj(Day07::simpleDistanceTo)
                .mapToInt(d -> positions.intStream().map(d).sum())
                .min().orElseThrow();
    }

    @Override
    public Object part2() {
        // the cheapest will always be the mean +- 1/2
        double mean = positions.intStream().average().orElseThrow();
        int start = (int) Math.floor(mean - 0.5);
        int end = (int) Math.ceil(mean + 0.5);
        return IntStream.rangeClosed(start, end)
                .mapToObj(Day07::advancedDistanceTo)
                .mapToInt(d -> positions.intStream().map(d).sum())
                .min().orElseThrow();
    }
}
