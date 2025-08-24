/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.backend.movegen;

import de.janfrase.blunder.engine.backend.movegen.move.Move;
import de.janfrase.blunder.engine.backend.state.board.BoardRepresentation;
import de.janfrase.blunder.engine.backend.state.game.GameState;
import de.janfrase.blunder.engine.backend.state.game.irreversibles.IrreversibleData;
import de.janfrase.blunder.utility.Constants;
import java.util.ArrayList;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The MoveGenerator class is responsible for generating both pseudo-legal
 * and legal moves for the active player in the current game state.
 */
public class MoveGenerator {

    // PERFORMANCE:
    /*
     * 1. One could implement multi-threading.
     * 2. When using something like alpha-beta pruning, it might happen that we generate all the moves for a given position and then notice after the first one that we don't even need to consider the others.
     *       In that case it could be useful to only generate the moves lazily when needed. However, this might conflict with move ordering etc.
     */

    private static final Logger logger = LogManager.getLogger(MoveGenerator.class);

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
    public static ArrayList<Move> generatePseudoLegalMoves() {
        logger.trace("Starting move generation");
        ArrayList<Move> moves = new ArrayList<>();

        GameState gameState = GameState.getInstance();
        BoardRepresentation board = gameState.getBoardRepresentation();
        IrreversibleData irreversibleData = gameState.getIrreversibleData();
        Constants.Side activeSide = gameState.getFriendlySide();

        for (int y = 0; y < Constants.BOARD_SIDE_LENGTH; y++) {
            for (int x = 0; x < Constants.BOARD_SIDE_LENGTH; x++) {
                Constants.PieceType pieceType = board.getPieceAt(x, y);
                Constants.Side pieceSide = board.getSideAt(x, y);

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
                    case KNIGHT -> KnightMoveGenerator.generateKnightMoves(
                            moves, x, y, board, activeSide);
                    case PAWN -> PawnMoveGenerator.generatePawnMove(
                            moves, x, y, board, activeSide, irreversibleData.enPassantX());
                    case EMPTY -> throw new IllegalStateException(
                            "Empty piece type should have been skipped");
                }
            }
        }

        logger.trace("Finished move generation");
        return moves;
    }

    /**
     * Generates a list of all possible legal moves for the active side
     * in the current game state.
     * <p>
     * This works by first generating all pseudo legal moves,
     * then making each one and checking if the opponent can capture our king.
     * <p>
     * This needs one make and one unmake causing the method to be rather slow.
     *
     * @return a list of {@code Move} objects representing all legal moves
     *         for the active player in the current game state.
     */
    public static ArrayList<Move> generateLegalMoves() {
        ArrayList<Move> pseudoLegalMoves = MoveGenerator.generatePseudoLegalMoves();
        ArrayList<Move> legalMoves = new ArrayList<>();

        for (Move move : pseudoLegalMoves) {
            GameState.getInstance().makeMove(move);
            if (!canCaptureKing()) {
                legalMoves.add(move);
            }
            GameState.getInstance().unmakeMove(move);
        }
        return legalMoves;
    }

    public static boolean canCaptureKing() {
        Optional<int[]> kingPos =
                GameState.getInstance()
                        .getBoardRepresentation()
                        .getPiece(Constants.PieceType.KING, GameState.getInstance().getEnemySide());

        if (kingPos.isEmpty()) {
            return false;
        }

        // TODO: Refactor this so that the KingInCheckDecier finds the king position instead of
        // doing it here.
        return KingInCheckDecider.isKingUnderAttack(
                kingPos.get()[0], kingPos.get()[1], GameState.getInstance().getEnemySide());
    }
}
