package io.github.ititus.aoc.aoc20.day08;

public class MachineState {

    private int instructionPointer;
    private int accumulator;
    private boolean exit;

    public MachineState() {
    }

    public MachineState(MachineState state) {
        this.instructionPointer = state.instructionPointer;
        this.accumulator = state.accumulator;
        this.exit = state.exit;
    }

    public int getInstructionPointer() {
        return instructionPointer;
    }

    public void setInstructionPointer(int instructionPointer) {
        this.instructionPointer = instructionPointer;
    }

    public void incInstructionPointer() {
        incInstructionPointer(1);
    }

    public void incInstructionPointer(int by) {
        this.instructionPointer += by;
    }

    public int getAccumulator() {
        return accumulator;
    }

    public void setAccumulator(int accumulator) {
        this.accumulator = accumulator;
    }

    public void incAccumulator(int by) {
        this.accumulator += by;
    }

    public void exit() {
        this.exit = true;
    }

    public boolean isExit() {
        return exit;
    }
}
