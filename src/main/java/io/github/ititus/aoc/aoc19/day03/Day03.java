package io.github.ititus.aoc.aoc19.day03;

import io.github.ititus.aoc.InputProvider;

import java.util.*;

public class Day03 {

    public static void main(String[] args) {
        List<String> input = InputProvider.readAllLines(2019, 3);

        String[] wire1Commands = input.get(0).split(",");
        String[] wire2Commands = input.get(1).split(",");

        Map<Pos, Integer> wire1Path = getWirePath(wire1Commands);
        Map<Pos, Integer> wire2Path = getWirePath(wire2Commands);

        // 1
        Set<Pos> intersect1 = new HashSet<>(wire1Path.keySet());
        intersect1.retainAll(wire2Path.keySet());
        Pos closestIntersection1 = Collections.min(intersect1, Comparator.comparingInt(Pos::manhattanDistance));
        System.out.println(closestIntersection1 + " with " + closestIntersection1.manhattanDistance());

        // 2
        Map<Pos, Integer> intersect2 = new HashMap<>(wire1Path);
        intersect2.keySet().retainAll(wire2Path.keySet());
        wire2Path.forEach((pos, steps) -> intersect2.computeIfPresent(pos, (p, s) -> s + steps));
        Map.Entry<Pos, Integer> closestIntersection2 = Collections.min(intersect2.entrySet(), Comparator.comparingInt(Map.Entry::getValue));
        System.out.println(closestIntersection2.getKey() + " with " + closestIntersection2.getValue());
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
}
