package io.github.ititus.aoc.aoc19.day20;

import io.github.ititus.math.vector.Vec2i;
import io.github.ititus.math.vector.Vec3i;

public class Portal {

    private final Vec2i in;
    private final Vec2i out;

    public Portal(Vec2i in, Vec2i out) {
        this.in = in;
        this.out = out;
    }

    public Vec3i getOtherEnd(boolean recursive, Vec3i p) {
        int currentLevel = p.getZ();
        Vec2i p2 = new Vec2i(p.getX(), p.getY());
        return in.equals(p2) ?
                new Vec3i(out.getX(), out.getY(), recursive ? currentLevel - 1 : 0) :
                recursive && currentLevel == 0 ? null : new Vec3i(in.getX(), in.getY(), recursive ? currentLevel + 1
                        : 0);
    }
}
