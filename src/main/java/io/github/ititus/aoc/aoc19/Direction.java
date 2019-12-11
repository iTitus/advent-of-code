package io.github.ititus.aoc.aoc19;

import io.github.ititus.math.vector.Vec2i;

public enum Direction {

    NORTH(0, -1),
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0);

    private final Vec2i directionVector;

    Direction(int x, int y) {
        this.directionVector = new Vec2i(x, y);
    }

    public Direction rotateCW() {
        switch (this) {
            case NORTH:
                return EAST;
            case EAST:
                return SOUTH;
            case SOUTH:
                return WEST;
            case WEST:
                return NORTH;
            default:
                throw new RuntimeException();
        }
    }

    public Direction rotateCCW() {
        switch (this) {
            case NORTH:
                return WEST;
            case EAST:
                return NORTH;
            case SOUTH:
                return EAST;
            case WEST:
                return SOUTH;
            default:
                throw new RuntimeException();
        }
    }

    public Vec2i getDirectionVector() {
        return directionVector;
    }
}
