package io.github.ititus.aoc.aoc19.day21;

import io.github.ititus.aoc.aoc19.IntComputer;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class SpringDroid {

    private final BigInteger[] memory;

    public SpringDroid(BigInteger[] memory) {
        this.memory = Arrays.copyOf(memory, memory.length);
    }

    public int runSpringScript(String... script) {
        BlockingQueue<Integer> input = new LinkedBlockingQueue<>();
        BlockingDeque<Integer> output = new LinkedBlockingDeque<>();
        for (String cmd : script) {
            insertScriptCommand(input, cmd);
        }

        IntComputer comp = new IntComputer(() -> {
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

        comp.run();

        Integer lastInteger = output.peekLast();
        if (lastInteger != null && lastInteger > 0xFF) {
            return lastInteger;
        }

        for (Integer i : output) {
            char c = (char) i.intValue();
            System.out.print(c);
        }

        return -1;
    }

    private void insertScriptCommand(BlockingQueue<Integer> input, String cmd) {
        try {
            for (char c : cmd.toCharArray()) {
                input.put((int) c);
            }
            input.put((int) '\n');
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
