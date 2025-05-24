package de.janfrase.blunder.engine.state.game;

import de.janfrase.blunder.engine.movegen.Move;
import de.janfrase.blunder.engine.state.game.irreversibles.CastlingRights;
import de.janfrase.blunder.utility.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {

    private final GameState gameState = GameState.getInstance();

    @AfterEach
    void setUp() {
        GameState.resetGameState();
    }

    @Test
    void testSimpleMakeMove() {
        gameState.boardRepresentation.fillSquare(2, 2, Constants.PieceType.PAWN, Constants.Side.WHITE);
        gameState.boardRepresentation.fillSquare(6, 6, Constants.PieceType.ROOK, Constants.Side.BLACK);

        assertEquals(Constants.PieceType.PAWN, gameState.boardRepresentation.getPieceAtPosition(2, 2), "Source square should be a pawn before the move");
        assertEquals(Constants.PieceType.EMPTY, gameState.boardRepresentation.getPieceAtPosition(3, 3), "Target square should be empty before the move");

        Move pawnMove = new Move(2, 2, 2, 3, Move.MoveType.QUIET_MOVE);
        gameState.makeMove(pawnMove);

        assertEquals(Constants.PieceType.EMPTY, gameState.boardRepresentation.getPieceAtPosition(2, 2), "Source square should be empty after the move");
        assertEquals(Constants.PieceType.PAWN, gameState.boardRepresentation.getPieceAtPosition(2, 3), "Target square should contain the moved piece");

        assertFalse(gameState.isWhitesTurn, "Turn should toggle after a move");

        assertEquals(0, gameState.irreversibleDataStack.peek().halfMoveClock(), "Half move clock should be 0 since we just moved a pawn");

        Move rookMove = new Move(6, 6, 6, 4, Move.MoveType.QUIET_MOVE);
        gameState.makeMove(rookMove);

        assertEquals(1, gameState.irreversibleDataStack.peek().halfMoveClock(), "Half move clock should be 1");

        assertEquals(2, gameState.fullMoveCounter, "Full move counter should increment after Black's move");
    }

    @Test
    void testSimpleMakeMoveBlack() {
        GameState.resetGameState();
        gameState.isWhitesTurn = false;
        gameState.boardRepresentation.fillSquare(2, 5, Constants.PieceType.PAWN, Constants.Side.BLACK);
        gameState.boardRepresentation.fillSquare(6, 1, Constants.PieceType.ROOK, Constants.Side.WHITE);

        assertEquals(Constants.PieceType.PAWN, gameState.boardRepresentation.getPieceAtPosition(2, 5), "Source square should be a pawn before the move");
        assertEquals(Constants.PieceType.EMPTY, gameState.boardRepresentation.getPieceAtPosition(2, 4), "Target square should be empty before the move");

        Move pawnMove = new Move(2, 5, 2, 4, Move.MoveType.QUIET_MOVE);
        gameState.makeMove(pawnMove);

        assertEquals(Constants.PieceType.EMPTY, gameState.boardRepresentation.getPieceAtPosition(2, 5), "Source square should be empty after the move");
        assertEquals(Constants.PieceType.PAWN, gameState.boardRepresentation.getPieceAtPosition(2, 4), "Target square should contain the moved piece");

        assertTrue(gameState.isWhitesTurn, "Turn should toggle after a move");

        assertEquals(0, gameState.irreversibleDataStack.peek().halfMoveClock(), "Half move clock should be 0 since we just moved a pawn");

        Move rookMove = new Move(6, 1, 6, 3, Move.MoveType.QUIET_MOVE);
        gameState.makeMove(rookMove);

        assertEquals(1, gameState.irreversibleDataStack.peek().halfMoveClock(), "Half move clock should be 1");
    }

    @Test
    void testEnPassant() {
        gameState.boardRepresentation.fillSquare(4, 4, Constants.PieceType.PAWN, Constants.Side.WHITE);
        gameState.boardRepresentation.fillSquare(5, 6, Constants.PieceType.PAWN, Constants.Side.BLACK);

        Move blackDoublePawnPush = new Move(5, 6, 5, 4, Move.MoveType.DOUBLE_PAWN_PUSH);
        gameState.makeMove(blackDoublePawnPush);

        assertTrue(gameState.irreversibleDataStack.peek().enPassantX().isPresent(), "En passant target square should be present after a double pawn push");
        assertEquals(5, gameState.irreversibleDataStack.peek().enPassantX().getAsInt(), "En passant target square should be the correct square after a double pawn push");

        Move enPassant = new Move(4, 4, 5, 5, Move.MoveType.EP_CAPTURE);
        gameState.makeMove(enPassant);


        assertFalse(gameState.irreversibleDataStack.peek().enPassantX().isPresent(), "En passant target square should not be present en passant capture");
        assertEquals(Constants.PieceType.EMPTY, gameState.boardRepresentation.getPieceAtPosition(5, 4), "The captured pawn square should be empty after en passant.");
        assertEquals(Constants.PieceType.EMPTY, gameState.boardRepresentation.getPieceAtPosition(4, 4), "The source square should be empty after en passant.");
        assertEquals(Constants.PieceType.PAWN, gameState.boardRepresentation.getPieceAtPosition(5, 5), "The capturing pawn should exist in the correct target square after en passant.");
    }

    @Test
    void testEnPassantBlack() {
        GameState.resetGameState();
        gameState.isWhitesTurn = false;
        gameState.boardRepresentation.fillSquare(4, 3, Constants.PieceType.PAWN, Constants.Side.BLACK);
        gameState.boardRepresentation.fillSquare(5, 1, Constants.PieceType.PAWN, Constants.Side.WHITE);

        Move whiteDoublePawnPush = new Move(5, 1, 5, 3, Move.MoveType.DOUBLE_PAWN_PUSH);
        gameState.makeMove(whiteDoublePawnPush);

        assertTrue(gameState.irreversibleDataStack.peek().enPassantX().isPresent(), "En passant target square should be present after a double pawn push");
        assertEquals(5, gameState.irreversibleDataStack.peek().enPassantX().getAsInt(), "En passant target square should be the correct square after a double pawn push");

        Move enPassant = new Move(4, 3, 5, 2, Move.MoveType.EP_CAPTURE);
        gameState.makeMove(enPassant);

        assertFalse(gameState.irreversibleDataStack.peek().enPassantX().isPresent(), "En passant target square should not be present en passant capture");
        assertEquals(Constants.PieceType.EMPTY, gameState.boardRepresentation.getPieceAtPosition(5, 3), "The captured pawn square should be empty after en passant.");
        assertEquals(Constants.PieceType.EMPTY, gameState.boardRepresentation.getPieceAtPosition(4, 3), "The source square should be empty after en passant.");
        assertEquals(Constants.PieceType.PAWN, gameState.boardRepresentation.getPieceAtPosition(5, 2), "The capturing pawn should exist in the correct target square after en passant.");
    }

    @Test
    void testCastle() {
        gameState.boardRepresentation.fillSquare(4, 0, Constants.PieceType.KING, Constants.Side.WHITE);
        gameState.boardRepresentation.fillSquare(7, 0, Constants.PieceType.ROOK, Constants.Side.WHITE);

        Move kingCastle = new Move(4, 0, 6, 0, Move.MoveType.SHORT_CASTLE);
        gameState.makeMove(kingCastle);

        assertEquals(Constants.PieceType.EMPTY, gameState.boardRepresentation.getPieceAtPosition(7, 0), "The rook's original position should be empty after castling.");
        assertEquals(Constants.PieceType.ROOK, gameState.boardRepresentation.getPieceAtPosition(5, 0), "The rook should be in the correct position after castling.");
        assertEquals(Constants.PieceType.EMPTY, gameState.boardRepresentation.getPieceAtPosition(4, 0), "The king's original position should be empty after castling.");
        assertEquals(Constants.PieceType.KING, gameState.boardRepresentation.getPieceAtPosition(6, 0), "The king should be in the correct position after castling.");

        CastlingRights castlingRights = gameState.irreversibleDataStack.peek().castlingRights();
        assertFalse(castlingRights.whiteShortCastle(), "White should not be able to castle kingside after castling");
        assertFalse(castlingRights.whiteLongCastle(), "White should not be able to castle queenside after castling");
    }

    @Test
    void testCastleBlack() {
        GameState.resetGameState();
        gameState.isWhitesTurn = false;
        gameState.boardRepresentation.fillSquare(4, 7, Constants.PieceType.KING, Constants.Side.BLACK);
        gameState.boardRepresentation.fillSquare(7, 7, Constants.PieceType.ROOK, Constants.Side.BLACK);

        Move kingCastle = new Move(4, 7, 6, 7, Move.MoveType.SHORT_CASTLE);
        gameState.makeMove(kingCastle);

        assertEquals(Constants.PieceType.EMPTY, gameState.boardRepresentation.getPieceAtPosition(7, 7), "The rook's original position should be empty after castling.");
        assertEquals(Constants.PieceType.ROOK, gameState.boardRepresentation.getPieceAtPosition(5, 7), "The rook should be in the correct position after castling.");
        assertEquals(Constants.PieceType.EMPTY, gameState.boardRepresentation.getPieceAtPosition(4, 7), "The king's original position should be empty after castling.");
        assertEquals(Constants.PieceType.KING, gameState.boardRepresentation.getPieceAtPosition(6, 7), "The king should be in the correct position after castling.");

        CastlingRights castlingRights = gameState.irreversibleDataStack.peek().castlingRights();
        assertFalse(castlingRights.blackShortCastle(), "Black should not be able to castle kingside after castling");
        assertFalse(castlingRights.blackLongCastle(), "Black should not be able to castle queenside after castling");
    }

    @Test
    void testPromotion() {
        gameState.boardRepresentation.fillSquare(6, 6, Constants.PieceType.PAWN, Constants.Side.WHITE);

        Move promotionMove = new Move(6, 6, 7, 6, Move.MoveType.QUEEN_PROMOTION);
        gameState.makeMove(promotionMove);

        assertEquals(Constants.PieceType.EMPTY, gameState.boardRepresentation.getPieceAtPosition(6, 6), "The source square should be empty after the pawn promotion.");
        assertEquals(Constants.PieceType.QUEEN, gameState.boardRepresentation.getPieceAtPosition(7, 6), "The promoted piece should be a queen in the correct position.");
    }

    @Test
    void testPromotionCapture() {
        gameState.boardRepresentation.fillSquare(6, 6, Constants.PieceType.PAWN, Constants.Side.WHITE);
        gameState.boardRepresentation.fillSquare(7, 7, Constants.PieceType.BISHOP, Constants.Side.BLACK);

        Move promotionCaptureMove = new Move(6, 6, 7, 7, Move.MoveType.QUEEN_PROMOTION_CAPTURE);
        gameState.makeMove(promotionCaptureMove);

        assertEquals(Constants.PieceType.EMPTY, gameState.boardRepresentation.getPieceAtPosition(6, 6), "The source square should be empty after the pawn promotion capture.");
        assertEquals(Constants.PieceType.QUEEN, gameState.boardRepresentation.getPieceAtPosition(7, 7), "The promoted piece should replace the captured piece, becoming a queen in the correct position.");
    }

    @Test
    void testKingMovementRemovesCastlingRights() {
        gameState.boardRepresentation.fillSquare(4, 0, Constants.PieceType.KING, Constants.Side.WHITE);
        gameState.boardRepresentation.fillSquare(7, 0, Constants.PieceType.ROOK, Constants.Side.WHITE);
        gameState.boardRepresentation.fillSquare(0, 0, Constants.PieceType.ROOK, Constants.Side.WHITE);

        Move kingMove = new Move(4, 0, 4, 1);
        gameState.makeMove(kingMove);

        CastlingRights castlingRights = gameState.irreversibleDataStack.peek().castlingRights();
        assertFalse(castlingRights.whiteShortCastle(), "White should not be able to castle kingside after king movement");
        assertFalse(castlingRights.whiteLongCastle(), "White should not be able to castle queenside after king movement");
    }

    @Test
    void testKingMovementRemovesCastlingRightsBlack() {
        GameState.resetGameState();
        gameState.isWhitesTurn = false;
        gameState.boardRepresentation.fillSquare(4, 7, Constants.PieceType.KING, Constants.Side.BLACK);
        gameState.boardRepresentation.fillSquare(7, 7, Constants.PieceType.ROOK, Constants.Side.BLACK);
        gameState.boardRepresentation.fillSquare(0, 7, Constants.PieceType.ROOK, Constants.Side.BLACK);

        Move kingMove = new Move(4, 7, 4, 6);
        gameState.makeMove(kingMove);

        CastlingRights castlingRights = gameState.irreversibleDataStack.peek().castlingRights();
        assertFalse(castlingRights.blackShortCastle(), "Black should not be able to castle kingside after king movement");
        assertFalse(castlingRights.blackLongCastle(), "Black should not be able to castle queenside after king movement");
    }

    @Test
    void testRookMovementRemovesSpecificCastlingRight() {
        gameState.boardRepresentation.fillSquare(4, 0, Constants.PieceType.KING, Constants.Side.WHITE);
        gameState.boardRepresentation.fillSquare(7, 0, Constants.PieceType.ROOK, Constants.Side.WHITE);
        gameState.boardRepresentation.fillSquare(0, 0, Constants.PieceType.ROOK, Constants.Side.WHITE);

        Move kingSideRookMove = new Move(7, 0, 7, 1);
        gameState.makeMove(kingSideRookMove);

        CastlingRights castlingRights = gameState.irreversibleDataStack.peek().castlingRights();
        assertFalse(castlingRights.whiteShortCastle(), "White should not be able to castle kingside after kingside rook movement");
        assertTrue(castlingRights.whiteLongCastle(), "White should still be able to castle queenside after kingside rook movement");
    }

    @Test
    void testRookMovementRemovesSpecificCastlingRightBlack() {
        GameState.resetGameState();
        gameState.isWhitesTurn = false;
        gameState.boardRepresentation.fillSquare(4, 7, Constants.PieceType.KING, Constants.Side.BLACK);
        gameState.boardRepresentation.fillSquare(7, 7, Constants.PieceType.ROOK, Constants.Side.BLACK);
        gameState.boardRepresentation.fillSquare(0, 7, Constants.PieceType.ROOK, Constants.Side.BLACK);

        Move kingSideRookMove = new Move(7, 7, 7, 6);
        gameState.makeMove(kingSideRookMove);

        CastlingRights castlingRights = gameState.irreversibleDataStack.peek().castlingRights();
        assertFalse(castlingRights.blackShortCastle(), "Black should not be able to castle kingside after kingside rook movement");
        assertTrue(castlingRights.blackLongCastle(), "Black should still be able to castle queenside after kingside rook movement");
    }


}
