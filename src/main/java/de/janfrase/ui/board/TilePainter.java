package de.janfrase.ui.board;

import javax.swing.*;
import java.awt.*;

class TilePainter {
    private static final Color DARK_COLOR = Color.DARK_GRAY;
    private static final Color LIGHT_COLOR = Color.GRAY;

    private final JPanel panel;

    public TilePainter(boolean isWhite) {
        panel = new JPanel();
        setPanelBackgroundColor(isWhite);
    }

    public JPanel getPanel() {
        return panel;
    }

    private void setPanelBackgroundColor(boolean isWhite) {
        if (isWhite) {
            panel.setBackground(LIGHT_COLOR);
        } else {
            panel.setBackground(DARK_COLOR);
        }
    }
}