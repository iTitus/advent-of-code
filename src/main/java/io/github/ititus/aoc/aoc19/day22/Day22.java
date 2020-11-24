package io.github.ititus.aoc.aoc19.day22;

import io.github.ititus.aoc.common.InputProvider;
import io.github.ititus.math.time.DurationFormatter;
import io.github.ititus.math.time.StopWatch;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class Day22 {

    public static void main(String[] args) {
        List<String> lines = InputProvider.readAllLines(2019, 22);

        test();

        // 1
        System.out.println("### 1 ###");
        CardDeck cd1 = new CardDeck(10_007);
        cd1.executeShuffles(lines);
        testGetCardAtAndPositionOf(cd1);
        System.out.println(cd1.getPositionOf(2019));

        // 2
        System.out.println("### 2 ###");
        CardDeck cd2 = new CardDeck(119315717514047L);
        cd2.executeShuffles(101741582076661L, lines);
        System.out.println(cd2.getCardAt(2020));
    }

    private static void test() {
        test1("9 8 7 6 5 4 3 2 1 0",
                "deal into new stack"
        );
        test1("3 4 5 6 7 8 9 0 1 2",
                "cut 3"
        );
        test1("6 7 8 9 0 1 2 3 4 5",
                "cut -4"
        );
        test1("0 7 4 1 8 5 2 9 6 3",
                "deal with increment 3"
        );
        test1("0 1 2 3 4 5 6 7 8 9",
                "deal into new stack",
                "deal into new stack"
        );
        test1("0 1 2 3 4 5 6 7 8 9",
                "cut 3",
                "cut 7"
        );
        test1("0 1 2 3 4 5 6 7 8 9",
                "cut 3",
                "cut -3"
        );
        test1("6 7 8 9 0 1 2 3 4 5",
                "cut 1",
                "cut 2",
                "cut 3"
        );
        test1("0 1 2 3 4 5 6 7 8 9",
                "deal with increment 3",
                "deal with increment 7"
        );
        test1("0 3 6 9 2 5 8 1 4 7",
                "deal with increment 7",
                "deal into new stack",
                "deal into new stack"
        );
        test1("3 0 7 4 1 8 5 2 9 6",
                "cut 6",
                "deal with increment 7",
                "deal into new stack"
        );
        test1("6 3 0 7 4 1 8 5 2 9",
                "deal with increment 7",
                "deal with increment 9",
                "cut -2"
        );
        test1("9 2 5 8 1 4 7 0 3 6",
                "deal into new stack",
                "cut -2",
                "deal with increment 7",
                "cut 8",
                "cut -4",
                "deal with increment 7",
                "cut 3",
                "deal with increment 9",
                "deal with increment 3",
                "cut -1"
        );
    }

    private static void test1(String expectedString, String... commandStrings) {
        long[] expected = Arrays.stream(expectedString.split(" ")).mapToLong(Long::parseLong).toArray();
        List<String> commands = Arrays.asList(commandStrings);

        StopWatch s = StopWatch.createRunning();
        CardDeck cd = new CardDeck(expected.length);
        cd.executeShuffles(commands);
        long[] actual = cd.getCards();
        Duration d = s.stop();

        testGetCardAtAndPositionOf(cd);

        if (!Arrays.equals(expected, actual)) {
            throw new RuntimeException("Part 1: expected=" + Arrays.toString(expected) + " actual=" + Arrays.toString(actual));
        } else {
            System.out.println("Successfully passed test in " + DurationFormatter.format(d) + " for Part 1: output=" + Arrays.toString(actual));
        }
    }

    private static void testGetCardAtAndPositionOf(CardDeck cd) {
        for (int i = 0; i < cd.getCardCount(); i++) {
            if (cd.getPositionOf(cd.getCardAt(i)) != i) {
                throw new RuntimeException();
            }
            if (cd.getCardAt(cd.getPositionOf(i)) != i) {
                throw new RuntimeException();
            }
        }
    }
}
