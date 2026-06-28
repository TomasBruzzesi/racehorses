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

import controllers.PlayerController;
import controllers.RaceController;
import dtos.PlayerDTO;
import dtos.RaceResultDTO;

/**
 * Pantalla de carrera en vivo.
 */
public class RaceScreen extends JFrame {

    private static final int TICK_DELAY_MS = 80;

    private final PlayerDTO player;
    private final RaceFinishListener listener;
    private final RaceController raceController;
    private final RaceTrackPanel trackPanel;
    private final JLabel statusLabel;
    private Timer raceTimer;

    public RaceScreen(PlayerDTO player, RaceFinishListener listener) {
        this.player = player;
        this.listener = listener;
        this.raceController = RaceController.getInstance();
        this.trackPanel = new RaceTrackPanel(
                raceController.getTrackDistance(),
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
        raceController.launchRace();
        trackPanel.updateHorses(raceController.tick());

        raceTimer = new Timer(TICK_DELAY_MS, e -> advanceRace());
        raceTimer.start();
    }

    private void advanceRace() {
        if (raceController.isFinished()) {
            raceTimer.stop();
            statusLabel.setText("Carrera finalizada! Todos los caballos cruzaron la meta.");
            trackPanel.updateHorses(raceController.tick());
            finishRace();
            return;
        }

        trackPanel.updateHorses(raceController.tick());
    }

    private void finishRace() {
        RaceResultDTO result = raceController.finishRace();
        if (result == null) {
            return;
        }

        int totalScore = PlayerController.getInstance().getPlayerScore();
        player.setScore(totalScore);

        setVisible(false);
        dispose();
        listener.onRaceFinished(result, totalScore);
    }

    public interface RaceFinishListener {
        void onRaceFinished(RaceResultDTO result, int totalScore);
    }
}
