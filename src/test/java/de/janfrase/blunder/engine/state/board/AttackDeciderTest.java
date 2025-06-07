/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.state.board;

import static org.junit.jupiter.api.Assertions.*;

import de.janfrase.blunder.engine.state.game.FenParser;
import de.janfrase.blunder.engine.state.game.GameState;
import de.janfrase.blunder.utility.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class AttackDeciderTest {

    @AfterEach
    void setUp() {
        GameState.resetGameState();
    }

    @Test
    void isKingUnderAttackStraightSimple() {
        FenParser.loadFenString("8/1r6/8/8/8/8/8/8 w - - 0 1");

        assertTrue(AttackDecider.isKingUnderAttack(0, 1, Constants.Side.WHITE));
        assertFalse(AttackDecider.isKingUnderAttack(1, 1, Constants.Side.WHITE));
        assertTrue(AttackDecider.isKingUnderAttack(2, 1, Constants.Side.WHITE));
        assertTrue(AttackDecider.isKingUnderAttack(3, 1, Constants.Side.WHITE));
        assertTrue(AttackDecider.isKingUnderAttack(4, 1, Constants.Side.WHITE));
        assertTrue(AttackDecider.isKingUnderAttack(5, 1, Constants.Side.WHITE));
        assertTrue(AttackDecider.isKingUnderAttack(6, 1, Constants.Side.WHITE));
        assertTrue(AttackDecider.isKingUnderAttack(7, 1, Constants.Side.WHITE));

        assertTrue(AttackDecider.isKingUnderAttack(1, 0, Constants.Side.WHITE));
        assertFalse(AttackDecider.isKingUnderAttack(1, 1, Constants.Side.WHITE));
        assertTrue(AttackDecider.isKingUnderAttack(1, 2, Constants.Side.WHITE));
        assertTrue(AttackDecider.isKingUnderAttack(1, 3, Constants.Side.WHITE));
        assertTrue(AttackDecider.isKingUnderAttack(1, 4, Constants.Side.WHITE));
        assertTrue(AttackDecider.isKingUnderAttack(1, 5, Constants.Side.WHITE));
        assertTrue(AttackDecider.isKingUnderAttack(1, 6, Constants.Side.WHITE));
        assertTrue(AttackDecider.isKingUnderAttack(1, 7, Constants.Side.WHITE));
    }

    @Test
    void isKingUnderAttackDiagonalSimple() {
        FenParser.loadFenString("8/8/2b5/8/8/8/8/8 w - - 0 1");

        assertTrue(AttackDecider.isKingUnderAttack(0, 0, Constants.Side.WHITE));
        assertFalse(AttackDecider.isKingUnderAttack(2, 2, Constants.Side.WHITE));
        assertTrue(AttackDecider.isKingUnderAttack(3, 3, Constants.Side.WHITE));
    }

    @Test
    void isKingUnderAttackKnightSimple() {
        FenParser.loadFenString("8/8/2n5/8/8/8/8/8 w - - 0 1");

        assertTrue(AttackDecider.isKingUnderAttack(1, 0, Constants.Side.WHITE));
        assertTrue(AttackDecider.isKingUnderAttack(3, 0, Constants.Side.WHITE));

        assertTrue(AttackDecider.isKingUnderAttack(0, 1, Constants.Side.WHITE));
        assertTrue(AttackDecider.isKingUnderAttack(4, 1, Constants.Side.WHITE));

        assertTrue(AttackDecider.isKingUnderAttack(0, 3, Constants.Side.WHITE));
        assertTrue(AttackDecider.isKingUnderAttack(4, 3, Constants.Side.WHITE));

        assertTrue(AttackDecider.isKingUnderAttack(1, 4, Constants.Side.WHITE));
        assertTrue(AttackDecider.isKingUnderAttack(3, 4, Constants.Side.WHITE));
    }
}
