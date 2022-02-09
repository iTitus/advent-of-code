package io.github.ititus.aoc.common;

import io.github.ititus.commons.math.number.BigIntegerMath;
import io.github.ititus.commons.math.number.BigRational;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface AocInput {

    Stream<String> lines();

    String readString();

    List<String> readAllLines();

    default BigInteger[] readAsIntCodeMemory() {
        return Arrays.stream(readString().split(","))
                .map(String::strip)
                .map(BigIntegerMath::of)
                .toArray(BigInteger[]::new);
    }

    default IntList readAllLinesAsInt() {
        try (Stream<String> stream = lines()) {
            return stream
                    .mapToInt(Integer::parseInt)
                    .collect(IntArrayList::new, IntList::add, IntList::addAll);
        }
    }

    default LongList readAllLinesAsLong() {
        try (Stream<String> stream = lines()) {
            return stream
                    .mapToLong(Long::parseLong)
                    .collect(LongArrayList::new, LongList::add, LongList::addAll);
        }
    }

    default List<BigInteger> readAllLinesAsBigInteger() {
        try (Stream<String> stream = lines()) {
            return stream
                    .map(BigIntegerMath::of)
                    .collect(Collectors.toList());
        }
    }

    default DoubleList readAllLinesAsDouble() {
        try (Stream<String> stream = lines()) {
            return stream
                    .mapToDouble(Double::parseDouble)
                    .collect(DoubleArrayList::new, DoubleList::add, DoubleList::addAll);
        }
    }

    default List<BigRational> readAllLinesAsBigRational() {
        try (Stream<String> stream = lines()) {
            return stream
                    .map(BigRational::of)
                    .collect(Collectors.toList());
        }
    }
}
