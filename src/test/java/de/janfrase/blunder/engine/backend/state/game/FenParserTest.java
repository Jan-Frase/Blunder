/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.backend.state.game;

import static org.junit.jupiter.api.Assertions.*;

import de.janfrase.blunder.engine.backend.state.board.BoardRepresentation;
import de.janfrase.blunder.utility.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class FenParserTest {

    final GameState gameState = GameState.getInstance();

    @AfterEach
    void beforeEach() {
        GameState.resetGameState();
    }

    /**
     * Tests for the loadFenString method in the FenLoader class.
     * The loadFenString method parses a FEN string representing a chessboard state
     * and populates the given GameState object with the corresponding board state and side details.
     */
    @Test
    void testLoadStartingPosition() {
        FenParser.loadStartingPosition();

        BoardRepresentation board = this.gameState.boardRepresentation;

        assertNotNull(board, "Board representation should not be null after loading FEN.");

        assertEquals(
                Constants.PieceType.ROOK,
                board.getPieceAt(0, 0),
                "Expected a rook at position (0, 0).");
        assertEquals(
                Constants.Side.BLACK,
                board.getSideAt(0, 0),
                "Expected a white rook at position (0, 0).");

        assertEquals(
                Constants.PieceType.KING,
                board.getPieceAt(4, 0),
                "Expected a king at position (4, 0).");
        assertEquals(
                Constants.Side.BLACK,
                board.getSideAt(4, 0),
                "Expected a white king at position (4, 0).");

        assertEquals(
                Constants.PieceType.PAWN,
                board.getPieceAt(3, 1),
                "Expected a white pawn at position (4, 0).");
        assertEquals(
                Constants.Side.BLACK,
                board.getSideAt(3, 1),
                "Expected a white pawn at position (4, 0).");

        assertTrue(this.gameState.isWhitesTurn, "White should move first in starting position");
        assertTrue(
                gameState.irreversibleDataStack.peek().castlingRights().whiteShortCastle(),
                "White should have kingside castling rights");
        assertTrue(
                gameState.irreversibleDataStack.peek().castlingRights().whiteLongCastle(),
                "White should have queenside castling rights");
        assertTrue(
                gameState.irreversibleDataStack.peek().castlingRights().blackShortCastle(),
                "Black should have kingside castling rights");
        assertTrue(
                gameState.irreversibleDataStack.peek().castlingRights().blackLongCastle(),
                "Black should have queenside castling rights");
        assertEquals(
                0,
                gameState.irreversibleDataStack.peek().halfMoveClock(),
                "Half move clock should be 0");
        assertEquals(1, gameState.fullMoveCounter, "Full move counter should be 1");
    }

    @Test
    void testLoadCustomFenString() {
        String customFen = "N4k2/8/8/1Pp5/5q2/4Q3/1K6/8 b - c6 36 45";

        FenParser.loadFenString(customFen);

        BoardRepresentation board = gameState.boardRepresentation;

        assertNotNull(board, "Board representation should not be null after loading FEN.");
        assertEquals(
                Constants.PieceType.KNIGHT,
                board.getPieceAt(0, 0),
                "Expected a white knight at position (0, 0).");
        assertEquals(
                Constants.Side.WHITE,
                board.getSideAt(0, 0),
                "Expected a white knight at position (0, 0).");

        assertEquals(
                Constants.PieceType.KING,
                board.getPieceAt(5, 0),
                "Expected a black king at position (5, 0).");
        assertEquals(
                Constants.Side.BLACK,
                board.getSideAt(5, 0),
                "Expected a black king at position (5, 0).");

        assertEquals(
                Constants.PieceType.PAWN,
                board.getPieceAt(2, 3),
                "Expected a black pawn at position (2, 3).");
        assertEquals(
                Constants.Side.BLACK,
                board.getSideAt(2, 3),
                "Expected a black pawn at position (2, 3).");

        assertFalse(gameState.isWhitesTurn, "Black should be to move");
        assertTrue(
                gameState.irreversibleDataStack.peek().enPassantX().isPresent(),
                "En passant square should exist");
        assertEquals(
                2,
                gameState.irreversibleDataStack.peek().enPassantX().getAsInt(),
                "En passant square should be on c-file");
        assertEquals(
                36,
                gameState.irreversibleDataStack.peek().halfMoveClock(),
                "Half move clock should be 36");
        assertEquals(45, gameState.fullMoveCounter, "Full move counter should be 45");
    }

    @Test
    void testLoadEmptyFenString() {
        String emptyFen = "8/8/8/8/8/8/8/8 w - - 0 1";

        FenParser.loadFenString(emptyFen);

        BoardRepresentation board = gameState.boardRepresentation;

        assertNotNull(board, "Board representation should not be null after loading FEN.");
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                assertEquals(
                        Constants.Side.EMPTY,
                        board.getSideAt(x, y),
                        "Expected no piece at position (" + x + ", " + y + ").");
            }
        }

        assertTrue(gameState.isWhitesTurn, "White should be to move");
        assertFalse(
                gameState.irreversibleDataStack.peek().castlingRights().whiteShortCastle(),
                "No castling rights should be available");
        assertFalse(
                gameState.irreversibleDataStack.peek().castlingRights().whiteLongCastle(),
                "No castling rights should be available");
        assertFalse(
                gameState.irreversibleDataStack.peek().castlingRights().blackShortCastle(),
                "No castling rights should be available");
        assertFalse(
                gameState.irreversibleDataStack.peek().castlingRights().blackLongCastle(),
                "No castling rights should be available");
        assertEquals(
                0,
                gameState.irreversibleDataStack.peek().halfMoveClock(),
                "Half move clock should be 0");
        assertEquals(1, gameState.fullMoveCounter, "Full move counter should be 1");
    }

    @Test
    void testCrashCase() {
        FenParser.loadFenString(
                "rnbqk2r/ppp1bppp/3p1n2/4p3/1P6/4PN2/PBPP1PPP/RN1QKB1R w KQkq - 3 5");
    }
}
