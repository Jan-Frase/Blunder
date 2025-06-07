/* Made by Jan Frase :) */
package de.janfrase.blunder;

import de.janfrase.blunder.engine.movegen.MoveGenerator;
import de.janfrase.blunder.engine.movegen.PerftRunner;
import de.janfrase.blunder.engine.movegen.move.Move;
import de.janfrase.blunder.engine.state.game.GameState;
import de.janfrase.blunder.engine.state.game.GameStateFenParser;
import de.janfrase.blunder.engine.state.game.GameStatePrinter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class Main {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        GameStateFenParser.loadFenString(
                "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");

        // this produces the correct result
        // Move move = UciMoveParser.parseUciMove("a1b1");

        // this as well -> thus the problem lies in me not resetting some of the state
        Move move =
                MoveGenerator.generatePseudoLegalMoves().stream()
                        .filter(m -> m.toString().equals("a1b1"))
                        .findFirst()
                        .orElseThrow();

        GameState.getInstance().makeMove(move);

        // int nodes = PerftRunner.perft(3);

        int nodes =
                PerftRunner.perft(
                        4, "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");

        System.out.println(nodes);

        Configurator.setAllLevels(LogManager.getRootLogger().getName(), Level.TRACE);
        logger.trace(GameStatePrinter.print());
    }
}
