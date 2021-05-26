package io.github.ititus.aoc.aoc20.day22;

import io.github.ititus.data.pair.Pair;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.HashSet;
import java.util.Set;

public class Combat {

    private final IntList startingHand1, startingHand2;
    private final Set<Pair<IntList, IntList>> history;
    private final IntList hand1, hand2;
    private int winner;

    public Combat(IntList startingHand1, IntList startingHand2) {
        this.startingHand1 = startingHand1;
        this.startingHand2 = startingHand2;
        this.history = new HashSet<>();
        this.hand1 = new IntArrayList();
        this.hand2 = new IntArrayList();
    }

    public void reset() {
        winner = 0;
        history.clear();
        hand1.clear();
        hand2.clear();
        hand1.addAll(startingHand1);
        hand2.addAll(startingHand2);
    }

    public boolean round(boolean recursive) {
        if (hand1.isEmpty()) {
            winner = 2;
            return false;
        } else if (hand2.isEmpty()) {
            winner = 1;
            return false;
        }

        if (recursive && history.contains(Pair.of(hand1, hand2))) {
            winner = 1;
            return false;
        }

        if (recursive) {
            history.add(Pair.of(new IntArrayList(hand1), new IntArrayList(hand2)));
        }

        int c1 = hand1.removeInt(0);
        int c2 = hand2.removeInt(0);
        if (c1 == c2) {
            throw new RuntimeException();
        }

        boolean win1;
        if (recursive && hand1.size() >= c1 && hand2.size() >= c2) {
            IntList copy1 = new IntArrayList(hand1.subList(0, c1));
            IntList copy2 = new IntArrayList(hand2.subList(0, c2));

            Combat subgame = new Combat(copy1, copy2);
            subgame.reset();
            subgame.simulate(true);

            win1 = subgame.winner == 1;
        } else {
            win1 = c1 > c2;
        }

        if (win1) {
            hand1.add(c1);
            hand1.add(c2);
        } else {
            hand2.add(c2);
            hand2.add(c1);
        }

        return true;
    }

    public int simulate(boolean recursive) {
        while (round(recursive))
            ;
        if (winner == 0) {
            throw new RuntimeException();
        }

        return getPoints();
    }

    private int getPoints() {
        IntList winner;
        if (this.winner == 1) {
            winner = hand1;
        } else if (this.winner == 2) {
            winner = hand2;
        } else {
            throw new RuntimeException();
        }

        int points = 0;
        int n = winner.size();
        for (var it = winner.iterator(); it.hasNext(); ) {
            points += n-- * it.nextInt();
        }

        return points;
    }
}
