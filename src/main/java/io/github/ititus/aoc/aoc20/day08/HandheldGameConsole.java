package io.github.ititus.aoc.aoc20.day08;

import java.util.List;

public class HandheldGameConsole {

    private final Instruction[] instructions;
    private final MachineState state;

    private HandheldGameConsole(Instruction... instructions) {
        this.instructions = instructions;
        this.state = new MachineState();
    }

    public static HandheldGameConsole of(List<String> lines) {
        return new HandheldGameConsole(
                lines.stream()
                        .map(Instruction::parse)
                        .toArray(Instruction[]::new)
        );
    }

    public void step() {
        int instructionPointer = state.getInstructionPointer();
        if (instructionPointer >= instructions.length) {
            state.exit();
            return;
        }

        instructions[instructionPointer].execute(state);
    }

    public int getInstructionPointer() {
        return state.getInstructionPointer();
    }

    public int getAccumulator() {
        return state.getAccumulator();
    }

    public boolean isExit() {
        return state.isExit();
    }
}
