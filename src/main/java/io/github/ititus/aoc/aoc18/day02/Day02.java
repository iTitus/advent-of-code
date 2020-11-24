package io.github.ititus.aoc.aoc18.day02;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Aoc(year = 2018, day = 2)
public final class Day02 implements AocSolution {

    private List<String> ids;

    private static int hammingDistance(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() != s2.length()) {
            throw new IllegalArgumentException();
        }

        int d = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                d++;
            }
        }

        return d;
    }

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        ids = input.readAllLines();
    }

    @Override
    public Object part1() {
        List<RepStats> repStats = ids.stream()
                .map(RepStats::of)
                .collect(Collectors.toList());
        long twiceRepeat = repStats.stream().filter(RepStats::isTwice).count();
        long thriceRepeat = repStats.stream().filter(RepStats::isThrice).count();
        long checksum = twiceRepeat * thriceRepeat;

        return checksum;
    }

    @Override
    public Object part2() {
        for (int i = 0; i < ids.size(); i++) {
            for (int j = i + 1; j < ids.size(); j++) {
                String s1 = ids.get(i);
                String s2 = ids.get(j);
                if (hammingDistance(s1, s2) == 1) {
                    return s1 + "\n" + s2;
                }
            }
        }

        return null;
    }

    private static final class RepStats {

        private final boolean twice;
        private final boolean thrice;

        private RepStats(boolean twice, boolean thrice) {
            this.twice = twice;
            this.thrice = thrice;
        }

        private static RepStats of(String s) {
            Map<Integer, Long> counts = s.chars()
                    .boxed()
                    .collect(groupingBy(Function.identity(), counting()));
            return new RepStats(counts.containsValue(2L), counts.containsValue(3L));
        }

        private boolean isTwice() {
            return twice;
        }

        private boolean isThrice() {
            return thrice;
        }
    }
}
