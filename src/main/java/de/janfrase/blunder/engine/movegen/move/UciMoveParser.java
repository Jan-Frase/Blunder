/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.movegen.move;

import de.janfrase.blunder.engine.state.game.GameState;
import de.janfrase.blunder.utility.Constants;

/**
 * This class can turn our moves into UCI notation or get an UCI notation String and turn it into a move record.
 */
public class UciMoveParser {

    /**
     * Parses a move in UCI (Universal Chess Interface) notation into an array of {@link Move} objects.
     * The method analyzes the UCI string to derive the starting position, target position,
     * and any special moves such as castling, promotions, or en passant captures.
     *
     * @param uciMove the UCI notation representation of the move, formatted as a string of four characters
     *                (e.g., "e2e4" for moving a piece from e2 to e4).
     * @return an array of {@link Move} objects describing the possible moves derived from the given UCI notation.
     * The array might include one or more moves in the case of ambiguous results (e.g., promotions).
     */
    public static Move parseUciMove(String uciMove) {
        int fromX = uciMove.charAt(0) - 'a';
        int fromY = Constants.BOARD_SIDE_LENGTH - (uciMove.charAt(1) - '1') - 1;

        int toX = uciMove.charAt(2) - 'a';
        int toY = Constants.BOARD_SIDE_LENGTH - (uciMove.charAt(3) - '1') - 1;

        Constants.PieceType movingPieceType =
                GameState.getInstance().getBoardRepresentation().getPieceAt(fromX, fromY);

        Constants.PieceType capturedPieceType =
                GameState.getInstance().getBoardRepresentation().getPieceAt(toX, toY);

        if (movingPieceType == Constants.PieceType.PAWN) {
            // we are moving 2 squares -> double pawn push
            if (Math.abs(fromY - toY) == 2) {
                return new Move(fromX, fromY, toX, toY, Move.MoveType.DOUBLE_PAWN_PUSH);
            }
            // en passant capture?
            if (fromX != toX && capturedPieceType == Constants.PieceType.EMPTY) {
                return new Move(fromX, fromY, toX, toY, Move.MoveType.EP_CAPTURE);
            }

            // we are promoting!
            if (toY == 0 || toY == Constants.BOARD_SIDE_LENGTH - 1) {
                return switch (uciMove.charAt(4)) {
                    case 'q', 'Q' -> new Move(
                            fromX,
                            fromY,
                            toX,
                            toY,
                            Move.MoveType.QUEEN_PROMOTION,
                            capturedPieceType);

                    case 'r', 'R' -> new Move(
                            fromX,
                            fromY,
                            toX,
                            toY,
                            Move.MoveType.ROOK_PROMOTION,
                            capturedPieceType);

                    case 'b', 'B' -> new Move(
                            fromX,
                            fromY,
                            toX,
                            toY,
                            Move.MoveType.BISHOP_PROMOTION,
                            capturedPieceType);

                    case 'n', 'N' -> new Move(
                            fromX,
                            fromY,
                            toX,
                            toY,
                            Move.MoveType.KNIGHT_PROMOTION,
                            capturedPieceType);

                    default -> throw new IllegalStateException(
                            "Unexpected value: " + uciMove.charAt(4));
                };
            }
        }

        if (movingPieceType == Constants.PieceType.KING) {
            // we are moving 2 squares -> castle
            if (Math.abs(fromX - toX) == 2) {
                int dir = fromX - toX;
                // short castle
                if (dir > 0) {
                    return new Move(fromX, fromY, toX, toY, Move.MoveType.LONG_CASTLE);
                } else {
                    return new Move(fromX, fromY, toX, toY, Move.MoveType.SHORT_CASTLE);
                }
            }
        }

        if (capturedPieceType != Constants.PieceType.EMPTY) {
            return new Move(fromX, fromY, toX, toY, capturedPieceType);
        }
        return new Move(fromX, fromY, toX, toY);
    }
}
