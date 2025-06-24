/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.evaluation;

import de.janfrase.blunder.engine.backend.state.game.GameState;

public interface Evaluator {
    float calculateEvaluation(GameState gameState);
}
