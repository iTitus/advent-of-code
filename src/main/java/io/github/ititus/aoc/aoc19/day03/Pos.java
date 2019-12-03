package io.github.ititus.aoc.aoc19.day03;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Pos {

    private final int x, y;

    public Pos() {
        this(0, 0);
    }

    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public List<Pos> move(String command) {
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

        List<Pos> pos = new ArrayList<>();
        int amount = Integer.parseInt(command.substring(1));
        for (int i = 1; i <= amount; i++) {
            pos.add(new Pos(x + i * dirX, y + i * dirY));
        }

        return pos;
    }

    public int manhattanDistance() {
        return Math.abs(x) + Math.abs(y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pos)) {
            return false;
        }
        Pos pos = (Pos) o;
        return x == pos.x && y == pos.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Pos{" + "x=" + x + ", y=" + y + '}';
    }
}
