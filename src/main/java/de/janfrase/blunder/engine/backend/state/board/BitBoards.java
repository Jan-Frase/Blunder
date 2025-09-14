/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.backend.state.board;

import de.janfrase.blunder.engine.backend.Piece;
import de.janfrase.blunder.utility.Constants;
import java.util.Optional;

public class BitBoards {

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

    private final BitBoard[] bitBoards = {
        whitePawnBitBoard,
        blackPawnBitBoard,
        whiteRookBitBoard,
        blackRookBitBoard,
        whiteKnightBitBoard,
        blackKnightBitBoard,
        whiteBishopBitBoard,
        blackBishopBitBoard,
        whiteQueenBitBoard,
        blackQueenBitBoard,
        whiteKingBitBoard,
        blackKingBitBoard
    };

    public static final Piece[] allPossiblePieces = {
        new Piece(Piece.PAWN, Piece.WHITE),
        new Piece(Piece.PAWN, Piece.BLACK),
        new Piece(Piece.ROOK, Piece.WHITE),
        new Piece(Piece.ROOK, Piece.BLACK),
        new Piece(Piece.KNIGHT, Piece.WHITE),
        new Piece(Piece.KNIGHT, Piece.BLACK),
        new Piece(Piece.BISHOP, Piece.WHITE),
        new Piece(Piece.BISHOP, Piece.BLACK),
        new Piece(Piece.QUEEN, Piece.WHITE),
        new Piece(Piece.QUEEN, Piece.BLACK),
        new Piece(Piece.KING, Piece.WHITE),
        new Piece(Piece.KING, Piece.BLACK)
    };

    public BitBoards() {}

    private int pieceToIndex(Piece piece) {
        return switch (piece.value) {
            case Piece.WHITE | Piece.PAWN -> 0;
            case Piece.BLACK | Piece.PAWN -> 1;
            case Piece.WHITE | Piece.ROOK -> 2;
            case Piece.BLACK | Piece.ROOK -> 3;
            case Piece.WHITE | Piece.KNIGHT -> 4;
            case Piece.BLACK | Piece.KNIGHT -> 5;
            case Piece.WHITE | Piece.BISHOP -> 6;
            case Piece.BLACK | Piece.BISHOP -> 7;
            case Piece.WHITE | Piece.QUEEN -> 8;
            case Piece.BLACK | Piece.QUEEN -> 9;
            case Piece.WHITE | Piece.KING -> 10;
            case Piece.BLACK | Piece.KING -> 11;

            default -> throw new IllegalStateException("Unexpected value: " + piece);
        };
    }

    public void clearSquare(int x, int y) {
        for (BitBoard bitBoard : bitBoards) {
            bitBoard.clearBit(x, y);
        }
    }

    public Piece getPieceAt(int x, int y) {
        for (int i = 0; i < bitBoards.length; i++) {
            BitBoard bitBoard = bitBoards[i];
            boolean isNotEmpty = bitBoard.getBit(x, y);
            if (isNotEmpty) {
                return allPossiblePieces[i];
            }
        }
        return Piece.createEmptyPiece();
    }

    public void setPieceAt(int x, int y, Piece piece) {
        BitBoard bitBoard = bitBoards[pieceToIndex(piece)];
        bitBoard.setBit(x, y);
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
     * Searches in a specified direction from the given starting coordinates for the first obstacle (piece)
     * on the board and returns its coordinates if found. If no obstacle exists in that direction or the
     * search goes off the board, an empty {@link Optional} is returned.
     *
     * @param x   The starting x-coordinate, representing the column index on the board.
     * @param y   The starting y-coordinate, representing the row index on the board.
     * @param dir An integer array representing the directional vector for the search, where
     *            dir[0] is the change in x-coordinate and dir[1] is the change in y-coordinate
     *            (e.g., {1, 0} for rightward movement, {-1, 1} for upward-left diagonal movement).
     * @return An {@link Optional} containing an integer array with the x and y coordinates of the
     *         first obstacle encountered in the specified direction, or an empty {@link Optional}
     *         if no obstacle is found within the boundaries of the board.
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

            // Create Mask for the current position
            long currentPiece = BitBoard.getLongWithBitAtIndex(currentX, currentY);
            // Check if any piece occupies the masked position
            boolean isSomethingHere = (allPieces.value & currentPiece) != 0;

            if (isSomethingHere) {
                return Optional.of(new int[] {currentX, currentY});
            }
        }
    }
}
