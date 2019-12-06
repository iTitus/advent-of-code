package io.github.ititus.aoc.aoc19;

import java.util.Arrays;
import java.util.Scanner;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class IntComputer {

    private final IntSupplier input;
    private final IntConsumer output;
    private final int[] memory;
    private int insnPtr;

    public IntComputer(int[] memory) {
        this(() -> {
            System.out.print("Input: ");
            return new Scanner(System.in).nextInt();
        }, System.out::println, memory);
    }

    public IntComputer(IntSupplier input, IntConsumer output, int[] memory) {
        this.input = input;
        this.output = output;
        this.memory = Arrays.copyOf(memory, memory.length);
    }

    private static int powBase10(int e) {
        if (e < 0) {
            throw new IllegalArgumentException();
        } else if (e == 0) {
            return 1;
        } else if (e == 1) {
            return 10;
        }

        int n = 10;
        for (int i = 1; i < e; i++) {
            n *= 10;
        }

        return n;
    }

    public int run() {
        while (true) {
            int insn = memory[insnPtr];
            int opcode = insn % 100;

            if (opcode == 1) { // add
                ParameterReadAccessor in1 = getParameterRead(insn, 1);
                ParameterReadAccessor in2 = getParameterRead(insn, 2);
                ParameterWriteAccessor out = getParameterWrite(insn, 3);

                out.write(in1.read() + in2.read());

                insnPtr += 4;
            } else if (opcode == 2) { // mul
                ParameterReadAccessor in1 = getParameterRead(insn, 1);
                ParameterReadAccessor in2 = getParameterRead(insn, 2);
                ParameterWriteAccessor out = getParameterWrite(insn, 3);

                out.write(in1.read() * in2.read());

                insnPtr += 4;
            } else if (opcode == 3) { // store
                ParameterWriteAccessor out = getParameterWrite(insn, 1);

                out.write(input.getAsInt());

                insnPtr += 2;
            } else if (opcode == 4) { // print
                ParameterReadAccessor in = getParameterRead(insn, 1);

                output.accept(in.read());

                insnPtr += 2;
            } else if (opcode == 5) { // jump-if-true
                ParameterReadAccessor in1 = getParameterRead(insn, 1);
                ParameterReadAccessor in2 = getParameterRead(insn, 2);

                if (in1.read() != 0) {
                    insnPtr = in2.read();
                } else {
                    insnPtr += 3;
                }
            } else if (opcode == 6) { // jump-if-false
                ParameterReadAccessor in1 = getParameterRead(insn, 1);
                ParameterReadAccessor in2 = getParameterRead(insn, 2);

                if (in1.read() == 0) {
                    insnPtr = in2.read();
                } else {
                    insnPtr += 3;
                }
            } else if (opcode == 7) { // less-than
                ParameterReadAccessor in1 = getParameterRead(insn, 1);
                ParameterReadAccessor in2 = getParameterRead(insn, 2);
                ParameterWriteAccessor out = getParameterWrite(insn, 3);

                out.write(in1.read() < in2.read() ? 1 : 0);

                insnPtr += 4;
            } else if (opcode == 8) { // equals
                ParameterReadAccessor in1 = getParameterRead(insn, 1);
                ParameterReadAccessor in2 = getParameterRead(insn, 2);
                ParameterWriteAccessor out = getParameterWrite(insn, 3);

                out.write(in1.read() == in2.read() ? 1 : 0);

                insnPtr += 4;
            } else if (opcode == 99) {
                insnPtr++;
                break;
            } else {
                throw new IllegalStateException();
            }
        }

        return memory[0];
    }

    private ParameterReadAccessor getParameterRead(int insn, int parameter) {
        int parameterMode = (insn / powBase10(parameter + 1)) % 10;
        if (parameterMode == 0) {
            return () -> memory[memory[insnPtr + parameter]];
        } else if (parameterMode == 1) {
            return () -> memory[insnPtr + parameter];
        }

        throw new IllegalArgumentException();
    }

    private ParameterWriteAccessor getParameterWrite(int insn, int parameter) {
        int parameterMode = (insn / powBase10(parameter + 1)) % 10;
        if (parameterMode == 0) {
            return n -> memory[memory[insnPtr + parameter]] = n;
        }

        throw new IllegalArgumentException();
    }

    @FunctionalInterface
    interface ParameterReadAccessor {
        int read();
    }

    @FunctionalInterface
    interface ParameterWriteAccessor {
        void write(int n);
    }
}
