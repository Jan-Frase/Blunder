/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.movegen;

import de.janfrase.blunder.engine.movegen.move.Move;
import de.janfrase.blunder.engine.state.board.BoardRepresentation;
import de.janfrase.blunder.utility.Constants;
import java.util.ArrayList;
import java.util.OptionalInt;

public class PawnMoveGenerator {

    private static final int[] x_attack_directions = {1, -1};

    // I have to admit - this method is a bit ugly
    protected static void generatePawnMove(
            ArrayList<Move> moves,
            int x,
            int y,
            BoardRepresentation board,
            Constants.Side activeSide,
            OptionalInt enPassantX) {
        int moveDirection = (activeSide == Constants.Side.WHITE ? -1 : 1);
        int yDestination = y + moveDirection;

        generateStepForward(moves, x, y, board, yDestination);

        generateDoubleStepForward(moves, x, y, board, moveDirection, activeSide);

        generateAttacks(moves, x, y, board, activeSide, yDestination, enPassantX);
    }

    private static void generateDoubleStepForward(
            ArrayList<Move> moves,
            int x,
            int y,
            BoardRepresentation board,
            int moveDirection,
            Constants.Side activeSide) {
        // can't double push outside the starting square
        if (!isOnStartingSquare(y, activeSide)) {
            return;
        }

        int yInBetween = y + moveDirection;
        int yDestination = y + 2 * moveDirection;

        Constants.Side sideInBetween = board.getSideAtPosition(x, yInBetween);
        Constants.Side sideAtDestination = board.getSideAtPosition(x, yDestination);

        // both squares in front are empty
        if (sideInBetween == Constants.Side.EMPTY && sideAtDestination == Constants.Side.EMPTY) {
            moves.add(new Move(x, y, x, yDestination, Move.MoveType.DOUBLE_PAWN_PUSH));
        }
    }

    private static void generateAttacks(
            ArrayList<Move> moves,
            int x,
            int y,
            BoardRepresentation board,
            Constants.Side activeSide,
            int yDestination,
            OptionalInt enPassantX) {
        for (int x_attack_dir : x_attack_directions) {
            int xDestination = x + x_attack_dir;

            if (MoveGenerator.isOffBoard(xDestination, yDestination)) continue;

            int enPassantY = activeSide == Constants.Side.WHITE ? 2 : 5;

            // we can capture an en passant :D
            if (enPassantX.isPresent()
                    && xDestination == enPassantX.getAsInt()
                    && yDestination == enPassantY) {
                moves.add(
                        new Move(
                                x,
                                y,
                                xDestination,
                                yDestination,
                                Move.MoveType.EP_CAPTURE,
                                Constants.PieceType.PAWN));
                continue;
            }

            Constants.Side sideAtAttackDestination =
                    board.getSideAtPosition(xDestination, yDestination);
            Constants.PieceType pieceAtAttackDestination =
                    board.getPieceAtPosition(xDestination, yDestination);

            // nothing to capture
            if (sideAtAttackDestination == Constants.Side.EMPTY) {
                continue;
            }

            // can't capture friendly piece
            if (sideAtAttackDestination == activeSide) {
                continue;
            }

            // we are promoting!
            if (yDestination == 0 || yDestination == Constants.BOARD_SIDE_LENGTH - 1) {
                moves.add(
                        new Move(
                                x,
                                y,
                                xDestination,
                                yDestination,
                                Move.MoveType.ROOK_PROMOTION,
                                pieceAtAttackDestination));
                moves.add(
                        new Move(
                                x,
                                y,
                                xDestination,
                                yDestination,
                                Move.MoveType.KNIGHT_PROMOTION,
                                pieceAtAttackDestination));
                moves.add(
                        new Move(
                                x,
                                y,
                                xDestination,
                                yDestination,
                                Move.MoveType.BISHOP_PROMOTION,
                                pieceAtAttackDestination));
                moves.add(
                        new Move(
                                x,
                                y,
                                xDestination,
                                yDestination,
                                Move.MoveType.QUEEN_PROMOTION,
                                pieceAtAttackDestination));
            } else {
                moves.add(new Move(x, y, xDestination, yDestination, pieceAtAttackDestination));
            }
        }
    }

    private static void generateStepForward(
            ArrayList<Move> moves, int x, int y, BoardRepresentation board, int yDestination) {
        Constants.Side sideAtDestination = board.getSideAtPosition(x, yDestination);
        // the square in front is empty
        if (sideAtDestination == Constants.Side.EMPTY) {
            // we are promoting!
            if (yDestination == 0 || yDestination == Constants.BOARD_SIDE_LENGTH - 1) {
                moves.add(new Move(x, y, x, yDestination, Move.MoveType.ROOK_PROMOTION));
                moves.add(new Move(x, y, x, yDestination, Move.MoveType.KNIGHT_PROMOTION));
                moves.add(new Move(x, y, x, yDestination, Move.MoveType.BISHOP_PROMOTION));
                moves.add(new Move(x, y, x, yDestination, Move.MoveType.QUEEN_PROMOTION));
            } else {
                moves.add(new Move(x, y, x, yDestination));
            }
        }
    }

    private static boolean isOnStartingSquare(int y, Constants.Side activeSide) {
        if (activeSide == Constants.Side.WHITE) {
            return y == 6;
        }
        if (activeSide == Constants.Side.BLACK) {
            return y == 1;
        }
        throw new IllegalStateException("We should not be here!");
    }
}
