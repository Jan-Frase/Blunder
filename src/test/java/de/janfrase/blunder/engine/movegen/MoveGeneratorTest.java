/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.movegen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.janfrase.blunder.engine.state.board.BoardRepresentation;
import de.janfrase.blunder.engine.state.game.GameState;
import de.janfrase.blunder.engine.state.game.GameStateFenParser;
import de.janfrase.blunder.engine.state.game.GameStatePrinter;
import de.janfrase.blunder.utility.Constants;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class MoveGeneratorTest {

    private final GameState gameState = GameState.getInstance();
    private ArrayList<Move> moves = new ArrayList<>();

    @AfterEach
    void setUp() {
        GameState.resetGameState();
        moves = new ArrayList<>();
    }

    @Test
    public void testGenerateMoves_EmptyBoard() {
        MoveGenerator.generateMoves();

        assertTrue(moves.isEmpty(), "Moves should be empty on an empty board.");
    }

    @Test
    public void testGenerateMoves_OnlyWhiteKing() {
        GameStateFenParser.loadFenString("8/8/8/8/4K3/8/8/8 w - - 0 1");
        BoardRepresentation board = gameState.getBoardRepresentation();

        KingMoveGenerator.generateKingMoves(
                moves,
                4,
                4,
                board,
                Constants.Side.WHITE,
                gameState.getIrreversibleData().castlingRights());

        assertEquals(8, moves.size(), "White king should have 8 possible moves when not blocked.");
        for (Move move : moves) {
            assertEquals(4, move.fromX(), "The White king's starting X position should be 4.");
            assertEquals(4, move.fromY(), "The White king's starting Y position should be 4.");
        }
    }

    @Test
    public void testGenerateMoves_WhiteKingPartiallyBlocked() {
        GameStateFenParser.loadFenString("8/8/8/8/3PK3/8/8/8 w - - 0 1");
        BoardRepresentation board = gameState.getBoardRepresentation();

        KingMoveGenerator.generateKingMoves(
                moves,
                4,
                4,
                board,
                Constants.Side.WHITE,
                gameState.getIrreversibleData().castlingRights());

        assertEquals(
                7, moves.size(), "White king should have 7 possible moves when partially blocked.");
        assertTrue(
                moves.stream().noneMatch(move -> move.toX() == 3 && move.toY() == 4),
                "King should not move to the square occupied by friendly pieces (3, 4).");
    }

    @Test
    public void testGenerateMoves_WhiteKingCapturingEnemyPawn() {
        GameStateFenParser.loadFenString("8/8/8/8/4K3/5p2/8/8 w - - 0 1");
        BoardRepresentation board = gameState.getBoardRepresentation();

        KingMoveGenerator.generateKingMoves(
                moves,
                4,
                4,
                board,
                Constants.Side.WHITE,
                gameState.getIrreversibleData().castlingRights());

        assertEquals(
                8, moves.size(), "White king should have 8 possible moves, including one capture.");
        assertTrue(
                moves.stream().anyMatch(move -> move.toX() == 5 && move.toY() == 5),
                "King should be able to capture the enemy piece at (5, 5).");
        assertTrue(
                moves.stream()
                        .anyMatch(move -> move.capturedPieceType() == Constants.PieceType.PAWN),
                "King should be able to capture the enemy piece at (5, 5).");
    }

    @Test
    public void testGenerateMoves_WhiteCastleRights() {
        GameStateFenParser.loadFenString("8/8/8/8/8/8/8/R3K2R w KQ - 0 1");
        BoardRepresentation board = gameState.getBoardRepresentation();

        KingMoveGenerator.generateKingMoves(
                moves,
                4,
                7,
                board,
                Constants.Side.WHITE,
                gameState.getIrreversibleData().castlingRights());

        assertEquals(
                7,
                moves.size(),
                "White king should have 10 possible moves, including two castles.");
        assertTrue(
                moves.stream().anyMatch(move -> move.moveType() == Move.MoveType.SHORT_CASTLE),
                "White king should be able to perform short castling.");
        assertTrue(
                moves.stream().anyMatch(move -> move.moveType() == Move.MoveType.LONG_CASTLE),
                "White king should be able to perform long castling.");
    }

    @Test
    public void testGenerateMoves_BlockingCastlePath() {
        GameStateFenParser.loadFenString("8/8/8/8/8/8/8/R3KQ1R w KQ - 0 1");
        BoardRepresentation board = gameState.getBoardRepresentation();

        KingMoveGenerator.generateKingMoves(
                moves,
                4,
                7,
                board,
                Constants.Side.WHITE,
                gameState.getIrreversibleData().castlingRights());

        assertEquals(
                5, moves.size(), "White king should have 5 possible moves, including one castle.");
        assertTrue(
                moves.stream().noneMatch(move -> move.moveType() == Move.MoveType.SHORT_CASTLE),
                "White king should not be able to perform short castling when path is blocked.");
    }

    @Test
    public void testGeneratesMoves_ExecuteLongCastle() {
        GameStateFenParser.loadFenString("8/8/8/8/8/8/3PPP2/R3KP1R w KQ - 0 1");
        BoardRepresentation board = gameState.getBoardRepresentation();

        KingMoveGenerator.generateKingMoves(
                moves,
                4,
                7,
                board,
                Constants.Side.WHITE,
                gameState.getIrreversibleData().castlingRights());

        assertEquals(
                2, moves.size(), "White king should have 2 possible moves, including one castle.");
        assertTrue(
                moves.stream().anyMatch(move -> move.moveType() == Move.MoveType.LONG_CASTLE),
                "White king should be able to perform long castling.");

        Move longCastleMove =
                moves.stream()
                        .filter(move -> move.moveType() == Move.MoveType.LONG_CASTLE)
                        .findFirst()
                        .orElseThrow();
        gameState.makeMove(longCastleMove);

        GameStatePrinter.print();

        // Verify king's new position
        assertEquals(
                Constants.PieceType.KING,
                board.getPieceAtPosition(2, 7),
                "King should be at c1 after long castle");
        assertEquals(
                Constants.Side.WHITE,
                board.getSideAtPosition(2, 7),
                "White king should be at c1 after long castle");

        // Verify rook's new position
        assertEquals(
                Constants.PieceType.ROOK,
                board.getPieceAtPosition(3, 7),
                "Rook should be at d1 after long castle");
        assertEquals(
                Constants.Side.WHITE,
                board.getSideAtPosition(3, 7),
                "White rook should be at d1 after long castle");

        // Verify old positions are empty
        assertEquals(
                Constants.PieceType.EMPTY,
                board.getPieceAtPosition(0, 7),
                "Original rook position should be empty");
        assertEquals(
                Constants.PieceType.EMPTY,
                board.getPieceAtPosition(4, 7),
                "Original king position should be empty");
    }

    @Test
    public void testGeneratesMoves_ExecuteShortCastle() {
        GameStateFenParser.loadFenString("8/8/8/8/8/8/3PPP2/R2PK2R w KQ - 0 1");
        BoardRepresentation board = gameState.getBoardRepresentation();

        KingMoveGenerator.generateKingMoves(
                moves,
                4,
                7,
                board,
                Constants.Side.WHITE,
                gameState.getIrreversibleData().castlingRights());

        assertEquals(
                2, moves.size(), "White king should have 2 possible moves, including one castle.");
        assertTrue(
                moves.stream().anyMatch(move -> move.moveType() == Move.MoveType.SHORT_CASTLE),
                "White king should be able to perform short castling.");

        Move longCastleMove =
                moves.stream()
                        .filter(move -> move.moveType() == Move.MoveType.SHORT_CASTLE)
                        .findFirst()
                        .orElseThrow();
        gameState.makeMove(longCastleMove);

        GameStatePrinter.print();

        // Verify king's new position
        assertEquals(
                Constants.PieceType.KING,
                board.getPieceAtPosition(6, 7),
                "King should be at g1 after short castle");
        assertEquals(
                Constants.Side.WHITE,
                board.getSideAtPosition(6, 7),
                "White king should be at g1 after short castle");

        // Verify rook's new position
        assertEquals(
                Constants.PieceType.ROOK,
                board.getPieceAtPosition(5, 7),
                "Rook should be at f1 after short castle");
        assertEquals(
                Constants.Side.WHITE,
                board.getSideAtPosition(5, 7),
                "White rook should be at f1 after short castle");

        // Verify old positions are empty
        assertEquals(
                Constants.PieceType.EMPTY,
                board.getPieceAtPosition(7, 7),
                "Original rook position should be empty");
        assertEquals(
                Constants.PieceType.EMPTY,
                board.getPieceAtPosition(4, 7),
                "Original king position should be empty");
    }
}
