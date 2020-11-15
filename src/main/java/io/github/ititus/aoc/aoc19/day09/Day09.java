package io.github.ititus.aoc.aoc19.day09;

import io.github.ititus.aoc.InputProvider;
import io.github.ititus.aoc.aoc19.IntComputer;
import io.github.ititus.math.number.BigIntegerMath;

import java.math.BigInteger;
import java.util.Arrays;

public class Day09 {

    public static void main(String[] args) {
        String input = InputProvider.readString(2019, 9);
        BigInteger[] memory =
                Arrays.stream(input.split(",")).map(String::strip).map(BigIntegerMath::of).toArray(BigInteger[]::new);

        // 1
        System.out.println("### 1 ###");
        new IntComputer(() -> BigInteger.ONE, System.out::println, memory).run();

        // 2
        System.out.println("### 2 ###");
        new IntComputer(() -> BigInteger.TWO, System.out::println, memory).run();
    }
}
