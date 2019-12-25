package io.github.ititus.aoc.aoc19.day25;

import io.github.ititus.aoc.InputProvider;
import io.github.ititus.math.number.BigIntegerMath;

import java.math.BigInteger;
import java.util.Arrays;

public class Day25 {

    public static void main(String[] args) {
        String input = InputProvider.readString(2019, 25);
        BigInteger[] memory = Arrays.stream(input.split(",")).map(String::strip).map(BigIntegerMath::of).toArray(BigInteger[]::new);

        Droid d = new Droid(memory);
        d.run();
    }
}
