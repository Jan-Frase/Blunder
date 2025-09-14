/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.backend.state.game;

import de.janfrase.blunder.engine.backend.Piece;
import de.janfrase.blunder.engine.backend.movegen.Move;
import de.janfrase.blunder.engine.backend.state.board.BitBoards;
import de.janfrase.blunder.engine.backend.state.game.irreversibles.CastlingRights;
import de.janfrase.blunder.engine.backend.state.game.irreversibles.IrreversibleData;
import java.util.OptionalInt;
import java.util.Stack;

/**
 * The {@code GameState} class is responsible for managing the current state of a chess game.
 * It maintains game attributes such as board representation, move history, player turns, and move counters.
 * This class follows the Singleton design pattern, ensuring there is only one active instance during a game session.
 */
public class GameState {
    public static final int UP = -1;
    public static final int DOWN = 1;
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
    private static final GameState instance = new GameState();

    public BitBoards getBitBoards() {
        return bitBoards;
    }

    public IrreversibleData getIrreversibleData() {
        return irreversibleDataStack.peek();
    }

    public boolean isWhitesTurn() {
        return isWhitesTurn;
    }

    public boolean isHalfMoveClockAt50() {
        return this.irreversibleDataStack.peek().halfMoveClock() == 50;
    }

    public byte getFriendlySide() {
        return isWhitesTurn ? Piece.WHITE : Piece.BLACK;
    }

    public byte getEnemySide() {
        return isWhitesTurn ? Piece.BLACK : Piece.WHITE;
    }

    public boolean isRepeatedPosition() {
        return this.repeatTable.isRepeat();
    }

    // State variables
    BitBoards bitBoards;
    Stack<IrreversibleData> irreversibleDataStack;
    boolean isWhitesTurn;
    int fullMoveCounter;

    ZobristHasher zobristHasher;
    RepeatTable repeatTable;

    private GameState() {
        init();
    }

    /**
     * Returns the singleton instance of the {@code GameState} class.
     * The {@code GameState} class represents the current state of a chess game
     * and ensures that only one instance of this class exists at any given time.
     *
     * @return the singleton instance of {@code GameState}
     */
    public static GameState getInstance() {
        // TODO: At some point i should upgrade to multithreading to explore multiple lines at the
        // same time. I then need to get rid of the singleton pattern.
        return instance;
    }

    public long getZobristHash() {
        return this.zobristHasher.getZobristHash();
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
        GameState.getInstance().init();
    }

    private void init() {
        this.bitBoards = new BitBoards();
        this.irreversibleDataStack = new Stack<>();
        this.irreversibleDataStack.push(new IrreversibleData.Builder().buildDefault());

        this.isWhitesTurn = true;
        this.fullMoveCounter = 1;

        // init the zobristHash
        // also gets called when the FenParser did its thing
        this.zobristHasher = new ZobristHasher();
        this.zobristHasher.initZobristHash(this);

        // init the repeat table
        this.repeatTable = new RepeatTable();
        this.repeatTable.addHash(this.zobristHasher.getZobristHash());
    }

    @Override
    public String toString() {
        return StatePrinter.stateToString();
    }

    // ------------------------------
    // Make Section
    // ------------------------------

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
        // TODO: Rename this to friendlyPieceType or movedPieceType
        Piece fromPieceType = this.bitBoards.getPieceAt(move.fromX(), move.fromY());
        this.bitBoards.setPieceAt(move.toX(), move.toY(), fromPieceType);

        // set the square we are moving away from, to empty
        this.bitBoards.clearSquare(move.fromX(), move.fromY());

        // copy the old irreversibleData
        IrreversibleData.Builder irreversibleDataBuilder =
                new IrreversibleData.Builder().transferFromOldState(irreversibleDataStack.peek());
        CastlingRights.Builder castlingRightsBuilder =
                new CastlingRights.Builder()
                        .transferFromOldState(irreversibleDataStack.peek().castlingRights());

        this.halfMoveRelatedMakeMove(move, fromPieceType, irreversibleDataBuilder);

        this.enPassantRelatedMakeMove(move, fromPieceType, irreversibleDataBuilder);

        this.castlingRelatedMakeMove(move, castlingRightsBuilder, fromPieceType);

        this.promotionRelatedMakeMove(move, fromPieceType);

        // if this was blacks turn -> increment full move counter
        if (!this.isWhitesTurn) fullMoveCounter++;

        // the other player can now take his turn
        this.isWhitesTurn = !this.isWhitesTurn;

        // store the old castling rights, we need them to update the zobrist hash in a moment
        IrreversibleData oldIrreversibleData = irreversibleDataStack.peek();

        // add the new castling rights to the irreversible data builder
        irreversibleDataBuilder.castlingRights(castlingRightsBuilder.build());
        IrreversibleData newIrreversibleData = irreversibleDataBuilder.build();
        // and push it onto the stack
        this.irreversibleDataStack.push(newIrreversibleData);

        // and update the zobrist hash
        this.zobristHasher.updateZobristHashAfterMove(
                move, fromPieceType, oldIrreversibleData, newIrreversibleData);

