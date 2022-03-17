package io.github.ititus.aoc.aoc21.day22;

import io.github.ititus.commons.math.vector.Vec3i;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CuboidTest {

    @Test
    void volume1() {
        Cuboid c = new Cuboid(new Vec3i(0, 0, 0), new Vec3i(0, 0, 0));
        assertThat(c.volume()).isEqualTo(1);
    }

    @Test
    void volume2() {
        Cuboid c = new Cuboid(new Vec3i(10, 10, 10), new Vec3i(12, 12, 12));
        assertThat(c.volume()).isEqualTo(27);
    }

    @Test
    void intersect() {
        Cuboid c1 = new Cuboid(new Vec3i(10, 10, 10), new Vec3i(12, 12, 12));
        Cuboid c2 = new Cuboid(new Vec3i(11, 11, 11), new Vec3i(13, 13, 13));
        Cuboid expected = new Cuboid(new Vec3i(11, 11, 11), new Vec3i(12, 12, 12));
        assertThat(c1.intersection(c2)).contains(expected);
    }

    @Test
    void difference() {
        Cuboid c1 = new Cuboid(new Vec3i(10, 10, 10), new Vec3i(12, 12, 12));
        Cuboid c2 = new Cuboid(new Vec3i(11, 11, 11), new Vec3i(13, 13, 13));
        List<Cuboid> expected = List.of(
                new Cuboid(new Vec3i(11, 11, 13), new Vec3i(13, 13, 13)),
                new Cuboid(new Vec3i(11, 13, 11), new Vec3i(13, 13, 12)),
                new Cuboid(new Vec3i(13, 11, 11), new Vec3i(13, 12, 12))
        );
        assertThat(c2.difference(c1)).containsExactlyInAnyOrderElementsOf(expected);
    }
}
