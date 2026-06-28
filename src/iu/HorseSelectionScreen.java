package iu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import controllers.HorseController;
import controllers.PlayerController;
import dtos.HorseDTO;
import dtos.PlayerDTO;

/**
 * Pantalla de seleccion de caballo antes de iniciar una carrera.
 */
public class HorseSelectionScreen extends JFrame {

    private final PlayerDTO player;
    private final Runnable onBack;
    private final Runnable onHorseSelected;
    private final List<HorseDTO> horses;
    private final JList<String> horseList;

    public HorseSelectionScreen(
            PlayerDTO player,
            Runnable onBack,
            Runnable onHorseSelected
    ) {
        this.player = player;
        this.onBack = onBack;
        this.onHorseSelected = onHorseSelected;
        this.horses = HorseController.getInstance().getHorseDTOs();
        this.horseList = new JList<>(buildListModel());
        buildUi();
    }

    private DefaultListModel<String> buildListModel() {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (HorseDTO horse : horses) {
            model.addElement(formatHorse(horse));
        }
        return model;
    }

    private String formatHorse(HorseDTO horse) {
        return horse.getName()
                + "  |  Vel: " + horse.getBaseSpeed()
                + "  |  Res: " + horse.getStamina();
    }

    private void buildUi() {
        setTitle("Carrera de Caballos - Elegir caballo");
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

        JLabel title = new JLabel("Elegí tu caballo", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        content.add(title, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 8, 0);
        JLabel subtitle = new JLabel("Jugador: " + player.getName(), SwingConstants.CENTER);
        content.add(subtitle, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 16, 0);
        content.add(new JSeparator(), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 16, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;

        horseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (!horses.isEmpty()) {
            horseList.setSelectedIndex(0);
        }

        JScrollPane scrollPane = new JScrollPane(horseList);
        scrollPane.setPreferredSize(new Dimension(400, 160));
        content.add(scrollPane, gbc);

        gbc.gridy++;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttons.setOpaque(false);

        JButton selectButton = new JButton("Seleccionar");
        selectButton.addActionListener(e -> handleSelect());
        buttons.add(selectButton);

        JButton backButton = new JButton("Volver");
        backButton.addActionListener(e -> goBack());
        buttons.add(backButton);

        content.add(buttons, gbc);

        root.add(content, BorderLayout.CENTER);
        setContentPane(root);
        pack();
        setMinimumSize(new Dimension(480, 360));
        setLocationRelativeTo(null);
    }

    private void handleSelect() {
        int index = horseList.getSelectedIndex();
        if (index < 0 || index >= horses.size()) {
            showWarning("Seleccioná un caballo de la lista.");
            return;
        }

        HorseDTO selectedHorse = horses.get(index);
        PlayerController.getInstance().selectHorse(selectedHorse.getName());
        player.setSelectedHorseName(selectedHorse.getName());

        setVisible(false);
        dispose();
        onHorseSelected.run();
    }

    private void goBack() {
        setVisible(false);
        dispose();
        onBack.run();
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}
