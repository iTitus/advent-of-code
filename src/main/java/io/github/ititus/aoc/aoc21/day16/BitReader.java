package io.github.ititus.aoc.aoc21.day16;

import java.util.HexFormat;

public class BitReader {

    private final String s;
    private final int maxPos;
    private int pos;
    private int bitPos;

    public BitReader(String s) {
        this.s = s;
        this.maxPos = s.length();
        this.pos = 0;
        this.bitPos = 0;
    }

    public int readBits(int n) {
        if (n <= 0 || n >= 32) {
            throw new IllegalArgumentException();
        }

        int value = 0;
        for (int i = 0; i < n; ) {
            int bits4 = HexFormat.fromHexDigit(s.charAt(pos));
            int bitsToTake = Math.min(n - i, 4 - bitPos);
            value <<= bitsToTake;
            value |= (bits4 >>> (4 - bitPos - bitsToTake)) & ((1 << bitsToTake) - 1);
            i += bitsToTake;
            bitPos += bitsToTake;
            pos += bitPos / 4;
            bitPos %= 4;

            if (pos > maxPos || pos == maxPos && bitPos > 0) {
                throw new RuntimeException();
            }
        }

        return value;
    }
}
