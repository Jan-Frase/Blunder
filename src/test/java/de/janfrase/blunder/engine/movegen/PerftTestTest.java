/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.movegen;

import de.janfrase.blunder.engine.state.game.GameState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class PerftTestTest {

    private final GameState gameState = GameState.getInstance();

    @AfterEach
    void setUp() {
        GameState.resetGameState();
    }

    @Test
    void testPerft1() {
        final String fenString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        PerftTest.main(new String[] {Integer.toString(2), fenString});
        PerftTest.main(new String[] {Integer.toString(1), fenString, "a2a4"});
    }

    @Test
    void testPerft2() {
        final String fenString =
                "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1";
        PerftTest.main(new String[] {Integer.toString(2), fenString});
        PerftTest.main(new String[] {Integer.toString(1), fenString, "a1b1"});
    }

    @Test
    void testPerft3() {
        final String fenString = "8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1";
        PerftTest.main(new String[] {Integer.toString(2), fenString});
        PerftTest.main(new String[] {Integer.toString(1), fenString, "e2e4"});
    }
}
