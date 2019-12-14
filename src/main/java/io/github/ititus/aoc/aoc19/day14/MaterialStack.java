package io.github.ititus.aoc.aoc19.day14;

import io.github.ititus.math.number.BigIntegerMath;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MaterialStack implements Comparable<MaterialStack> {

    private static final Pattern MATERIAL_STACK_PATTERN = Pattern.compile("\\s*([0-9]+)\\s*([A-Z]+)\\s*");
    private static final Comparator<MaterialStack> COMPARATOR = Comparator.comparing(MaterialStack::getMaterial).thenComparing(MaterialStack::getAmount);

    private final String material;
    private final BigInteger amount;

    public MaterialStack(String material, int amount) {
        this(material, BigIntegerMath.of(amount));
    }

    public MaterialStack(String material, BigInteger amount) {
        this.material = Objects.requireNonNull(material);
        this.amount = Objects.requireNonNull(amount);
    }

    public static MaterialStack parse(String materialStack) {
        Matcher m = MATERIAL_STACK_PATTERN.matcher(materialStack);
        if (!m.matches()) {
            throw new IllegalArgumentException();
        }

        String material = m.group(2);
        int amount = Integer.parseInt(m.group(1));

        return new MaterialStack(material, amount);
    }

    public boolean canStack(MaterialStack o) {
        return material.equals(o.material);
    }

    public MaterialStack stack(MaterialStack o) {
        if (!canStack(o)) {
            throw new IllegalArgumentException();
        }
        return new MaterialStack(material, amount.add(o.amount));
    }

    public MaterialStack withAmount(int amount) {
        return withAmount(BigIntegerMath.of(amount));
    }

    public MaterialStack withAmount(BigInteger amount) {
        return new MaterialStack(material, amount);
    }

    public MaterialStack add(int n) {
        return add(BigIntegerMath.of(n));
    }

    public MaterialStack add(BigInteger r) {
        return new MaterialStack(material, amount.add(r));
    }

    public MaterialStack subtract(int n) {
        return subtract(BigIntegerMath.of(n));
    }

    public MaterialStack subtract(BigInteger r) {
        return new MaterialStack(material, amount.subtract(r));
    }

    public MaterialStack multiply(int n) {
        return multiply(BigIntegerMath.of(n));
    }

    public MaterialStack multiply(BigInteger r) {
        return new MaterialStack(material, amount.multiply(r));
    }

    public String getMaterial() {
        return material;
    }

    public BigInteger getAmount() {
        return amount;
    }

    @Override
    public int compareTo(MaterialStack o) {
        return COMPARATOR.compare(this, o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaterialStack)) {
            return false;
        }
        MaterialStack that = (MaterialStack) o;
        return amount.equals(that.amount) && material.equals(that.material);
    }

    @Override
    public int hashCode() {
        return Objects.hash(material, amount);
    }

    @Override
    public String toString() {
        return "[" + amount + " " + material + "]";
    }
}
