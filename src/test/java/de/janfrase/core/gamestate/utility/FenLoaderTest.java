package de.janfrase.core.gamestate.utility;

import de.janfrase.core.gamestate.GameState;
import de.janfrase.core.gamestate.board.BoardRepresentation;
import de.janfrase.utility.Constants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FenLoaderTest {

    /**
     * Tests for the loadFenString method in the FenLoader class.
     * The loadFenString method parses a FEN string representing a chessboard state
     * and populates the given GameState object with the corresponding board state and side details.
     */
    @Test
    void testLoadStartingPosition() {
        GameState gameState = new GameState();

        FenLoader.loadStartingPosition(gameState);

        BoardRepresentation board = gameState.getBoardRepresentation();

        assertNotNull(board, "Board representation should not be null after loading FEN.");

        assertEquals(Constants.PieceType.ROOK, board.getSquare(0, 0).getValue0(), "Expected a rook at position (0, 0).");
        assertEquals(Constants.Side.BLACK, board.getSquare(0, 0).getValue1(), "Expected a white rook at position (0, 0).");

        assertEquals(Constants.PieceType.KING, board.getSquare(4, 0).getValue0(), "Expected a king at position (4, 0).");
        assertEquals(Constants.Side.BLACK, board.getSquare(4, 0).getValue1(), "Expected a white king at position (4, 0).");

        assertEquals(Constants.PieceType.PAWN, board.getSquare(3, 1).getValue0(), "Expected a white pawn at position (4, 0).");
        assertEquals(Constants.Side.BLACK, board.getSquare(3, 1).getValue1(), "Expected a white pawn at position (4, 0).");
    }

    @Test
    void testLoadCustomFenString() {
        GameState gameState = new GameState();
        String customFen = "N4k2/8/2p5/8/5q2/4Q3/1K6/8 w - - 0 1";

        FenLoader.loadFenString(customFen, gameState);

        BoardRepresentation board = gameState.getBoardRepresentation();

        assertNotNull(board, "Board representation should not be null after loading FEN.");
        assertEquals(Constants.Side.WHITE, board.getSquare(0, 0).getValue1(), "Expected a white knight at position (0, 0).");
        assertEquals(Constants.PieceType.KNIGHT, board.getSquare(0, 0).getValue0(), "Expected a white knight at position (0, 0).");


        assertEquals(Constants.Side.BLACK, board.getSquare(5, 0).getValue1(), "Expected a black king at position (5, 0).");
        assertEquals(Constants.PieceType.KING, board.getSquare(5, 0).getValue0(), "Expected a black king at position (5, 0).");


        assertEquals(Constants.Side.BLACK, board.getSquare(2, 2).getValue1(), "Expected a black pawn at position (2, 2).");
        assertEquals(Constants.PieceType.PAWN, board.getSquare(2, 2).getValue0(), "Expected a black pawn at position (2, 2).");
    }

    @Test
    void testLoadEmptyFenString() {
        GameState gameState = new GameState();
        String emptyFen = "8/8/8/8/8/8/8/8 w - - 0 1";

        FenLoader.loadFenString(emptyFen, gameState);

        BoardRepresentation board = gameState.getBoardRepresentation();

        assertNotNull(board, "Board representation should not be null after loading FEN.");
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                assertEquals(Constants.Side.EMPTY, board.getSquare(x, y).getValue1(), "Expected no piece at position (" + x + ", " + y + ").");
            }
        }
    }
}