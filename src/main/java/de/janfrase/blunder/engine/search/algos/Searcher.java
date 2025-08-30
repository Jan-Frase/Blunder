/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.search.algos;

import de.janfrase.blunder.engine.backend.movegen.KingInCheckDecider;
import de.janfrase.blunder.engine.backend.movegen.MoveGenerator;
import de.janfrase.blunder.engine.backend.movegen.move.Move;
import de.janfrase.blunder.engine.backend.state.game.GameState;
import de.janfrase.blunder.engine.evaluation.Evaluator;
import de.janfrase.blunder.uci.UciMessageHandler;
import de.janfrase.blunder.utility.Constants;
import java.util.List;

/**
 * <a href="https://www.chessprogramming.org/Alpha-Beta">Alpha Beta</a>
 */
public class Searcher {

    private static final float WE_GOT_CHECKMATED_EVAL = 100000f;

    private int nodesSearched = 0;

    private Move bestMove = null;

    private final GameState gameState = GameState.getInstance();

    public Move startSearching(int depth) {
        Constants.Side sideToMove = gameState.getFriendlySide();
        boolean isMaximizingPlayer = (sideToMove == Constants.Side.WHITE);

        alphaBetaSearch(
                depth, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, isMaximizingPlayer, true);
        return bestMove;
    }

    private float alphaBetaSearch(
            int remainingDepth,
            float alpha,
            float beta,
            boolean isMaximizingPlayer,
            boolean isRoot) {
        if (gameState.isHalfMoveClockAt50() || gameState.isRepeatedPosition()) {
            // if either of these is true, we will consider the position a draw
            return 0f;
        }

        // we have reached the end! return the eval
        if (remainingDepth == 0) {
            return Evaluator.calculateEvaluation(GameState.getInstance());
        }

        // If we are the maximizing player, the score needs to be negative
        float mostExtremeEval =
                isMaximizingPlayer ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;

        // if we can't find any move to play, we just got checkmated or the game is stalemated
        boolean noLegalMoves = true;
        Constants.Side activeSide = GameState.getInstance().getFriendlySide();

        List<Move> moves = MoveGenerator.generatePseudoLegalMoves();
        for (Move move : moves) {
            gameState.makeMove(move);

            // If this move results in our king being captured, it's not a legal move and should be
            // skipped.
            if (KingInCheckDecider.isKingUnderAttack(activeSide)) {
                gameState.unmakeMove(move);
                continue;
            }

            // making this move does not result in our king being captured, thus we found at least
            // one legal move
            noLegalMoves = false;
            nodesSearched++;

            // search deeper
            float eval =
                    alphaBetaSearch(remainingDepth - 1, alpha, beta, !isMaximizingPlayer, false);

            // update alpha beta, mostExtremeEval and bestMove depending on isMaximizingPlayer and
            // isRoot
            if (isMaximizingPlayer) {
                if (isRoot && eval > mostExtremeEval) {
                    bestMove = move;
                }
                alpha = Math.max(alpha, eval);
                mostExtremeEval = Math.max(mostExtremeEval, eval);
            } else {
                if (isRoot && eval < mostExtremeEval) {
                    bestMove = move;
                }
                beta = Math.min(beta, eval);
                mostExtremeEval = Math.min(mostExtremeEval, eval);
            }

            gameState.unmakeMove(move);

            // output some info to the ui
            if (isRoot) UciMessageHandler.getInstance().sendInfo(nodesSearched);

            // pruning!
            if (beta <= alpha) return mostExtremeEval;
        }

        // if we can't make any move
        if (noLegalMoves) {
            // and we are in check
            if (KingInCheckDecider.isKingUnderAttack(activeSide)) {
                // its checkmate
                return isMaximizingPlayer
                        ? -WE_GOT_CHECKMATED_EVAL - remainingDepth
                        : WE_GOT_CHECKMATED_EVAL + remainingDepth;
                // else if we aren't in check
            } else {
                // its draw
                return 0f;
            }
        }

        return mostExtremeEval;
    }
}
