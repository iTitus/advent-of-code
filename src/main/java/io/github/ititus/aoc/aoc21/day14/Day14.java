package io.github.ititus.aoc.aoc21.day14;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;
import it.unimi.dsi.fastutil.chars.Char2LongLinkedOpenHashMap;
import it.unimi.dsi.fastutil.chars.Char2LongMap;
import it.unimi.dsi.fastutil.chars.CharCharPair;
import it.unimi.dsi.fastutil.objects.Object2CharMap;
import it.unimi.dsi.fastutil.objects.Object2CharOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

import java.util.List;

@Aoc(year = 2021, day = 14)
public class Day14 implements AocSolution {

    String initial;
    Object2CharMap<CharCharPair> rules;

    @Override
    public void executeTests() {
        AocInput input = new AocStringInput("""
                NNCB
                                
                CH -> B
                HH -> N
                CB -> H
                NH -> C
                HB -> C
                HC -> B
                HN -> C
                NN -> C
                BH -> H
                NC -> B
                NB -> B
                BN -> B
                BB -> N
                BC -> B
                CC -> N
                CN -> C""");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());
    }

    @Override
    public void readInput(AocInput input) {
        List<String> lines = input.readAllLines();
        initial = lines.get(0).strip();
        rules = new Object2CharOpenHashMap<>();
        lines.stream()
                .skip(1)
                .map(String::strip)
                .filter(s -> !s.isEmpty())
                .forEach(s -> {
                    if (s.length() != 7 || s.charAt(6) == '\0') {
                        throw new RuntimeException();
                    }

                    rules.put(CharCharPair.of(s.charAt(0), s.charAt(1)), s.charAt(6));
                });
    }

    @Override
    public Object part1() {
        return step(10);
    }

    @Override
    public Object part2() {
        return step(40);
    }

    private long step(int n) {
        Char2LongMap freqs = new Char2LongLinkedOpenHashMap();
        Object2LongMap<CharCharPair> pairs = new Object2LongOpenHashMap<>();

        char prev = initial.charAt(0);
        freqs.mergeLong(prev, 1, Long::sum);
        for (int i = 1, length = initial.length(); i < length; i++) {
            char cur = initial.charAt(i);
            freqs.mergeLong(cur, 1, Long::sum);
            pairs.mergeLong(CharCharPair.of(prev, cur), 1, Long::sum);
            prev = cur;
        }

        for (int i = 0; i < n; i++) {
            Object2LongMap<CharCharPair> newPairs = new Object2LongOpenHashMap<>();
            for (Object2LongMap.Entry<CharCharPair> entry : pairs.object2LongEntrySet()) {
                CharCharPair in = entry.getKey();
                long count = entry.getLongValue();
                if (count > 0) {
                    char out = rules.getChar(in);
                    if (out != '\0') {
                        freqs.mergeLong(out, count, Long::sum);
                        newPairs.mergeLong(CharCharPair.of(in.leftChar(), out), count, Long::sum);
                        newPairs.mergeLong(CharCharPair.of(out, in.rightChar()), count, Long::sum);
                    }
                } else {
                    throw new RuntimeException();
                }
            }

            pairs = newPairs;
        }

        return freqs.values().longStream().max().orElseThrow() - freqs.values().longStream().min().orElseThrow();
    }
}
