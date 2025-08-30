/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.janfrase.blunder.engine.backend.movegen.Move;
import de.janfrase.blunder.engine.backend.state.game.FenParser;
import org.junit.jupiter.api.Test;

class SearcherTest {

    @Test
    void testSimpleCase() {
        FenParser.loadFenString("qR6/8/8/8/8/8/8/k6K w - - 0 1");

        Searcher searcher = new Searcher();

        Move move = searcher.startSearching(4);
        String moveString = move.toString();

        assertEquals("b8a8", moveString);
    }

    @Test
    void testSimpleCase2() {
        FenParser.loadFenString("qR6/k7/8/8/8/8/8/7K w - - 0 1");

        Searcher searcher = new Searcher();

        Move move = searcher.startSearching(4);
        String moveString = move.toString();

        assertEquals("b8a8", moveString);
    }

    @Test
    void testMate() {
        FenParser.loadFenString("6k1/8/8/8/3K4/8/2r1r3/1r6 b - - 0 1");

        Searcher searcher = new Searcher();

        Move move = searcher.startSearching(4);
        String moveString = move.toString();

        assertEquals("b1d1", moveString);
    }
}
