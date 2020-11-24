package io.github.ititus.aoc.aoc19.day20;

import io.github.ititus.aoc.common.InputProvider;

import java.util.List;

public class Day20 {

    public static void main(String[] args) {
        List<String> lines = InputProvider.readAllLines(2019, 20);

        // 1
        System.out.println("### 1 ###");
        PlutoMaze pm1 = new PlutoMaze(false, lines);
        System.out.println(pm1.findShortestPath("AA", "ZZ"));

        // 2
        System.out.println("### 2 ###");
        PlutoMaze pm2 = new PlutoMaze(true, lines);
        System.out.println(pm2.findShortestPath("AA", "ZZ"));
    }
}
