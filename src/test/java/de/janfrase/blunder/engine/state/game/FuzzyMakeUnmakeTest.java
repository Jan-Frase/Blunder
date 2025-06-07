/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.state.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import de.janfrase.blunder.engine.movegen.MoveGenerator;
import de.janfrase.blunder.engine.movegen.move.Move;
import java.util.ArrayList;
import java.util.Random;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class FuzzyMakeUnmakeTest {
    @AfterEach
    void setUp() {
        GameState.resetGameState();
    }

    @Test
    void comparePreAndPostMakeMove() {
        FenParser.loadFenString(
                "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");

        for (int depth = 1; depth <= 1000; depth++) {
            ArrayList<Move> moves = MoveGenerator.generateLegalMoves();

            for (Move move : moves) {
                int preHash = GameState.getInstance().hashCode();
                GameState.getInstance().makeMove(move);
                int betweenHash = GameState.getInstance().hashCode();
                GameState.getInstance().unmakeMove(move);
                int postHash = GameState.getInstance().hashCode();

                assertNotEquals(
                        preHash,
                        betweenHash,
                        "Hashcode of game state before and after makeMove() is equal!");
                assertNotEquals(
                        betweenHash,
                        postHash,
                        "Hashcode of game state before and after makeMove() is equal!");
                assertEquals(
                        preHash,
                        postHash,
                        "Hashcode of game state before and after makeMove() and unmakeMove() is not"
                                + " equal!");
            }

            if (moves.isEmpty()) return;

            Random random = new Random();
            int randomNumber = random.nextInt(0, moves.size()); // Generates a number
            GameState.getInstance().makeMove(moves.get(randomNumber));
        }
    }
}
