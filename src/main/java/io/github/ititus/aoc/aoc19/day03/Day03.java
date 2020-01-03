package io.github.ititus.aoc.aoc19.day03;

import io.github.ititus.aoc.InputProvider;
import io.github.ititus.math.vector.Vec2i;

import java.util.*;

public class Day03 {

    public static void main(String[] args) {
        // doExercises(List.of("R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83"));
        // doExercises(List.of("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"));
        doExercises(InputProvider.readAllLines(2019, 3));
    }

    private static void doExercises(List<String> input) {
        if (input.size() != 2) {
            throw new IllegalArgumentException();
        }

        String[] wire1Commands = input.get(0).split(",");
        String[] wire2Commands = input.get(1).split(",");

        calculate(wire1Commands, wire2Commands);
    }

    private static void calculate(String[] wire1Commands, String[] wire2Commands) {
        Map<Vec2i, Integer> wire1Path = getWirePath(wire1Commands);
        Map<Vec2i, Integer> wire2Path = getWirePath(wire2Commands);

        // 1
        System.out.println("### 1 ###");
        Set<Vec2i> intersect1 = getIntersection(wire1Path, wire2Path).keySet();
        Vec2i closestIntersection1 = Collections.min(intersect1, Comparator.comparingInt(Vec2i::manhattanDistance));
        System.out.println(closestIntersection1 + " with distance " + closestIntersection1.manhattanDistance());

        // 2
        System.out.println("### 2 ###");
        Map<Vec2i, Integer> intersect2 = getIntersection(wire1Path, wire2Path);
        Map.Entry<Vec2i, Integer> closestIntersection2 = Collections.min(intersect2.entrySet(), Comparator.comparingInt(Map.Entry::getValue));
        System.out.println(closestIntersection2.getKey() + " with combined steps " + closestIntersection2.getValue());
    }

    private static Map<Vec2i, Integer> getWirePath(String[] commands) {
        Map<Vec2i, Integer> wire2Pos = new HashMap<>();
        Vec2i pos = new Vec2i();
        int steps = 0;
        for (String cmd : commands) {
            List<Vec2i> newPos = move(pos, cmd);
            for (Vec2i p : newPos) {
                wire2Pos.putIfAbsent(p, ++steps);
            }
            pos = newPos.get(newPos.size() - 1);
        }
        return wire2Pos;
    }

    private static Map<Vec2i, Integer> getIntersection(Map<Vec2i, Integer> a, Map<Vec2i, Integer> b) {
        Map<Vec2i, Integer> intersect = new HashMap<>(a);
        intersect.keySet().retainAll(b.keySet());
        b.forEach((posB, stepsB) -> intersect.computeIfPresent(posB, (posA, stepsA) -> stepsA + stepsB));
        return intersect;
    }

    /**
     * Moves the starting position by the given command.
     *
     * @param pos     starting position
     * @param command "&lt;direction&gt;&lt;number&gt;"
     * @return path from this position to the target position
     */
    private static List<Vec2i> move(Vec2i pos, String command) {
        char c = command.charAt(0);
        int dirX = 0, dirY = 0;

        if (c == 'R') {
            dirX = 1;
        } else if (c == 'L') {
            dirX = -1;
        } else if (c == 'U') {
            dirY = 1;
        } else if (c == 'D') {
            dirY = -1;
        } else {
            throw new IllegalArgumentException();
        }

        List<Vec2i> path = new ArrayList<>();
        int amount = Integer.parseInt(command.substring(1));
        for (int i = 1; i <= amount; i++) {
            path.add(new Vec2i(pos.getX() + i * dirX, pos.getY() + i * dirY));
        }

        return path;
    }
}
