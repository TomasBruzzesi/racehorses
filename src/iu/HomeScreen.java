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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import dtos.PlayerDTO;

/**
 * Pantalla de inicio: menu principal del jugador logueado.
 */
public class HomeScreen extends JFrame {

    private final PlayerDTO player;
    private final HomeListener listener;

    public HomeScreen(PlayerDTO player, HomeListener listener) {
        this.player = player;
        this.listener = listener;
        buildUi();
    }

    private void buildUi() {
        setTitle("Carrera de Caballos - Inicio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        JLabel title = new JLabel("Menú principal", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        content.add(title, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 8, 0);
        JLabel welcome = new JLabel("Hola, " + player.getName(), SwingConstants.CENTER);
        content.add(welcome, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 16, 0);
        content.add(new JSeparator(), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.fill = GridBagConstraints.NONE;
        content.add(createMenuButton("Ver puntaje", e -> listener.onViewScore(player)), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 10, 0);
        content.add(createMenuButton("Iniciar una carrera", e -> startRace()), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        content.add(createMenuButton("Salir", e -> exitApp()), gbc);

        root.add(content, BorderLayout.CENTER);
        setContentPane(root);
        pack();
        setMinimumSize(new Dimension(480, 320));
        setLocationRelativeTo(null);
    }

    private JButton createMenuButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(220, 32));
        button.addActionListener(action);
        return button;
    }

    private void startRace() {
        listener.onStartRace(player);
    }

    private void exitApp() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "¿Salir de la aplicación?",
                "Salir",
                JOptionPane.YES_NO_OPTION
        );
        if (option == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    public interface HomeListener {
        void onViewScore(PlayerDTO player);

        void onStartRace(PlayerDTO player);
    }
}
