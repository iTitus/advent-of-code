package io.github.ititus.aoc;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.LongList;

import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.*;

public final class FastUtilStreams {

    private FastUtilStreams() {
    }

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

    public static <T, V> Collector<T, ?, Int2ObjectMap<V>> toMap(ToIntFunction<? super T> keyMapper,
                                                                 Function<? super T, ? extends V> valueMapper) {
        return Collector.of(
                Int2ObjectOpenHashMap::new,
                (map, element) -> {
                    int k = keyMapper.applyAsInt(element);
                    V v = Objects.requireNonNull(valueMapper.apply(element));
                    V u = map.putIfAbsent(k, v);
                    if (u != null) {
                        throw new IllegalStateException(
                                String.format("Duplicate key %s (attempted merging values %s and %s)", k, u, v)
                        );
                    }
                },
                (m1, m2) -> {
                    for (Int2ObjectMap.Entry<V> e : m2.int2ObjectEntrySet()) {
                        int k = e.getIntKey();
                        V v = Objects.requireNonNull(e.getValue());
                        V u = m1.putIfAbsent(k, v);
                        if (u != null) {
                            throw new IllegalStateException(
                                    String.format("Duplicate key %s (attempted merging values %s and %s)", k, u, v)
                            );
                        }
                    }
                    return m1;
                }
        );
    }

    public static <V> void forEach(Int2ObjectMap<V> map, BiIntObjConsumer<? super V> action) {
        Objects.requireNonNull(map);
        Objects.requireNonNull(action);
        map.int2ObjectEntrySet().forEach(e -> action.accept(e.getIntKey(), e.getValue()));
    }

    @FunctionalInterface
    public interface BiIntObjConsumer<T> {

        void accept(int i, T t);

        default BiIntObjConsumer<T> andThen(BiIntObjConsumer<? super T> after) {
            Objects.requireNonNull(after);
            return (i, t) -> {
                accept(i, t);
                after.accept(i, t);
            };
        }
    }
}
