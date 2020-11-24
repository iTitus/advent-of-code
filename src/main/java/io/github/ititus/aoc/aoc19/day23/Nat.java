package io.github.ititus.aoc.aoc19.day23;

import java.util.concurrent.atomic.AtomicReference;

public class Nat implements Runnable {

    private final Network network;
    private final int id;
    private final AtomicReference<Packet> lastPacket;
    private final Thread t;

    public Nat(Network network, int id) {
        this.network = network;
        this.id = id;
        this.lastPacket = new AtomicReference<>();
        this.t = new Thread(this, "NAT");
    }

    public void start() {
        t.start();
    }

    public void receivePacket(Packet p) {
        lastPacket.set(p);
    }

    @Override
    public void run() {
        while (true) {
            if (network.isIdle()) {
                Packet p = lastPacket.getAndSet(null);
                if (p != null) {
                    network.sendPacket(p.withHeader(id, 0));
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
