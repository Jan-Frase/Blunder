package de.janfrase.ui.board;

import de.janfrase.utility.Constants.Color;
import de.janfrase.utility.Constants.Piece;

import javax.swing.*;
import java.net.URL;
import java.util.EnumMap;

/**
 * Handles the visual representation of chess pieces on the board.
 * This class manages piece icons and their mapping to specific chess pieces and colors.
 */
public class PiecePainter {
    private static final String IMAGE_BASE_PATH = "/pieces/";
    private static final String FILE_EXTENSION = ".svg.png";
    private static final EnumMap<Piece, String> pieceNameMap = new EnumMap<>(Piece.class);
    private static final EnumMap<Color, String> colorNameMap = new EnumMap<>(Color.class);
    private static final EnumMap<Color, EnumMap<Piece, JLabel>> pieceIconCache = new EnumMap<>(Color.class);

    static {
        initializePieceNames();
        initializeColorNames();
        loadPieceImages();
    }

    private Icon pieceIcon;

    public PiecePainter(Piece piece, Color color) {
        setPiece(piece, color);
    }

    public PiecePainter() {
        this.pieceIcon = null;
    }

    private static void initializePieceNames() {
        pieceNameMap.put(Piece.BISHOP, "bishop");
        pieceNameMap.put(Piece.KING, "king");
        pieceNameMap.put(Piece.KNIGHT, "knight");
        pieceNameMap.put(Piece.PAWN, "pawn");
        pieceNameMap.put(Piece.QUEEN, "queen");
        pieceNameMap.put(Piece.ROOK, "rook");
    }

    private static void initializeColorNames() {
        colorNameMap.put(Color.BLACK, "black");
        colorNameMap.put(Color.WHITE, "white");
    }

    private static void loadPieceImages() {
        for (Color color : colorNameMap.keySet()) {
            EnumMap<Piece, Icon> pieceMap = new EnumMap<>(Piece.class);
            pieceIconCache.put(color, pieceMap);

            for (Piece piece : pieceNameMap.keySet()) {
                String imagePath = buildImagePath(piece, color);
                URL imageUrl = PiecePainter.class.getResource(imagePath);

                if (imageUrl == null) {
                    throw new IllegalStateException("Chess piece image not found: " + imagePath);
                }

                pieceMap.put(piece, new ImageIcon(imageUrl));
            }
        }
    }

    private static String buildImagePath(Piece piece, Color color) {
        return IMAGE_BASE_PATH +
                pieceNameMap.get(piece) +
                "_" +
                colorNameMap.get(color) +
                FILE_EXTENSION;
    }

    public Icon getIcon() {
        return pieceIcon;
    }

    public void setPiece(Piece piece, Color color) {
        EnumMap<Piece, Icon> pieceMap = pieceIconCache.get(color);
        pieceIcon = (pieceMap != null) ? pieceMap.get(piece) : null;
    }

    public void removeIcon() {
        this.pieceIcon = null;
    }
}
