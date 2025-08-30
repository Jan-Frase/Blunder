/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.backend.movegen;

import de.janfrase.blunder.engine.backend.state.board.BoardRepresentation;
import de.janfrase.blunder.utility.Constants;
import java.util.ArrayList;

/**
 * A utility class that provides functionality for generating valid moves for a bishop, rook and queen in a chess game.
 */
public class LineMoveGenerator {

    private static void generateLineMoves(
            ArrayList<Move> moves,
            int x,
            int y,
            BoardRepresentation board,
            Constants.Side activeSide,
            int[][] directions) {
        for (int[] direction : directions) {
            int currentX = x;
            int currentY = y;

            while (true) {
                currentX += direction[0];
                currentY += direction[1];

                // Check if we're still on the board
                if (Constants.isOffBoard(currentX, currentY)) {
                    break;
                }

                Constants.PieceType pieceAtDestination = board.getPieceAt(currentX, currentY);
                Constants.Side sideAtDestination = board.getSideAt(currentX, currentY);

                // Empty square - add move and continue
                if (pieceAtDestination == Constants.PieceType.EMPTY) {
                    moves.add(new Move(x, y, currentX, currentY));
                    continue;
                }

                // Friendly piece - stop in this direction
                if (sideAtDestination == activeSide) {
                    break;
                }
                // Enemy piece - add capture move and stop in this direction
                moves.add(new Move(x, y, currentX, currentY, pieceAtDestination));
                break;
            }
        }
    }

    protected static void generateDiagonalMoves(
            ArrayList<Move> moves,
            int x,
            int y,
            BoardRepresentation board,
            Constants.Side activeSide) {
        generateLineMoves(moves, x, y, board, activeSide, Constants.DIAGONAL_DIRECTIONS);
    }

    protected static void generateStraightMoves(
            ArrayList<Move> moves,
            int x,
            int y,
            BoardRepresentation board,
            Constants.Side activeSide) {
        generateLineMoves(moves, x, y, board, activeSide, Constants.STRAIGHT_DIRECTIONS);
    }
}
