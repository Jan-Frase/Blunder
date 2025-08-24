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

    private static final float WE_GOT_CHECKMATED_EVAL = 100000f;

    private int nodesSearched = 0;

    private Move bestMove = null;

    private final GameState gameState = GameState.getInstance();

    public Move startSearching(int depth) {
        recursiveSearch(depth, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, true, true);
        return bestMove;
    }

    private float recursiveSearch(
            int depth, float alpha, float beta, boolean isMaximizingPlayer, boolean isRoot) {
        if (depth == 0) {
            return Evaluator.calculateEvaluation(GameState.getInstance());
        }

        if (gameState.isHalfMoveClockAt50() || gameState.isRepeatedPosition()) {
            // if either of these are true we will consider the position a draw
            // for now we will just never draw
            return -WE_GOT_CHECKMATED_EVAL;
        }

        List<Move> moves = MoveGenerator.generatePseudoLegalMoves();

        float mostExtremeEval;

        if (isMaximizingPlayer) mostExtremeEval = -WE_GOT_CHECKMATED_EVAL * depth;
        else mostExtremeEval = WE_GOT_CHECKMATED_EVAL * depth;

        // if we can't find any move to play, we just got checkmated or the game is stalemated
        for (Move move : moves) {
            gameState.makeMove(move);

            // If this move results in our king being captured, it's not a legal move and should be
            // skipped.
            if (MoveGenerator.canCaptureKing()) {
                gameState.unmakeMove(move);
                continue;
            }

            nodesSearched++;

            float eval = recursiveSearch(depth - 1, alpha, beta, !isMaximizingPlayer, false);

            if (isMaximizingPlayer) {
                alpha = Math.max(alpha, eval);

                if (isRoot && eval > mostExtremeEval) {
                    bestMove = move;
                }

                mostExtremeEval = Math.max(mostExtremeEval, eval);
            } else {
                beta = Math.min(beta, eval);

                if (eval < mostExtremeEval) mostExtremeEval = eval;
            }

            gameState.unmakeMove(move);
            if (isRoot) UciMessageHandler.getInstance().sendInfo(nodesSearched);

            if (beta <= alpha) return mostExtremeEval;
        }
        return mostExtremeEval;
    }
}
