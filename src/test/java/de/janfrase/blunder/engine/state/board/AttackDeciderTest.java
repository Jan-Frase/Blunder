/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.state.board;

import static org.junit.jupiter.api.Assertions.*;

import de.janfrase.blunder.engine.state.game.GameState;
import de.janfrase.blunder.engine.state.game.GameStateFenParser;
import de.janfrase.blunder.utility.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class AttackDeciderTest {

    private final GameState gameState = GameState.getInstance();

    @AfterEach
    void setUp() {
        GameState.resetGameState();
    }

    @Test
    void isAttackedStraightSimple() {
        GameStateFenParser.loadFenString("8/1r6/8/8/8/8/8/8 w - - 0 1");

        assertTrue(AttackDecider.isAttacked(0, 1, Constants.Side.WHITE));
        assertFalse(AttackDecider.isAttacked(1, 1, Constants.Side.WHITE));
        assertTrue(AttackDecider.isAttacked(2, 1, Constants.Side.WHITE));
        assertTrue(AttackDecider.isAttacked(3, 1, Constants.Side.WHITE));
        assertTrue(AttackDecider.isAttacked(4, 1, Constants.Side.WHITE));
        assertTrue(AttackDecider.isAttacked(5, 1, Constants.Side.WHITE));
        assertTrue(AttackDecider.isAttacked(6, 1, Constants.Side.WHITE));
        assertTrue(AttackDecider.isAttacked(7, 1, Constants.Side.WHITE));

        assertTrue(AttackDecider.isAttacked(1, 0, Constants.Side.WHITE));
        assertFalse(AttackDecider.isAttacked(1, 1, Constants.Side.WHITE));
        assertTrue(AttackDecider.isAttacked(1, 2, Constants.Side.WHITE));
        assertTrue(AttackDecider.isAttacked(1, 3, Constants.Side.WHITE));
        assertTrue(AttackDecider.isAttacked(1, 4, Constants.Side.WHITE));
        assertTrue(AttackDecider.isAttacked(1, 5, Constants.Side.WHITE));
        assertTrue(AttackDecider.isAttacked(1, 6, Constants.Side.WHITE));
        assertTrue(AttackDecider.isAttacked(1, 7, Constants.Side.WHITE));
    }

    @Test
    void isAttackedDiagonalSimple() {
        GameStateFenParser.loadFenString("8/8/2b5/8/8/8/8/8 w - - 0 1");

        assertTrue(AttackDecider.isAttacked(0, 0, Constants.Side.WHITE));
        assertFalse(AttackDecider.isAttacked(2, 2, Constants.Side.WHITE));
        assertTrue(AttackDecider.isAttacked(3, 3, Constants.Side.WHITE));
    }

    @Test
    void isAttackedKnightSimple() {
        GameStateFenParser.loadFenString("8/8/2n5/8/8/8/8/8 w - - 0 1");

        assertTrue(AttackDecider.isAttacked(1, 0, Constants.Side.WHITE));
        assertTrue(AttackDecider.isAttacked(3, 0, Constants.Side.WHITE));

        assertTrue(AttackDecider.isAttacked(0, 1, Constants.Side.WHITE));
        assertTrue(AttackDecider.isAttacked(4, 1, Constants.Side.WHITE));

        assertTrue(AttackDecider.isAttacked(0, 3, Constants.Side.WHITE));
        assertTrue(AttackDecider.isAttacked(4, 3, Constants.Side.WHITE));

        assertTrue(AttackDecider.isAttacked(1, 4, Constants.Side.WHITE));
        assertTrue(AttackDecider.isAttacked(3, 4, Constants.Side.WHITE));
    }

    @Test
    void isAttackedStraightPin() {
        GameStateFenParser.loadFenString("B7/1r6/2k5/8/8/8/8/8 w - - 0 1");

        assertFalse(AttackDecider.isAttacked(0, 1, Constants.Side.WHITE));
        assertFalse(AttackDecider.isAttacked(2, 1, Constants.Side.WHITE));
        assertFalse(AttackDecider.isAttacked(3, 1, Constants.Side.WHITE));
        assertFalse(AttackDecider.isAttacked(4, 1, Constants.Side.WHITE));
        assertFalse(AttackDecider.isAttacked(5, 1, Constants.Side.WHITE));
        assertFalse(AttackDecider.isAttacked(6, 1, Constants.Side.WHITE));
        assertFalse(AttackDecider.isAttacked(7, 1, Constants.Side.WHITE));

        assertFalse(AttackDecider.isAttacked(1, 0, Constants.Side.WHITE));
        assertFalse(AttackDecider.isAttacked(1, 2, Constants.Side.WHITE));
        assertFalse(AttackDecider.isAttacked(1, 3, Constants.Side.WHITE));
        assertFalse(AttackDecider.isAttacked(1, 4, Constants.Side.WHITE));
        assertFalse(AttackDecider.isAttacked(1, 5, Constants.Side.WHITE));
        assertFalse(AttackDecider.isAttacked(1, 6, Constants.Side.WHITE));
        assertFalse(AttackDecider.isAttacked(1, 7, Constants.Side.WHITE));
    }

    @Test
    void isAttackedDiagonalPin() {
        GameStateFenParser.loadFenString("8/8/1Rbk4/8/8/8/8/8 w - - 0 1");

        assertFalse(AttackDecider.isAttacked(0, 0, Constants.Side.WHITE));
        assertFalse(AttackDecider.isAttacked(3, 3, Constants.Side.WHITE));
    }

    @Test
    void isAttackedKnightPin() {
        GameStateFenParser.loadFenString("8/1B6/2n5/3k4/8/8/8/8 w - - 0 1");

        assertFalse(AttackDecider.isAttacked(1, 0, Constants.Side.WHITE));
        assertFalse(AttackDecider.isAttacked(3, 0, Constants.Side.WHITE));

        assertFalse(AttackDecider.isAttacked(0, 1, Constants.Side.WHITE));
        assertFalse(AttackDecider.isAttacked(4, 1, Constants.Side.WHITE));

        assertFalse(AttackDecider.isAttacked(0, 3, Constants.Side.WHITE));
        assertFalse(AttackDecider.isAttacked(4, 3, Constants.Side.WHITE));

        assertFalse(AttackDecider.isAttacked(1, 4, Constants.Side.WHITE));
        assertFalse(AttackDecider.isAttacked(3, 4, Constants.Side.WHITE));
    }
}
