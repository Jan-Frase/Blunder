package de.janfrase.core.board;

import de.janfrase.utility.Constants.Colors;
import de.janfrase.utility.Constants.Piece;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;

/**
 * Represents an 8x8 chessboard containing tiles with pieces and their corresponding colors.
 * This class provides methods to manipulate and retrieve information about the board's tiles,
 * including their pieces and colors. The board is initialized according to the standard chess rules.
 */
public class Board8x8 {
    private static final Logger logger = LogManager.getLogger();
    private Piece[] pieces;
    private Colors[] colors;

    public Board8x8() {
        initPieces();
    }

    /**
     * Retrieves the piece located at the specified position on the board.
     *
     * @param x The column index (0-based) of the board.
     * @param y The row index (0-based) of the board.
     * @return The piece located at the specified position, represented by the {@link Piece} enum.
     */
    public Piece getPieceAtPosition(int x, int y) {
        return pieces[convertIndices(x, y)];
    }

    /**
     * Retrieves the color of the tile at the specified position on the board.
     *
     * @param x The column index (0-based) of the board.
     * @param y The row index (0-based) of the board.
     * @return The color of the specified tile, represented by the {@link Colors} enum.
     */
    public Colors getColorAtPosition(int x, int y) {
        return colors[convertIndices(x, y)];
    }

    /**
     * Retrieves the piece and color of the tile at the specified position on the board.
     *
     * @param x The column index (0-based) of the board.
     * @param y The row index (0-based) of the board.
     * @return A Pair containing the {@link Piece} located at the specified position and
     * the {@link Colors} of the tile.
     */
    public Pair<Piece, Colors> getTile(int x, int y) {
        Piece piece = getPieceAtPosition(x, y);
        Colors colors = getColorAtPosition(x, y);
        return new Pair<>(piece, colors);
    }

    /**
     * Removes a tile from the board at the specified position by setting its
     * piece and color to empty.
     *
     * @param x The column index (0-based) of the board.
     * @param y The row index (0-based) of the board.
     */
    public void makeSquareEmpty(int x, int y) {
        this.pieces[convertIndices(x, y)] = Piece.EMPTY;
        this.colors[convertIndices(x, y)] = Colors.EMPTY;
    }

    /**
     * Places a piece with a specific color at the given position on the board.
     *
     * @param x     The column index (0-based) of the board.
     * @param y     The row index (0-based) of the board.
     * @param piece The piece to be added at the specified position, represented by the {@link Piece} enum.
     * @param color The color associated with the piece to be placed, represented by the {@link Colors} enum.
     */
    public void fillSquare(int x, int y, Piece piece, Colors color) {
        this.pieces[convertIndices(x, y)] = piece;
        this.colors[convertIndices(x, y)] = color;
    }


    /**
     * Converts a two-dimensional board index (x, y) into a one-dimensional array index.
     *
     * @param x The column index (0-based) of the board.
     * @param y The row index (0-based) of the board.
     * @return The converted one-dimensional array index.
     */
    private int convertIndices(int x, int y) {
        return x + y * 8;
    }

    /**
     * Initializes the board pieces and their respective colors for an 8x8 chessboard.
     * <p>
     * The method assigns specific chess pieces to their initial positions for both
     * black and white sides, filling the intermediary squares with empty values.
     * Additionally, it sets the color of each square to signify the side it belongs to
     * (BLACK, WHITE, or EMPTY for unoccupied squares).
     */
    private void initPieces() {
        // TODO: Remove this lol.
        this.pieces = new Piece[]{
                Piece.ROOK, Piece.KNIGHT, Piece.BISHOP, Piece.QUEEN, Piece.KING, Piece.BISHOP, Piece.KNIGHT, Piece.ROOK,
                Piece.PAWN, Piece.PAWN, Piece.PAWN, Piece.PAWN, Piece.PAWN, Piece.PAWN, Piece.PAWN, Piece.PAWN,
                Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY,
                Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY,
                Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY,
                Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY,
                Piece.PAWN, Piece.PAWN, Piece.PAWN, Piece.PAWN, Piece.PAWN, Piece.PAWN, Piece.PAWN, Piece.PAWN,
                Piece.ROOK, Piece.KNIGHT, Piece.BISHOP, Piece.QUEEN, Piece.KING, Piece.BISHOP, Piece.KNIGHT, Piece.ROOK
        };

        this.colors = new Colors[]{
                Colors.BLACK, Colors.BLACK, Colors.BLACK, Colors.BLACK, Colors.BLACK, Colors.BLACK, Colors.BLACK, Colors.BLACK,
                Colors.BLACK, Colors.BLACK, Colors.BLACK, Colors.BLACK, Colors.BLACK, Colors.BLACK, Colors.BLACK, Colors.BLACK,
                Colors.EMPTY, Colors.EMPTY, Colors.EMPTY, Colors.EMPTY, Colors.EMPTY, Colors.EMPTY, Colors.EMPTY, Colors.EMPTY,
                Colors.EMPTY, Colors.EMPTY, Colors.EMPTY, Colors.EMPTY, Colors.EMPTY, Colors.EMPTY, Colors.EMPTY, Colors.EMPTY,
                Colors.EMPTY, Colors.EMPTY, Colors.EMPTY, Colors.EMPTY, Colors.EMPTY, Colors.EMPTY, Colors.EMPTY, Colors.EMPTY,
                Colors.EMPTY, Colors.EMPTY, Colors.EMPTY, Colors.EMPTY, Colors.EMPTY, Colors.EMPTY, Colors.EMPTY, Colors.EMPTY,
                Colors.WHITE, Colors.WHITE, Colors.WHITE, Colors.WHITE, Colors.WHITE, Colors.WHITE, Colors.WHITE, Colors.WHITE,
                Colors.WHITE, Colors.WHITE, Colors.WHITE, Colors.WHITE, Colors.WHITE, Colors.WHITE, Colors.WHITE, Colors.WHITE
        };
    }
}
