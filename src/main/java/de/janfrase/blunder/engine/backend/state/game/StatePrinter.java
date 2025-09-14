/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.backend.state.game;

import de.janfrase.blunder.utility.Constants;
import java.util.EnumMap;

/**
 * The BoardPrinter class provides utilities for converting a chess board representation
 * into a human-readable string format. It supports rendering the board using either
 * Unicode or ASCII characters for the chess pieces.
 */
public class StatePrinter {
    private static final boolean USE_ASCII = false;
    private static final String LIGHT_SQUARE = "◻";
    private static final String DARK_SQUARE = "◼";

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

    // A map from (Color, Piece) -> Ascii Char :)
    // A bit ugly but also a bit funny - imma leave it in.
    static final EnumMap<Constants.Side, EnumMap<Constants.PieceType, AsciiCharacter>> mapToAscii =
            new EnumMap<>(Constants.Side.class) {
                {
                    put(
                            Constants.Side.WHITE,
                            new EnumMap<>(Constants.PieceType.class) {
                                {
                                    put(Constants.PieceType.KING, AsciiCharacter.WHITE_KING);
                                    put(Constants.PieceType.QUEEN, AsciiCharacter.WHITE_QUEEN);
                                    put(Constants.PieceType.ROOK, AsciiCharacter.WHITE_ROOK);
                                    put(Constants.PieceType.BISHOP, AsciiCharacter.WHITE_BISHOP);
                                    put(Constants.PieceType.KNIGHT, AsciiCharacter.WHITE_KNIGHT);
                                    put(Constants.PieceType.PAWN, AsciiCharacter.WHITE_PAWN);
                                    put(Constants.PieceType.EMPTY, AsciiCharacter.EMPTY);
                                }
                            });
                    put(
                            Constants.Side.BLACK,
                            new EnumMap<>(Constants.PieceType.class) {
                                {
                                    put(Constants.PieceType.KING, AsciiCharacter.BLACK_KING);
                                    put(Constants.PieceType.QUEEN, AsciiCharacter.BLACK_QUEEN);
                                    put(Constants.PieceType.ROOK, AsciiCharacter.BLACK_ROOK);
                                    put(Constants.PieceType.BISHOP, AsciiCharacter.BLACK_BISHOP);
                                    put(Constants.PieceType.KNIGHT, AsciiCharacter.BLACK_KNIGHT);
                                    put(Constants.PieceType.PAWN, AsciiCharacter.BLACK_PAWN);
                                    put(Constants.PieceType.EMPTY, AsciiCharacter.EMPTY);
                                }
                            });
                    put(
                            Constants.Side.EMPTY,
                            new EnumMap<>(Constants.PieceType.class) {
                                {
                                    put(Constants.PieceType.EMPTY, AsciiCharacter.EMPTY);
                                }
                            });
                }
            };

    /*
     * ---------------
     * Unicode section
     * ---------------
     */
    enum UnicodeCharacter {
        EMPTY(" "),
        WHITE_KING("♔"),
        WHITE_QUEEN("♕"),
        WHITE_ROOK("♖"),
        WHITE_BISHOP("♗"),
        WHITE_KNIGHT("♘"),
        WHITE_PAWN("♙"),
        BLACK_KING("♚"),
        BLACK_QUEEN("♛"),
        BLACK_ROOK("♜"),
        BLACK_BISHOP("♝"),
        BLACK_KNIGHT("♞"),
        BLACK_PAWN("♟");

        private final String character;

        UnicodeCharacter(String character) {
            this.character = character;
        }

        public String getCharacter() {
            return this.character;
        }
    }

