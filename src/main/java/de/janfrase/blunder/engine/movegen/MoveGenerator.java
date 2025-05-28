/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.movegen;

import de.janfrase.blunder.engine.state.board.BoardRepresentation;
import de.janfrase.blunder.engine.state.game.GameState;
import de.janfrase.blunder.engine.state.game.irreversibles.IrreversibleData;
import de.janfrase.blunder.utility.Constants;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MoveGenerator {

    private static final Logger logger = LogManager.getLogger();

    /**
     * Generates a list of all possible PSEUDO-legal moves for the active side
     * in the current game state.
     * The method considers the positions
     * and types of pieces, the board configuration, and specific rules
     * such as castling rights.
     *
     * @return a list of {@code Move} objects representing all legal moves
     *         for the active player in the current game state.
     */
    public static List<Move> generateMoves() {
        logger.trace("Starting move generation");
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
                    case QUEEN -> {
                        LineMoveGenerator.generateStraightMoves(moves, x, y, board, activeSide);
                        LineMoveGenerator.generateDiagonalMoves(moves, x, y, board, activeSide);
                    }
                    case ROOK -> LineMoveGenerator.generateStraightMoves(
                            moves, x, y, board, activeSide);
                    case BISHOP -> LineMoveGenerator.generateDiagonalMoves(
                            moves, x, y, board, activeSide);
                    case KNIGHT -> {}
                    case PAWN -> {}
                    case EMPTY -> throw new IllegalStateException(
                            "Empty piece type should have been skipped");
                }
            }
        }

        logger.trace("Finished move generation");
        return moves;
    }
}
