package de.janfrase.core.gamestate;

import de.janfrase.core.gamestate.board.BoardRepresentation;

public class GameState {

    BoardRepresentation boardRepresentation;
    // TODO: Add stack with irreversible state information - castling rights, half move clock and more.

    public GameState() {
        this.boardRepresentation = new BoardRepresentation();
    }

    public BoardRepresentation getBoardRepresentation() {
        return boardRepresentation;
    }
}
