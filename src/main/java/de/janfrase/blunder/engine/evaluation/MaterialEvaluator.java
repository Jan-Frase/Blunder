/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.evaluation;

import de.janfrase.blunder.engine.backend.state.board.BoardRepresentation;
import de.janfrase.blunder.engine.backend.state.game.GameState;
import de.janfrase.blunder.utility.Constants;

public class MaterialEvaluator {

    public static float calculateEvaluation(GameState gameState) {
        int friendlyMaterial = 0;
        int enemyMaterial = 0;

        BoardRepresentation board = gameState.getBoardRepresentation();

        for (int y = 0; y < Constants.BOARD_SIDE_LENGTH; y++) {
            for (int x = 0; x < Constants.BOARD_SIDE_LENGTH; x++) {
                Constants.PieceType piece = board.getPieceAt(x, y);
                Constants.Side side = board.getSideAt(x, y);

                if (side == Constants.Side.EMPTY) continue;

                if (side == gameState.getFriendlySide()) {
                    friendlyMaterial += getMaterialValue(piece);
                } else {
                    enemyMaterial += getMaterialValue(piece);
                }
            }
        }

        return friendlyMaterial - enemyMaterial;
    }

    private static int getMaterialValue(Constants.PieceType pieceType) {
        return switch (pieceType) {
            case KING -> 100;
            case QUEEN -> 9;
            case ROOK -> 5;
            case BISHOP, KNIGHT -> 3;
            case PAWN -> 1;
            case EMPTY -> 0;
        };
    }
}
