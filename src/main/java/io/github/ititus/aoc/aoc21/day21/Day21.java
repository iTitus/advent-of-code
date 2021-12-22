package io.github.ititus.aoc.aoc21.day21;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;

import java.util.List;

@Aoc(year = 2021, day = 21)
public class Day21 implements AocSolution {

    static final int BOARD_MOD = 10;
    static final int PART_1_SCORE = 1000;
    static final int PART_2_SCORE = 21;
    static final int[] DISTINCT_DICE_SUMS = { 3, 4, 5, 6, 7, 8, 9 };
    static final int[] DICE_SUM_FREQS = {
            0, 0, 0,
            1,
            3,
            6, 7, 6,
            3,
            1
    };

    int player1Start;
    int player2Start;

    @Override
    public void executeTests() {
        AocInput input = new AocStringInput("""
                Player 1 starting position: 4
                Player 2 starting position: 8""");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());
    }

    @Override
    public void readInput(AocInput input) {
        List<String> lines = input.readAllLines();
        player1Start = Integer.parseInt(lines.get(0).strip().substring("Player 1 starting position: ".length())) - 1;
        player2Start = Integer.parseInt(lines.get(1).strip().substring("Player 2 starting position: ".length())) - 1;
    }

    void playDirac(WinTracker wt, int pos1, int score1, int pos2, int score2, long universeSplit) {
        for (int diceSum1 : DISTINCT_DICE_SUMS) {
            int newPos1 = (pos1 + diceSum1) % BOARD_MOD;
            int newScore1 = score1 + newPos1 + 1;
            if (newScore1 >= PART_2_SCORE) {
                wt.p1Wins += universeSplit * DICE_SUM_FREQS[diceSum1];
                continue;
            }

            for (int diceSum2 : DISTINCT_DICE_SUMS) {
                int newPos2 = (pos2 + diceSum2) % BOARD_MOD;
                int newScore2 = score2 + newPos2 + 1;
                if (newScore2 >= PART_2_SCORE) {
                    wt.p2Wins += universeSplit * DICE_SUM_FREQS[diceSum1] * DICE_SUM_FREQS[diceSum2];
                    continue;
                }

                playDirac(wt, newPos1, newScore1, newPos2, newScore2, universeSplit * DICE_SUM_FREQS[diceSum1] * DICE_SUM_FREQS[diceSum2]);
            }
        }
    }

    @Override
    public Object part1() {
        final int DIE_MOD = 100;

        int pos1 = player1Start, pos2 = player2Start;
        int score1 = 0, score2 = 0;
        int die = 1;
        int rolls = 0;

        while (true) {
            pos1 += die++ + die++ + die++;
            rolls += 3;
            die %= DIE_MOD;
            pos1 %= BOARD_MOD;
            score1 += pos1 + 1;
            if (score1 >= PART_1_SCORE) {
                return score2 * rolls;
            }

            pos2 += die++ + die++ + die++;
            rolls += 3;
            die %= DIE_MOD;
            pos2 %= BOARD_MOD;
            score2 += pos2 + 1;
            if (score2 >= PART_1_SCORE) {
                return score1 * rolls;
            }
        }
    }

    @Override
    public Object part2() {
        WinTracker wt = new WinTracker();
        playDirac(wt, player1Start, 0, player2Start, 0, 1);
        return Math.max(wt.p1Wins, wt.p2Wins);
    }

    private static class WinTracker {

        long p1Wins;
        long p2Wins;

    }
}
