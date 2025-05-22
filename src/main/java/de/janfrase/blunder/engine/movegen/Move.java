package de.janfrase.blunder.engine.movegen;

/**
 * Represents a move in a chess game. A move consists of a starting position
 * and an ending position, both represented by x and y coordinates on the board.
 * It also includes an optional special move type such as castling or promotion.
 * <p>
 * This class is immutable and thread-safe.
 *
 * @param fromX           The starting x-coordinate of the move, specifying the column position (0-based).
 * @param fromY           The starting y-coordinate of the move, specifying the row position (0-based).
 * @param toX             The ending x-coordinate of the move, specifying the column position (0-based).
 * @param toY             The ending y-coordinate of the move, specifying the row position (0-based).
 * @param moveType The type of special move, represented by {@code SpecialMoveType}.
 */
public record Move(int fromX, int fromY, int toX, int toY, MoveType moveType) {


    /**
     * Initializes a new move with the specified starting and ending positions, defaulting
     * to a {@code SpecialMoveType.QUIET_MOVE}.
     *
     * @param fromX The starting x-coordinate of the move, specifying the column position (0-based).
     * @param fromY The starting y-coordinate of the move, specifying the row position (0-based).
     * @param toX   The ending x-coordinate of the move, specifying the column position (0-based).
     * @param toY   The ending y-coordinate of the move, specifying the row position (0-based).
     */
    public Move(int fromX, int fromY, int toX, int toY) {
        this(fromX, fromY, toX, toY, MoveType.QUIET_MOVE);
    }


    /**
     * Based on this <a href="https://www.chessprogramming.org/Encoding_Moves#From-To_Based">chessprogramming</a> page.
     */
    public enum MoveType {
        QUIET_MOVE,

        CAPTURE, EP_CAPTURE,

        DOUBLE_PAWN_PUSH,

        KING_CASTLE, QUEEN_CASTLE,

        ROOK_PROMOTION, ROOK_PROMOTION_CAPTURE, BISHOP_PROMOTION, BISHOP_PROMOTION_CAPTURE, KNIGHT_PROMOTION, KNIGHT_PROMOTION_CAPTURE, QUEEN_PROMOTION, QUEEN_PROMOTION_CAPTURE,
    }
}
