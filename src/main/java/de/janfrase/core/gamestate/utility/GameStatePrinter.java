package de.janfrase.core.gamestate.utility;

import de.janfrase.core.gamestate.GameState;
import de.janfrase.utility.Constants;
import org.javatuples.Pair;

import static de.janfrase.core.gamestate.utility.PieceToCharacterConstants.mapToAscii;
import static de.janfrase.core.gamestate.utility.PieceToCharacterConstants.mapToUnicode;

/**
 * The BoardPrinter class provides utilities for converting a chess board representation
 * into a human-readable string format. It supports rendering the board using either
 * Unicode or ASCII characters for the chess pieces.
 */
public class GameStatePrinter {

    private static final boolean USE_ASCII = false;


    private static String getStringRepresentation(Constants.PieceType pieceType, Constants.Side color) {
        if( USE_ASCII )
            return mapToAscii.get(color).get(pieceType).getCharacter();

        return mapToUnicode.get(color).get(pieceType);
    }

    public static String print(GameState gameState) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        for (int y = 0; y < Constants.BOARD_WIDTH; y++) {
            for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
                Pair<Constants.PieceType, Constants.Side> tile = gameState.getBoardRepresentation().getSquare(x, y);

                sb.append(getStringRepresentation(tile.getValue0(), tile.getValue1()));

            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
