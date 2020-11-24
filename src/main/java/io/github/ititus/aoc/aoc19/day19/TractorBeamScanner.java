package io.github.ititus.aoc.aoc19.day19;

import io.github.ititus.aoc.aoc19.IntComputer;
import io.github.ititus.math.vector.Vec2i;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.IntStream;

public class TractorBeamScanner {

    private final ExecutorService executorService;
    private final BigInteger[] memory;

    public TractorBeamScanner(BigInteger[] memory) {
        this.executorService = Executors.newWorkStealingPool();
        this.memory = Arrays.copyOf(memory, memory.length);
    }

    public int getNumberOfAffectedTiles(int size) {
        return Math.toIntExact(
                IntStream.range(0, size)
                        .parallel()
                        .mapToObj(y -> IntStream.range(0, size).mapToObj(x -> new Vec2i(x, y)))
                        .flatMap(Function.identity())
                        .filter(this::isTractorBeamAffected)
                        .count()
        );
    }

    public boolean isTractorBeamAffected(Vec2i pos) {
        BlockingQueue<Integer> input = new ArrayBlockingQueue<>(2);
        input.offer(pos.getX());
        input.offer(pos.getY());

        BlockingQueue<Integer> output = new ArrayBlockingQueue<>(1);

        try {
            executorService.submit(() -> {
                IntComputer c = new IntComputer(() -> {
                    try {
                        return input.take();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }, i -> {
                    try {
                        output.put(i);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }, memory);

                c.run();
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        int out;
        try {
            out = output.take();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        return out != 0;
    }

    public Vec2i getClosestPosForShip(int size) {
        Vec2i out;

        Set<Vec2i> fringe = new HashSet<>();

        int widthStart = 0;
        int widthEnd = 10 * size;
        outer:
        for (int y = 0; ; y++) {
            boolean first = true;
            for (int x = widthStart; ; x++) {
                Vec2i pos = new Vec2i(x, y);
                boolean b = isTractorBeamAffected(pos);
                if (b) {
                    if (first) {
                        fringe.add(pos);
                        Vec2i otherPos = new Vec2i(x + (size - 1), y - (size - 1));
                        if (fringe.contains(otherPos)) {
                            out = new Vec2i(x, y - (size - 1));
                            break outer;
                        }
                        widthStart = x;
                        first = false;
                    }
                } else {
                    if (!first) {
                        fringe.add(new Vec2i(x - 1, y));
                        widthEnd = x - 1;
                        break;
                    } else if (x > widthEnd + 2) {
                        break;
                    }
                }
            }
        }

        Vec2i edgeOffset1 = new Vec2i(size - 1, 0);
        Vec2i edgeOffset2 = new Vec2i(0, size - 1);

        // this may not be necessary
        int lookbehind = 5;
        for (int dy = -lookbehind; dy <= 0; dy++) {
            for (int dx = -lookbehind; dx <= 0; dx++) {
                Vec2i altPos = out.add(new Vec2i(dx, dy));
                if (
                        isTractorBeamAffected(altPos)
                                && isTractorBeamAffected(altPos.add(edgeOffset1))
                                && isTractorBeamAffected(altPos.add(edgeOffset2))
                ) {
                    return altPos;
                }
            }
        }

        return out;
    }
}
