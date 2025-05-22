package de.janfrase.blunder.engine.gamestate;

import de.janfrase.blunder.engine.gamestate.board.BoardRepresentation;

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

    public BoardRepresentation getBoardRepresentation() {
        return boardRepresentation;
    }

    public Stack<IrreversibleData> getIrreversibleDataStack() {
        return irreversibleData;
    }

    public boolean isWhitesTurn(){
        return isWhitesTurn;
    }

    public int getFullMoveCounter(){
        return fullMoveCounter;
    }

    public void setIsWhitesTurn(boolean isWhitesTurn){
        this.isWhitesTurn = isWhitesTurn;
    }

    public void setFullMoveCounter(int fullMoveCounter) {
        this.fullMoveCounter = fullMoveCounter;
    }

}
