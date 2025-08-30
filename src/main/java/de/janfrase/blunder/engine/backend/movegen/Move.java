/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.backend.movegen;

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
    // PERFORMANCE: This could be squeezed into a single 16 bit type:
    // to and from: 2^6=64 -> 6bits each -> 12 bits
    // promotion piece: 2^2=4 -> 2bits for the 4 different pieces
    // leaves 2 bits for various data: en passant, check, capture ... whatever seems useful

    /**
     * Initializes a new move with the specified starting and ending positions, defaulting
     * to a {@code SpecialMoveType.NORMAL_MOVE} and a {@code Constants.PieceType.EMPTY} for the captured piece.}.
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(xToLetter(fromX))
                .append(yToInverse(fromY))
                .append(xToLetter(toX))
                .append(yToInverse(toY));

        if (moveType == MoveType.BISHOP_PROMOTION
                || moveType == MoveType.KNIGHT_PROMOTION
                || moveType == MoveType.QUEEN_PROMOTION
                || moveType == MoveType.ROOK_PROMOTION) {
            builder.append(promotionToLetter(moveType));
        }

        return builder.toString();
    }

    private static String xToLetter(int x) {
        return switch (x) {
            case 0 -> "a";
            case 1 -> "b";
            case 2 -> "c";
            case 3 -> "d";
            case 4 -> "e";
            case 5 -> "f";
            case 6 -> "g";
            case 7 -> "h";
            default -> throw new IllegalArgumentException("x must be between 0 and 7");
        };
    }

    private static String yToInverse(int y) {
        return switch (y) {
            case 0 -> "8";
            case 1 -> "7";
            case 2 -> "6";
            case 3 -> "5";
            case 4 -> "4";
            case 5 -> "3";
            case 6 -> "2";
            case 7 -> "1";
            default -> throw new IllegalStateException("Unexpected value: " + y);
        };
    }

    private static String promotionToLetter(MoveType moveType) {
        return switch (moveType) {
            case BISHOP_PROMOTION -> "b";
            case KNIGHT_PROMOTION -> "n";
            case QUEEN_PROMOTION -> "q";
            case ROOK_PROMOTION -> "r";
            default -> throw new IllegalArgumentException("moveType must be a promotion move type");
        };
    }

    /**
     * Based on this <a href="https://www.chessprogramming.org/Encoding_Moves#From-To_Based">chessprogramming</a> page.
     */
    public enum MoveType {
        NORMAL_MOVE, // A quiet move OR if capturedPieceType is set -> a capture move.

        EP_CAPTURE, // When an En Passant happens, do NOT store the captured piece but only the
        // special move type.

        DOUBLE_PAWN_PUSH,

        SHORT_CASTLE,
        LONG_CASTLE,

        ROOK_PROMOTION,
        BISHOP_PROMOTION,
        KNIGHT_PROMOTION,
        QUEEN_PROMOTION,
    }
}
