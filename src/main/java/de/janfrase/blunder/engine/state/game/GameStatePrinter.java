package de.janfrase.blunder.engine.state.game;

import de.janfrase.blunder.utility.Constants;

import java.util.EnumMap;

/**
 * The BoardPrinter class provides utilities for converting a chess board representation
 * into a human-readable string format. It supports rendering the board using either
 * Unicode or ASCII characters for the chess pieces.
 */
public class GameStatePrinter {
    // A map from (Color, Piece) -> Ascii Char :)
    // A bit ugly but also a bit funny - imma leave it in.
    static final EnumMap<Constants.Side, EnumMap<Constants.PieceType, AsciiCharacter>> mapToAscii = new EnumMap<>(Constants.Side.class) {{
        put(Constants.Side.WHITE, new EnumMap<>(Constants.PieceType.class) {{
            put(Constants.PieceType.KING, AsciiCharacter.WHITE_KING);
            put(Constants.PieceType.QUEEN, AsciiCharacter.WHITE_QUEEN);
            put(Constants.PieceType.ROOK, AsciiCharacter.WHITE_ROOK);
            put(Constants.PieceType.BISHOP, AsciiCharacter.WHITE_BISHOP);
            put(Constants.PieceType.KNIGHT, AsciiCharacter.WHITE_KNIGHT);
            put(Constants.PieceType.PAWN, AsciiCharacter.WHITE_PAWN);
            put(Constants.PieceType.EMPTY, AsciiCharacter.EMPTY);
        }});
        put(Constants.Side.BLACK, new EnumMap<>(Constants.PieceType.class) {{
            put(Constants.PieceType.KING, AsciiCharacter.BLACK_KING);
            put(Constants.PieceType.QUEEN, AsciiCharacter.BLACK_QUEEN);
            put(Constants.PieceType.ROOK, AsciiCharacter.BLACK_ROOK);
            put(Constants.PieceType.BISHOP, AsciiCharacter.BLACK_BISHOP);
            put(Constants.PieceType.KNIGHT, AsciiCharacter.BLACK_KNIGHT);
            put(Constants.PieceType.PAWN, AsciiCharacter.BLACK_PAWN);
            put(Constants.PieceType.EMPTY, AsciiCharacter.EMPTY);
        }});
        put(Constants.Side.EMPTY, new EnumMap<>(Constants.PieceType.class) {{
            put(Constants.PieceType.EMPTY, AsciiCharacter.EMPTY);
        }});
    }};
    /*
     * ---------------
     * Unicode section
     * ---------------
     */
    // All the Unicode constants.
    // TODO: Convert this to an enum?
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
    // A map from (Color, Piece) -> Unicode Char :)
    // A bit ugly but also a bit funny - imma leave it in.
    static final EnumMap<Constants.Side, EnumMap<Constants.PieceType, String>> mapToUnicode = new EnumMap<>(Constants.Side.class) {{
        put(Constants.Side.WHITE, new EnumMap<>(Constants.PieceType.class) {{
            put(Constants.PieceType.KING, UNICODE_WHITE_KING);
            put(Constants.PieceType.QUEEN, UNICODE_WHITE_QUEEN);
            put(Constants.PieceType.ROOK, UNICODE_WHITE_ROOK);
            put(Constants.PieceType.BISHOP, UNICODE_WHITE_BISHOP);
            put(Constants.PieceType.KNIGHT, UNICODE_WHITE_KNIGHT);
            put(Constants.PieceType.PAWN, UNICODE_WHITE_PAWN);
            put(Constants.PieceType.EMPTY, AsciiCharacter.EMPTY.getCharacter());
        }});
        put(Constants.Side.BLACK, new EnumMap<>(Constants.PieceType.class) {{
            put(Constants.PieceType.KING, UNICODE_BLACK_KING);
            put(Constants.PieceType.QUEEN, UNICODE_BLACK_QUEEN);
            put(Constants.PieceType.ROOK, UNICODE_BLACK_ROOK);
            put(Constants.PieceType.BISHOP, UNICODE_BLACK_BISHOP);
            put(Constants.PieceType.KNIGHT, UNICODE_BLACK_KNIGHT);
            put(Constants.PieceType.PAWN, UNICODE_BLACK_PAWN);
            put(Constants.PieceType.EMPTY, AsciiCharacter.EMPTY.getCharacter());
        }});
        put(Constants.Side.EMPTY, new EnumMap<>(Constants.PieceType.class) {{
            put(Constants.PieceType.EMPTY, AsciiCharacter.EMPTY.getCharacter());
        }});
    }};

    public static String print() {
        GameState gameState = GameState.getInstance();

        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        for (int y = 0; y < Constants.BOARD_WIDTH; y++) {
            for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
                Constants.Side side = gameState.boardRepresentation.getSideAtPosition(x, y);
                Constants.PieceType pieceType = gameState.boardRepresentation.getPieceAtPosition(x, y);
                sb.append(getStringRepresentation(pieceType, side, x, y));

            }
            sb.append("\n");
        }

        return sb.toString();
    }


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

    enum AsciiCharacter {
        EMPTY(" "),
        WHITE_KING("K"),
        WHITE_QUEEN("Q"),
        WHITE_ROOK("R"),
        WHITE_BISHOP("B"),
        WHITE_KNIGHT("N"),
        WHITE_PAWN("P"),
        BLACK_KING("k"),
        BLACK_QUEEN("q"),
        BLACK_ROOK("r"),
        BLACK_BISHOP("b"),
        BLACK_KNIGHT("n"),
        BLACK_PAWN("p");

        private final String character;

        AsciiCharacter(String character) {
            this.character = character;
        }

        public static AsciiCharacter getCharacter(String character) {
            return switch (character) {
                case "K" -> WHITE_KING;
                case "Q" -> WHITE_QUEEN;
                case "R" -> WHITE_ROOK;
                case "B" -> WHITE_BISHOP;
                case "N" -> WHITE_KNIGHT;
                case "P" -> WHITE_PAWN;
                case "k" -> BLACK_KING;
                case "q" -> BLACK_QUEEN;
                case "r" -> BLACK_ROOK;
                case "b" -> BLACK_BISHOP;
                case "n" -> BLACK_KNIGHT;
                case "p" -> BLACK_PAWN;
                default -> EMPTY;
            };
        }

        public String getCharacter() {
            return this.character;
        }
    }
}
