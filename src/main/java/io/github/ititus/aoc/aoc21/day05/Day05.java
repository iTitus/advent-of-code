package io.github.ititus.aoc.aoc21.day05;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.math.vector.Vec2i;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Aoc(year = 2021, day = 5)
public class Day05 implements AocSolution {

    List<Vent> vents;
    Vec2i min;
    Vec2i max;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        vents = input.lines()
                .map(Vent::parse)
                .toList();
        min = new Vec2i(
                vents.stream()
                        .flatMap(v -> Stream.of(v.start(), v.end()))
                        .mapToInt(Vec2i::x)
                        .min()
                        .orElseThrow(),
                vents.stream()
                        .flatMap(v -> Stream.of(v.start(), v.end()))
                        .mapToInt(Vec2i::y)
                        .min()
                        .orElseThrow()
        );
        max = new Vec2i(
                vents.stream()
                        .flatMap(v -> Stream.of(v.start(), v.end()))
                        .mapToInt(Vec2i::x)
                        .max()
                        .orElseThrow(),
                vents.stream()
                        .flatMap(v -> Stream.of(v.start(), v.end()))
                        .mapToInt(Vec2i::y)
                        .max()
                        .orElseThrow()
        );
    }

    @Override
    public Object part1() {
        int count = 0;
        for (int y = min.y(); y <= max.y(); y++) {
            for (int x = min.x(); x < max.x(); x++) {
                Vec2i pos = new Vec2i(x, y);
                Optional<Vent> second = vents.stream()
                        .filter(v -> v.isVertical() || v.isHorizontal())
                        .filter(v -> v.contains(pos))
                        .skip(1)
                        .findAny();
                if (second.isPresent()) {
                    count++;
                }
            }
        }

        return count;
    }

    @Override
    public Object part2() {
        int count = 0;
        for (int y = min.y(); y <= max.y(); y++) {
            for (int x = min.x(); x < max.x(); x++) {
                Vec2i pos = new Vec2i(x, y);
                Optional<Vent> second = vents.stream()
                        .filter(v -> v.contains(pos))
                        .skip(1)
                        .findAny();
                if (second.isPresent()) {
                    count++;
                }
            }
        }

        return count;
    }

    record Vent(
            Vec2i start,
            Vec2i end
    ) {

        Vent {
            if (start.x() != end.x() && start.y() != end.y()) {
                Vec2i diff = end.subtract(start).reduce();
                if (Math.abs(diff.x()) != 1 || Math.abs(diff.y()) != 1) {
                    throw new IllegalArgumentException();
                }
            }
        }

        boolean isVertical() {
            return start.x() == end.x();
        }

        boolean isHorizontal() {
            return start.y() == end.y();
        }

        boolean isDiagonal() {
            return !isVertical() && !isHorizontal();
        }

        boolean contains(Vec2i pos) {
            if (isVertical()) {
                return pos.x() == start.x() && start().y() <= pos.y() && pos.y() <= end.y();
            } else if (isHorizontal()) {
                return pos.y() == start.y() && start().x() <= pos.x() && pos.x() <= end.x();
            } else {
                if (start.equals(pos) || end.equals(pos)) {
                    return true;
                }

                Vec2i diff1 = pos.subtract(start).reduce();
                Vec2i diff2 = end.subtract(pos).reduce();
                return diff1.equals(diff2);
            }
        }

        static Vent parse(String s) {
            String[] points = s.split(" -> ", 2);
            Vec2i start = parseVec2i(points[0]);
            Vec2i end = parseVec2i(points[1]);
            if (end.x() < start.x() || end.y() < start.y()) {
                Vec2i tmp = start;
                start = end;
                end = tmp;
            }

            return new Vent(start, end);
        }

        static Vec2i parseVec2i(String s) {
            String[] components = s.split(",", 2);
            return new Vec2i(Integer.parseInt(components[0]), Integer.parseInt(components[1]));
        }
    }
}
