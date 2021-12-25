package io.github.ititus.aoc.aoc21.day24;

import java.util.ArrayList;
import java.util.List;

public abstract sealed class Insn permits Insn.ImmBinInsn, Insn.Inp, Insn.RegBinInsn {

    private final String name;
    private final char target;

    protected Insn(String name, char target) {
        this.name = name;
        this.target = target;
    }

    private static char parse(String s) {
        if (s.length() != 1) {
            throw new RuntimeException();
        }

        return s.charAt(0);
    }

    public static List<Insn> parse(List<String> lines) {
        List<Insn> insns = new ArrayList<>();
        for (String line : lines) {
            if (line.isEmpty()) {
                continue;
            }

            String[] s = line.split(" ", 3);
            if (s.length < 2) {
                throw new RuntimeException();
            }

            String op = s[0];
            if ("inp".equals(op)) {
                insns.add(new Inp(parse(s[1])));
            } else {
                char target = parse(s[1]);
                String rightString = s[2];
                if (rightString.length() == 1) {
                    char left = rightString.charAt(0);
                    if (left == 'x' || left == 'y' || left == 'z' || left == 'w') {
                        insns.add(new RegBinInsn(op, target, left));
                        continue;
                    }
                }

                int right = Integer.parseInt(rightString);
                insns.add(new ImmBinInsn(op, target, right));
            }
        }

        return insns;
    }

    public String name() {
        return name;
    }

    public char target() {
        return target;
    }

    public static final class Inp extends Insn {

        public Inp(char target) {
            super("inp", target);
        }

        @Override
        public String toString() {
            return target() + " = " + name();
        }
    }

    public static final class ImmBinInsn extends Insn {

        private final long right;

        public ImmBinInsn(String name, char target, long right) {
            super(name, target);
            this.right = right;
        }

        public long right() {
            return right;
        }

        @Override
        public String toString() {
            return target() + " = " + target() + " " + name() + " " + right;
        }
    }

    public static final class RegBinInsn extends Insn {

        private final char right;

        public RegBinInsn(String name, char target, char right) {
            super(name, target);
            this.right = right;
        }

        public char right() {
            return right;
        }

        @Override
        public String toString() {
            return target() + " = " + target() + " " + name() + " " + right;
        }
    }
}
