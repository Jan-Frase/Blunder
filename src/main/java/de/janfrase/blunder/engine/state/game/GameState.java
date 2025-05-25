/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.state.game;

import de.janfrase.blunder.engine.movegen.Move;
import de.janfrase.blunder.engine.state.board.BoardRepresentation;
import de.janfrase.blunder.engine.state.game.irreversibles.CastlingRights;
import de.janfrase.blunder.engine.state.game.irreversibles.IrreversibleData;
import de.janfrase.blunder.utility.Constants;
import java.util.OptionalInt;
import java.util.Stack;

/**
 * The {@code GameState} class is responsible for managing the current state of a chess game.
 * It maintains game attributes such as board representation, move history, player turns, and move counters.
 * This class follows the Singleton design pattern, ensuring there is only one active instance during a game session.
 */
public class GameState {

    public static final int UP = 1;
    public static final int DOWN = -1;
    public static final int LEFT = -1;
    public static final int RIGHT = 1;
    public static final int RIGHT_X_ROOK_START = 7;
    public static final int LEFT_X_ROOK_START = 0;
    public static final int KING_STARTING_X = 4;

    /**
     * The singleton instance of the {@code GameState} class.
     * This ensures that only one instance of the {@code GameState} class exists at any time,
     * representing the current state of a chess game.
     * <p>
     * This instance is lazily initialized and can be accessed or reset using provided
     * static methods.
     */
    private static GameState instance = new GameState();

    final BoardRepresentation boardRepresentation;
    final Stack<IrreversibleData> irreversibleDataStack;
    boolean isWhitesTurn;
    int fullMoveCounter;

    private GameState() {
        this.boardRepresentation = new BoardRepresentation();
        this.irreversibleDataStack = new Stack<>();
        this.irreversibleDataStack.push(new IrreversibleData.Builder().buildDefault());

        this.isWhitesTurn = true;
        this.fullMoveCounter = 1;
    }

    /**
     * Returns the singleton instance of the {@code GameState} class.
     * The {@code GameState} class represents the current state of a chess game
     * and ensures that only one instance of this class exists at any given time.
     *
     * @return the singleton instance of {@code GameState}
     */
    public static GameState getInstance() {
        return instance;
    }

    /**
     * Resets the game state to its initial condition by reinitializing
     * the {@code GameState} singleton instance.
     * <p>
     * This method ensures the game state is reset to a newly instantiated default configuration, clearing any prior
     * game progress or data.
     * <p>
     * This is mostly here to easily implement unit tests.
     */
    public static void resetGameState() {
        instance = new GameState();
    }

    // ------------------------------
    // Make Section
    // ------------------------------

    private static void halfMoveRelatedMakeMove(
            Move move,
            Constants.PieceType fromPieceType,
            IrreversibleData.Builder irreversibleDataBuilder) {
        // now we can get to the edge cases :)
        boolean wasSomethingCaptured =
                move.moveType().equals(Move.MoveType.CAPTURE)
                        || move.moveType().equals(Move.MoveType.EP_CAPTURE)
                        || move.moveType().equals(Move.MoveType.ROOK_PROMOTION_CAPTURE)
                        || move.moveType().equals(Move.MoveType.KNIGHT_PROMOTION_CAPTURE)
                        || move.moveType().equals(Move.MoveType.BISHOP_PROMOTION_CAPTURE)
                        || move.moveType().equals(Move.MoveType.QUEEN_PROMOTION_CAPTURE);

        // half-move clock handling
        if (wasSomethingCaptured || fromPieceType.equals(Constants.PieceType.PAWN)) {
            // reset the half-move clock on capture or if a pawn was moved
            irreversibleDataBuilder.halfMoveClock(0);
        }
    }

    /**
     * Executes a move in the current game state.
     * <p>
     * Keep in mind that this executes moves regardless of legality.
     * This kind of check will have to happen after.
     * <p>
     * This method updates the board representation,
     * manages special cases (such as en passant, castling, promotions, and captures),
     * and updates the game's immutable state stack and other relevant data.
     *
     * @param move The move to be executed, represented by a {@link Move} object that contains
     *             the starting position, target position, and the type of move.
     */
    public void makeMove(Move move) {
        // set the square we are moving to, to have the piece we moved
        Constants.Side fromSide =
                this.boardRepresentation.getSideAtPosition(move.fromX(), move.fromY());
        Constants.PieceType fromPieceType =
                this.boardRepresentation.getPieceAtPosition(move.fromX(), move.fromY());
        this.boardRepresentation.fillSquare(move.toX(), move.toY(), fromPieceType, fromSide);

        // set the square we are moving away from, to empty
        this.boardRepresentation.fillSquare(
                move.fromX(), move.fromY(), Constants.PieceType.EMPTY, Constants.Side.EMPTY);

        // copy the old irreversibleData
        IrreversibleData.Builder irreversibleDataBuilder =
                new IrreversibleData.Builder().transferFromOldState(irreversibleDataStack.peek());
        CastlingRights.Builder castlingRightsBuilder =
                new CastlingRights.Builder()
                        .transferFromOldState(irreversibleDataStack.peek().castlingRights());

        halfMoveRelatedMakeMove(move, fromPieceType, irreversibleDataBuilder);

        enPassantRelatedMakeMove(move, fromSide, irreversibleDataBuilder);

        castlingRelatedMakeMove(move, fromSide, castlingRightsBuilder, fromPieceType);

        promotionRelatedMakeMove(move, fromSide);

        // if this was blacks turn -> increment full move counter
        if (!this.isWhitesTurn) fullMoveCounter++;

        // the other player can now make his turn
        this.isWhitesTurn = !this.isWhitesTurn;

        irreversibleDataBuilder.castlingRights(castlingRightsBuilder.build());
        this.irreversibleDataStack.push(irreversibleDataBuilder.build());
    }

