package de.janfrase.blunder.utility;

public class Constants {
    public static final int BOARD_WIDTH = 8;

    public static final int BOARD_TOTAL_SIZE = BOARD_WIDTH * BOARD_WIDTH;

    public enum PieceType {
        KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN, EMPTY
    }

    public enum Side {
        WHITE, BLACK, EMPTY
    }

    /**
     * Not meant for instantiation. Constants only!
     */
    private Constants() {
    }
}