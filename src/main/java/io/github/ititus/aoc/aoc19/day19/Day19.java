package io.github.ititus.aoc.aoc19.day19;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.math.vector.Vec2i;

@Aoc(year = 2019, day = 19)
public final class Day19 implements AocSolution {

    private TractorBeamScanner scanner;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        scanner = new TractorBeamScanner(input.readAsIntCodeMemory());
    }

    @Override
    public Object part1() {
        return scanner.getNumberOfAffectedTiles(50);
    }

    @Override
    public Object part2() {
        Vec2i pos = scanner.getClosestPosForShip(100);
        return pos.getX() * 10_000 + pos.getY();
    }
}