    private void enPassantRelatedMakeMove(
            Move move, Constants.Side fromSide, IrreversibleData.Builder irreversibleDataBuilder) {
        // en passant capture handling
        if (move.moveType().equals(Move.MoveType.EP_CAPTURE)) {
            // off set the move.toY value depending on who took the piece
            // if black took, we need to raise the value and vice versa
            int yOffset = (fromSide.equals(Constants.Side.BLACK)) ? UP : DOWN;

            // remove the captured pawn from the board
            this.boardRepresentation.fillSquare(
                    move.toX(),
                    move.toY() + yOffset,
                    Constants.PieceType.EMPTY,
                    Constants.Side.EMPTY);
        }

        // en passant move handling
        if (move.moveType().equals(Move.MoveType.DOUBLE_PAWN_PUSH)) {
            // set the en passant square
            irreversibleDataBuilder.enPassantX(OptionalInt.of(move.fromX()));
        }
    }

    private void castlingRelatedMakeMove(
            Move move,
            Constants.Side fromSide,
            CastlingRights.Builder castlingRightsBuilder,
            Constants.PieceType fromPieceType) {
        // castle move handling
        if (move.moveType().equals(Move.MoveType.SHORT_CASTLE)
                || move.moveType().equals(Move.MoveType.LONG_CASTLE)) {
            // the rook goes to the left of the king on a king side castle and vice versa
            int rookXOffset = (move.moveType().equals(Move.MoveType.SHORT_CASTLE)) ? LEFT : RIGHT;
            this.boardRepresentation.fillSquare(
                    move.toX() + rookXOffset, move.toY(), Constants.PieceType.ROOK, fromSide);

            // clear the rooks starting square
            int rookXStart =
                    (move.moveType().equals(Move.MoveType.SHORT_CASTLE))
                            ? RIGHT_X_ROOK_START
                            : LEFT_X_ROOK_START;
            this.boardRepresentation.fillSquare(
                    rookXStart, move.toY(), Constants.PieceType.EMPTY, Constants.Side.EMPTY);

            // disable the castling rights
            castlingRightsBuilder.disableSpecifiedCastle(isWhitesTurn, true);
            castlingRightsBuilder.disableSpecifiedCastle(isWhitesTurn, false);
        }

        int startingY = isWhitesTurn ? 0 : 7;

        // castle right loss on king move
        if (fromPieceType.equals(Constants.PieceType.KING)
                && move.fromX() == KING_STARTING_X
                && move.fromY() == startingY) {
            // disable all castling rights if the king was moved
            castlingRightsBuilder.disableSpecifiedCastle(isWhitesTurn, true);
            castlingRightsBuilder.disableSpecifiedCastle(isWhitesTurn, false);
        }

        // castle right loss on rook move
        if (fromPieceType.equals(Constants.PieceType.ROOK)
                && move.fromX() == RIGHT_X_ROOK_START
                && move.fromY() == startingY) {
            // disable the short castling rights if the right rook was moved from its starting
            // square
            castlingRightsBuilder.disableSpecifiedCastle(isWhitesTurn, true);
        }

        // castle right loss on rook move
        if (fromPieceType.equals(Constants.PieceType.ROOK)
                && move.fromX() == LEFT_X_ROOK_START
                && move.fromY() == startingY) {
            // disable the long castling rights if the left rook was moved from its starting square
            castlingRightsBuilder.disableSpecifiedCastle(isWhitesTurn, false);
        }
    }

    private void promotionRelatedMakeMove(Move move, Constants.Side fromSide) {
        boolean wasSomethingPromoted =
                move.moveType().equals(Move.MoveType.ROOK_PROMOTION)
                        || move.moveType().equals(Move.MoveType.KNIGHT_PROMOTION)
                        || move.moveType().equals(Move.MoveType.BISHOP_PROMOTION)
                        || move.moveType().equals(Move.MoveType.QUEEN_PROMOTION)
                        || move.moveType().equals(Move.MoveType.ROOK_PROMOTION_CAPTURE)
                        || move.moveType().equals(Move.MoveType.KNIGHT_PROMOTION_CAPTURE)
                        || move.moveType().equals(Move.MoveType.BISHOP_PROMOTION_CAPTURE)
                        || move.moveType().equals(Move.MoveType.QUEEN_PROMOTION_CAPTURE);

        if (wasSomethingPromoted) {
            Constants.PieceType promotedPieceType =
                    switch (move.moveType()) {
                        case ROOK_PROMOTION, ROOK_PROMOTION_CAPTURE -> Constants.PieceType.ROOK;
                        case KNIGHT_PROMOTION, KNIGHT_PROMOTION_CAPTURE -> Constants.PieceType
                                .KNIGHT;
                        case BISHOP_PROMOTION, BISHOP_PROMOTION_CAPTURE -> Constants.PieceType
                                .BISHOP;
                        case QUEEN_PROMOTION, QUEEN_PROMOTION_CAPTURE -> Constants.PieceType.QUEEN;
                        default -> throw new IllegalStateException(
                                "Unexpected value: " + move.moveType());
                    };

            this.boardRepresentation.fillSquare(
                    move.toX(), move.toY(), promotedPieceType, fromSide);
        }
    }

    // ------------------------------
    // Unmake Section
    // ------------------------------

    public void unmakeMove(Move move) {}
}
