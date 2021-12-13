package io.github.ititus.aoc.aoc21.day13;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;
import io.github.ititus.math.vector.Vec2i;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Aoc(year = 2021, day = 13)
public class Day13 implements AocSolution {

    Set<Vec2i> dots;
    List<Fold> folds;

    @Override
    public void executeTests() {
        AocInput input = new AocStringInput("""
                6,10
                0,14
                9,10
                0,3
                10,4
                4,11
                6,0
                6,12
                4,1
                0,13
                10,12
                3,4
                3,0
                8,4
                1,10
                2,14
                8,10
                9,0
                                
                fold along y=7
                fold along x=5""");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());
    }

    @Override
    public void readInput(AocInput input) {
        List<String> lines = input.readAllLines();

        this.dots = new HashSet<>();
        folds = new ArrayList<>();
        boolean dots = true;
        for (String line : lines) {
            if (dots) {
                if (line.isEmpty()) {
                    dots = false;
                    continue;
                }

                String[] split = line.split(",");
                this.dots.add(new Vec2i(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
            } else if (!line.isEmpty()) {
                folds.add(Fold.parse(line));
            }
        }
    }

    @Override
    public Object part1() {
        return folds.get(0).fold(dots).size();
    }

    @Override
    public Object part2() {
        Set<Vec2i> dots = this.dots;
        for (Fold fold : folds) {
            dots = fold.fold(dots);
        }

        return toString(dots);
    }

    String toString(Set<Vec2i> dots) {
        Vec2i min = new Vec2i(dots.stream().mapToInt(Vec2i::x).min().orElseThrow(), dots.stream().mapToInt(Vec2i::y).min().orElseThrow());
        Vec2i max = new Vec2i(dots.stream().mapToInt(Vec2i::x).max().orElseThrow(), dots.stream().mapToInt(Vec2i::y).max().orElseThrow());

        StringBuilder b = new StringBuilder();
        for (int y = min.y(); y <= max.y(); y++) {
            for (int x = min.x(); x <= max.x(); x++) {
                if (dots.contains(new Vec2i(x, y))) {
                    b.append('\u2588'); // █
                } else {
                    b.append(' ');
                }
            }

            b.append('\n');
        }

        return b.toString();
    }

    record Fold(
            boolean horizontalAxis,
            int coordinate
    ) {

        static Fold parse(String s) {
            s = s.substring("fold along ".length());
            char type = s.charAt(0);
            if (type != 'x' && type != 'y') {
                throw new RuntimeException();
            }

            int coordinate = Integer.parseInt(s, 2, s.length(), 10);
            return new Fold(type == 'y', coordinate);
        }

        Set<Vec2i> fold(Set<Vec2i> dots) {
            return dots.stream()
                    .map(this::translate)
                    .collect(Collectors.toUnmodifiableSet());
        }

        Vec2i translate(Vec2i v) {
            if (horizontalAxis) {
                int diff = v.y() - coordinate;
                if (diff == 0) {
                    throw new IllegalArgumentException();
                } else if (diff < 0) {
                    return v;
                }

                return new Vec2i(v.x(), coordinate - diff);
            } else {
                int diff = v.x() - coordinate;
                if (diff == 0) {
                    throw new IllegalArgumentException();
                } else if (diff < 0) {
                    return v;
                }

                return new Vec2i(coordinate - diff, v.y());
            }
        }
    }
}
