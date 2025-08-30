/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.backend.state.game;

import static org.junit.jupiter.api.Assertions.*;

import de.janfrase.blunder.engine.backend.movegen.Move;
import de.janfrase.blunder.uci.UciMoveParser;
import org.junit.jupiter.api.Test;

class ZobristHasherTest {

    private final GameState gameState = GameState.getInstance();

    @Test
    void enPassantTest() {
        FenParser.loadFenString("3k4/8/8/8/3p4/8/2P5/3K4 w - - 0 1");

        String[] moves =
                new String[] {
                    "c2c4", "d4c3", "d1e1",
                };

        testHelper(moves);
    }

    @Test
    void enPassantTest2() {
        FenParser.loadFenString("4k3/8/2b5/8/8/8/2P5/4K3 w - - 0 1");

        String[] moves = new String[] {"c2c4", "c6b7", "e1e2", "b7c6", "e2e1"};

        testHelper(moves);
    }

    @Test
    void castlingTest() {
        FenParser.loadFenString("4k3/8/8/8/8/8/8/R3K2R w KQ - 0 1");

        String[] moves =
                new String[] {
                    "h1h2", "e1c1", "d1d2",
                };

        testHelper(moves);
    }

    @Test
    void captureTest() {
        FenParser.loadFenString("4k3/8/2b5/8/8/8/2B5/4K3 w - - 0 1");

        String[] moves =
                new String[] {
                    "c2e4", "c6e4", "e1e2",
                };

        testHelper(moves);
    }

    private void testHelper(String[] moves) {
        long[] hashes = new long[moves.length];
        Move[] parsedMoves = new Move[moves.length];
        for (int i = 0; i < moves.length; i++) {
            long zobristHash = gameState.zobristHasher.getZobristHash();
            hashes[i] = zobristHash;

            Move move = UciMoveParser.parseUciMove(moves[i]);
            parsedMoves[i] = move;
            gameState.makeMove(move);
        }

        for (int i = moves.length - 1; i >= 0; i--) {
            Move move = parsedMoves[i];
            gameState.unmakeMove(move);

            long zobristHash = gameState.zobristHasher.getZobristHash();
            assertEquals(hashes[i], zobristHash);
        }

        for (int i = 0; i < moves.length; i++) {
            for (int j = 0; j < moves.length; j++) {
                if (i == j) continue;
                assertNotEquals(hashes[i], hashes[j]);
            }
        }
    }
}
