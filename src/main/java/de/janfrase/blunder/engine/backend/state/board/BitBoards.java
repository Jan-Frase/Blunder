/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.backend.state.board;

import de.janfrase.blunder.engine.backend.Piece;
import de.janfrase.blunder.utility.Constants;
import java.util.Optional;

public class BitBoards {
    BitBoard[] bitBoards = new BitBoard[12];

    BitBoard whitePawnBitBoard = new BitBoard();
    BitBoard blackPawnBitBoard = new BitBoard();
    BitBoard whiteRookBitBoard = new BitBoard();
    BitBoard blackRookBitBoard = new BitBoard();
    BitBoard whiteKnightBitBoard = new BitBoard();
    BitBoard blackKnightBitBoard = new BitBoard();
    BitBoard whiteBishopBitBoard = new BitBoard();
    BitBoard blackBishopBitBoard = new BitBoard();
    BitBoard whiteQueenBitBoard = new BitBoard();
    BitBoard blackQueenBitBoard = new BitBoard();
    BitBoard whiteKingBitBoard = new BitBoard();
    BitBoard blackKingBitBoard = new BitBoard();

    public BitBoards() {}

    private int pieceToIndex(Piece piece) {
        return switch (piece.value) {
            case Piece.IS_WHITE | Piece.PAWN -> 0;
            case Piece.IS_BLACK | Piece.PAWN -> 1;
            case Piece.IS_WHITE | Piece.ROOK -> 2;
            case Piece.IS_BLACK | Piece.ROOK -> 3;
            case Piece.IS_WHITE | Piece.KNIGHT -> 4;
            case Piece.IS_BLACK | Piece.KNIGHT -> 5;
            case Piece.IS_WHITE | Piece.BISHOP -> 6;
            case Piece.IS_BLACK | Piece.BISHOP -> 7;
            case Piece.IS_WHITE | Piece.QUEEN -> 8;
            case Piece.IS_BLACK | Piece.QUEEN -> 9;
            case Piece.IS_WHITE | Piece.KING -> 10;
            case Piece.IS_BLACK | Piece.KING -> 11;

            default -> throw new IllegalStateException("Unexpected value: " + piece);
        };
    }

    public BitBoard getBitBoard(Piece piece) {
        int index = pieceToIndex(piece);
        return bitBoards[index];
    }

    public BitBoard getAllPieces() {
        BitBoard allPieces = new BitBoard();

        for (BitBoard bitBoard : bitBoards) {
            allPieces.value |= bitBoard.value;
        }

        return allPieces;
    }

    /**
     * Finds the first obstacle in a given direction starting from the specified position on the board.
     * An obstacle is defined as a board square that contains a piece (not empty). The search continues
     * in the specified direction until an obstacle is encountered or the edge of the board is reached.
     *
     * @param x   The starting x-coordinate (column index) on the board.
     * @param y   The starting y-coordinate (row index) on the board.
     * @param dir An array representing the direction to move in, where dir[0] indicates
     *            the x increment, and dir[1] indicates the y increment for each step.
     * @return An {@code Optional<int[]>} containing the coordinates of the first obstacle as
     *         an array {x, y} if an obstacle is found, or {@code Optional.empty()} if the
     *         search reaches the edge of the board without encountering an obstacle.
     */
    public Optional<int[]> firstObstacleInDir(int x, int y, int[] dir) {
        BitBoard allPieces = getAllPieces();

        int currentX = x;
        int currentY = y;

        // loop until we find something or leave the board
        while (true) {
            currentX += dir[0];
            currentY += dir[1];

            // we have left the board -> there is nothing in this direction
            if (Constants.isOffBoard(currentX, currentY)) {
                return Optional.empty();
            }

            long currentPiece = 1L << BitBoard.calculateIndex(currentX, currentY);

            long isSomethingHere = allPieces.value & currentPiece;

            if (isSomethingHere != 0) {
                return Optional.of(new int[] {currentX, currentY});
            }
        }
    }
}
