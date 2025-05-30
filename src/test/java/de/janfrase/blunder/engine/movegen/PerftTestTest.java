/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.movegen;

import de.janfrase.blunder.engine.state.game.GameState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class PerftTestTest {

    private final GameState gameState = GameState.getInstance();

    private static final String fenString =
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    @AfterEach
    void setUp() {
        GameState.resetGameState();
    }

    @Test
    void testPerft1() {
        PerftTest.main(new String[] {Integer.toString(2), fenString});
        PerftTest.main(new String[] {Integer.toString(1), fenString, "a2a4"});
    }
}
