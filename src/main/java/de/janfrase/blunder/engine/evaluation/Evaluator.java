/* Made by Jan Frase :) */
package de.janfrase.blunder.engine.evaluation;

import de.janfrase.blunder.engine.backend.state.board.BoardRepresentation;
import de.janfrase.blunder.engine.backend.state.game.GameState;
import de.janfrase.blunder.utility.Constants;
import java.util.EnumMap;

/**
 * This currently uses the simplified evaluation function. I should exchange this with some neural network at some point.
 * <a href="https://www.chessprogramming.org/Simplified_Evaluation_Function">Link.</a>
 */
public class Evaluator {

    private static final int[][] PAWN_VALUE_TABLE =
            new int[][] {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {50, 50, 50, 50, 50, 50, 50, 50},
                {10, 10, 20, 30, 30, 20, 10, 10},
                {5, 5, 10, 25, 25, 10, 5, 5},
                {0, 0, 0, 20, 20, 0, 0, 0},
                {5, -5, -10, 0, 0, -10, -5, 5},
                {5, 10, 10, -20, -20, 10, 10, 5},
                {0, 0, 0, 0, 0, 0, 0, 0}
            };

    private static final int[][] KNIGHT_VALUE_TABLE =
            new int[][] {
                {-50, -40, -30, -30, -30, -30, -40, -50},
                {-40, -20, 0, 0, 0, 0, -20, -40},
                {-30, 0, 10, 15, 15, 10, 0, -30},
                {-30, 5, 15, 20, 20, 15, 5, -30},
                {-30, 0, 15, 20, 20, 15, 0, -30},
                {-30, 5, 10, 15, 15, 10, 5, -30},
                {-40, -20, 0, 5, 5, 0, -20, -40},
                {-50, -40, -30, -30, -30, -30, -40, -50}
            };

    private static final int[][] BISHOP_VALUE_TABLE =
            new int[][] {
                {-20, -10, -10, -10, -10, -10, -10, -20},
                {-10, 0, 0, 0, 0, 0, 0, -10},
                {-10, 0, 5, 10, 10, 5, 0, -10},
                {-10, 5, 5, 10, 10, 5, 5, -10},
                {-10, 0, 10, 10, 10, 10, 0, -10},
                {-10, 10, 10, 10, 10, 10, 10, -10},
                {-10, 5, 0, 0, 0, 0, 5, -10},
                {-20, -10, -10, -10, -10, -10, -10, -20}
            };

    private static final int[][] ROOK_VALUE_TABLE =
            new int[][] {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {5, 10, 10, 10, 10, 10, 10, 5},
                {-5, 0, 0, 0, 0, 0, 0, -5},
                {-5, 0, 0, 0, 0, 0, 0, -5},
                {-5, 0, 0, 0, 0, 0, 0, -5},
                {-5, 0, 0, 0, 0, 0, 0, -5},
                {-5, 0, 0, 0, 0, 0, 0, -5},
                {0, 0, 0, 5, 5, 0, 0, 0}
            };

    private static final int[][] QUEEN_VALUE_TABLE =
            new int[][] {
                {-20, -10, -10, -5, -5, -10, -10, -20},
                {-10, 0, 0, 0, 0, 0, 0, -10},
                {-10, 0, 5, 5, 5, 5, 0, -10},
                {-5, 0, 5, 5, 5, 5, 0, -5},
                {0, 0, 5, 5, 5, 5, 0, -5},
                {-10, 5, 5, 5, 5, 5, 0, -10},
                {-10, 0, 5, 0, 0, 0, 0, -10},
                {-20, -10, -10, -5, -5, -10, -10, -20}
            };

    private static final int[][] KING_EARLY_VALUE_TABLE =
            new int[][] {
                {-30, -40, -40, -50, -50, -40, -40, -30},
                {-30, -40, -40, -50, -50, -40, -40, -30},
                {-30, -40, -40, -50, -50, -40, -40, -30},
                {-30, -40, -40, -50, -50, -40, -40, -30},
                {-20, -30, -30, -40, -40, -30, -30, -20},
                {-10, -20, -20, -20, -20, -20, -20, -10},
                {20, 20, 0, 0, 0, 0, 20, 20},
                {20, 30, 10, 0, 0, 10, 30, 20}
            };

    private static final int[][] KING_LATE_VALUE_TABLE =
            new int[][] {
                {-50, -40, -30, -20, -20, -30, -40, -50},
                {-30, -20, -10, 0, 0, -10, -20, -30},
                {-30, -10, 20, 30, 30, 20, -10, -30},
                {-30, -10, 30, 40, 40, 30, -10, -30},
                {-30, -10, 30, 40, 40, 30, -10, -30},
                {-30, -10, 20, 30, 30, 20, -10, -30},
                {-30, -30, 0, 0, 0, 0, -30, -30},
                {-50, -30, -30, -30, -30, -30, -30, -50}
            };

