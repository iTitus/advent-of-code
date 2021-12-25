package io.github.ititus.aoc.aoc21.day24;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;

import java.util.List;

@Aoc(year = 2021, day = 24)
public class Day24 implements AocSolution {

    List<Insn> insns;

    private static void doBlock(int n, long initialZ) {
        boolean divZ = n >= 10 || n == 7 || n == 4;
        int addX = switch (n) {
            case 1, 6 -> 12;
            case 2 -> 11;
            case 3, 8, 9 -> 14;
            case 4 -> -6;
            case 5 -> 15;
            case 7, 11 -> -9;
            case 10, 12 -> -5;
            case 13 -> -2;
            case 14 -> -7;
            default -> throw new RuntimeException();
        };
        int addY = switch (n) {
            case 1 -> 4;
            case 2 -> 10;
            case 3 -> 12;
            case 4 -> 14;
            case 5 -> 6;
            case 6 -> 16;
            case 7, 13 -> 1;
            case 8 -> 7;
            case 9, 11, 14 -> 8;
            case 10 -> 11;
            case 12 -> 3;
            default -> throw new RuntimeException();
        };

        System.out.println();
        boolean outOfBounds = (1 - addX) >= 26 || (9 - addX) < 0;
        System.out.println(n + " (z" + (divZ ? "/26" : "") + " * " + (outOfBounds ? "26" : "(25 * [z%26 != i + " + -addX + "] + 1)") + " + (i + " + addY + ")" + (outOfBounds ? "" : " * [z%26 != i + " + -addX + "]") + "):");
        for (int i = 9; i >= 1; i--) {
            long z = new ALU().executeAll(block(i, initialZ, divZ, addX, addY));
            System.out.printf("i%02d=%d => z%02d=%d%n", n, i, n, z);
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private static List<Insn> block(int input, long initialZ, boolean divZ, int addX, int addY) {
        char x = 'x';
        char y = 'y';
        char z = 'z';
        char w = 'w';

        return List.of(
                new Insn.ImmBinInsn("mul", z, 0),
                new Insn.ImmBinInsn("add", z, initialZ),
                new Insn.ImmBinInsn("mul", w, 0),
                new Insn.ImmBinInsn("add", w, input),
                // new Insn.Inp(w),
                new Insn.ImmBinInsn("mul", x, 0),
                new Insn.RegBinInsn("add", x, z),
                new Insn.ImmBinInsn("mod", x, 26),
                new Insn.ImmBinInsn("div", z, divZ ? 26 : 1),
                new Insn.ImmBinInsn("add", x, addX),
                new Insn.RegBinInsn("eql", x, w),
                new Insn.ImmBinInsn("eql", x, 0),
                new Insn.ImmBinInsn("mul", y, 0),
                new Insn.ImmBinInsn("add", y, 25),
                new Insn.RegBinInsn("mul", y, x),
                new Insn.ImmBinInsn("add", y, 1),
                new Insn.RegBinInsn("mul", z, y),
                new Insn.ImmBinInsn("mul", y, 0),
                new Insn.RegBinInsn("add", y, w),
                new Insn.ImmBinInsn("add", y, addY),
                new Insn.RegBinInsn("mul", y, x),
                new Insn.RegBinInsn("add", z, y)
        );
    }

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        insns = Insn.parse(input.readAllLines());
    }

    @Override
    public Object part1() {
        /*
        assume i04 = i03 + 6
        assume i07 = i06 + 7
        assume i10 = i09 + 3
        assume i11 = i08 - 2
        assume i12 = i05 + 1
        assume i13 = i02 + 8
        assume i14 = i01 - 3
         */

        doBlock(1, 0); // 9
        doBlock(2, 13); // 1
        doBlock(3, 349); // 3
        doBlock(4, 9089); // 9
        doBlock(5, 349); // 8
        doBlock(6, 9088); // 2
        doBlock(7, 236306); // 9
        doBlock(8, 9088); // 9
        doBlock(9, 236304); // 6
        doBlock(10, 6143918); // 9
        doBlock(11, 236304); // 7
        doBlock(12, 9088); // 9
        doBlock(13, 349); // 9
        doBlock(14, 13); // 6

        return 91398299697996L;
    }

    @Override
    public Object part2() {
        doBlock(1, 0); // 4
        doBlock(2, 8); // 1
        doBlock(3, 219); // 1
        doBlock(4, 5707); // 7
        doBlock(5, 219); // 1
        doBlock(6, 5701); // 1
        doBlock(7, 148243); // 8
        doBlock(8, 5701); // 3
        doBlock(9, 148236); // 1
        doBlock(10, 3854145); // 4
        doBlock(11, 148236); // 1
        doBlock(12, 5701); // 2
        doBlock(13, 219); // 9
        doBlock(14, 8); // 1

        return 41171183141291L;
    }
}
