package io.github.ititus.aoc.aoc20.day11;

import java.util.Arrays;
import java.util.List;

public class SeatGrid {

    private final int width, height;
    private final byte[] grid;

    public SeatGrid(List<String> lines) {
        this.width = lines.get(0).length();
        this.height = lines.size();
        this.grid = new byte[width * height];

        for (int y = 0; y < height; y++) {
            String line = lines.get(y);
            for (int x = 0; x < width; x++) {
                char c = line.charAt(x);
                set(grid, width, x, y, SeatState.of(c));
            }
        }
    }

    private static void set(byte[] grid, int width, int x, int y, SeatState state) {
        grid[x + y * width] = state.getId();
    }

    public boolean step(boolean part2) {
        byte[] newGrid = new byte[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                set(newGrid, width, x, y, nextState(x, y, part2));
            }
        }

        if (Arrays.equals(grid, newGrid)) {
            return false;
        }

        System.arraycopy(newGrid, 0, grid, 0, grid.length);
        return true;
    }

    public int run(boolean part2) {
        //noinspection StatementWithEmptyBody
        while (step(part2))
            ;
        return countOccupied();
    }

    private int countOccupied() {
        int occupied = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (get(x, y) == SeatState.OCCUPIED) {
                    occupied++;
                }
            }
        }
        return occupied;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                b.append(get(x, y).getPrintChar());
            }
            b.append('\n');
        }
        return b.toString();
    }

    private SeatState get(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return SeatState.OUT_OF_MAP;
        }
        return SeatState.of(grid[x + y * width]);
    }

    private SeatState nextState(int x, int y, boolean part2) {
        SeatState state = get(x, y);
        if (state == null || state == SeatState.OUT_OF_MAP) {
            throw new IndexOutOfBoundsException();
        } else if (state == SeatState.FLOOR) {
            return SeatState.FLOOR;
        }

        int neighbors = 0;
        for (int yOffset = -1; yOffset <= 1; yOffset++) {
            for (int xOffset = -1; xOffset <= 1; xOffset++) {
                if (xOffset == 0 && yOffset == 0) {
                    continue;
                }

                for (int i = 1; part2 || i <= 1; i++) {
                    SeatState visible = get(x + i * xOffset, y + i * yOffset);
                    if (visible == SeatState.FLOOR) {
                        continue;
                    } else if (visible == SeatState.OCCUPIED) {
                        neighbors++;
                    }
                    break;
                }
            }
        }

        if (state == SeatState.EMPTY && neighbors == 0) {
            return SeatState.OCCUPIED;
        } else if (state == SeatState.OCCUPIED && neighbors >= (part2 ? 5 : 4)) {
            return SeatState.EMPTY;
        }

        return state;
    }

}
