package io.github.ititus.aoc.aoc19.day10;

import io.github.ititus.aoc.InputProvider;
import io.github.ititus.math.vector.Vec2i;

import java.util.*;
import java.util.stream.Collectors;

public class Day10 {

    public static void main(String[] args) {
        List<String> lines = InputProvider.readAllLines(2019, 10);
        Set<Vec2i> asteroids = new HashSet<>();

        int sizeY = lines.size();
        int sizeX = lines.get(0).length();

        for (int y = 0; y < sizeY; y++) {
            char[] line = lines.get(y).toCharArray();
            for (int x = 0; x < sizeX; x++) {
                if (line[x] == '#') {
                    asteroids.add(new Vec2i(x, y));
                }
            }
        }

        // 1
        System.out.println("### 1 ###");

        Map<Vec2i, Map<Vec2i, SortedSet<Vec2i>>> rays = new HashMap<>();
        for (Vec2i asteroidPos : asteroids) {
            Comparator<Vec2i> distanceComparator = Comparator.comparingInt(asteroidPos::manhattanDistanceTo);
            Map<Vec2i, SortedSet<Vec2i>> rayTraces = rays.computeIfAbsent(asteroidPos, k -> new HashMap<>());
            for (Vec2i otherAsteroidPos : asteroids) {
                if (otherAsteroidPos == asteroidPos) {
                    continue;
                }

                Vec2i rayDir = otherAsteroidPos.subtract(asteroidPos).reduce();
                rayTraces.computeIfAbsent(rayDir, k -> new TreeSet<>(distanceComparator)).add(otherAsteroidPos);
            }
        }

        Map.Entry<Vec2i, Map<Vec2i, SortedSet<Vec2i>>> max = Collections.max(
                rays.entrySet(),
                Comparator.comparingInt(e -> e.getValue().values().size())
        );
        System.out.println(max.getKey());
        System.out.println(max.getValue().size());

        // 2
        System.out.println("### 2 ###");
        Vec2i initialLaserPos = new Vec2i(0, -1);
        Vec2i laser = max.getKey();

        Map<Vec2i, SortedSet<Vec2i>> laserRayTraces = new HashMap<>();
        rays.get(laser).forEach((laserDir, laserHits) -> laserRayTraces.put(laserDir, new TreeSet<>(laserHits)));

        List<Map.Entry<Vec2i, SortedSet<Vec2i>>> laserRayDirs = laserRayTraces.entrySet().stream()
                .sorted(Comparator.comparingDouble(e -> initialLaserPos.getAngleTo(e.getKey())))
                .collect(Collectors.toList());

        List<Vec2i> vaporized = new ArrayList<>();

        while (true) {
            boolean allEmpty = true;
            for (Map.Entry<Vec2i, SortedSet<Vec2i>> rayTraces : laserRayDirs) {
                SortedSet<Vec2i> laserHits = rayTraces.getValue();

                if (!laserHits.isEmpty()) {
                    allEmpty = false;
                    Vec2i first = laserHits.first();
                    laserHits.remove(first);
                    vaporized.add(first);
                }
            }

            if (allEmpty) {
                break;
            }
        }

        Vec2i p200 = vaporized.get(199);
        System.out.println(p200);
        System.out.println(p200.getX() * 100 + p200.getY());
    }
}
