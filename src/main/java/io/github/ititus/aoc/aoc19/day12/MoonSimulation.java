package io.github.ititus.aoc.aoc19.day12;

import io.github.ititus.math.number.BigIntegerMath;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MoonSimulation {

    private final List<Moon> moons;

    public MoonSimulation(List<Moon> moons) {
        this.moons = moons.stream().map(Moon::new).collect(Collectors.toList());
    }

    public void step() {
        moons.forEach(m -> moons.forEach(m::addGravity));
        moons.forEach(Moon::move);
    }

    public void simulate(int steps) {
        IntStream.range(0, steps).forEach(i -> step());
    }

    public int getTotalEnergy() {
        return moons.stream().mapToInt(Moon::getTotalEnergy).sum();
    }

    public BigInteger simulateUntilRepeat() {
        BigInteger rx = null;
        BigInteger ry = null;
        BigInteger rz = null;

        BigInteger step = BigInteger.ZERO;
        while (true) {
            step();
            step = step.add(BigInteger.ONE);

            boolean allFound = true;
            if (rx == null) {
                allFound = false;
                if (moons.stream().allMatch(Moon::isInitialX)) {
                    rx = step;
                }
            }
            if (ry == null) {
                allFound = false;
                if (moons.stream().allMatch(Moon::isInitialY)) {
                    ry = step;
                }
            }
            if (rz == null) {
                allFound = false;
                if (moons.stream().allMatch(Moon::isInitialZ)) {
                    rz = step;
                }
            }

            if (allFound) {
                break;
            }
        }

        return BigIntegerMath.lcm(BigIntegerMath.lcm(rx, ry), rz);
    }
}
