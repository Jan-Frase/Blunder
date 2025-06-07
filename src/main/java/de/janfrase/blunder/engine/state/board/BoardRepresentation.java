/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.state.board;

import de.janfrase.blunder.utility.Constants;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class represents a higher-level abstraction of a chessboard and
 * provides methods to manipulate and retrieve information about the board's
 * state. It uses a {@link Board8x8} object internally to manage the underlying
 * representation of the board.
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
    public Constants.PieceType getPieceAt(int x, int y) {
        return this.squareBoard.getPieceAtPosition(x, y);
    }

    /**
     * Retrieves the color of the tile at the specified position on the board.
     *
     * @param x The column index (0-based) of the board.
     * @param y The row index (0-based) of the board.
     * @return The color of the specified tile, represented by the {@link Constants.Side} enum.
     */
    public Constants.Side getSideAt(int x, int y) {
        return this.squareBoard.getSideAtPosition(x, y);
    }

    /**
     * Removes a tile from the board at the specified position by setting its
     * piece and color to empty.
     *
     * @param x The column index (0-based) of the board.
     * @param y The row index (0-based) of the board.
     */
    public void clearSquare(int x, int y) {
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
    public void setPieceAt(int x, int y, Constants.PieceType pieceType, Constants.Side color) {
        logger.trace("Placing {} from {} on x={} y={}", pieceType, color, x, y);
        this.squareBoard.fillSquare(x, y, pieceType, color);
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

            // we have found something!
            if (getPieceAt(currentX, currentY) != Constants.PieceType.EMPTY) {
                return Optional.of(new int[] {currentX, currentY});
            }
        }
    }
}
