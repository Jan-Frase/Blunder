/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.backend.movegen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.janfrase.blunder.engine.backend.movegen.move.Move;
import de.janfrase.blunder.engine.backend.state.board.BoardRepresentation;
import de.janfrase.blunder.engine.backend.state.game.FenParser;
import de.janfrase.blunder.engine.backend.state.game.GameState;
import de.janfrase.blunder.utility.Constants;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class KnightMoveGeneratorTest {

    private final GameState gameState = GameState.getInstance();
    private ArrayList<Move> moves = new ArrayList<>();

    @AfterEach
    void setUp() {
        GameState.resetGameState();
        moves = new ArrayList<>();
    }

    @Test
    void testKnightMovementAllDirections() {
        BoardRepresentation board = gameState.getBoardRepresentation();
        board.setPieceAt(3, 3, Constants.PieceType.KNIGHT, Constants.Side.WHITE);

        KnightMoveGenerator.generateKnightMoves(moves, 3, 3, board, Constants.Side.WHITE);

        assertEquals(8, moves.size());
        assertTrue(moves.contains(new Move(3, 3, 1, 2)));
        assertTrue(moves.contains(new Move(3, 3, 1, 4)));
        assertTrue(moves.contains(new Move(3, 3, 2, 1)));
        assertTrue(moves.contains(new Move(3, 3, 2, 5)));
        assertTrue(moves.contains(new Move(3, 3, 4, 1)));
        assertTrue(moves.contains(new Move(3, 3, 4, 5)));
        assertTrue(moves.contains(new Move(3, 3, 5, 2)));
        assertTrue(moves.contains(new Move(3, 3, 5, 4)));
    }

    @Test
    void testKnightBlockedByFriendlyPieces() {
        BoardRepresentation board = gameState.getBoardRepresentation();
        board.setPieceAt(3, 3, Constants.PieceType.KNIGHT, Constants.Side.WHITE);
        board.setPieceAt(1, 2, Constants.PieceType.PAWN, Constants.Side.WHITE);
        board.setPieceAt(5, 4, Constants.PieceType.PAWN, Constants.Side.WHITE);

        KnightMoveGenerator.generateKnightMoves(moves, 3, 3, board, Constants.Side.WHITE);

        assertEquals(6, moves.size());
        assertTrue(moves.contains(new Move(3, 3, 1, 4)));
        assertTrue(moves.contains(new Move(3, 3, 2, 1)));
        assertTrue(moves.contains(new Move(3, 3, 2, 5)));
        assertTrue(moves.contains(new Move(3, 3, 4, 1)));
        assertTrue(moves.contains(new Move(3, 3, 4, 5)));
        assertTrue(moves.contains(new Move(3, 3, 5, 2)));
    }

    @Test
    void testKnightCapturingEnemyPieces() {
        FenParser.loadFenString("8/8/1p6/3N4/5r2/8/8/8 w - - 0 1");
        BoardRepresentation board = gameState.getBoardRepresentation();

        KnightMoveGenerator.generateKnightMoves(moves, 3, 3, board, Constants.Side.WHITE);

        assertTrue(moves.contains(new Move(3, 3, 1, 2, Constants.PieceType.PAWN)));
        assertTrue(moves.contains(new Move(3, 3, 5, 4, Constants.PieceType.ROOK)));

        Move capturePawnMove =
                moves.stream()
                        .filter(move -> move.capturedPieceType() == Constants.PieceType.PAWN)
                        .findFirst()
                        .orElseThrow();
        gameState.makeMove(capturePawnMove);
        assertEquals(Constants.PieceType.KNIGHT, board.getPieceAt(1, 2));
        assertEquals(Constants.Side.WHITE, board.getSideAt(1, 2));
        assertEquals(Constants.PieceType.EMPTY, board.getPieceAt(3, 3));
    }
}
