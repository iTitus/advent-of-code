package io.github.ititus.aoc.aoc20.day13;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;
import io.github.ititus.math.number.BigIntegerMath;
import io.github.ititus.math.number.ExtendedGdcBigIntegerResult;
import io.github.ititus.math.number.ExtendedGdcLongResult;
import io.github.ititus.math.number.JavaMath;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntList;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.ititus.math.number.BigIntegerMath.of;
import static java.math.BigInteger.ONE;

@Aoc(year = 2020, day = 13)
public class Day13 implements AocSolution {

    private int earliestDepartTime;
    private IntList busIds;

    @Override
    public void executeTests() {
        readInput(new AocStringInput("""
                939
                7,13,x,x,59,x,31,19
                """));
        System.out.println("Part 1 (expected 295): " + part1());
        System.out.println("Part 2 (expected 1068781): " + part2());

        readInput(new AocStringInput("""
                0
                17,x,13,19
                """));
        System.out.println("Part 2 (expected 3417): " + part2());

        readInput(new AocStringInput("""
                0
                67,7,59,61
                """));
        System.out.println("Part 2 (expected 754018): " + part2());

        readInput(new AocStringInput("""
                0
                67,x,7,59,61
                """));
        System.out.println("Part 2 (expected 779210): " + part2());

        readInput(new AocStringInput("""
                0
                67,7,x,59,61
                """));
        System.out.println("Part 2 (expected 1261476): " + part2());

        readInput(new AocStringInput("""
                0
                1789,37,47,1889
                """));
        System.out.println("Part 2 (expected 1202161486): " + part2());
    }

    @Override
    public void readInput(AocInput input) {
        List<String> lines = input.readAllLines();
        if (lines.size() != 2) {
            throw new RuntimeException();
        }

        earliestDepartTime = Integer.parseInt(lines.get(0));
        busIds = Arrays.stream(lines.get(1).split(","))
                .mapToInt(s -> s.equals("x") ? 0 : Integer.parseInt(s))
                .collect(IntArrayList::new, IntArrayList::add, IntArrayList::addAll);
    }

    @Override
    public Object part1() {
        int minId = 0, minArrivalTime = 0;
        for (IntIterator it = busIds.iterator(); it.hasNext(); ) {
            int id = it.nextInt();
            if (id <= 0) {
                continue;
            }

            int arrivingIn = earliestDepartTime % id;
            if (arrivingIn > 0) {
                arrivingIn = id - arrivingIn;
            }

            if (minId <= 0 || arrivingIn < minArrivalTime) {
                minId = id;
                minArrivalTime = arrivingIn;
            }
        }

        return minId * minArrivalTime;
    }

    @Override
    public Object part2() {
        return part2Long();
    }

    private BigInteger part2BigInt() {
        // https://en.wikipedia.org/wiki/Chinese_remainder_theorem#Using_the_existence_construction

        int i = 0;
        int id;
        do {
            id = busIds.getInt(i++);
        } while (id <= 0);

        BigInteger mod1 = of(id);
        BigInteger val1 = of(i - 1).negate().mod(mod1);

        for (; i < busIds.size(); i++) {
            id = busIds.getInt(i);
            if (id <= 0) {
                continue;
            }

            BigInteger mod2 = of(id);
            BigInteger val2 = of(i).negate().mod(mod2);

            ExtendedGdcBigIntegerResult result = BigIntegerMath.extendedGcd(mod1, mod2);
            if (!result.getGcd().equals(ONE)) {
                throw new RuntimeException("coprimality required");
            }

            BigInteger sol = val2.multiply(result.getBezoutCoeffA().multiply(mod1))
                    .add(val1.multiply(result.getBezoutCoeffB().multiply(mod2)));

            mod1 = mod1.multiply(mod2);
            val1 = sol.mod(mod1);
        }

        return val1;
    }

    private long part2Long() {
        // https://en.wikipedia.org/wiki/Chinese_remainder_theorem#Existence_(direct_construction)

        List<IntIntPair> entries = new ArrayList<>();
        for (int i = 0; i < busIds.size(); i++) {
            int id = busIds.getInt(i);
            if (id > 0) {
                entries.add(IntIntPair.of(Math.floorMod(-i, id), id));
            }
        }

        long N = entries
                .stream()
                .mapToLong(IntIntPair::rightInt)
                .reduce(1, (a, b) -> a * b);
        long result = entries
                .stream()
                .mapToLong(e -> {
                    int a_i = e.leftInt();
                    int n_i = e.rightInt();

                    long N_i = N / n_i;

                    ExtendedGdcLongResult res = JavaMath.extendedGcd(N_i, n_i);
                    if (res.getGcd() != 1) {
                        throw new RuntimeException("coprimality required");
                    }

                    long M_i = res.getBezoutCoeffA();

                    return a_i * M_i * N_i;
                })
                .sum();

        return Math.floorMod(result, N);
    }
}
