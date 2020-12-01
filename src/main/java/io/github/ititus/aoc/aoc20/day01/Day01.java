package io.github.ititus.aoc.aoc20.day01;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import it.unimi.dsi.fastutil.ints.IntList;

@Aoc(year = 2020, day = 1)
public class Day01 implements AocSolution {

    private IntList numbers;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        numbers = input.readAllLinesAsInt();
        numbers.sort(null);
    }

    private int interpolationSearch(int key) {
        int low = 0;
        int high = numbers.size() - 1;
        if (high < 0) {
            return -1;
        }

        while (true) {
            int lowKey = numbers.getInt(low);
            int highKey = numbers.getInt(high);

            if (key < lowKey || key > highKey) {
                return -1;
            } else if (key == lowKey) {
                return low;
            } else if (key == highKey) {
                return high;
            } else if (lowKey == highKey) {
                return -1;
            }

            int mid = low + ((key - lowKey) * (high - low) / (highKey - lowKey));
            int midKey = numbers.getInt(mid);

            if (midKey < key) {
                low = mid + 1;
            } else if (key < midKey) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
    }

    @Override
    public Object part1() {
        for (int i = 0; i < numbers.size(); i++) {
            int num1 = numbers.getInt(i);

            int num2 = 2020 - num1;
            int j = interpolationSearch(num2);

            if (j >= 0) {
                return num1 * num2;
            }
        }
        return null;
    }

    @Override
    public Object part2() {
        for (int i = 0; i < numbers.size(); i++) {
            int num1 = numbers.getInt(i);
            for (int j = i + 1; j < numbers.size(); j++) {
                int num2 = numbers.getInt(j);

                int num3 = 2020 - num1 - num2;
                int k = interpolationSearch(num3);

                if (k >= 0) {
                    return num1 * num2 * num3;
                }
            }
        }
        return null;
    }
}
