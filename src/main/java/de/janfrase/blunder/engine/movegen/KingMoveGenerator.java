/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.movegen;

import de.janfrase.blunder.engine.movegen.move.Move;
import de.janfrase.blunder.engine.state.board.AttackDecider;
import de.janfrase.blunder.engine.state.board.BoardRepresentation;
import de.janfrase.blunder.engine.state.game.irreversibles.CastlingRights;
import de.janfrase.blunder.utility.Constants;
import java.util.ArrayList;

/**
 * A utility class that provides functionality for generating valid moves for a king in a chess game.
 */
public class KingMoveGenerator {
    protected static final int LONG_CASTLE_X_OFFSET = -2;
    protected static final int SHORT_CASTLE_X_OFFSET = 2;

    protected static void generateKingMoves(
            ArrayList<Move> moves,
            int x,
            int y,
            BoardRepresentation board,
            Constants.Side activeSide,
            CastlingRights castlingRights) {
        // takes care of normal king moves - leaves castles for later
        for (int yOffset = -1; yOffset <= 1; yOffset++) {
            int targetY = y + yOffset;
            // we are above or below the board - nothing to do
            if (targetY < 0 || targetY >= Constants.BOARD_SIDE_LENGTH) continue;

            for (int xOffset = -1; xOffset <= 1; xOffset++) {

                if (xOffset == 0 && yOffset == 0) continue;

                int targetX = x + xOffset;
                // we are left or right of the board - nothing to do
                if (targetX < 0 || targetX >= Constants.BOARD_SIDE_LENGTH) continue;

                Constants.PieceType targetPieceType = board.getPieceAt(targetX, targetY);
                Constants.Side targetPieceSide = board.getSideAt(targetX, targetY);

                // a quiet move since the square is empty
                if (targetPieceSide == Constants.Side.EMPTY) {
                    Move quietMove = new Move(x, y, targetX, targetY);
                    moves.add(quietMove);
                    continue;
                }

                boolean friendlyPiece = activeSide == targetPieceSide;
                // the square is already occupied by a friendly piece - nothing to do
                if (friendlyPiece) {
                    continue;
                }

                // an enemy is on the square - KILL HIM
                Move captureMove = new Move(x, y, targetX, targetY, targetPieceType);
                moves.add(captureMove);
            }
        }

        // let's make some castles. 🏰
        boolean canLongCastle = castlingRights.getLongCastle(activeSide);
        boolean canShortCastle = castlingRights.getShortCastle(activeSide);

        // we can't castle anymore
        if (!(canLongCastle || canShortCastle)) {
            return;
        }

        if (canShortCastle) {
            Constants.Side sideOneRight = board.getSideAt(x + 1, y);
            Constants.Side sideTwoRight = board.getSideAt(x + 2, y);

            if (AttackDecider.isAttacked(x + 1, y, activeSide)
                    || AttackDecider.isAttacked(x + 2, y, activeSide)) return;

            // there is nothing between the king and the rook
            if (sideOneRight == Constants.Side.EMPTY && sideTwoRight == Constants.Side.EMPTY) {
                Move castleMove =
                        new Move(x, y, x + SHORT_CASTLE_X_OFFSET, y, Move.MoveType.SHORT_CASTLE);
                moves.add(castleMove);
            }
        }

        if (canLongCastle) {
            Constants.Side sideOneRight = board.getSideAt(x - 1, y);
            Constants.Side sideTwoRight = board.getSideAt(x - 2, y);
            Constants.Side sideThreeRight = board.getSideAt(x - 3, y);

            if (AttackDecider.isAttacked(x - 1, y, activeSide)
                    || AttackDecider.isAttacked(x - 2, y, activeSide)) return;

            // there is nothing between the king and the rook
            if (sideOneRight == Constants.Side.EMPTY
                    && sideTwoRight == Constants.Side.EMPTY
                    && sideThreeRight == Constants.Side.EMPTY) {
                Move castleMove =
                        new Move(x, y, x + LONG_CASTLE_X_OFFSET, y, Move.MoveType.LONG_CASTLE);
                moves.add(castleMove);
            }
        }
    }
}
