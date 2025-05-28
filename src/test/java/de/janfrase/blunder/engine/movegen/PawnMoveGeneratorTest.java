/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.movegen;

import static org.junit.jupiter.api.Assertions.*;

import de.janfrase.blunder.engine.state.game.GameState;
import de.janfrase.blunder.engine.state.game.GameStateFenParser;
import de.janfrase.blunder.utility.Constants;
import java.util.ArrayList;
import java.util.OptionalInt;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class PawnMoveGeneratorTest {

    private final GameState gameState = GameState.getInstance();
    private ArrayList<Move> moves = new ArrayList<>();

    @AfterEach
    void setUp() {
        GameState.resetGameState();
        moves = new ArrayList<>();
    }

    @Test
    void testBasicPawnMoves() {
        GameStateFenParser.loadFenString("8/8/8/8/8/8/4P3/8 w - - 0 1");
        PawnMoveGenerator.generatePawnMove(
                moves,
                4,
                6,
                gameState.getBoardRepresentation(),
                Constants.Side.WHITE,
                OptionalInt.empty());
        assertEquals(2, moves.size());
        assertTrue(moves.stream().anyMatch(m -> m.toY() == 5 && m.toX() == 4));
        assertTrue(moves.stream().anyMatch(m -> m.toY() == 4 && m.toX() == 4));
    }

    @Test
    void testPawnCaptures() {
        GameStateFenParser.loadFenString("8/8/8/3p1n2/4P3/8/8/8 w - - 0 1");
        PawnMoveGenerator.generatePawnMove(
                moves,
                4,
                4,
                gameState.getBoardRepresentation(),
                Constants.Side.WHITE,
                OptionalInt.empty());
        assertEquals(3, moves.size());
        assertTrue(moves.stream().anyMatch(m -> m.toX() == 3 && m.toY() == 3));
        assertTrue(moves.stream().anyMatch(m -> m.toX() == 5 && m.toY() == 3));
        assertEquals(
                2,
                moves.stream()
                        .filter(move -> move.capturedPieceType() != Constants.PieceType.EMPTY)
                        .count());
    }

    @Test
    void testEnPassantCapture() {
        GameStateFenParser.loadFenString("1k6/8/8/4Pp2/8/8/8/1K6 w - f6 0 1");
        PawnMoveGenerator.generatePawnMove(
                moves,
                4,
                3,
                gameState.getBoardRepresentation(),
                Constants.Side.WHITE,
                OptionalInt.of(5));

        assertEquals(2, moves.size());
        assertTrue(moves.stream().anyMatch(m -> m.moveType() == Move.MoveType.EP_CAPTURE));
    }

    @Test
    void testPawnPromotion() {
        GameStateFenParser.loadFenString("1k6/4P3/8/8/8/8/8/1K6 w - - 0 1");
        PawnMoveGenerator.generatePawnMove(
                moves,
                4,
                1,
                gameState.getBoardRepresentation(),
                Constants.Side.WHITE,
                OptionalInt.empty());
        assertEquals(4, moves.size());
        assertTrue(moves.stream().anyMatch(m -> m.moveType() == Move.MoveType.QUEEN_PROMOTION));
        assertTrue(moves.stream().anyMatch(m -> m.moveType() == Move.MoveType.ROOK_PROMOTION));
        assertTrue(moves.stream().anyMatch(m -> m.moveType() == Move.MoveType.BISHOP_PROMOTION));
        assertTrue(moves.stream().anyMatch(m -> m.moveType() == Move.MoveType.KNIGHT_PROMOTION));
    }

    @Test
    void testCapturePromotion() {
        GameStateFenParser.loadFenString("1k1n4/4P3/8/8/8/8/8/1K6 w - - 0 1");
        PawnMoveGenerator.generatePawnMove(
                moves,
                4,
                1,
                gameState.getBoardRepresentation(),
                Constants.Side.WHITE,
                OptionalInt.empty());

        assertEquals(8, moves.size());
        assertTrue(
                moves.stream()
                        .anyMatch(
                                m ->
                                        m.moveType() == Move.MoveType.QUEEN_PROMOTION
                                                && m.toX() == 3
                                                && m.capturedPieceType()
                                                        == Constants.PieceType.KNIGHT));

        assertEquals(
                4,
                moves.stream()
                        .filter(m -> m.capturedPieceType() == Constants.PieceType.KNIGHT)
                        .count());
    }

    @Test
    void testPawnOnLeftEdge() {
        GameStateFenParser.loadFenString("8/8/8/8/P7/8/8/8 w - - 0 1");
        PawnMoveGenerator.generatePawnMove(
                moves,
                0,
                4,
                gameState.getBoardRepresentation(),
                Constants.Side.WHITE,
                OptionalInt.empty());
        assertEquals(1, moves.size());
        assertTrue(moves.stream().anyMatch(m -> m.toY() == 3 && m.toX() == 0));
    }
}
