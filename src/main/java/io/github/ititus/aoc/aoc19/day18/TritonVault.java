package io.github.ititus.aoc.aoc19.day18;

import io.github.ititus.aoc.aoc19.Direction;
import io.github.ititus.math.vector.Vec2i;

import java.util.*;

public class TritonVault {

    private final Map<RobotState, Visibility> visibilityCache;
    private final Set<Vec2i> allEntrances;

    private char[][] map;
    private Key[] allKeys;
    private Door[] allDoors;

    public TritonVault(boolean changeMap, List<String> input) {
        this.visibilityCache = new HashMap<>();
        this.allEntrances = new HashSet<>();
        parse(changeMap, input);
    }

    public int findBestPathLength() {
        RobotState initialState = RobotState.start(allEntrances);
        return findBestPathLengthRecursive(initialState, Integer.MAX_VALUE);
    }

    private int findBestPathLengthRecursive(RobotState state, int maxLength) {
        if (state.getCollectedCount() == allKeys.length) {
            return 0;
        }

        int minDistance = Integer.MAX_VALUE;

        if (maxLength > 0) {
            Visibility visibility = getVisibility(state);

            int cachedMinDist = visibility.getMinDistance();
            if (cachedMinDist < Integer.MAX_VALUE) {
                return cachedMinDist;
            }

            for (int i = 0; i < state.getRobotCount(); i++) {
                for (Map.Entry<Key, Integer> keyDistance : visibility.getKeyDistances(i).entrySet()) {
                    Key key = keyDistance.getKey();
                    int distance = keyDistance.getValue();

                    if (distance <= maxLength && distance < minDistance) {
                        int subPathMinLength = findBestPathLengthRecursive(state.collectKey(i, key), Math.min(maxLength - distance, minDistance - distance - 1));
                        if (subPathMinLength < Integer.MAX_VALUE) {
                            distance += subPathMinLength;

                            if (distance < minDistance) {
                                minDistance = distance;
                            }
                        }
                    }
                }
            }

            if (minDistance < Integer.MAX_VALUE) {
                visibility.setMinDistance(minDistance);
            }
        }

        return minDistance;
    }

    private Visibility getVisibility(RobotState state) {
        Visibility visibility = visibilityCache.get(state);
        if (visibility == null) {
            visibility = calculateVisibility(state);
            visibilityCache.put(state, visibility);
        }
        return visibility;
    }

    private Visibility calculateVisibility(RobotState state) {
        int robotCount = state.getRobotCount();
        Visibility visibility = new Visibility(robotCount);

        Queue<Vec2i> toVisit = new ArrayDeque<>();
        Set<Vec2i> visited = new HashSet<>();

        Vec2i nullMarker = new Vec2i();

        for (int i = 0; i < robotCount; i++) {
            visited.clear();

            Vec2i startPos = state.getPos(i);
            visibility.setPos(i, startPos);

            int depth = 0;

            toVisit.offer(startPos);
            toVisit.offer(nullMarker);
            visited.add(startPos);

            while (!toVisit.isEmpty()) {
                Vec2i p = toVisit.poll();
                if (p == nullMarker) {
                    depth++;
                    if (!toVisit.isEmpty()) {
                        toVisit.offer(nullMarker);
                    }
                    continue;
                }

                for (Direction d : Direction.VALUES) {
                    Vec2i o = p.add(d.getDirectionVector());
                    if (!visited.contains(o)) {
                        char c = map[o.getX()][o.getY()];
                        if (c == '.' || c == '@') {
                            toVisit.offer(o);
                            visited.add(o);
                        } else if ('A' <= c && c <= 'Z') {
                            if (state.isOpen(c)) {
                                toVisit.offer(o);
                            }
                            visited.add(o);
                        } else if ('a' <= c && c <= 'z') {
                            if (state.isCollected(c)) {
                                toVisit.offer(o);
                            } else {
                                visibility.addVisibleKey(i, getKey(c), depth + 1);
                            }
                            visited.add(o);
                        }
                    }
                }
            }
        }

        return visibility;
    }

    private void parse(boolean changeMap, List<String> input) {
        Set<Vec2i> allKeyPos = new HashSet<>();
        Set<Vec2i> allDoorPos = new HashSet<>();

        int sizeY = input.size();
        int sizeX = input.get(0).length();
        map = new char[sizeX][sizeY];

        Vec2i firstEntrance = null;

        for (int y = 0; y < sizeY; y++) {
            String line = input.get(y);
            for (int x = 0; x < sizeX; x++) {
                char c = line.charAt(x);
                if (c == '@') {
                    if (firstEntrance != null && changeMap) {
                        throw new RuntimeException();
                    }
                    firstEntrance = new Vec2i(x, y);
                }
                map[x][y] = c;
            }
        }

        if (changeMap) {
            Objects.requireNonNull(firstEntrance);

            int x = firstEntrance.getX();
            int y = firstEntrance.getY();

            map[x - 1][y - 1] = '@';
            map[x][y - 1] = '#';
            map[x + 1][y - 1] = '@';
            map[x - 1][y] = '#';
            map[x][y] = '#';
            map[x + 1][y] = '#';
            map[x - 1][y + 1] = '@';
            map[x][y + 1] = '#';
            map[x + 1][y + 1] = '@';
        }

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                Vec2i pos = new Vec2i(x, y);
                char c = map[x][y];
                if (c == '@') {
                    allEntrances.add(pos);
                } else if ('A' <= c && c <= 'Z') { // door
                    allDoorPos.add(pos);
                } else if ('a' <= c && c <= 'z') { // key
                    allKeyPos.add(pos);
                }
            }
        }

        int size = Math.max(allKeyPos.size(), allDoorPos.size());
        allKeys = new Key[size];
        allDoors = new Door[size];

        for (Vec2i keyPos : allKeyPos) {
            char keyChar = map[keyPos.getX()][keyPos.getY()];

            Key key = new Key(keyChar, keyPos);

            setKey(keyChar, key);
        }
        for (Vec2i doorPos : allDoorPos) {
            char doorChar = map[doorPos.getX()][doorPos.getY()];

            Key key = getKeyForDoor(doorChar);
            Door door = new Door(doorChar, doorPos);

            door.setKey(key);
            key.setDoor(door);

            setDoor(doorChar, door);
        }
    }

    private Key getKey(char keyChar) {
        return allKeys[keyChar - 'a'];
    }

    private Key getKeyForDoor(char doorChar) {
        return allKeys[doorChar - 'A'];
    }

    private void setKey(char keyChar, Key key) {
        allKeys[keyChar - 'a'] = key;
    }

    private Door getDoor(char doorChar) {
        return allDoors[doorChar - 'A'];
    }

    private Door getDoorForKey(char keyChar) {
        return allDoors[keyChar - 'a'];
    }

    private void setDoor(char doorChar, Door door) {
        allDoors[doorChar - 'A'] = door;
    }

    public void printState() {
        System.out.println("#".repeat(80));
        visibilityCache.forEach((k, v) -> System.out.println(k + " -> " + v));
        System.out.println("#".repeat(80));
    }
}
