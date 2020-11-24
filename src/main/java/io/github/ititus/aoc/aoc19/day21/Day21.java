package io.github.ititus.aoc.aoc19.day21;

import io.github.ititus.aoc.common.InputProvider;
import io.github.ititus.math.number.BigIntegerMath;

import java.math.BigInteger;
import java.util.Arrays;

public class Day21 {

    public static void main(String[] args) {
        String input = InputProvider.readString(2019, 21);
        BigInteger[] memory =
                Arrays.stream(input.split(",")).map(String::strip).map(BigIntegerMath::of).toArray(BigInteger[]::new);

        SpringDroid sd = new SpringDroid(memory);

        // 1
        System.out.println("### 1 ###");
        int r1 = sd.runSpringScript(
                "NOT J J",
                "AND A J",
                "AND B J",
                "AND C J",
                "NOT J J",
                "AND D J",
                "WALK"
        );
        System.out.println(r1);

        // 2
        System.out.println("### 2 ###");
        int r2 = sd.runSpringScript(
                "NOT J J",
                "AND A J",
                "AND B J",
                "AND C J",
                "NOT J J",
                "AND D J",
                "OR E T",
                "OR H T",
                "AND T J",
                "RUN"
        );
        System.out.println(r2);
    }
}
