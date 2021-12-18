package io.github.ititus.aoc.aoc21.day18;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.Optional;

public record SnailfishNumber(
        Node root
) {

    public static SnailfishNumber parse(String s) {
        try {
            return new SnailfishNumber(Node.parse(new PushbackReader(new StringReader(s), 1)));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String toString() {
        return root.toString();
    }

    public SnailfishNumber add(SnailfishNumber n) {
        return new SnailfishNumber(new Pair(root, n.root)).reduce();
    }

    public SnailfishNumber reduce() {
        SnailfishNumber current = this;
        while (true) {
            Optional<SnailfishNumber> exploded = current.explode();
            if (exploded.isPresent()) {
                current = exploded.get();
                continue;
            }

            Optional<SnailfishNumber> split = current.split();
            if (split.isPresent()) {
                current = split.get();
                continue;
            }

            break;
        }

        return current;
    }

    public Optional<SnailfishNumber> explode() {
        if (root instanceof Pair p) {
            return p.explode(1).map(ExplosionArtifacts::result).map(SnailfishNumber::new);
        }

        return Optional.empty();
    }

    public Optional<SnailfishNumber> split() {
        if (root instanceof Pair p) {
            return p.split().map(SnailfishNumber::new);
        } else if (root instanceof Number n) {
            int val = n.value();
            if (val >= 10) {
                int halfRoundDown = val / 2;
                return Optional.of(new SnailfishNumber(new Pair(new Number(halfRoundDown), new Number(val - halfRoundDown))));
            }
        } else {
            throw new RuntimeException();
        }

        return Optional.empty();
    }

    public int magnitude() {
        return root.magnitude();
    }

    public sealed interface Node permits Number, Pair {

        private static Node parse(PushbackReader r) throws IOException {
            int nextChar = r.read();
            if (nextChar == -1) {
                throw new RuntimeException();
            }

            r.unread(nextChar);

            if (nextChar == '[') {
                return Pair.parse(r);
            } else {
                return Number.parse(r);
            }
        }

        int magnitude();

        Node addToLeftNumber(Number n);

        Node addToRightNumber(Number n);

    }

    record ExplosionArtifacts(
            Node result,
            Number left,
            Number right
    ) {

    }

    public record Pair(
            Node left,
            Node right
    ) implements Node {

        private static Node parse(PushbackReader r) throws IOException {
            if (r.read() != '[') {
                throw new RuntimeException();
            }

            Node left = Node.parse(r);
            if (r.read() != ',') {
                throw new RuntimeException();
            }

            Node right = Node.parse(r);
            if (r.read() != ']') {
                throw new RuntimeException();
            }

            return new Pair(left, right);
        }

        @Override
        public String toString() {
            return "[" + left + "," + right + ']';
        }

        @Override
        public Node addToLeftNumber(Number n) {
            return new Pair(left.addToLeftNumber(n), right);
        }

        @Override
        public Node addToRightNumber(Number n) {
            return new Pair(left, right.addToRightNumber(n));
        }

        Optional<Pair> split() {
            if (left instanceof Number n) {
                int val = n.value();
                if (val >= 10) {
                    int halfRoundDown = val / 2;
                    return Optional.of(new Pair(new Pair(new Number(halfRoundDown), new Number(val - halfRoundDown)), right));
                }
            } else if (left instanceof Pair p) {
                Optional<Pair> split = p.split();
                if (split.isPresent()) {
                    return Optional.of(new Pair(split.get(), right));
                }
            } else {
                throw new RuntimeException();
            }

            if (right instanceof Number n) {
                int val = n.value();
                if (val >= 10) {
                    int halfRoundDown = val / 2;
                    return Optional.of(new Pair(left, new Pair(new Number(halfRoundDown), new Number(val - halfRoundDown))));
                }
            } else if (right instanceof Pair p) {
                Optional<Pair> split = p.split();
                if (split.isPresent()) {
                    return Optional.of(new Pair(left, split.get()));
                }
            } else {
                throw new RuntimeException();
            }

            return Optional.empty();
        }

        Optional<ExplosionArtifacts> explode(int level) {
            if (level > 4) {
                if (left instanceof Number l && right instanceof Number r) {
                    return Optional.of(new ExplosionArtifacts(Number.ZERO, l, r));
                } else {
                    throw new RuntimeException();
                }
            }

            if (left instanceof Pair p) {
                Optional<ExplosionArtifacts> exploded = p.explode(level + 1);
                if (exploded.isPresent()) {
                    Pair result = new Pair(exploded.get().result(), exploded.get().right() != null ? right.addToLeftNumber(exploded.get().right()) : right);
                    return Optional.of(new ExplosionArtifacts(result, exploded.get().left(), null));
                }
            }

            if (right instanceof Pair p) {
                Optional<ExplosionArtifacts> exploded = p.explode(level + 1);
                if (exploded.isPresent()) {
                    Pair result = new Pair(exploded.get().left() != null ? left.addToRightNumber(exploded.get().left()) : left, exploded.get().result());
                    return Optional.of(new ExplosionArtifacts(result, null, exploded.get().right()));
                }
            }

            return Optional.empty();
        }

        @Override
        public int magnitude() {
            return 3 * left.magnitude() + 2 * right.magnitude();
        }
    }

    public record Number(
            int value
    ) implements Node {

        public static final Node ZERO = new Number(0);

        private static Node parse(PushbackReader r) throws IOException {
            StringBuilder b = new StringBuilder();
            while (true) {
                int nextChar = r.read();
                if (nextChar == -1) {
                    break;
                } else if (nextChar == ',' || nextChar == ']') {
                    r.unread(nextChar);
                    break;
                }

                b.append((char) nextChar);
            }

            return new Number(Integer.parseInt(b.toString()));
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }

        @Override
        public Node addToLeftNumber(Number n) {
            return new Number(value + n.value);
        }

        @Override
        public Node addToRightNumber(Number n) {
            return new Number(value + n.value);
        }

        @Override
        public int magnitude() {
            return value;
        }
    }
}
