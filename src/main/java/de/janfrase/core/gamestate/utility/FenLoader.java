package de.janfrase.core.gamestate.utility;

import de.janfrase.core.gamestate.GameState;
import de.janfrase.core.gamestate.board.BoardRepresentation;
import de.janfrase.utility.Constants;
import org.javatuples.Pair;

import static de.janfrase.core.gamestate.utility.PieceToCharacterConstants.AsciiCharacter;

/**
 * See: <a href="https://www.chessprogramming.org/Forsyth-Edwards_Notation">Chess programming wiki.</a>
 *
 */
public class FenLoader {
    private static final String STARTING_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private static final String RANK_SEPARATOR = "/";
    private static final String SEGMENT_SEPARATOR = " ";


    public static void loadStartingPosition(GameState gameState) {
        loadFenString(STARTING_FEN, gameState);
    }

    public static void loadFenString(String fenString, GameState gameState) {
        String[] fenSegments = fenString.split(SEGMENT_SEPARATOR);

        parsePiecePlacement(fenSegments[FenSegments.PIECE_PLACEMENT.getIndex()], gameState.getBoardRepresentation());
    }

    private static void parsePiecePlacement(String piecePlacement, BoardRepresentation boardRepresentation) {
        int yPos = 0;
        for (String rank : piecePlacement.split(RANK_SEPARATOR)) {
            int xPos = 0;

            for (char c : rank.toCharArray()) {
                if (Character.isDigit(c)) {
                    xPos += Integer.parseInt(String.valueOf(c));
                } else {
                    // Get the fitting character enum for the char. This is a bit convoluted.
                    AsciiCharacter character = PieceToCharacterConstants.AsciiCharacter.getCharacter(String.valueOf(c));
                    // Get the Pair of Black/White and Piece enum.
                    Pair<Constants.Side, Constants.PieceType> piecePair = PieceToCharacterConstants.mapFromAscii.get(character);

                    Constants.Side pieceSide = piecePair.getValue0();
                    Constants.PieceType pieceType = piecePair.getValue1();

                    boardRepresentation.fillSquare(xPos, yPos, pieceType, pieceSide);

                    xPos++;
                }
            }
            yPos++;
        }
    }

    private enum FenSegments {
        PIECE_PLACEMENT(0), SIDE_TO_MOVE(1), CASTLING_ABILITY(2), EN_PASSANT_TARGET_SQUARE(3), HALF_MOVE_CLOCK(4), FULL_MOVE_COUNTER(5);

        private final int index;

        FenSegments(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

}
