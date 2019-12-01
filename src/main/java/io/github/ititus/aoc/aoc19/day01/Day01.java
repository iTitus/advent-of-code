package io.github.ititus.aoc.aoc19.day01;

import io.github.ititus.aoc.InputProvider;

import java.util.List;

public class Day01 {

    public static void main(String[] args) {
        List<Integer> moduleMasses = InputProvider.readAllLinesAsInt(2019, 1);

        // 1
        int result1 = moduleMasses.stream()
                .mapToInt(Integer::intValue)
                .map(m -> m / 3)
                .map(m -> m - 2)
                .sum();
        System.out.println(result1);

        // 2
        int result2 = moduleMasses.stream()
                .mapToInt(Integer::intValue)
                .map(m -> {
                    int fuel = 0;
                    int massToBeFueled = m;
                    while (massToBeFueled >= 9) { // (8 / 3) - 2 = 0, (9 / 3) - 2 = 1
                        massToBeFueled = (massToBeFueled / 3) - 2;
                        fuel += massToBeFueled;
                    }
                    return fuel;
                })
                .sum();
        System.out.println(result2);
    }
}
