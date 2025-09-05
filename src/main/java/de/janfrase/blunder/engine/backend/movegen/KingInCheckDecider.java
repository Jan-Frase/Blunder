/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.backend.movegen;

import de.janfrase.blunder.engine.backend.state.board.BoardRepresentation;
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
    public static boolean isKingUnderAttack(Constants.Side sideOfKingToCheck) {
        Optional<int[]> kingPos =
                GameState.getInstance()
                        .getBoardRepresentation()
                        .getPiece(Constants.PieceType.KING, sideOfKingToCheck);

        if (kingPos.isEmpty()) {
            return false;
        }

        int x = kingPos.get()[0];
        int y = kingPos.get()[1];

        return isKingUnderAttack(x, y, sideOfKingToCheck);
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
    public static boolean isKingUnderAttack(int x, int y, Constants.Side sideOfKingToCheck) {
        boolean isAttackByKnight = isAttackedByKnight(x, y, sideOfKingToCheck);

        boolean isAttackedOnDiagonal =
                isAttackedOnLine(x, y, Constants.DIAGONAL_DIRECTIONS, true, sideOfKingToCheck);

        boolean isAttackOnStraight =
                isAttackedOnLine(x, y, Constants.STRAIGHT_DIRECTIONS, false, sideOfKingToCheck);

        return isAttackByKnight | isAttackedOnDiagonal | isAttackOnStraight;
    }

    /**
     * Determines whether a given position is attacked by an opponent's knight.
     *
     * @param x The x-coordinate of the position to check.
     * @param y The y-coordinate of the position to check.
     * @param sideOfKingToCheck The side of the player (WHITE or BLACK) whose perspective is used to check for an attack.
     * @return true if the position is being attacked by an opponent's knight; false otherwise.
     */
    private static boolean isAttackedByKnight(int x, int y, Constants.Side sideOfKingToCheck) {
        BoardRepresentation board = GameState.getInstance().getBoardRepresentation();

        // loop over all knight offsets
        for (int[] dir : Constants.KNIGHT_DIRECTIONS) {
            int xTarget = x + dir[0];
            int yTarget = y + dir[1];

            // position is off the board -> skip
            if (Constants.isOffBoard(xTarget, yTarget)) {
                continue;
            }

            // if the piece we found isn't a knight -> skip
            if (Constants.PieceType.KNIGHT != board.getPieceAt(xTarget, yTarget)) {
                continue;
            }

            // knight is friendly -> skip
            if (sideOfKingToCheck == board.getSideAt(xTarget, yTarget)) {
                continue;
            }

            // has to be an enemy knight :(
            return true;
        }

        return false;
    }

    /**
     * Determines if a specific position on the chessboard is under attack along a straight or diagonal direction.
     *
     * @param x           The x-coordinate (column index) of the position to check.
     * @param y           The y-coordinate (row index) of the position to check.
     * @param dirs        An array of directional vectors to search for attacking pieces.
     * @param diagonal    A boolean specifying if the search is diagonal (true) or straight (false).
     * @param sideOfKingToCheck The side of the player (WHITE or BLACK) whose perspective is used to check for an attack.
     * @return true if the position is being attacked along the specified directions by valid opposing pieces; false otherwise.
     */
    private static boolean isAttackedOnLine(
            int x, int y, int[][] dirs, boolean diagonal, Constants.Side sideOfKingToCheck) {
        BoardRepresentation board = GameState.getInstance().getBoardRepresentation();

        for (int[] dir : dirs) {
            Optional<int[]> optional = board.firstObstacleInDir(x, y, dir);

            // nobody there -> skip
            if (optional.isEmpty()) {
                continue;
            }
            int[] obstacle = optional.get();

            // found obstacle is friendly -> skip
            if (board.getSideAt(obstacle[0], obstacle[1]) == sideOfKingToCheck) {
                continue;
            }

            Constants.PieceType attackingType = board.getPieceAt(obstacle[0], obstacle[1]);

            // found queen or when diagonal -> bishop or when straight -> rook
            if (attackingType == Constants.PieceType.QUEEN
                    || attackingType
                            == (diagonal ? Constants.PieceType.BISHOP : Constants.PieceType.ROOK)) {
                return true;
            }

            // special logic for pawns or king - checks if the distance is not too large
            if ((diagonal && attackingType == Constants.PieceType.PAWN) || attackingType == Constants.PieceType.KING) {
                int xDiff = obstacle[0] - x;
                int yDiff = obstacle[1] - y;

                if (Math.abs(xDiff) != 1) {
                    continue;
                }

                if (sideOfKingToCheck == Constants.Side.WHITE && yDiff == -1
                        || sideOfKingToCheck == Constants.Side.BLACK && yDiff == 1) {
                    return true;
                }
            }
        }

        return false;
    }
}
