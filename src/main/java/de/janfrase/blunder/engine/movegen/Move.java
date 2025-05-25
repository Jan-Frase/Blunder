/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.movegen;

import de.janfrase.blunder.utility.Constants;

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
 * @param moveType          The type of special move, represented by {@code SpecialMoveType}.
 * @param capturedPieceType The type of the captured piece, EMPTY if nothing was captured.
 */
public record Move(
        int fromX,
        int fromY,
        int toX,
        int toY,
        MoveType moveType,
        Constants.PieceType capturedPieceType) {

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
        this(fromX, fromY, toX, toY, MoveType.NORMAL_MOVE, Constants.PieceType.EMPTY);
    }

    /**
     * Initializes a new move with the specified starting and ending positions, defaulting
     * to a {@code Constants.PieceType.EMPTY} for the captured piece.
     *
     * @param fromX The starting x-coordinate of the move, specifying the column position (0-based).
     * @param fromY The starting y-coordinate of the move, specifying the row position (0-based).
     * @param toX   The ending x-coordinate of the move, specifying the column position (0-based).
     * @param toY   The ending y-coordinate of the move, specifying the row position (0-based).
     */
    public Move(int fromX, int fromY, int toX, int toY, MoveType moveType) {
        this(fromX, fromY, toX, toY, moveType, Constants.PieceType.EMPTY);
    }

    /**
     * Initializes a new move with the specified starting and ending positions, defaulting
     * to a {@code Constants.PieceType.EMPTY} for the captured piece.
     *
     * @param fromX The starting x-coordinate of the move, specifying the column position (0-based).
     * @param fromY The starting y-coordinate of the move, specifying the row position (0-based).
     * @param toX   The ending x-coordinate of the move, specifying the column position (0-based).
     * @param toY   The ending y-coordinate of the move, specifying the row position (0-based).
     */
    public Move(int fromX, int fromY, int toX, int toY, Constants.PieceType capturedPieceType) {
        this(fromX, fromY, toX, toY, MoveType.NORMAL_MOVE, capturedPieceType);
    }

    /**
     * Based on this <a href="https://www.chessprogramming.org/Encoding_Moves#From-To_Based">chessprogramming</a> page.
     */
    public enum MoveType {
        NORMAL_MOVE,

        EP_CAPTURE, // Technically not needed since we also store the captured piece type.

        DOUBLE_PAWN_PUSH,

        SHORT_CASTLE,
        LONG_CASTLE,

        ROOK_PROMOTION,
        BISHOP_PROMOTION,
        KNIGHT_PROMOTION,
        QUEEN_PROMOTION,
    }
}
