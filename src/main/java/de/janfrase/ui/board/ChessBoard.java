package de.janfrase.ui.board;

import de.janfrase.ui.board.tile.TilePainter;
import de.janfrase.utility.Constants;

import javax.swing.*;
import java.awt.*;

public class ChessBoard extends JPanel {

    private final TilePainter[][] tilePainters = new TilePainter[Constants.BOARD_WIDTH][Constants.BOARD_WIDTH];

    public ChessBoard() {
        this.setLayout(new GridLayout(Constants.BOARD_WIDTH, Constants.BOARD_WIDTH));

        initTiles();
    }

    private void initTiles() {
        boolean isWhite = true;
        for (int y = 0; y < Constants.BOARD_WIDTH; y++) {
            for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
                TilePainter painter = new TilePainter(isWhite);
                tilePainters[x][y] = painter;
                this.add(painter.getPanel());

                isWhite = !isWhite;
            }
            isWhite = !isWhite; // flip colors after every row
        }
    }
}