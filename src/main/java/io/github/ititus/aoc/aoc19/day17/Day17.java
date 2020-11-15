package io.github.ititus.aoc.aoc19.day17;

import io.github.ititus.aoc.InputProvider;
import io.github.ititus.math.number.BigIntegerMath;

import java.math.BigInteger;
import java.util.Arrays;

public class Day17 {

    public static void main(String[] args) {
        String input = InputProvider.readString(2019, 17);
        BigInteger[] memory =
                Arrays.stream(input.split(",")).map(String::strip).map(BigIntegerMath::of).toArray(BigInteger[]::new);

        AsciiRobot ar = new AsciiRobot(memory);
        ar.run();

        // 1
        System.out.println("### 1 ###");
        System.out.println(ar.sumAlignmentParameters());

        // 2
        System.out.println("### 2 ###");
        System.out.println(ar.getDustCleaned());
    }
}
