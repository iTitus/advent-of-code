package io.github.ititus.aoc.aoc19.day08;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;

import java.io.PrintWriter;
import java.io.StringWriter;

@Aoc(year = 2019, day = 8)
public final class Day08 implements AocSolution {

    private SpaceEncodedImage image;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        String encodedImage = input.readString().strip();
        image = new SpaceEncodedImage(25, 6);
        image.decode(encodedImage);
    }

    @Override
    public Object part1() {
        return image.getChecksum();
    }

    @Override
    public Object part2() {
        StringWriter w = new StringWriter();
        image.render(new PrintWriter(w));
        return w.toString();
    }
}
