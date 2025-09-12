/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.backend;

public class Piece {
    public static final byte EMPTY = 0;

    public static final byte IS_WHITE = 0;
    public static final byte IS_BLACK = 1;

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
}
