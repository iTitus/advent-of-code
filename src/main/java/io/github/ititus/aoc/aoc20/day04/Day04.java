package io.github.ititus.aoc.aoc20.day04;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;

import java.util.List;
import java.util.stream.Stream;

@Aoc(year = 2020, day = 4)
public class Day04 implements AocSolution {

    private List<Passport> passports;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        try (Stream<String> stream = input.lines()) {
            passports = stream.collect(Passport.PassportAccumulator.collector());
        }
    }

    @Override
    public Object part1() {
        return passports.stream().filter(Passport::areRequiredFieldsPresent).count();
    }

    @Override
    public Object part2() {
        return passports.stream().filter(Passport::areRequiredFieldsPresentAndValid).count();
    }
}
