package io.github.ititus.aoc.aoc19.day20;

import io.github.ititus.aoc.common.Direction;
import io.github.ititus.commons.math.vector.Vec2i;
import io.github.ititus.commons.math.vector.Vec3i;

import java.util.*;

public class PlutoMaze {

    private final boolean recursive;
    private final Map<Vec2i, Portal> portals;
    private final Map<String, Vec2i> labeledPos;

    private int sizeX = -1;
    private int sizeY = -1;
    private char[][] map;

    public PlutoMaze(boolean recursive, List<String> input) {
        this.recursive = recursive;
        this.portals = new HashMap<>();
        this.labeledPos = new HashMap<>();
        parse(input);
    }

    private void parse(List<String> input) {
        sizeY = input.size();
        sizeX = input.get(0).length();
        map = new char[sizeX][sizeY];

        Set<Vec2i> labels = new HashSet<>();

        for (int y = 0; y < sizeY; y++) {
            String line = input.get(y);
            for (int x = 0; x < sizeX; x++) {
                char c = line.charAt(x);

                if ('A' <= c && c <= 'Z') {
                    labels.add(new Vec2i(x, y));
                }

                map[x][y] = c;
            }
        }

        Vec2i mid = new Vec2i(sizeX / 2, sizeY / 2);
        Set<Vec2i> visited = new HashSet<>();

        for (Vec2i labelPos : labels) {
            if (visited.contains(labelPos)) {
                continue;
            }

            visited.add(labelPos);
            StringBuilder b = new StringBuilder(2).append(map[labelPos.x()][labelPos.y()]);

            Vec2i o = labelPos.add(Direction.EAST.getDirectionVector());
            if (labels.contains(o)) {
                visited.add(o);
                b.append(map[o.x()][o.y()]);

                String label = b.toString();
                Vec2i portalPos = labeledPos.get(label);
                if (portalPos != null) {
                    Vec2i p = findPortalEntrance(labelPos, o);

                    Vec2i pos1 = portalPos.add(mid.subtract(portalPos).sgn());
                    Vec2i pos2 = p.add(mid.subtract(p).sgn());
                    char c1 = map[pos1.x()][pos1.y()];
                    char c2 = map[pos2.x()][pos2.y()];
                    boolean outer1 = c1 == '#' || c1 == '.';
                    boolean outer2 = c2 == '#' || c2 == '.';
                    if (outer1 == outer2) {
                        throw new RuntimeException();
                    }
                    Portal portal = outer1 ? new Portal(p, portalPos) : new Portal(portalPos, p);

                    portals.put(portalPos, portal);
                    portals.put(p, portal);
                    labeledPos.remove(label);
                } else {
                    labeledPos.put(label, findPortalEntrance(labelPos, o));
                }
            }

            o = labelPos.add(Direction.SOUTH.getDirectionVector());
            if (labels.contains(o)) {
                visited.add(o);
                b.append(map[o.x()][o.y()]);

                String label = b.toString();
                Vec2i portalPos = labeledPos.get(label);
                if (portalPos != null) {
                    Vec2i p = findPortalEntrance(labelPos, o);

                    Vec2i pos1 = portalPos.add(mid.subtract(portalPos).sgn());
                    Vec2i pos2 = p.add(mid.subtract(p).sgn());
                    char c1 = map[pos1.x()][pos1.y()];
                    char c2 = map[pos2.x()][pos2.y()];
                    boolean outer1 = c1 == '#' || c1 == '.';
                    boolean outer2 = c2 == '#' || c2 == '.';
                    if (outer1 == outer2) {
                        throw new RuntimeException();
                    }
                    Portal portal = outer1 ? new Portal(p, portalPos) : new Portal(portalPos, p);

                    portals.put(portalPos, portal);
                    portals.put(p, portal);
                    labeledPos.remove(label);
                } else {
                    labeledPos.put(label, findPortalEntrance(labelPos, o));
                }
            }
        }
    }

    private boolean isInBounds(Vec2i pos) {
        return pos.x() >= 0 && pos.y() >= 0 && pos.x() < sizeX && pos.y() < sizeY;
    }

    private Vec2i findPortalEntrance(Vec2i main, Vec2i other) {
        boolean horizontal = other.x() - main.x() != 0;

        Direction first = horizontal ? Direction.WEST : Direction.NORTH;
        Direction second = horizontal ? Direction.EAST : Direction.SOUTH;

        Vec2i p = main.add(first.getDirectionVector());
        if (!isInBounds(p) || map[p.x()][p.y()] != '.') {
            p = other.add(second.getDirectionVector());
            if (!isInBounds(p) || map[p.x()][p.y()] != '.') {
                throw new RuntimeException();
            }
        }

        return p;
    }

    public int findShortestPath(String start, String end) {
        Vec2i startPos2 = Objects.requireNonNull(labeledPos.get(start));
        Vec2i endPos2 = Objects.requireNonNull(labeledPos.get(end));

        Vec3i startPos = new Vec3i(startPos2.x(), startPos2.y(), 0);
        Vec3i endPos = new Vec3i(endPos2.x(), endPos2.y(), 0);

        Queue<Vec3i> toVisit = new ArrayDeque<>();
        Set<Vec3i> visited = new HashSet<>();

        Vec3i nullMarker = new Vec3i();
        int depth = 0;

        toVisit.offer(startPos);
        toVisit.offer(nullMarker);
        visited.add(startPos);

        while (!toVisit.isEmpty()) {
            Vec3i p = toVisit.poll();
            if (p == nullMarker) {
                depth++;
                if (!toVisit.isEmpty()) {
                    toVisit.offer(nullMarker);
                }
                continue;
            }

            int currentLevel = p.z();
            Vec2i p2 = new Vec2i(p.x(), p.y());

            for (Direction d : Direction.VALUES) {
                Vec2i o2 = p2.add(d.getDirectionVector());
                Vec3i o = new Vec3i(o2.x(), o2.y(), currentLevel);

                if (!visited.contains(o)) {
                    char c = map[o.x()][o.y()];

                    if (endPos.equals(o)) {
                        return depth + 1;
                    } else if (c == '.') {
                        toVisit.offer(o);
                        visited.add(o);
                    } else if (c != '#') {
                        Portal portal = portals.get(p2);
                        if (portal != null) {
                            Vec3i otherEnd = portal.getOtherEnd(recursive, p);
                            if (otherEnd != null) {
                                toVisit.offer(otherEnd);
                                visited.add(otherEnd);
                            }
                        }
                        visited.add(o);
                    }
                }
            }
        }

        return -1;
    }
}
