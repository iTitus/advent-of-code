package io.github.ititus.aoc.aoc19.day07;

import io.github.ititus.aoc.aoc19.IntComputer;
import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.math.permutation.Permutations;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Aoc(year = 2019, day = 7)
public final class Day07 implements AocSolution {

    private BigInteger[] memory;

    private static int run(BigInteger[] memory, List<Integer> phases) {
        List<int[]> permutations = Permutations.permute(phases);
        List<Integer> outputs = new ArrayList<>();
        for (int[] p : permutations) {
            BlockingQueue<Integer> qa = new ArrayBlockingQueue<>(1, true);
            BlockingQueue<Integer> qb = new ArrayBlockingQueue<>(1, true);
            BlockingQueue<Integer> qc = new ArrayBlockingQueue<>(1, true);
            BlockingQueue<Integer> qd = new ArrayBlockingQueue<>(1, true);
            BlockingQueue<Integer> qe = new ArrayBlockingQueue<>(1, true);

            Thread ta = runInNewThread(qa, qb, memory, "A");
            Thread tb = runInNewThread(qb, qc, memory, "B");
            Thread tc = runInNewThread(qc, qd, memory, "C");
            Thread td = runInNewThread(qd, qe, memory, "D");
            Thread te = runInNewThread(qe, qa, memory, "E");

            try {
                qa.put(p[0]);
                qb.put(p[1]);
                qc.put(p[2]);
                qd.put(p[3]);
                qe.put(p[4]);

                qa.put(0);

                ta.join();
                tb.join();
                tc.join();
                td.join();
                te.join();
                outputs.add(qa.take());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return Collections.max(outputs);
    }

    private static Thread runInNewThread(BlockingQueue<Integer> in, BlockingQueue<Integer> out, BigInteger[] memory,
                                         String name) {
        IntComputer c = new IntComputer(() -> {
            try {
                return in.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, i -> {
            try {
                out.put(i);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, memory);
        Thread t = new Thread(c::run, "IntComputer Thread: " + name);
        t.start();
        return t;
    }

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        memory = input.readAsIntCodeMemory();
    }

    @Override
    public Object part1() {
        return run(memory, List.of(0, 1, 2, 3, 4));
    }

    @Override
    public Object part2() {
        return run(memory, List.of(5, 6, 7, 8, 9));
    }
}
