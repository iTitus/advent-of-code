package io.github.ititus.aoc.aoc19;

import io.github.ititus.math.vector.Vec2i;

public enum Direction {

    NORTH(1, 0, -1),
    EAST(4, 1, 0),
    SOUTH(2, 0, 1),
    WEST(3, -1, 0);

    private final int index;
    private final Vec2i directionVector;

    Direction(int index, int x, int y) {
        this.index = index;
        this.directionVector = new Vec2i(x, y);
    }

    public Direction getOpposite() {
        switch (this) {
            case NORTH:
                return SOUTH;
            case EAST:
                return WEST;
            case SOUTH:
                return NORTH;
            case WEST:
                return EAST;
            default:
                throw new RuntimeException();
        }
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

    public int getIndex() {
        return index;
    }

    public Vec2i getDirectionVector() {
        return directionVector;
    }
}
