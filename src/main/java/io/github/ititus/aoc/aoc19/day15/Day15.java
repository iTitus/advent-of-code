package io.github.ititus.aoc.aoc19.day15;

import io.github.ititus.aoc.InputProvider;
import io.github.ititus.math.number.BigIntegerMath;

import java.math.BigInteger;
import java.util.Arrays;

public class Day15 {

    public static void main(String[] args) {
        String input = InputProvider.readString(2019, 15);
        BigInteger[] memory = Arrays.stream(input.split(",")).map(String::strip).map(BigIntegerMath::of).toArray(BigInteger[]::new);

        RepairDroid droid = new RepairDroid(memory);
        droid.buildMap();
        droid.render();

        // 1
        System.out.println("### 1 ###");
        System.out.println(droid.getShortestPathToOxygen());

        // 2
        System.out.println("### 2 ###");
        System.out.println(droid.getLongestPathFromOxygen());
    }
}
