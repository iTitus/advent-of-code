package io.github.ititus.aoc.aoc19.day13;

import io.github.ititus.aoc.common.InputProvider;
import io.github.ititus.math.number.BigIntegerMath;

import java.math.BigInteger;
import java.util.Arrays;

public class Day13 {

    public static void main(String[] args) {
        String input = InputProvider.readString(2019, 13);
        BigInteger[] memory =
                Arrays.stream(input.split(",")).map(String::strip).map(BigIntegerMath::of).toArray(BigInteger[]::new);

        // 1
        System.out.println("### 1 ###");
        ArcadeMachine arcade1 = new ArcadeMachine(memory, false);
        arcade1.run();
        System.out.println(arcade1.getCount(ArcadeTile.BLOCK));

        // 2
        System.out.println("### 2 ###");
        ArcadeMachine arcade2 = new ArcadeMachine(memory, true);
        arcade2.run();
        System.out.println(arcade2.getScore());
    }
}
