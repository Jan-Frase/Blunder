/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.search;

import de.janfrase.blunder.engine.backend.movegen.Move;
import java.util.List;

public record SearchLimitations(
        List<Move> searchmoves,
        boolean ponder,
        int depth,
        int nodes,
        int mate,
        int moveTime,
        boolean infinite) {}
