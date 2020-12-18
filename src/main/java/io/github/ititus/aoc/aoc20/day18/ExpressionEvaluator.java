package io.github.ititus.aoc.aoc20.day18;

import it.unimi.dsi.fastutil.Stack;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.ArrayList;
import java.util.List;

public class ExpressionEvaluator {

    private static boolean isWhitespace(char c) {
        return c == ' ';
    }

    private static boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }

    private static List<String> tokenize(String expression) {
        char[] chars = expression.toCharArray();
        List<String> tokens = new ArrayList<>();
        int i = 0;
        while (i < chars.length) {
            // whitespaces
            if (isWhitespace(chars[i])) {
                i++;
                continue;
            }

            // digits
            int start = i;
            while (i < chars.length && isDigit(chars[i])) {
                i++;
            }
            if (i > start) {
                tokens.add(new String(chars, start, i - start));
                continue;
            }

            // operators that consist only of 1 char
            if (Operator.get(chars[i]) != null) {
                tokens.add(Character.toString(chars[i]));
                i++;
                continue;
            }

            throw new RuntimeException("unexpected character");
        }

        return tokens;
    }

    public static long evaluate(String expression, boolean part2) {
        List<String> tokens = tokenize(expression);

        Stack<Operator> ops = new ObjectArrayList<>();
        LongStack output = new LongArrayList();
        for (String token : tokens) {
            Operator op = Operator.get(token);
            if (op == null) { // number
                output.push(Long.parseLong(token));
            } else if (op == Operator.OPEN) {
                ops.push(op);
            } else if (op == Operator.CLOSE) {
                boolean foundOpeningBracket = false;
                while (!ops.isEmpty()) {
                    Operator next = ops.pop();
                    if (next == Operator.OPEN) {
                        foundOpeningBracket = true;
                        break;
                    }

                    output.push(next.apply(output));
                }

                if (!foundOpeningBracket) {
                    throw new RuntimeException("no opening bracket");
                }
            } else { // binary operator + or *
                while (!ops.isEmpty()
                        && !ops.peek(0).isBracket()
                        && (op.precedence(part2) < ops.peek(0).precedence(part2)
                        || (op.isLeftAssociative() && op.precedence(part2) == ops.peek(0).precedence(part2)))) {
                    output.push(ops.pop().apply(output));
                }

                ops.push(op);
            }
        }

        while (!ops.isEmpty()) {
            Operator next = ops.pop();
            if (next == Operator.OPEN) {
                throw new RuntimeException("too many opening brackets");
            }

            output.push(next.apply(output));
        }

        if (output.isEmpty()) {
            throw new RuntimeException("empty expression");
        }

        long result = output.popLong();
        if (!output.isEmpty()) {
            throw new RuntimeException("dangling tokens");
        }

        return result;
    }
}
