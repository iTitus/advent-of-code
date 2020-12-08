package io.github.ititus.aoc.aoc20.day08;

public class Instruction {

    protected final int argument;
    private final String operation;
    private final InstructionExecutor executor;
    private Instruction(String operation, int argument, InstructionExecutor executor) {
        this.operation = operation;
        this.argument = argument;
        this.executor = executor;
    }

    public static Instruction parse(String line) {
        String[] split = line.split(" ");
        String operation = split[0];

        InstructionExecutor executor = switch (operation) {
            case "acc" -> (arg, state) -> {
                state.incAccumulator(arg);
                state.incInstructionPointer();
            };
            case "jmp" -> (arg, state) -> state.incInstructionPointer(arg);
            case "nop" -> (arg, state) -> state.incInstructionPointer();
            default -> throw new RuntimeException("unknown operation in instruction");
        };

        return new Instruction(operation, Integer.parseInt(split[1]), executor);
    }

    public String getOperation() {
        return operation;
    }

    public int getArgument() {
        return argument;
    }

    public void execute(MachineState state) {
        executor.execute(argument, state);
    }

    @FunctionalInterface
    private interface InstructionExecutor {
        void execute(int arg, MachineState state);
    }
}
