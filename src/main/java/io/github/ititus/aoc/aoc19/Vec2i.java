package io.github.ititus.aoc.aoc19;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Vec2i {

    private final int x, y;

    public Vec2i() {
        this(0, 0);
    }

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2i add(Vec2i o) {
        return new Vec2i(x + o.x, y + o.y);
    }

    public Vec2i subtract(Vec2i o) {
        return new Vec2i(x - o.x, y - o.y);
    }

    public Vec2i multiply(int n) {
        return new Vec2i(n * x, n * y);
    }

    /**
     * Moves this position by the given command.
     *
     * @param command "&lt;direction&gt;&lt;number&gt;"
     * @return path from this position to the target position
     */
    public List<Vec2i> move(String command) {
        char c = command.charAt(0);
        int dirX = 0, dirY = 0;

        if (c == 'R') {
            dirX = 1;
        } else if (c == 'L') {
            dirX = -1;
        } else if (c == 'U') {
            dirY = 1;
        } else if (c == 'D') {
            dirY = -1;
        } else {
            throw new IllegalArgumentException();
        }

        List<Vec2i> path = new ArrayList<>();
        int amount = Integer.parseInt(command.substring(1));
        for (int i = 1; i <= amount; i++) {
            path.add(new Vec2i(x + i * dirX, y + i * dirY));
        }

        return path;
    }

    public int manhattanDistance() {
        return Math.abs(x) + Math.abs(y);
    }

    public double distance() {
        return Math.hypot(x, y);
    }

    public double distanceTo(Vec2i other) {
        return Math.hypot(x - other.x, y - other.y);
    }

    public int innerProduct(Vec2i other) {
        return x * other.x + y * other.y;
    }

    /**
     * In clockwise rotation, always returns positive angle in [0, 2pi).
     */
    public double getAngleTo(Vec2i other) {
        double angle = Math.atan2(other.y, other.x) - Math.atan2(y, x);
        if (angle < 0) {
            angle += 2 * Math.PI;
        }

        return angle;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vec2i)) {
            return false;
        }
        Vec2i v = (Vec2i) o;
        return x == v.x && y == v.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';
    }
}
