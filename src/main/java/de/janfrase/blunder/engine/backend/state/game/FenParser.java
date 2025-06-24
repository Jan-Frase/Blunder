/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.backend.state.game;

import de.janfrase.blunder.engine.backend.state.game.irreversibles.CastlingRights;
import de.janfrase.blunder.engine.backend.state.game.irreversibles.IrreversibleData;
import de.janfrase.blunder.utility.Constants;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * See: <a href="https://www.chessprogramming.org/Forsyth-Edwards_Notation">Chess programming wiki.</a>
 */
public class FenParser {
    private static final Logger logger = LogManager.getLogger();
    private static final String STARTING_FEN =
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private static final String RANK_SEPARATOR = "/";
    private static final String SEGMENT_SEPARATOR = " ";
    public static final Map<String, Constants.PieceType> charToPieceMap =
            new HashMap<>() {
                {
                    put("K", Constants.PieceType.KING);
                    put("Q", Constants.PieceType.QUEEN);
                    put("R", Constants.PieceType.ROOK);
                    put("B", Constants.PieceType.BISHOP);
                    put("N", Constants.PieceType.KNIGHT);
                    put("P", Constants.PieceType.PAWN);
                    put("k", Constants.PieceType.KING);
                    put("q", Constants.PieceType.QUEEN);
                    put("r", Constants.PieceType.ROOK);
                    put("b", Constants.PieceType.BISHOP);
                    put("n", Constants.PieceType.KNIGHT);
                    put("p", Constants.PieceType.PAWN);
                }
            };

    public static void loadStartingPosition() {
        loadFenString(STARTING_FEN);
    }

    public static void loadFenString(String fenString) {
        logger.info("Starting fen parsing on : {}", fenString);
        GameState.resetGameState();
        String[] fenSegments = fenString.split(SEGMENT_SEPARATOR);

        parsePiecePlacement(fenSegments[FenSegments.PIECE_PLACEMENT.getIndex()]);
        parseSideToMove(fenSegments[FenSegments.SIDE_TO_MOVE.getIndex()]);

        parseIrreversibleData(
                fenSegments[FenSegments.CASTLING_ABILITY.getIndex()],
                fenSegments[FenSegments.EN_PASSANT_TARGET_SQUARE.getIndex()],
                fenSegments[FenSegments.HALF_MOVE_CLOCK.getIndex()]);

        parseFullMoveCounter(fenSegments[FenSegments.FULL_MOVE_COUNTER.getIndex()]);

        logger.trace("Finished fen parsing : {}", StatePrinter.stateToString());
    }

    private static void parsePiecePlacement(String piecePlacement) {
        int yPos = 0;
        for (String rank : piecePlacement.split(RANK_SEPARATOR)) {
            int xPos = 0;

            for (char c : rank.toCharArray()) {
                if (Character.isDigit(c)) {
                    xPos += Integer.parseInt(String.valueOf(c));
                } else {
                    // Get the piece type and color.
                    Constants.PieceType pieceType = charToPieceMap.get(String.valueOf(c));
                    Constants.Side pieceSide =
                            Character.isUpperCase(c) ? Constants.Side.WHITE : Constants.Side.BLACK;

                    GameState.getInstance()
                            .boardRepresentation
                            .setPieceAt(xPos, yPos, pieceType, pieceSide);

                    xPos++;
                }
            }
            yPos++;
        }
    }

    private static void parseSideToMove(String sideToMove) {
        GameState.getInstance().isWhitesTurn = "w".equals(sideToMove);
    }

    private static void parseIrreversibleData(
            String castlingAbility, String enPassantSquare, String halfMoveClock) {
        CastlingRights castlingRights = parseCastlingAbility(castlingAbility);
        OptionalInt enPassantTargetSquare = parseEnPassantSquare(enPassantSquare);
        int halfMoveCount = Integer.parseInt(halfMoveClock);

        // Clear the stack of irreversible data, as we're about to parse a new one.
        GameState.getInstance().irreversibleDataStack.clear();

        GameState.getInstance()
                .irreversibleDataStack
                .push(new IrreversibleData(castlingRights, enPassantTargetSquare, halfMoveCount));
    }

    private static CastlingRights parseCastlingAbility(String castlingAbility) {
        boolean whiteShortCastle = castlingAbility.indexOf('K') != -1;
        boolean whiteLongCastle = castlingAbility.indexOf('Q') != -1;
        boolean blackShortCastle = castlingAbility.indexOf('k') != -1;
        boolean blackLongCastle = castlingAbility.indexOf('q') != -1;

        return new CastlingRights(
                whiteLongCastle, whiteShortCastle, blackLongCastle, blackShortCastle);
    }

    private static OptionalInt parseEnPassantSquare(String enPassantSquare) {
        if ("-".equals(enPassantSquare)) {
            return OptionalInt.empty();
        }
        int xPos = enPassantSquare.charAt(0) - 'a';
        return OptionalInt.of(xPos);
    }

    private static void parseFullMoveCounter(String fullMoveCounter) {
        GameState.getInstance().fullMoveCounter = Integer.parseInt(fullMoveCounter);
    }

    private enum FenSegments {
        PIECE_PLACEMENT(0),
        SIDE_TO_MOVE(1),
        CASTLING_ABILITY(2),
        EN_PASSANT_TARGET_SQUARE(3),
        HALF_MOVE_CLOCK(4),
        FULL_MOVE_COUNTER(5);

        private final int index;

        FenSegments(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }
}
