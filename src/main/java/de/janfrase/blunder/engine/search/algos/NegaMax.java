/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.search.algos;

import de.janfrase.blunder.engine.backend.movegen.MoveGenerator;
import de.janfrase.blunder.engine.backend.movegen.move.Move;
import de.janfrase.blunder.engine.backend.state.game.GameState;
import de.janfrase.blunder.engine.evaluation.MaterialEvaluator;
import java.util.List;

/**
 * <a href="https://www.chessprogramming.org/Negamax">Negamax</a>
 */
public class NegaMax {

    public static Move startSearching(int depth) {
        List<Move> moves = MoveGenerator.generateLegalMoves();

        Move bestMove = null;
        float bestEval = Float.NEGATIVE_INFINITY;

        for (Move move : moves) {
            GameState.getInstance().makeMove(move);

            float eval = -recursiveSearch(depth - 1);

            if (eval > bestEval) {
                bestMove = move;
                bestEval = eval;
            }

            GameState.getInstance().unmakeMove(move);
        }
        return bestMove;
    }

    private static float recursiveSearch(int depth) {
        if (depth == 0) {
            return MaterialEvaluator.calculateEvaluation(GameState.getInstance());
        }

        float max = Float.NEGATIVE_INFINITY;

        List<Move> moves = MoveGenerator.generateLegalMoves();
        for (Move move : moves) {
            GameState.getInstance().makeMove(move);
            // negate this because we switch perspective between players
            float eval = -recursiveSearch(depth - 1);

            if (eval > max) {
                max = eval;
            }

            GameState.getInstance().unmakeMove(move);
        }
        return max;
    }
}
