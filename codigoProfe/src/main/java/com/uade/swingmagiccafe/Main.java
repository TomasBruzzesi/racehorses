package com.uade.swingmagiccafe;

import com.uade.swingmagiccafe.ui.MagicCafeFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MagicCafeFrame frame = new MagicCafeFrame();
            frame.setVisible(true);
        });
    }
}
