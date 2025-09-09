/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.backend.state.game;

import de.janfrase.blunder.engine.backend.movegen.Move;
import de.janfrase.blunder.engine.backend.state.game.irreversibles.CastlingRights;
import de.janfrase.blunder.engine.backend.state.game.irreversibles.IrreversibleData;
import de.janfrase.blunder.utility.Constants;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

/**
 * The ZobristHash class implements a hashing mechanism used to uniquely represent a game state
 * in chess using Zobrist hashing. Zobrist hashing involves XORing random values associated with
 * the state of a chessboard (piece placement, castling rights, en passant, and whose turn it is)
 * to generate a unique hash for that state.
 * <p>
 * This class precomputes random keys for all potential states and uses them during the game to
 * update the hash incrementally, making it efficient for use in scenarios such as search trees.
 * <p>
 * For more details take a look at: <a href="https://www.chessprogramming.org/Zobrist_Hashing">Zobrist Hashing</a>
 */
class ZobristHasher {

    /**
     * Admittedly, this is a bit cursed.
     * This maps from Side -> another map.
     * This second map maps from PieceType -> long[][].
     * <p>
     * The purpose of this is to have a random value to xor with for each side, piece and square.
     */
    private static final EnumMap<Constants.Side, EnumMap<Constants.PieceType, long[][]>>
            pieceArraysMap;

    /**
     * One random long for each possible right (example for one right: white-long castle (yes or no)).
     */
    private static final long[] castlingRightsArray;

    private static final int WHITE_LONG_CASTLE = 0;
    private static final int WHITE_SHORT_CASTLE = 1;
    private static final int BLACK_LONG_CASTLE = 2;
    private static final int BLACK_SHORT_CASTLE = 3;

    /**
     * Random long for black is moving.
     */
    private static final long sideToMoveIsBlack;

    /**
     * One random long for each possible en passant file.
     */
    private static final long[] enPassantFileArray;

    /*
     * This initializes all the necessary values with random longs.
     */
    static {
        Random random = new Random();

        castlingRightsArray = random.longs(4).toArray();

        sideToMoveIsBlack = random.nextLong();

        enPassantFileArray = random.longs(8).toArray();

        // init pieceArrays with empty values
        pieceArraysMap = new EnumMap<>(Constants.Side.class);
        pieceArraysMap.put(Constants.Side.WHITE, new EnumMap<>(Constants.PieceType.class));
        pieceArraysMap.put(Constants.Side.BLACK, new EnumMap<>(Constants.PieceType.class));

        // iterate over white black
        for (Constants.Side side : Constants.Side.values()) {
            if (side == Constants.Side.EMPTY) continue;

            // iterate over pawn, rook etc
            for (Constants.PieceType pieceType : Constants.PieceType.values()) {
                if (pieceType == Constants.PieceType.EMPTY) continue;

                // get the relevant map
                EnumMap<Constants.PieceType, long[][]> map = pieceArraysMap.get(side);

                // create the array with a random number for each position
                long[][] boardArray =
                        new long[Constants.BOARD_SIDE_LENGTH][Constants.BOARD_SIDE_LENGTH];

                // fill it with random values
                for (int i = 0; i < Constants.BOARD_SIDE_LENGTH; i++) {
                    boardArray[i] = random.longs(Constants.BOARD_SIDE_LENGTH).toArray();
                }

                // put it where it belongs
                map.put(pieceType, boardArray);
            }
        }
    }

    private long zobristHash = 0;

    protected ZobristHasher() {}

    protected long getZobristHash() {
        return zobristHash;
    }

    /**
     * Initializes the Zobrist hash for the given game state by encoding the various aspects of the state,
     * including piece positions, player turn, en passant rights, and castling rights.
     * <p>
     * This should ONLY be called ONCE and after that be incrementally updated. Otherwise, it's VERY slow.
     *
     * @param gameState The current state of the game, including board representation,
     *                  player turn, and irreversible data such as en passant and castling rights.
     */
    protected void initZobristHash(GameState gameState) {
        // piece positions
        for (int x = 0; x < Constants.BOARD_SIDE_LENGTH; x++) {
            for (int y = 0; y < Constants.BOARD_SIDE_LENGTH; y++) {
                Constants.PieceType piece = gameState.getBoardRepresentation().getPieceAt(x, y);
                Constants.Side side = gameState.getBoardRepresentation().getSideAt(x, y);

                if (piece == Constants.PieceType.EMPTY) continue;

                zobristHash ^= pieceArraysMap.get(side).get(piece)[x][y];
            }
        }

        // player turn
        if (!gameState.isWhitesTurn) zobristHash ^= sideToMoveIsBlack;

        // en passant
        gameState
                .getIrreversibleData()
                .enPassantX()
                .ifPresent(
                        enPassantX -> {
                            long enPassantRandom = enPassantFileArray[enPassantX];
                            zobristHash ^= enPassantRandom;
                        });

        // castling
        boolean whiteLongCastle =
                gameState.getIrreversibleData().castlingRights().whiteLongCastle();
        boolean whiteShortCastle =
                gameState.getIrreversibleData().castlingRights().whiteShortCastle();
        boolean blackLongCastle =
                gameState.getIrreversibleData().castlingRights().blackLongCastle();
        boolean blackShortCastle =
                gameState.getIrreversibleData().castlingRights().blackShortCastle();

        if (whiteLongCastle) zobristHash ^= castlingRightsArray[WHITE_LONG_CASTLE];
        if (whiteShortCastle) zobristHash ^= castlingRightsArray[WHITE_SHORT_CASTLE];
        if (blackLongCastle) zobristHash ^= castlingRightsArray[BLACK_LONG_CASTLE];
        if (blackShortCastle) zobristHash ^= castlingRightsArray[BLACK_SHORT_CASTLE];
    }

