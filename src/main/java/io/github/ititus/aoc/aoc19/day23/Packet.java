package io.github.ititus.aoc.aoc19.day23;

import java.math.BigInteger;
import java.util.Objects;

public class Packet {

    private final int from, to;
    private final BigInteger x, y;

    public Packet(int from, int to, BigInteger x, BigInteger y) {
        this.from = from;
        this.to = to;
        this.x = x;
        this.y = y;
    }

    public static Builder builder() {
        return new Builder(null, null, null, null);
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public BigInteger x() {
        return x;
    }

    public BigInteger y() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Packet)) {
            return false;
        }
        Packet packet = (Packet) o;
        // noinspection SuspiciousNameCombination
        return from == packet.from && to == packet.to && x.equals(packet.x) && y.equals(packet.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, x, y);
    }

    @Override
    public String toString() {
        return "Packet{from=" + from + ", to=" + to + ", x=" + x + ", y=" + y + '}';
    }

    public Packet withHeader(int from, int to) {
        return new Packet(from, to, x, y);
    }

    public static class Builder {

        private final Integer from, to;
        private final BigInteger x, y;

        public Builder(Integer from, Integer to, BigInteger x, BigInteger y) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.y = y;
        }

        public Builder from(int from) {
            if (this.from != null) {
                throw new RuntimeException();
            }
            return new Builder(from, to, x, y);
        }

        public Builder to(int to) {
            if (this.to != null) {
                throw new RuntimeException();
            }
            return new Builder(from, to, x, y);
        }

        public Builder x(BigInteger x) {
            if (this.x != null) {
                throw new RuntimeException();
            }
            return new Builder(from, to, x, y);
        }

        public Builder y(BigInteger y) {
            if (this.y != null) {
                throw new RuntimeException();
            }
            return new Builder(from, to, x, y);
        }

        public Packet build() {
            return new Packet(from, to, x, y);
        }
    }
}
