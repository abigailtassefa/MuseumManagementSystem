package com.museum;

import com.museum.gui.WelcomeFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WelcomeFrame();
        });
    }
} 