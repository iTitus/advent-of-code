package io.github.ititus.aoc.aoc18.day03;

import io.github.ititus.aoc.InputProvider;
import io.github.ititus.math.vector.Vec2i;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.ititus.aoc.FastUtilStreams.toMap;

public class Day03 {

    public static void main(String[] args) {
        Int2ObjectMap<Claim> claims = InputProvider.lines(2018, 3)
                .map(Claim::of)
                .collect(toMap(Claim::getId, Function.identity()));

        Fabric fabric = new Fabric(new Vec2i(1000, 1000));
        claims.values().forEach(fabric::addClaim);

        // 1
        System.out.println(fabric.getNumberOfDoubleClaims());

        // 2
        System.out.println(fabric.getUnobstructedClaim());
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
            for (int y = 0; y < size.getY(); y++) {
                for (int x = 0; x < size.getX(); x++) {
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
            if (pos.getX() < 0 || pos.getY() < 0 || size.getX() <= 0 || size.getY() <= 0) {
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
            return this.pos.getX() <= pos.getX() && pos.getX() < this.pos.getX() + this.size.getX()
                    && this.pos.getY() <= pos.getY() && pos.getY() < this.pos.getY() + this.size.getY();
        }

        public boolean intersects(Claim c) {
            int tx = this.pos.getX();
            int ty = this.pos.getY();
            int ox = c.pos.getX();
            int oy = c.pos.getY();
            int tw = tx + this.size.getX();
            int th = ty + this.size.getY();
            int ow = ox + c.size.getX();
            int oh = oy + c.size.getY();

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
