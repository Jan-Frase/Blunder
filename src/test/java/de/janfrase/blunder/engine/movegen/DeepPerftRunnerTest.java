/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.movegen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.janfrase.blunder.engine.state.game.GameStateFenParser;
import org.junit.jupiter.api.Test;

public class DeepPerftRunnerTest {
    @Test
    void Position2Test() {
        GameStateFenParser.loadFenString(
                "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");

        int nodes = PerftRunner.perft(4);
        assertEquals(4085603, nodes);
    }
}
