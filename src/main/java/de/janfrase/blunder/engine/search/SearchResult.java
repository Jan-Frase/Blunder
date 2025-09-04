/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.search;

import de.janfrase.blunder.engine.backend.movegen.Move;
import java.util.ArrayList;

public record SearchResult(float eval, ArrayList<Move> principalVariation) {}
