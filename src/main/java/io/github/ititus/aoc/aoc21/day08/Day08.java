package io.github.ititus.aoc.aoc21.day08;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;
import it.unimi.dsi.fastutil.chars.CharOpenHashSet;
import it.unimi.dsi.fastutil.chars.CharSet;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Aoc(year = 2021, day = 8)
public class Day08 implements AocSolution {

    List<Entry> input;

    @Override
    public void executeTests() {
        AocInput input = new AocStringInput("acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf");
        readInput(input);
        System.out.println(part2());

        System.out.println();
        input = new AocStringInput("""
                be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
                edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
                fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
                fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
                aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
                fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
                dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
                bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
                egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
                gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce""");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());
    }

    @Override
    public void readInput(AocInput input) {
        this.input = input.lines()
                .map(Entry::parse)
                .toList();
    }

    @Override
    public Object part1() {
        return input.stream()
                .map(Entry::outputs)
                .flatMap(Collection::stream)
                .filter(SignalPattern::isEasyDigit)
                .count();
    }

    @Override
    public Object part2() {
        return input.stream()
                .mapToInt(Entry::decode)
                .sum();
    }

    record Entry(
            List<SignalPattern> inputs,
            List<SignalPattern> outputs
    ) {

        Entry {
            if (inputs.size() != 10 || outputs.size() != 4) {
                throw new IllegalArgumentException();
            }
        }

        private static int intersectSize(CharSet s1, CharSet s2) {
            CharSet s = new CharOpenHashSet(s1);
            s.retainAll(s2);
            return s.size();
        }

        static Entry parse(String s) {
            String[] patterns = s.split(" \\| ", 2);
            return new Entry(parsePattern(patterns[0]), parsePattern(patterns[1]));
        }

        static List<SignalPattern> parsePattern(String s) {
            return Arrays.stream(s.split(" "))
                    .map(SignalPattern::parse)
                    .toList();
        }

        int decode() {
            CharSet p1 = findPatternWithSize(2);
            CharSet p4 = findPatternWithSize(4);

            int[] outputDeductions = outputs.stream()
                    .map(SignalPattern::pattern)
                    .mapToInt(p -> {
                        int size = p.size();
                        if (size == 2) {
                            return 1;
                        } else if (size == 3) {
                            return 7;
                        } else if (size == 4) {
                            return 4;
                        } else if (size == 7) {
                            return 8;
                        } else if (size == 5) {
                            if (intersectSize(p, p1) == 1) {
                                return intersectSize(p, p4) == 3 ? 5 : 2;
                            } else {
                                return 3;
                            }
                        } else if (size == 6) {
                            if (intersectSize(p, p1) == 2) {
                                return intersectSize(p, p4) == 4 ? 9 : 0;
                            } else {
                                return 6;
                            }
                        }

                        throw new RuntimeException();
                    })
                    .toArray();

            int result = 0;
            for (int n : outputDeductions) {
                result *= 10;
                result += n;
            }

            return result;
        }

        CharSet findPatternWithSize(int size) {
            List<CharSet> list = inputs.stream()
                    .map(SignalPattern::pattern)
                    .filter(p -> p.size() == size)
                    .toList();
            if (list.size() != 1) {
                throw new RuntimeException();
            }

            return list.get(0);
        }
    }

    record SignalPattern(
            CharSet pattern
    ) {

        SignalPattern {
            if (pattern.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }

        static SignalPattern parse(String s) {
            return new SignalPattern(CharSet.of(s.toCharArray()));
        }

        boolean isEasyDigit() {
            int size = pattern.size();
            return size == 2 || size == 3 || size == 4 || size == 7;
        }
    }
}
