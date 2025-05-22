package de.janfrase.blunder.engine.gamestate;

import de.janfrase.blunder.engine.gamestate.board.BoardRepresentation;
import de.janfrase.blunder.engine.movegen.Move;
import de.janfrase.blunder.utility.Constants;

import java.util.OptionalInt;
import java.util.Stack;

public class GameState {

    private final BoardRepresentation boardRepresentation;
    private final Stack<IrreversibleData> irreversibleData;
    private boolean isWhitesTurn;
    private int fullMoveCounter;

    public GameState() {
        this.boardRepresentation = new BoardRepresentation();
        this.irreversibleData = new Stack<>();
        this.isWhitesTurn = true;
        this.fullMoveCounter = 1;
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
        IrreversibleData.Builder irreversibleDataBuilder = new IrreversibleData.Builder().transferFromOldState(irreversibleData.peek());

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

    // ------------------------------
    // GETTERS AND SETTERS
    // ------------------------------

    public BoardRepresentation getBoardRepresentation() {
        return boardRepresentation;
    }

    public Stack<IrreversibleData> getIrreversibleDataStack() {
        return irreversibleData;
    }

    public boolean isWhitesTurn() {
        return isWhitesTurn;
    }

    public int getFullMoveCounter() {
        return fullMoveCounter;
    }

    public void setFullMoveCounter(int fullMoveCounter) {
        this.fullMoveCounter = fullMoveCounter;
    }

    public void setIsWhitesTurn(boolean isWhitesTurn) {
        this.isWhitesTurn = isWhitesTurn;
    }

}
