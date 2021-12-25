package io.github.ititus.aoc.aoc21.day24;

public class ALU {

    private long x, y, z, w;

    private long getValueLeft(Insn insn) {
        return getValue(insn.target());
    }

    private long getValueRight(Insn insn) {
        if (insn instanceof Insn.ImmBinInsn ibi) {
            return ibi.right();
        } else if (insn instanceof Insn.RegBinInsn vbi) {
            return getValue(vbi.right());
        }

        throw new RuntimeException();
    }

    private long getValue(char c) {
        return switch (c) {
            case 'x' -> x;
            case 'y' -> y;
            case 'z' -> z;
            case 'w' -> w;
            default -> throw new RuntimeException();
        };
    }

    private void write(char target, long value) {
        if (target == 'x') {
            x = value;
        } else if (target == 'y') {
            y = value;
        } else if (target == 'z') {
            z = value;
        } else if (target == 'w') {
            w = value;
        } else {
            throw new RuntimeException();
        }
    }

    void execute(Insn insn) {
        write(insn.target(), switch (insn.name()) {
            case "inp" -> throw new UnsupportedOperationException();
            case "add" -> getValueLeft(insn) + getValueRight(insn);
            case "mul" -> getValueLeft(insn) * getValueRight(insn);
            case "div" -> getValueLeft(insn) / getValueRight(insn);
            case "mod" -> {
                long l = getValueLeft(insn);
                long r = getValueRight(insn);
                if (l < 0 || r < 0) {
                    throw new RuntimeException();
                }

                yield l % r;
            }
            case "eql" -> getValueLeft(insn) == getValueRight(insn) ? 1 : 0;
            default -> throw new RuntimeException();
        });
    }

    long executeAll(Iterable<? extends Insn> it) {
        for (Insn insn : it) {
            execute(insn);
        }

        return z;
    }
}
