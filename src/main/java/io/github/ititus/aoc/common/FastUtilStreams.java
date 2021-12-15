package io.github.ititus.aoc.common;

import io.github.ititus.function.BiIntObjConsumer;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.longs.LongCollection;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public final class FastUtilStreams {

    private FastUtilStreams() {
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
}
