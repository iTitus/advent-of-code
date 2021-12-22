package io.github.ititus.aoc.aoc21.day22;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CuboidCollection implements Iterable<Cuboid> {

    private final List<Cuboid> cuboids = new ArrayList<>();

    public boolean add(Cuboid toAdd) {
        List<Cuboid> toAddList = List.of(toAdd);
        for (Cuboid existing : cuboids) {
            toAddList = toAddList.stream()
                    .flatMap(c -> c.difference(existing))
                    .toList();
            if (toAddList.isEmpty()) {
                return false;
            }
        }

        cuboids.addAll(toAddList);
        return true;
    }

    public boolean addAll(Iterable<? extends Cuboid> it) {
        boolean didSomething = false;
        for (Cuboid c : it) {
            didSomething |= add(c);
        }

        return didSomething;
    }

    public boolean remove(Cuboid toRemove) {
        boolean didSomething = false;
        List<Cuboid> leftover = new ArrayList<>();
        for (Iterator<Cuboid> it = cuboids.iterator(); it.hasNext(); ) {
            Cuboid existing = it.next();
            if (existing.contains(toRemove)) {
                it.remove();
                leftover.addAll(existing.difference(toRemove).toList());
                didSomething = true;
                break;
            } else if (existing.intersects(toRemove)) {
                it.remove();
                leftover.addAll(existing.difference(toRemove).toList());
                didSomething = true;
            }
        }

        cuboids.addAll(leftover);
        return didSomething;
    }

    public boolean removeAll(Iterable<? extends Cuboid> it) {
        boolean didSomething = false;
        for (Cuboid c : it) {
            didSomething |= remove(c);
        }

        return didSomething;
    }

    public boolean retain(Cuboid bounds) {
        boolean didSomething = false;
        List<Cuboid> newCuboids = new ArrayList<>();
        for (Cuboid candidate : cuboids) {
            if (candidate.contains(bounds)) {
                newCuboids.add(bounds);
                didSomething = true;
                break;
            } else if (bounds.contains(candidate)) {
                newCuboids.add(candidate);
            } else if (candidate.intersects(bounds)) {
                newCuboids.addAll(candidate.difference(bounds).toList());
                didSomething = true;
            } else {
                didSomething = true;
            }
        }

        if (didSomething) {
            cuboids.clear();
            cuboids.addAll(newCuboids);
            return true;
        }

        return false;
    }

    public long getVolume() {
        return cuboids.stream()
                .mapToLong(Cuboid::volume)
                .sum();
    }

    @Override
    public Iterator<Cuboid> iterator() {
        return cuboids.iterator();
    }
}
