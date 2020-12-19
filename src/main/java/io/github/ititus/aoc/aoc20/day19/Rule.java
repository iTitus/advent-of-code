package io.github.ititus.aoc.aoc20.day19;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public interface Rule {

    String toRegex(Int2ObjectMap<Rule> rules);

}
