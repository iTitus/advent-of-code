package io.github.ititus.aoc.aoc18.day06;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.math.vector.Vec2i;

import java.util.List;
import java.util.stream.Collectors;

@Aoc(year = 2018, day = 6)
public final class Day06 implements AocSolution {

    private List<Vec2i> inputs;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        this.inputs = input.lines()
                .map(s -> s.split(","))
                .map(ss -> new Vec2i(Integer.parseInt(ss[0].trim()), Integer.parseInt(ss[1].trim())))
                .collect(Collectors.toList());
    }

    @Override
    public Object part1() {
        return null;
    }

    @Override
    public Object part2() {
        return null;
    }
}
