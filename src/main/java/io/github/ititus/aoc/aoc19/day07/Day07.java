package io.github.ititus.aoc.aoc19.day07;

import io.github.ititus.aoc.InputProvider;
import io.github.ititus.aoc.aoc19.IntComputer;
import io.github.ititus.math.permutation.Permutations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Day07 {

    public static void main(String[] args) {
        String input = InputProvider.readString(2019, 7);
        int[] memory = Arrays.stream(input.split(",")).map(String::strip).mapToInt(Integer::parseInt).toArray();

        // 1
        System.out.println("### 1 ###");
        run(memory, List.of(0, 1, 2, 3, 4));

        // 2
        System.out.println("### 2 ###");
        run(memory, List.of(5, 6, 7, 8, 9));
    }

    private static void run(int[] memory, List<Integer> phases) {
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
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }

        System.out.println(Collections.max(outputs));
    }

    private static Thread runInNewThread(BlockingQueue<Integer> in, BlockingQueue<Integer> out, int[] memory, String name) {
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
}
