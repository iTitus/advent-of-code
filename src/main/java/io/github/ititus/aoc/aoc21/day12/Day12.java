package io.github.ititus.aoc.aoc21.day12;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;
import io.github.ititus.commons.math.graph.Graph;
import io.github.ititus.commons.math.graph.Vertex;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Aoc(year = 2021, day = 12)
public class Day12 implements AocSolution {

    Graph<String> graph;
    Vertex<String> start;
    Vertex<String> end;

    static boolean canVisit(boolean allowDouble, List<Vertex<String>> currentPath, Vertex<String> w) {
        if (isLargeCave(w.get())) {
            return true;
        } else if (!currentPath.contains(w)) {
            return true;
        }

        if (allowDouble) {
            Map<Vertex<String>, Long> freq = currentPath.stream()
                    .filter(v -> isSmallCave(v.get()))
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            return !freq.containsValue(2L);
        }

        return false;
    }

    static boolean isSmallCave(String name) {
        return name.toLowerCase(Locale.ROOT).equals(name);
    }

    static boolean isLargeCave(String name) {
        return name.toUpperCase(Locale.ROOT).equals(name);
    }

    @Override
    public void executeTests() {
        AocInput input = new AocStringInput("""
                start-A
                start-b
                A-c
                A-b
                b-d
                A-end
                b-end""");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());

        System.out.println();
        input = new AocStringInput("""
                dc-end
                HN-start
                start-kj
                dc-start
                dc-HN
                LN-dc
                HN-end
                kj-sa
                kj-HN
                kj-dc""");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());

        System.out.println();
        input = new AocStringInput("""
                fs-end
                he-DX
                fs-he
                start-DX
                pj-DX
                end-zg
                zg-sl
                zg-pj
                pj-he
                RW-he
                fs-DX
                pj-RW
                zg-RW
                start-pj
                he-WI
                zg-he
                pj-fs
                start-RW""");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());
    }

    @Override
    public void readInput(AocInput input) {
        graph = new Graph<>();
        input.lines()
                .map(String::strip)
                .map(l -> l.split("-"))
                .forEach(e -> {
                    Optional<Vertex<String>> optV1 = graph.getVertex(e[0]);
                    Vertex<String> v1;
                    if (optV1.isEmpty()) {
                        v1 = graph.addVertex(e[0]);
                    } else {
                        v1 = optV1.get();
                    }

                    Optional<Vertex<String>> optV2 = graph.getVertex(e[1]);
                    Vertex<String> v2;
                    if (optV2.isEmpty()) {
                        v2 = graph.addVertex(e[1]);
                    } else {
                        v2 = optV2.get();
                    }

                    graph.addEdge(v1, v2);
                });
        start = graph.getVertex("start").orElseThrow();
        end = graph.getVertex("end").orElseThrow();
    }

    @Override
    public Object part1() {
        List<List<Vertex<String>>> allPaths = findAllPaths();
        return allPaths.size();
    }

    @Override
    public Object part2() {
        List<List<Vertex<String>>> allPaths = findAllPathsWithDouble();
        return allPaths.size();
    }

    List<List<Vertex<String>>> findAllPaths() {
        return findAllPathsFrom(false, start, List.of(start));
    }

    List<List<Vertex<String>>> findAllPathsWithDouble() {
        return findAllPathsFrom(true, start, List.of(start));
    }

    List<List<Vertex<String>>> findAllPathsFrom(boolean allowDouble, Vertex<String> v, List<Vertex<String>> currentPath) {
        if (end.equals(v)) {
            return List.of(currentPath);
        }

        List<List<Vertex<String>>> allPaths = new ArrayList<>();
        for (Vertex<String> w : graph.getNeighborVertices(v)) {
            if (!start.equals(w) && canVisit(allowDouble, currentPath, w)) {
                List<Vertex<String>> path = Stream.concat(currentPath.stream(), Stream.of(w)).toList();
                allPaths.addAll(findAllPathsFrom(allowDouble, w, path));
            }
        }

        return allPaths;
    }
}
