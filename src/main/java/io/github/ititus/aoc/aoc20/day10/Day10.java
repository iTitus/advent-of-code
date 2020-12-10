package io.github.ititus.aoc.aoc20.day10;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;
import it.unimi.dsi.fastutil.ints.IntList;

@Aoc(year = 2020, day = 10)
public class Day10 implements AocSolution {

    private IntList adapters;

    @Override
    public void executeTests() {
        readInput(new AocStringInput("""
                16
                10
                15
                5
                1
                11
                7
                19
                6
                12
                4
                """));
        System.out.println("Part 1 (expected 35): " + part1());
        System.out.println("Part 2 (expected 8): " + part2());

        readInput(new AocStringInput("""
                28
                33
                18
                42
                31
                14
                46
                20
                48
                47
                24
                23
                49
                45
                19
                38
                39
                11
                1
                32
                25
                35
                8
                17
                7
                9
                4
                2
                34
                10
                3
                """));
        System.out.println("Part 1 (expected 220): " + part1());
        System.out.println("Part 2 (expected 19208): " + part2());
    }

    @Override
    public void readInput(AocInput input) {
        adapters = input.readAllLinesAsInt();
        adapters.sort(null);

        int outletJoltage = 0;
        adapters.add(0, outletJoltage);

        int deviceJoltage = adapters.getInt(adapters.size() - 1) + 3;
        adapters.add(deviceJoltage);
    }

    @Override
    public Object part1() {
        int oneDiff = 0;
        int threeDiff = 0;

        for (int i = 1; i < adapters.size(); i++) {
            int diff = adapters.getInt(i) - adapters.getInt(i - 1);
            if (diff == 1) {
                oneDiff++;
            } else if (diff == 3) {
                threeDiff++;
            }
        }

        return oneDiff * threeDiff;
    }

    @Override
    public Object part2() {
        int length = adapters.size();
        // counts[i] = number of valid arrangements that end with adapter i
        long[] counts = new long[length];
        counts[0]++;

        outer:
        for (int i = 1; i < length; i++) {
            int adapter = adapters.getInt(i);
            for (int j = 1; j <= 3; j++) {
                if (i - j < 0 || adapter - adapters.getInt(i - j) > 3) {
                    continue outer;
                }
                counts[i] += counts[i - j];
            }
        }

        return counts[length - 1];
    }
}
