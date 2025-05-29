/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.movegen;

import de.janfrase.blunder.engine.movegen.move.Move;
import de.janfrase.blunder.engine.movegen.move.UciMoveParser;
import de.janfrase.blunder.engine.state.game.GameState;
import de.janfrase.blunder.engine.state.game.GameStateFenParser;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PerftTest {
    private static final Logger logger = LogManager.getLogger();

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
            GameState.getInstance().makeMove(move);
        }

        int nodes = 0;

        ArrayList<Move> legalMoves = MoveGenerator.generateLegalMoves();
        for (Move move : legalMoves) {
            GameState.getInstance().makeMove(move);

            int current_nodes = perft(depths - 1);
            nodes += current_nodes;
            System.out.println(move.toString() + " " + current_nodes);

            GameState.getInstance().unmakeMove(move);
        }

        System.out.println();
        System.out.println(nodes);
        return nodes;
    }

    protected static int perft(int depths) {
        int nodes = 0;

        if (depths == 0) {
            return 1;
        }
        ArrayList<Move> moves = MoveGenerator.generatePseudoLegalMoves();

        for (Move move : moves) {
            GameState.getInstance().makeMove(move);
            if (!MoveGenerator.canCaptureKing()) {
                nodes += perft(depths - 1);
            } else {
                logger.trace("Check!");
            }
            GameState.getInstance().unmakeMove(move);
        }

        return nodes;
    }
}
