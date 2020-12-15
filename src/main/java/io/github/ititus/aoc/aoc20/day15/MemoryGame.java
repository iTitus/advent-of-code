package io.github.ititus.aoc.aoc20.day15;

import it.unimi.dsi.fastutil.ints.*;

import java.util.function.IntConsumer;

public class MemoryGame {

    private final Int2ObjectMap<IntIntPair> numbers;
    private int turn;
    private int lastNumber;

    public MemoryGame(IntList numbers) {
        if (numbers.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.numbers = new Int2ObjectOpenHashMap<>();
        this.turn = 1;
        numbers.forEach((IntConsumer) this::say);
    }

    public int run(int turns) {
        while (turn <= turns) {
            nextTurn();
        }
        return lastNumber;
    }

    public void nextTurn() {
        IntIntPair lastTurns = numbers.get(lastNumber);
        if (lastTurns.leftInt() == 0) {
            say(0);
        } else {
            say(lastTurns.rightInt() - lastTurns.leftInt());
        }
    }

    private void say(int n) {
        IntIntPair lastTurns = numbers.computeIfAbsent(n, k -> IntIntMutablePair.of(0, 0));
        lastTurns.left(lastTurns.rightInt());
        lastTurns.right(turn);

        lastNumber = n;
        turn++;
    }
}
