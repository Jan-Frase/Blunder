/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.janfrase.blunder.engine.backend.movegen.move.Move;
import de.janfrase.blunder.engine.backend.state.game.FenParser;
import de.janfrase.blunder.engine.search.algos.NegaMax;
import org.junit.jupiter.api.Test;

class NegaMaxTest {

    @Test
    void testSimpleCase() {
        FenParser.loadFenString("qR6/8/8/8/8/8/8/k6K w - - 0 1");

        NegaMax negaMax = new NegaMax();

        Move move = negaMax.startSearching(4);
        String moveString = move.toString();

        assertEquals("b8a8", moveString);
    }

    @Test
    void testSimpleCase2() {
        FenParser.loadFenString("qR6/k7/8/8/8/8/8/7K w - - 0 1");

        NegaMax negaMax = new NegaMax();

        Move move = negaMax.startSearching(4);
        String moveString = move.toString();

        assertEquals("b8a8", moveString);
    }
}
