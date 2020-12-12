package io.github.ititus.aoc.aoc19.day17;

import io.github.ititus.aoc.common.Direction;

public enum ExteriorType {

    SCAFFOLDING,
    OPEN_SPACE,
    ROBOT_UP,
    ROBOT_DOWN,
    ROBOT_LEFT,
    ROBOT_RIGHT,
    TUMBLING_ROBOT;

    public static ExteriorType get(int i) {
        switch (i) {
            case '#':
                return SCAFFOLDING;
            case '^':
                return ROBOT_UP;
            case 'v':
                return ROBOT_DOWN;
            case '<':
                return ROBOT_LEFT;
            case '>':
                return ROBOT_RIGHT;
            case 'X':
                return TUMBLING_ROBOT;
            case '.':
                return OPEN_SPACE;
            default:
                throw new IllegalArgumentException();
        }
    }

    public boolean isRobot() {
        return this == ROBOT_UP || this == ROBOT_DOWN || this == ROBOT_LEFT || this == ROBOT_RIGHT;
    }

    public Direction getDirection() {
        switch (this) {
            case ROBOT_UP:
                return Direction.NORTH;
            case ROBOT_DOWN:
                return Direction.SOUTH;
            case ROBOT_LEFT:
                return Direction.WEST;
            case ROBOT_RIGHT:
                return Direction.EAST;
            default:
                throw new RuntimeException();
        }
    }
}
