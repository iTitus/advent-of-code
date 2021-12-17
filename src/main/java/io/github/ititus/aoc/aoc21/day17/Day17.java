package io.github.ititus.aoc.aoc21.day17;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;
import io.github.ititus.data.pair.IntIntPair;
import io.github.ititus.data.pair.Pair;
import io.github.ititus.math.vector.Vec2i;

import java.util.HashSet;
import java.util.OptionalInt;
import java.util.Set;

@Aoc(year = 2021, day = 17)
public class Day17 implements AocSolution {

    Vec2i min;
    Vec2i max;

    private static IntIntPair parse(String s) {
        String[] split = s.substring(2).split("\\.\\.", 2);
        return Pair.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    static OptionalInt getInitialVelocityYToReachTargetY(int targetY, int steps) {
        if (steps < 0) {
            throw new IllegalArgumentException();
        } else if (steps == 0) {
            return targetY == 0 ? OptionalInt.of(0) : OptionalInt.empty();
        }

        int num = 2 * targetY + steps * steps - steps;
        int denom = 2 * steps;
        return num % denom == 0 ? OptionalInt.of(num / denom) : OptionalInt.empty();
    }

    static OptionalInt getInitialVelocityXToReachTargetX(int targetX, int steps) {
        if (steps < 0) {
            throw new IllegalArgumentException();
        } else if (steps == 0) {
            return targetX == 0 ? OptionalInt.of(0) : OptionalInt.empty();
        } else if (targetX == 0) {
            return OptionalInt.of(0);
        }

        for (int vx = Math.abs(targetX) + 1; vx >= 0; vx--) {
            int x = sumStepsX(vx, steps);
            if (x == targetX) {
                return OptionalInt.of(vx);
            }
        }

        return OptionalInt.empty();
    }

    static int sumStepsX(int vx, int steps) {
        int x = 0;
        for (int i = 0; i < steps; i++) {
            x += vx;
            if (vx > 1) {
                vx--;
            } else {
                break;
            }
        }

        return x;
    }

    @Override
    public void executeTests() {
        AocInput input = new AocStringInput("target area: x=20..30, y=-10..-5");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());
    }

    @Override
    public void readInput(AocInput input) {
        String s = input.readString().strip().substring("target area: ".length());
        String[] split = s.split(", ", 2);
        IntIntPair x = parse(split[0]);
        IntIntPair y = parse(split[1]);
        min = new Vec2i(Math.min(x.aInt(), x.bInt()), Math.min(y.aInt(), y.bInt()));
        max = new Vec2i(Math.max(x.aInt(), x.bInt()), Math.max(y.aInt(), y.bInt()));

        if (max.y() >= 0 || min.x() <= 0) {
            throw new RuntimeException();
        }
    }

    @Override
    public Object part1() {
        // assume min.y() < 0 and there exists vx, such that an x-coordinate of the target area can be reached
        int vy = Math.abs(min.y()) - 1;
        return (vy * (vy + 1)) / 2;
    }

    @Override
    public Object part2() {
        // the trajectory with the highest y-value (part 1!) will be the longest=most steps
        int maxvy = Math.abs(min.y()) - 1;
        int maxSteps = (2 * maxvy + 1) + 1;
        Set<Vec2i> vels = new HashSet<>();
        for (int steps = 1; steps <= maxSteps; steps++) {
            for (int y = min.y(); y <= max.y(); y++) {
                for (int x = min.x(); x <= max.x(); x++) {
                    OptionalInt vy = getInitialVelocityYToReachTargetY(y, steps);
                    if (vy.isEmpty()) {
                        continue;
                    }

                    OptionalInt vx = getInitialVelocityXToReachTargetX(x, steps);
                    if (vx.isEmpty()) {
                        continue;
                    }

                    vels.add(new Vec2i(vx.getAsInt(), vy.getAsInt()));
                }
            }
        }

        return vels.size();
    }
}
