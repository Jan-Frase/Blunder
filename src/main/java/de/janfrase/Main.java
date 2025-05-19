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


        gameState = new GameState();
        FenLoader.loadFenString("N4k2/8/2p5/8/5q2/4Q3/1K6/8 w - - 0 1", gameState);
        logger.trace(GameStatePrinter.print(gameState));

        logger.info("Chess engine finished.");
    }
}