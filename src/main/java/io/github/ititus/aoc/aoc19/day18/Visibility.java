package io.github.ititus.aoc.aoc19.day18;

import io.github.ititus.math.vector.Vec2i;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Visibility {

    private final Vec2i[] pos;
    private final Map<Key, Integer>[] keyDistances;

    private int minDistance = Integer.MAX_VALUE;

    @SuppressWarnings("unchecked")
    public Visibility(int robotCount) {
        this.pos = new Vec2i[robotCount];
        this.keyDistances = new Map[robotCount];
        for (int i = 0; i < keyDistances.length; i++) {
            this.keyDistances[i] = new HashMap<>();
        }
    }

    public void addVisibleKey(int robotIndex, Key key, int distance) {
        keyDistances[robotIndex].put(key, distance);
    }

    public void setPos(int robotIndex, Vec2i pos) {
        this.pos[robotIndex] = pos;
    }

    public int getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(int minDistance) {
        this.minDistance = minDistance;
    }

    public Map<Key, Integer> getKeyDistances(int robotIndex) {
        return keyDistances[robotIndex];
    }

    @Override
    public String toString() {
        // TODO: ???
        return "Visibility{" + "pos=" + Arrays.toString(pos) + /*", minDistance=" + minDistance
        +*/ ", keyDistances=" + Arrays.toString(keyDistances) + '}';
    }
}
