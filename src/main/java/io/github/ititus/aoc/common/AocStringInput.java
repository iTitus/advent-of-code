package io.github.ititus.aoc.common;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public final class AocStringInput implements AocInput {

    private final String input;

    public AocStringInput(String input) {
        this.input = input;
    }

    @Override
    public String readString() {
        return input;
    }

    @Override
    public Stream<String> lines() {
        return Arrays.stream(input.split("\n"));
    }

    @Override
    public List<String> readAllLines() {
        return List.of(input.split("\n"));
    }
}
