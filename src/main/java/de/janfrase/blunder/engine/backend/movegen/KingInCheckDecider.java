/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.backend.movegen;

import de.janfrase.blunder.engine.backend.Piece;
import de.janfrase.blunder.engine.backend.state.board.BitBoard;
import de.janfrase.blunder.engine.backend.state.board.BitBoards;
import de.janfrase.blunder.engine.backend.state.game.GameState;
import de.janfrase.blunder.utility.Constants;
import java.util.Optional;

public class KingInCheckDecider {

    /**
     * Determines whether a king at the specified position is under attack.
     * This only works for the king because it does not look for pins!
     * The king can be killed by pinned pieces.
     * <p>
     * Furthermore, the king does not have to actually stand on the specified location.
     * This is needed to check if the king moves through check during castling.
     *
     * @return true if the king is under attack by any opponent piece; false otherwise.
     */
    public static boolean isKingUnderAttack(byte kingSide) {
        BitBoard kingBoard =
                GameState.getInstance().getBitBoards().getBitBoard(new Piece(Piece.KING, kingSide));

        if (kingBoard.isEmpty()) {
            throw new IllegalStateException("King position is empty");
        }

        int[] kingPos = kingBoard.getKingPosition();

        return isKingUnderAttack(kingPos, kingSide);
    }

    /**
     * Determines whether a king at the specified position is under attack.
     * This only works for the king because it does not look for pins!
     * The king can be killed by pinned pieces.
     * <p>
     * Furthermore, the king does not have to actually stand on the specified location.
     * This is needed to check if the king moves through check during castling.
     *
     * @return true if the king is under attack by any opponent piece; false otherwise.
     */
    public static boolean isKingUnderAttack(int[] kingPos, byte kingPiece) {
        boolean isAttackByKnight = isAttackedByKnight(kingPos, kingPiece);

        boolean isAttackedOnDiagonal =
                isAttackedOnLine(kingPos, Constants.DIAGONAL_DIRECTIONS, true, kingPiece);

        boolean isAttackOnStraight =
                isAttackedOnLine(kingPos, Constants.STRAIGHT_DIRECTIONS, false, kingPiece);

        return isAttackByKnight | isAttackedOnDiagonal | isAttackOnStraight;
    }

    /**
     * Determines whether a given position is attacked by an opponent's knight.
     *
     * @param kingPos The coordinate of the position to check.
     * @param kingSide The side of the player (WHITE or BLACK) whose perspective is used to check for an attack.
     * @return true if the position is being attacked by an opponent's knight; false otherwise.
     */
    private static boolean isAttackedByKnight(int[] kingPos, byte kingSide) {
        BitBoards board = GameState.getInstance().getBitBoards();

        // loop over all knight offsets
        for (int[] dir : Constants.KNIGHT_DIRECTIONS) {
            int xTarget = kingPos[0] + dir[0];
            int yTarget = kingPos[1] + dir[1];

            // position is off the board -> skip
            if (Constants.isOffBoard(xTarget, yTarget)) {
                continue;
            }

            // What an enemy knight would look like.
            Piece enemyKnight = new Piece(Piece.KNIGHT, Piece.getEnemySide(kingSide));

            // Get the piece at the relevant location.
            Piece pieceAtPosition = board.getPieceAt(xTarget, yTarget);

            boolean areTheyEqual = enemyKnight.value == pieceAtPosition.value;

            if (!areTheyEqual) {
                continue;
            }

            // if they are equal -> we are getting attacked
            return true;
        }

        return false;
    }

    /**
     * Determines if a specific position on the chessboard is under attack along a straight or diagonal direction.
     *
     * @param kingPos           The coordinate of the position to check.
     * @param dirs        An array of directional vectors to search for attacking pieces.
     * @param diagonal    A boolean specifying if the search is diagonal (true) or straight (false).
     * @param kingSide The side of the player (WHITE or BLACK) whose perspective is used to check for an attack.
     * @return true if the position is being attacked along the specified directions by valid opposing pieces; false otherwise.
     */
    private static boolean isAttackedOnLine(
            int[] kingPos, int[][] dirs, boolean diagonal, byte kingSide) {
        BitBoards board = GameState.getInstance().getBitBoards();

        for (int[] dir : dirs) {
            Optional<int[]> optional = board.firstObstacleInDir(kingPos[0], kingPos[1], dir);

            // nobody there -> skip
            if (optional.isEmpty()) {
                continue;
            }
            int[] obstacle = optional.get();

            // found obstacle is friendly -> skip
            if (board.getPieceAt(obstacle[0], obstacle[1]).getSide() == kingSide) {
                continue;
            }

            byte attackingPieceType = board.getPieceAt(obstacle[0], obstacle[1]).getType();

            // found queen
            if (attackingPieceType == Piece.QUEEN) {
                return true;
            }

            // or when diagonal -> bishop or when straight -> rook
            if (attackingPieceType == (diagonal ? Piece.BISHOP : Piece.ROOK)) {
                return true;
            }

            // special logic for pawns - checks if the distance is not too large
            if (diagonal && attackingPieceType == Piece.PAWN) {
                int xDiff = obstacle[0] - kingPos[0];
                int yDiff = obstacle[1] - kingPos[1];

                if (Math.abs(xDiff) != 1) {
                    continue;
                }

                if (kingSide == Piece.WHITE && yDiff == -1
                        || kingSide == Piece.BLACK && yDiff == 1) {
                    return true;
                }
            }

            // special logic for kings - checks if the distance is not too large
            if (attackingPieceType == Piece.KING) {
                int xDiff = obstacle[0] - kingPos[0];
                int yDiff = obstacle[1] - kingPos[1];

                if (Math.abs(xDiff) <= 1 && Math.abs(yDiff) <= 1) {
                    return true;
                }
            }
        }

        return false;
    }
}
