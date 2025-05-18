package de.janfrase.core.board;

import de.janfrase.utility.Constants;
import org.javatuples.Pair;

import java.util.EnumMap;

/**
 * The BoardPrinter class provides utilities for converting a chess board representation
 * into a human-readable string format. It supports rendering the board using either
 * Unicode or ASCII characters for the chess pieces.
 */
public class BoardPrinter {

    private static final boolean USE_ASCII = false;

    private static final String EMPTY = " ";

    // All the Unicode constants.
    private static final String UNICODE_WHITE_KING = "♔";
    private static final String UNICODE_WHITE_QUEEN = "♕";
    private static final String UNICODE_WHITE_ROOK = "♖";
    private static final String UNICODE_WHITE_BISHOP = "♗";
    private static final String UNICODE_WHITE_KNIGHT = "♘";
    private static final String UNICODE_WHITE_PAWN = "♙";
    private static final String UNICODE_BLACK_KING = "♚";
    private static final String UNICODE_BLACK_QUEEN = "♛";
    private static final String UNICODE_BLACK_ROOK = "♜";
    private static final String UNICODE_BLACK_BISHOP = "♝";
    private static final String UNICODE_BLACK_KNIGHT = "♞";
    private static final String UNICODE_BLACK_PAWN = "♟";

    // A bit ugly but also a bit funny - imma leave it in.
    private static final EnumMap<Constants.Colors, EnumMap<Constants.Piece, String>> unicodeCharMap = new EnumMap<>(Constants.Colors.class) {{
        put(Constants.Colors.WHITE, new EnumMap<>(Constants.Piece.class) {{
            put(Constants.Piece.KING, UNICODE_WHITE_KING);
            put(Constants.Piece.QUEEN, UNICODE_WHITE_QUEEN);
            put(Constants.Piece.ROOK, UNICODE_WHITE_ROOK);
            put(Constants.Piece.BISHOP, UNICODE_WHITE_BISHOP);
            put(Constants.Piece.KNIGHT, UNICODE_WHITE_KNIGHT);
            put(Constants.Piece.PAWN, UNICODE_WHITE_PAWN);
            put(Constants.Piece.EMPTY, EMPTY);
        }});
        put(Constants.Colors.BLACK, new EnumMap<>(Constants.Piece.class) {{
            put(Constants.Piece.KING, UNICODE_BLACK_KING);
            put(Constants.Piece.QUEEN, UNICODE_BLACK_QUEEN);
            put(Constants.Piece.ROOK, UNICODE_BLACK_ROOK);
            put(Constants.Piece.BISHOP, UNICODE_BLACK_BISHOP);
            put(Constants.Piece.KNIGHT, UNICODE_BLACK_KNIGHT);
            put(Constants.Piece.PAWN, UNICODE_BLACK_PAWN);
            put(Constants.Piece.EMPTY, EMPTY);
        }});
        put(Constants.Colors.EMPTY, new EnumMap<>(Constants.Piece.class) {{
            put(Constants.Piece.EMPTY, EMPTY);
        }});
    }};

    // All the Unicode constants.
    private static final String WHITE_KING = "K";
    private static final String WHITE_QUEEN = "Q";
    private static final String WHITE_ROOK = "R";
    private static final String WHITE_BISHOP = "B";
    private static final String WHITE_KNIGHT = "N";
    private static final String WHITE_PAWN = "P";
    private static final String BLACK_KING = "k";
    private static final String BLACK_QUEEN = "q";
    private static final String BLACK_ROOK = "r";
    private static final String BLACK_BISHOP = "b";
    private static final String BLACK_KNIGHT = "n";
    private static final String BLACK_PAWN = "p";

    // A bit ugly but also a bit funny - imma leave it in.
    private static final EnumMap<Constants.Colors, EnumMap<Constants.Piece, String>> asciiCharMap = new EnumMap<>(Constants.Colors.class) {{
        put(Constants.Colors.WHITE, new EnumMap<>(Constants.Piece.class) {{
            put(Constants.Piece.KING, WHITE_KING);
            put(Constants.Piece.QUEEN, WHITE_QUEEN);
            put(Constants.Piece.ROOK, WHITE_ROOK);
            put(Constants.Piece.BISHOP, WHITE_BISHOP);
            put(Constants.Piece.KNIGHT, WHITE_KNIGHT);
            put(Constants.Piece.PAWN, WHITE_PAWN);
            put(Constants.Piece.EMPTY, EMPTY);
        }});
        put(Constants.Colors.BLACK, new EnumMap<>(Constants.Piece.class) {{
            put(Constants.Piece.KING, BLACK_KING);
            put(Constants.Piece.QUEEN, BLACK_QUEEN);
            put(Constants.Piece.ROOK, BLACK_ROOK);
            put(Constants.Piece.BISHOP, BLACK_BISHOP);
            put(Constants.Piece.KNIGHT, BLACK_KNIGHT);
            put(Constants.Piece.PAWN, BLACK_PAWN);
            put(Constants.Piece.EMPTY, EMPTY);
        }});
        put(Constants.Colors.EMPTY, new EnumMap<>(Constants.Piece.class) {{
            put(Constants.Piece.EMPTY, EMPTY);
        }});
    }};

    private static String getStringRepresentation(Constants.Piece piece, Constants.Colors color) {
        if( USE_ASCII )
            return asciiCharMap.get(color).get(piece);

        return unicodeCharMap.get(color).get(piece);
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
