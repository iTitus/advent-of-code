package io.github.ititus.aoc.aoc19.day18;

import io.github.ititus.math.vector.Vec2i;

import java.util.Arrays;
import java.util.Set;

public class RobotState {

    private final Vec2i[] pos;
    private final int collectedKeys;

    private RobotState(Vec2i[] pos, int collectedKeys) {
        this.pos = pos;
        this.collectedKeys = collectedKeys;
    }

    public static RobotState start(Set<Vec2i> startPosSet) {
        return new RobotState(startPosSet.toArray(new Vec2i[0]), 0);
    }

    public RobotState collectKey(int robotIndex, Key key) {
        Vec2i[] newPos = Arrays.copyOf(pos, pos.length);
        newPos[robotIndex] = key.getKeyPos();
        return new RobotState(newPos, collectedKeys | (1 << (key.getKeyChar() - 'a')));
    }

    public int getRobotCount() {
        return pos.length;
    }

    public Vec2i getPos(int robotIndex) {
        return pos[robotIndex];
    }

    public int getCollectedCount() {
        return Integer.bitCount(collectedKeys);
    }

    public boolean isOpen(char doorChar) {
        char keyChar = (char) (doorChar + ('a' - 'A'));
        return isCollected(keyChar);
    }

    public boolean isCollected(char keyChar) {
        return (collectedKeys & (1 << (keyChar - 'a'))) != 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RobotState)) {
            return false;
        }
        RobotState that = (RobotState) o;
        return Arrays.equals(pos, that.pos) && collectedKeys == that.collectedKeys;
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(collectedKeys);
        result = 31 * result + Arrays.hashCode(pos);
        return result;
    }

    @Override
    public String toString() {
        return "RobotState{pos=" + Arrays.toString(pos) + ", collectedKeys=" + collectedKeys + '}';
    }
}
