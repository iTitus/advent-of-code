package io.github.ititus.aoc.aoc20.day20;

import io.github.ititus.aoc.common.Direction;

public abstract class TileView {

    protected final TileView parent;

    protected TileView(TileView parent) {
        this.parent = parent;
    }

    public int getId() {
        return getParent().getId();
    }

    public int getSize() {
        return getParent().getSize();
    }

    public TileView getParent() {
        return parent.getParent();
    }

    public abstract char get(int x, int y);

    /**
     * Rotate clockwise by 90°
     */
    public TileView rotate() {
        return rotate(1);
    }

    /**
     * Rotate clockwise by amount * 90°
     */
    public TileView rotate(int amount) {
        amount = Math.floorMod(amount, 4);
        if (amount == 0) {
            return this;
        }

        return rotate_(amount);
    }

    protected TileView rotate_(int amount) {
        return new Rotate(this, amount);
    }

    /**
     * Flip along (0.5,0)-(0.5,1)
     */
    public TileView flipX() {
        return new FlipPerpendicular(this, true);
    }

    /**
     * Flip along (0,0.5)-(1,0.5)
     */
    public TileView flipY() {
        return new FlipPerpendicular(this, false);
    }

    /**
     * Flip along (0,0)-(1,1)
     */
    public TileView flipXY() {
        return new FlipDiagonal(this, true);
    }

    /**
     * Flip along (1,0)-(0,1)
     */
    public TileView flipYX() {
        return new FlipDiagonal(this, false);
    }

    public TileView[] getAllOrientations() {
        return new TileView[] { this, rotate(), rotate(2), rotate(3), flipX(), flipY(), flipXY(), flipYX() };
    }

    public char[] getBorder(Direction d) {
        int size = getSize();
        char[] border = new char[size];
        switch (d) {
            case NORTH -> {
                for (int i = 0; i < size; i++) {
                    border[i] = get(i, 0);
                }
            }
            case EAST -> {
                for (int i = 0; i < size; i++) {
                    border[i] = get(size - 1, i);
                }
            }
            case SOUTH -> {
                for (int i = 0; i < size; i++) {
                    border[i] = get(i, size - 1);
                }
            }
            case WEST -> {
                for (int i = 0; i < size; i++) {
                    border[i] = get(0, i);
                }
            }
        }

        return border;
    }

    @Override
    public String toString() {
        int size = getSize();
        StringBuilder b = new StringBuilder();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                b.append(get(x, y));
            }
            b.append('\n');
        }

        b.setLength(b.length() - 1);
        return b.toString();
    }

    private static class Rotate extends TileView {

        private final int amount;

        private Rotate(TileView parent, int amount) {
            super(parent);
            if (amount < 1 || amount > 3) {
                throw new RuntimeException();
            }

            this.amount = amount;
        }


        @Override
        @SuppressWarnings("SuspiciousNameCombination")
        public char get(int x, int y) {
            int size = getSize();
            return switch (amount) {
                case 1 -> parent.get(y, size - 1 - x);
                case 2 -> parent.get(size - 1 - x, size - 1 - y);
                case 3 -> parent.get(size - 1 - y, x);
                default -> throw new RuntimeException();
            };
        }

        @Override
        protected TileView rotate_(int amount) {
            return parent.rotate(this.amount + amount);
        }

        @Override
        public TileView flipX() {
            return switch (amount) {
                case 1 -> parent.flipXY();
                case 2 -> parent.flipY();
                case 3 -> parent.flipYX();
                default -> throw new RuntimeException();
            };
        }

        @Override
        public TileView flipY() {
            return switch (amount) {
                case 1 -> parent.flipYX();
                case 2 -> parent.flipX();
                case 3 -> parent.flipXY();
                default -> throw new RuntimeException();
            };
        }

        @Override
        public TileView flipXY() {
            return switch (amount) {
                case 1 -> parent.flipY();
                case 2 -> parent.flipYX();
                case 3 -> parent.flipX();
                default -> throw new RuntimeException();
            };
        }

        @Override
        public TileView flipYX() {
            return switch (amount) {
                case 1 -> parent.flipX();
                case 2 -> parent.flipXY();
                case 3 -> parent.flipY();
                default -> throw new RuntimeException();
            };
        }
    }

    private static class FlipPerpendicular extends TileView {

        private final boolean flipX;

        private FlipPerpendicular(TileView parent, boolean flipX) {
            super(parent);
            this.flipX = flipX;
        }

        @Override
        public char get(int x, int y) {
            int size = getSize();
            return parent.get(flipX ? size - 1 - x : x, flipX ? y : size - 1 - y);
        }

        @Override
        protected TileView rotate_(int amount) {
            return switch (amount) {
                case 0 -> this;
                case 1 -> flipX ? parent.flipYX() : parent.flipXY();
                case 2 -> flipX ? parent.flipY() : parent.flipX();
                case 3 -> flipX ? parent.flipXY() : parent.flipYX();
                default -> throw new RuntimeException();
            };
        }

        @Override
        public TileView flipX() {
            return flipX ? parent : parent.rotate(2);
        }

        @Override
        public TileView flipY() {
            return flipX ? parent.rotate(2) : parent;
        }

        @Override
        public TileView flipXY() {
            return flipX ? parent.rotate(3) : parent.rotate(1);
        }

        @Override
        public TileView flipYX() {
            return flipX ? parent.rotate(1) : parent.rotate(3);
        }
    }

    private static class FlipDiagonal extends TileView {

        private final boolean flipX;

        private FlipDiagonal(TileView parent, boolean flipX) {
            super(parent);
            this.flipX = flipX;
        }

        @Override
        public char get(int x, int y) {
            int size = getSize();
            return parent.get(flipX ? y : size - 1 - y, flipX ? x : size - 1 - x);
        }

        @Override
        protected TileView rotate_(int amount) {
            return switch (amount) {
                case 0 -> this;
                case 1 -> flipX ? parent.flipX() : parent.flipY();
                case 2 -> flipX ? parent.flipYX() : parent.flipXY();
                case 3 -> flipX ? parent.flipY() : parent.flipX();
                default -> throw new RuntimeException();
            };
        }

        @Override
        public TileView flipX() {
            return flipX ? parent.rotate(1) : parent.rotate(3);
        }

        @Override
        public TileView flipY() {
            return flipX ? parent.rotate(3) : parent.rotate(1);
        }

        @Override
        public TileView flipXY() {
            return flipX ? parent : parent.rotate(2);
        }

        @Override
        public TileView flipYX() {
            return flipX ? parent.rotate(2) : parent;
        }
    }
}
