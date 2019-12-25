package io.github.ititus.aoc.aoc19.day25;

import io.github.ititus.aoc.aoc19.IntComputer;
import io.github.ititus.math.permutation.Permutations;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

public class Droid {

    private final BlockingQueue<Integer> input;
    private final StringBuilder currentLine;
    private final List<String> printedLines;
    private final IntComputer c;
    private final Thread t;

    public Droid(BigInteger[] memory) {
        this.input = new LinkedBlockingQueue<>();
        this.currentLine = new StringBuilder();
        this.printedLines = Collections.synchronizedList(new ArrayList<>());
        this.c = new IntComputer(() -> {
            try {
                return input.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, i -> {
            char c = (char) i;
            if (c == '\n') {
                if (currentLine.length() > 0) {
                    printedLines.add(currentLine.toString());
                    currentLine.setLength(0);
                }
            } else {
                currentLine.append(c);
            }
            System.out.print(c);
        }, memory);
        this.t = new Thread(c::run, "IntComputer");
    }

    public void run() {
        t.setDaemon(true);
        t.start();

        Scanner sc = new Scanner(System.in);
        while (true) {
            if (sc.hasNextLine()) {
                handleCommand(sc.nextLine());
            } else {
                System.out.println("Stopping...");
                break;
            }
        }
    }

    private void handleCommand(String command) {
        if (command.equals("collect")) {
            sendCommand("south");
            sendCommand("take monolith"); // engineering

            sendCommand("east");
            sendCommand("take asterisk"); // kitchen

            sendCommand("west");
            sendCommand("north");
            sendCommand("west");
            sendCommand("take coin"); // arcade

            sendCommand("north");
            sendCommand("east");
            sendCommand("take astronaut ice cream"); // sick bay

            sendCommand("west");
            sendCommand("south");
            sendCommand("east");
            sendCommand("north");
            sendCommand("north");
            sendCommand("take mutex"); // corridor

            sendCommand("west");
            sendCommand("take astrolabe"); // stables

            sendCommand("west");
            sendCommand("take dehydrated water"); // hot chocolate fountain

            sendCommand("west");
            sendCommand("take wreath"); // crew quarters

            sendCommand("east");
            sendCommand("south");
            sendCommand("east");
            sendCommand("north");
            sendCommand("north"); // pressure-sensitive floor

            sendCommand("inv"); // security checkpoint
        } else if (command.equals("try")) {
            Set<String> items = Set.of("astronaut ice cream", "wreath", "coin", "dehydrated water", "asterisk", "monolith", "astrolabe", "mutex");
            Set<Set<String>> permutations = Permutations.permuteWithoutDuplicates(items);

            outer:
            for (Set<String> permutation : permutations) {
                items.stream().map(i -> "drop " + i).forEach(this::sendCommand);
                permutation.stream().map(i -> "take " + i).forEach(this::sendCommand);
                sendCommand("inv");

                sendCommand("north");

                while (!input.isEmpty() || !c.isWaitingForInput()) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                for (int i = printedLines.size() - 1; i >= 0; i--) {
                    String line = printedLines.get(i);
                    if (line.startsWith("==")) {
                        if (!line.contains("Security Checkpoint")) {
                            break outer;
                        } else {
                            break;
                        }
                    }
                }
            }
        } else {
            sendCommand(command);
        }
    }

    private void sendCommand(String command) {
        IntStream.concat(command.chars(), IntStream.of('\n')).forEachOrdered(i -> {
            try {
                input.put(i);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
