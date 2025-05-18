package de.janfrase.core.gamestate.board;

import de.janfrase.utility.Constants;
import de.janfrase.utility.Constants.Side;
import de.janfrase.utility.Constants.PieceType;
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
    private final PieceType[] pieceTypes;
    private final Side[] side;

    public Board8x8() {
        this.pieceTypes = new PieceType[Constants.BOARD_TOTAL_SIZE];
        this.side = new Side[Constants.BOARD_TOTAL_SIZE];

        for (int i = 0; i < Constants.BOARD_TOTAL_SIZE; i++) {
            this.pieceTypes[i] = PieceType.EMPTY;
            this.side[i] = Side.EMPTY;
        }
    }

    /**
     * Retrieves the piece located at the specified position on the board.
     *
     * @param x The column index (0-based) of the board.
     * @param y The row index (0-based) of the board.
     * @return The piece located at the specified position, represented by the {@link PieceType} enum.
     */
    public PieceType getPieceAtPosition(int x, int y) {
        return pieceTypes[convertIndices(x, y)];
    }

    /**
     * Retrieves the color of the tile at the specified position on the board.
     *
     * @param x The column index (0-based) of the board.
     * @param y The row index (0-based) of the board.
     * @return The color of the specified tile, represented by the {@link Side} enum.
     */
    public Side getColorAtPosition(int x, int y) {
        return side[convertIndices(x, y)];
    }

    /**
     * Retrieves the piece and color of the tile at the specified position on the board.
     *
     * @param x The column index (0-based) of the board.
     * @param y The row index (0-based) of the board.
     * @return A Pair containing the {@link PieceType} located at the specified position and
     * the {@link Side} of the tile.
     */
    public Pair<PieceType, Side> getTile(int x, int y) {
        PieceType pieceType = getPieceAtPosition(x, y);
        Side side = getColorAtPosition(x, y);
        return new Pair<>(pieceType, side);
    }

    /**
     * Removes a tile from the board at the specified position by setting its
     * piece and color to empty.
     *
     * @param x The column index (0-based) of the board.
     * @param y The row index (0-based) of the board.
     */
    public void makeSquareEmpty(int x, int y) {
        this.pieceTypes[convertIndices(x, y)] = PieceType.EMPTY;
        this.side[convertIndices(x, y)] = Side.EMPTY;
    }

    /**
     * Places a piece with a specific color at the given position on the board.
     *
     * @param x     The column index (0-based) of the board.
     * @param y     The row index (0-based) of the board.
     * @param pieceType The piece to be added at the specified position, represented by the {@link PieceType} enum.
     * @param color The color associated with the piece to be placed, represented by the {@link Side} enum.
     */
    public void fillSquare(int x, int y, PieceType pieceType, Side color) {
        this.pieceTypes[convertIndices(x, y)] = pieceType;
        this.side[convertIndices(x, y)] = color;
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
}