    /*
     * -------------------------------
     * MAKE SECTION
     * -------------------------------
     */

    /**
     * Updates the Zobrist hash to reflect the changes in the game state caused by a move.
     * This works BOTH for making and unmaking a move! This is a very happy side effect of xor being its own inverse.
     * <p>
     * This method modifies the hash by handling the removal and addition of pieces, as well as flipping the turn side
     * and updating special rules like en passant and castling.
     *
     * @param move                The move that was made, containing the starting and ending positions,
     *                            and any captured piece or special move type information.
     * @param movedPieceType      The type of the piece that made the move (e.g., pawn, knight, etc.).
     * @param movedSide           The side (white or black) of the piece that made the move.
     * @param oldIrreversibleData The prior state of irreversible data, such as en passant and castling rights,
     *                            before the move was executed.
     * @param newIrreversibleData The updated state of irreversible data after the move has been executed.
     */
    protected void updateZobristHashAfterMove(
            Move move,
            Constants.PieceType movedPieceType,
            Constants.Side movedSide,
            IrreversibleData oldIrreversibleData,
            IrreversibleData newIrreversibleData) {
        // remove the piece
        Map<Constants.PieceType, long[][]> pieceToHashValueMap = pieceArraysMap.get(movedSide);
        // TODO: FIX THIS!
        if (pieceToHashValueMap == null) {
            System.out.println(move.toString());
            System.out.println(StatePrinter.stateToString());
        }
        zobristHash ^= pieceToHashValueMap.get(movedPieceType)[move.fromX()][move.fromY()];

        // remove the captured piece if it exists
        Constants.Side enemySide =
                movedSide == Constants.Side.WHITE ? Constants.Side.BLACK : Constants.Side.WHITE;
        Constants.PieceType capturedPieceType = move.capturedPieceType();
        if (capturedPieceType != Constants.PieceType.EMPTY) {
            if (move.moveType() != Move.MoveType.EP_CAPTURE)
                zobristHash ^=
                        pieceArraysMap.get(enemySide)
                                .get(capturedPieceType)[move.toX()][move.toY()];
            else {
                int yOffset = GameState.getYOffsetOnEnPassantCapture(movedSide);
                zobristHash ^=
                        pieceArraysMap.get(enemySide)
                                .get(capturedPieceType)[move.toX()][move.toY() + yOffset];
            }
        }
        // add the new piece
        zobristHash ^= pieceArraysMap.get(movedSide).get(movedPieceType)[move.toX()][move.toY()];

        // flip the side
        zobristHash ^= sideToMoveIsBlack;

        this.updateEnPassant(oldIrreversibleData, newIrreversibleData, move);
        this.updateCastlingRights(oldIrreversibleData, newIrreversibleData);
    }

    private void updateEnPassant(
            IrreversibleData oldIrreversibleData, IrreversibleData newIrreversibleData, Move move) {
        // remove the old en passant position if it exists
        oldIrreversibleData
                .enPassantX()
                .ifPresent(enPassantX -> zobristHash ^= enPassantFileArray[enPassantX]);
        newIrreversibleData
                .enPassantX()
                .ifPresent(enPassantX -> zobristHash ^= enPassantFileArray[enPassantX]);

        // add new en passant position if it exists
        if (move.moveType() != Move.MoveType.DOUBLE_PAWN_PUSH) return;

        zobristHash ^= enPassantFileArray[move.toX()];
    }

    private void updateCastlingRights(
            IrreversibleData oldIrreversibleData, IrreversibleData newIrreversibleData) {
        CastlingRights oldCastlingRights = oldIrreversibleData.castlingRights();
        CastlingRights newCastlingRights = newIrreversibleData.castlingRights();

        boolean oldWhiteLongCastle = oldCastlingRights.whiteLongCastle();
        boolean oldWhiteShortCastle = oldCastlingRights.whiteShortCastle();
        boolean oldBlackLongCastle = oldCastlingRights.blackLongCastle();
        boolean oldBlackShortCastle = oldCastlingRights.blackShortCastle();

        boolean newWhiteLongCastle = newCastlingRights.whiteLongCastle();
        boolean newWhiteShortCastle = newCastlingRights.whiteShortCastle();
        boolean newBlackLongCastle = newCastlingRights.blackLongCastle();
        boolean newBlackShortCastle = newCastlingRights.blackShortCastle();

        if (oldWhiteLongCastle != newWhiteLongCastle)
            zobristHash ^= castlingRightsArray[WHITE_LONG_CASTLE];
        if (oldWhiteShortCastle != newWhiteShortCastle)
            zobristHash ^= castlingRightsArray[WHITE_SHORT_CASTLE];
        if (oldBlackLongCastle != newBlackLongCastle)
            zobristHash ^= castlingRightsArray[BLACK_LONG_CASTLE];
        if (oldBlackShortCastle != newBlackShortCastle)
            zobristHash ^= castlingRightsArray[BLACK_SHORT_CASTLE];
    }
}
