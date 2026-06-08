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

import controllers.RaceSystemController;
import dtos.PlayerDTO;

/**
 * Pantalla de puntaje del jugador logueado.
 * Solo utiliza RaceSystemController.
 */
public class PlayerScoreScreen extends JFrame {

    private final Runnable onBack;

    public PlayerScoreScreen(RaceSystemController controller, PlayerDTO player, Runnable onBack) {
        this.onBack = onBack;
        buildUi(controller, player);
    }

    private void buildUi(RaceSystemController controller, PlayerDTO player) {
        setTitle("Carrera de Caballos - Puntaje");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(238, 238, 238));
        root.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 12, 0);

        JLabel title = new JLabel("Puntaje del jugador", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        content.add(title, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 16, 0);
        content.add(new JSeparator(), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 8, 0);
        JLabel nameLabel = new JLabel("Jugador: " + player.getName(), SwingConstants.CENTER);
        content.add(nameLabel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 24, 0);
        int score = controller.getPlayerScore();
        JLabel scoreLabel = new JLabel("Puntaje acumulado: " + score, SwingConstants.CENTER);
        scoreLabel.setFont(scoreLabel.getFont().deriveFont(Font.BOLD, 16f));
        content.add(scoreLabel, gbc);

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
        setMinimumSize(new Dimension(440, 260));
        setLocationRelativeTo(null);
    }

    private void goBack() {
        setVisible(false);
        dispose();
        onBack.run();
    }
}
