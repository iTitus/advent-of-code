package io.github.ititus.aoc.aoc20.day03;

import io.github.ititus.commons.math.vector.Vec2i;

import java.util.BitSet;
import java.util.List;
import java.util.stream.Stream;

public class TreeMap {

    private final int width, height;
    private final BitSet trees;

    public TreeMap(List<String> input) {
        this.height = input.size();
        this.width = input.get(0).length();

        this.trees = new BitSet(width * height);

        for (int y = 0; y < height; y++) {
            String line = input.get(y);
            for (int x = 0; x < width; x++) {
                char tree = line.charAt(x);
                if (tree == '#') {
                    setTree(x, y);
                } else if (tree != '.') {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    private void setTree(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            throw new IndexOutOfBoundsException();
        }
        trees.set(y * width + x);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean hasTree(Vec2i pos) {
        return hasTree(pos.x(), pos.y());
    }

    public boolean hasTree(int x, int y) {
        if (x < 0 || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException();
        }
        x %= width;
        return trees.get(y * width + x);
    }

    public long countTreesEncountered(Vec2i slope) {
        return Stream
                .iterate(new Vec2i(), pos -> pos.y() < height, slope::add)
                .filter(this::hasTree)
                .count();
    }
}
