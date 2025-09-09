/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.backend.movegen;

import static org.junit.jupiter.api.Assertions.*;

import de.janfrase.blunder.engine.backend.state.game.FenParser;
import de.janfrase.blunder.utility.Constants;
import org.junit.jupiter.api.Test;

class KingInCheckDeciderTest {

    @Test
    void isKingUnderAttack() {
        FenParser.loadFenString("7k/6K1/8/8/8/8/8/8 w - - 0 1");

        boolean isInCheck = KingInCheckDecider.isKingUnderAttack(Constants.Side.WHITE);

        assertTrue(isInCheck);
    }
}
