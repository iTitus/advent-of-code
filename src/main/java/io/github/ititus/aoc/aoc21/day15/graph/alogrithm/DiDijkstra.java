package io.github.ititus.aoc.aoc21.day15.graph.alogrithm;

import io.github.ititus.aoc.aoc21.day15.graph.DiGraph;
import io.github.ititus.aoc.aoc21.day15.graph.Edge;
import io.github.ititus.aoc.aoc21.day15.graph.Vertex;
import io.github.ititus.math.number.BigRational;
import io.github.ititus.math.number.BigRationalConstants;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Directed Dijkstra algorithm
 *
 * @param <T> content type
 */
public class DiDijkstra<T> {

    private static final boolean PRINT_DEBUG_INFO = false;

    private final DiGraph<T> graph;
    private final Vertex<T> start;
    private final Result r;
    private final Comparator<Vertex<T>> distanceComparator;

    public DiDijkstra(DiGraph<T> graph, Vertex<T> start) {
        this.graph = graph;
        this.start = start;
        this.r = new Result();
        this.distanceComparator = (v1, v2) -> {
            Optional<BigRational> r1 = r.getDist(v1);
            Optional<BigRational> r2 = r.getDist(v2);

            if (r1.isPresent()) {
                return r2.map(r2_ -> r1.get().compareTo(r2_)).orElse(-1);
            } else if (r2.isPresent()) {
                return 1;
            }

            return 0;
        };
    }

    public Result findShortestPaths() {
        if (!r.done) {
            PriorityQueue<Vertex<T>> q = new PriorityQueue<>(distanceComparator);
            r.setDist(start, BigRationalConstants.ZERO);
            q.add(start);

            while (!q.isEmpty()) {
                Vertex<T> u = q.remove();

                Optional<BigRational> distOpt = r.getDist(u);
                if (distOpt.isEmpty()) {
                    continue;
                }

                BigRational dist = distOpt.get();
                for (Edge<T> e : graph.getOutgoingEdges(u)) {
                    Vertex<T> v = e.getEnd();
                    BigRational newDist = dist.add(e.getWeight());
                    Optional<BigRational> oldDist = r.getDist(v);
                    if (oldDist.isEmpty() || newDist.compareTo(oldDist.get()) < 0) {
                        r.setDist(v, newDist);
                        r.setPrev(v, u);

                        // re-insert at correct position
                        q.remove(v);
                        q.add(v);
                    }
                }

                if (PRINT_DEBUG_INFO) {
                    System.out.printf("visiting=%s | %s%n", u, resultToString(r));
                }
            }

            r.done = true;
        }

        return r;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private String resultToString(Result r) {
        Stream<Vertex<T>> stream = graph.getVertices().stream();
        if (graph.getVertices().iterator().next().get() instanceof Comparable) {
            stream = stream.sorted(Comparator.comparing(v -> (Comparable) v.get()));
        }
        List<Vertex<T>> vertices = stream.toList();

        StringBuilder b = new StringBuilder();
        for (Vertex<T> v : vertices) {
            b.append(v.get()).append('=');

            Optional<BigRational> dist = r.getDist(v);
            if (dist.isPresent()) {
                b.append(dist.get());
                r.getPrev(v).ifPresent(p -> b.append(", ").append(p));
            } else {
                b.append("∞");
            }

            b.append(" | ");
        }

        b.setLength(b.length() - 3);
        return b.toString();
    }

    public class Result {

        private final Map<T, BigRational> dist;
        private final Map<T, Vertex<T>> prev;
        private boolean done;

        private Result() {
            this.dist = new HashMap<>();
            this.prev = new HashMap<>();
            this.done = false;
        }

        private void setDist(Vertex<T> v, BigRational r) {
            dist.put(v.get(), r);
        }

        private void setPrev(Vertex<T> v, Vertex<T> w) {
            prev.put(v.get(), w);
        }

        private Optional<BigRational> getDist(Vertex<T> v) {
            return Optional.ofNullable(dist.get(v.get()));
        }

        private Optional<Vertex<T>> getPrev(Vertex<T> v) {
            return Optional.ofNullable(prev.get(v.get()));
        }

        public List<Vertex<T>> getShortestPathVertices(Vertex<T> end) {
            List<Vertex<T>> list = new ArrayList<>();
            list.add(end);

            Vertex<T> v = end;
            while (!v.equals(start)) {
                v = getPrev(v).orElseThrow();
                list.add(v);
            }

            Collections.reverse(list);
            return list;
        }

        public List<T> getShortestPathObjects(Vertex<T> end) {
            return getShortestPathVertices(end).stream()
                    .map(Vertex::get)
                    .collect(Collectors.toList());
        }

        public List<Edge<T>> getShortestPathEdges(Vertex<T> end) {
            List<Edge<T>> list = new ArrayList<>();

            Vertex<T> v = end, w;
            while (!v.equals(start)) {
                w = getPrev(v).orElseThrow();
                list.add(graph.getEdgeByVertices(v, w).orElseThrow());

                v = w;
            }

            Collections.reverse(list);
            return list;
        }

        public BigRational getShortestPathLength(Vertex<T> end) {
            return r.getDist(end).orElseThrow();
        }
    }
}
