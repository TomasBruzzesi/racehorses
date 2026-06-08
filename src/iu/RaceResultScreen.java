package iu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import dtos.PlayerDTO;
import dtos.RaceResultDTO;

/**
 * Pantalla de resultado al finalizar una carrera.
 */
public class RaceResultScreen extends JFrame {

    private final Runnable onBack;

    public RaceResultScreen(PlayerDTO player, RaceResultDTO result, int totalScore, Runnable onBack) {
        this.onBack = onBack;
        buildUi(player, result, totalScore);
    }

    private void buildUi(PlayerDTO player, RaceResultDTO result, int totalScore) {
        setTitle("Carrera de Caballos - Resultado");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(235, 235, 235));
        root.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 12, 0);

        JLabel title = new JLabel("Resultado de la carrera", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setForeground(new Color(35, 35, 35));
        content.add(title, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 16, 0);
        content.add(new JSeparator(), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 8, 0);
        content.add(centerLabel("Jugador: " + player.getName()), gbc);

        gbc.gridy++;
        content.add(centerLabel("Ganador: " + result.getWinnerName()), gbc);

        gbc.gridy++;
        content.add(centerLabel("Tu posición: " + result.getPlayerPosition() + "°"), gbc);

        gbc.gridy++;
        content.add(centerLabel("Puntos de esta carrera: +" + result.getPointsEarned()), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(8, 0, 24, 0);
        JLabel totalLabel = new JLabel("Puntaje total: " + totalScore, SwingConstants.CENTER);
        totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD, 16f));
        totalLabel.setForeground(new Color(35, 35, 35));
        content.add(totalLabel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.NONE;
        JButton backButton = new JButton("Volver");
        backButton.setPreferredSize(new Dimension(120, 32));
        backButton.addActionListener(e -> goBack());
        content.add(backButton, gbc);

        root.add(content, BorderLayout.CENTER);
        setContentPane(root);
        pack();
        setMinimumSize(new Dimension(460, 300));
        setLocationRelativeTo(null);
    }

    private JLabel centerLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(new Color(60, 60, 60));
        return label;
    }

    private void goBack() {
        setVisible(false);
        dispose();
        onBack.run();
    }
}
