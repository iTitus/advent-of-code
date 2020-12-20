package io.github.ititus.aoc.aoc20.day20;

public class CompositeImage extends TileView {

    private final TileView[] tiles;
    private final int size, tileSize;

    public CompositeImage(int size, int tileSize, TileView[] tiles) {
        this.size = size;
        this.tileSize = tileSize;
        this.tiles = tiles;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public int getSize() {
        return size * (tileSize - 2);
    }

    @Override
    public char get(int x, int y) {
        return tiles[(x / (tileSize - 2)) + (y / (tileSize - 2)) * size]
                .get(x % (tileSize - 2) + 1, y % (tileSize - 2) + 1);
    }
}
