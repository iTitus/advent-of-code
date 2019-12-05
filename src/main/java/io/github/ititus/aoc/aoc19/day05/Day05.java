package io.github.ititus.aoc.aoc19.day05;

import io.github.ititus.aoc.InputProvider;
import io.github.ititus.aoc.aoc19.IntComputer;

import java.util.Arrays;


public class Day05 {

    public static void main(String[] args) {
        String input = InputProvider.readString(2019, 5);
        int[] memory = Arrays.stream(input.split(",")).map(String::strip).mapToInt(Integer::parseInt).toArray();

        // 1
        System.out.println("### 1 ###");
        run(memory, 1);

        // 2
        System.out.println("### 2 ###");
        run(memory, 5);
    }

    private static void run(int[] memory, int input) {
        new IntComputer(() -> input, System.out::println, memory).run();
    }
}
