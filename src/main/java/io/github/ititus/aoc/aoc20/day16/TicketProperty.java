package io.github.ititus.aoc.aoc20.day16;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TicketProperty {

    public static final Pattern P = Pattern.compile(
            "^(?<name>[a-z ]+): (?<min1>\\d+)-(?<max1>\\d+) or (?<min2>\\d+)-(?<max2>\\d+)$");

    private final String name;
    private final int min1, max1, min2, max2;

    private TicketProperty(String name, int min1, int max1, int min2, int max2) {
        this.name = name;
        this.min1 = min1;
        this.max1 = max1;
        this.min2 = min2;
        this.max2 = max2;
    }

    public static TicketProperty of(Matcher m) {
        return new TicketProperty(
                m.group("name"),
                Integer.parseInt(m.group("min1")),
                Integer.parseInt(m.group("max1")),
                Integer.parseInt(m.group("min2")),
                Integer.parseInt(m.group("max2"))
        );
    }

    public boolean matches(int n) {
        return (min1 <= n && n <= max1) || (min2 <= n && n <= max2);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TicketProperty)) {
            return false;
        }
        TicketProperty that = (TicketProperty) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
