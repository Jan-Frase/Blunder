package de.janfrase.ui.board.tile;

import de.janfrase.utility.Constants.Colors;
import de.janfrase.utility.Constants.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;

/**
 * Handles the visual representation of chess pieces on the board.
 * This class manages piece icons and their mapping to specific chess pieces and colors.
 */
class TileForegroundPainter {
    private static final String IMAGE_BASE_PATH = "/pieces/";
    private static final String FILE_EXTENSION = ".svg.png";

    private static final EnumMap<Piece, String> pieceNameMap = new EnumMap<>(Piece.class);
    private static final EnumMap<Colors, String> colorNameMap = new EnumMap<>(Colors.class);
    private static final EnumMap<Colors, EnumMap<Piece, BufferedImage>> pieceImageCache = new EnumMap<>(Colors.class);

    static {
        initializePieceNames();
        initializeColorNames();
        loadPieceImages();
    }

    private final ImageJPanel panel = new ImageJPanel();

    public TileForegroundPainter(Piece piece, Colors color) {
        this.panel.setOpaque(false);

        setPiece(piece, color);
    }

    public TileForegroundPainter() {
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
        colorNameMap.put(Colors.BLACK, "black");
        colorNameMap.put(Colors.WHITE, "white");
    }

    private static void loadPieceImages() {
        for (Colors color : colorNameMap.keySet()) {
            EnumMap<Piece, BufferedImage> pieceMap = new EnumMap<>(Piece.class);
            pieceImageCache.put(color, pieceMap);

            for (Piece piece : pieceNameMap.keySet()) {
                String imagePath = buildImagePath(piece, color);
                InputStream imageUrl = TileForegroundPainter.class.getResourceAsStream(imagePath);

                if (imageUrl == null) {
                    throw new IllegalStateException("Chess piece image not found: " + imagePath);
                }

                try {
                    BufferedImage pieceImage = ImageIO.read(imageUrl);
                    pieceMap.put(piece,pieceImage);
                } catch (IOException e) {
                    throw new IllegalStateException("Cant load chess piece image: " + imagePath);
                }
            }
        }
    }

    private static String buildImagePath(Piece piece, Colors colors) {
        return IMAGE_BASE_PATH + pieceNameMap.get(piece) + "_" + colorNameMap.get(colors) + FILE_EXTENSION;
    }

    public JPanel getPanel() {
        return this.panel;
    }

    public void setPiece(Piece piece, Colors colors) {
        EnumMap<Piece, BufferedImage> pieceMap = pieceImageCache.get(colors);
        BufferedImage pieceImage = (pieceMap != null) ? pieceMap.get(piece) : null;

        this.panel.setImage(pieceImage);
        this.panel.repaint();
    }
}
