package io.github.ititus.aoc.aoc20.day11;

public enum SeatState {

    FLOOR((byte) 0, '.'),
    EMPTY((byte) 1, 'L'),
    OCCUPIED((byte) 2, '#'),
    OUT_OF_MAP((byte) -1, ' ');

    private final byte id;
    private final char printChar;

    SeatState(byte id, char printChar) {
        this.id = id;
        this.printChar = printChar;
    }

    public static SeatState of(byte id) {
        return switch (id) {
            case 0 -> FLOOR;
            case 1 -> EMPTY;
            case 2 -> OCCUPIED;
            default -> null;
        };
    }

    public static SeatState of(char c) {
        return switch (c) {
            case '.' -> FLOOR;
            case 'L' -> EMPTY;
            case '#' -> OCCUPIED;
            default -> null;
        };
    }

    public byte getId() {
        return id;
    }

    public char getPrintChar() {
        return printChar;
    }
}
