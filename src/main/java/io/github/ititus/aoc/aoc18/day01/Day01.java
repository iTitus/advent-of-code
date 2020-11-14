package io.github.ititus.aoc.aoc18.day01;

import io.github.ititus.aoc.FastUtilStreams;
import io.github.ititus.aoc.InputProvider;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

public class Day01 {

    public static void main(String[] args) {
        IntList freqChanges = InputProvider.readAllLinesAsInt(2018, 1);

        // First part
        int freq = FastUtilStreams.stream(freqChanges).sum();
        System.out.println(freq);

        // Second part
        IntSet visited = new IntOpenHashSet();
        IntListIterator freqIt = freqChanges.iterator();
        int curFreq = 0;

        while (true) {
            if (visited.contains(curFreq)) {
                System.out.println(curFreq);
                break;
            }

            visited.add(curFreq);
            if (!freqIt.hasNext()) {
                freqIt = freqChanges.iterator();
            }
            curFreq += freqIt.nextInt();
        }
    }
}
