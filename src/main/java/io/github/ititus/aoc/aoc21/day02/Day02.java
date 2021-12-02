package io.github.ititus.aoc.aoc21.day02;

import io.github.ititus.aoc.common.*;
import io.github.ititus.math.vector.Vec2i;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;

import java.util.List;

@Aoc(year = 2021, day = 2)
public class Day02 implements AocSolution {

    private List<ObjectIntPair<Direction>> input;

    @Override
    public void executeTests() {
        AocInput in = new AocStringInput("""
                forward 5
                down 5
                forward 8
                up 3
                down 8
                forward 2""");
        readInput(in);
        System.out.println(part1());
        System.out.println(part2());
    }

    @Override
    public void readInput(AocInput input) {
        this.input = input.lines()
                .map(s -> s.split(" "))
                .map(arr -> ObjectIntPair.of(toDir(arr[0]), Integer.parseInt(arr[1])))
                .toList();
    }

    private static Direction toDir(String s) {
        return switch (s) {
            case "forward" -> Direction.EAST;
            case "down" -> Direction.SOUTH;
            case "up" -> Direction.NORTH;
            default -> throw new IllegalArgumentException();
        };
    }

    @Override
    public Object part1() {
        Vec2i pos = new Vec2i(0, 0);
        for (ObjectIntPair<Direction> pair : input) {
            pos = pos.add(pair.left().getDirectionVector().multiply(pair.rightInt()));
        }

        return pos.x() * pos.y();
    }

    @Override
    public Object part2() {
        Vec2i aim = new Vec2i(1, 0);
        Vec2i pos = new Vec2i(0, 0);
        for (ObjectIntPair<Direction> pair : input) {
            switch (pair.left()) {
                case SOUTH -> aim = aim.add(new Vec2i(0, pair.rightInt()));
                case NORTH -> aim = aim.subtract(new Vec2i(0, pair.rightInt()));
                case EAST -> pos = pos.add(aim.multiply(pair.rightInt()));
                default -> throw new IllegalArgumentException();
            }
        }

        return pos.x() * pos.y();
    }
}
