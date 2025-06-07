/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.movegen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.janfrase.blunder.engine.movegen.move.Move;
import de.janfrase.blunder.engine.state.board.BoardRepresentation;
import de.janfrase.blunder.engine.state.game.GameState;
import de.janfrase.blunder.engine.state.game.GameStateFenParser;
import de.janfrase.blunder.engine.state.game.GameStatePrinter;
import de.janfrase.blunder.utility.Constants;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class KingMoveGeneratorTest {

    private final GameState gameState = GameState.getInstance();
    private ArrayList<Move> moves = new ArrayList<>();

    @AfterEach
    void setUp() {
        GameState.resetGameState();
        moves = new ArrayList<>();
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
                board.getPieceAt(2, 7),
                "King should be at c1 after long castle");
        assertEquals(
                Constants.Side.WHITE,
                board.getSideAt(2, 7),
                "White king should be at c1 after long castle");

        // Verify rook's new position
        assertEquals(
                Constants.PieceType.ROOK,
                board.getPieceAt(3, 7),
                "Rook should be at d1 after long castle");
        assertEquals(
                Constants.Side.WHITE,
                board.getSideAt(3, 7),
                "White rook should be at d1 after long castle");

        // Verify old positions are empty
        assertEquals(
                Constants.PieceType.EMPTY,
                board.getPieceAt(0, 7),
                "Original rook position should be empty");
        assertEquals(
                Constants.PieceType.EMPTY,
                board.getPieceAt(4, 7),
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
                board.getPieceAt(6, 7),
                "King should be at g1 after short castle");
        assertEquals(
                Constants.Side.WHITE,
                board.getSideAt(6, 7),
                "White king should be at g1 after short castle");

        // Verify rook's new position
        assertEquals(
                Constants.PieceType.ROOK,
                board.getPieceAt(5, 7),
                "Rook should be at f1 after short castle");
        assertEquals(
                Constants.Side.WHITE,
                board.getSideAt(5, 7),
                "White rook should be at f1 after short castle");

        // Verify old positions are empty
        assertEquals(
                Constants.PieceType.EMPTY,
                board.getPieceAt(7, 7),
                "Original rook position should be empty");
        assertEquals(
                Constants.PieceType.EMPTY,
                board.getPieceAt(4, 7),
                "Original king position should be empty");
    }

    @Test
    void testLongCastleAttackedByKnight() {
        GameStateFenParser.loadFenString(
                "r3k2r/p1ppqpb1/bnN1pnp1/3P4/1p2P3/2N2Q1p/PPPBBPPP/R3K2R b KQkq - 0 1");

        KingMoveGenerator.generateKingMoves(
                moves,
                4,
                0,
                gameState.getBoardRepresentation(),
                gameState.isWhitesTurn() ? Constants.Side.WHITE : Constants.Side.BLACK,
                gameState.getIrreversibleData().castlingRights());

        assertEquals(
                3, moves.size(), "White king should have 3 possible moves, including one castle.");
        assertEquals(
                1,
                moves.stream()
                        .filter(move -> move.moveType() == Move.MoveType.SHORT_CASTLE)
                        .count(),
                "White king can short castle.");
        assertEquals(
                2,
                moves.stream().filter(move -> move.moveType() == Move.MoveType.NORMAL_MOVE).count(),
                "White king move in two directions.");
    }

    @Test
    void testLongCastleAttackedByKnight2() {
        GameStateFenParser.loadFenString(
                "r3k2r/p1pNqpb1/bn2pnp1/3P4/1p2P3/2N2Q1p/PPPBBPPP/R3K2R b KQkq - 0 1");

        KingMoveGenerator.generateKingMoves(
                moves,
                4,
                0,
                gameState.getBoardRepresentation(),
                gameState.isWhitesTurn() ? Constants.Side.WHITE : Constants.Side.BLACK,
                gameState.getIrreversibleData().castlingRights());

        assertEquals(
                4, moves.size(), "White king should have 4 possible moves, including one castle.");
        assertEquals(
                1,
                moves.stream().filter(move -> move.moveType() == Move.MoveType.LONG_CASTLE).count(),
                "White king can long castle.");
        assertEquals(
                3,
                moves.stream().filter(move -> move.moveType() == Move.MoveType.NORMAL_MOVE).count(),
                "White king move in two directions.");
    }

    @Test
    void testShortCastleAttackedByBishop() {
        GameStateFenParser.loadFenString(
                "r3k2r/Pppp1ppp/1b3nb1/nPB2N2/B1P1P3/5N2/qp1P2PP/R2Q1RK1 b kq - 1 2");

        KingMoveGenerator.generateKingMoves(
                moves,
                4,
                0,
                gameState.getBoardRepresentation(),
                gameState.isWhitesTurn() ? Constants.Side.WHITE : Constants.Side.BLACK,
                gameState.getIrreversibleData().castlingRights());

        assertEquals(
                4, moves.size(), "Black king should have 4 possible moves, including one castle.");
    }
}
