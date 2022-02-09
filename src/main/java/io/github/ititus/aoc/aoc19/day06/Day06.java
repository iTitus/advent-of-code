package io.github.ititus.aoc.aoc19.day06;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.commons.math.graph.Edge;
import io.github.ititus.commons.math.graph.Graph;
import io.github.ititus.commons.math.graph.Vertex;
import io.github.ititus.commons.math.graph.algorithm.Dijkstra;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Aoc(year = 2019, day = 6)
public final class Day06 implements AocSolution {

    private Graph<String> orbitGraph;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        orbitGraph = new Graph<>();
        try (Stream<String> stream = input.lines()) {
            stream
                    .map(s -> s.split("\\)"))
                    .forEach(s -> {
                        Vertex<String> vs = orbitGraph.getVertex(s[1]).orElseGet(() -> orbitGraph.addVertex(s[1]));
                        Vertex<String> ve = orbitGraph.getVertex(s[0]).orElseGet(() -> orbitGraph.addVertex(s[0]));
                        orbitGraph.addEdge(vs, ve);
                    });
        }
    }

    @Override
    public Object part1() {
        Map<Vertex<String>, Integer> orbitingCountCache = new HashMap<>();
        return orbitGraph.getVertices().stream()
                .mapToInt(v -> computeOrbitCount(v, orbitingCountCache))
                .sum();
    }

    @Override
    public Object part2() {
        Vertex<String> youOrbiting = getOrbiting(orbitGraph.getVertex("YOU").orElseThrow()).orElseThrow();
        Vertex<String> santaOrbiting = getOrbiting(orbitGraph.getVertex("SAN").orElseThrow()).orElseThrow();
        Dijkstra<String>.Result r = new Dijkstra<>(orbitGraph, youOrbiting).findShortestPaths();
        return r.getShortestPathLength(santaOrbiting);
    }

    private int computeOrbitCount(Vertex<String> v, Map<Vertex<String>, Integer> orbitingCountCache) {
        Integer cached = orbitingCountCache.get(v);
        if (cached != null) {
            return cached;
        }

        Optional<Vertex<String>> orbiting = getOrbiting(v);
        if (orbiting.isEmpty()) {
            orbitingCountCache.put(v, 0);
            return 0;
        }

        int count = 1 + computeOrbitCount(orbiting.get(), orbitingCountCache);
        orbitingCountCache.put(v, count);
        return count;
    }

    private Optional<Vertex<String>> getOrbiting(Vertex<String> v) {
        Set<Edge<String>> outgoingEdges = orbitGraph.getAdjacentEdges(v).stream()
                .filter(e -> e.getStart().equals(v))
                .collect(Collectors.toSet());

        if (outgoingEdges.size() > 1) {
            throw new RuntimeException();
        } else if (outgoingEdges.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(outgoingEdges.iterator().next().getEnd());
    }
}
