package de.janfrase.ui.board;

import javax.swing.*;
import java.awt.*;

class Tile {
    private static final Color DARK_COLOR = Color.DARK_GRAY;
    private static final Color LIGHT_COLOR = Color.GRAY;

    private final boolean isWhite;
    private final JPanel panel;

    public Tile(boolean isWhite) {
        panel = new JPanel();
        this.isWhite = isWhite;

        setColor();
    }

    public JPanel getPanel() {
        return panel;
    }

    private void setColor() {
        if (this.isWhite) {
            panel.setBackground(LIGHT_COLOR);
        } else {
            panel.setBackground(DARK_COLOR);
        }
    }
}