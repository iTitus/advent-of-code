package io.github.ititus.aoc.aoc20.day06;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;

import java.util.List;
import java.util.stream.Stream;

@Aoc(year = 2020, day = 6)
public class Day06 implements AocSolution {

    private List<Group> groups;

    @Override
    public void executeTests() {
        List<String> input = List.of(
                "abc",
                "",
                "a",
                "b",
                "c",
                "",
                "ab",
                "ac",
                "",
                "a",
                "a",
                "a",
                "a",
                "",
                "b"
        );
        groups = input.stream().collect(Group.GroupAccumulator.collector());
        System.out.println("Should be 11: " + part1());
        System.out.println("Should be 6: " + part2());
    }

    @Override
    public void readInput(AocInput input) {
        try (Stream<String> stream = input.lines()) {
            groups = stream.collect(Group.GroupAccumulator.collector());
        }
    }

    @Override
    public Object part1() {
        return groups.stream()
                .mapToInt(Group::getAnyYesCount)
                .sum();
    }

    @Override
    public Object part2() {
        return groups.stream()
                .mapToLong(Group::getAllYesCount)
                .sum();
    }
}