    // A map from (Color, Piece) -> Unicode Char :)
    // A bit ugly but also a bit funny - imma leave it in.
    static final EnumMap<Constants.Side, EnumMap<Constants.PieceType, UnicodeCharacter>>
            mapToUnicode =
                    new EnumMap<>(Constants.Side.class) {
                        {
                            put(
                                    Constants.Side.WHITE,
                                    new EnumMap<>(Constants.PieceType.class) {
                                        {
                                            put(
                                                    Constants.PieceType.KING,
                                                    UnicodeCharacter.WHITE_KING);
                                            put(
                                                    Constants.PieceType.QUEEN,
                                                    UnicodeCharacter.WHITE_QUEEN);
                                            put(
                                                    Constants.PieceType.ROOK,
                                                    UnicodeCharacter.WHITE_ROOK);
                                            put(
                                                    Constants.PieceType.BISHOP,
                                                    UnicodeCharacter.WHITE_BISHOP);
                                            put(
                                                    Constants.PieceType.KNIGHT,
                                                    UnicodeCharacter.WHITE_KNIGHT);
                                            put(
                                                    Constants.PieceType.PAWN,
                                                    UnicodeCharacter.WHITE_PAWN);
                                            put(Constants.PieceType.EMPTY, UnicodeCharacter.EMPTY);
                                        }
                                    });
                            put(
                                    Constants.Side.BLACK,
                                    new EnumMap<>(Constants.PieceType.class) {
                                        {
                                            put(
                                                    Constants.PieceType.KING,
                                                    UnicodeCharacter.BLACK_KING);
                                            put(
                                                    Constants.PieceType.QUEEN,
                                                    UnicodeCharacter.BLACK_QUEEN);
                                            put(
                                                    Constants.PieceType.ROOK,
                                                    UnicodeCharacter.BLACK_ROOK);
                                            put(
                                                    Constants.PieceType.BISHOP,
                                                    UnicodeCharacter.BLACK_BISHOP);
                                            put(
                                                    Constants.PieceType.KNIGHT,
                                                    UnicodeCharacter.BLACK_KNIGHT);
                                            put(
                                                    Constants.PieceType.PAWN,
                                                    UnicodeCharacter.BLACK_PAWN);
                                            put(Constants.PieceType.EMPTY, UnicodeCharacter.EMPTY);
                                        }
                                    });
                            put(
                                    Constants.Side.EMPTY,
                                    new EnumMap<>(Constants.PieceType.class) {
                                        {
                                            put(Constants.PieceType.EMPTY, UnicodeCharacter.EMPTY);
                                        }
                                    });
                        }
                    };

    /**
     * Converts the current game state to a string representation.
     * The output includes various details of the game state such as move counters, turn information,
     * en passant coordinates, castling rights, and the board's piece layout.
     *
     * @return A string representation of the game state, formatted for readability.
     */
    public static String stateToString() {
        GameState gameState = GameState.getInstance();

        StringBuilder sb = new StringBuilder();

        sb.append("\n");

        sb.append("Full move counter: ").append(gameState.fullMoveCounter).append("\n");
        sb.append("Is whites turn: ").append(gameState.isWhitesTurn).append("\n");
        sb.append("Stack size: ").append(gameState.irreversibleDataStack.size()).append("\n");
        sb.append("Half move clock: ")
                .append(gameState.irreversibleDataStack.peek().halfMoveClock())
                .append("\n");
        sb.append("En Passant X: ")
                .append(gameState.irreversibleDataStack.peek().enPassantX())
                .append("\n");
        sb.append(gameState.irreversibleDataStack.peek().castlingRights()).append("\n");

        sb.append("\n");
        for (int y = 0; y < Constants.BOARD_SIDE_LENGTH; y++) {
            for (int x = 0; x < Constants.BOARD_SIDE_LENGTH; x++) {
                Constants.Side side = gameState.bitBoards.getSideAt(x, y);
                Constants.PieceType pieceType = gameState.bitBoards.getPieceAt(x, y);
                sb.append(getStringRepresentation(pieceType, side, x, y));
                sb.append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private static String getStringRepresentation(
            Constants.PieceType pieceType, Constants.Side color, int x, int y) {
        String piece =
                USE_ASCII
                        ? mapToAscii.get(color).get(pieceType).getCharacter()
                        : mapToUnicode.get(color).get(pieceType).getCharacter();
        if (pieceType != Constants.PieceType.EMPTY) {
            return piece;
        }

        return ((x + y) % 2 == 0) ? LIGHT_SQUARE : DARK_SQUARE;
    }
}
