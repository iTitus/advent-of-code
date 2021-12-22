package io.github.ititus.aoc.aoc21.day15.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Vertex
 *
 * @param <T> content type
 */
public final class Vertex<T> {

    private final T t;
    private final Map<T, Edge<T>> incomingEdges;
    private final Map<T, Edge<T>> outgoingEdges;

    Vertex(T t) {
        this.t = t;
        this.incomingEdges = new HashMap<>();
        this.outgoingEdges = new HashMap<>();
    }

    public T get() {
        return t;
    }

    public boolean hasIncomingEdgeFrom(Vertex<T> start) {
        return hasIncomingEdgeFrom(start.get());
    }

    public boolean hasIncomingEdgeFrom(T start) {
        return incomingEdges.containsKey(start);
    }

    public boolean hasOutgoingEdgeTo(Vertex<T> end) {
        return hasOutgoingEdgeTo(end.get());
    }

    public boolean hasOutgoingEdgeTo(T end) {
        return outgoingEdges.containsKey(end);
    }

    void addIncomingEdge(T start, Edge<T> e) {
        incomingEdges.put(start, e);
    }

    Collection<Edge<T>> getIncomingEdges() {
        return incomingEdges.values();
    }

    Optional<Edge<T>> getIncomingEdge(T start) {
        return Optional.ofNullable(incomingEdges.get(start));
    }

    void addOutgoingEdge(T end, Edge<T> e) {
        outgoingEdges.put(end, e);
    }

    Collection<Edge<T>> getOutgoingEdges() {
        return outgoingEdges.values();
    }

    Optional<Edge<T>> getOutgoingEdge(T end) {
        return Optional.ofNullable(outgoingEdges.get(end));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Vertex)) {
            return false;
        }

        Vertex<?> v = (Vertex<?>) o;
        return t.equals(v.t);
    }

    @Override
    public int hashCode() {
        return t.hashCode();
    }

    @Override
    public String toString() {
        return t.toString();
    }
}
