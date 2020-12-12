package io.github.ititus.aoc.aoc19.day11;

import io.github.ititus.aoc.aoc19.IntComputer;
import io.github.ititus.aoc.common.Direction;
import io.github.ititus.data.Bag;
import io.github.ititus.math.vector.Vec2i;

import java.math.BigInteger;
import java.util.*;

public class EmergencyHullPaintingRobot {

    private final Set<Vec2i> painted;
    private final Map<Vec2i, HullColor> hull;
    private final Bag<Vec2i> pos;
    private final Bag<Direction> facing;
    private final IntComputer computer;

    public EmergencyHullPaintingRobot(BigInteger[] memory, boolean startOnWhite) {
        memory = Arrays.copyOf(memory, memory.length);
        this.painted = new HashSet<>();
        this.hull = new HashMap<>();
        this.pos = new Bag<>(new Vec2i());
        this.facing = new Bag<>(Direction.NORTH);

        hull.put(pos.get(), startOnWhite ? HullColor.WHITE : HullColor.BLACK);

        Bag<Boolean> state = new Bag<>(true);
        this.computer = new IntComputer(
                () -> hull.computeIfAbsent(pos.get(), k -> HullColor.BLACK).getIndex(),
                i -> {
                    if (state.get()) {
                        if (i == 0) {
                            hull.put(pos.get(), HullColor.BLACK);
                        } else if (i == 1) {
                            hull.put(pos.get(), HullColor.WHITE);
                        } else {
                            throw new RuntimeException();
                        }

                        painted.add(pos.get());
                    } else {
                        if (i == 0) {
                            facing.set(facing.get().rotateCCW());
                        } else if (i == 1) {
                            facing.set(facing.get().rotateCW());
                        } else {
                            throw new RuntimeException();
                        }

                        pos.set(pos.get().add(facing.get().getDirectionVector()));
                    }

                    state.set(!state.get());
                },
                memory
        );
    }

    public void run() {
        computer.run();
    }

    public Map<Vec2i, HullColor> getHull() {
        return hull;
    }

    public Set<Vec2i> getPainted() {
        return painted;
    }
}
