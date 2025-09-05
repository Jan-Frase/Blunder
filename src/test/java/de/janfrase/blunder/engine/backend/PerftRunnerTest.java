/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.janfrase.blunder.PerftRunner;
import de.janfrase.blunder.engine.backend.state.game.FenParser;
import de.janfrase.blunder.engine.backend.state.game.GameState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class PerftRunnerTest {
    @AfterEach
    void setUp() {
        GameState.resetGameState();
    }

    @Test
    void startingPositionTest() {
        FenParser.loadStartingPosition();

        long nodes = PerftRunner.perft(2);
        assertEquals(400, nodes);
    }

    @Test
    void Position2Test() {
        FenParser.loadFenString(
                "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");

        long nodes = PerftRunner.perft(2);
        assertEquals(2039, nodes);
    }

    @Test
    void Position3Test() {
        FenParser.loadFenString("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1");

        long nodes = PerftRunner.perft(1);
        assertEquals(14, nodes);

        nodes = PerftRunner.perft(2);
        assertEquals(191, nodes);
    }

    @Test
    void Position4Test() {
        FenParser.loadFenString("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");

        long nodes = PerftRunner.perft(2);
        assertEquals(264, nodes);
    }

    @Test
    void Position5Test() {
        FenParser.loadFenString("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");

        long nodes = PerftRunner.perft(2);
        assertEquals(1486, nodes);
    }

    @Test
    void Position6Test() {
        FenParser.loadFenString(
                "r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10 ");

        long nodes = PerftRunner.perft(2);
        assertEquals(2079, nodes);
    }

    @Test
    void Position7Test() {
        FenParser.loadFenString(
                "7k/1pp3p1/p5Kr/P3r1p1/1P3b2/B1P2B1P/2Q3P1/3R4 w - - 7 40");

        long nodes = PerftRunner.perft(1);
        assertEquals(1, nodes);
    }

    @Test
    void Position8Test() {
        FenParser.loadFenString(
                "7k/1pp3p1/p2r4/P5pK/1P3b2/B1P1rB1P/2Q3P1/3R4 b - - 0 36");

        // TODO: Fix this.
        long nodes = PerftRunner.perft(4);
        assertEquals(1327529, nodes);
    }
}
