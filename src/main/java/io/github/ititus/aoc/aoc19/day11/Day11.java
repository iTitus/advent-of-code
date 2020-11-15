package io.github.ititus.aoc.aoc19.day11;

import io.github.ititus.aoc.InputProvider;
import io.github.ititus.math.number.BigIntegerMath;
import io.github.ititus.math.vector.Vec2i;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;

public class Day11 {

    public static void main(String[] args) {
        String input = InputProvider.readString(2019, 11);
        BigInteger[] memory =
                Arrays.stream(input.split(",")).map(String::strip).map(BigIntegerMath::of).toArray(BigInteger[]::new);

        // 1
        System.out.println("### 1 ###");
        EmergencyHullPaintingRobot robot1 = new EmergencyHullPaintingRobot(memory, false);
        robot1.run();
        System.out.println(robot1.getPainted().size());

        // 2
        System.out.println("### 2 ###");
        EmergencyHullPaintingRobot robot2 = new EmergencyHullPaintingRobot(memory, true);
        robot2.run();

        Map<Vec2i, HullColor> hull = robot2.getHull();

        int minX = hull.keySet().stream().mapToInt(Vec2i::getX).min().orElseThrow();
        int minY = hull.keySet().stream().mapToInt(Vec2i::getY).min().orElseThrow();
        int maxX = hull.keySet().stream().mapToInt(Vec2i::getX).max().orElseThrow();
        int maxY = hull.keySet().stream().mapToInt(Vec2i::getY).max().orElseThrow();

        if (minX != 0 && minY != 0) {
            throw new RuntimeException();
        }

        int sizeX = maxX + 1;
        int sizeY = maxY + 1;

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                System.out.print(hull.getOrDefault(new Vec2i(x, y), HullColor.BLACK).getRenderChar());
            }
            System.out.println();
        }
    }
}
