package io.github.ititus.aoc.aoc20.day16;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Aoc(year = 2020, day = 16)
public class Day16 implements AocSolution {

    private TicketReport report;

    @Override
    public void executeTests() {
        readInput(new AocStringInput("""
                class: 1-3 or 5-7
                row: 6-11 or 33-44
                seat: 13-40 or 45-50
                                
                your ticket:
                7,1,14
                                
                nearby tickets:
                7,3,47
                40,4,50
                55,2,20
                38,6,12
                """));
        System.out.println("Part 1 (expected 71): " + part1());

        readInput(new AocStringInput("""
                class: 0-1 or 4-19
                row: 0-5 or 8-19
                seat: 0-13 or 16-19
                                
                your ticket:
                11,12,13
                                
                nearby tickets:
                3,9,18
                15,1,5
                5,14,9
                """));
        System.out.println("Part 2 (expected 12): " + part2_("class"::equals));
        System.out.println("Part 2 (expected 11): " + part2_("row"::equals));
        System.out.println("Part 2 (expected 13): " + part2_("seat"::equals));
    }

    @Override
    public void readInput(AocInput input) {
        try (Stream<String> stream = input.lines()) {
            report = stream.collect(TicketReport.collector());
        }
    }

    @Override
    public Object part1() {
        return report.getTicketScanningErrorRate();
    }

    @Override
    public Object part2() {
        return part2_(propName -> propName.startsWith("departure "));
    }

    private long part2_(Predicate<String> resultFilter) {
        return report.determineProperties(resultFilter);
    }
}
