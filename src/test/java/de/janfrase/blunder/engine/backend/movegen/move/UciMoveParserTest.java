/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.backend.movegen.move;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import de.janfrase.blunder.engine.backend.state.game.GameState;
import de.janfrase.blunder.uci.UciMoveParser;
import de.janfrase.blunder.utility.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class UciMoveParserTest {

    private final GameState gameState = GameState.getInstance();

    @AfterEach
    void setUp() {
        GameState.resetGameState();
    }

    /**
     * Test class for the UciMoveParser class.
     * <p>
     * This class tests the parseUciMove method, which parses UCI (Universal Chess Interface) strings
     * representing chess moves and translates them into Move objects. The method determines the type of move
     * (normal, capture, castling, en passant, or promotion) based on the board state, provided by the GameState class.
     */
    @Test
    public void testParsePawnNormalMove() {
        gameState
                .getBoardRepresentation()
                .setPieceAt(4, 6, Constants.PieceType.PAWN, Constants.Side.WHITE);

        // Act
        String uciMove = "e2e3";
        Move move = UciMoveParser.parseUciMove(uciMove);

        // Assert
        assertEquals(4, move.fromX());
        assertEquals(6, move.fromY());
        assertEquals(4, move.toX());
        assertEquals(5, move.toY());
        assertEquals(Move.MoveType.NORMAL_MOVE, move.moveType());
    }

    @Test
    public void testParsePawnDoublePush() {
        // Arrange: Set up board with a pawn
        gameState
                .getBoardRepresentation()
                .setPieceAt(4, 6, Constants.PieceType.PAWN, Constants.Side.WHITE);

        // Act
        String uciMove = "e2e4";
        Move move = UciMoveParser.parseUciMove(uciMove);

        // Assert
        assertEquals(4, move.fromX());
        assertEquals(6, move.fromY());
        assertEquals(4, move.toX());
        assertEquals(4, move.toY());
        assertEquals(Move.MoveType.DOUBLE_PAWN_PUSH, move.moveType());
    }

    @Test
    public void testParseKingShortCastle() {
        // Arrange: Set up board with king
        gameState
                .getBoardRepresentation()
                .setPieceAt(4, 7, Constants.PieceType.KING, Constants.Side.WHITE);
        gameState
                .getBoardRepresentation()
                .setPieceAt(7, 7, Constants.PieceType.ROOK, Constants.Side.WHITE);

        // Act
        String uciMove = "e1g1";
        Move move = UciMoveParser.parseUciMove(uciMove);

        // Assert
        assertEquals(4, move.fromX());
        assertEquals(7, move.fromY());
        assertEquals(6, move.toX());
        assertEquals(7, move.toY());
        assertEquals(Move.MoveType.SHORT_CASTLE, move.moveType());
    }

    @Test
    public void testParseKingLongCastle() {
        // Arrange: Set up board with king
        gameState
                .getBoardRepresentation()
                .setPieceAt(4, 7, Constants.PieceType.KING, Constants.Side.WHITE);
        gameState
                .getBoardRepresentation()
                .setPieceAt(0, 7, Constants.PieceType.ROOK, Constants.Side.WHITE);

        // Act
        String uciMove = "e1c1";
        Move move = UciMoveParser.parseUciMove(uciMove);

        // Assert
        assertEquals(4, move.fromX());
        assertEquals(7, move.fromY());
        assertEquals(2, move.toX());
        assertEquals(7, move.toY());
        assertEquals(Move.MoveType.LONG_CASTLE, move.moveType());
    }

    @Test
    public void testParsePawnPromotionToQueen() {
        // Arrange: Set up board with pawn
        gameState
                .getBoardRepresentation()
                .setPieceAt(0, 1, Constants.PieceType.PAWN, Constants.Side.WHITE);

        // Act
        String uciMove = "a7a8q";
        Move move = UciMoveParser.parseUciMove(uciMove);

        // Assert
        assertEquals(0, move.fromX());
        assertEquals(1, move.fromY());
        assertEquals(0, move.toX());
        assertEquals(0, move.toY());
        assertEquals(Move.MoveType.QUEEN_PROMOTION, move.moveType());
        assertEquals(Constants.PieceType.EMPTY, move.capturedPieceType());
    }

    @Test
    public void testParseNormalCapture() {
        // Arrange: Set up board with pieces
        gameState
                .getBoardRepresentation()
                .setPieceAt(4, 4, Constants.PieceType.KNIGHT, Constants.Side.WHITE);
        gameState
                .getBoardRepresentation()
                .setPieceAt(5, 2, Constants.PieceType.BISHOP, Constants.Side.BLACK);

        // Act
        String uciMove = "e4f6";
        Move move = UciMoveParser.parseUciMove(uciMove);

        // Assert
        assertEquals(4, move.fromX());
        assertEquals(4, move.fromY());
        assertEquals(5, move.toX());
        assertEquals(2, move.toY());
        assertEquals(Move.MoveType.NORMAL_MOVE, move.moveType());
        assertEquals(Constants.PieceType.BISHOP, move.capturedPieceType());
    }

    @Test
    public void testParsePawnPromotionCapture() {
        // Arrange: Set up board with pieces
        gameState
                .getBoardRepresentation()
                .setPieceAt(0, 1, Constants.PieceType.PAWN, Constants.Side.WHITE);
        gameState
                .getBoardRepresentation()
                .setPieceAt(1, 0, Constants.PieceType.ROOK, Constants.Side.BLACK);

        // Act
        String uciMove = "a7b8q";
        Move move = UciMoveParser.parseUciMove(uciMove);

        // Assert
        assertEquals(0, move.fromX());
        assertEquals(1, move.fromY());
        assertEquals(1, move.toX());
        assertEquals(0, move.toY());
        assertEquals(Move.MoveType.QUEEN_PROMOTION, move.moveType());
        assertEquals(Constants.PieceType.ROOK, move.capturedPieceType());
    }

    @Test
    public void testParseEnPassantCapture() {
        // Arrange: Set up board with pawns
        gameState
                .getBoardRepresentation()
                .setPieceAt(4, 3, Constants.PieceType.PAWN, Constants.Side.WHITE);
        gameState
                .getBoardRepresentation()
                .setPieceAt(5, 3, Constants.PieceType.PAWN, Constants.Side.BLACK);

        // Act
        String uciMove = "e5f6";
        Move move = UciMoveParser.parseUciMove(uciMove);

        // Assert
        assertEquals(4, move.fromX());
        assertEquals(3, move.fromY());
        assertEquals(5, move.toX());
        assertEquals(2, move.toY());
        assertEquals(Move.MoveType.EP_CAPTURE, move.moveType());
        assertNotEquals(Constants.PieceType.PAWN, move.capturedPieceType());
    }
}
