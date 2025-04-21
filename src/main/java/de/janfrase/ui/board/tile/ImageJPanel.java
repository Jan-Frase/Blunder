package de.janfrase.ui.board.tile;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageJPanel extends JPanel {

    BufferedImage image;

    public ImageJPanel(BufferedImage image) {
        this.image = image;
    }

    public ImageJPanel() {}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(image == null)
            return;

        Image scaledImage = image.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);

        g.drawImage(scaledImage, 0, 0, null);
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        this.repaint();
    }
}
