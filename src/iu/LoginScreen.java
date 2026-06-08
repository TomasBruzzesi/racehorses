package iu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import controllers.RaceSystemController;
import dtos.PlayerDTO;

/**
 * Pantalla 1: ingreso por e-mail con un solo boton Comenzar.
 */
public class LoginScreen extends JFrame {

    private final RaceSystemController controller;
    private final LoginListener listener;
    private final JTextField emailField;

    public LoginScreen(RaceSystemController controller, LoginListener listener) {
        this.controller = controller;
        this.listener = listener;
        this.emailField = new JTextField(28);
        buildUi();
    }

    private void buildUi() {
        setTitle("Carrera de Caballos - Ingreso");
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

        JLabel title = new JLabel("Bienvenido a Carrera de Caballos", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        content.add(title, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 16, 0);
        content.add(new JSeparator(), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 12, 0);
        JLabel subtitle = new JLabel("Ingresá tu e-mail para jugar o registrarte:", SwingConstants.CENTER);
        content.add(subtitle, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 20, 0);
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        emailPanel.setOpaque(false);
        JLabel emailLabel = new JLabel("E-mail:");
        emailLabel.setFont(emailLabel.getFont().deriveFont(Font.BOLD));
        emailPanel.add(emailLabel);
        emailPanel.add(emailField);
        content.add(emailPanel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        JButton startButton = new JButton("Comenzar");
        startButton.addActionListener(e -> handleStart());
        content.add(startButton, gbc);

        root.add(content, BorderLayout.CENTER);
        setContentPane(root);
        pack();
        setMinimumSize(new Dimension(580, 260));
        setLocationRelativeTo(null);
    }

    private void handleStart() {
        String email = emailField.getText().trim();
        if (email.isBlank()) {
            showWarning("Ingresá un e-mail.");
            return;
        }

        PlayerDTO dto = new PlayerDTO();
        dto.setEmail(email);

        PlayerDTO player = controller.registerPlayer(dto);
        if (player != null) {
            goToMainMenu(player);
            return;
        }

        String name = JOptionPane.showInputDialog(
                this,
                "E-mail no registrado. Ingresá tu nombre:",
                "Registro",
                JOptionPane.PLAIN_MESSAGE
        );

        if (name == null) {
            return;
        }

        name = name.trim();
        if (name.isBlank()) {
            showWarning("El nombre no puede estar vacío.");
            return;
        }

        dto.setName(name);
        player = controller.registerPlayer(dto);
        if (player != null) {
            goToMainMenu(player);
        }
    }

    private void goToMainMenu(PlayerDTO player) {
        setVisible(false);
        dispose();
        listener.onLoginSuccess(player);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public interface LoginListener {
        void onLoginSuccess(PlayerDTO player);
    }
}
