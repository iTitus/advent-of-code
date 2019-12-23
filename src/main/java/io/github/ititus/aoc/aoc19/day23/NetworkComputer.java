package io.github.ititus.aoc.aoc19.day23;

import io.github.ititus.aoc.aoc19.IntComputer;
import io.github.ititus.math.number.BigIntegerMath;

import java.math.BigInteger;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class NetworkComputer {

    private final Network network;

    private final int id;
    private final Queue<Packet> input;
    private final AtomicInteger inputState;
    private final AtomicInteger outputState;
    private final AtomicReference<Packet.Builder> packetBuilder;
    private final IntComputer c;
    private final Thread t;

    public NetworkComputer(Network network, int id, BigInteger[] memory) {
        this.network = network;
        this.id = id;
        this.input = new ConcurrentLinkedQueue<>();
        this.inputState = new AtomicInteger(-1);
        this.outputState = new AtomicInteger(0);
        this.packetBuilder = new AtomicReference<>();
        this.c = new IntComputer(this::read, this::write, memory);
        this.t = new Thread(c::run, "IntComputer " + id);
    }

    public void start() {
        t.start();
    }

    public void receivePacket(Packet p) {
        input.offer(p);
    }

    private void write(BigInteger i) {
        if (outputState.compareAndSet(0, 1)) {
            packetBuilder.set(Packet.builder().from(id).to(i.intValueExact()));
        } else if (outputState.compareAndSet(1, 2)) {
            packetBuilder.getAndUpdate(p -> p.x(i));
        } else if (outputState.compareAndSet(2, 0)) {
            network.sendPacket(packetBuilder.updateAndGet(p -> p.y(i)).build());
        } else {
            throw new RuntimeException();
        }
    }

    private BigInteger read() {
        if (inputState.compareAndSet(-1, 0)) {
            return BigIntegerMath.of(id);
        }
        Packet p = input.peek();
        if (p != null) {
            if (inputState.compareAndSet(0, 1)) {
                return p.getX();
            } else if (inputState.compareAndSet(1, 0)) {
                input.poll();
                return p.getY();
            } else {
                throw new RuntimeException();
            }
        }

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return BigIntegerMath.of(-1);
    }

    public boolean isIdle() {
        return inputState.get() == 0;
    }
}
