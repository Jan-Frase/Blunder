/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.search.algos;

import de.janfrase.blunder.engine.backend.movegen.MoveGenerator;
import de.janfrase.blunder.engine.backend.movegen.move.Move;
import de.janfrase.blunder.engine.backend.state.game.GameState;
import de.janfrase.blunder.engine.evaluation.Evaluator;
import de.janfrase.blunder.uci.UciMessageHandler;
import java.util.List;

/**
 * <a href="https://www.chessprogramming.org/Negamax">Negamax</a>
 */
public class Searcher {

    private static final float WE_GOT_CHECKMATED_EVAL = -100000f;

    private int nodesSearched = 0;

    public Move startSearching(int depth) {
        List<Move> moves = MoveGenerator.generateLegalMoves();

        Move bestMove = moves.getFirst();
        float bestEval = Float.NEGATIVE_INFINITY;

        GameState gameState = GameState.getInstance();

        for (Move move : moves) {
            gameState.makeMove(move);
            nodesSearched++;

            float eval = 0;
            if (gameState.isHalfMoveClockAt50() || gameState.isRepeatedPosition()) {
                // if either of these are true we will consider the position a draw
                // for now we will just never draw
                eval = Float.NEGATIVE_INFINITY;
            } else {
                eval = -recursiveSearch(depth - 1);
            }

            if (eval > bestEval) {
                bestMove = move;
                bestEval = eval;
            }

            gameState.unmakeMove(move);
            UciMessageHandler.getInstance().sendInfo(nodesSearched);
        }
        return bestMove;
    }

    private float recursiveSearch(int depth) {
        if (depth == 0) {
            return Evaluator.calculateEvaluation(GameState.getInstance());
        }

        // if we can't find any move to play, we just got checkmated or the game is stalemated
        float max = WE_GOT_CHECKMATED_EVAL * depth;

        GameState gameState = GameState.getInstance();

        List<Move> moves = MoveGenerator.generatePseudoLegalMoves();
        for (Move move : moves) {
            gameState.makeMove(move);

            // If this move results in our king being captured, it's not a legal move and should be
            // skipped.
            if (MoveGenerator.canCaptureKing()) {
                gameState.unmakeMove(move);
                continue;
            }

            nodesSearched++;

            float eval = 0;

            if (gameState.isHalfMoveClockAt50() || gameState.isRepeatedPosition()) {
                // if either of these are true we will consider the position a draw
                // for now we will just never draw
                eval = Float.NEGATIVE_INFINITY;
            } else {
                // negate this because we switch perspective between players
                eval = -recursiveSearch(depth - 1);
            }

            if (eval > max) {
                max = eval;
            }

            gameState.unmakeMove(move);
        }
        return max;
    }
}
