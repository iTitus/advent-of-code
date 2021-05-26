package io.github.ititus.aoc.aoc19.day10;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.math.vector.Vec2i;

import java.util.*;

@Aoc(year = 2019, day = 10)
public final class Day10 implements AocSolution {

    private Map<Vec2i, SortedMap<Vec2i, SortedSet<Vec2i>>> rays;
    private Vec2i maxLaser;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        List<String> lines = input.readAllLines();
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

        Vec2i initialLaserPos = new Vec2i(0, -1);
        Comparator<Vec2i> angleComparator = Comparator.comparingDouble(initialLaserPos::getAngleTo);

        rays = new HashMap<>();
        for (Vec2i asteroidPos : asteroids) {
            Comparator<Vec2i> distanceComparator = Comparator.comparingInt(asteroidPos::manhattanDistanceTo);
            SortedMap<Vec2i, SortedSet<Vec2i>> rayTraces = rays.computeIfAbsent(asteroidPos,
                    k -> new TreeMap<>(angleComparator));
            for (Vec2i otherAsteroidPos : asteroids) {
                if (otherAsteroidPos == asteroidPos) {
                    continue;
                }

                Vec2i rayDir = otherAsteroidPos.subtract(asteroidPos).reduce();
                rayTraces.computeIfAbsent(rayDir, k -> new TreeSet<>(distanceComparator)).add(otherAsteroidPos);
            }
        }
    }

    @Override
    public Object part1() {
        Map.Entry<Vec2i, SortedMap<Vec2i, SortedSet<Vec2i>>> max = Collections.max(
                rays.entrySet(),
                Comparator.comparingInt(e -> e.getValue().size())
        );

        maxLaser = max.getKey();
        int size = max.getValue().size();
        return "maxLaser=" + maxLaser + ", size=" + size;
    }

    @Override
    public Object part2() {
        SortedMap<Vec2i, SortedSet<Vec2i>> originalRayTraces = rays.get(maxLaser);
        SortedMap<Vec2i, SortedSet<Vec2i>> laserRayTraces = new TreeMap<>(originalRayTraces.comparator());
        originalRayTraces.forEach((laserDir, laserHits) -> laserRayTraces.put(laserDir, new TreeSet<>(laserHits)));

        List<Vec2i> vaporized = new ArrayList<>();

        while (true) {
            boolean allEmpty = true;
            for (Map.Entry<Vec2i, SortedSet<Vec2i>> rayTraces : laserRayTraces.entrySet()) {
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
        return "200th vaporized asteroid is " + p200 + " with result " + p200.x() * 100 + p200.y();
    }
}
