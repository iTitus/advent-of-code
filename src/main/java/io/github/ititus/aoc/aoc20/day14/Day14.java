package io.github.ititus.aoc.aoc20.day14;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;

import java.util.List;

@Aoc(year = 2020, day = 14)
public class Day14 implements AocSolution {

    private List<String> lines;

    @Override
    public void executeTests() {
        readInput(new AocStringInput("""
                mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
                mem[8] = 11
                mem[7] = 101
                mem[8] = 0
                """));
        System.out.println("Part 1 (expected 165): " + part1());

        readInput(new AocStringInput("""
                mask = 000000000000000000000000000000X1001X
                mem[42] = 100
                mask = 00000000000000000000000000000000X0XX
                mem[26] = 1
                """));
        System.out.println("Part 2 (expected 208): " + part2());
    }

    @Override
    public void readInput(AocInput input) {
        lines = input.readAllLines();
    }

    @Override
    public Object part1() {
        Memory m = new Memory(false);
        return m.execute(lines);
    }

    @Override
    public Object part2() {
        Memory m = new Memory(true);
        return m.execute(lines);
    }
}
