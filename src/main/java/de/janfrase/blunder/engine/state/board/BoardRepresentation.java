package de.janfrase.blunder.engine.state.board;

import de.janfrase.blunder.utility.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class combines a square-centric representation and a piece-centric representation.
 * By doing this, I can combine the advantages of both.
 */
public class BoardRepresentation {
    private static final Logger logger = LogManager.getLogger();

    private final Board8x8 squareBoard;

    public BoardRepresentation() {
        logger.debug("Creating new BoardRepresentation.");
        this.squareBoard = new Board8x8();
    }

    /**
     * Retrieves the piece located at the specified position on the board.
     *
     * @param x The column index (0-based) of the board.
     * @param y The row index (0-based) of the board.
     * @return The piece located at the specified position, represented by the {@link Constants.PieceType} enum.
     */
    public Constants.PieceType getPieceAtPosition(int x, int y) {
        return this.squareBoard.getPieceAtPosition(x, y);
    }

    /**
     * Retrieves the color of the tile at the specified position on the board.
     *
     * @param x The column index (0-based) of the board.
     * @param y The row index (0-based) of the board.
     * @return The color of the specified tile, represented by the {@link Constants.Side} enum.
     */
    public Constants.Side getSideAtPosition(int x, int y) {
        return this.squareBoard.getSideAtPosition(x, y);
    }

    /**
     * Removes a tile from the board at the specified position by setting its
     * piece and color to empty.
     *
     * @param x The column index (0-based) of the board.
     * @param y The row index (0-based) of the board.
     */
    public void makeSquareEmpty(int x, int y) {
        this.squareBoard.makeSquareEmpty(x, y);
    }

    /**
     * Places a piece with a specific color at the given position on the board.
     *
     * @param x     The column index (0-based) of the board.
     * @param y     The row index (0-based) of the board.
     * @param pieceType The piece to be added at the specified position, represented by the {@link Constants.PieceType} enum.
     * @param color The color associated with the piece to be placed, represented by the {@link Constants.Side} enum.
     */
    public void fillSquare(int x, int y, Constants.PieceType pieceType, Constants.Side color) {
        this.squareBoard.fillSquare(x, y, pieceType, color);
    }
}
