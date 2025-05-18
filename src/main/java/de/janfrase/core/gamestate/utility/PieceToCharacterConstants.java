package de.janfrase.core.gamestate.utility;

import de.janfrase.utility.Constants;
import org.javatuples.Pair;

import java.util.EnumMap;


public class PieceToCharacterConstants {


    // A map from Ascii Char -> (Color, Piece) :)
    // A bit ugly but also a bit funny - imma leave it in.
    static final EnumMap<AsciiCharacter, Pair<Constants.Side, Constants.PieceType>> mapFromAscii = new EnumMap<>(AsciiCharacter.class) {{
        put(AsciiCharacter.EMPTY, new Pair<>(Constants.Side.EMPTY, Constants.PieceType.EMPTY));
        put(AsciiCharacter.WHITE_KING, new Pair<>(Constants.Side.WHITE, Constants.PieceType.KING));
        put(AsciiCharacter.WHITE_QUEEN, new Pair<>(Constants.Side.WHITE, Constants.PieceType.QUEEN));
        put(AsciiCharacter.WHITE_ROOK, new Pair<>(Constants.Side.WHITE, Constants.PieceType.ROOK));
        put(AsciiCharacter.WHITE_BISHOP, new Pair<>(Constants.Side.WHITE, Constants.PieceType.BISHOP));
        put(AsciiCharacter.WHITE_KNIGHT, new Pair<>(Constants.Side.WHITE, Constants.PieceType.KNIGHT));
        put(AsciiCharacter.WHITE_PAWN, new Pair<>(Constants.Side.WHITE, Constants.PieceType.PAWN));
        put(AsciiCharacter.BLACK_KING, new Pair<>(Constants.Side.BLACK, Constants.PieceType.KING));
        put(AsciiCharacter.BLACK_QUEEN, new Pair<>(Constants.Side.BLACK, Constants.PieceType.QUEEN));
        put(AsciiCharacter.BLACK_ROOK, new Pair<>(Constants.Side.BLACK, Constants.PieceType.ROOK));
        put(AsciiCharacter.BLACK_BISHOP, new Pair<>(Constants.Side.BLACK, Constants.PieceType.BISHOP));
        put(AsciiCharacter.BLACK_KNIGHT, new Pair<>(Constants.Side.BLACK, Constants.PieceType.KNIGHT));
        put(AsciiCharacter.BLACK_PAWN, new Pair<>(Constants.Side.BLACK, Constants.PieceType.PAWN));
    }};

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

        public String getCharacter() {
            return this.character;
        }

        public static AsciiCharacter getCharacter(String character) {
            return switch (character) {
                case " " -> EMPTY;
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
                default -> null;
            };
        }
    }


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
}
