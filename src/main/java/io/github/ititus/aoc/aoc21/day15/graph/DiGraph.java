package io.github.ititus.aoc.aoc21.day15.graph;

import io.github.ititus.commons.math.number.BigRational;
import io.github.ititus.commons.math.number.BigRationalConstants;

import java.util.*;

/**
 * Directed Graph
 *
 * @param <T> content type
 */
public class DiGraph<T> {

    private final Map<T, Vertex<T>> vertices;
    private final Set<Edge<T>> edges;

    public DiGraph() {
        this.vertices = new HashMap<>();
        this.edges = new HashSet<>();
    }

    public Vertex<T> addVertex(T t) {
        if (getVertex(t).isPresent()) {
            throw new IllegalArgumentException("vertex already exists");
        }

        Vertex<T> v = new Vertex<>(t);
        vertices.put(t, v);
        return v;
    }

    public Edge<T> addEdge(T start, T end) {
        return addEdge(getVertex(start).orElseThrow(), getVertex(end).orElseThrow());
    }

    public Edge<T> addEdgeAndVertices(T start, T end) {
        return addEdge(getVertex(start).orElseGet(() -> addVertex(start)), getVertex(end).orElseGet(() -> addVertex(end)));
    }

    public Edge<T> addEdge(T start, T end, BigRational weight) {
        return addEdge(getVertex(start).orElseThrow(), getVertex(end).orElseThrow(), weight);
    }

    public Edge<T> addEdge(Vertex<T> start, Vertex<T> end) {
        return addEdge(start, end, BigRationalConstants.ONE);
    }

    public Edge<T> addEdge(Vertex<T> start, Vertex<T> end, BigRational weight) {
        if (Objects.requireNonNull(start).equals(Objects.requireNonNull(end))) {
            throw new IllegalArgumentException("start=end");
        } else if (getEdgeByVertices(start, end).isPresent()) {
            throw new IllegalArgumentException("edge already exists");
        } else if (!Objects.requireNonNull(weight).isPositive()) {
            throw new IllegalArgumentException("weight");
        }

        Edge<T> e = new Edge<>(start, end, weight);
        edges.add(e);
        start.addOutgoingEdge(end.get(), e);
        end.addIncomingEdge(start.get(), e);
        return e;
    }

    public Collection<Vertex<T>> getVertices() {
        return vertices.values();
    }

    public Set<Edge<T>> getEdges() {
        return edges;
    }

    public Optional<Vertex<T>> getVertex(T t) {
        return Optional.ofNullable(vertices.get(Objects.requireNonNull(t)));
    }

    public Optional<Edge<T>> getEdge(T start, T end) {
        return getEdgeByVertices(getVertex(start).orElseThrow(), getVertex(end).orElseThrow());
    }

    public Optional<Edge<T>> getEdgeByVertices(Vertex<T> start, Vertex<T> end) {
        return Objects.requireNonNull(start).getOutgoingEdge(Objects.requireNonNull(end).get());
    }

    public Collection<Edge<T>> getIncomingEdges(Vertex<T> end) {
        return Collections.unmodifiableCollection(Objects.requireNonNull(end).getIncomingEdges());
    }

    public Collection<Edge<T>> getOutgoingEdges(Vertex<T> start) {
        return Collections.unmodifiableCollection(Objects.requireNonNull(start).getOutgoingEdges());
    }
}
