package io.github.ititus.aoc.aoc20.day05;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.stream.Stream;

@Aoc(year = 2020, day = 5)
public class Day05 implements AocSolution {

    private IntList seatIds;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        try (Stream<String> stream = input.lines()) {
            seatIds = stream
                    .map(l -> l.replace('F', '0'))
                    .map(l -> l.replace('B', '1'))
                    .map(l -> l.replace('L', '0'))
                    .map(l -> l.replace('R', '1'))
                    .mapToInt(l -> Integer.parseInt(l, 2))
                    .sorted()
                    .collect(IntArrayList::new, IntArrayList::add, IntArrayList::addAll);
        }
    }

    @Override
    public Object part1() {
        return seatIds.getInt(seatIds.size() - 1);
    }

    @Override
    public Object part2() {
        for (int i = 0; i < seatIds.size() - 1; i++) {
            int cur = seatIds.getInt(i);
            int next = seatIds.getInt(i + 1);
            if (next - cur == 2) {
                return cur + 1;
            }
        }
        return null;
    }
}
