/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.movegen;

import de.janfrase.blunder.engine.state.board.BoardRepresentation;
import de.janfrase.blunder.utility.Constants;
import java.util.ArrayList;

public class KnightMoveGenerator {

    private static final int[][] directions = {
        {1, 2}, {-1, 2}, {1, -2}, {-1, -2}, {2, 1}, {-2, 1}, {2, -1}, {-2, -1}
    };

    protected static void generateKnightMoves(
            ArrayList<Move> moves,
            int x,
            int y,
            BoardRepresentation board,
            Constants.Side activeSide) {
        for (int[] direction : directions) {
            int currentX = x + direction[0];
            int currentY = y + direction[1];

            // Check if we're still on the board
            if (MoveGenerator.isOffBoard(currentX, currentY)) {
                continue;
            }

            Constants.PieceType pieceAtDestination = board.getPieceAtPosition(currentX, currentY);
            Constants.Side sideAtDestination = board.getSideAtPosition(currentX, currentY);

            // Empty square - add move and continue
            if (pieceAtDestination == Constants.PieceType.EMPTY) {
                moves.add(new Move(x, y, currentX, currentY));
                continue;
            }

            // Enemy piece
            if (sideAtDestination != activeSide) {
                // add capture move and stop in this direction
                moves.add(new Move(x, y, currentX, currentY, pieceAtDestination));
            }
        }
    }
}