        // update the repeat table
        this.repeatTable.addHash(this.zobristHasher.getZobristHash());
    }

    private void halfMoveRelatedMakeMove(
            Move move, Piece fromPiece, IrreversibleData.Builder irreversibleDataBuilder) {
        // now we can get to the edge cases :)
        boolean wasSomethingCaptured =
                move.capturedPieceType().value != Piece.EMPTY
                        || move.moveType().equals(Move.MoveType.EP_CAPTURE);

        // half-move clock handling
        if (wasSomethingCaptured || fromPiece.getType() == Piece.PAWN) {
            // reset the half-move clock on capture or if a pawn was moved
            irreversibleDataBuilder.halfMoveClock(0);
        }
    }

    private void enPassantRelatedMakeMove(
            Move move, Piece fromPiece, IrreversibleData.Builder irreversibleDataBuilder) {
        // en passant capture handling
        if (move.moveType().equals(Move.MoveType.EP_CAPTURE)) {
            int yOffset = getYOffsetOnEnPassantCapture(fromPiece);

            // remove the captured pawn from the board
            this.bitBoards.clearSquare(move.toX(), move.toY() + yOffset);
        }

        // en passant move handling
        if (move.moveType().equals(Move.MoveType.DOUBLE_PAWN_PUSH)) {
            // set the en passant square
            irreversibleDataBuilder.enPassantX(OptionalInt.of(move.fromX()));
        }
    }

    protected static int getYOffsetOnEnPassantCapture(Piece fromSide) {
        // off set the move.toY value depending on who took the piece
        // if black took, we need to raise the value and vice versa
        return (fromSide.value == Piece.BLACK) ? UP : DOWN;
    }

    // TODO refactor this method?
    private void castlingRelatedMakeMove(
            Move move, CastlingRights.Builder castlingRightsBuilder, Piece fromPieceType) {
        // castle move handling
        if (move.moveType().equals(Move.MoveType.SHORT_CASTLE)
                || move.moveType().equals(Move.MoveType.LONG_CASTLE)) {
            // the rook goes to the left of the king on a king side castle and vice versa
            int rookXOffset = (move.moveType().equals(Move.MoveType.SHORT_CASTLE)) ? LEFT : RIGHT;
            this.bitBoards.setPieceAt(
                    move.toX() + rookXOffset,
                    move.toY(),
                    new Piece(Piece.ROOK, fromPieceType.getSide()));

            // clear the rooks starting square
            int rookXStart =
                    (move.moveType().equals(Move.MoveType.SHORT_CASTLE))
                            ? RIGHT_X_ROOK_START
                            : LEFT_X_ROOK_START;
            this.bitBoards.clearSquare(rookXStart, move.toY());

            // disable the castling rights
            castlingRightsBuilder.disableSpecifiedCastle(isWhitesTurn, true);
            castlingRightsBuilder.disableSpecifiedCastle(isWhitesTurn, false);
        }

        int startingY = isWhitesTurn ? 7 : 0;

        // castle right loss on king move
        if (fromPieceType.value == Piece.KING
                && move.fromX() == KING_STARTING_X
                && move.fromY() == startingY) {
            // disable all castling rights if the king was moved
            castlingRightsBuilder.disableSpecifiedCastle(isWhitesTurn, true);
            castlingRightsBuilder.disableSpecifiedCastle(isWhitesTurn, false);
        }

        // castle right loss on rook move
        if (fromPieceType.value == Piece.ROOK
                && move.fromX() == RIGHT_X_ROOK_START
                && move.fromY() == startingY) {
            // disable the short castling rights if the right rook was moved from its starting
            // square
            castlingRightsBuilder.disableSpecifiedCastle(isWhitesTurn, true);
        }

        // castle right loss on rook move
        if (fromPieceType.value == Piece.ROOK
                && move.fromX() == LEFT_X_ROOK_START
                && move.fromY() == startingY) {
            // disable the long castling rights if the left rook was moved from its starting square
            castlingRightsBuilder.disableSpecifiedCastle(isWhitesTurn, false);
        }

        // castle right loss on rook capture -> short castle side
        int enemyStartingY = isWhitesTurn ? 0 : 7;
        if (move.capturedPieceType().value == Piece.ROOK
                && move.toY() == enemyStartingY
                && move.toX() == RIGHT_X_ROOK_START) {
            castlingRightsBuilder.disableSpecifiedCastle(!isWhitesTurn, true);
        }

        // castle right loss on rook capture -> long castle side
        if (move.capturedPieceType().value == Piece.ROOK
                && move.toY() == enemyStartingY
                && move.toX() == LEFT_X_ROOK_START) {
            castlingRightsBuilder.disableSpecifiedCastle(!isWhitesTurn, false);
        }
    }

    private void promotionRelatedMakeMove(Move move, Piece fromSide) {
        boolean wasSomethingPromoted =
                move.moveType().equals(Move.MoveType.ROOK_PROMOTION)
                        || move.moveType().equals(Move.MoveType.KNIGHT_PROMOTION)
                        || move.moveType().equals(Move.MoveType.BISHOP_PROMOTION)
                        || move.moveType().equals(Move.MoveType.QUEEN_PROMOTION);

        if (!wasSomethingPromoted) {
            return;
        }

        Piece promotedPiece =
                switch (move.moveType()) {
                    case ROOK_PROMOTION -> new Piece(Piece.ROOK, fromSide.getSide());
                    case KNIGHT_PROMOTION -> new Piece(Piece.KNIGHT, fromSide.getSide());
                    case BISHOP_PROMOTION -> new Piece(Piece.BISHOP, fromSide.getSide());
                    case QUEEN_PROMOTION -> new Piece(Piece.QUEEN, fromSide.getSide());
                    default -> throw new IllegalStateException(
                            "Unexpected value: " + move.moveType());
                };
        this.bitBoards.setPieceAt(move.toX(), move.toY(), promotedPiece);
    }

    // ------------------------------
    // Unmake Section
    // ------------------------------

    /**
     * Reverses the last move made in the game, restoring the game state to what it was
     * before the move was executed.
     * This includes updating the board representation,
     * restoring captured pieces, handling special move cases (such as en passant, castling,
     * or promotions), and adjusting game metadata such as the turn counter.
     *
     * @param move The move to be undone, represented by a {@link Move} object that contains
     *             the starting position, target position, and details about the move such as
     *             captured piece type and special move type.
     */
    public void unmakeMove(Move move) {
        // set the square we moved from, to have the piece we moved
        Piece fromPieceType = this.bitBoards.getPieceAt(move.toX(), move.toY());
        this.bitBoards.setPieceAt(move.fromX(), move.fromY(), fromPieceType);

        // set the square we moved to, to contain what was previously there
        if (move.capturedPieceType().isEmpty()) this.bitBoards.clearSquare(move.toX(), move.toY());
        else this.bitBoards.setPieceAt(move.toX(), move.toY(), move.capturedPieceType());

        // store the old irreversible data
        IrreversibleData oldIrreversibleData = irreversibleDataStack.peek();

        // go back to the previous stack frame of irreversible data
        this.irreversibleDataStack.pop();

        // get the new irreversibel data
        IrreversibleData newIrreversibleData = irreversibleDataStack.peek();

        this.enPassantRelatedUnmakeMove(move, fromPieceType);

        this.castlingRelatedUnmakeMove(move, fromPieceType);

        this.promotionRelatedUnmakeMove(move, fromPieceType);

        // if this was blacks turn -> increment full move counter
        if (this.isWhitesTurn) fullMoveCounter--;

        // the other player can now make his turn
        this.isWhitesTurn = !this.isWhitesTurn;

        // and update the zobrist hash
        this.zobristHasher.updateZobristHashAfterMove(
                move, fromPieceType, oldIrreversibleData, newIrreversibleData);

        // update the repeat table
        this.repeatTable.removeLastHash();
    }

    private void enPassantRelatedUnmakeMove(Move move, Piece fromSide) {
        // en passant capture handling
        if (move.moveType().equals(Move.MoveType.EP_CAPTURE)) {
            // add the captured pawn back to the board
            this.bitBoards.setPieceAt(
                    move.toX(), move.fromY(), new Piece(Piece.PAWN, fromSide.getEnemySide()));
        }
    }

    private void castlingRelatedUnmakeMove(Move move, Piece fromSide) {

        // castle move handling
        if (move.moveType().equals(Move.MoveType.SHORT_CASTLE)
                || move.moveType().equals(Move.MoveType.LONG_CASTLE)) {

            int rookXOffset = (move.moveType().equals(Move.MoveType.SHORT_CASTLE)) ? LEFT : RIGHT;
            int rookXCurrent = move.toX() + rookXOffset;

            // empty the square the rook is currently standing on
            bitBoards.clearSquare(rookXCurrent, move.toY());

            int rookXStart =
                    (move.moveType().equals(Move.MoveType.SHORT_CASTLE))
                            ? RIGHT_X_ROOK_START
                            : LEFT_X_ROOK_START;

            // put the rook back on its starting square
            bitBoards.setPieceAt(rookXStart, move.toY(), new Piece(Piece.ROOK, fromSide.getSide()));
        }
    }

    private void promotionRelatedUnmakeMove(Move move, Piece fromSide) {
        boolean wasSomethingPromoted =
                move.moveType().equals(Move.MoveType.ROOK_PROMOTION)
                        || move.moveType().equals(Move.MoveType.KNIGHT_PROMOTION)
                        || move.moveType().equals(Move.MoveType.BISHOP_PROMOTION)
                        || move.moveType().equals(Move.MoveType.QUEEN_PROMOTION);

        if (!wasSomethingPromoted) {
            return;
        }

        // put the pawn back :)
        this.bitBoards.setPieceAt(
                move.fromX(), move.fromY(), new Piece(Piece.PAWN, fromSide.getSide()));
    }
}
