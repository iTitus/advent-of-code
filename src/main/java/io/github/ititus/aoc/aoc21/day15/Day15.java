package io.github.ititus.aoc.aoc21.day15;

import io.github.ititus.aoc.aoc21.day15.graph.DiGraph;
import io.github.ititus.aoc.aoc21.day15.graph.Vertex;
import io.github.ititus.aoc.aoc21.day15.graph.alogrithm.DiDijkstra;
import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;
import io.github.ititus.math.number.BigRational;
import io.github.ititus.math.vector.Vec2i;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntImmutableList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.List;

@Aoc(year = 2021, day = 15)
public class Day15 implements AocSolution {

    static int MAX_RISK = 9;

    Board board;

    @Override
    public void executeTests() {
        AocInput input = new AocStringInput("""
                1163751742
                1381373672
                2136511328
                3694931569
                7463417111
                1319128137
                1359912421
                3125421639
                1293138521
                2311944581""");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());

        input = new AocStringInput("""
                1911191111
                1119111991
                9999999111
                9999911199
                9999119999
                9999199999
                9111199999
                9199999111
                9111911191
                9991119991""");
        readInput(input);
        System.out.println(part1());
    }

    @Override
    public void readInput(AocInput input) {
        board = Board.parse(input.readAllLines());
    }

    @Override
    public Object part1() {
        return board.getLowestTotalRisk(1);
    }

    @Override
    public Object part2() {
        return board.getLowestTotalRisk(5);
    }

    record Board(
            int sizeX,
            int sizeY,
            IntList risks
    ) {

        static Board parse(List<String> lines) {
            int sizeY = lines.size();
            int sizeX = -1;
            IntList risks = new IntArrayList();
            for (String line : lines) {
                line = line.strip();
                if (sizeX < 0) {
                    sizeX = line.length();
                } else if (line.length() != sizeX) {
                    throw new IllegalArgumentException();
                }

                line.chars()
                        .map(c -> c - '0')
                        .forEachOrdered(risks::add);
            }

            return new Board(sizeX, sizeY, new IntImmutableList(risks));
        }

        static int idx(int sizeX, int x, int y) {
            return x + y * sizeX;
        }

        int getRisk(int x, int y) {
            int risk = risks.getInt(idx(sizeX, x % sizeX, y % sizeY));
            risk += (x / sizeX) + (y / sizeY);
            while (risk > MAX_RISK) {
                risk -= MAX_RISK;
            }

            return risk;
        }

        int getLowestTotalRisk(int tiles) {
            DiGraph<Vec2i> g = new DiGraph<>();
            for (int y = 0; y < tiles * sizeY; y++) {
                for (int x = 0; x < tiles * sizeX; x++) {
                    g.addVertex(new Vec2i(x, y));
                }
            }

            for (int y = 0; y < tiles * sizeY; y++) {
                for (int x = 0; x < tiles * sizeX; x++) {
                    Vec2i start = new Vec2i(x, y);
                    if (x + 1 < tiles * sizeX) {
                        g.addEdge(start, new Vec2i(x + 1, y), BigRational.of(getRisk(x + 1, y)));
                    }
                    if (y + 1 < tiles * sizeY) {
                        g.addEdge(start, new Vec2i(x, y + 1), BigRational.of(getRisk(x, y + 1)));
                    }
                    if (x - 1 >= 0) {
                        g.addEdge(start, new Vec2i(x - 1, y), BigRational.of(getRisk(x - 1, y)));
                    }
                    if (y - 1 >= 0) {
                        g.addEdge(start, new Vec2i(x, y - 1), BigRational.of(getRisk(x, y - 1)));
                    }
                }
            }

            Vertex<Vec2i> start = g.getVertex(new Vec2i(0, 0)).orElseThrow();
            Vertex<Vec2i> end = g.getVertex(new Vec2i(tiles * sizeX - 1, tiles * sizeY - 1)).orElseThrow();

            DiDijkstra<Vec2i>.Result shortestPaths = new DiDijkstra<>(g, start).findShortestPaths();
            return shortestPaths.getShortestPathLength(end).intValueExact();
        }
    }
}
