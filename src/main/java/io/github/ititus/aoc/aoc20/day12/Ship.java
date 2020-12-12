package io.github.ititus.aoc.aoc20.day12;

import io.github.ititus.aoc.common.Direction;
import io.github.ititus.math.vector.Vec2i;

import java.util.List;

public class Ship {

    private Vec2i pos = new Vec2i();
    private Vec2i waypoint = new Vec2i(10, -1);
    private Direction facing = Direction.EAST;

    private static Vec2i rotateVectorCCW(Vec2i vec, int degrees) {
        if (degrees % 90 != 0) {
            throw new RuntimeException();
        }
        degrees /= 90;
        degrees = Math.floorMod(degrees, 4);

        Vec2i out = vec;
        for (int i = 0; i < degrees; i++) {
            out = new Vec2i(out.getY(), -out.getX());
        }
        return out;
    }

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
                case 'L' -> turnCCW(amount, part2);
                case 'R' -> turnCCW(-amount, part2);
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

    private void turnCCW(int degrees, boolean useWaypoint) {
        if (useWaypoint) {
            waypoint = rotateVectorCCW(waypoint, degrees);
        } else {
            facing = facing.rotateCCW(degrees);
        }
    }

    public int getManhattanDistance() {
        return pos.manhattanDistance();
    }
}
