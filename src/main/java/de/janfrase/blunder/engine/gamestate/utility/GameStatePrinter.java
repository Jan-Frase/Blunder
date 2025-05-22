package de.janfrase.blunder.engine.gamestate.utility;

import de.janfrase.blunder.engine.gamestate.GameState;
import de.janfrase.blunder.utility.Constants;

import static de.janfrase.blunder.engine.gamestate.utility.PieceToCharacterConstants.mapToAscii;
import static de.janfrase.blunder.engine.gamestate.utility.PieceToCharacterConstants.mapToUnicode;

/**
 * The BoardPrinter class provides utilities for converting a chess board representation
 * into a human-readable string format. It supports rendering the board using either
 * Unicode or ASCII characters for the chess pieces.
 */
public class GameStatePrinter {

    private static final boolean USE_ASCII = false;
    private static final String LIGHT_SQUARE = "◻";
    private static final String DARK_SQUARE = "◼";


    private static String getStringRepresentation(Constants.PieceType pieceType, Constants.Side color, int x, int y) {
        String piece = USE_ASCII ? mapToAscii.get(color).get(pieceType).getCharacter() : mapToUnicode.get(color).get(pieceType);
        if(pieceType != Constants.PieceType.EMPTY) {
            return piece;
        }

        return ((x + y) % 2 == 0) ? LIGHT_SQUARE : DARK_SQUARE;
    }

    public static String print(GameState gameState) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        for (int y = 0; y < Constants.BOARD_WIDTH; y++) {
            for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
                Constants.Side side = gameState.getBoardRepresentation().getSideAtPosition(x, y);
                Constants.PieceType pieceType = gameState.getBoardRepresentation().getPieceAtPosition(x, y);
                sb.append(getStringRepresentation(pieceType, side, x, y));

            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
