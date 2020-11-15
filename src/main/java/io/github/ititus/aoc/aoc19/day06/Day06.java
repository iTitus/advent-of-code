package io.github.ititus.aoc.aoc19.day06;

import io.github.ititus.aoc.InputProvider;
import io.github.ititus.math.graph.Edge;
import io.github.ititus.math.graph.Graph;
import io.github.ititus.math.graph.Vertex;
import io.github.ititus.math.graph.algorithm.Dijkstra;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Day06 {

    private static final Map<Vertex<String>, Integer> orbitingCount = new HashMap<>();

    public static void main(String[] args) {
        Graph<String> g = new Graph<>();
        InputProvider.lines(2019, 6)
                .map(s -> s.split("\\)"))
                .forEach(s -> {
                    Vertex<String> vs = g.getVertex(s[1]).orElseGet(() -> g.addVertex(s[1]));
                    Vertex<String> ve = g.getVertex(s[0]).orElseGet(() -> g.addVertex(s[0]));
                    g.addEdge(vs, ve);
                });

        // 1
        System.out.println("### 1 ###");
        System.out.println(
                g.getVertices().stream()
                        .mapToInt(v -> computeOrbitCount(g, v))
                        .sum()
        );

        // 2
        System.out.println("### 2 ###");
        Vertex<String> youOrbiting = getOrbiting(g, g.getVertex("YOU").orElseThrow()).orElseThrow();
        Vertex<String> santaOrbiting = getOrbiting(g, g.getVertex("SAN").orElseThrow()).orElseThrow();
        Dijkstra<String>.Result r = new Dijkstra<>(g, youOrbiting).findShortestPaths();
        System.out.println(r.getShortestPathLength(santaOrbiting));
    }

    private static int computeOrbitCount(Graph<String> g, Vertex<String> v) {
        Integer cached = orbitingCount.get(v);
        if (cached != null) {
            return cached;
        }

        Optional<Vertex<String>> orbiting = getOrbiting(g, v);
        if (orbiting.isEmpty()) {
            orbitingCount.put(v, 0);
            return 0;
        }

        int count = 1 + computeOrbitCount(g, orbiting.get());
        orbitingCount.put(v, count);
        return count;
    }

    private static Optional<Vertex<String>> getOrbiting(Graph<String> g, Vertex<String> v) {
        Set<Edge<String>> outgoingEdges =
                g.getAdjacentEdges(v).stream().filter(e -> e.getStart().equals(v)).collect(Collectors.toSet());
        if (outgoingEdges.size() > 1) {
            throw new RuntimeException();
        } else if (outgoingEdges.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(outgoingEdges.iterator().next().getEnd());
    }
}
