package io.github.ititus.aoc.aoc20.day22;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import static java.util.Arrays.stream;

@Aoc(year = 2020, day = 22)
public class Day22 implements AocSolution {

    private Combat combat;

    @Override
    public void executeTests() {
        readInput(new AocStringInput("""
                Player 1:
                9
                2
                6
                3
                1
                                
                Player 2:
                5
                8
                4
                7
                10
                """));
        System.out.println("Part 1 (expected 306): " + part1());
        System.out.println("Part 2 (expected 291): " + part2());
    }

    @Override
    public void readInput(AocInput input) {
        String[] split = input.readString().split("\n\n");
        if (split.length != 2) {
            throw new RuntimeException();
        }

        IntList startingHand1 = stream(split[0].split("\n"))
                .skip(1)
                .mapToInt(Integer::parseInt)
                .collect(IntArrayList::new, IntList::add, IntList::addAll);
        IntList startingHand2 = stream(split[1].split("\n"))
                .skip(1)
                .mapToInt(Integer::parseInt)
                .collect(IntArrayList::new, IntList::add, IntList::addAll);

        combat = new Combat(startingHand1, startingHand2);
    }

    @Override
    public Object part1() {
        combat.reset();
        return combat.simulate(false);
    }

    @Override
    public Object part2() {
        combat.reset();
        return combat.simulate(true);
    }
}
