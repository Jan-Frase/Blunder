/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.search;

import static org.junit.jupiter.api.Assertions.*;

import de.janfrase.blunder.engine.backend.movegen.Move;
import de.janfrase.blunder.engine.backend.state.game.FenParser;
import de.janfrase.blunder.engine.backend.state.game.GameState;
import de.janfrase.blunder.uci.UciMessageHandler;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

class SearchManagerTest {

    // fails
    @Test
    void go1() {
        FenParser.loadFenString("4Q3/2b4r/7B/6R1/5k2/8/7K/5q2 w - - 0 1");

        String[] expectedMoves = new String[] {"e8e3", "f4e3", "g5g3"};
        testLine(expectedMoves);
    }

    // passes
    @Test
    void go2() {
        FenParser.loadFenString("8/2b4r/2Q4B/6R1/5k2/8/7K/5q2 b - - 1 1");

        String[] expectedMoves = new String[] {"h7h6", "c6h6"};
        testLine(expectedMoves);
    }

    // fails - passes with 6 seconds
    @Test
    void go3() {
        FenParser.loadFenString("7k/1pp3p1/p2r4/P5pK/1P3b2/B1P1rB1P/2Q3P1/3R4 b - - 0 36");

        // long mate
        String[] expectedMoves =
                new String[] {
                    "d6h6", "h5g4", "h6h4", "g4f5", "e3e5", "f5g6", "h4h6", "g6f7", "h6f6"
                };
        testLine(expectedMoves);
    }

    // passes
    @Test
    void go4() {
        FenParser.loadFenString("r4r1k/pp1q2np/2pp1bRQ/4pN2/2B1P3/3P1P2/PPP2P1P/7K w - - 3 22");

        // short mate
        String[] expectedMoves = new String[] {"h6h7", "h8h7", "g6h6"};
        testLine(expectedMoves);
    }

    private static volatile Move bestMove = null;

    private void testLine(String[] expectedMoves) {
        // set what limits the search
        SearchLimitations searchLimitations =
                new SearchLimitations(null, false, -1, -1, -1, 6000, false);

        // create a consumer that prints the best move and sets it into bestMove
        UciMessageHandler.getInstance()
                .setMoveConsumer(
                        new Consumer<Move>() {
                            @Override
                            public void accept(Move move) {
                                System.out.println("bestmove " + move.toString());
                                bestMove = move;
                            }
                        });

        // loop through the expected moves
        for (int i = 0; i < expectedMoves.length; i++) {
            // delete the old best move
            bestMove = null;
            SearchManager.getInstance().go(searchLimitations);

            // busy wait until bestMove has been set
            Move move = waitForBestMove();

            assertEquals(expectedMoves[i], move.toString(), "Move " + (i + 1) + " was wrong.");
            GameState.getInstance().makeMove(move);
        }
    }

    private Move waitForBestMove() {
        while (bestMove == null) {
            Thread.onSpinWait();
        }

        return bestMove;
    }
}
