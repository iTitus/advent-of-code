package io.github.ititus.aoc.aoc21.day22;

import io.github.ititus.math.vector.Vec3i;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CuboidCollectionTest {

    @Test
    void volume0() {
        CuboidCollection cc = new CuboidCollection();
        assertThat(cc.getVolume()).isEqualTo(0);
    }

    @Test
    void volume1() {
        CuboidCollection cc = new CuboidCollection();
        cc.add(new Cuboid(new Vec3i(10, 10, 10), new Vec3i(12, 12, 12)));
        assertThat(cc.getVolume()).isEqualTo(27);
    }

    @Test
    void volume2() {
        CuboidCollection cc = new CuboidCollection();
        cc.add(new Cuboid(new Vec3i(10, 10, 10), new Vec3i(12, 12, 12)));
        cc.add(new Cuboid(new Vec3i(11, 11, 11), new Vec3i(13, 13, 13)));
        assertThat(cc.getVolume()).isEqualTo(27 + 19);
    }

    @Test
    void volume3() {
        CuboidCollection cc = new CuboidCollection();
        cc.add(new Cuboid(new Vec3i(10, 10, 10), new Vec3i(12, 12, 12)));
        cc.add(new Cuboid(new Vec3i(11, 11, 11), new Vec3i(13, 13, 13)));
        cc.remove(new Cuboid(new Vec3i(9, 9, 9), new Vec3i(11, 11, 11)));
        assertThat(cc.getVolume()).isEqualTo(27 + 19 - 8);
    }

    @Test
    void volume4() {
        CuboidCollection cc = new CuboidCollection();
        cc.add(new Cuboid(new Vec3i(10, 10, 10), new Vec3i(12, 12, 12)));
        cc.add(new Cuboid(new Vec3i(11, 11, 11), new Vec3i(13, 13, 13)));
        cc.remove(new Cuboid(new Vec3i(9, 9, 9), new Vec3i(11, 11, 11)));
        cc.add(new Cuboid(new Vec3i(10, 10, 10), new Vec3i(10, 10, 10)));
        assertThat(cc.getVolume()).isEqualTo(27 + 19 - 8 + 1);
    }
}
