package io.github.ititus.aoc.aoc21.day20;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;
import io.github.ititus.math.vector.Vec2i;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Aoc(year = 2021, day = 20)
public class Day20 implements AocSolution {

    static final int ALGO_LENGTH = 1 << 9;

    String enhancementAlgorithm;
    Image startingImage;

    @Override
    public void executeTests() {
        AocInput input = new AocStringInput("""
                ..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#

                #..#.
                #....
                ##..#
                ..#..
                ..###""");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());
    }

    @Override
    public void readInput(AocInput input) {
        List<String> lines = input.readAllLines();
        enhancementAlgorithm = lines.get(0).strip();
        if (enhancementAlgorithm.length() != ALGO_LENGTH || enhancementAlgorithm.chars().anyMatch(c -> c != '#' && c != '.')) {
            throw new RuntimeException();
        }

        List<String> imageLines = lines.stream()
                .skip(1)
                .map(String::strip)
                .filter(l -> !l.isEmpty())
                .toList();
        int sizeY = imageLines.size();
        int sizeX = -1;
        Set<Vec2i> pixels = new HashSet<>();
        for (int y = 0; y < sizeY; y++) {
            String line = imageLines.get(y);
            if (sizeX < 0) {
                sizeX = line.length();
            } else if (line.length() != sizeX) {
                throw new RuntimeException();
            }

            for (int x = 0; x < sizeX; x++) {
                if (line.charAt(x) == '#') {
                    pixels.add(new Vec2i(x, y));
                }
            }
        }

        startingImage = new Image(true, new Vec2i(0, 0), new Vec2i(sizeX, sizeY), pixels);
    }

    @Override
    public Object part1() {
        return startingImage.enhance(2, enhancementAlgorithm).pixels().size();
    }

    @Override
    public Object part2() {
        return startingImage.enhance(50, enhancementAlgorithm).pixels().size();
    }

    record Image(
            boolean lit,
            Vec2i min,
            Vec2i max,
            Set<Vec2i> pixels
    ) {

        boolean isLit(Vec2i pos) {
            return pixels.contains(pos) == lit;
        }

        int index(Vec2i pos) {
            int n = 0;
            for (int y = -1; y <= 1; y++) {
                for (int x = -1; x <= 1; x++) {
                    n <<= 1;
                    if (isLit(pos.add(new Vec2i(x, y)))) {
                        n |= 1;
                    }
                }
            }

            return n;
        }

        Image enhance(int n, String enhancementAlgorithm) {
            Image current = this;
            for (int i = 0; i < n; i++) {
                current = current.enhance(enhancementAlgorithm);
            }

            return current;
        }

        Image enhance(String enhancementAlgorithm) {
            Vec2i min = this.min.subtract(new Vec2i(1, 1));
            Vec2i max = this.max.add(new Vec2i(1, 1));

            boolean flip = (lit && enhancementAlgorithm.charAt(0) == '#') || (!lit && enhancementAlgorithm.charAt(ALGO_LENGTH - 1) == '.');
            Set<Vec2i> newPixels = new HashSet<>();
            for (int y = min.y(); y <= max.y(); y++) {
                for (int x = min.x(); x <= max.x(); x++) {
                    Vec2i pos = new Vec2i(x, y);
                    char result = enhancementAlgorithm.charAt(index(pos));
                    if ((lit && result == (flip ? '.' : '#')) || (!lit && result == (flip ? '#' : '-'))) {
                        newPixels.add(pos);
                    }
                }
            }

            return new Image(flip != lit, min, max, newPixels);
        }
    }
}
