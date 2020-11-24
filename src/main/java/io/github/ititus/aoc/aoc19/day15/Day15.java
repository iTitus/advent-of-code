package io.github.ititus.aoc.aoc19.day15;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.math.graph.algorithm.Dijkstra;
import io.github.ititus.math.number.BigRational;
import io.github.ititus.math.vector.Vec2i;

@Aoc(year = 2019, day = 15, skip = true)
public final class Day15 implements AocSolution {

    private RepairDroid droid;
    private Dijkstra<Vec2i>.Result paths;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        droid = new RepairDroid(input.readAsIntCodeMemory());
        droid.buildMap();
        droid.render();
        paths = droid.getOxygenPaths();
    }

    @Override
    public Object part1() {
        return paths.getShortestPathLength(droid.getMap().getVertex(droid.getStartingPos()).orElseThrow())
                .intValueExact();
    }

    @Override
    public Object part2() {
        return droid.getMap().getVertices().stream()
                .map(paths::getShortestPathLength)
                .mapToInt(BigRational::intValueExact)
                .max().orElseThrow();
    }
}
