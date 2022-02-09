package io.github.ititus.aoc.aoc19.day22;

import io.github.ititus.commons.math.modulus.LinearModFunction;
import io.github.ititus.commons.math.number.BigIntegerMath;

import java.util.Arrays;
import java.util.List;

public class CardDeck {

    public static final String DEAL_INTO_NEW_STACK = "deal into new stack";
    public static final String CUT_N = "cut ";
    public static final String DEAL_WITH_INCREMENT_N = "deal with increment ";

    private final long cardCount;
    private LinearModFunction f;

    public CardDeck(long cardCount) {
        this.cardCount = cardCount;
        this.f = LinearModFunction.identity(cardCount);
    }

    public void executeShuffles(List<String> commands) {
        executeShuffles(1, commands);
    }

    public void executeShuffles(long n, List<String> commands) {
        for (String command : commands) {
            executeShuffle(command);
        }
        f = f.selfCompose(n);
    }

    public void executeShuffle(String command) {
        if (command.equals(DEAL_INTO_NEW_STACK)) {
            dealIntoNewStack();
        } else if (command.startsWith(CUT_N)) {
            cut(Long.parseLong(command.substring(CUT_N.length())));
        } else if (command.startsWith(DEAL_WITH_INCREMENT_N)) {
            dealWithIncrement(Long.parseLong(command.substring(DEAL_WITH_INCREMENT_N.length())));
        } else {
            throw new IllegalArgumentException();
        }
    }

    public long getCardCount() {
        return cardCount;
    }

    public long[] getCards() {
        long[] cards = new long[Math.toIntExact(cardCount)];
        Arrays.setAll(cards, this::getCardAt);
        return cards;
    }

    public long getCardAt(long index) {
        return f.apply(BigIntegerMath.of(index)).longValueExact();
    }

    public long getPositionOf(long card) {
        return f.inverse().apply(BigIntegerMath.of(card)).longValueExact();
    }

    public void dealIntoNewStack() {
        f = f.compose(new LinearModFunction(cardCount, -1, -1));
    }

    public void cut(long n) {
        f = f.compose(new LinearModFunction(cardCount, 1, n));
    }

    public void dealWithIncrement(long n) {
        f = f.compose(new LinearModFunction(cardCount, n, 0).inverse());
    }
}
