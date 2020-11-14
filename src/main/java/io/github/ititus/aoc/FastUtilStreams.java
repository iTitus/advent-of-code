package io.github.ititus.aoc;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.LongList;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

public final class FastUtilStreams {

    public static IntStream stream(IntList intList) {
        Spliterator.OfInt intSpliterator = Spliterators.spliterator(intList.iterator(), intList.size(),
                Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL);
        return StreamSupport.intStream(intSpliterator, false);
    }

    public static IntStream parallelStream(IntList intList) {
        Spliterator.OfInt intSpliterator = Spliterators.spliterator(intList.iterator(), intList.size(),
                Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL);
        return StreamSupport.intStream(intSpliterator, true);
    }

    public static LongStream stream(LongList longList) {
        Spliterator.OfLong intSpliterator = Spliterators.spliterator(longList.iterator(), longList.size(),
                Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL);
        return StreamSupport.longStream(intSpliterator, false);
    }

    public static LongStream parallelStream(LongList longList) {
        Spliterator.OfLong intSpliterator = Spliterators.spliterator(longList.iterator(), longList.size(),
                Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL);
        return StreamSupport.longStream(intSpliterator, true);
    }

    public static DoubleStream stream(DoubleList doubleList) {
        Spliterator.OfDouble intSpliterator = Spliterators.spliterator(doubleList.iterator(), doubleList.size(),
                Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL);
        return StreamSupport.doubleStream(intSpliterator, false);
    }

    public static DoubleStream parallelStream(DoubleList doubleList) {
        Spliterator.OfDouble intSpliterator = Spliterators.spliterator(doubleList.iterator(), doubleList.size(),
                Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL);
        return StreamSupport.doubleStream(intSpliterator, true);
    }

    private FastUtilStreams() {
    }
}
