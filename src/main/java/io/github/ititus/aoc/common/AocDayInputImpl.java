package io.github.ititus.aoc.common;

import io.github.ititus.math.number.BigRational;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.LongList;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Stream;

final class AocDayInputImpl implements AocDayInput {

    private final AocDay day;

    AocDayInputImpl(AocDay day) {
        this.day = day;
    }

    @Override
    public String readString() {
        return InputProvider.readString(day.getYear(), day.getDay());
    }

    @Override
    public List<String> readAllLines() {
        return InputProvider.readAllLines(day.getYear(), day.getDay());
    }

    @Override
    public Stream<String> lines() {
        return InputProvider.lines(day.getYear(), day.getDay());
    }

    @Override
    public IntList readAllLinesAsInt() {
        return InputProvider.readAllLinesAsInt(day.getYear(), day.getDay());
    }

    @Override
    public LongList readAllLinesAsLong() {
        return InputProvider.readAllLinesAsLong(day.getYear(), day.getDay());
    }

    @Override
    public List<BigInteger> readAllLinesAsBigInteger() {
        return InputProvider.readAllLinesAsBigInteger(day.getYear(), day.getDay());
    }

    @Override
    public DoubleList readAllLinesAsDouble() {
        return InputProvider.readAllLinesAsDouble(day.getYear(), day.getDay());
    }

    @Override
    public List<BigRational> readAllLinesAsBigRational() {
        return InputProvider.readAllLinesAsBigRational(day.getYear(), day.getDay());
    }
}
