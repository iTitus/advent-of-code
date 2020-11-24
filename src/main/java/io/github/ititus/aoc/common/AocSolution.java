package io.github.ititus.aoc.common;

public interface AocSolution {

    void executeTests();

    void readInput(AocInput input);

    Object part1();

    Object part2();

}
