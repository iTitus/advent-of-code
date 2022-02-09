package io.github.ititus.aoc.aoc20.day03;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.commons.math.vector.Vec2i;

import java.util.Arrays;

@Aoc(year = 2020, day = 3)
public class Day03 implements AocSolution {

    private TreeMap map;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        map = new TreeMap(input.readAllLines());
        if (map.hasTree(0, 0)) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Object part1() {
        Vec2i slope = new Vec2i(3, 1);
        return map.countTreesEncountered(slope);
    }

    @Override
    public Object part2() {
        Vec2i[] slopes = {
                new Vec2i(1, 1),
                new Vec2i(3, 1),
                new Vec2i(5, 1),
                new Vec2i(7, 1),
                new Vec2i(1, 2)
        };

        return Arrays
                .stream(slopes)
                .mapToLong(map::countTreesEncountered)
                .reduce((i, j) -> i * j)
                .orElseThrow();
    }
}
