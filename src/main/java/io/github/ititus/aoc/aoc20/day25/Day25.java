package io.github.ititus.aoc.aoc20.day25;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;
import io.github.ititus.math.number.BigIntegerMath;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;

@Aoc(year = 2020, day = 25)
public class Day25 implements AocSolution {

    private static final BigInteger MOD = BigIntegerMath.of(20201227);
    private static final BigInteger BASE = BigIntegerMath.of(7);

    private BigInteger cardPublicKey, doorPublicKey;

    private static BigInteger modPow(BigInteger base, BigInteger exponent) {
        return base.modPow(exponent, MOD);
    }

    private static int findLoopSize(BigInteger expectedPow) {
        BigInteger pow = ONE;
        for (int n = 0; ; n++) {
            if (pow.equals(expectedPow)) {
                return n;
            }

            pow = pow.multiply(BASE).mod(MOD);
        }
    }

    @Override
    public void executeTests() {
        readInput(new AocStringInput("""
                5764801
                17807724
                """));
        System.out.println("Part 1 (expected 14897079): " + part1());
    }

    @Override
    public void readInput(AocInput input) {
        String[] split = input.readString().split("\n");
        cardPublicKey = BigIntegerMath.of(split[0]);
        doorPublicKey = BigIntegerMath.of(split[1]);
    }

    @Override
    public Object part1() {
        int loopSize = findLoopSize(cardPublicKey);
        return modPow(doorPublicKey, BigIntegerMath.of(loopSize));
    }

    @Override
    public Object part2() {
        return null;
    }
}
