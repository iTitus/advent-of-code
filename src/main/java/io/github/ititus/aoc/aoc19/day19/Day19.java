package io.github.ititus.aoc.aoc19.day19;

import io.github.ititus.aoc.InputProvider;
import io.github.ititus.math.number.BigIntegerMath;
import io.github.ititus.math.vector.Vec2i;

import java.math.BigInteger;
import java.util.Arrays;

public class Day19 {

    public static void main(String[] args) {
        String input = InputProvider.readString(2019, 19);
        BigInteger[] memory = Arrays.stream(input.split(",")).map(String::strip).map(BigIntegerMath::of).toArray(BigInteger[]::new);

        TractorBeamScanner s = new TractorBeamScanner(memory);
        int no = s.getNumberOfAffectedTiles(50);
        System.out.println(no);

        Vec2i pos = s.getClosestPosForShip(100);
        int out = pos.getX() * 10_000 + pos.getY();
        System.out.println(out);
    }
}
