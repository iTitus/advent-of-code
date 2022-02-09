package io.github.ititus.aoc.aoc21.day15.graph;

import io.github.ititus.commons.math.number.BigRational;

import java.util.Objects;

/**
 * Edge
 *
 * @param <T> content type
 */
public final class Edge<T> {

    private final Vertex<T> start, end;
    private final BigRational weight;

    Edge(Vertex<T> start, Vertex<T> end, BigRational weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    public Vertex<T> getStart() {
        return start;
    }

    public Vertex<T> getEnd() {
        return end;
    }

    public BigRational getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Edge)) {
            return false;
        }

        Edge<?> edge = (Edge<?>) o;
        return start.equals(edge.start) && end.equals(edge.end) && weight.equals(edge.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, weight);
    }

    @Override
    public String toString() {
        return start + " - [" + weight + "] - " + end;
    }
}
