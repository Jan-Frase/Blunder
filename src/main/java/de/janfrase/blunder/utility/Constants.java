/* Made by Jan Frase :) */
package de.janfrase.blunder.utility;

public class Constants {
    public static final int BOARD_SIDE_LENGTH = 8;

    public static final int BOARD_TOTAL_SIZE = BOARD_SIDE_LENGTH * BOARD_SIDE_LENGTH;

    public static boolean isOffBoard(int x, int y) {
        return x < 0 || x >= BOARD_SIDE_LENGTH || y < 0 || y >= BOARD_SIDE_LENGTH;
    }

    // Diagonal directions: bot-right, bot-left, top-right, top-left
    public static final int[][] DIAGONAL_DIRECTIONS = {{1, 1}, {-1, 1}, {1, -1}, {-1, -1}};
    public static final int[][] STRAIGHT_DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    public static final int[][] KNIGHT_DIRECTIONS = {
        {1, 2}, {-1, 2}, {1, -2}, {-1, -2}, {2, 1}, {-2, 1}, {2, -1}, {-2, -1}
    };

    /**
     * Not meant for instantiation. Constants only!
     */
    private Constants() {}
}
