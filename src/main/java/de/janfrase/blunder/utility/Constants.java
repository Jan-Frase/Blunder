/* Made by Jan Frase :) */
package de.janfrase.blunder.utility;

public class Constants {
    public static final int BOARD_SIDE_LENGTH = 8;

    public static final int BOARD_TOTAL_SIZE = BOARD_SIDE_LENGTH * BOARD_SIDE_LENGTH;

    public enum PieceType {
        KING,
        QUEEN,
        ROOK,
        BISHOP,
        KNIGHT,
        PAWN,
        EMPTY
    }

    public enum Side {
        WHITE,
        BLACK,
        EMPTY
    }

    /**
     * Not meant for instantiation. Constants only!
     */
    private Constants() {}
}
