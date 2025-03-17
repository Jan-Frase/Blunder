package de.janfrase.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import de.janfrase.ui.board.ChessBoard;

import javax.swing.*;
import java.awt.*;

public class ApplicationWindow {

    private static final ApplicationWindow instance = new ApplicationWindow();

    private final JFrame frame;

    private ApplicationWindow() {
        loadFlatLaf();

        frame = createFrame();
    }

    private static void loadFlatLaf() {
        FlatDarkLaf laf = new FlatDarkLaf();
        try {
            UIManager.setLookAndFeel(laf);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
    }

    public static ApplicationWindow getInstance() {
        return instance;
    }

    private JFrame createFrame() {
        final JFrame frame;
        frame = new JFrame();

        frame.setUndecorated(false);
        frame.setSize(400, 400);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.setLayout(new BorderLayout());
        frame.add(new ChessBoard(), BorderLayout.CENTER);

        frame.setVisible(true);
        return frame;
    }

}
