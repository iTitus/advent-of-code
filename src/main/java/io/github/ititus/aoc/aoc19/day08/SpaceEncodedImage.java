package io.github.ititus.aoc.aoc19.day08;

import java.io.PrintWriter;
import java.util.*;

public class SpaceEncodedImage {

    private final int width;
    private final int height;
    private final List<int[]> layers;

    public SpaceEncodedImage(int width, int height) {
        this.width = width;
        this.height = height;
        this.layers = new ArrayList<>();
    }

    public void decode(String encodedImage) {
        int currentIndex = 0;
        int[] currentLayer = null;
        for (char c : encodedImage.toCharArray()) {
            if (c < '0' || c > '2') {
                throw new IllegalArgumentException();
            }

            int digit = c - '0';
            if (currentLayer == null) {
                currentLayer = new int[width * height];
                currentIndex = 0;
            }

            currentLayer[currentIndex++] = digit;

            if (currentIndex >= currentLayer.length) {
                layers.add(currentLayer);
                currentLayer = null;
            }
        }

        if (currentLayer != null) {
            throw new IllegalArgumentException();
        }
    }

    private int getPixel(int layer, int x, int y) {
        return layers.get(layer)[x + y * width];
    }

    public SpaceEncodedColor getVisiblePixel(int x, int y) {
        for (int layer = 0; layer < layers.size(); layer++) {
            SpaceEncodedColor c = SpaceEncodedColor.get(getPixel(layer, x, y));
            if (!c.isTransparent()) {
                return c;
            }
        }

        throw new RuntimeException();
    }

    public void render(PrintWriter out) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                out.print(getVisiblePixel(x, y).getRenderChar());
            }
            out.println();
        }
    }

    public int getChecksum() {
        int[] layer = Collections.min(layers, Comparator.comparingLong(l -> Math.toIntExact(Arrays.stream(l).filter(i -> i == 0).count())));

        int ones = Math.toIntExact(Arrays.stream(layer).filter(i -> i == 1).count());
        int twos = Math.toIntExact(Arrays.stream(layer).filter(i -> i == 2).count());

        return ones * twos;
    }
}
