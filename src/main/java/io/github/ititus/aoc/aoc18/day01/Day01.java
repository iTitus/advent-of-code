package io.github.ititus.aoc.aoc18.day01;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.FastUtilStreams;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

@Aoc(year = 2018, day = 1)
public final class Day01 implements AocSolution {

    private IntList freqChanges;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        freqChanges = input.readAllLinesAsInt();
    }

    @Override
    public Object part1() {
        return FastUtilStreams.stream(freqChanges).sum();
    }

    @Override
    public Object part2() {
        IntSet visited = new IntOpenHashSet();
        IntListIterator freqIt = freqChanges.iterator();
        int curFreq = 0;

        while (true) {
            if (visited.contains(curFreq)) {
                return curFreq;
            }

            visited.add(curFreq);
            if (!freqIt.hasNext()) {
                freqIt = freqChanges.iterator();
            }
            curFreq += freqIt.nextInt();
        }
    }
}
