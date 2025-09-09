/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.search;

import de.janfrase.blunder.engine.backend.movegen.KingInCheckDecider;
import de.janfrase.blunder.engine.backend.movegen.Move;
import de.janfrase.blunder.engine.backend.movegen.MoveGenerator;
import de.janfrase.blunder.engine.backend.state.game.GameState;
import de.janfrase.blunder.engine.evaluation.Evaluator;
import de.janfrase.blunder.utility.Constants;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <a href="https://www.chessprogramming.org/Alpha-Beta">Alpha Beta</a>
 */
public class Searcher {

    private static final float WE_GOT_CHECKMATED_EVAL = 100000f;
    private final GameState gameState = GameState.getInstance();

    // infos for the ui
    private int nodesSearched = 0;

    // important search state
    private Move bestMove = null;
    AtomicBoolean stopSearchingImmediately = new AtomicBoolean(false);

    // TODO: Eventually replace this with a transposition table.
    private ArrayList<Move> previousPrincipalVariation = new ArrayList<>();

    public SearchResult startSearching(int depth, ArrayList<Move> previousPrincipalVariation) {
        Constants.Side sideToMove = gameState.getFriendlySide();
        boolean isMaximizingPlayer = (sideToMove == Constants.Side.WHITE);
        this.previousPrincipalVariation = previousPrincipalVariation;

        return alphaBetaSearch(
                depth, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, isMaximizingPlayer, true);
    }

    private SearchResult alphaBetaSearch(
            int remainingDepth,
            float alpha,
            float beta,
            boolean isMaximizingPlayer,
            boolean isRoot) {
        ArrayList<Move> principalVariation = new ArrayList<>();
        if (gameState.isHalfMoveClockAt50() || gameState.isRepeatedPosition()) {
            // if either of these is true, we will consider the position a draw
            return new SearchResult(0f, principalVariation);
        }

        // we have reached the end! return the eval
        if (remainingDepth == 0) {
            float eval = quiesceSearch(alpha, beta, isMaximizingPlayer);
            return new SearchResult(eval, principalVariation);
        }

        // TODO: Check if i can get rid of this.
        // If we are the maximizing player, the score needs to be negative
        float mostExtremeEval =
                isMaximizingPlayer ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;

        // if we can't find any move to play, we just got checkmated or the game is stalemated
        boolean noLegalMoves = true;
        Constants.Side activeSide = GameState.getInstance().getFriendlySide();

        ArrayList<Move> moves = getOrderedMoves();
        for (Move move : moves) {
            if (stopSearchingImmediately.get()) {
                return new SearchResult(mostExtremeEval, principalVariation);
            }
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
            SearchResult childSearchResult =
                    alphaBetaSearch(remainingDepth - 1, alpha, beta, !isMaximizingPlayer, false);

            float eval = childSearchResult.eval();

            // update alpha beta, mostExtremeEval and bestMove depending on isMaximizingPlayer and
            // isRoot
            if (isMaximizingPlayer) {
                if (eval > mostExtremeEval) {
                    if (isRoot) {
                        bestMove = move;
                    }

                    principalVariation = new ArrayList<>();
                    principalVariation.add(move);
                    principalVariation.addAll(childSearchResult.principalVariation());
                }
                alpha = Math.max(alpha, eval);
                mostExtremeEval = Math.max(mostExtremeEval, eval);
            } else {
                if (eval < mostExtremeEval) {
                    if (isRoot) {
                        bestMove = move;
                    }
                    // TODO: Remove code duplication from above
                    principalVariation = new ArrayList<>();
                    principalVariation.add(move);
                    principalVariation.addAll(childSearchResult.principalVariation());
                }
                beta = Math.min(beta, eval);
                mostExtremeEval = Math.min(mostExtremeEval, eval);
            }

            gameState.unmakeMove(move);

            // pruning!
            if (beta <= alpha) return new SearchResult(mostExtremeEval, principalVariation);
        }

        // if we can't make any move
        if (noLegalMoves) {
            // and we are in check
            if (KingInCheckDecider.isKingUnderAttack(activeSide)) {
                // its checkmate
                float mateEval =
                        isMaximizingPlayer
                                ? -WE_GOT_CHECKMATED_EVAL - remainingDepth
                                : WE_GOT_CHECKMATED_EVAL + remainingDepth;
                return new SearchResult(mateEval, principalVariation);
                // else if we aren't in check
            } else {
                // its draw
                return new SearchResult(0f, principalVariation);
            }
        }

        return new SearchResult(mostExtremeEval, principalVariation);
    }

    private ArrayList<Move> getOrderedMoves() {
        // PERFORMANCE: 41% of the time is spend on generating an sorting
        // 24% for generating, 17% for sorting
        ArrayList<Move> moves = MoveGenerator.generatePseudoLegalMoves();
        // MVV-VLA (Most Valuable Victim - Least Valuable Aggressor)
        // https://www.chessprogramming.org/MVV-LVA
        Comparator<Move> comparator =
                (m1, m2) ->
                        Float.compare(
                                calculatePieceValueDifference(m1),
                                calculatePieceValueDifference(m2));
        moves.sort(comparator);

        if (!previousPrincipalVariation.isEmpty()) {
            Move pvMove = previousPrincipalVariation.removeFirst();

            moves.remove(pvMove);
            moves.addFirst(pvMove);
        }

        return moves;
    }

    private float quiesceSearch(float alpha, float beta, boolean isMaximizingPlayer) {
        float eval = Evaluator.calculateEvaluation(GameState.getInstance());

        // Stand pat - if the current position is already good enough
        if (isMaximizingPlayer) {
            if (eval >= beta) return beta;
            alpha = Math.max(alpha, eval);
        } else {
            if (eval <= alpha) return alpha;
            beta = Math.min(beta, eval);
        }

        float bestEval = eval;
        ArrayList<Move> moves = MoveGenerator.generatePseudoLegalMoves();
        // TODO: Currently this also removes checks. It should not.
        moves.removeIf(move -> move.capturedPieceType() == Constants.PieceType.EMPTY);

        // Sort captures by MVV-LVA
        moves.sort(
                (m1, m2) ->
                        Float.compare(
                                calculatePieceValueDifference(m1),
                                calculatePieceValueDifference(m2)));

        Constants.Side activeSide = GameState.getInstance().getFriendlySide();

        for (Move move : moves) {
            if (stopSearchingImmediately.get()) {
                return bestEval;
            }

            gameState.makeMove(move);

            if (KingInCheckDecider.isKingUnderAttack(activeSide)) {
                gameState.unmakeMove(move);
                continue;
            }

            float score = quiesceSearch(alpha, beta, !isMaximizingPlayer);

            gameState.unmakeMove(move);

            if (isMaximizingPlayer) {
                bestEval = Math.max(bestEval, score);
                alpha = Math.max(alpha, score);
            } else {
                bestEval = Math.min(bestEval, score);
                beta = Math.min(beta, score);
            }

            if (beta <= alpha) {
                break;
            }
        }

        return bestEval;
    }

    private float calculatePieceValueDifference(Move move) {
        return Evaluator.getMaterialValue(
                        GameState.getInstance()
                                .getBoardRepresentation()
                                .getPieceAt(move.fromX(), move.fromY()))
                - Evaluator.getMaterialValue(move.capturedPieceType());
    }

    public int getNodesSearched() {
        return nodesSearched;
    }
}
