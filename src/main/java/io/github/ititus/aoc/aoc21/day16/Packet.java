package io.github.ititus.aoc.aoc21.day16;

import io.github.ititus.math.number.BigIntegerConstants;
import io.github.ititus.math.number.BigIntegerMath;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class Packet {

    private final int bits;
    private final int version;
    private final int typeId;

    private Packet(int bits, int version, int typeId) {
        this.bits = bits;
        this.version = version;
        this.typeId = typeId;
    }

    public static Packet read(BitReader r) {
        int version = r.readBits(3);
        int typeId = r.readBits(3);
        return typeId == 4 ?
                Literal.read(r, 6, version, typeId) :
                Operator.read(r, 6, version, typeId);
    }

    public int bits() {
        return bits;
    }

    public int version() {
        return version;
    }

    public int typeId() {
        return typeId;
    }

    public abstract int sumOfAllVersions();

    public abstract BigInteger value();

    public static class Literal extends Packet {

        private final BigInteger value;

        private Literal(int bits, int version, int typeId, BigInteger value) {
            super(bits, version, typeId);
            this.value = value;
        }

        public static Literal read(BitReader r, int bits, int version, int typeId) {
            BigInteger value = BigIntegerConstants.ZERO;
            int cur;
            do
            {
                cur = r.readBits(5);
                bits += 5;
                value = value.shiftLeft(4).or(BigIntegerMath.of(cur & 0xf));
            } while ((cur & 0x10) != 0);

            return new Literal(bits, version, typeId, value);
        }

        @Override
        public int sumOfAllVersions() {
            return version();
        }

        @Override
        public BigInteger value() {
            return value;
        }
    }

    public static class Operator extends Packet {

        private final boolean lengthTypeId;
        private final int subPacketCountOrBits;
        private final List<Packet> subPackets;

        private Operator(int bits, int version, int typeId, boolean lengthTypeId, int subPacketCountOrBits, List<Packet> subPackets) {
            super(bits, version, typeId);
            this.lengthTypeId = lengthTypeId;
            this.subPacketCountOrBits = subPacketCountOrBits;
            this.subPackets = subPackets;
        }

        public static Operator read(BitReader r, int bits, int version, int typeId) {
            boolean lengthTypeId = r.readBits(1) != 0;
            bits++;
            int subPacketCountOrBits;
            List<Packet> subPackets = new ArrayList<>();
            if (lengthTypeId) {
                subPacketCountOrBits = r.readBits(11);
                bits += 11;
                for (int i = 0; i < subPacketCountOrBits; i++) {
                    Packet p = Packet.read(r);
                    subPackets.add(p);
                    bits += p.bits();
                }
            } else {
                subPacketCountOrBits = r.readBits(15);
                bits += 15;
                int subBits = 0;
                while (subBits < subPacketCountOrBits) {
                    Packet p = Packet.read(r);
                    subPackets.add(p);
                    subBits += p.bits();
                }

                if (subBits > subPacketCountOrBits) {
                    throw new RuntimeException();
                }

                bits += subPacketCountOrBits;
            }

            return new Operator(bits, version, typeId, lengthTypeId, subPacketCountOrBits, List.copyOf(subPackets));
        }

        @Override
        public int sumOfAllVersions() {
            return version() + subPackets.stream().mapToInt(Packet::sumOfAllVersions).sum();
        }

        @Override
        public BigInteger value() {
            return switch (typeId()) {
                case 0 -> subPackets.stream().map(Packet::value).reduce(BigInteger::add).orElseThrow();
                case 1 -> subPackets.stream().map(Packet::value).reduce(BigInteger::multiply).orElseThrow();
                case 2 -> subPackets.stream().map(Packet::value).min(Comparator.naturalOrder()).orElseThrow();
                case 3 -> subPackets.stream().map(Packet::value).max(Comparator.naturalOrder()).orElseThrow();
                case 5 -> {
                    if (subPackets.size() != 2) {
                        throw new RuntimeException();
                    }

                    yield subPackets.get(0).value().compareTo(subPackets.get(1).value()) > 0 ? BigIntegerConstants.ONE : BigIntegerConstants.ZERO;
                }
                case 6 -> {
                    if (subPackets.size() != 2) {
                        throw new RuntimeException();
                    }

                    yield subPackets.get(0).value().compareTo(subPackets.get(1).value()) < 0 ? BigIntegerConstants.ONE : BigIntegerConstants.ZERO;
                }
                case 7 -> {
                    if (subPackets.size() != 2) {
                        throw new RuntimeException();
                    }

                    yield subPackets.get(0).value().equals(subPackets.get(1).value()) ? BigIntegerConstants.ONE : BigIntegerConstants.ZERO;
                }
                default -> throw new RuntimeException();
            };
        }
    }
}
