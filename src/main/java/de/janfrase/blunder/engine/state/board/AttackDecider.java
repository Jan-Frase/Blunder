/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.state.board;

import de.janfrase.blunder.engine.state.game.GameState;
import de.janfrase.blunder.utility.Constants;
import java.util.Optional;

public class AttackDecider {

    public static boolean isAttacked(int x, int y, Constants.Side friendlySide) {
        boolean isAttackByKnight = isAttackedByKnight(x, y, friendlySide);
        boolean isAttackedOnDiagonal =
                isAttackedOnLine(x, y, Constants.DIAGONAL_DIRECTIONS, true, friendlySide);
        boolean isAttackOnStraight =
                isAttackedOnLine(x, y, Constants.STRAIGHT_DIRECTIONS, false, friendlySide);

        return isAttackByKnight | isAttackedOnDiagonal | isAttackOnStraight;
    }

    private static boolean isAttackedByKnight(int x, int y, Constants.Side friendlySide) {
        BoardRepresentation board = GameState.getInstance().getBoardRepresentation();

        for (int[] dir : Constants.KNIGHT_DIRECTIONS) {
            int xTarget = x + dir[0];
            int yTarget = y + dir[1];

            if (Constants.isOffBoard(xTarget, yTarget)) {
                continue;
            }

            if (Constants.PieceType.KNIGHT != board.getPieceAt(xTarget, yTarget)) {
                continue;
            }

            if (friendlySide == board.getSideAt(xTarget, yTarget)) {
                continue;
            }

            if (PinDecider.isPinned(xTarget, yTarget)) {
                continue;
            }

            return true;
        }

        return false;
    }

    private static boolean isAttackedOnLine(
            int x, int y, int[][] dirs, boolean diagonal, Constants.Side friendlySide) {
        BoardRepresentation board = GameState.getInstance().getBoardRepresentation();

        for (int[] dir : dirs) {
            Optional<int[]> optional = board.firstObstacleInDir(x, y, dir);

            // nobody there
            if (optional.isEmpty()) {
                continue;
            }
            int[] obstacle = optional.get();

            // found obstacle is friendly
            if (board.getSideAt(obstacle[0], obstacle[1]) == friendlySide) {
                continue;
            }

            Constants.PieceType attackingType = board.getPieceAt(obstacle[0], obstacle[1]);

            if (PinDecider.isPinned(obstacle[0], obstacle[1])) {
                continue;
            }

            // queen and when diagonal -> bishop or when straight -> rook
            if (attackingType == Constants.PieceType.QUEEN
                    || attackingType
                            == (diagonal ? Constants.PieceType.BISHOP : Constants.PieceType.ROOK)) {
                return true;
            }

            if (diagonal && attackingType == Constants.PieceType.PAWN) {
                int xDiff = obstacle[0] - x;
                int yDiff = obstacle[1] - y;

                if (Math.abs(xDiff) != 1) {
                    continue;
                }

                if (friendlySide == Constants.Side.WHITE && yDiff == -1
                        || friendlySide == Constants.Side.BLACK && yDiff == 1) {
                    return true;
                }
            }
        }

        return false;
    }
}
