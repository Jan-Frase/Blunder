package de.janfrase;

import de.janfrase.core.gamestate.GameState;
import de.janfrase.core.gamestate.utility.FenLoader;
import de.janfrase.core.gamestate.utility.GameStatePrinter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.info("Chess engine started.");

        GameState gameState = new GameState();
        logger.trace(GameStatePrinter.print(gameState));

        FenLoader.loadStartingPosition(gameState);
        logger.trace(GameStatePrinter.print(gameState));

        logger.trace(gameState.getBoardRepresentation().getSquare(0,0));

        logger.info("Chess engine finished.");
    }
}