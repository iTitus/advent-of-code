package io.github.ititus.aoc.aoc20.day09;

import io.github.ititus.aoc.common.*;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;

@Aoc(year = 2020, day = 9)
public class Day09 implements AocSolution {

    private LongList numbers;
    private long savedPart1;

    private long checkPart1(int lookbehindLength) {
        LongSet behind = new LongOpenHashSet(numbers.subList(0, lookbehindLength));
        outer:
        for (int i = lookbehindLength; i < numbers.size(); i++) {
            long n = numbers.getLong(i);
            for (long s1 : behind) {
                long s2 = n - s1;
                if (s2 != s1 && behind.contains(s2)) {
                    behind.remove(numbers.getLong(i - lookbehindLength));
                    behind.add(n);
                    continue outer;
                }
            }
            return n;
        }
        return -1;
    }

    private long checkPart2(long invalid) {
        outer:
        for (int i = 0; i < numbers.size() - 1; i++) {
            long s1 = numbers.getLong(i);
            long required = invalid - s1;
            for (int j = i + 1; j < numbers.size(); j++) {
                long sNext = numbers.getLong(j);
                required -= sNext;
                if (required == 0) {
                    LongList validRange = numbers.subList(i, j + 1);
                    return FastUtilStreams.stream(validRange).min().orElseThrow()
                            + FastUtilStreams.stream(validRange).max().orElseThrow();
                } else if (required < 0) {
                    continue outer;
                }
            }
        }
        return -1;
    }

    @Override
    public void executeTests() {
        AocInput input = new AocStringInput("""
                35
                20
                15
                25
                47
                40
                62
                55
                65
                95
                102
                117
                150
                182
                127
                219
                299
                277
                309
                576
                """);
        readInput(input);
        System.out.println("Part 1 (expected 127): " + checkPart1(5));
        System.out.println("Part 2 (expected 62): " + checkPart2(127));
    }

    @Override
    public void readInput(AocInput input) {
        numbers = input.readAllLinesAsLong();
        if (numbers.longStream().anyMatch(n -> n <= 0)) {
            throw new RuntimeException("expected positive numbers");
        }
    }

    @Override
    public Object part1() {
        savedPart1 = checkPart1(25);
        return savedPart1;
    }

    @Override
    public Object part2() {
        return checkPart2(savedPart1);
    }
}
