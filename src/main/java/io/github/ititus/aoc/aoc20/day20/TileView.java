package io.github.ititus.aoc.aoc20.day20;

import io.github.ititus.aoc.common.Direction;

public abstract class TileView {

    public abstract int getId();

    public abstract int getSize();

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
        return new Rotate(amount);
    }

    /**
     * Flip along (0.5,0)-(0.5,1)
     */
    public TileView flipX() {
        return new FlipPerpendicular(true);
    }

    /**
     * Flip along (0,0.5)-(1,0.5)
     */
    public TileView flipY() {
        return new FlipPerpendicular(false);
    }

    /**
     * Flip along (0,0)-(1,1)
     */
    public TileView flipXY() {
        return new FlipDiagonal(true);
    }

    /**
     * Flip along (1,0)-(0,1)
     */
    public TileView flipYX() {
        return new FlipDiagonal(false);
    }

    public TileView[] getAllOrientations() {
        return new TileView[] { this, rotate(), rotate(2), rotate(3), flipX(), flipY(), flipXY(), flipYX() };
    }

    public char[] getBorder(Direction d) {
        int s = getSize();
        char[] border = new char[s];
        switch (d) {
            case NORTH -> {
                for (int i = 0; i < s; i++) {
                    border[i] = get(i, 0);
                }
            }
            case EAST -> {
                for (int i = 0; i < s; i++) {
                    border[i] = get(s - 1, i);
                }
            }
            case SOUTH -> {
                for (int i = 0; i < s; i++) {
                    border[i] = get(i, s - 1);
                }
            }
            case WEST -> {
                for (int i = 0; i < s; i++) {
                    border[i] = get(0, i);
                }
            }
        }

        return border;
    }

    @Override
    public String toString() {
        int s = getSize();
        StringBuilder b = new StringBuilder();
        for (int y = 0; y < s; y++) {
            for (int x = 0; x < s; x++) {
                b.append(get(x, y));
            }
            b.append('\n');
        }

        b.setLength(b.length() - 1);
        return b.toString();
    }

    private abstract class Transformation extends TileView {

        @Override
        public int getId() {
            return TileView.this.getId();
        }

        @Override
        public int getSize() {
            return TileView.this.getSize();
        }
    }

    private class Rotate extends Transformation {

        private final int amount;

        private Rotate(int amount) {
            if (amount < 1 || amount > 3) {
                throw new RuntimeException();
            }

            this.amount = amount;
        }

        @Override
        @SuppressWarnings("SuspiciousNameCombination")
        public char get(int x, int y) {
            TileView p = TileView.this;
            int s = getSize() - 1;
            return switch (amount) {
                case 1 -> p.get(y, s - x);
                case 2 -> p.get(s - x, s - y);
                case 3 -> p.get(s - y, x);
                default -> throw new RuntimeException();
            };
        }

        @Override
        protected TileView rotate_(int amount) {
            return TileView.this.rotate(this.amount + amount);
        }

        @Override
        public TileView flipX() {
            TileView p = TileView.this;
            return switch (amount) {
                case 1 -> p.flipXY();
                case 2 -> p.flipY();
                case 3 -> p.flipYX();
                default -> throw new RuntimeException();
            };
        }

        @Override
        public TileView flipY() {
            TileView p = TileView.this;
            return switch (amount) {
                case 1 -> p.flipYX();
                case 2 -> p.flipX();
                case 3 -> p.flipXY();
                default -> throw new RuntimeException();
            };
        }

        @Override
        public TileView flipXY() {
            TileView p = TileView.this;
            return switch (amount) {
                case 1 -> p.flipY();
                case 2 -> p.flipYX();
                case 3 -> p.flipX();
                default -> throw new RuntimeException();
            };
        }

        @Override
        public TileView flipYX() {
            TileView p = TileView.this;
            return switch (amount) {
                case 1 -> p.flipX();
                case 2 -> p.flipXY();
                case 3 -> p.flipY();
                default -> throw new RuntimeException();
            };
        }
    }

    private class FlipPerpendicular extends Transformation {

        private final boolean flipX;

        private FlipPerpendicular(boolean flipX) {
            this.flipX = flipX;
        }

        @Override
        public char get(int x, int y) {
            int s = getSize() - 1;
            return TileView.this.get(flipX ? s - x : x, flipX ? y : s - y);
        }

        @Override
        protected TileView rotate_(int amount) {
            TileView p = TileView.this;
            return switch (amount) {
                case 1 -> flipX ? p.flipYX() : p.flipXY();
                case 2 -> flipX ? p.flipY() : p.flipX();
                case 3 -> flipX ? p.flipXY() : p.flipYX();
                default -> throw new RuntimeException();
            };
        }

        @Override
        public TileView flipX() {
            TileView p = TileView.this;
            return flipX ? p : p.rotate(2);
        }

        @Override
        public TileView flipY() {
            TileView p = TileView.this;
            return flipX ? p.rotate(2) : p;
        }

        @Override
        public TileView flipXY() {
            TileView p = TileView.this;
            return flipX ? p.rotate(3) : p.rotate(1);
        }

        @Override
        public TileView flipYX() {
            TileView p = TileView.this;
            return flipX ? p.rotate(1) : p.rotate(3);
        }
    }

    private class FlipDiagonal extends Transformation {

        private final boolean flipXY;

        private FlipDiagonal(boolean flipXY) {
            this.flipXY = flipXY;
        }

        @Override
        public char get(int x, int y) {
            int s = getSize() - 1;
            return TileView.this.get(flipXY ? y : s - y, flipXY ? x : s - x);
        }

        @Override
        protected TileView rotate_(int amount) {
            TileView p = TileView.this;
            return switch (amount) {
                case 1 -> flipXY ? p.flipX() : p.flipY();
                case 2 -> flipXY ? p.flipYX() : p.flipXY();
                case 3 -> flipXY ? p.flipY() : p.flipX();
                default -> throw new RuntimeException();
            };
        }

        @Override
        public TileView flipX() {
            TileView p = TileView.this;
            return flipXY ? p.rotate(1) : p.rotate(3);
        }

        @Override
        public TileView flipY() {
            TileView p = TileView.this;
            return flipXY ? p.rotate(3) : p.rotate(1);
        }

        @Override
        public TileView flipXY() {
            TileView p = TileView.this;
            return flipXY ? p : p.rotate(2);
        }

        @Override
        public TileView flipYX() {
            TileView p = TileView.this;
            return flipXY ? p.rotate(2) : p;
        }
    }
}
