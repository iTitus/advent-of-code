package io.github.ititus.aoc.aoc19.day23;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Network {

    private final NetworkComputer[] computers;
    private final boolean enableNat;
    private final Set<BigInteger> natYValues;
    private final Nat nat;

    public Network(int size, boolean enableNat, BigInteger[] memory) {
        this.computers = new NetworkComputer[size];
        this.enableNat = enableNat;
        this.natYValues = Collections.newSetFromMap(new ConcurrentHashMap<>());
        Arrays.setAll(this.computers, i -> new NetworkComputer(this, i, memory));
        this.nat = new Nat(this, 255);
    }

    public void start() {
        for (NetworkComputer c : computers) {
            c.start();
        }
        if (enableNat) {
            nat.start();
        }
    }

    public boolean isIdle() {
        for (NetworkComputer c : computers) {
            if (!c.isIdle()) {
                return false;
            }
        }
        return true;
    }

    public void sendPacket(Packet p) {
        int to = p.getTo();

        if (p.getFrom() == 255) {
            BigInteger y = p.y();
            if (!natYValues.add(p.y())) {
                System.out.println(y);
            }
        }

        if (to == 255) {
            // System.out.println(p);
            nat.receivePacket(p);
        } else if (to < 0 || to >= computers.length) {
            throw new RuntimeException();
        } else {
            computers[p.getTo()].receivePacket(p);
        }
    }
}
