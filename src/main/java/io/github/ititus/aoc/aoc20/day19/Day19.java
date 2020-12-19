package io.github.ititus.aoc.aoc20.day19;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;

import java.util.stream.Stream;

@Aoc(year = 2020, day = 19)
public class Day19 implements AocSolution {

    private Messages messages;

    @Override
    public void executeTests() {
        readInput(new AocStringInput("""
                42: 9 14 | 10 1
                9: 14 27 | 1 26
                10: 23 14 | 28 1
                1: "a"
                11: 42 31
                5: 1 14 | 15 1
                19: 14 1 | 14 14
                12: 24 14 | 19 1
                16: 15 1 | 14 14
                31: 14 17 | 1 13
                6: 14 14 | 1 14
                2: 1 24 | 14 4
                0: 8 11
                13: 14 3 | 1 12
                15: 1 | 14
                17: 14 2 | 1 7
                23: 25 1 | 22 14
                28: 16 1
                4: 1 1
                20: 14 14 | 1 15
                3: 5 14 | 16 1
                27: 1 6 | 14 18
                14: "b"
                21: 14 1 | 1 14
                25: 1 1 | 1 14
                22: 14 14
                8: 42
                26: 14 22 | 1 20
                18: 15 15
                7: 14 5 | 1 21
                24: 14 1
                                
                bbabbbbaabaabba
                babbbbaabbbbbabbbbbbaabaaabaaa
                aaabbbbbbaaaabaababaabababbabaaabbababababaaa
                bbbbbbbaaaabbbbaaabbabaaa
                bbbababbbbaaaaaaaabbababaaababaabab
                ababaaaaaabaaab
                ababaaaaabbbaba
                baabbaaaabbaaaababbaababb
                abbbbabbbbaaaababbbbbbaaaababb
                aaaaabbaabaaaaababaa
                aaaabbaabbaaaaaaabbbabbbaaabbaabaaa
                aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba
                """));

        System.out.println("Part 1 (expected 3): " + part1());
        System.out.println("Part 2 (expected 12): " + part2());
    }

    @Override
    public void readInput(AocInput input) {
        try (Stream<String> stream = input.lines()) {
            messages = stream.collect(Messages.collector());
        }
    }

    @Override
    public Object part1() {
        return messages.countMatches(0);
    }

    @Override
    public Object part2() {
        return messages.countAdvancedMatches(0);
    }
}
