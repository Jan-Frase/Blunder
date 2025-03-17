package de.janfrase.ui.board;

import de.janfrase.utility.Constants;

import javax.swing.*;
import java.awt.*;

public class ChessBoard extends JPanel {

    public final Tile[][] tiles = new Tile[Constants.BOARD_WIDTH][Constants.BOARD_WIDTH];

    public ChessBoard() {
        this.setLayout(new GridLayout(Constants.BOARD_WIDTH, Constants.BOARD_WIDTH));

        initTiles();
    }

    private void initTiles() {
        boolean isWhite = true;
        for (int y = 0; y < Constants.BOARD_WIDTH; y++) {
            for (int x = 0; x < Constants.BOARD_WIDTH; x++) {

                tiles[x][y] = new Tile(isWhite);
                this.add(tiles[x][y].getPanel());

                isWhite = !isWhite;
            }
            isWhite = !isWhite; // flip colors after every row
        }
    }
}