/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.movegen;

import de.janfrase.blunder.engine.state.board.BoardRepresentation;
import de.janfrase.blunder.engine.state.game.GameState;
import de.janfrase.blunder.engine.state.game.irreversibles.IrreversibleData;
import de.janfrase.blunder.utility.Constants;
import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {

    public static List<Move> generateMoves() {
        ArrayList<Move> moves = new ArrayList<>();

        GameState gameState = GameState.getInstance();
        BoardRepresentation board = gameState.getBoardRepresentation();
        IrreversibleData irreversibleData = gameState.getIrreversibleData();
        Constants.Side activeSide =
                gameState.isWhitesTurn() ? Constants.Side.WHITE : Constants.Side.BLACK;

        for (int y = 0; y < Constants.BOARD_SIDE_LENGTH; y++) {
            for (int x = 0; x < Constants.BOARD_SIDE_LENGTH; x++) {
                Constants.PieceType pieceType = board.getPieceAtPosition(x, y);
                Constants.Side pieceSide = board.getSideAtPosition(x, y);

                // skip empty squares
                if (pieceType == Constants.PieceType.EMPTY) {
                    continue;
                }

                // skip opponents pieces
                if (pieceSide != activeSide) {
                    continue;
                }

                switch (pieceType) {
                    case KING -> KingMoveGenerator.generateKingMoves(
                            moves, x, y, board, activeSide, irreversibleData.castlingRights());
                    case QUEEN -> {}
                    case ROOK -> {}
                    case BISHOP -> {}
                    case KNIGHT -> {}
                    case PAWN -> {}
                    case EMPTY -> throw new IllegalStateException(
                            "Empty piece type should have been skipped");
                }
            }
        }

        return moves;
    }
}
