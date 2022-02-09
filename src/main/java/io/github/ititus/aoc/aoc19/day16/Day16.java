package io.github.ititus.aoc.aoc19.day16;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.commons.math.time.DurationFormatter;
import io.github.ititus.commons.math.time.StopWatch;

import java.time.Duration;

@Aoc(year = 2019, day = 16, skip = true)
public final class Day16 implements AocSolution {

    private int[] digits;

    private static int[] splitDigits(String inputString) {
        return inputString.chars().map(c -> {
            int digit = c - '0';
            if (digit < 0 || digit > 9) {
                throw new IllegalArgumentException();
            }
            return digit;
        }).toArray();
    }

    private static int part1(int[] digits) {
        FlawedFrequencyTransmission fft = new FlawedFrequencyTransmission(1, false, digits);
        return fft.decode(100);
    }

    private static int part2(int[] digits) {
        FlawedFrequencyTransmission fft = new FlawedFrequencyTransmission(10_000, true, digits);
        return fft.decode(100);
    }

    // https://www.reddit.com/r/adventofcode/comments/ebb8w6/2019_day_16_part_three_a_fanfiction_by_askalski/
    private static int part3(int[] digits) {
        FlawedFrequencyTransmission fft = new FlawedFrequencyTransmission(10_000, true, digits);
        return fft.decode(287029238942L);
    }

    private static void test1(int expected, String inputString) {
        StopWatch s = StopWatch.createRunning();
        int actual = part1(splitDigits(inputString));
        Duration d = s.stop();
        if (actual != expected) {
            throw new RuntimeException("Part 1: expected=" + expected + " actual=" + actual);
        } else {
            System.out.println("Successfully passed test in " + DurationFormatter.formatMillis(d) + " for Part 1: " +
                    "output=" + actual);
        }
    }

    private static void test2(int expected, String inputString) {
        StopWatch s = StopWatch.createRunning();
        int actual = part2(splitDigits(inputString));
        Duration d = s.stop();
        if (actual != expected) {
            throw new RuntimeException("Part 2: expected=" + expected + " actual=" + actual);
        } else {
            System.out.println("Successfully passed test in " + DurationFormatter.formatMillis(d) + " for Part 2: " +
                    "output=" + actual);
        }
    }

    @Override
    public void executeTests() {
        test1(24176176, "80871224585914546619083218645595");
        test1(73745418, "19617804207202209144916044189917");
        test1(52432133, "69317163492948606335995924319873");

        test2(84462026, "03036732577212944063491565474664");
        test2(78725270, "02935109699940807407585447034323");
        test2(53553731, "03081770884921959731165446850517");
    }

    @Override
    public void readInput(AocInput input) {
        digits = splitDigits(input.readString().strip());
    }

    @Override
    public Object part1() {
        StopWatch s = StopWatch.createRunning();
        int r1 = part1(digits);
        Duration d = s.stop();
        System.out.println(DurationFormatter.formatMillis(d));
        return r1;
    }

    @Override
    public Object part2() {
        StopWatch s = StopWatch.createRunning();
        int r2 = part2(digits);
        Duration d = s.stop();
        String d2 = DurationFormatter.formatMillis(d);

        // 3
        s.start();
        int r3 = part3(digits);
        d = s.stop();
        System.out.println("Part 3: " + r3 + " (" + DurationFormatter.formatMillis(d) + ")");

        return r2 + " (" + d2 + ")";
    }
}
