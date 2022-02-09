package io.github.ititus.aoc.aoc19.day12;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.commons.math.vector.Vec3i;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Aoc(year = 2019, day = 12)
public final class Day12 implements AocSolution {

    private static final Pattern INPUT_FORMAT = Pattern.compile(
            "\\s*<\\s*x\\s*=\\s*(-?[0-9]+)\\s*,\\s*y\\s*=\\s*(-?[0-9]+)\\s*,\\s*z\\s*=\\s*(-?[0-9]+)\\s*>\\s*"
    );

    private List<Moon> moons;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        try (Stream<String> stream = input.lines()) {
            moons = stream
                    .map(INPUT_FORMAT::matcher)
                    .peek(m -> {
                        if (!m.matches()) {
                            throw new RuntimeException();
                        }
                    })
                    .map(m -> new Vec3i(
                                    Integer.parseInt(m.group(1)),
                                    Integer.parseInt(m.group(2)),
                                    Integer.parseInt(m.group(3))
                            )
                    )
                    .map(Moon::new)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Object part1() {
        MoonSimulation sim = new MoonSimulation(moons);
        sim.simulate(1000);
        return sim.getTotalEnergy();
    }

    @Override
    public Object part2() {
        MoonSimulation sim = new MoonSimulation(moons);
        return sim.simulateUntilRepeat();
    }
}
