package io.github.ititus.aoc.aoc19.day21;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;

@Aoc(year = 2019, day = 21)
public final class Day21 implements AocSolution {

    private SpringDroid springDroid;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        springDroid = new SpringDroid(input.readAsIntCodeMemory());
    }

    @Override
    public Object part1() {
        return springDroid.runSpringScript(
                "NOT J J",
                "AND A J",
                "AND B J",
                "AND C J",
                "NOT J J",
                "AND D J",
                "WALK"
        );
    }

    @Override
    public Object part2() {
        return springDroid.runSpringScript(
                "NOT J J",
                "AND A J",
                "AND B J",
                "AND C J",
                "NOT J J",
                "AND D J",
                "OR E T",
                "OR H T",
                "AND T J",
                "RUN"
        );
    }
}
