package io.github.ititus.aoc.aoc19.day17;

import io.github.ititus.aoc.aoc19.IntComputer;
import io.github.ititus.aoc.common.Direction;
import io.github.ititus.math.vector.Vec2i;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AsciiRobot {

    private final BlockingQueue<Integer> input;
    private final BlockingDeque<Integer> output;
    private final IntComputer c;

    private int sizeY = -1;
    private int sizeX = -1;
    private ExteriorType[][] map;

    public AsciiRobot(BigInteger[] memory) {
        memory = Arrays.copyOf(memory, memory.length);
        this.input = new LinkedBlockingQueue<>();
        this.output = new LinkedBlockingDeque<>();
        memory[0] = BigInteger.TWO;

        this.c = new IntComputer(
                () -> {
                    System.out.println("input");
                    try {
                        return input.take();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                },
                i -> {
                    char c = (char) i;
                    System.out.println("output: " + (c == '\n' ? "\\n" : c) + " [" + i + "]");
                    try {
                        output.put(i);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                },
                memory
        );
    }

    private void generateInput() {
        analyzeMap();

        // main
        appendCommand("A,B,A,C,A,B,A,C,B,C");

        // A
        appendCommand("R,4,L,12,L,8,R,4");

        // B
        appendCommand("L,8,R,10,R,10,R,6");

        // C
        appendCommand("R,4,R,10,L,12");

        // continuous feed?
        appendCommand("n");
    }

    private void analyzeMap() {
        Vec2i robotStartPos = null;
        Direction robotStartDir = null;

        outer:
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                ExteriorType t = map[x][y];
                if (t.isRobot()) {
                    robotStartPos = new Vec2i(x, y);
                    robotStartDir = t.getDirection();
                    break outer;
                }
            }
        }

        if (robotStartDir == null) {
            throw new RuntimeException();
        }

        List<String> commands = new ArrayList<>();

        Vec2i pos = robotStartPos;
        Direction rd = robotStartDir;

        int move = 0;
        int rotate = 0;

        while (true) {
            Vec2i checkPos = pos.add(rd.getDirectionVector());
            ExteriorType check = get(checkPos);
            if (check == ExteriorType.SCAFFOLDING) {
                if (rotate > 0) {
                    if (rotate == 1) {
                        commands.add("R");
                    } else {
                        commands.add("L");
                    }
                    rotate = 0;
                }
                pos = checkPos;
                move++;
            } else {
                if (move > 0) {
                    commands.add(String.valueOf(move));
                    move = 0;
                }

                if (rotate == 0) {
                    rd = rd.rotateCW();
                    rotate = 1;
                } else if (rotate == 1) {
                    rd = rd.rotateCW().rotateCW();
                    rotate = 3;
                } else {
                    break;
                }
            }
        }

        System.out.println(String.join(",", commands));
    }

    private ExteriorType get(Vec2i pos) {
        int y = pos.y();
        int x = pos.x();
        if (y >= 0 && x >= 0 && y < sizeY && x < sizeX) {
            return map[x][y];
        }
        return ExteriorType.OPEN_SPACE;
    }

    private void appendCommand(String command) {
        if (command.length() > 20) {
            throw new IllegalArgumentException();
        }
        IntStream.concat(command.chars(), IntStream.of('\n')).forEach(c -> {
            try {
                input.put(c);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void run() {
        Thread t = new Thread(c::run, "IntComputer");
        t.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String out = output.stream()
                .map(i -> String.valueOf((char) i.intValue()))
                .collect(Collectors.joining());
        String[] lines = Arrays.stream(out.split("\n"))
                .takeWhile(s -> !s.isEmpty())
                .toArray(String[]::new);

        for (String l : lines) {
            System.out.println(l);
        }

        sizeY = lines.length;
        sizeX = lines[0].length();

        map = new ExteriorType[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            String line = lines[y];
            for (int x = 0; x < sizeX; x++) {
                map[x][y] = ExteriorType.get(line.charAt(x));
            }
        }

        generateInput();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public int getDustCleaned() {
        try {
            return output.takeLast();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public int sumAlignmentParameters() {
        int alignmentParameters = 0;

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                if (map[x][y] == ExteriorType.SCAFFOLDING) {
                    int neighbors = 0;
                    for (Direction d : Direction.values()) {
                        Vec2i v = d.getDirectionVector();
                        int dy = y + v.y();
                        int dx = x + v.x();

                        if (dy >= 0 && dx >= 0 && dy < sizeY && dx < sizeX && map[dx][dy] == ExteriorType.SCAFFOLDING) {
                            neighbors++;
                        }
                    }

                    if (neighbors > 2) {
                        alignmentParameters += y * x;
                    }
                }
            }
        }

        return alignmentParameters;
    }
}
