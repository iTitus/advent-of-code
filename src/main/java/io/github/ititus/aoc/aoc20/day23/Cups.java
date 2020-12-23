package io.github.ititus.aoc.aoc20.day23;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

public class Cups {

    private static final int PACKET_SIZE = 3;

    private final boolean part2;
    private final int[] cups;
    private int current;

    public Cups(String input, boolean part2) {
        this.part2 = part2;
        int[] initial = new int[input.length()];
        int min = -1, max = -1;
        for (int i = 0; i < initial.length; i++) {
            int n = Character.digit(input.charAt(i), 10);
            initial[i] = n;
            if (min < 0 || n < min) {
                min = n;
            }
            if (max < 0 || n > max) {
                max = n;
            }
        }

        if (min != 1 || max != initial.length) {
            throw new RuntimeException();
        }

        cups = new int[part2 ? 1_000_001 : initial.length + 1];
        cups[0] = -1;
        for (int i = 0; i < initial.length - 1; i++) {
            cups[initial[i]] = initial[i + 1];
        }

        if (part2) {
            cups[initial[initial.length - 1]] = initial.length + 1;
            for (int i = initial.length + 1; i < cups.length - 1; i++) {
                cups[i] = i + 1;
            }
            cups[cups.length - 1] = initial[0];
        } else {
            cups[initial[initial.length - 1]] = initial[0];
        }

        current = initial[0];
    }

    public String play(int moves) {
        for (int i = 0; i < moves; i++) {
            doMove();
        }

        return getResult();
    }

    private void doMove() {
        IntSet packet = new IntOpenHashSet(PACKET_SIZE);
        int firstPacket = cups[current];
        int lastPacket = current;
        for (int i = 0; i < PACKET_SIZE; i++) {
            lastPacket = cups[lastPacket];
            packet.add(lastPacket);
        }

        int destination = current;
        do {
            destination = Math.floorMod(destination - 1, cups.length);
        } while (cups[destination] <= 0 || packet.contains(destination));
        int afterDestination = cups[destination];
        cups[destination] = firstPacket;

        cups[current] = cups[lastPacket];
        cups[lastPacket] = afterDestination;

        current = cups[current];
    }

    private String getResult() {
        int next = cups[1];
        if (part2) {
            return Long.toString((long) next * cups[next]);
        } else {
            StringBuilder b = new StringBuilder();
            for (; next != 1; next = cups[next]) {
                b.append(next);
            }

            return b.toString();
        }
    }
}
