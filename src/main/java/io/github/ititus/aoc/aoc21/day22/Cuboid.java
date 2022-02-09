package io.github.ititus.aoc.aoc21.day22;

import io.github.ititus.commons.math.vector.Vec3i;

import java.util.Optional;
import java.util.stream.Stream;

public record Cuboid(
        Vec3i min,
        Vec3i max
) {

    public Cuboid {
        if (min.x() > max.x() || min.y() > max.y() || min.z() > max.z()) {
            throw new IllegalArgumentException();
        }
    }

    public boolean contains(Vec3i v) {
        return min.x() <= v.x() && v.x() <= max.x()
                && min.y() <= v.y() && v.y() <= max.y()
                && min.z() <= v.z() && v.z() <= max.z();
    }

    public boolean contains(Cuboid c) {
        return min.x() <= c.min.x() && c.max.x() <= max.x()
                && min.y() <= c.min.y() && c.max.y() <= max.y()
                && min.z() <= c.min.z() && c.max.z() <= max.z();
    }

    public long volume() {
        return (long) (max.x() - min.x() + 1) * (long) (max.y() - min.y() + 1) * (long) (max.z() - min.z() + 1);
    }

    public boolean intersects(Cuboid that) {
        return intersection(that).isPresent();
    }

    public Optional<Cuboid> intersection(Cuboid that) {
        if (this.contains(that)) {
            return Optional.of(that);
        } else if (that.contains(this)) {
            return Optional.of(this);
        }

        int minX = Math.max(this.min.x(), that.min.x());
        int maxX = Math.min(this.max.x(), that.max.x());
        if (minX > maxX) {
            return Optional.empty();
        }

        int minY = Math.max(this.min.y(), that.min.y());
        int maxY = Math.min(this.max.y(), that.max.y());
        if (minY > maxY) {
            return Optional.empty();
        }

        int minZ = Math.max(this.min.z(), that.min.z());
        int maxZ = Math.min(this.max.z(), that.max.z());
        if (minZ > maxZ) {
            return Optional.empty();
        }

        return Optional.of(new Cuboid(new Vec3i(minX, minY, minZ), new Vec3i(maxX, maxY, maxZ)));
    }

    public Stream<Cuboid> difference(Cuboid that) {
        if (that.contains(this)) {
            return Stream.of();
        } else if (!intersects(that)) {
            return Stream.of(this);
        }

        Optional<Cuboid> inv1;
        if (this.min.z() < that.min.z()) {
            inv1 = Optional.of(new Cuboid(
                    this.min,
                    new Vec3i(this.max.x(), this.max.y(), that.min.z() - 1)
            ));
        } else {
            inv1 = Optional.empty();
        }
        Optional<Cuboid> inv2;
        if (this.max.z() > that.max.z()) {
            inv2 = Optional.of(new Cuboid(
                    new Vec3i(this.min.x(), this.min.y(), that.max.z() + 1),
                    this.max
            ));
        } else {
            inv2 = Optional.empty();
        }

        Optional<Cuboid> inv3;
        if (this.min.y() < that.min.y()) {
            inv3 = Optional.of(new Cuboid(
                    inv1.map(c -> new Vec3i(this.min.x(), this.min.y(), c.max.z() + 1)).orElseGet(this::min),
                    inv2.map(c -> new Vec3i(this.max.x(), that.min.y() - 1, c.min.z() - 1)).orElseGet(() -> new Vec3i(this.max.x(), that.min.y() - 1, this.max.z()))
            ));
        } else {
            inv3 = Optional.empty();
        }
        Optional<Cuboid> inv4;
        if (this.max.y() > that.max.y()) {
            inv4 = Optional.of(new Cuboid(
                    inv1.map(c -> new Vec3i(this.min.x(), that.max.y() + 1, c.max.z() + 1)).orElseGet(() -> new Vec3i(this.min.x(), that.max.y() + 1, this.min.z())),
                    inv2.map(c -> new Vec3i(this.max.x(), this.max.y(), c.min.z() - 1)).orElseGet(this::max)
            ));
        } else {
            inv4 = Optional.empty();
        }

        Optional<Cuboid> inv5;
        if (this.min.x() < that.min.x()) {
            int minZ = inv1.isPresent() ? inv1.get().max.z() + 1 : this.min.z();
            int minY = inv3.isPresent() ? inv3.get().max.y() + 1 : this.min.y();
            int minX = this.min.x();

            int maxZ = inv2.isPresent() ? inv2.get().min.z() - 1 : this.max.z();
            int maxY = inv4.isPresent() ? inv4.get().min.y() - 1 : this.max.y();
            int maxX = that.min.x() - 1;

            inv5 = Optional.of(new Cuboid(new Vec3i(minX, minY, minZ), new Vec3i(maxX, maxY, maxZ)));
        } else {
            inv5 = Optional.empty();
        }
        Optional<Cuboid> inv6;
        if (this.max.x() > that.max.x()) {
            int minZ = inv1.isPresent() ? inv1.get().max.z() + 1 : this.min.z();
            int minY = inv3.isPresent() ? inv3.get().max.y() + 1 : this.min.y();
            int minX = that.max.x() + 1;

            int maxZ = inv2.isPresent() ? inv2.get().min.z() - 1 : this.max.z();
            int maxY = inv4.isPresent() ? inv4.get().min.y() - 1 : this.max.y();
            int maxX = this.max.x();

            inv6 = Optional.of(new Cuboid(new Vec3i(minX, minY, minZ), new Vec3i(maxX, maxY, maxZ)));
        } else {
            inv6 = Optional.empty();
        }

        return Stream.of(inv1, inv2, inv3, inv4, inv5, inv6)
                .flatMap(Optional::stream);
    }
}
