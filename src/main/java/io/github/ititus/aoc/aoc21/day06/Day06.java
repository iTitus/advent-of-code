package io.github.ititus.aoc.aoc21.day06;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.Arrays;

@Aoc(year = 2021, day = 6)
public class Day06 implements AocSolution {

    IntList initial;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        initial = Arrays.stream(input.readString().split(","))
                .map(String::strip)
                .mapToInt(Integer::parseInt)
                .collect(IntArrayList::new, IntList::add, IntList::addAll);
    }

    @Override
    public Object part1() {
        int[] fishes = new int[9];
        for (IntIterator it = initial.intIterator(); it.hasNext(); ) {
            fishes[it.nextInt()]++;
        }

        for (int i = 0; i < 80; i++) {
            int[] newFishes = new int[9];
            System.arraycopy(fishes, 1, newFishes, 0, fishes.length - 1);

            newFishes[8] = fishes[0];
            newFishes[6] += fishes[0];

            fishes = newFishes;
        }

        return Arrays.stream(fishes)
                .sum();
    }

    @Override
    public Object part2() {
        long[] fishes = new long[9];
        for (IntIterator it = initial.intIterator(); it.hasNext(); ) {
            fishes[it.nextInt()]++;
        }

        for (int i = 0; i < 256; i++) {
            long[] newFishes = new long[9];
            System.arraycopy(fishes, 1, newFishes, 0, fishes.length - 1);

            newFishes[8] = fishes[0];
            newFishes[6] += fishes[0];

            fishes = newFishes;
        }

        return Arrays.stream(fishes)
                .sum();
    }
}
