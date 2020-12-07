package io.github.ititus.aoc.aoc20.day07;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BagRule {

    private static final Pattern P1 = Pattern.compile(
            "^(?<colormod>[a-z]+) (?<color>[a-z]+) bags contain (?<rest>.*)\\.$"
    );
    private static final Pattern P2 = Pattern.compile(", ", Pattern.LITERAL);
    private static final Pattern P3 = Pattern.compile(
            "^(?<amount>\\d+) (?<colormod>[a-z]+) (?<color>[a-z]+) bags?$"
    );

    private final BagColor in;
    private final Object2IntMap<BagColor> out;

    private BagRule(BagColor in, Object2IntMap<BagColor> out) {
        this.in = in;
        this.out = out;
    }

    public static BagRule parse(String line) {
        Matcher m = P1.matcher(line);
        if (!m.matches()) {
            throw new RuntimeException();
        }

        BagColor in = BagColor.parse(m);
        Object2IntMap<BagColor> out;

        String rest = m.group("rest");
        if ("no other bags".equals(rest)) {
            out = Object2IntMaps.emptyMap();
        } else {
            out = new Object2IntOpenHashMap<>();
            for (String s : P2.split(rest)) {
                m = P3.matcher(s);
                if (!m.matches()) {
                    throw new RuntimeException();
                }

                BagColor outColor = BagColor.parse(m);
                int amount = Integer.parseInt(m.group("amount"));
                if (amount <= 0) {
                    throw new RuntimeException();
                }
                if (out.put(outColor, amount) != 0) {
                    throw new RuntimeException();
                }
            }
        }

        return new BagRule(in, Object2IntMaps.unmodifiable(out));
    }

    public BagColor getIn() {
        return in;
    }

    public Object2IntMap<BagColor> getOut() {
        return out;
    }

    public boolean inputMatches(BagColor color) {
        return in.equals(color);
    }

    public boolean isEmpty() {
        return out.isEmpty();
    }

    public boolean outputContains(BagColor color) {
        return out.containsKey(color);
    }
}
