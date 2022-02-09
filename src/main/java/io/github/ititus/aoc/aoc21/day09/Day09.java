package io.github.ititus.aoc.aoc21.day09;

import io.github.ititus.aoc.common.*;
import io.github.ititus.commons.math.vector.Vec2i;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntImmutableList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Aoc(year = 2021, day = 9)
public class Day09 implements AocSolution {

    static final int MAX_HEIGHT = 9;

    Ground ground;

    @Override
    public void executeTests() {
        AocInput input = new AocStringInput("""
                2199943210
                3987894921
                9856789892
                8767896789
                9899965678""");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());
    }

    @Override
    public void readInput(AocInput input) {
        ground = Ground.parse(input.readAllLines());
    }

    @Override
    public Object part1() {
        return ground.allLowPoints()
                .mapToInt(pos -> ground.getRiskFactor(pos))
                .sum();
    }

    @Override
    public Object part2() {
        return ground.allLowPoints()
                .map(p -> ground.findBasin(p))
                .sorted(Comparator.comparingInt(Basin::size).reversed())
                .limit(3)
                .mapToInt(Basin::size)
                .reduce(1, (a, b) -> a * b);
    }

    record Ground(
            int sizeX,
            int sizeY,
            IntList heights
    ) {

        static Ground parse(List<String> lines) {
            IntList heights = new IntArrayList();
            int sizeY = lines.size();
            int sizeX = -1;
            for (String line : lines) {
                line = line.strip();
                if (sizeX < 0) {
                    sizeX = line.length();
                } else if (line.length() != sizeX) {
                    throw new IllegalArgumentException();
                }

                line.chars()
                        .map(c -> c - '0')
                        .forEachOrdered(heights::add);
            }

            return new Ground(sizeX, sizeY, new IntImmutableList(heights));
        }

        Stream<Vec2i> allLowPoints() {
            return IntStream.range(0, sizeY)
                    .mapToObj(y -> IntStream.rangeClosed(0, sizeX).mapToObj(x -> new Vec2i(x, y)))
                    .flatMap(Function.identity())
                    .filter(this::isLowPoint);
        }

        OptionalInt getHeight(Vec2i pos) {
            try {
                return OptionalInt.of(getHeight(pos.x(), pos.y()));
            } catch (IllegalArgumentException ignored) {
                return OptionalInt.empty();
            }
        }

        int getHeight(int x, int y) {
            if (x < 0 || x >= sizeX || y < 0 || y >= sizeY) {
                throw new IllegalArgumentException();
            }

            return heights.getInt(x + y * sizeX);
        }

        boolean isLowPoint(Vec2i pos) {
            OptionalInt h = getHeight(pos);
            if (h.isEmpty()) {
                return false;
            }

            int height = h.getAsInt();
            return Arrays.stream(Direction.VALUES)
                    .map(Direction::getDirectionVector)
                    .map(pos::add)
                    .map(this::getHeight)
                    .flatMapToInt(OptionalInt::stream)
                    .allMatch(nh -> height < nh);
        }

        int getRiskFactor(Vec2i lowPoint) {
            return 1 + getHeight(lowPoint).orElseThrow();
        }

        Basin findBasin(Vec2i lowPoint) {
            Set<Vec2i> allPos = new HashSet<>();

            Queue<Vec2i> q = new LinkedList<>();
            q.add(lowPoint);
            while (!q.isEmpty()) {
                Vec2i pos = q.remove();
                if (!allPos.add(pos)) {
                    continue;
                }

                int height = getHeight(pos).orElseThrow();
                Arrays.stream(Direction.VALUES)
                        .map(Direction::getDirectionVector)
                        .map(pos::add)
                        .filter(p -> {
                            int h = getHeight(p).orElse(MAX_HEIGHT);
                            return h > height && h < MAX_HEIGHT;
                        })
                        .forEach(q::add);
            }

            return new Basin(lowPoint, Set.copyOf(allPos));
        }
    }

    record Basin(
            Vec2i lowPoint,
            Set<Vec2i> allPos
    ) {

        int size() {
            return allPos.size();
        }
    }
}
