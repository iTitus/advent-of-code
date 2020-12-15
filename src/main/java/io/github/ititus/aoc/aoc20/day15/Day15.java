package io.github.ititus.aoc.aoc20.day15;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import static java.util.Arrays.stream;

@Aoc(year = 2020, day = 15)
public class Day15 implements AocSolution {

    private IntList numbers;

    @Override
    public void executeTests() {
        readInput(new AocStringInput("0,3,6"));
        System.out.println("Part 1 (expected 436): " + part1());
        System.out.println("Part 2 (expected 175594): " + part2());
    }

    @Override
    public void readInput(AocInput input) {
        numbers = stream(input.readString().trim().split(","))
                .mapToInt(Integer::parseInt)
                .collect(IntArrayList::new, IntList::add, IntList::addAll);
    }

    @Override
    public Object part1() {
        MemoryGame game = new MemoryGame(numbers);
        return game.run(2020);
    }

    @Override
    public Object part2() {
        MemoryGame game = new MemoryGame(numbers);
        return game.run(30000000);
    }
}
