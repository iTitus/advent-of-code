package io.github.ititus.aoc.aoc19.day15;

import io.github.ititus.aoc.aoc19.Direction;
import io.github.ititus.aoc.aoc19.IntComputer;
import io.github.ititus.math.graph.Graph;
import io.github.ititus.math.graph.Vertex;
import io.github.ititus.math.graph.algorithm.Dijkstra;
import io.github.ititus.math.vector.Vec2i;

import java.math.BigInteger;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

public class RepairDroid {

    private static final Direction[] DIRECTIONS = { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST };

    private final BlockingQueue<Direction> input;
    private final BlockingQueue<MovementStatus> output;
    private final IntComputer computer;

    private final Vec2i startingPos;
    private final Graph<Vec2i> map;

    private Vec2i currentPos;
    private Vec2i oxygenPos;

    public RepairDroid(BigInteger[] memory) {
        this.input = new ArrayBlockingQueue<>(1, true);
        this.output = new ArrayBlockingQueue<>(1, true);

        this.computer = new IntComputer(() -> {
            try {
                return input.take().getIndex();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, i -> {
            try {
                output.put(MovementStatus.get(i));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, memory);

        this.currentPos = this.startingPos = new Vec2i();
        this.map = new Graph<>();
    }

    public Dijkstra<Vec2i>.Result getOxygenPaths() {
        return new Dijkstra<>(map, map.getVertex(oxygenPos).orElseThrow()).findShortestPaths();
    }

    public void render() {
        Set<Vec2i> passable = map.getVertices().stream().map(Vertex::get).collect(Collectors.toSet());

        int minX = passable.stream().mapToInt(Vec2i::getX).min().orElseThrow();
        int minY = passable.stream().mapToInt(Vec2i::getY).min().orElseThrow();
        int maxX = passable.stream().mapToInt(Vec2i::getX).max().orElseThrow();
        int maxY = passable.stream().mapToInt(Vec2i::getY).max().orElseThrow();

        for (int y = minY - 1; y <= maxY + 1; y++) {
            for (int x = minX - 1; x <= maxX + 1; x++) {
                Vec2i v = new Vec2i(x, y);
                if (v.equals(currentPos)) {
                    System.out.print('D');
                } else if (v.equals(oxygenPos)) {
                    System.out.print('O');
                } else if (passable.contains(v)) {
                    System.out.print('.');
                } else if (passable.stream().anyMatch(v::isDirectlyAdjacentTo)) {
                    System.out.print('\u2588');
                } else {
                    System.out.print(' ');
                }
            }
            System.out.println();
        }
    }

    public void buildMap() {
        Thread t = new Thread(this.computer::run, "IntComputer");
        t.setDaemon(true);
        t.start();

        map.addVertex(startingPos);
        exploreAllDirs();
    }

    private void exploreAllDirs() {
        Set<Direction> valid = EnumSet.noneOf(Direction.class);

        Vec2i center = currentPos;
        for (Direction dir : DIRECTIONS) {
            if (map.getVertex(currentPos.add(dir.getDirectionVector())).isPresent()) {
                continue;
            }

            MovementStatus status = move(dir);
            if (status != MovementStatus.BLOCKED) {
                valid.add(dir);

                map.addVertex(currentPos);
                map.addEdge(center, currentPos);

                if (status == MovementStatus.OXYGEN) {
                    if (oxygenPos != null && !oxygenPos.equals(currentPos)) {
                        throw new RuntimeException();
                    }
                    oxygenPos = currentPos;
                }

                move(dir.getOpposite());
            }
        }

        for (Direction dir : valid) {
            move(dir);
            exploreAllDirs();
            move(dir.getOpposite());
        }
    }

    private MovementStatus move(Direction dir) {
        try {
            input.put(dir);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        MovementStatus status;
        try {
            status = output.take();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        if (status != MovementStatus.BLOCKED) {
            currentPos = currentPos.add(dir.getDirectionVector());
        }
        return status;
    }

    public Graph<Vec2i> getMap() {
        return map;
    }

    public Vec2i getStartingPos() {
        return startingPos;
    }
}
