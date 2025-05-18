package de.janfrase.core.gamestate.utility;

import de.janfrase.utility.Constants;
import org.javatuples.Pair;

import java.util.EnumMap;


public class PieceToCharacterConstants {


    // A map from Ascii Char -> (Color, Piece) :)
    // A bit ugly but also a bit funny - imma leave it in.
    static final EnumMap<AsciiCharacter, Pair<Constants.Colors, Constants.Piece>> mapFromAscii = new EnumMap<>(AsciiCharacter.class) {{
        put(AsciiCharacter.EMPTY, new Pair<>(Constants.Colors.EMPTY, Constants.Piece.EMPTY));
        put(AsciiCharacter.WHITE_KING, new Pair<>(Constants.Colors.WHITE, Constants.Piece.KING));
        put(AsciiCharacter.WHITE_QUEEN, new Pair<>(Constants.Colors.WHITE, Constants.Piece.QUEEN));
        put(AsciiCharacter.WHITE_ROOK, new Pair<>(Constants.Colors.WHITE, Constants.Piece.ROOK));
        put(AsciiCharacter.WHITE_BISHOP, new Pair<>(Constants.Colors.WHITE, Constants.Piece.BISHOP));
        put(AsciiCharacter.WHITE_KNIGHT, new Pair<>(Constants.Colors.WHITE, Constants.Piece.KNIGHT));
        put(AsciiCharacter.WHITE_PAWN, new Pair<>(Constants.Colors.WHITE, Constants.Piece.PAWN));
        put(AsciiCharacter.BLACK_KING, new Pair<>(Constants.Colors.BLACK, Constants.Piece.KING));
        put(AsciiCharacter.BLACK_QUEEN, new Pair<>(Constants.Colors.BLACK, Constants.Piece.QUEEN));
        put(AsciiCharacter.BLACK_ROOK, new Pair<>(Constants.Colors.BLACK, Constants.Piece.ROOK));
        put(AsciiCharacter.BLACK_BISHOP, new Pair<>(Constants.Colors.BLACK, Constants.Piece.BISHOP));
        put(AsciiCharacter.BLACK_KNIGHT, new Pair<>(Constants.Colors.BLACK, Constants.Piece.KNIGHT));
        put(AsciiCharacter.BLACK_PAWN, new Pair<>(Constants.Colors.BLACK, Constants.Piece.PAWN));
    }};

    // A map from (Color, Piece) -> Ascii Char :)
    // A bit ugly but also a bit funny - imma leave it in.
    static final EnumMap<Constants.Colors, EnumMap<Constants.Piece, AsciiCharacter>> mapToAscii = new EnumMap<>(Constants.Colors.class) {{
        put(Constants.Colors.WHITE, new EnumMap<>(Constants.Piece.class) {{
            put(Constants.Piece.KING, AsciiCharacter.WHITE_KING);
            put(Constants.Piece.QUEEN, AsciiCharacter.WHITE_QUEEN);
            put(Constants.Piece.ROOK, AsciiCharacter.WHITE_ROOK);
            put(Constants.Piece.BISHOP, AsciiCharacter.WHITE_BISHOP);
            put(Constants.Piece.KNIGHT, AsciiCharacter.WHITE_KNIGHT);
            put(Constants.Piece.PAWN, AsciiCharacter.WHITE_PAWN);
            put(Constants.Piece.EMPTY, AsciiCharacter.EMPTY);
        }});
        put(Constants.Colors.BLACK, new EnumMap<>(Constants.Piece.class) {{
            put(Constants.Piece.KING, AsciiCharacter.BLACK_KING);
            put(Constants.Piece.QUEEN, AsciiCharacter.BLACK_QUEEN);
            put(Constants.Piece.ROOK, AsciiCharacter.BLACK_ROOK);
            put(Constants.Piece.BISHOP, AsciiCharacter.BLACK_BISHOP);
            put(Constants.Piece.KNIGHT, AsciiCharacter.BLACK_KNIGHT);
            put(Constants.Piece.PAWN, AsciiCharacter.BLACK_PAWN);
            put(Constants.Piece.EMPTY, AsciiCharacter.EMPTY);
        }});
        put(Constants.Colors.EMPTY, new EnumMap<>(Constants.Piece.class) {{
            put(Constants.Piece.EMPTY, AsciiCharacter.EMPTY);
        }});
    }};
    // All the Unicode constants.
    // TODO: Convert this to an enum?
    private static final String UNICODE_WHITE_KING = "♔";

    /*
     * ---------------
     * Unicode section
     * ---------------
     */
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
    static final EnumMap<Constants.Colors, EnumMap<Constants.Piece, String>> mapToUnicode = new EnumMap<>(Constants.Colors.class) {{
        put(Constants.Colors.WHITE, new EnumMap<>(Constants.Piece.class) {{
            put(Constants.Piece.KING, UNICODE_WHITE_KING);
            put(Constants.Piece.QUEEN, UNICODE_WHITE_QUEEN);
            put(Constants.Piece.ROOK, UNICODE_WHITE_ROOK);
            put(Constants.Piece.BISHOP, UNICODE_WHITE_BISHOP);
            put(Constants.Piece.KNIGHT, UNICODE_WHITE_KNIGHT);
            put(Constants.Piece.PAWN, UNICODE_WHITE_PAWN);
            put(Constants.Piece.EMPTY, AsciiCharacter.EMPTY.getCharacter());
        }});
        put(Constants.Colors.BLACK, new EnumMap<>(Constants.Piece.class) {{
            put(Constants.Piece.KING, UNICODE_BLACK_KING);
            put(Constants.Piece.QUEEN, UNICODE_BLACK_QUEEN);
            put(Constants.Piece.ROOK, UNICODE_BLACK_ROOK);
            put(Constants.Piece.BISHOP, UNICODE_BLACK_BISHOP);
            put(Constants.Piece.KNIGHT, UNICODE_BLACK_KNIGHT);
            put(Constants.Piece.PAWN, UNICODE_BLACK_PAWN);
            put(Constants.Piece.EMPTY, AsciiCharacter.EMPTY.getCharacter());
        }});
        put(Constants.Colors.EMPTY, new EnumMap<>(Constants.Piece.class) {{
            put(Constants.Piece.EMPTY, AsciiCharacter.EMPTY.getCharacter());
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
    }

}
