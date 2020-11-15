package io.github.ititus.aoc.aoc19.day15;

import io.github.ititus.aoc.InputProvider;
import io.github.ititus.math.graph.algorithm.Dijkstra;
import io.github.ititus.math.number.BigIntegerMath;
import io.github.ititus.math.number.BigRational;
import io.github.ititus.math.vector.Vec2i;

import java.math.BigInteger;
import java.util.Arrays;

public class Day15 {

    public static void main(String[] args) {
        String input = InputProvider.readString(2019, 15);
        BigInteger[] memory =
                Arrays.stream(input.split(",")).map(String::strip).map(BigIntegerMath::of).toArray(BigInteger[]::new);

        RepairDroid droid = new RepairDroid(memory);
        droid.buildMap();
        droid.render();
        Dijkstra<Vec2i>.Result paths = droid.getOxygenPaths();

        // 1
        System.out.println("### 1 ###");
        int l1 =
                paths.getShortestPathLength(droid.getMap().getVertex(droid.getStartingPos()).orElseThrow()).intValueExact();
        System.out.println(l1);

        // 2
        System.out.println("### 2 ###");
        int l2 =
                droid.getMap().getVertices().stream().map(paths::getShortestPathLength).mapToInt(BigRational::intValueExact).max().orElseThrow();
        System.out.println(l2);
    }
}
