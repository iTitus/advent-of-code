package io.github.ititus.aoc.aoc19.day01;

import io.github.ititus.aoc.FastUtilStreams;
import io.github.ititus.aoc.InputProvider;
import it.unimi.dsi.fastutil.ints.IntList;

public class Day01 {

    public static void main(String[] args) {
        IntList moduleMasses = InputProvider.readAllLinesAsInt(2019, 1);

        // 1
        System.out.println("### 1 ###");
        int result1 = FastUtilStreams.stream(moduleMasses)
                .map(m -> m / 3)
                .map(m -> m - 2)
                .sum();
        System.out.println(result1);

        // 2
        System.out.println("### 2 ###");
        int result2 = FastUtilStreams.stream(moduleMasses)
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
