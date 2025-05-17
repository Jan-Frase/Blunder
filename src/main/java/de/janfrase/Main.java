package de.janfrase;

import de.janfrase.core.board.BoardPrinter;
import de.janfrase.core.board.BoardRepresentation;

public class Main {
    public static void main(String[] args) {
        BoardRepresentation boardRepresentation = new BoardRepresentation();
        System.out.println(BoardPrinter.convertBoardToString(boardRepresentation));
    }
}