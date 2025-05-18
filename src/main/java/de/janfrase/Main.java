package de.janfrase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.janfrase.core.board.BoardPrinter;
import de.janfrase.core.board.BoardRepresentation;

public class Main {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.info("Chess engine started.");

        BoardRepresentation boardRepresentation = new BoardRepresentation();
        logger.trace(BoardPrinter.convertBoardToString(boardRepresentation));

        logger.info("Chess engine finished.");
    }
}