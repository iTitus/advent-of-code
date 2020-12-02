package io.github.ititus.aoc.aoc20.day02;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Aoc(year = 2020, day = 2)
public class Day02 implements AocSolution {

    private List<PasswordEntry> entries;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        entries = input
                .lines()
                .map(PasswordEntry::of)
                .collect(toList());
    }

    @Override
    public Object part1() {
        return entries.stream()
                .filter(PasswordEntry::isValidPart1)
                .count();
    }

    @Override
    public Object part2() {
        return entries.stream()
                .filter(PasswordEntry::isValidPart2)
                .count();
    }
}
