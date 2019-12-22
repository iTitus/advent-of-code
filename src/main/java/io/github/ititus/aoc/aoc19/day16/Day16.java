package io.github.ititus.aoc.aoc19.day16;

import io.github.ititus.aoc.InputProvider;
import io.github.ititus.math.time.DurationFormatter;
import io.github.ititus.math.time.StopWatch;

import java.time.Duration;

public class Day16 {

    public static void main(String[] args) {
        String inputString = InputProvider.readString(2019, 16).strip();

        test();

        // 1
        System.out.println("### 1 ###");
        StopWatch s = StopWatch.createRunning();
        int r1 = part1(inputString);
        Duration d = s.stop();
        System.out.println(r1);
        System.out.println(DurationFormatter.formatMillis(d));

        // 2
        System.out.println("### 2 ###");
        s.start();
        int r2 = part2(inputString);
        d = s.stop();
        System.out.println(r2);
        System.out.println(DurationFormatter.formatMillis(d));

        // 3
        System.out.println("### 3 ###");
        s.start();
        int r3 = part3(inputString);
        d = s.stop();
        System.out.println(r3);
        System.out.println(DurationFormatter.formatMillis(d));

    }

    private static int[] splitDigits(String inputString) {
        return inputString.chars().map(c -> {
            int digit = c - '0';
            if (digit < 0 || digit > 9) {
                throw new IllegalArgumentException();
            }
            return digit;
        }).toArray();
    }

    private static int part1(String inputString) {
        FlawedFrequencyTransmission fft = new FlawedFrequencyTransmission(1, false, splitDigits(inputString));
        return fft.decode(100);
    }

    private static int part2(String inputString) {
        FlawedFrequencyTransmission fft = new FlawedFrequencyTransmission(10_000, true, splitDigits(inputString));
        return fft.decode(100);
    }

    // https://www.reddit.com/r/adventofcode/comments/ebb8w6/2019_day_16_part_three_a_fanfiction_by_askalski/
    private static int part3(String inputString) {
        FlawedFrequencyTransmission fft = new FlawedFrequencyTransmission(10_000, true, splitDigits(inputString));
        return fft.decode(287029238942L);
    }

    private static void test() {
        test1(24176176, "80871224585914546619083218645595");
        test1(73745418, "19617804207202209144916044189917");
        test1(52432133, "69317163492948606335995924319873");

        test2(84462026, "03036732577212944063491565474664");
        test2(78725270, "02935109699940807407585447034323");
        test2(53553731, "03081770884921959731165446850517");
    }

    private static void test1(int expected, String inputString) {
        StopWatch s = StopWatch.createRunning();
        int actual = part1(inputString);
        Duration d = s.stop();
        if (actual != expected) {
            throw new RuntimeException("Part 1: expected=" + expected + " actual=" + actual);
        } else {
            System.out.println("Successfully passed test in " + DurationFormatter.formatMillis(d) + " for Part 1: output=" + actual);
        }
    }

    private static void test2(int expected, String inputString) {
        StopWatch s = StopWatch.createRunning();
        int actual = part2(inputString);
        Duration d = s.stop();
        if (actual != expected) {
            throw new RuntimeException("Part 2: expected=" + expected + " actual=" + actual);
        } else {
            System.out.println("Successfully passed test in " + DurationFormatter.formatMillis(d) + " for Part 2: output=" + actual);
        }
    }
}
