package io.github.ititus.aoc.aoc19.day11;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.math.vector.Vec2i;

import java.math.BigInteger;
import java.util.Map;

@Aoc(year = 2019, day = 11)
public final class Day11 implements AocSolution {

    private BigInteger[] memory;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        memory = input.readAsIntCodeMemory();
    }

    @Override
    public Object part1() {
        EmergencyHullPaintingRobot robot = new EmergencyHullPaintingRobot(memory, false);
        robot.run();
        return robot.getPainted().size();
    }

    @Override
    public Object part2() {
        EmergencyHullPaintingRobot robot = new EmergencyHullPaintingRobot(memory, true);
        robot.run();

        Map<Vec2i, HullColor> hull = robot.getHull();

        int minX = hull.keySet().stream().mapToInt(Vec2i::x).min().orElseThrow();
        int minY = hull.keySet().stream().mapToInt(Vec2i::y).min().orElseThrow();
        int maxX = hull.keySet().stream().mapToInt(Vec2i::x).max().orElseThrow();
        int maxY = hull.keySet().stream().mapToInt(Vec2i::y).max().orElseThrow();

        if (minX != 0 && minY != 0) {
            throw new RuntimeException();
        }

        int sizeX = maxX + 1;
        int sizeY = maxY + 1;

        StringBuilder b = new StringBuilder();
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                b.append(hull.getOrDefault(new Vec2i(x, y), HullColor.BLACK).getRenderChar());
            }
            b.append('\n');
        }
        return b.toString();
    }
}
