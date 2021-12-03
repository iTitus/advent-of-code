package io.github.ititus.aoc.aoc21.day03;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntList;

@Aoc(year = 2021, day = 3)
public class Day03 implements AocSolution {

    private int bitCount;
    private int fullMask;
    private IntList numbers;

    private static boolean mostCommon(IntList numbers, int position) {
        int mask = 1 << position;
        int ones = 0;
        for (IntIterator it = numbers.intIterator(); it.hasNext(); ) {
            int n = it.nextInt();
            if ((n & mask) != 0) {
                ones++;
            }
        }

        if (ones > numbers.size() / 2) {
            return true;
        }

        return ones == numbers.size() / 2 && numbers.size() % 2 == 0;
    }

    @Override
    public void executeTests() {
        AocInput in = new AocStringInput("""
                00100
                11110
                10110
                10111
                10101
                01111
                00111
                11100
                10000
                11001
                00010
                01010""");
        readInput(in);
        System.out.println(part1());
        System.out.println(part2());
    }

    @Override
    public void readInput(AocInput input) {
        numbers = input.lines()
                .mapToInt(l -> Integer.parseInt(l, 2))
                .collect(IntArrayList::new, IntList::add, IntList::addAll);
        bitCount = input.lines()
                .limit(1)
                .map(String::strip)
                .mapToInt(String::length)
                .findAny().orElseThrow();

        fullMask = 0;
        for (int i = 0; i < bitCount; i++) {
            fullMask |= (1 << i);
        }
    }

    private int findLifeSupportRating(boolean invert) {
        if (numbers.isEmpty()) {
            throw new RuntimeException();
        } else if (numbers.size() == 1) {
            return numbers.getInt(0);
        }

        IntList current = new IntArrayList(numbers);
        for (int i = bitCount - 1; i >= 0; i--) {
            int mask = 1 << i;
            boolean bit = invert != mostCommon(current, i);
            current.removeIf(n -> ((n & mask) == 0) == bit);

            if (current.isEmpty()) {
                throw new RuntimeException();
            } else if (current.size() == 1) {
                return current.getInt(0);
            }
        }

        throw new RuntimeException();
    }

    @Override
    public Object part1() {
        int gammaRate = 0;
        for (int i = 0; i < bitCount; i++) {
            if (mostCommon(numbers, i)) {
                gammaRate |= 1 << i;
            }
        }

        int epsilonRate = ~gammaRate & fullMask;
        return gammaRate * epsilonRate;
    }

    @Override
    public Object part2() {
        return findLifeSupportRating(false) * findLifeSupportRating(true);
    }
}
