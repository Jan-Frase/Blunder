package de.janfrase.core.gamestate.utility;

import de.janfrase.core.gamestate.GameState;
import de.janfrase.core.gamestate.board.BoardRepresentation;

/**
 * See: <a href="https://www.chessprogramming.org/Forsyth-Edwards_Notation">Chess programming wiki.</a>
 * Default String is rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
 */
public class FenLoader {
    private static final String RANK_SEPERATOR = "/";
    private static final String SEGMENT_SEPERATOR = " ";

    public static void loadFenString(String fenString, GameState gameState) {
        String[] fenSegments = fenString.split(SEGMENT_SEPERATOR);

        parsePiecePlacement(fenSegments[FenSegments.PIECE_PLACEMENT.getIndex()], gameState.getBoardRepresentation());
    }

    private static void parsePiecePlacement(String piecePlacement, BoardRepresentation boardRepresentation) {
        int yPos = 0;
        for (String rank : piecePlacement.split(RANK_SEPERATOR)) {
            int xPos = 0;

            for (char c : rank.toCharArray()) {
                if (Character.isDigit(c)) {
                    xPos += Integer.parseInt(String.valueOf(c));
                } else {
                    mapFromAscii

                }
            }

            yPos++;
        }
    }

    private enum FenSegments {
        PIECE_PLACEMENT(0),
        SIDE_TO_MOVE(1),
        CASTLING_ABILITY(2),
        EN_PASSANT_TARGET_SQUARE(3),
        HALF_MOVE_CLOCK(4),
        FULL_MOVE_COUNTER(5);

        private final int index;

        FenSegments(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }
}
