package io.github.ititus.aoc.aoc21.day10;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;
import io.github.ititus.commons.text.TextUtil;
import it.unimi.dsi.fastutil.chars.AbstractCharList;
import it.unimi.dsi.fastutil.chars.CharArrayList;
import it.unimi.dsi.fastutil.chars.CharImmutableList;

import java.util.List;

@Aoc(year = 2021, day = 10)
public class Day10 implements AocSolution {

    List<String> lines;

    static int getSyntaxErrorScore(char c) {
        return switch (c) {
            case '<', '>' -> 25137;
            case '{', '}' -> 1197;
            case '[', ']' -> 57;
            case '(', ')' -> 3;
            default -> throw new IllegalStateException();
        };
    }

    static long getAutoCompleteScore(String s) {
        long score = 0;
        for (int i = 0; i < s.length(); i++) {
            score *= 5;
            score += switch (s.charAt(i)) {
                case '<', '>' -> 4;
                case '{', '}' -> 3;
                case '[', ']' -> 2;
                case '(', ')' -> 1;
                default -> throw new IllegalArgumentException();
            };
        }

        return score;
    }

    @Override
    public void executeTests() {
        AocInput input = new AocStringInput("""
                [({(<(())[]>[[{[]{<()<>>
                [(()[<>])]({[<{<<[]>>(
                {([(<{}[<>[]}>{[]{[(<()>
                (((({<>}<{<{<>}{[]{[]{}
                [[<[([]))<([[{}[[()]]]
                [{[{({}]{}}([{[{{{}}([]
                {<[[]]>}<{[{[{[]{()[[[]
                [<(<(<(<{}))><([]([]()
                <{([([[(<>()){}]>(<<{{
                <{([{{}}[<[[[<>{}]]]>[]]""");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());
    }

    @Override
    public void readInput(AocInput input) {
        lines = input.readAllLines();
    }

    @Override
    public Object part1() {
        return lines.stream()
                .mapToInt(State::parseAndGetSyntaxErrorScore)
                .sum();
    }

    @Override
    public Object part2() {
        long[] scores = lines.stream()
                .mapToLong(State::parseAndGetAutocompleteScore)
                .filter(s -> s > 0)
                .sorted()
                .toArray();
        if (scores.length % 2 == 0) {
            throw new RuntimeException();
        }

        return scores[scores.length / 2];
    }

    static class State {

        private final AbstractCharList stack;

        private State(AbstractCharList stack) {
            this.stack = stack;
        }

        static State empty() {
            return new State(CharImmutableList.of());
        }

        static int parseAndGetSyntaxErrorScore(String line) {
            State state = empty();
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                state = state.read(c);
                if (state == null) {
                    return getSyntaxErrorScore(c);
                }
            }

            return 0;
        }

        static long parseAndGetAutocompleteScore(String line) {
            State state = empty();
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                state = state.read(c);
                if (state == null) {
                    return 0;
                }
            }

            return getAutoCompleteScore(state.getAutoComplete());
        }

        State read(char c) {
            return switch (c) {
                case '(' -> push('(');
                case ')' -> pop('(');
                case '[' -> push('[');
                case ']' -> pop('[');
                case '{' -> push('{');
                case '}' -> pop('{');
                case '<' -> push('<');
                case '>' -> pop('<');
                default -> throw new IllegalArgumentException();
            };
        }

        State push(char c) {
            AbstractCharList stack = new CharArrayList(this.stack);
            stack.push(c);
            return new State(new CharImmutableList(stack));
        }

        State pop(char c) {
            if (stack.isEmpty() || stack.peekChar(0) != c) {
                return null;
            }

            AbstractCharList stack = new CharArrayList(this.stack);
            stack.popChar();
            return new State(new CharImmutableList(stack));
        }

        String getAutoComplete() {
            return TextUtil.reverse(String.valueOf(stack.toCharArray()));
        }
    }
}
