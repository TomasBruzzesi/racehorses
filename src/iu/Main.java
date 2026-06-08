package iu;

import javax.swing.SwingUtilities;

import controllers.RaceSystemController;
import dtos.PlayerDTO;
import dtos.RaceResultDTO;

/**
 * Punto de entrada. La UI solo opera a traves de RaceSystemController.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::startApp);
    }

    private static RaceSystemController controller;

    private static void startApp() {
        controller = new RaceSystemController();
        controller.initSystem();

        LoginScreen loginScreen = new LoginScreen(controller, Main::openHomeScreen);
        loginScreen.setVisible(true);
    }

    private static void openHomeScreen(PlayerDTO player) {
        HomeScreen[] homeRef = new HomeScreen[1];
        homeRef[0] = new HomeScreen(player, new HomeScreen.HomeListener() {
            @Override
            public void onViewScore(PlayerDTO p) {
                homeRef[0].setVisible(false);
                openPlayerScoreScreen(p, () -> homeRef[0].setVisible(true));
            }

            @Override
            public void onStartRace(PlayerDTO p) {
                homeRef[0].setVisible(false);
                openHorseSelectionScreen(p, () -> homeRef[0].setVisible(true));
            }
        });
        homeRef[0].setVisible(true);
    }

    private static void openPlayerScoreScreen(PlayerDTO player, Runnable onBack) {
        PlayerScoreScreen scoreScreen = new PlayerScoreScreen(controller, player, onBack);
        scoreScreen.setVisible(true);
    }

    private static void openHorseSelectionScreen(PlayerDTO player, Runnable onBack) {
        HorseSelectionScreen screen = new HorseSelectionScreen(
                controller,
                player,
                onBack,
                () -> openRaceScreen(player, onBack)
        );
        screen.setVisible(true);
    }

    private static void openRaceScreen(PlayerDTO player, Runnable onBackToMenu) {
        RaceScreen raceScreen = new RaceScreen(controller, player, (result, totalScore) -> {
            openRaceResultScreen(player, result, totalScore, onBackToMenu);
        });
        raceScreen.setVisible(true);
    }

    private static void openRaceResultScreen(
            PlayerDTO player,
            RaceResultDTO result,
            int totalScore,
            Runnable onBackToMenu
    ) {
        RaceResultScreen resultScreen = new RaceResultScreen(player, result, totalScore, onBackToMenu);
        resultScreen.setVisible(true);
    }
}
