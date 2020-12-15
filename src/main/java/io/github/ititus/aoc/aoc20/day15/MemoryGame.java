package io.github.ititus.aoc.aoc20.day15;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntList;

public class MemoryGame {

    private final Int2IntMap numbers;
    private int turn;
    private int lastNumber;

    public MemoryGame(IntList numbers) {
        if (numbers.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.numbers = new Int2IntOpenHashMap();
        this.turn = 1;
        numbers.forEach((int n) -> {
            this.numbers.put(n, turn++);
            this.lastNumber = n;
        });
    }

    public int run(int turns) {
        if (turn - 1 > turns) {
            throw new RuntimeException();
        }

        while (turn <= turns) {
            nextTurn();
        }

        return lastNumber;
    }

    public void nextTurn() {
        int lastTurn = turn++ - 1;
        int lastSeen = numbers.put(lastNumber, lastTurn);
        if (lastSeen == 0) {
            lastNumber = 0;
        } else {
            lastNumber = lastTurn - lastSeen;
        }
    }
}
