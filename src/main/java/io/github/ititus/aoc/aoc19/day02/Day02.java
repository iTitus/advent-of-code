package io.github.ititus.aoc.aoc19.day02;

import io.github.ititus.aoc.InputProvider;
import io.github.ititus.aoc.aoc19.IntComputer;

import java.util.Arrays;

public class Day02 {

    public static void main(String[] args) {
        String input = InputProvider.readString(2019, 2);
        int[] memory = Arrays.stream(input.split(",")).map(String::strip).mapToInt(Integer::parseInt).toArray();

        // 1
        System.out.println(run(memory, 12, 2));

        // 2
        int result = 19690720;
        outer:
        for (int noun = 0; noun < 100; noun++) {
            for (int verb = 0; verb < 100; verb++) {
                if (result == run(memory, noun, verb)) {
                    System.out.println(100 * noun + verb);
                    break outer;
                }
            }
        }
    }

    private static int run(int[] memory, int noun, int verb) {
        memory = Arrays.copyOf(memory, memory.length);
        memory[1] = noun;
        memory[2] = verb;
        return new IntComputer(memory).run().intValueExact();
    }
}
