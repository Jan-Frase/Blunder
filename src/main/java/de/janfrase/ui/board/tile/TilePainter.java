package de.janfrase.ui.board.tile;

import de.janfrase.utility.Constants;

import javax.swing.*;
import java.awt.*;

public class TilePainter {

    private final JPanel panel = new JPanel();

    private final TileBackgroundPainter backgroundPainter;
    private final TileForegroundPainter foregroundPainter;

    public TilePainter(boolean isWhite) {
        this.panel.setLayout(new BorderLayout());

        this.backgroundPainter = new TileBackgroundPainter(isWhite);
        this.foregroundPainter = new TileForegroundPainter(Constants.Piece.EMPTY, Constants.Colors.EMPTY);

        JPanel bgPanel = backgroundPainter.getPanel();
        // TODO: Is there any way to do this prettier?
        bgPanel.add(foregroundPainter.getPanel());

        this.panel.add(bgPanel, BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPiece(Constants.Piece piece, Constants.Colors colors) {
        this.foregroundPainter.setPiece(piece, colors);
        this.panel.repaint();
    }

}
