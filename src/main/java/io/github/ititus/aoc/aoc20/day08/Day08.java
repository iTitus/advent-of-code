package io.github.ititus.aoc.aoc20.day08;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

import java.util.ArrayList;
import java.util.List;

@Aoc(year = 2020, day = 8)
public class Day08 implements AocSolution {

    private List<String> lines;

    private static boolean switchInstruction(List<String> list, int i) {
        String wrong = list.get(i);

        String fixed;
        if (wrong.startsWith("jmp ")) {
            fixed = "nop ";
        } else if (wrong.startsWith("nop ")) {
            fixed = "jmp ";
        } else {
            return false;
        }

        fixed += wrong.substring(4);

        list.set(i, fixed);
        return true;
    }

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        lines = input.readAllLines();
    }

    @Override
    public Object part1() {
        HandheldGameConsole c = HandheldGameConsole.of(lines);
        IntSet visited = new IntOpenHashSet();

        while (!visited.contains(c.getInstructionPointer())) {
            visited.add(c.getInstructionPointer());
            c.step();
        }

        return c.getAccumulator();
    }

    @Override
    public Object part2() {
        List<String> fixedLines = new ArrayList<>(lines);
        for (int i = 0; i < fixedLines.size(); i++) {
            if (!switchInstruction(fixedLines, i)) {
                continue;
            }

            IntSet visited = new IntOpenHashSet();
            HandheldGameConsole fixedConsole = HandheldGameConsole.of(fixedLines);
            while (!visited.contains(fixedConsole.getInstructionPointer())) {
                visited.add(fixedConsole.getInstructionPointer());
                fixedConsole.step();
                if (fixedConsole.isExit()) {
                    return fixedConsole.getAccumulator();
                }
            }

            switchInstruction(fixedLines, i);
        }

        return null;
    }
}
