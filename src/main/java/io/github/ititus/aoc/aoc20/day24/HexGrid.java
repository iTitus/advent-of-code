package io.github.ititus.aoc.aoc20.day24;

import io.github.ititus.math.vector.Vec2i;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HexGrid {

    private static final Vec2i[] NEIGHBORS = new Vec2i[] {
            new Vec2i(-1, -1),
            new Vec2i(0, -1),
            new Vec2i(-1, 0),
            new Vec2i(1, 0),
            new Vec2i(0, 1),
            new Vec2i(1, 1)
    };

    private Set<Vec2i> blackTiles = new HashSet<>();
    private int minX, maxX;
    private int minY, maxY;

    public int getBlackTiles() {
        return blackTiles.size();
    }

    public void flip(List<String> lines) {
        lines.forEach(this::doFlip);
    }

    public void simulate(int days) {
        for (int i = 0; i < days; i++) {
            doSimulate();
        }
    }

    private void doFlip(String line) {
        int x = 0;
        int y = 0;
        for (int i = 0; i < line.length(); i++) {
            switch (line.charAt(i)) {
                case 'e' -> x++;
                case 'w' -> x--;
                case 's' -> {
                    y++;
                    switch (line.charAt(++i)) {
                        case 'e' -> x++;
                        case 'w' -> {
                        }
                        default -> throw new RuntimeException();
                    }
                }
                case 'n' -> {
                    y--;
                    switch (line.charAt(++i)) {
                        case 'w' -> x--;
                        case 'e' -> {
                        }
                        default -> throw new RuntimeException();
                    }
                }
                default -> throw new RuntimeException();
            }
        }

        Vec2i pos = new Vec2i(x, y);
        if (!blackTiles.add(pos)) {
            blackTiles.remove(pos);
        } else {
            if (x < minX) {
                minX = x;
            } else if (x > maxX) {
                maxX = x;
            }

            if (y < minY) {
                minY = y;
            } else if (y > maxY) {
                maxY = y;
            }
        }
    }

    private void doSimulate() {
        Set<Vec2i> newBlackTiles = new HashSet<>();

        for (int y = minY - 1; y <= maxY + 1; y++) {
            for (int x = minX - 1; x <= maxX + 1; x++) {
                Vec2i pos = new Vec2i(x, y);
                if (nextState(pos)) {
                    if (x < minX) {
                        minX = x;
                    } else if (x > maxX) {
                        maxX = x;
                    }

                    if (y < minY) {
                        minY = y;
                    } else if (y > maxY) {
                        maxY = y;
                    }

                    newBlackTiles.add(pos);
                }
            }
        }

        blackTiles = newBlackTiles;
    }

    private boolean nextState(Vec2i pos) {
        int activeNeighbors = 0;
        for (Vec2i neighbor : NEIGHBORS) {
            if (blackTiles.contains(neighbor.add(pos)) && ++activeNeighbors > 2) {
                return false;
            }
        }

        return activeNeighbors > 0 && (activeNeighbors == 2 || blackTiles.contains(pos));
    }
}
