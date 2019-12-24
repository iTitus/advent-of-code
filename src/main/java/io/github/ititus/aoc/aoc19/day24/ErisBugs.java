package io.github.ititus.aoc.aoc19.day24;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ErisBugs {

    private static final int SIZE = 5;
    private static final List<String> EMPTY;

    static {
        String[] arr = new String[SIZE];
        Arrays.fill(arr, ".".repeat(SIZE));
        EMPTY = List.of(arr);
    }

    private final Set<Integer> previousStates;
    private final boolean recursive;
    private final int level;

    private int state;
    private ErisBugs inner, outer;

    public ErisBugs(boolean recursive, int level, ErisBugs inner, ErisBugs outer) {
        this(recursive, level, EMPTY, inner, outer);

    }

    public ErisBugs(boolean recursive, List<String> lines) {
        this(recursive, 0, lines, null, null);
    }

    private ErisBugs(boolean recursive, int level, List<String> lines, ErisBugs inner, ErisBugs outer) {
        this.recursive = recursive;
        this.level = level;
        this.previousStates = recursive ? Set.of() : new HashSet<>();
        this.inner = inner;
        this.outer = outer;
        parse(lines);
    }

    private static int setAlive(int state, int x, int y) {
        return state | (1 << (y * SIZE + x));
    }

    private void parse(List<String> lines) {
        if (lines.size() != SIZE) {
            throw new RuntimeException();
        }
        for (int y = 0; y < SIZE; y++) {
            String line = lines.get(y);
            if (line.length() != SIZE) {
                throw new RuntimeException();
            }
            for (int x = 0; x < SIZE; x++) {
                char c = line.charAt(x);
                if (c == '#') {
                    state = setAlive(state, x, y);
                } else if (c != '.') {
                    throw new RuntimeException();
                }
            }
        }

        if (recursive && get(SIZE / 2, SIZE / 2)) {
            throw new RuntimeException();
        }

        if (!recursive) {
            previousStates.add(state);
        }
    }

    private boolean get(int x, int y) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
            return false;
        }
        return (state & (1 << (y * SIZE + x))) != 0;
    }

    private int countAliveNeighbors(int x, int y) {
        int n = 0;
        if (get(x - 1, y)) {
            n++;
        }
        if (get(x + 1, y)) {
            n++;
        }
        if (get(x, y - 1)) {
            n++;
        }
        if (get(x, y + 1)) {
            n++;
        }
        if (recursive) {
            if (inner != null) {
                if (x == SIZE / 2 && y == SIZE / 2 - 1) {
                    for (int x_ = 0; x_ < SIZE; x_++) {
                        if (inner.get(x_, 0)) {
                            n++;
                        }
                    }
                }
                if (x == SIZE / 2 && y == SIZE / 2 + 1) {
                    for (int x_ = 0; x_ < SIZE; x_++) {
                        if (inner.get(x_, SIZE - 1)) {
                            n++;
                        }
                    }
                }
                if (x == SIZE / 2 - 1 && y == SIZE / 2) {
                    for (int y_ = 0; y_ < SIZE; y_++) {
                        if (inner.get(0, y_)) {
                            n++;
                        }
                    }
                }
                if (x == SIZE / 2 + 1 && y == SIZE / 2) {
                    for (int y_ = 0; y_ < SIZE; y_++) {
                        if (inner.get(SIZE - 1, y_)) {
                            n++;
                        }
                    }
                }
            }

            if (outer != null) {
                if (x == 0) {
                    if (outer.get(SIZE / 2 - 1, SIZE / 2)) {
                        n++;
                    }
                }
                if (x == SIZE - 1) {
                    if (outer.get(SIZE / 2 + 1, SIZE / 2)) {
                        n++;
                    }
                }
                if (y == 0) {
                    if (outer.get(SIZE / 2, SIZE / 2 - 1)) {
                        n++;
                    }
                }
                if (y == SIZE - 1) {
                    if (outer.get(SIZE / 2, SIZE / 2 + 1)) {
                        n++;
                    }
                }
            }
        }
        return n;
    }

    public int stepUntilRepeat() {
        // noinspection StatementWithEmptyBody
        while (update(null)) {
            // NO-OP
        }
        return state;
    }

    public int step(int n) {
        for (int i = 0; i < n; i++) {
            update(null);
        }
        return getBugCount(null);
    }

    private int getBugCount(ErisBugs from) {
        int n = Integer.bitCount(state);
        if (recursive) {
            if (inner != null && inner != from) {
                n += inner.getBugCount(this);
            }
            if (outer != null && outer != from) {
                n += outer.getBugCount(this);
            }
        }
        return n;
    }

    public boolean update(ErisBugs from) {
        int newState = 0;

        if (recursive) {
            if (outer == null) {
                boolean createOuter = false;

                for (int x = 0; x < SIZE - 1; x++) {
                    if (get(x, 0)) {
                        createOuter = true;
                        break;
                    }
                }
                if (!createOuter) {
                    for (int x = 1; x < SIZE; x++) {
                        if (get(x, SIZE - 1)) {
                            createOuter = true;
                            break;
                        }
                    }
                }
                if (!createOuter) {
                    for (int y = 1; y < SIZE; y++) {
                        if (get(0, y)) {
                            createOuter = true;
                            break;
                        }
                    }
                }
                if (!createOuter) {
                    for (int y = 0; y < SIZE - 1; y++) {
                        if (get(SIZE - 1, y)) {
                            createOuter = true;
                            break;
                        }
                    }
                }

                if (createOuter) {
                    outer = new ErisBugs(true, level - 1, this, null);
                }
            }

            if (inner == null) {
                int count = countAliveNeighbors(SIZE / 2, SIZE / 2);
                if (count > 0) {
                    inner = new ErisBugs(true, level + 1, null, this);
                }
            }
        }

        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                if (!recursive || x != SIZE / 2 || y != SIZE / 2) {
                    int count = countAliveNeighbors(x, y);
                    if (get(x, y)) {
                        if (count == 1) {
                            newState = setAlive(newState, x, y);
                        }
                    } else {
                        if (count == 1 || count == 2) {
                            newState = setAlive(newState, x, y);
                        }
                    }
                }
            }
        }

        if (recursive) {
            if (inner != null && inner != from) {
                inner.update(this);
            }
            if (outer != null && outer != from) {
                outer.update(this);
            }
        }

        state = newState;
        if (!recursive) {
            return previousStates.add(newState);
        }

        return true;
    }
}
