package io.github.ititus.aoc.aoc19.day12;

import io.github.ititus.aoc.common.InputProvider;
import io.github.ititus.math.vector.Vec3i;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12 {

    private static final Pattern INPUT_FORMAT = Pattern.compile("\\s*<\\s*x\\s*=\\s*(-?[0-9]+)\\s*,\\s*y\\s*=\\s*" +
            "(-?[0-9]+)\\s*,\\s*z\\s*=\\s*(-?[0-9]+)\\s*>\\s*");

    public static void main(String[] args) {
        List<Moon> moons;
        try (Stream<String> stream = InputProvider.lines(2019, 12)) {
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

        // 1
        System.out.println("### 1 ###");
        MoonSimulation sim1 = new MoonSimulation(moons);
        sim1.simulate(1000);
        System.out.println(sim1.getTotalEnergy());

        // 2
        System.out.println("### 2 ###");
        MoonSimulation sim2 = new MoonSimulation(moons);
        System.out.println(sim2.simulateUntilRepeat());
    }
}
