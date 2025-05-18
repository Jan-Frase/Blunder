package de.janfrase.core.gamestate.board;

import de.janfrase.utility.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;

/**
 * This class combines a square-centric representation and a piece-centric representation.
 * By doing this I can combine the advantages of both.
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
     * @return The piece located at the specified position, represented by the {@link Constants.Piece} enum.
     */
    public Constants.Piece getPieceAtPosition(int x, int y) {
        return this.squareBoard.getPieceAtPosition(x, y);
    }

    /**
     * Retrieves the color of the tile at the specified position on the board.
     *
     * @param x The column index (0-based) of the board.
     * @param y The row index (0-based) of the board.
     * @return The color of the specified tile, represented by the {@link Constants.Colors} enum.
     */
    public Constants.Colors getColorAtPosition(int x, int y) {
        return this.squareBoard.getColorAtPosition(x, y);
    }

    /**
     * Retrieves the piece and color of the tile at the specified position on the board.
     *
     * @param x The column index (0-based) of the board.
     * @param y The row index (0-based) of the board.
     * @return A Pair containing the {@link Constants.Piece} located at the specified position and
     * the {@link Constants.Colors} of the tile.
     */
    public Pair<Constants.Piece, Constants.Colors> getTile(int x, int y) {
        return this.squareBoard.getTile(x, y);
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
     * @param piece The piece to be added at the specified position, represented by the {@link Constants.Piece} enum.
     * @param color The color associated with the piece to be placed, represented by the {@link Constants.Colors} enum.
     */
    public void fillSquare(int x, int y, Constants.Piece piece, Constants.Colors color) {
        this.squareBoard.fillSquare(x, y, piece, color);
    }
}
