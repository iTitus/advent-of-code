package io.github.ititus.aoc.aoc19.day23;

import io.github.ititus.aoc.common.InputProvider;
import io.github.ititus.math.number.BigIntegerMath;

import java.math.BigInteger;
import java.util.Arrays;

public class Day23 {

    public static void main(String[] args) {
        String input = InputProvider.readString(2019, 23);
        BigInteger[] memory =
                Arrays.stream(input.split(",")).map(String::strip).map(BigIntegerMath::of).toArray(BigInteger[]::new);

        // 1
        /*System.out.println("### 1 ###");
        Network n1 = new Network(50, false, memory);
        n1.start();*/

        // 2
        System.out.println("### 2 ###");
        Network n2 = new Network(50, true, memory);
        n2.start();
    }
}
