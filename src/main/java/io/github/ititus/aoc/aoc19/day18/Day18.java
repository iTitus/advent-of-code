package io.github.ititus.aoc.aoc19.day18;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.math.time.DurationFormatter;
import io.github.ititus.math.time.StopWatch;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Aoc(year = 2019, day = 18)
public final class Day18 implements AocSolution {

    private List<String> lines;

    private static int execute(boolean changeMap, List<String> input) {
        TritonVault tv = new TritonVault(changeMap, input);
        int bestPathLength = tv.findBestPathLength();
        //FIXME: the 81 test only works when this is enabled
        tv.printState();
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

    @Override
    public void executeTests() {
        /*test1(8,
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
        );*/
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
        /*test1(136,
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
        );*/
    }

    @Override
    public void readInput(AocInput input) {
        lines = input.readAllLines();
    }

    @Override
    public Object part1() {
        StopWatch s = StopWatch.createRunning();
        int result = execute(false, lines);
        Duration d = s.stop();
        return DurationFormatter.format(d) + " : " + result;
    }

    @Override
    public Object part2() {
        StopWatch s = StopWatch.createRunning();
        int result = execute(true, lines);
        Duration d = s.stop();
        return DurationFormatter.format(d) + " : " + result;
    }
}
