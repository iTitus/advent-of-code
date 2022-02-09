package io.github.ititus.aoc.aoc19.day20;

import io.github.ititus.commons.math.vector.Vec2i;
import io.github.ititus.commons.math.vector.Vec3i;

public class Portal {

    private final Vec2i in;
    private final Vec2i out;

    public Portal(Vec2i in, Vec2i out) {
        this.in = in;
        this.out = out;
    }

    public Vec3i getOtherEnd(boolean recursive, Vec3i p) {
        int currentLevel = p.z();
        Vec2i p2 = new Vec2i(p.x(), p.y());
        return in.equals(p2) ?
                new Vec3i(out.x(), out.y(), recursive ? currentLevel - 1 : 0) :
                recursive && currentLevel == 0 ? null : new Vec3i(in.x(), in.y(), recursive ? currentLevel + 1
                        : 0);
    }
}
