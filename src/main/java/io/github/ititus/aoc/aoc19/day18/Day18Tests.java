package io.github.ititus.aoc.aoc19.day18;

import io.github.ititus.commons.math.time.DurationFormatter;
import io.github.ititus.commons.math.time.StopWatch;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class Day18Tests {

    public static void main(String[] args) {
        new Day18Tests().tests();
    }

    private static int execute(boolean changeMap, List<String> input) {
        TritonVault tv = new TritonVault(changeMap, input);
        //tv.printState();
        int bestPathLength = tv.findBestPathLength();
        //tv.printState();
        return bestPathLength;
    }

    private static void test1(int expected, String... input) {
        StopWatch s = StopWatch.createRunning();
        int actual = execute(false, Arrays.asList(input));
        Duration d = s.stop();
        if (actual != expected) {
            throw new RuntimeException("Part 1: expected=" + expected + " actual=" + actual);
        } else {
            System.out.println("Successfully passed test in " + DurationFormatter.format(d) + " for Part 1: output=" + actual);
        }
    }

    private void tests() {
        test1(132,
                "########################",
                "#...............b.C.D.f#",
                "#.######################",
                "#.....@.a.B.c.d.A.e.F.g#",
                "########################"
        );
        test1(81,
                "########################",
                "#@..............ac.GI.b#",
                "###d#e#f################",
                "###A#B#C################",
                "###g#h#i################",
                "########################"
        );
    }
}
