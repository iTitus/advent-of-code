package io.github.ititus.aoc.aoc19.day03;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.math.vector.Vec2i;

import java.util.*;

@Aoc(year = 2019, day = 3)
public final class Day03 implements AocSolution {

    Map<Vec2i, Integer> wire1Path, wire2Path;

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

    @Override
    public void executeTests() {
        parseInput(List.of("R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83"));
        System.out.println("Test1.1: " + part1());
        System.out.println("Test1.2: " + part2());

        parseInput(List.of("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"));
        System.out.println("Test2.1: " + part1());
        System.out.println("Test2.2: " + part2());
    }

    @Override
    public void readInput(AocInput input) {
        parseInput(input.readAllLines());
    }

    @Override
    public Object part1() {
        Set<Vec2i> intersect = getIntersection(wire1Path, wire2Path).keySet();
        Vec2i closestIntersection = Collections.min(intersect, Comparator.comparingInt(Vec2i::manhattanDistance));

        return closestIntersection + " with distance " + closestIntersection.manhattanDistance();
    }

    @Override
    public Object part2() {
        Map<Vec2i, Integer> intersect = getIntersection(wire1Path, wire2Path);
        Map.Entry<Vec2i, Integer> closestIntersection = Collections.min(intersect.entrySet(),
                Comparator.comparingInt(Map.Entry::getValue));

        return closestIntersection.getKey() + " with combined steps " + closestIntersection.getValue();
    }

    private void parseInput(List<String> lines) {
        if (lines.size() != 2) {
            throw new IllegalArgumentException();
        }

        String[] wire1Commands = lines.get(0).split(",");
        String[] wire2Commands = lines.get(1).split(",");

        wire1Path = getWirePath(wire1Commands);
        wire2Path = getWirePath(wire2Commands);
    }
}
