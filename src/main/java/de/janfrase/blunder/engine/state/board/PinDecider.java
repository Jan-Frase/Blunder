/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.state.board;

import de.janfrase.blunder.engine.state.game.GameState;
import de.janfrase.blunder.utility.Constants;
import java.util.ArrayList;
import java.util.Optional;
import java.util.OptionalInt;

public class PinDecider {

    protected static boolean isPinned(int x, int y) {
        int[][][] straights = {{{1, 0}, {-1, 0}}, {{0, 1}, {0, -1}}};
        int[][][] diagonals = {{{1, 1}, {-1, -1}}, {{1, -1}, {-1, 1}}};

        boolean pinnedStraight = isPinned(x, y, straights, Constants.PieceType.ROOK);
        boolean pinnedDiagonal = isPinned(x, y, diagonals, Constants.PieceType.BISHOP);

        return pinnedStraight || pinnedDiagonal;
    }

    private static boolean isPinned(
            int x, int y, int[][][] directions, Constants.PieceType pinningPiece) {
        // Could be improved by looking up the king position over a secondary piece-centric board
        // representation.
        BoardRepresentation board = GameState.getInstance().getBoardRepresentation();

        for (int[][] dir_pair : directions) {
            ArrayList<Optional<int[]>> obstacles = new ArrayList<>(2);
            obstacles.add(board.firstObstacleInDir(x, y, dir_pair[0]));
            obstacles.add(board.firstObstacleInDir(x, y, dir_pair[1]));

            // cant be pinned if we are running of the board in one direction
            if (obstacles.stream().anyMatch(Optional::isEmpty)) {
                continue;
            }

            // get the pieces and their sides in both directions
            Constants.PieceType[] pieces =
                    obstacles.stream()
                            .map(
                                    obstacle ->
                                            board.getPieceAt(
                                                    obstacle.orElseThrow()[0],
                                                    obstacle.orElseThrow()[1]))
                            .toArray(Constants.PieceType[]::new);

            Constants.Side[] sides =
                    obstacles.stream()
                            .map(
                                    obstacle ->
                                            board.getSideAt(
                                                    obstacle.orElseThrow()[0],
                                                    obstacle.orElseThrow()[1]))
                            .toArray(Constants.Side[]::new);

            boolean friendlyKingPresent = false;
            OptionalInt enemyIndex = OptionalInt.empty();

            Constants.Side activeSide = board.getSideAt(x, y);
            // check if our king and en enemy is present
            for (int i = 0; i < sides.length; i++) {
                if (pieces[i] == Constants.PieceType.KING && sides[i] == activeSide)
                    friendlyKingPresent = true;
                if (sides[i] != activeSide) enemyIndex = OptionalInt.of(i);
            }

            // cant be pinned if the king isn't their
            if (!friendlyKingPresent) continue;

            // can't be pinned without an enemy
            if (enemyIndex.isEmpty()) continue;

            Constants.PieceType enemyPiece = pieces[enemyIndex.getAsInt()];

            // check if the enemy can pin - aka bishop / queen for diagonals and rook / queen for
            // straights
            if (enemyPiece == pinningPiece || enemyPiece == Constants.PieceType.QUEEN) return true;
        }

        return false;
    }
}
