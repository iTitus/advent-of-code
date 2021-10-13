package io.github.ititus.aoc.aoc18.day05;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;

@Aoc(year = 2018, day = 5)
public final class Day05 implements AocSolution {

    private String input;

    @Override
    public void executeTests() {
        readInput(new AocStringInput("aA"));
        System.out.println("Part 1 (expected 0): " + part1());

        readInput(new AocStringInput("Aa"));
        System.out.println("Part 1 (expected 0): " + part1());

        readInput(new AocStringInput("A"));
        System.out.println("Part 1 (expected 1): " + part1());

        readInput(new AocStringInput("Ab"));
        System.out.println("Part 1 (expected 2): " + part1());

        readInput(new AocStringInput("aAa"));
        System.out.println("Part 1 (expected 1): " + part1());

        readInput(new AocStringInput("aAaA"));
        System.out.println("Part 1 (expected 0): " + part1());

        readInput(new AocStringInput("aAbB"));
        System.out.println("Part 1 (expected 0): " + part1());

        readInput(new AocStringInput("dabAcCaCBAcCcaDA"));
        System.out.println("Part 1 (expected 10): " + part1());
    }

    @Override
    public void readInput(AocInput input) {
        this.input = input.readString().strip();

        // assume a-z
        for (int i = 0; i < this.input.length(); i++) {
            char c = this.input.charAt(i);
            if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z')) {
                throw new RuntimeException();
            }
        }
    }

    @Override
    public Object part1() {
        return reduce((byte) -1);
    }

    @Override
    public Object part2() {
        int min = Integer.MAX_VALUE;
        for (byte ignore = 'A'; ignore <= 'Z'; ignore++) {
            int result = reduce(ignore);
            if (result < min) {
                min = result;
            }
        }

        return min;
    }

    private int reduce(byte ignore) {
        ByteArrayList stack = new ByteArrayList();
        for (int i = 0; i < input.length(); i++) {
            byte c = (byte) input.charAt(i);
            if (toUpperCase(c) == ignore) {
                continue;
            }

            stack.push(c);

            while (stack.size() >= 2) {
                if (shouldAnnihilate(stack.getByte(stack.size() - 1), stack.getByte(stack.size() - 2))) {
                    stack.removeByte(stack.size() - 1);
                    stack.removeByte(stack.size() - 1);
                } else {
                    break;
                }
            }
        }

        return stack.size();
    }

    private boolean shouldAnnihilate(byte c1, byte c2) {
        return c1 != c2 && toUpperCase(c1) == toUpperCase(c2);
    }

    private byte toUpperCase(byte c) {
        if (c >= 'a') {
            return (byte) (c - 32);
        }

        return c;
    }
}
