/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.backend;

public class Piece {
    public static final byte EMPTY = 0;

    public static final byte WHITE = 0;
    public static final byte BLACK = 1;

    public static final byte PAWN = 2;
    public static final byte ROOK = 4;
    public static final byte KNIGHT = 8;
    public static final byte BISHOP = 16;
    public static final byte QUEEN = 32;
    public static final byte KING = 64;

    public byte value;

    public Piece(byte type, byte side) {
        this.value = (byte) (type | side);
    }

    public static Piece createEmptyPiece() {
        return new Piece(EMPTY, EMPTY);
    }

    public byte getType() {
        return (byte) (value & ~BLACK);
    }

    public byte getSide() {
        return (byte) (value & BLACK);
    }

    public byte getEnemySide() {
        return getEnemySide(getSide());
    }

    public boolean isEmpty() {
        return value == EMPTY;
    }

    public static byte getEnemySide(byte friendlySide) {
        return (byte) (friendlySide ^ BLACK);
    }
}
