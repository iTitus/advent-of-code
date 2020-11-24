package io.github.ititus.aoc.aoc18.day01;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocDayInput;
import io.github.ititus.aoc.common.AocDaySolution;
import io.github.ititus.aoc.common.FastUtilStreams;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

@Aoc(year = 2018, day = 1)
public final class Day01 implements AocDaySolution {

    private IntList freqChanges;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocDayInput input) {
        freqChanges = input.readAllLinesAsInt();
    }

    @Override
    public String part1() {
        return String.valueOf(FastUtilStreams.stream(freqChanges).sum());
    }

    @Override
    public String part2() {
        IntSet visited = new IntOpenHashSet();
        IntListIterator freqIt = freqChanges.iterator();
        int curFreq = 0;

        while (true) {
            if (visited.contains(curFreq)) {
                return String.valueOf(curFreq);
            }

            visited.add(curFreq);
            if (!freqIt.hasNext()) {
                freqIt = freqChanges.iterator();
            }
            curFreq += freqIt.nextInt();
        }
    }
}