    private static final int[][] FLIP =
            new int[][] {
                {56, 57, 58, 59, 60, 61, 62, 63},
                {48, 49, 50, 51, 52, 53, 54, 55},
                {40, 41, 42, 43, 44, 45, 46, 47},
                {32, 33, 34, 35, 36, 37, 38, 39},
                {24, 25, 26, 27, 28, 29, 30, 31},
                {16, 17, 18, 19, 20, 21, 22, 23},
                {8, 9, 10, 11, 12, 13, 14, 15},
                {0, 1, 2, 3, 4, 5, 6, 7}
            };

    private static final EnumMap<Constants.PieceType, int[][]> mapPieceToValueTable =
            new EnumMap<>(Constants.PieceType.class) {
                {
                    put(Constants.PieceType.PAWN, PAWN_VALUE_TABLE);
                    put(Constants.PieceType.KNIGHT, KNIGHT_VALUE_TABLE);
                    put(Constants.PieceType.BISHOP, BISHOP_VALUE_TABLE);
                    put(Constants.PieceType.ROOK, ROOK_VALUE_TABLE);
                    put(Constants.PieceType.QUEEN, QUEEN_VALUE_TABLE);
                }
            };

    private static final int CASH_DEFAULT_SIZE = (int) (4 * Math.pow(10, 6)); //4 million
    private static final EvaluationCash evaluationCash = new EvaluationCash(CASH_DEFAULT_SIZE);

    public static float calculateEvaluation(GameState gameState) {
        long zobristHash = gameState.getZobristHash();
        return evaluationCash.checkHash(zobristHash, () -> doCalculateEvaluation(gameState));
    }

    private static float doCalculateEvaluation(GameState gameState) {
        float whiteMaterial = 0;
        float blackMaterial = 0;

        BoardRepresentation board = gameState.getBoardRepresentation();

        for (int y = 0; y < Constants.BOARD_SIDE_LENGTH; y++) {
            for (int x = 0; x < Constants.BOARD_SIDE_LENGTH; x++) {
                Constants.PieceType piece = board.getPieceAt(x, y);
                Constants.Side side = board.getSideAt(x, y);

                if (side == Constants.Side.EMPTY) continue;

                float pieceValue = evaluateSinglePiece(x, y, piece, side, board);

                if (side == Constants.Side.WHITE) {
                    whiteMaterial += pieceValue;
                } else {
                    blackMaterial += pieceValue;
                }
            }
        }

        return whiteMaterial - blackMaterial;
    }

    private static float evaluateSinglePiece(
            int x,
            int y,
            Constants.PieceType piece,
            Constants.Side side,
            BoardRepresentation board) {
        float pieceValue = getMaterialValue(piece);

        int xIndex = getXIndex(x, y, side == Constants.Side.WHITE);
        int yIndex = getYIndex(x, y, side == Constants.Side.WHITE);

        int[][] pieceValueTable = getPieceValueTable(piece);
        pieceValue += pieceValueTable[yIndex][xIndex];

        return pieceValue;
    }

    private static int getXIndex(int x, int y, boolean isWhite) {
        if (isWhite) return x;

        return FLIP[y][x] / 8;
    }

    private static int getYIndex(int x, int y, boolean isWhite) {
        if (isWhite) return y;

        return FLIP[y][x] % 8;
    }

    private static int[][] getPieceValueTable(Constants.PieceType pieceType) {
        if (pieceType != Constants.PieceType.KING) {
            return mapPieceToValueTable.get(pieceType);
        }

        if (isLateGame(GameState.getInstance())) {
            return KING_LATE_VALUE_TABLE;
        }

        return KING_EARLY_VALUE_TABLE;
    }

    private static boolean isLateGame(GameState gameState) {
        BoardRepresentation board = gameState.getBoardRepresentation();

        boolean whiteHasQueen =
                board.getPiece(Constants.PieceType.QUEEN, Constants.Side.WHITE).isPresent();
        boolean blackHasQueen =
                board.getPiece(Constants.PieceType.QUEEN, Constants.Side.BLACK).isPresent();

        if (!whiteHasQueen && !blackHasQueen) return true;

        boolean whiteHasRook =
                board.getPiece(Constants.PieceType.ROOK, Constants.Side.WHITE).isPresent();
        boolean blackHasRook =
                board.getPiece(Constants.PieceType.ROOK, Constants.Side.BLACK).isPresent();

        if (whiteHasQueen && whiteHasRook) return false;

        if (blackHasQueen && blackHasRook) return false;

        return !whiteHasQueen || !blackHasQueen;

        // TODO: This isn't correct. We should also check that each side with a queen only has one
        // minor piece.
    }

    public static float getMaterialValue(Constants.PieceType pieceType) {
        return switch (pieceType) {
            case KING -> 20000;
            case QUEEN -> 900;
            case ROOK -> 500;
            case BISHOP -> 330;
            case KNIGHT -> 320;
            case PAWN -> 100;
            case EMPTY -> 0;
        };
    }
}
