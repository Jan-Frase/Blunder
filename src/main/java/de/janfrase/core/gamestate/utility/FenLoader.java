package de.janfrase.core.gamestate.utility;

import de.janfrase.core.gamestate.GameState;
import de.janfrase.core.gamestate.board.BoardRepresentation;

/**
 * See: <a href="https://www.chessprogramming.org/Forsyth-Edwards_Notation">Chess programming wiki.</a>
 * Default String is rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
 */
public class FenLoader {
    private static final int PIECE_PLACEMENT = 0;
    private static final int SIDE_TO_MOVE = 1;
    private static final int CASTLING_ABILITY = 2;
    private static final int EN_PASSANT_TARGET_SQUARE = 3;
    private static final int HALF_MOVE_CLOCK = 4;
    private static final int FULL_MOVE_COUNTER = 5;

    public static void loadFenString(String fenString, GameState gameState) {
        String[] fenStringParts = fenString.split(" ");

        parsePiecePlacement(fenStringParts[PIECE_PLACEMENT], gameState.getBoardRepresentation());
    }

    private static void parsePiecePlacement(String piecePlacement, BoardRepresentation boardRepresentation) {

    }
}
