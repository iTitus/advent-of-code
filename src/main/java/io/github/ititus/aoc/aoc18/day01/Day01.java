package io.github.ititus.aoc.aoc18.day01;

import io.github.ititus.aoc.InputProvider;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class Day01 {

    public static void main(String[] args) {
        Path p = InputProvider.getInput(2018, 1);

        List<Integer> freqChanges = new ArrayList<>();
        try (Stream<String> stream = Files.lines(p)) {
            stream
                    .filter(s -> !s.isBlank())
                    .mapToInt(Integer::parseInt)
                    .forEachOrdered(freqChanges::add);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

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
