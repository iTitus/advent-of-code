package io.github.ititus.aoc.common;

import io.github.ititus.math.number.BigRational;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.LongList;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Stream;

public interface AocDayInput {

    static AocDayInput of(AocDay day) {
        return new AocDayInputImpl(day);
    }

    String readString();

    List<String> readAllLines();

    Stream<String> lines();

    IntList readAllLinesAsInt();

    LongList readAllLinesAsLong();

    List<BigInteger> readAllLinesAsBigInteger();

    DoubleList readAllLinesAsDouble();

    List<BigRational> readAllLinesAsBigRational();

}
