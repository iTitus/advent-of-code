package io.github.ititus.aoc.aoc18.day04;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.data.ArrayUtil;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

@Aoc(year = 2018, day = 4)
public final class Day04 implements AocSolution {

    private List<Shift> shifts;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        try (Stream<String> stream = input.lines()) {
            shifts = stream
                    .map(Record::of)
                    .sorted(comparing(Record::getTimestamp))
                    .collect(Collector.<Record, List<Shift>, List<Shift>>of(
                            ArrayList::new,
                            (l, r) -> {
                                switch (r.getType()) {
                                    case SHIFT_BEGIN -> l.add(new Shift(r.getTimestamp(), r.getId()));
                                    case FALL_ASLEEP -> l.get(l.size() - 1).fallAsleep(r.getTimestamp());
                                    case WAKE_UP -> l.get(l.size() - 1).wakeUp(r.getTimestamp());
                                }
                            },
                            (l1, l2) -> {
                                throw new UnsupportedOperationException();
                            },
                            l -> {
                                if (!l.isEmpty() && l.get(l.size() - 1).isUnfinished()) {
                                    throw new UnsupportedOperationException();
                                }
                                return l;
                            }
                    ));
        }
    }

    @Override
    public Object part1() {
        var longestSleeper = shifts.stream()
                .collect(groupingBy(Shift::getId, summingInt(Shift::getTotalMinutesAsleep)))
                .entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .orElseThrow();
        System.out.printf("Guard #%d slept for %d minutes%n", longestSleeper.getKey(), longestSleeper.getValue());

        var mostSleptMinute = shifts.stream()
                .filter(s -> s.getId() == longestSleeper.getKey())
                .flatMapToInt(s -> {
                    IntStream stream = IntStream.empty();
                    for (Sleep sleep : s.getSleeps()) {
                        stream = IntStream.concat(stream, IntStream.range(sleep.getBegin(), sleep.getEnd()));
                    }
                    return stream;
                })
                .collect(Int2IntOpenHashMap::new,
                        (m, minute) -> m.addTo(minute, 1),
                        (m1, m2) -> m2.int2IntEntrySet().fastForEach(e -> m1.addTo(e.getIntKey(), e.getIntValue()))
                )
                .int2IntEntrySet().stream()
                .max(Comparator.comparingInt(Int2IntMap.Entry::getIntValue))
                .orElseThrow();
        System.out.printf("Slept %d times at minute %d%n", mostSleptMinute.getIntValue(), mostSleptMinute.getIntKey());

        int answer = longestSleeper.getKey() * mostSleptMinute.getIntKey();
        return "Answer: " + answer;
    }

    @Override
    public Object part2() {
        var mostSleptMinuteAll = shifts.stream()
                .flatMap(s -> {
                    Stream<GuardMinute> stream = Stream.empty();
                    for (Sleep sleep : s.getSleeps()) {
                        stream = Stream.concat(
                                stream,
                                IntStream.range(sleep.getBegin(), sleep.getEnd())
                                        .mapToObj(i -> new GuardMinute(s.getId(), i))
                        );
                    }
                    return stream;
                })
                .collect(groupingBy(Function.identity(), counting()))
                .entrySet().stream()
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .orElseThrow();
        System.out.printf("Guard #%d slept %d times at minute %d%n", mostSleptMinuteAll.getKey().getId(),
                mostSleptMinuteAll.getValue(), mostSleptMinuteAll.getKey().getMinute());

        int answer = mostSleptMinuteAll.getKey().getId() * mostSleptMinuteAll.getKey().getMinute();
        return "Answer: " + answer;
    }

    private enum RecordType {
        SHIFT_BEGIN, FALL_ASLEEP, WAKE_UP
    }

    private static final class Record {

        private static final Pattern BEGINS_SHIFT =
                Pattern.compile(
                        "\\[(?<y>\\d+)-(?<m>\\d+)-(?<d>\\d+) (?<h>\\d+):(?<min>\\d+)] Guard #(?<id>\\d+) begins shift");
        private static final Pattern FALLS_ASLEEP =
                Pattern.compile(
                        "\\[(?<y>\\d+)-(?<m>\\d+)-(?<d>\\d+) (?<h>\\d+):(?<min>\\d+)] falls asleep");
        private static final Pattern WAKES_UP =
                Pattern.compile(
                        "\\[(?<y>\\d+)-(?<m>\\d+)-(?<d>\\d+) (?<h>\\d+):(?<min>\\d+)] wakes up");

        private final Timestamp timestamp;
        private final int id;
        private final RecordType type;

        private Record(Timestamp timestamp, int id, RecordType type) {
            this.timestamp = timestamp;
            this.id = id;
            this.type = Objects.requireNonNull(type);
        }

        private static Record of(String s) {
            boolean success = false;
            int id = -1;
            RecordType type = null;

            Matcher m = BEGINS_SHIFT.matcher(s);
            if (m.matches()) {
                success = true;
                id = Integer.parseInt(m.group("id"));
                if (id < 0) {
                    throw new IllegalArgumentException();
                }
                type = RecordType.SHIFT_BEGIN;
            }

            if (!success) {
                m = FALLS_ASLEEP.matcher(s);
                if (m.matches()) {
                    success = true;
                    id = -1;
                    type = RecordType.FALL_ASLEEP;
                }
            }

            if (!success) {
                m = WAKES_UP.matcher(s);
                if (m.matches()) {
                    success = true;
                    id = -1;
                    type = RecordType.WAKE_UP;
                }
            }

            if (!success) {
                throw new IllegalArgumentException();
            }

            return new Record(Timestamp.of(m), id, type);
        }

        private Timestamp getTimestamp() {
            return timestamp;
        }

        public int getId() {
            return id;
        }

        public RecordType getType() {
            return type;
        }
    }

    private static final class Timestamp implements Comparable<Timestamp> {

        private final int year, month, day, hour, minute;

        private Timestamp(int year, int month, int day, int hour, int minute) {
            if (minute < 0 || minute >= 60) {
                throw new IllegalArgumentException();
            } else if (hour < 0 || hour >= 24) {
                throw new IllegalArgumentException();
            } else if (day <= 0 || day > 31) {
                throw new IllegalArgumentException();
            } else if (month <= 0 || month > 12) {
                throw new IllegalArgumentException();
            } else if (year < 0) {
                throw new IllegalArgumentException();
            }

            this.year = year;
            this.month = month;
            this.day = day;
            this.hour = hour;
            this.minute = minute;
        }

        private static Timestamp of(Matcher m) {
            return new Timestamp(
                    Integer.parseInt(m.group("y")),
                    Integer.parseInt(m.group("m")),
                    Integer.parseInt(m.group("d")),
                    Integer.parseInt(m.group("h")),
                    Integer.parseInt(m.group("min"))
            );
        }

        private int getYear() {
            return year;
        }

        private int getMonth() {
            return month;
        }

        private int getDay() {
            return day;
        }

        private int getHour() {
            return hour;
        }

        private int getMinute() {
            return minute;
        }

        @Override
        public int compareTo(Timestamp t) {
            int c = Integer.compare(year, t.year);
            if (c != 0) {
                return c;
            }

            c = Integer.compare(month, t.month);
            if (c != 0) {
                return c;
            }

            c = Integer.compare(day, t.day);
            if (c != 0) {
                return c;
            }

            c = Integer.compare(hour, t.hour);
            if (c != 0) {
                return c;
            }

            return Integer.compare(minute, t.minute);
        }
    }

    private static final class Shift {

        private final Timestamp timestamp;
        private final int id;
        private final List<Sleep> sleeps;

        private Timestamp sleepStart;

        private Shift(Timestamp timestamp, int id) {
            this.timestamp = timestamp;
            this.id = id;
            this.sleeps = new ArrayList<>();
        }

        private Timestamp getTimestamp() {
            return timestamp;
        }

        private int getId() {
            return id;
        }

        private List<Sleep> getSleeps() {
            return sleeps;
        }

        private void fallAsleep(Timestamp timestamp) {
            if (sleepStart != null) {
                throw new IllegalStateException();
            }

            sleepStart = timestamp;
        }

        private void wakeUp(Timestamp timestamp) {
            if (sleepStart == null) {
                throw new IllegalStateException();
            }

            sleeps.add(new Sleep(id, sleepStart, timestamp));
            sleepStart = null;
        }

        private boolean isUnfinished() {
            return sleepStart != null;
        }

        private int getTotalMinutesAsleep() {
            return sleeps.stream().mapToInt(Sleep::getMinutesAsleep).sum();
        }
    }

    private static final class Sleep {

        private final int id;
        private final Timestamp begin;
        private final Timestamp end;

        public Sleep(int id, Timestamp begin, Timestamp end) {
            if (begin.getYear() != end.getYear()) {
                throw new IllegalArgumentException();
            } else if (begin.getMonth() != end.getMonth()) {
                throw new IllegalArgumentException();
            } else if (begin.getDay() != end.getDay()) {
                throw new IllegalArgumentException();
            } else if (begin.getHour() != end.getHour() || begin.getHour() != 0) {
                throw new IllegalArgumentException();
            } else if (begin.getMinute() >= end.getMinute()) {
                throw new IllegalArgumentException();
            }

            this.id = id;
            this.begin = begin;
            this.end = end;
        }

        private int getId() {
            return id;
        }

        private int getBegin() {
            return begin.getMinute();
        }

        private int getEnd() {
            return end.getMinute();
        }

        private int getMinutesAsleep() {
            return end.getMinute() - begin.getMinute();
        }
    }

    private static final class GuardMinute {

        private final int id;
        private final int minute;

        private GuardMinute(int id, int minute) {
            this.id = id;
            this.minute = minute;
        }

        private int getId() {
            return id;
        }

        private int getMinute() {
            return minute;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GuardMinute)) {
                return false;
            }
            GuardMinute that = (GuardMinute) o;
            return id == that.id && minute == that.minute;
        }

        @Override
        public int hashCode() {
            return ArrayUtil.hash(id, minute);
        }
    }
}
