package io.github.ititus.aoc.aoc19.day14;

import java.math.BigInteger;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Stream;

public final class MaterialStackSet {

    private final NavigableMap<String, BigInteger> map = new TreeMap<>();

    public void add(MaterialStack stack) {
        map.merge(stack.getMaterial(), stack.getAmount(), BigInteger::add);
    }

    public void set(MaterialStack stack) {
        map.merge(stack.getMaterial(), stack.getAmount(), (oldVal, newVal) -> newVal);
    }

    public Optional<MaterialStack> get(String material) {
        return stream().filter(s -> s.getMaterial().equals(material)).findFirst();
    }

    public MaterialStack get(int index) {
        return stream().skip(index).findFirst().orElseThrow();
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public BigInteger remove(String material) {
        return map.remove(material);
    }

    public Stream<MaterialStack> stream() {
        return map.entrySet().stream().map(e -> new MaterialStack(e.getKey(), e.getValue()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MaterialStackSet)) {
            return false;
        }
        MaterialStackSet that = (MaterialStackSet) o;
        return map.equals(that.map);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }
}
