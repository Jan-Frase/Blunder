/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.state.board;

import static org.junit.jupiter.api.Assertions.*;

import de.janfrase.blunder.engine.state.game.GameState;
import de.janfrase.blunder.engine.state.game.GameStateFenParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class PinDeciderTest {

    private final GameState gameState = GameState.getInstance();

    @AfterEach
    void setUp() {
        GameState.resetGameState();
    }

    @Test
    void isPinnedDiagonalSimple() {
        GameStateFenParser.loadFenString("8/8/8/2b5/3R4/4K3/8/8 b - - 0 1");

        assertFalse(PinDecider.isPinned(2, 3));
        assertTrue(PinDecider.isPinned(3, 4));
        assertFalse(PinDecider.isPinned(4, 5));
    }

    @Test
    void isPinnedStraightSimple() {
        GameStateFenParser.loadFenString("8/8/8/3r4/3N4/3K4/8/8 w - - 0 1");

        assertFalse(PinDecider.isPinned(3, 3));
        assertTrue(PinDecider.isPinned(3, 4));
        assertFalse(PinDecider.isPinned(3, 5));
    }

    @Test
    void isPinnedDiagonalDouble() {
        GameStateFenParser.loadFenString("8/8/8/1Rb1k3/3R4/4K3/8/8 w - - 0 1");

        assertTrue(PinDecider.isPinned(2, 3));
        assertTrue(PinDecider.isPinned(3, 4));
        assertFalse(PinDecider.isPinned(4, 5));
    }
}
