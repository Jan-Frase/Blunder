/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.movegen;

import static org.junit.jupiter.api.Assertions.*;

import de.janfrase.blunder.engine.movegen.move.Move;
import de.janfrase.blunder.engine.movegen.move.UciMoveParser;
import de.janfrase.blunder.engine.state.game.FenParser;
import de.janfrase.blunder.engine.state.game.GameState;
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
        FenParser.loadFenString("8/8/8/8/8/8/4P3/8 w - - 0 1");
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
        FenParser.loadFenString("8/8/8/3p1n2/4P3/8/8/8 w - - 0 1");
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
        FenParser.loadFenString("1k6/8/8/4Pp2/8/8/8/1K6 w - f6 0 1");
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
    void testWrongEnPassantCapture() {
        FenParser.loadFenString("1k6/8/8/5p2/8/4P3/8/1K6 w - - 0 1");
        PawnMoveGenerator.generatePawnMove(
                moves,
                4,
                5,
                gameState.getBoardRepresentation(),
                Constants.Side.WHITE,
                OptionalInt.of(5));

        assertEquals(1, moves.size());
        assertFalse(moves.stream().anyMatch(m -> m.moveType() == Move.MoveType.EP_CAPTURE));
    }

    @Test
    void testPawnPromotion() {
        FenParser.loadFenString("1k6/4P3/8/8/8/8/8/1K6 w - - 0 1");
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
        FenParser.loadFenString("1k1n4/4P3/8/8/8/8/8/1K6 w - - 0 1");
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
        FenParser.loadFenString("8/8/8/8/P7/8/8/8 w - - 0 1");
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

    @Test
    void testEnPassantDiscoveredCheck() {
        FenParser.loadFenString("8/2p5/3p4/KP5r/1R2Pp1k/8/6P1/8 b e3 - 0 1");
        PawnMoveGenerator.generatePawnMove(
                moves,
                5,
                4,
                gameState.getBoardRepresentation(),
                Constants.Side.BLACK,
                OptionalInt.of(4));
    }

    /*
        * 13:05:29.478 [Test worker] TRACE de.janfrase.blunder.engine.movegen.MoveGenerator - Starting move generation
    13:05:29.478 [Test worker] TRACE de.janfrase.blunder.engine.movegen.MoveGenerator - Finished move generation
    13:05:29.479 [Test worker] TRACE de.janfrase.blunder.engine.state.game.GameState - Unmaking move h5h8.
    13:05:29.479 [Test worker] TRACE de.janfrase.blunder.engine.state.board.BoardRepresentation - Placing ROOK from BLACK on x=7 y=3
    13:05:29.479 [Test worker] TRACE de.janfrase.blunder.engine.state.board.BoardRepresentation - Placing EMPTY from EMPTY on x=7 y=0
    13:05:29.480 [Test worker] TRACE de.janfrase.blunder.engine.state.game.GameState - Finished unmaking move:
    ◻◼◻◼◻◼◻◼
    ◼◻♟◻◼◻◼◻
    ◻◼◻♟◻◼◻◼
    ♔♙◼◻◼◻◼♜
    ◻♖◻◼♙♟◻♚
    ◼◻◼◻◼◻◼◻
    ◻◼◻◼◻◼♙◼
    ◼◻◼◻◼◻◼◻

    13:05:29.480 [Test worker] TRACE de.janfrase.blunder.engine.state.game.GameState - Making move f4f3.
    13:05:29.480 [Test worker] TRACE de.janfrase.blunder.engine.state.board.BoardRepresentation - Placing PAWN from BLACK on x=5 y=5
    13:05:29.481 [Test worker] TRACE de.janfrase.blunder.engine.state.board.BoardRepresentation - Placing EMPTY from EMPTY on x=5 y=4
    13:05:29.481 [Test worker] TRACE de.janfrase.blunder.engine.state.game.GameState - Finished making move:
    ◻◼◻◼◻◼◻◼
    ◼◻♟◻◼◻◼◻
    ◻◼◻♟◻◼◻◼
    ♔♙◼◻◼◻◼♜
    ◻♖◻◼♙◼◻♚
    ◼◻◼◻◼♟◼◻
    ◻◼◻◼◻◼♙◼
    ◼◻◼◻◼◻◼◻

    13:05:29.481 [Test worker] TRACE de.janfrase.blunder.engine.movegen.MoveGenerator - Starting move generation
    13:05:29.482 [Test worker] TRACE de.janfrase.blunder.engine.movegen.MoveGenerator - Finished move generation
    13:05:29.482 [Test worker] TRACE de.janfrase.blunder.engine.state.game.GameState - Unmaking move f4f3.
    13:05:29.482 [Test worker] TRACE de.janfrase.blunder.engine.state.board.BoardRepresentation - Placing PAWN from BLACK on x=5 y=4
    13:05:29.482 [Test worker] TRACE de.janfrase.blunder.engine.state.board.BoardRepresentation - Placing EMPTY from EMPTY on x=5 y=5
    13:05:29.483 [Test worker] TRACE de.janfrase.blunder.engine.state.game.GameState - Finished unmaking move:
    ◻◼◻◼◻◼◻◼
    ◼◻♟◻◼◻◼◻
    ◻◼◻♟◻◼◻◼
    ♔♙◼◻◼◻◼♜
    ◻♖◻◼♙♟◻♚
    ◼◻◼◻◼◻◼◻
    ◻◼◻◼◻◼♙◼
    ◼◻◼◻◼◻◼◻

    13:05:29.483 [Test worker] TRACE de.janfrase.blunder.engine.state.game.GameState - Making move f4e3.
    13:05:29.483 [Test worker] TRACE de.janfrase.blunder.engine.state.board.BoardRepresentation - Placing PAWN from BLACK on x=4 y=5
    13:05:29.484 [Test worker] TRACE de.janfrase.blunder.engine.state.board.BoardRepresentation - Placing EMPTY from EMPTY on x=5 y=4
    13:05:29.484 [Test worker] TRACE de.janfrase.blunder.engine.state.board.BoardRepresentation - Placing EMPTY from EMPTY on x=4 y=6
    13:05:29.484 [Test worker] TRACE de.janfrase.blunder.engine.state.game.GameState - Finished making move:
    ◻◼◻◼◻◼◻◼
    ◼◻♟◻◼◻◼◻
    ◻◼◻♟◻◼◻◼
    ♔♙◼◻◼◻◼♜
    ◻♖◻◼♙◼◻♚
    ◼◻◼◻♟◻◼◻
    ◻◼◻◼◻◼♙◼
    ◼◻◼◻◼◻◼◻

    13:05:29.485 [Test worker] TRACE de.janfrase.blunder.engine.movegen.MoveGenerator - Starting move generation
    13:05:29.485 [Test worker] TRACE de.janfrase.blunder.engine.movegen.MoveGenerator - Finished move generation
    13:05:29.485 [Test worker] TRACE de.janfrase.blunder.engine.state.game.GameState - Unmaking move f4e3.
    13:05:29.485 [Test worker] TRACE de.janfrase.blunder.engine.state.board.BoardRepresentation - Placing PAWN from BLACK on x=5 y=4
    13:05:29.486 [Test worker] TRACE de.janfrase.blunder.engine.state.board.BoardRepresentation - Placing PAWN from WHITE on x=4 y=5
    13:05:29.486 [Test worker] TRACE de.janfrase.blunder.engine.state.board.BoardRepresentation - Placing PAWN from WHITE on x=4 y=6
    13:05:29.486 [Test worker] TRACE de.janfrase.blunder.engine.state.game.GameState - Finished unmaking move:
    ◻◼◻◼◻◼◻◼
    ◼◻♟◻◼◻◼◻
    ◻◼◻♟◻◼◻◼
    ♔♙◼◻◼◻◼♜
    ◻♖◻◼♙♟◻♚
    ◼◻◼◻♙◻◼◻
    ◻◼◻◼♙◼♙◼
    ◼◻◼◻◼◻◼◻
        * */
    @Test
    void testMakingUnmakingEnPassant() {
        FenParser.loadFenString("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1");

        Move doublePawnPushMove = UciMoveParser.parseUciMove("e2e4");
        gameState.makeMove(doublePawnPushMove);

        // assertTrue(gameState.getIrreversibleData().enPassantX().isPresent());
        // assertEquals(5, gameState.getIrreversibleData().enPassantX().getAsInt());

        // Move enPassantCaptureMove = UciMoveParser.parseUciMove("f4e3");

        PawnMoveGenerator.generatePawnMove(
                moves,
                5,
                4,
                gameState.getBoardRepresentation(),
                gameState.getFriendlySide(),
                gameState.getIrreversibleData().enPassantX());

        Move enPassantCaptureMove =
                moves.stream()
                        .filter(m -> m.moveType() == Move.MoveType.EP_CAPTURE)
                        .findFirst()
                        .orElseThrow();

        gameState.makeMove(enPassantCaptureMove);

        assertEquals(
                Constants.PieceType.EMPTY, gameState.getBoardRepresentation().getPieceAt(4, 4));
        assertEquals(Constants.PieceType.PAWN, gameState.getBoardRepresentation().getPieceAt(4, 5));

        gameState.unmakeMove(enPassantCaptureMove);

        assertEquals(Constants.PieceType.PAWN, gameState.getBoardRepresentation().getPieceAt(5, 4));
        assertEquals(Constants.PieceType.PAWN, gameState.getBoardRepresentation().getPieceAt(4, 4));
        assertEquals(
                Constants.PieceType.EMPTY, gameState.getBoardRepresentation().getPieceAt(4, 5));
    }

    @Test
    void testMakingUnmakingEnPassantCapture2() {
        FenParser.loadFenString("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1R1K b kq - 0 1");

        PawnMoveGenerator.generatePawnMove(
                moves,
                1,
                6,
                gameState.getBoardRepresentation(),
                gameState.getFriendlySide(),
                OptionalInt.empty());

        assertEquals(8, moves.size());
    }
}
