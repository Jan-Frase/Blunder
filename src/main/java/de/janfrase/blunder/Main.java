/* Made by Jan Frase :) */
package de.janfrase.blunder;

import de.janfrase.blunder.engine.state.game.GameState;
import de.janfrase.blunder.engine.state.game.GameStateFenParser;
import de.janfrase.blunder.engine.state.game.GameStatePrinter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.info("Chess engine started.");

        logger.trace(GameStatePrinter.print());

        GameStateFenParser.loadStartingPosition();

        GameState.resetGameState();

        GameStateFenParser.loadFenString("N4k2/8/2p5/8/5q2/4Q3/1K6/8 w - - 0 1");

        logger.info("Chess engine finished.");
    }
}
