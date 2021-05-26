package io.github.ititus.aoc.aoc20.day17;

import io.github.ititus.math.vector.Vec4i;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConwayCube {

    private final boolean useW;
    private int minX, maxX;
    private int minY, maxY;
    private int minZ, maxZ;
    private int minW, maxW;
    private Set<Vec4i> map;

    public ConwayCube(List<String> lines, boolean useW) {
        this.useW = useW;
        this.map = new HashSet<>();

        int initialSizeX = lines.get(0).length();
        int initialSizeY = lines.size();

        minX = minY = 0;
        maxX = initialSizeX - 1;
        maxY = initialSizeY - 1;
        int z = minZ = maxZ = 0;
        int w = minW = maxW = 0;

        for (int y = 0; y < initialSizeY; y++) {
            char[] line = lines.get(y).toCharArray();
            for (int x = 0; x < initialSizeX; x++) {
                char c = line[x];
                if (c == '#') {
                    set(map, new Vec4i(x, y, z, w));
                } else if (c != '.') {
                    throw new RuntimeException();
                }
            }
        }
    }

    private static void set(Set<Vec4i> map, Vec4i pos) {
        map.add(pos);
    }

    public int run(int steps) {
        for (int i = 0; i < steps; i++) {
            step();
        }

        return map.size();
    }

    private void step() {
        Set<Vec4i> newMap = new HashSet<>();
        for (int w = useW ? minW - 1 : 0; w <= (useW ? maxW + 1 : 0); w++) {
            for (int z = minZ - 1; z <= maxZ + 1; z++) {
                for (int y = minY - 1; y <= maxY + 1; y++) {
                    for (int x = minX - 1; x <= maxX + 1; x++) {
                        Vec4i pos = new Vec4i(x, y, z, w);
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

                            if (z < minZ) {
                                minZ = z;
                            } else if (z > maxZ) {
                                maxZ = z;
                            }

                            if (w < minW) {
                                minW = w;
                            } else if (w > maxW) {
                                maxW = w;
                            }

                            set(newMap, pos);
                        }
                    }
                }
            }
        }

        map = newMap;
    }

    private boolean nextState(Vec4i pos) {
        int activeNeighbors = 0;
        for (int w = useW ? pos.w() - 1 : pos.w(); w <= (useW ? pos.w() + 1 : pos.w()); w++) {
            for (int z = pos.z() - 1; z <= pos.z() + 1; z++) {
                for (int y = pos.y() - 1; y <= pos.y() + 1; y++) {
                    for (int x = pos.x() - 1; x <= pos.x() + 1; x++) {
                        Vec4i neighbor = new Vec4i(x, y, z, w);
                        if (neighbor.equals(pos)) {
                            continue;
                        }

                        if (get(neighbor)) {
                            activeNeighbors++;
                        }

                        if (activeNeighbors > 3) {
                            return false;
                        }
                    }
                }
            }
        }

        return activeNeighbors == 3 || (get(pos) && activeNeighbors == 2);
    }

    private boolean get(Vec4i pos) {
        return map.contains(pos);
    }
}
