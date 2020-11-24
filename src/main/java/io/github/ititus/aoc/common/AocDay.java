package io.github.ititus.aoc.common;

import io.github.ititus.data.ArrayUtil;

public final class AocDay implements Comparable<AocDay> {

    private final int year;
    private final int day;

    public AocDay(Aoc aoc) {
        this(aoc.year(), aoc.day());
    }

    public AocDay(int year, int day) {
        if (year < 2015 || day < 1 || day > 25) {
            throw new IllegalArgumentException();
        }

        this.year = year;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getDay() {
        return day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AocDay)) {
            return false;
        }
        AocDay aocDay = (AocDay) o;
        return year == aocDay.year && day == aocDay.day;
    }

    @Override
    public int hashCode() {
        return ArrayUtil.hash(year, day);
    }

    @Override
    public int compareTo(AocDay o) {
        int c = Integer.compare(year, o.year);
        if (c != 0) {
            return c;
        }

        return Integer.compare(day, o.day);
    }
}
