package de.janfrase.core.gamestate.utility;

import de.janfrase.core.gamestate.board.BoardRepresentation;
import de.janfrase.utility.Constants;
import org.javatuples.Pair;

import static de.janfrase.core.gamestate.utility.PieceToCharacterConstants.mapToAscii;
import static de.janfrase.core.gamestate.utility.PieceToCharacterConstants.mapToUnicode;

/**
 * The BoardPrinter class provides utilities for converting a chess board representation
 * into a human-readable string format. It supports rendering the board using either
 * Unicode or ASCII characters for the chess pieces.
 */
public class BoardPrinter {

    private static final boolean USE_ASCII = false;


    private static String getStringRepresentation(Constants.Piece piece, Constants.Colors color) {
        if( USE_ASCII )
            return mapToAscii.get(color).get(piece).getCharacter();

        return mapToUnicode.get(color).get(piece);
    }

    public static String convertBoardToString(BoardRepresentation boardRepresentation) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        for (int y = 0; y < Constants.BOARD_WIDTH; y++) {
            for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
                Pair<Constants.Piece, Constants.Colors> tile = boardRepresentation.getTile(x, y);

                sb.append(getStringRepresentation(tile.getValue0(), tile.getValue1()));


            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
