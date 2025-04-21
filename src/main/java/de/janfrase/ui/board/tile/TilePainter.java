package de.janfrase.ui.board.tile;

import de.janfrase.utility.Constants;

import javax.swing.*;
import java.awt.*;

public class TilePainter {

    private final JPanel panel = new JPanel();

    private final TileBackgroundPainter backgroundPainter;
    private final TileForegroundPainter foregroundPainter;

    public TilePainter(boolean isWhite) {
        this.backgroundPainter = new TileBackgroundPainter(isWhite);
        this.foregroundPainter = new TileForegroundPainter(Constants.Piece.BISHOP, Constants.Colors.BLACK);

        this.panel.setLayout(new BorderLayout());

        JPanel bgPanel = backgroundPainter.getPanel();
        bgPanel.add(foregroundPainter.getPanel());

        this.panel.add(bgPanel, BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return panel;
    }

}
