package io.github.ititus.aoc.aoc19.day03;

import io.github.ititus.aoc.InputProvider;

import java.util.*;

public class Day03 {

    public static void main(String[] args) {
        doExercises(List.of("R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83"));
        doExercises(List.of("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"));
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
        Map<Pos, Integer> wire1Path = getWirePath(wire1Commands);
        Map<Pos, Integer> wire2Path = getWirePath(wire2Commands);

        // 1
        Set<Pos> intersect1 = getIntersection(wire1Path, wire2Path).keySet();
        Pos closestIntersection1 = Collections.min(intersect1, Comparator.comparingInt(Pos::manhattanDistance));
        System.out.println(closestIntersection1 + " with distance " + closestIntersection1.manhattanDistance());

        // 2
        Map<Pos, Integer> intersect2 = getIntersection(wire1Path, wire2Path);
        Map.Entry<Pos, Integer> closestIntersection2 = Collections.min(intersect2.entrySet(), Comparator.comparingInt(Map.Entry::getValue));
        System.out.println(closestIntersection2.getKey() + " with combined steps " + closestIntersection2.getValue());
    }

    private static Map<Pos, Integer> getWirePath(String[] commands) {
        Map<Pos, Integer> wire2Pos = new HashMap<>();
        Pos pos = new Pos();
        int steps = 0;
        for (String cmd : commands) {
            List<Pos> newPos = pos.move(cmd);
            for (Pos p : newPos) {
                wire2Pos.putIfAbsent(p, ++steps);
            }
            pos = newPos.get(newPos.size() - 1);
        }
        return wire2Pos;
    }

    private static Map<Pos, Integer> getIntersection(Map<Pos, Integer> a, Map<Pos, Integer> b) {
        Map<Pos, Integer> intersect = new HashMap<>(a);
        intersect.keySet().retainAll(b.keySet());
        b.forEach((posB, stepsB) -> intersect.computeIfPresent(posB, (posA, stepsA) -> stepsA + stepsB));
        return intersect;
    }
}
