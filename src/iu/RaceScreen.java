package iu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import controllers.RaceSystemController;
import dtos.PlayerDTO;
import dtos.RaceResultDTO;

/**
 * Pantalla de carrera en vivo. Solo utiliza RaceSystemController.
 */
public class RaceScreen extends JFrame {

    private static final int TICK_DELAY_MS = 80;

    private final RaceSystemController controller;
    private final PlayerDTO player;
    private final RaceFinishListener listener;
    private final RaceTrackPanel trackPanel;
    private final JLabel statusLabel;
    private Timer raceTimer;

    public RaceScreen(RaceSystemController controller, PlayerDTO player, RaceFinishListener listener) {
        this.controller = controller;
        this.player = player;
        this.listener = listener;
        this.trackPanel = new RaceTrackPanel(
                controller.getRaceTrackDistance(),
                player.getSelectedHorseName()
        );
        this.statusLabel = new JLabel("¡La carrera comenzó!", SwingConstants.CENTER);
        buildUi();
        startRace();
    }

    private void buildUi() {
        setTitle("Carrera de Caballos - En curso");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBackground(new Color(235, 235, 235));
        root.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 28));

        JLabel title = new JLabel("Carrera en vivo", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 17f));
        title.setForeground(new Color(35, 35, 35));
        root.add(title, BorderLayout.NORTH);

        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.PLAIN, 12f));
        statusLabel.setForeground(new Color(80, 80, 80));
        JPanel center = new JPanel(new BorderLayout(0, 10));
        center.setOpaque(false);
        center.add(statusLabel, BorderLayout.NORTH);
        center.add(trackPanel, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);

        setContentPane(root);
        pack();
        setMinimumSize(new Dimension(600, 420));
        setLocationRelativeTo(null);
    }

    private void startRace() {
        controller.launchRace();
        trackPanel.updateHorses(controller.tickRace());

        raceTimer = new Timer(TICK_DELAY_MS, e -> advanceRace());
        raceTimer.start();
    }

    private void advanceRace() {
        if (controller.isRaceFinished()) {
            raceTimer.stop();
            statusLabel.setText("Carrera finalizada! Todos los caballos cruzaron la meta.");
            trackPanel.updateHorses(controller.tickRace());
            finishRace();
            return;
        }

        trackPanel.updateHorses(controller.tickRace());
    }

    private void finishRace() {
        RaceResultDTO result = controller.finishRace();
        if (result == null) {
            return;
        }

        int totalScore = controller.getPlayerScore();
        player.setScore(totalScore);

        setVisible(false);
        dispose();
        listener.onRaceFinished(result, totalScore);
    }

    public interface RaceFinishListener {
        void onRaceFinished(RaceResultDTO result, int totalScore);
    }
}
