package io.github.ititus.aoc.aoc21.day04;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;
import it.unimi.dsi.fastutil.ints.*;

import java.util.*;

@Aoc(year = 2021, day = 4)
public class Day04 implements AocSolution {

    private IntList numbers;
    private List<BingoBoard> boards;

    @Override
    public void executeTests() {
        AocInput input = new AocStringInput("""          
                7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

                22 13 17 11  0
                 8  2 23  4 24
                21  9 14 16  7
                 6 10  3 18  5
                 1 12 20 15 19

                 3 15  0  2 22
                 9 18 13 17  5
                19  8  7 25 23
                20 11 10 24  4
                14 21 16 12  6

                14 21 17 24  4
                10 16 15  9 19
                18  8 23 26 20
                22 11 13  6  5
                 2  0 12  3  7""");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());
    }

    @Override
    public void readInput(AocInput input) {
        List<String> lines = input.readAllLines();
        numbers = Arrays.stream(lines.get(0).split(","))
                .mapToInt(Integer::parseInt)
                .collect(IntArrayList::new, IntList::add, IntList::addAll);

        boards = new ArrayList<>();
        for (int i = 2; i < lines.size(); i += 6) {
            boards.add(BingoBoard.read(lines.subList(i, i + 5)));
        }
    }

    @Override
    public Object part1() {
        IntSet drawnNumbers = new IntOpenHashSet();
        for (IntIterator it = numbers.intIterator(); it.hasNext(); ) {
            int n = it.nextInt();
            drawnNumbers.add(n);
            if (drawnNumbers.size() < 5) {
                continue;
            }

            for (BingoBoard board : boards) {
                OptionalInt sum = board.sumOfUnmarkedAfterWin(drawnNumbers);
                if (sum.isPresent()) {
                    return sum.getAsInt() * n;
                }
            }
        }

        throw new RuntimeException();
    }

    @Override
    public Object part2() {
        List<BingoBoard> boardsWithoutWinner = new ArrayList<>(boards);
        IntSet drawnNumbers = new IntOpenHashSet();
        for (IntIterator nit = numbers.intIterator(); nit.hasNext(); ) {
            int n = nit.nextInt();
            drawnNumbers.add(n);
            if (drawnNumbers.size() < 5) {
                continue;
            }

            int lastScore = -1;
            for (Iterator<BingoBoard> bit = boardsWithoutWinner.iterator(); bit.hasNext(); ) {
                BingoBoard board = bit.next();
                OptionalInt sum = board.sumOfUnmarkedAfterWin(drawnNumbers);
                if (sum.isPresent()) {
                    lastScore = sum.getAsInt() * n;
                    bit.remove();
                }
            }

            if (boardsWithoutWinner.isEmpty()) {
                return lastScore;
            }
        }

        throw new RuntimeException();
    }

    private record BingoBoard(
            IntList board
    ) {

        BingoBoard {
            if (board.size() != 5 * 5) {
                throw new IllegalArgumentException();
            }
        }

        static BingoBoard read(List<String> lines) {
            return new BingoBoard(lines.stream()
                    .flatMap(l -> Arrays.stream(l.split(" ")))
                    .filter(s -> !s.isEmpty())
                    .mapToInt(Integer::parseInt)
                    .collect(IntArrayList::new, IntList::add, IntList::addAll));
        }

        OptionalInt sumOfUnmarkedAfterWin(IntSet drawnNumbers) {
            outer:
            for (int y = 0; y < 5; y++) {
                for (int x = 0; x < 5; x++) {
                    if (!drawnNumbers.contains(board.getInt(x + y * 5))) {
                        continue outer;
                    }
                }

                return OptionalInt.of(calSumOfUnmarked(drawnNumbers));
            }

            outer:
            for (int x = 0; x < 5; x++) {
                for (int y = 0; y < 5; y++) {
                    if (!drawnNumbers.contains(board.getInt(x + y * 5))) {
                        continue outer;
                    }
                }

                return OptionalInt.of(calSumOfUnmarked(drawnNumbers));
            }

            return OptionalInt.empty();
        }

        private int calSumOfUnmarked(IntSet drawnNumbers) {
            int sum = 0;
            for (int y = 0; y < 5; y++) {
                for (int x = 0; x < 5; x++) {
                    int n = board.getInt(x + y * 5);
                    if (!drawnNumbers.contains(n)) {
                        sum += n;
                    }
                }
            }

            return sum;
        }
    }
}
