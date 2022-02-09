package io.github.ititus.aoc.aoc19.day13;

import io.github.ititus.aoc.aoc19.IntComputer;
import io.github.ititus.commons.data.mutable.Mutable;
import io.github.ititus.commons.data.mutable.MutableInt;
import io.github.ititus.commons.math.number.JavaMath;
import io.github.ititus.commons.math.vector.Vec2i;

import java.math.BigInteger;
import java.util.Arrays;

public class ArcadeMachine {

    private static final int SIZE_X = 44;
    private static final int SIZE_Y = 23;

    private final IntComputer computer;
    private final Mutable<Integer> bx;
    private final Mutable<Integer> by;
    private final ArcadeTile[][] screen;
    private final Mutable<Vec2i> paddlePos;
    private final Mutable<Vec2i> ballPos;
    private final MutableInt score;

    public ArcadeMachine(BigInteger[] memory, boolean hackMoney) {
        memory = Arrays.copyOf(memory, memory.length);
        this.bx = Mutable.empty();
        this.by = Mutable.empty();
        this.screen = new ArcadeTile[SIZE_X][SIZE_Y];
        for (int y = 0; y < SIZE_Y; y++) {
            for (int x = 0; x < SIZE_Y; x++) {
                this.screen[x][y] = ArcadeTile.EMPTY;
            }
        }
        this.paddlePos = Mutable.empty();
        this.ballPos = Mutable.empty();
        this.score = MutableInt.ofZero();

        if (hackMoney) {
            memory[0] = BigInteger.TWO;
        }

        this.computer = new IntComputer(
                this::getNextPaddleMovement,
                this::update,
                memory
        );
    }

    private void render() {
        // NO-OP
    }

    private int getNextPaddleMovement() {
        return JavaMath.sgn(ballPos.get().subtract(paddlePos.get()).x());
    }

    private void update(int output) {
        if (bx.get() == null) {
            bx.set(output);
        } else if (by.get() == null) {
            by.set(output);
        } else {
            int x = bx.get();
            int y = by.get();
            if (x == -1 && y == 0) {
                score.set(output);
            } else {
                ArcadeTile tile = ArcadeTile.get(output);
                screen[x][y] = tile;
                if (tile == ArcadeTile.HORIZONTAL_PADDLE) {
                    paddlePos.set(new Vec2i(x, y));
                } else if (tile == ArcadeTile.BALL) {
                    ballPos.set(new Vec2i(x, y));
                }
            }
            bx.set(null);
            by.set(null);

            render();
        }
    }

    public void run() {
        computer.run();
    }

    public int getCount(ArcadeTile tile) {
        return Math.toIntExact(Arrays.stream(screen).flatMap(Arrays::stream).filter(tile::equals).count());
    }

    public int getScore() {
        return score.get();
    }
}
