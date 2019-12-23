package io.github.ititus.aoc.aoc19.day18;

import io.github.ititus.aoc.InputProvider;
import io.github.ititus.math.time.DurationFormatter;
import io.github.ititus.math.time.StopWatch;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class Day18 {

    public static void main(String[] args) {
        List<String> lines = InputProvider.readAllLines(2019, 18);

        test();

        // 1
        System.out.println("### 1 ###");
        StopWatch sw = StopWatch.createRunning();
        int ret1 = execute(false, lines);
        Duration d = sw.stop();
        System.out.println(DurationFormatter.format(d) + " : " + ret1);

        // 2
        System.out.println("### 2 ###");
        sw.start();
        int ret2 = execute(true, lines);
        d = sw.stop();
        System.out.println(DurationFormatter.format(d) + " : " + ret2);
    }

    private static int execute(boolean changeMap, List<String> input) {
        TritonVault tv = new TritonVault(changeMap, input);
        int bestPathLength = tv.findBestPathLength();
        tv.printState();
        return bestPathLength;
    }

    private static void test() {
        test1(8,
                "#########",
                "#b.A.@.a#",
                "#########"
        );
        test1(86,
                "########################",
                "#f.D.E.e.C.b.A.@.a.B.c.#",
                "######################.#",
                "#d.....................#",
                "########################"
        );
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
        test1(136,
                "#################",
                "#i.G..c...e..H.p#",
                "########.########",
                "#j.A..b...f..D.o#",
                "########@########",
                "#k.E..a...g..B.n#",
                "########.########",
                "#l.F..d...h..C.m#",
                "#################"
        );

        test2(8,
                true,
                "#######",
                "#a.#Cd#",
                "##...##",
                "##.@.##",
                "##...##",
                "#cB#Ab#",
                "#######"
        );
        test2(24,
                false,
                "###############",
                "#d.ABC.#.....a#",
                "######@#@######",
                "###############",
                "######@#@######",
                "#b.....#.....c#",
                "###############"
        );
        test2(32,
                false,
                "#############",
                "#DcBa.#.GhKl#",
                "#.###@#@#I###",
                "#e#d#####j#k#",
                "###C#@#@###J#",
                "#fEbA.#.FgHi#",
                "#############"
        );
        test2(72,
                false,
                "#############",
                "#g#f.D#..h#l#",
                "#F###e#E###.#",
                "#dCba@#@BcIJ#",
                "#############",
                "#nK.L@#@G...#",
                "#M###N#H###.#",
                "#o#m..#i#jk.#",
                "#############"
        );
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

    private static void test2(int expected, boolean changeMap, String... input) {
        StopWatch s = StopWatch.createRunning();
        int actual = execute(changeMap, Arrays.asList(input));
        Duration d = s.stop();
        if (actual != expected) {
            throw new RuntimeException("Part 1: expected=" + expected + " actual=" + actual);
        } else {
            System.out.println("Successfully passed test in " + DurationFormatter.format(d) + " for Part 2: output=" + actual);
        }
    }
}
