package io.github.ititus.aoc.aoc18.day01;

import io.github.ititus.aoc.InputProvider;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Day01 {

    public static void main(String[] args) {
        List<Integer> freqChanges = InputProvider.readAllLinesAsInt(2018, 1);

        // First part
        int freq = freqChanges.stream().mapToInt(Integer::intValue).sum();
        System.out.println(freq);

        // Second part
        Set<Integer> visited = new HashSet<>();
        Iterator<Integer> freqIt = freqChanges.iterator();
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
            curFreq += freqIt.next();
        }
    }
}
