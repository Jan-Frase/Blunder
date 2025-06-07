/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.movegen;

import de.janfrase.blunder.engine.movegen.move.Move;
import de.janfrase.blunder.engine.movegen.move.UciMoveParser;
import de.janfrase.blunder.engine.state.game.GameState;
import de.janfrase.blunder.engine.state.game.GameStateFenParser;
import de.janfrase.blunder.engine.state.game.GameStatePrinter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PerftRunner {
    private static final Logger logger = LogManager.getLogger();
    private static final GameState gameState = GameState.getInstance();

    public static void main(String[] args) {
        int depths = Integer.parseInt(args[0]);
        String fenString = args[1];

        String[] moveStrings = new String[args.length - 2];
        System.arraycopy(args, 2, moveStrings, 0, moveStrings.length);

        Move[] moves =
                Arrays.stream(moveStrings)
                        .map(UciMoveParser::parseUciMove)
                        .toList()
                        .toArray(new Move[0]);

        perft(depths, fenString, moves);
    }

    public static int perft(int depths, String fenString, Move... moves) {
        GameStateFenParser.loadFenString(fenString);

        for (Move move : moves) {
            gameState.makeMove(move);
        }

        int nodes = 0;

        ArrayList<Move> legalMoves = MoveGenerator.generateLegalMoves();
        legalMoves.sort(Comparator.comparing(Move::toString));
        for (Move move : legalMoves) {
            int preHash = gameState.hashCode();
            gameState.makeMove(move);
            logger.info(GameStatePrinter.print());

            int current_nodes = perft(depths - 1);
            nodes += current_nodes;
            System.out.println(move.toString() + " " + current_nodes);

            gameState.unmakeMove(move);
            int postHash = gameState.hashCode();

            assert preHash == postHash : "Hashcode of game state changed after move!";
        }

        System.out.println();
        System.out.println(nodes);
        return nodes;
    }

    public static int perft(int depths) {
        // Configurator.setAllLevels(LogManager.getRootLogger().getName(), Level.ERROR);

        int nodes = 0;

        if (depths == 0) {
            return 1;
        }
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
