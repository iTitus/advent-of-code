package io.github.ititus.aoc.aoc19.day04;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;

import java.util.Arrays;

@Aoc(year = 2019, day = 4)
public final class Day04 implements AocSolution {

    private int count1, count2;

    private static int getMinRepeatingCount(int password) {
        char[] chars = String.valueOf(password).toCharArray();
        if (chars.length != 6) {
            return -1;
        }

        int minRepeatingCount = -1;
        int curRepeatingCount = 1;
        char last = chars[0];
        for (int i = 1; i < chars.length; i++) {
            char c = chars[i];

            if (c < last) {
                return -1;
            } else if (c == last) {
                curRepeatingCount++;
            }

            if (c > last || i == chars.length - 1) {
                if (curRepeatingCount > 1 && (minRepeatingCount == -1 || curRepeatingCount < minRepeatingCount)) {
                    minRepeatingCount = curRepeatingCount;
                }
                curRepeatingCount = 1;
            }

            last = c;
        }

        return minRepeatingCount;
    }

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        int[] range = Arrays.stream(input.readString().split("-"))
                .map(String::strip)
                .mapToInt(Integer::parseInt)
                .toArray();
        if (range.length != 2) {
            throw new IllegalArgumentException();
        }

        int lower = range[0];
        int higher = range[1];

        for (int i = lower; i <= higher; i++) {
            int minRepeatingCount = getMinRepeatingCount(i);
            if (minRepeatingCount >= 2) {
                count1++;
                if (minRepeatingCount == 2) {
                    count2++;
                }
            }
        }
    }

    @Override
    public Object part1() {
        return count1;
    }

    @Override
    public Object part2() {
        return count2;
    }
}
