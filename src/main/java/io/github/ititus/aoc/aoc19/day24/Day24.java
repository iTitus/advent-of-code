package io.github.ititus.aoc.aoc19.day24;

import io.github.ititus.aoc.InputProvider;

import java.util.List;

public class Day24 {

    public static void main(String[] args) {
        List<String> lines = InputProvider.readAllLines(2019, 24);

        // 1
        System.out.println("### 1 ###");
        ErisBugs eb1 = new ErisBugs(false, lines);
        System.out.println(eb1.stepUntilRepeat());

        // 2
        System.out.println("### 2 ###");
        ErisBugs eb2 = new ErisBugs(true, lines);
        System.out.println(eb2.step(200));
    }
}
