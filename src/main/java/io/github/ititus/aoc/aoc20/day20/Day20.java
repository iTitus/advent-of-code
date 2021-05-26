package io.github.ititus.aoc.aoc20.day20;

import io.github.ititus.aoc.common.*;
import io.github.ititus.math.vector.Vec2i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Aoc(year = 2020, day = 20)
public class Day20 implements AocSolution {

    private static final int TILE_SIZE = 10;

    private int size;
    private TileView[] image;

    private static TileView[] buildImage(TileView[] image, List<ImageTile> remaining, int size, int index) {
        if (index >= size * size) {
            return image;
        }

        int x = index % size;
        int y = index / size;

        for (int i = 0; i < remaining.size(); i++) {
            List<ImageTile> remainingCopy = new ArrayList<>(remaining);
            ImageTile candidate = remainingCopy.remove(i);

            views:
            for (TileView view : candidate.getAllOrientations()) {
                for (Direction d : Direction.VALUES) {
                    Vec2i dir = d.getDirectionVector();
                    TileView neighbor = get(image, size, x + dir.x(), y + dir.y());
                    if (neighbor != null && !Arrays.equals(view.getBorder(d), neighbor.getBorder(d.getOpposite()))) {
                        continue views;
                    }
                }

                TileView[] imageCopy = image == null ? new TileView[index + 1] : Arrays.copyOf(image, index + 1);
                set(imageCopy, size, x, y, view);
                TileView[] ret = buildImage(imageCopy, remainingCopy, size, index + 1);
                if (ret != null) {
                    return ret;
                }
            }
        }

        return null;
    }

    private static TileView get(TileView[] image, int size, int x, int y) {
        if (image == null || x < 0 || x >= size || y < 0 || y >= size) {
            return null;
        }

        int i = x + y * size;
        if (i >= image.length) {
            return null;
        }

        return image[i];
    }

    private static void set(TileView[] image, int size, int x, int y, TileView view) {
        image[x + y * size] = view;
    }

    @Override
    public void executeTests() {
        readInput(new AocStringInput("""
                Tile 2311:
                ..##.#..#.
                ##..#.....
                #...##..#.
                ####.#...#
                ##.##.###.
                ##...#.###
                .#.#.#..##
                ..#....#..
                ###...#.#.
                ..###..###
                                
                Tile 1951:
                #.##...##.
                #.####...#
                .....#..##
                #...######
                .##.#....#
                .###.#####
                ###.##.##.
                .###....#.
                ..#.#..#.#
                #...##.#..
                                
                Tile 1171:
                ####...##.
                #..##.#..#
                ##.#..#.#.
                .###.####.
                ..###.####
                .##....##.
                .#...####.
                #.##.####.
                ####..#...
                .....##...
                                
                Tile 1427:
                ###.##.#..
                .#..#.##..
                .#.##.#..#
                #.#.#.##.#
                ....#...##
                ...##..##.
                ...#.#####
                .#.####.#.
                ..#..###.#
                ..##.#..#.
                                
                Tile 1489:
                ##.#.#....
                ..##...#..
                .##..##...
                ..#...#...
                #####...#.
                #..#.#.#.#
                ...#.#.#..
                ##.#...##.
                ..##.##.##
                ###.##.#..
                                
                Tile 2473:
                #....####.
                #..#.##...
                #.##..#...
                ######.#.#
                .#...#.#.#
                .#########
                .###.#..#.
                ########.#
                ##...##.#.
                ..###.#.#.
                                
                Tile 2971:
                ..#.#....#
                #...###...
                #.#.###...
                ##.##..#..
                .#####..##
                .#..####.#
                #..#.#..#.
                ..####.###
                ..#.#.###.
                ...#.#.#.#
                                
                Tile 2729:
                ...#.#.#.#
                ####.#....
                ..#.#.....
                ....#..#.#
                .##..##.#.
                .#.####...
                ####.#.#..
                ##.####...
                ##..#.##..
                #.##...##.
                                
                Tile 3079:
                #.#.#####.
                .#..######
                ..#.......
                ######....
                ####.#..#.
                .#...#.##.
                #.#####.##
                ..#.###...
                ..#.......
                ..#.###...
                """));
        System.out.println("Part 1 (expected 20899048083289): " + part1());
        System.out.println("Part 2 (expected 273): " + part2());
    }

    @Override
    public void readInput(AocInput input) {
        List<ImageTile> tiles = stream(input.readString().split("\n\n"))
                .map(s -> s.split("\n"))
                .map(s -> new ImageTile(TILE_SIZE, s))
                .collect(Collectors.toList());

        size = (int) Math.sqrt(tiles.size());
        if (size * size != tiles.size()) {
            throw new RuntimeException();
        }

        image = buildImage(null, tiles, size, 0);
    }

    @Override
    public Object part1() {
        long l = get(image, size, 0, 0).getId();
        l *= get(image, size, size - 1, 0).getId();
        l *= get(image, size, 0, size - 1).getId();
        l *= get(image, size, size - 1, size - 1).getId();
        return l;
    }

    @Override
    public Object part2() {
        String[] seaMonsterPattern = new String[] {
                "                  # ",
                "#    ##    ##    ###",
                " #  #  #  #  #  #   "
        };
        int seaMonsterHeight = seaMonsterPattern.length;
        int seaMonsterLength = seaMonsterPattern[0].length();
        long seaMonsterMass = stream(seaMonsterPattern).flatMapToInt(String::chars).filter(c -> c == '#').count();

        TileView realImage = new CompositeImage(size, TILE_SIZE, image);

        int seaMonsterCount = 0;
        for (TileView v : realImage.getAllOrientations()) {
            for (int yStart = 0; yStart < realImage.getSize() - seaMonsterHeight; yStart++) {
                startPos:
                for (int xStart = 0; xStart < realImage.getSize() - seaMonsterLength; xStart++) {
                    for (int y = 0; y < seaMonsterHeight; y++) {
                        for (int x = 0; x < seaMonsterLength; x++) {
                            char pattern = seaMonsterPattern[y].charAt(x);
                            if (pattern != ' ' && pattern != v.get(xStart + x, yStart + y)) {
                                continue startPos;
                            }
                        }
                    }
                    seaMonsterCount++;
                }
            }
            if (seaMonsterCount > 0) {
                break;
            }
        }

        long waterRoughness = 0;
        for (int y = 0; y < realImage.getSize(); y++) {
            for (int x = 0; x < realImage.getSize(); x++) {
                if (realImage.get(x, y) == '#') {
                    waterRoughness++;
                }
            }
        }

        return waterRoughness - seaMonsterCount * seaMonsterMass;
    }
}
