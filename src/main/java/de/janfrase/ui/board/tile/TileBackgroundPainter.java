package de.janfrase.ui.board.tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class TileBackgroundPainter {
    private static final Color DARK_COLOR = Color.DARK_GRAY;
    private static final Color DARK_HIGHLIGHT_COLOR = Color.BLACK;

    private static final Color LIGHT_COLOR = Color.GRAY;
    private static final Color LIGHT_HIGHLIGHT_COLOR = Color.LIGHT_GRAY;

    private final JPanel panel = new JPanel();
    private final boolean isWhite;

    private boolean isHighlighted = false;

    public TileBackgroundPainter(boolean isWhite) {
        this.isWhite = isWhite;

        this.panel.setLayout(new BorderLayout());

        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                isHighlighted = !isHighlighted;
                setPanelBackgroundColor();
            }
        });

        setPanelBackgroundColor();
    }

    public JPanel getPanel() {
        return panel;
    }

    private void setPanelBackgroundColor() {
        Color backgroundColor = getBackgroundColor();

        this.panel.setBackground(backgroundColor);
        this.panel.repaint();
    }

    private Color getBackgroundColor() {
        if(this.isHighlighted) {
            if (this.isWhite) {
                return LIGHT_HIGHLIGHT_COLOR;
            } else {
                return DARK_HIGHLIGHT_COLOR;
            }
        }

        if (this.isWhite) {
            return LIGHT_COLOR;
        } else {
            return DARK_COLOR;
        }
    }
}