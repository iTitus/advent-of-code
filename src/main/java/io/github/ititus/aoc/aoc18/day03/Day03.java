package io.github.ititus.aoc.aoc18.day03;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.commons.math.vector.Vec2i;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static io.github.ititus.aoc.common.FastUtilStreams.toMap;

@Aoc(year = 2018, day = 3)
public final class Day03 implements AocSolution {

    private Fabric fabric;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        Int2ObjectMap<Claim> claims;
        try (Stream<String> stream = input.lines()) {
            claims = stream
                    .map(Claim::of)
                    .collect(toMap(Claim::getId, Function.identity()));
        }

        fabric = new Fabric(new Vec2i(1000, 1000));
        claims.values().forEach(fabric::addClaim);
    }

    @Override
    public Object part1() {
        return fabric.getNumberOfDoubleClaims();
    }

    @Override
    public Object part2() {
        return fabric.getUnobstructedClaim();
    }

    private static final class Fabric {

        private final Vec2i size;
        private final Set<Claim> claims;

        private Fabric(Vec2i size) {
            this.size = size;
            this.claims = new HashSet<>();
        }

        private void addClaim(Claim c) {
            if (!claims.add(c)) {
                throw new IllegalArgumentException();
            }
        }

        private int getNumberOfDoubleClaims() {
            int n = 0;
            for (int y = 0; y < size.y(); y++) {
                for (int x = 0; x < size.x(); x++) {
                    boolean claimedOnce = false;
                    for (Claim c : claims) {
                        if (c.isClaimed(new Vec2i(x, y))) {
                            if (claimedOnce) {
                                n++;
                                break;
                            }
                            claimedOnce = true;
                        }
                    }
                }
            }
            return n;
        }

        public int getUnobstructedClaim() {
            Set<Claim> unobstructed = new HashSet<>(claims);

            for (Claim c1 : claims) {
                if (!unobstructed.contains(c1)) {
                    continue;
                }

                for (Claim c2 : claims) {
                    if (c1 == c2) {
                        continue;
                    }

                    if (c1.intersects(c2)) {
                        unobstructed.remove(c1);
                        unobstructed.remove(c2);
                        break;
                    }
                }
            }

            if (unobstructed.size() != 1) {
                throw new RuntimeException();
            }

            return unobstructed.iterator().next().getId();
        }
    }

    private static final class Claim {

        private static final Pattern P =
                Pattern.compile("#(?<id>\\d+) @ (?<posX>\\d+),(?<posY>\\d+): (?<sizeX>\\d+)x(?<sizeY>\\d+)");

        private final int id;
        private final Vec2i pos;
        private final Vec2i size;

        private Claim(int id, Vec2i pos, Vec2i size) {
            if (pos.x() < 0 || pos.y() < 0 || size.x() <= 0 || size.y() <= 0) {
                throw new IllegalArgumentException();
            }

            this.id = id;
            this.pos = pos;
            this.size = size;
        }

        private static Claim of(String s) {
            Matcher m = P.matcher(s);
            if (!m.matches()) {
                throw new IllegalArgumentException();
            }

            return new Claim(
                    Integer.parseInt(m.group("id")),
                    new Vec2i(Integer.parseInt(m.group("posX")), Integer.parseInt(m.group("posY"))),
                    new Vec2i(Integer.parseInt(m.group("sizeX")), Integer.parseInt(m.group("sizeY")))
            );
        }

        private int getId() {
            return id;
        }

        private Vec2i getPos() {
            return pos;
        }

        private Vec2i getSize() {
            return size;
        }

        public boolean isClaimed(Vec2i pos) {
            return this.pos.x() <= pos.x() && pos.x() < this.pos.x() + this.size.x()
                    && this.pos.y() <= pos.y() && pos.y() < this.pos.y() + this.size.y();
        }

        public boolean intersects(Claim c) {
            int tx = this.pos.x();
            int ty = this.pos.y();
            int ox = c.pos.x();
            int oy = c.pos.y();
            int tw = tx + this.size.x();
            int th = ty + this.size.y();
            int ow = ox + c.size.x();
            int oh = oy + c.size.y();

            return !(ox >= tw || tx >= ow || oy >= th || ty >= oh);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Claim)) {
                return false;
            }
            Claim claim = (Claim) o;
            return id == claim.id;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(id);
        }
    }
}
