package io.github.ititus.aoc.aoc19;

import io.github.ititus.commons.math.number.BigIntegerMath;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IntComputer {

    private static final BigInteger ONE_HUNDRED = BigIntegerMath.of(100);

    private final Supplier<BigInteger> input;
    private final Consumer<BigInteger> output;
    private final List<BigInteger> memory;
    private final AtomicBoolean waitingForInput, waitingForOutput;
    private int insnPtr, relativeBase;

    public IntComputer(int[] memory) {
        this(Arrays.stream(memory).mapToObj(BigIntegerMath::of).toArray(BigInteger[]::new));
    }

    public IntComputer(BigInteger[] memory) {
        this(
                () -> {
                    throw new UnsupportedOperationException();
                },
                (BigInteger n) -> {
                    throw new UnsupportedOperationException();
                },
                memory
        );
    }

    public IntComputer(IntSupplier input, IntConsumer output, int[] memory) {
        this(input, output, Arrays.stream(memory).mapToObj(BigIntegerMath::of).toArray(BigInteger[]::new));
    }

    public IntComputer(IntSupplier input, IntConsumer output, BigInteger[] memory) {
        this(() -> BigIntegerMath.of(input.getAsInt()), n -> output.accept(n.intValueExact()), memory);
    }

    public IntComputer(Supplier<BigInteger> input, Consumer<BigInteger> output, int[] memory) {
        this(input, output, Arrays.stream(memory).mapToObj(BigIntegerMath::of).toArray(BigInteger[]::new));
    }

    public IntComputer(Supplier<BigInteger> input, Consumer<BigInteger> output, BigInteger[] memory) {
        if (Arrays.stream(memory).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException();
        }

        this.input = input;
        this.output = output;
        this.memory = new ArrayList<>(Arrays.asList(memory));
        this.waitingForInput = new AtomicBoolean(false);
        this.waitingForOutput = new AtomicBoolean(false);
    }

    public static int runGetFirst(int[] memory) {
        return new IntComputer(memory).run().intValueExact();
    }

    public static BigInteger runGetFirst(BigInteger[] memory) {
        return new IntComputer(memory).run();
    }

    public static int runGetOutput(int input, int[] memory) {
        int[] outputs = new int[1];
        of(input, outputs, memory).run();
        return outputs[0];
    }

    public static BigInteger runGetOutput(BigInteger input, BigInteger[] memory) {
        BigInteger[] outputs = new BigInteger[1];
        of(input, outputs, memory).run();
        return outputs[0];
    }

    public static BigInteger runGetLastOutput(BigInteger input, BigInteger[] memory) {
        List<BigInteger> outputs = new ArrayList<>();
        of(input, outputs, memory).run();
        if (outputs.isEmpty()) {
            throw new RuntimeException();
        }
        return outputs.get(outputs.size() - 1);
    }

    public static IntComputer of(int input, int[] outputs, int[] memory) {
        boolean[] inputUsed = { false };
        int[] outputsUsed = { 0 };
        return new IntComputer(
                () -> {
                    if (inputUsed[0]) {
                        throw new IllegalStateException();
                    }
                    inputUsed[0] = true;
                    return input;
                },
                n -> {
                    if (outputsUsed[0] >= outputs.length) {
                        throw new IllegalStateException();
                    }
                    outputs[outputsUsed[0]++] = n;
                },
                memory
        );
    }

    public static IntComputer of(BigInteger input, BigInteger[] outputs, BigInteger[] memory) {
        boolean[] inputUsed = { false };
        int[] outputsUsed = { 0 };
        return new IntComputer(
                () -> {
                    if (inputUsed[0]) {
                        throw new IllegalStateException();
                    }
                    inputUsed[0] = true;
                    return input;
                },
                n -> {
                    if (outputsUsed[0] >= outputs.length) {
                        throw new IllegalStateException();
                    }
                    outputs[outputsUsed[0]++] = n;
                },
                memory
        );
    }

    public static IntComputer of(BigInteger input, List<BigInteger> outputs, BigInteger[] memory) {
        boolean[] inputUsed = { false };
        return new IntComputer(
                () -> {
                    if (inputUsed[0]) {
                        throw new IllegalStateException();
                    }
                    inputUsed[0] = true;
                    return input;
                },
                outputs::add,
                memory
        );
    }

    public BigInteger run() {
        runLoop:
        while (true) {
            BigInteger insn = memory.get(insnPtr);
            BigInteger opcode = insn.mod(ONE_HUNDRED);

            switch (opcode.intValueExact()) {
                case 1 -> { // add
                    ParameterReadAccessor in1 = getParameterRead(insn, 1);
                    ParameterReadAccessor in2 = getParameterRead(insn, 2);
                    ParameterWriteAccessor out = getParameterWrite(insn, 3);

                    out.write(in1.read().add(in2.read()));

                    insnPtr += 4;
                }
                case 2 -> { // mul
                    ParameterReadAccessor in1 = getParameterRead(insn, 1);
                    ParameterReadAccessor in2 = getParameterRead(insn, 2);
                    ParameterWriteAccessor out = getParameterWrite(insn, 3);

                    out.write(in1.read().multiply(in2.read()));

                    insnPtr += 4;
                }
                case 3 -> { // store
                    ParameterWriteAccessor out = getParameterWrite(insn, 1);

                    waitingForInput.set(true);
                    out.write(Objects.requireNonNull(input.get()));
                    waitingForInput.set(false);

                    insnPtr += 2;
                }
                // print
                case 4 -> {
                    ParameterReadAccessor in = getParameterRead(insn, 1);
                    waitingForOutput.set(true);
                    output.accept(in.read());
                    waitingForOutput.set(false);
                    insnPtr += 2;
                }
                case 5 -> { // jump-if-true
                    ParameterReadAccessor in1 = getParameterRead(insn, 1);
                    ParameterReadAccessor in2 = getParameterRead(insn, 2);

                    if (in1.read().signum() != 0) {
                        insnPtr = in2.read().intValueExact();
                    } else {
                        insnPtr += 3;
                    }
                }
                case 6 -> { // jump-if-false
                    ParameterReadAccessor in1 = getParameterRead(insn, 1);
                    ParameterReadAccessor in2 = getParameterRead(insn, 2);

                    if (in1.read().signum() == 0) {
                        insnPtr = in2.read().intValueExact();
                    } else {
                        insnPtr += 3;
                    }
                }
                case 7 -> { // less-than
                    ParameterReadAccessor in1 = getParameterRead(insn, 1);
                    ParameterReadAccessor in2 = getParameterRead(insn, 2);
                    ParameterWriteAccessor out = getParameterWrite(insn, 3);

                    out.write(in1.read().compareTo(in2.read()) < 0 ? BigInteger.ONE : BigInteger.ZERO);

                    insnPtr += 4;
                }
                case 8 -> { // equals
                    ParameterReadAccessor in1 = getParameterRead(insn, 1);
                    ParameterReadAccessor in2 = getParameterRead(insn, 2);
                    ParameterWriteAccessor out = getParameterWrite(insn, 3);

                    out.write(in1.read().compareTo(in2.read()) == 0 ? BigInteger.ONE : BigInteger.ZERO);

                    insnPtr += 4;
                }
                case 9 -> { // change relative base
                    ParameterReadAccessor in1 = getParameterRead(insn, 1);

                    relativeBase += in1.read().intValueExact();

                    insnPtr += 2;
                }
                case 99 -> {
                    insnPtr++;
                    break runLoop;
                }
                default -> throw new IllegalStateException();
            }
        }

        return memory.get(0);
    }

    private int ensureCapacity(int index) {
        if (index < 0) {
            throw new IllegalArgumentException();
        }

        int oldSize = memory.size();
        if (index >= oldSize) {
            List<BigInteger> newMemory =
                    IntStream.rangeClosed(oldSize, index).mapToObj(i -> BigInteger.ZERO).collect(Collectors.toList());
            memory.addAll(newMemory);
        }
        return index;
    }

    private ParameterReadAccessor getParameterRead(BigInteger insn, int parameter) {
        int parameterMode = insn.divide(BigInteger.TEN.pow(parameter + 1)).mod(BigInteger.TEN).intValueExact();
        if (parameterMode == 0) { // position
            return () -> memory.get(ensureCapacity(memory.get(insnPtr + parameter).intValueExact()));
        } else if (parameterMode == 1) { // immediate
            return () -> memory.get(insnPtr + parameter);
        } else if (parameterMode == 2) { // relative
            return () -> memory.get(ensureCapacity(relativeBase + memory.get(insnPtr + parameter).intValueExact()));
        }

        throw new IllegalArgumentException();
    }

    private ParameterWriteAccessor getParameterWrite(BigInteger insn, int parameter) {
        int parameterMode = insn.divide(BigInteger.TEN.pow(parameter + 1)).mod(BigInteger.TEN).intValueExact();
        if (parameterMode == 0) { // position
            return n -> memory.set(ensureCapacity(memory.get(insnPtr + parameter).intValueExact()), n);
        } else if (parameterMode == 2) { // relative
            return n -> memory.set(ensureCapacity(relativeBase + memory.get(insnPtr + parameter).intValueExact()), n);
        }

        throw new IllegalArgumentException();
    }

    public boolean isWaitingForInput() {
        return waitingForInput.get();
    }

    public boolean isWaitingForOutput() {
        return waitingForOutput.get();
    }

    @FunctionalInterface
    interface ParameterReadAccessor {
        BigInteger read();
    }

    @FunctionalInterface
    interface ParameterWriteAccessor {
        void write(BigInteger n);
    }
}
