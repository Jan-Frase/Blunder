/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.janfrase.blunder.PerftRunner;
import de.janfrase.blunder.engine.backend.state.game.FenParser;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Slow")
public class DeepPerftRunnerTest {
    @Test
    void Position2Test() {
        // Takes: 23.273s -> 28.451s -GraalVM-> 18.390s
        FenParser.loadFenString(
                "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");

        long nodes = PerftRunner.perftWithTime(4);
        assertEquals(4085603, nodes);
    }

    @Test
    void Position3Test() {
        // Takes: 93s -> 92s -GraalVM-> 59.288s
        FenParser.loadFenString("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1");

        long nodes = PerftRunner.perftWithTime(5);
        assertEquals(674624, nodes);

        nodes = PerftRunner.perftWithTime(6);
        assertEquals(11030083, nodes);
    }

    @Test
    void Position4Test() {
        // Takes: 3.172s -> 3.008s -GraalVM-> 1.806s
        FenParser.loadFenString("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");

        long nodes = PerftRunner.perftWithTime(4);
        assertEquals(422333, nodes);
    }

    @Test
    void Position5Test() {
        // Takes: 16.329s -> 14.979s -GraalVM-> 9.275s
        FenParser.loadFenString("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");

        long nodes = PerftRunner.perftWithTime(4);
        assertEquals(2103487, nodes);
    }

    @Test
    void Position6Test() {
        // Takes: 28.226 -> 25.971 -GraalVM-> 16.260s
        FenParser.loadFenString(
                "r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10");

        long nodes = PerftRunner.perftWithTime(4);
        assertEquals(3894594, nodes);
    }
}
