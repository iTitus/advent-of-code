package io.github.ititus.aoc.aoc20.day20;

public class ImageTile extends TileView {

    private final int id;
    private final int size;
    private final char[] image;

    public ImageTile(int size, String[] lines) {
        if (lines.length != size + 1) {
            throw new RuntimeException();
        }

        String idLine = lines[0];
        this.id = Integer.parseInt(idLine.substring(5, idLine.length() - 1));

        this.size = size;

        this.image = new char[size * size];
        for (int y = 0; y < size; y++) {
            String line = lines[y + 1];
            if (line.length() != size) {
                throw new RuntimeException();
            }
            for (int x = 0; x < size; x++) {
                set(x, y, line.charAt(x));
            }
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public char get(int x, int y) {
        return image[x + y * size];
    }

    private void set(int x, int y, char c) {
        image[x + y * size] = c;
    }
}
