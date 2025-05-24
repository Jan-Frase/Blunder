package de.janfrase.blunder.engine.state.game;

import de.janfrase.blunder.engine.movegen.Move;
import de.janfrase.blunder.engine.state.board.BoardRepresentation;
import de.janfrase.blunder.engine.state.game.irreversibles.IrreversibleData;
import de.janfrase.blunder.utility.Constants;

import java.util.OptionalInt;
import java.util.Stack;

/**
 * The {@code GameState} class represents the current state of the chess game,
 * including the board, active player, move count, and irreversible data.
 * <p>
 * It uses a singleton pattern to ensure there's only one instance of the game state.
 */
public class GameState {

    private static GameState instance = new GameState();

    final BoardRepresentation boardRepresentation;
    final Stack<IrreversibleData> irreversibleDataStack;
    boolean isWhitesTurn;
    int fullMoveCounter;

    private GameState() {
        this.boardRepresentation = new BoardRepresentation();
        this.irreversibleDataStack = new Stack<>();
        this.isWhitesTurn = true;
        this.fullMoveCounter = 1;
    }


    /**
     * Returns the singleton instance of the {@code GameState} class.
     * The {@code GameState} class represents the current state of a chess game
     * and ensures that only one instance of this class exists at any given time.
     *
     * @return the singleton instance of {@code GameState}
     */
    public static GameState getInstance() {
        return instance;
    }

    /**
     * Resets the game state to its initial condition by reinitializing
     * the {@code GameState} singleton instance.
     * <p>
     * This method ensures the game state is reset to a newly instantiated default configuration, clearing any prior
     * game progress or data.
     * <p>
     * This is mostly here to easily implement unit tests.
     */
    public static void resetGameState() {
        instance = new GameState();
    }

    // ------------------------------
    // FUNCTIONS
    // ------------------------------

    public void makeMove(Move move) {
        // set the square we are moving away from, to empty
        this.boardRepresentation.fillSquare(move.fromX(), move.fromY(), Constants.PieceType.EMPTY, Constants.Side.EMPTY);

        // set the square we are moving to, to have the piece we moved
        Constants.Side fromSide = this.boardRepresentation.getSideAtPosition(move.fromX(), move.fromY());
        Constants.PieceType fromPieceType = this.boardRepresentation.getPieceAtPosition(move.fromX(), move.fromY());

        this.boardRepresentation.fillSquare(move.toX(), move.toY(), fromPieceType, fromSide);

        // if this was blacks turn -> increment full move counter
        if (!this.isWhitesTurn) fullMoveCounter++;

        // the other player can now make his turn
        this.isWhitesTurn = !this.isWhitesTurn;

        // copy the old irreversibleData
        IrreversibleData.Builder irreversibleDataBuilder = new IrreversibleData.Builder().transferFromOldState(irreversibleDataStack.peek());

        // now we can get to the edge cases :)
        if (move.moveType().equals(Move.MoveType.CAPTURE) || fromPieceType.equals(Constants.PieceType.PAWN)) {
            // reset the half-move clock on capture or if a pawn was moved
            irreversibleDataBuilder.halfMoveClock(0);
        }

        if (move.moveType().equals(Move.MoveType.EP_CAPTURE)) {
            // remove the captured pawn from the board
            // TODO: correct y axis value
            this.boardRepresentation.fillSquare(move.toX(), move.toY(), Constants.PieceType.EMPTY, Constants.Side.EMPTY);
        }

        if (move.moveType().equals(Move.MoveType.DOUBLE_PAWN_PUSH)) {
            // set the en passant square
            irreversibleDataBuilder.enPassantX(OptionalInt.of(move.fromX()));
        }
    }

    public void unmakeMove(Move move) {

    }
}
