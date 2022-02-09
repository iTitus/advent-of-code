package io.github.ititus.aoc.aoc20.day12;

import io.github.ititus.aoc.common.Direction;
import io.github.ititus.commons.math.vector.Vec2i;

import java.util.List;

public class Ship {

    private Vec2i pos = new Vec2i();
    private Vec2i waypoint = new Vec2i(10, -1);
    private Direction facing = Direction.EAST;

    public void execute(List<String> commands, boolean part2) {
        for (String command : commands) {
            char c = command.charAt(0);
            int amount = Integer.parseInt(command.substring(1));
            switch (c) {
                case 'N' -> move(Direction.NORTH, amount, part2);
                case 'S' -> move(Direction.SOUTH, amount, part2);
                case 'E' -> move(Direction.EAST, amount, part2);
                case 'W' -> move(Direction.WEST, amount, part2);
                case 'F' -> move(part2 ? waypoint : facing.getDirectionVector(), amount, false);
                case 'L' -> turn(amount, part2, true);
                case 'R' -> turn(amount, part2, false);
                default -> throw new RuntimeException();
            }
        }
    }

    private void move(Direction dir, int amount, boolean useWaypoint) {
        move(dir.getDirectionVector(), amount, useWaypoint);
    }

    private void move(Vec2i dir, int amount, boolean useWaypoint) {
        if (useWaypoint) {
            waypoint = waypoint.add(dir.multiply(amount));
        } else {
            pos = pos.add(dir.multiply(amount));
        }
    }

    private void turn(int degrees, boolean useWaypoint, boolean left) {
        if (useWaypoint) {
            waypoint = left ? waypoint.rotateCW(degrees) : waypoint.rotateCCW(degrees);
        } else {
            facing = left ? facing.rotateCCW(degrees) : facing.rotateCW(degrees);
        }
    }

    public int getManhattanDistance() {
        return pos.manhattanDistance();
    }
}
