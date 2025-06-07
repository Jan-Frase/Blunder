/* Made by Jan Frase :) */
package de.janfrase.blunder;

import de.janfrase.blunder.engine.movegen.MoveGenerator;
import de.janfrase.blunder.engine.movegen.move.Move;
import de.janfrase.blunder.engine.movegen.move.UciMoveParser;
import de.janfrase.blunder.engine.state.game.FenParser;
import de.janfrase.blunder.engine.state.game.GameState;
import de.janfrase.blunder.engine.state.game.StatePrinter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class PerftRunner {
    private static final Logger logger = LogManager.getLogger();
    private static final GameState gameState = GameState.getInstance();

    /**
     * This way to call the engine should only be used to run perftree.
     * <a href="https://github.com/agausmann/perftree">Perftree GitHub</a>
     *
     * @param args Command line arguments in the following order:
     *             <ul>
     *             <li>depth - The depth parameter of the perft function</li>
     *             <li>fen - The Forsyth-Edwards Notation string of the base position</li>
     *             <li>moves - Optional space-separated sequence of moves from the base position.
     *                 Moves are in UCI notation (source-target-promotion), where castling is encoded
     *                 as the king's move. Examples: e2e4, e1g1, a7b8Q</li>
     *             </ul>
     */
    public static void main(String[] args) {
        // Disable all logging (aside from errors). Has to be done because PerfTree relies on what
        // we print.
        // We could still continue logging to a file, but it's quite slow.
        Configurator.setAllLevels(LogManager.getRootLogger().getName(), Level.ERROR);

        int depths = Integer.parseInt(args[0]);
        String fenString = args[1];

        // Extract the move strings...
        String[] moveStrings = new String[args.length - 2];
        System.arraycopy(args, 2, moveStrings, 0, moveStrings.length);

        // ...and convert them to move records.
        Move[] moves =
                Arrays.stream(moveStrings)
                        .map(UciMoveParser::parseUciMove)
                        .toList()
                        .toArray(new Move[0]);

        // Load the starting position
        FenParser.loadFenString(fenString);

        // Make all moves that have to be applied before perft can begin.
        for (Move move : moves) {
            gameState.makeMove(move);
        }

        // Let's go :)
        perftWithPrint(depths);
    }

    /**
     * Executes a perft search but also prints the results in the format required by perftree.
     * <p>
     * Should look something like this:
     * <p>
     * a2a3 380
     * <p>
     * b2b3 420
     * <p>
     * c2c3 420
     * <p>
     * 8902
     *
     * @param depths How deep we should search. Scales exponentially.
     */
    private static void perftWithPrint(int depths) {
        long total_nodes = 0;
        ArrayList<Move> legalMoves = MoveGenerator.generateLegalMoves();

        // Since the perftree tool also sorts the moves, it can be helpful to have them in the same
        // order.
        legalMoves.sort(Comparator.comparing(Move::toString));
        for (Move move : legalMoves) {
            // TODO: Remove hashing logic.
            int preHash = gameState.hashCode();
            gameState.makeMove(move);
            logger.info(StatePrinter.stateToString());

            long current_nodes = perft(depths - 1);
            total_nodes += current_nodes;
            System.out.println(move.toString() + " " + current_nodes);

            gameState.unmakeMove(move);
            int postHash = gameState.hashCode();

            assert preHash == postHash : "Hashcode of game state changed after move!";
        }

        System.out.println();
        System.out.println(total_nodes);
    }

    /**
     * Starts a perft test from the current position.
     * <a href="https://www.chessprogramming.org/Perft">Wiki entry.</a>
     * @param depths How deep we should search. Scales exponentially.
     * @return The number of positions discovered.
     */
    public static long perft(int depths) {
        long nodes = 0;

        if (depths == 0) {
            return 1;
        }
        // TODO: If the depths == 1 we can simply return the length of this list. -> Easy
        // performance gains.
        ArrayList<Move> moves = MoveGenerator.generatePseudoLegalMoves();

        for (Move move : moves) {
            gameState.makeMove(move);
            if (MoveGenerator.canCaptureKing()) {
                gameState.unmakeMove(move);
                continue;
            }

            nodes += perft(depths - 1);
            gameState.unmakeMove(move);
        }

        return nodes;
    }
}
