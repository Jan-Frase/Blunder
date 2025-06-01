/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.movegen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.janfrase.blunder.engine.movegen.move.Move;
import de.janfrase.blunder.engine.state.board.BoardRepresentation;
import de.janfrase.blunder.engine.state.game.GameState;
import de.janfrase.blunder.engine.state.game.GameStateFenParser;
import de.janfrase.blunder.utility.Constants;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class LineMoveGeneratorTest {

    private final GameState gameState = GameState.getInstance();
    private ArrayList<Move> moves = new ArrayList<>();

    @AfterEach
    void setUp() {
        GameState.resetGameState();
        moves = new ArrayList<>();
    }

    @Test
    void testDiagonalMovementAllDirections() {
        BoardRepresentation board = gameState.getBoardRepresentation();
        board.setPieceAt(3, 3, Constants.PieceType.BISHOP, Constants.Side.WHITE);

        LineMoveGenerator.generateDiagonalMoves(moves, 3, 3, board, Constants.Side.WHITE);

        assertEquals(13, moves.size());
        assertTrue(moves.contains(new Move(3, 3, 4, 4)));
        assertTrue(moves.contains(new Move(3, 3, 5, 5)));
        assertTrue(moves.contains(new Move(3, 3, 6, 6)));
        assertTrue(moves.contains(new Move(3, 3, 7, 7)));
        assertTrue(moves.contains(new Move(3, 3, 2, 2)));
        assertTrue(moves.contains(new Move(3, 3, 1, 1)));
        assertTrue(moves.contains(new Move(3, 3, 0, 0)));
        assertTrue(moves.contains(new Move(3, 3, 4, 2)));
        assertTrue(moves.contains(new Move(3, 3, 5, 1)));
        assertTrue(moves.contains(new Move(3, 3, 6, 0)));
        assertTrue(moves.contains(new Move(3, 3, 2, 4)));
        assertTrue(moves.contains(new Move(3, 3, 1, 5)));
        assertTrue(moves.contains(new Move(3, 3, 0, 6)));
    }

    @Test
    void testDiagonalBlockedByFriendlyPieces() {
        BoardRepresentation board = gameState.getBoardRepresentation();
        board.setPieceAt(3, 3, Constants.PieceType.BISHOP, Constants.Side.WHITE);
        board.setPieceAt(4, 4, Constants.PieceType.PAWN, Constants.Side.WHITE);
        board.setPieceAt(2, 2, Constants.PieceType.PAWN, Constants.Side.WHITE);

        LineMoveGenerator.generateDiagonalMoves(moves, 3, 3, board, Constants.Side.WHITE);

        assertEquals(6, moves.size());
        assertTrue(moves.contains(new Move(3, 3, 4, 2)));
        assertTrue(moves.contains(new Move(3, 3, 5, 1)));
        assertTrue(moves.contains(new Move(3, 3, 6, 0)));
        assertTrue(moves.contains(new Move(3, 3, 2, 4)));
        assertTrue(moves.contains(new Move(3, 3, 1, 5)));
        assertTrue(moves.contains(new Move(3, 3, 0, 6)));
    }

    @Test
    void testDiagonalCapturingEnemyPieces() {
        GameStateFenParser.loadFenString("8/1n6/8/3B4/8/5p2/8/8 w - - 0 1");

        BoardRepresentation board = gameState.getBoardRepresentation();

        LineMoveGenerator.generateDiagonalMoves(moves, 3, 3, board, Constants.Side.WHITE);

        assertTrue(moves.contains(new Move(3, 3, 5, 5, Constants.PieceType.PAWN)));
        assertTrue(moves.contains(new Move(3, 3, 1, 1, Constants.PieceType.KNIGHT)));

        // Execute capture move and verify board state
        Move captureMove =
                moves.stream()
                        .filter(move -> move.capturedPieceType() == Constants.PieceType.PAWN)
                        .findFirst()
                        .orElseThrow();
        gameState.makeMove(captureMove);
        assertEquals(Constants.PieceType.BISHOP, board.getPieceAt(5, 5));
        assertEquals(Constants.Side.WHITE, board.getSideAt(5, 5));
        assertEquals(Constants.PieceType.EMPTY, board.getPieceAt(3, 3));
    }

    @Test
    void testStraightMovementAllDirections() {
        BoardRepresentation board = gameState.getBoardRepresentation();
        board.setPieceAt(3, 3, Constants.PieceType.ROOK, Constants.Side.WHITE);

        LineMoveGenerator.generateStraightMoves(moves, 3, 3, board, Constants.Side.WHITE);

        assertEquals(14, moves.size());
        assertTrue(moves.contains(new Move(3, 3, 3, 4)));
        assertTrue(moves.contains(new Move(3, 3, 3, 5)));
        assertTrue(moves.contains(new Move(3, 3, 3, 6)));
        assertTrue(moves.contains(new Move(3, 3, 3, 7)));
        assertTrue(moves.contains(new Move(3, 3, 3, 2)));
        assertTrue(moves.contains(new Move(3, 3, 3, 1)));
        assertTrue(moves.contains(new Move(3, 3, 3, 0)));
        assertTrue(moves.contains(new Move(3, 3, 4, 3)));
        assertTrue(moves.contains(new Move(3, 3, 5, 3)));
        assertTrue(moves.contains(new Move(3, 3, 6, 3)));
        assertTrue(moves.contains(new Move(3, 3, 7, 3)));
        assertTrue(moves.contains(new Move(3, 3, 2, 3)));
        assertTrue(moves.contains(new Move(3, 3, 1, 3)));
        assertTrue(moves.contains(new Move(3, 3, 0, 3)));
    }

    @Test
    void testStraightBlockedByFriendlyPieces() {
        BoardRepresentation board = gameState.getBoardRepresentation();
        board.setPieceAt(3, 3, Constants.PieceType.ROOK, Constants.Side.WHITE);
        board.setPieceAt(3, 4, Constants.PieceType.PAWN, Constants.Side.WHITE);
        board.setPieceAt(3, 2, Constants.PieceType.PAWN, Constants.Side.WHITE);

        LineMoveGenerator.generateStraightMoves(moves, 3, 3, board, Constants.Side.WHITE);

        assertEquals(7, moves.size());
        assertTrue(moves.contains(new Move(3, 3, 4, 3)));
        assertTrue(moves.contains(new Move(3, 3, 5, 3)));
        assertTrue(moves.contains(new Move(3, 3, 6, 3)));
        assertTrue(moves.contains(new Move(3, 3, 7, 3)));
        assertTrue(moves.contains(new Move(3, 3, 2, 3)));
        assertTrue(moves.contains(new Move(3, 3, 1, 3)));
    }

    @Test
    void testStraightCapturingEnemyPieces() {
        GameStateFenParser.loadFenString("8/8/8/1n1R4/8/3p4/8/8 w - - 0 1");
        BoardRepresentation board = gameState.getBoardRepresentation();

        LineMoveGenerator.generateStraightMoves(moves, 3, 3, board, Constants.Side.WHITE);

        assertTrue(moves.contains(new Move(3, 3, 3, 5, Constants.PieceType.PAWN)));
        assertTrue(moves.contains(new Move(3, 3, 1, 3, Constants.PieceType.KNIGHT)));

        // Execute capture move and verify board state
        Move capturePawnMove =
                moves.stream()
                        .filter(move -> move.capturedPieceType() == Constants.PieceType.PAWN)
                        .findFirst()
                        .orElseThrow();
        gameState.makeMove(capturePawnMove);
        assertEquals(Constants.PieceType.ROOK, board.getPieceAt(3, 5));
        assertEquals(Constants.Side.WHITE, board.getSideAt(3, 5));
        assertEquals(Constants.PieceType.EMPTY, board.getPieceAt(3, 3));
    }

    @Test
    void testPosition3Part() {
        GameStateFenParser.loadFenString("8/2p5/3p4/KP5r/2R2p1k/8/4P1P1/8 w - - 0 1");
        BoardRepresentation board = gameState.getBoardRepresentation();

        LineMoveGenerator.generateStraightMoves(moves, 7, 3, board, Constants.Side.BLACK);

        assertEquals(9, moves.size());
        assertEquals(
                8,
                moves.stream()
                        .filter(move -> move.capturedPieceType() == Constants.PieceType.EMPTY)
                        .count());
        assertEquals(
                1,
                moves.stream()
                        .filter(move -> move.capturedPieceType() == Constants.PieceType.PAWN)
                        .count());
        assertEquals(
                0,
                moves.stream()
                        .filter(move -> move.capturedPieceType() == Constants.PieceType.KING)
                        .count());
    }
}
