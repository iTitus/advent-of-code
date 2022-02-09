package io.github.ititus.aoc.aoc19.day12;

import io.github.ititus.commons.math.vector.Vec3i;

public class Moon {

    private final Vec3i initialPos;
    private final Vec3i initialVel;

    private Vec3i pos;
    private Vec3i vel;

    public Moon(Moon m) {
        this.initialPos = m.initialPos;
        this.initialVel = m.initialVel;
        this.pos = m.pos;
        this.vel = m.vel;
    }

    public Moon(Vec3i initialPos) {
        this.initialPos = this.pos = initialPos;
        this.initialVel = this.vel = new Vec3i();
    }

    public Vec3i getPos() {
        return pos;
    }

    public Vec3i getVel() {
        return vel;
    }

    public void addGravity(Moon o) {
        vel = vel.add(o.pos.subtract(pos).sgn());
    }

    public void move() {
        pos = pos.add(vel);
    }

    public int getPotentialEnergy() {
        return pos.manhattanDistance();
    }

    public int getKineticEnergy() {
        return vel.manhattanDistance();
    }

    public int getTotalEnergy() {
        return getPotentialEnergy() * getKineticEnergy();
    }

    public boolean isInitialX() {
        return pos.x() == initialPos.x() && vel.x() == initialVel.x();
    }

    public boolean isInitialY() {
        return pos.y() == initialPos.y() && vel.y() == initialVel.y();
    }

    public boolean isInitialZ() {
        return pos.z() == initialPos.z() && vel.z() == initialVel.z();
    }
}
