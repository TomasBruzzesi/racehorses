package iu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import dtos.HorseDTO;

/**
 * Panel que dibuja la pista con carriles, rivales y meta a cuadros.
 */
public class RaceTrackPanel extends JPanel {

    private static final Color PANEL_BG = new Color(235, 235, 235);
    private static final Color TRACK_BG = new Color(248, 248, 248);
    private static final Color LANE_ALT_BG = new Color(242, 242, 242);
    private static final Color TRACK_BORDER = new Color(160, 160, 160);
    private static final Color LANE_LINE = new Color(90, 90, 90);

    private static final Color PLAYER_HORSE_COLOR = new Color(139, 90, 43);
    private static final Color[] RIVAL_COLORS = {
            Color.BLACK,
            new Color(110, 110, 110),
            new Color(160, 70, 45)
    };

    private static final int LANE_COUNT = 4;
    private static final int LABEL_WIDTH = 108;
    private static final int FINISH_WIDTH = 26;
    private static final int SQUARE_SIZE = 13;
    private static final int HORSE_WIDTH = 38;
    private static final int KNIGHT_SIZE = 30;
    private static final float LABEL_FONT_SIZE = 12f;
    private static final float HORSE_NAME_FONT_SIZE = 10f;

    private final double trackDistance;
    private final String playerHorseName;
    private List<HorseDTO> horses = new ArrayList<>();

    public RaceTrackPanel(double trackDistance, String playerHorseName) {
        this.trackDistance = trackDistance;
        this.playerHorseName = playerHorseName;
        setBackground(PANEL_BG);
        setPreferredSize(new Dimension(580, 300));
    }

    public void updateHorses(List<HorseDTO> horses) {
        this.horses = orderForDisplay(horses);
        repaint();
    }

    private List<HorseDTO> orderForDisplay(List<HorseDTO> source) {
        List<HorseDTO> ordered = new ArrayList<>();
        if (source == null) {
            return ordered;
        }

        HorseDTO playerHorse = null;
        List<HorseDTO> rivals = new ArrayList<>();

        for (HorseDTO horse : source) {
            if (isPlayerHorse(horse)) {
                playerHorse = horse;
            } else {
                rivals.add(horse);
            }
        }

        if (playerHorse != null) {
            ordered.add(playerHorse);
        }
        ordered.addAll(rivals);
        return ordered;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int margin = 12;
        int trackX = margin + LABEL_WIDTH;
        int trackWidth = width - trackX - margin - FINISH_WIDTH;
        int trackTop = margin;
        int trackHeight = height - margin * 2;
        int laneHeight = trackHeight / LANE_COUNT;

        g2.setColor(TRACK_BORDER);
        g2.drawRect(trackX - 1, trackTop - 1, trackWidth + FINISH_WIDTH + 2, trackHeight + 2);

        for (int i = 0; i < LANE_COUNT; i++) {
            int laneTop = trackTop + i * laneHeight;
            g2.setColor(i % 2 == 0 ? TRACK_BG : LANE_ALT_BG);
            g2.fillRect(trackX, laneTop, trackWidth, laneHeight);
        }

        g2.setColor(LANE_LINE);
        for (int i = 1; i < LANE_COUNT; i++) {
            int y = trackTop + i * laneHeight;
            g2.drawLine(trackX, y, trackX + trackWidth, y);
        }

        drawCheckeredFinish(g2, trackX + trackWidth, trackTop, trackHeight);

        for (int i = 0; i < horses.size() && i < LANE_COUNT; i++) {
            HorseDTO horse = horses.get(i);
            int laneTop = trackTop + i * laneHeight;
            int centerY = laneTop + laneHeight / 2;
            boolean isPlayer = isPlayerHorse(horse);

            drawLaneLabel(g2, margin, centerY, laneLabel(i, isPlayer), horse.getName(), isPlayer);

            double progress = Math.min(1.0, horse.getDistanceTraveled() / trackDistance);
            int horseX = trackX + (int) (progress * (trackWidth - HORSE_WIDTH));

            Color horseColor = isPlayer
                    ? PLAYER_HORSE_COLOR
                    : RIVAL_COLORS[Math.min(playerHorseInList() ? i - 1 : i, RIVAL_COLORS.length - 1)];

            drawHorseIcon(g2, horseX, centerY, horseColor);
        }

        g2.dispose();
    }

    private void drawLaneLabel(Graphics2D g2, int x, int centerY, String role, String horseName, boolean bold) {
        Font roleFont = g2.getFont().deriveFont(bold ? Font.BOLD : Font.PLAIN, LABEL_FONT_SIZE);
        Font nameFont = g2.getFont().deriveFont(Font.PLAIN, HORSE_NAME_FONT_SIZE);

        FontMetrics roleMetrics = g2.getFontMetrics(roleFont);
        FontMetrics nameMetrics = g2.getFontMetrics(nameFont);
        int blockHeight = roleMetrics.getHeight() + nameMetrics.getHeight() - 2;
        int roleY = centerY - blockHeight / 2 + roleMetrics.getAscent();

        g2.setFont(roleFont);
        g2.setColor(new Color(45, 45, 45));
        g2.drawString(role, x, roleY);

        g2.setFont(nameFont);
        g2.setColor(new Color(95, 95, 95));
        g2.drawString(shortHorseName(horseName), x, roleY + roleMetrics.getHeight() - 2);
    }

    private String shortHorseName(String name) {
        if (name == null || name.isBlank()) {
            return "-";
        }
        return name.length() > 12 ? name.substring(0, 12) : name;
    }

    private boolean playerHorseInList() {
        for (HorseDTO horse : horses) {
            if (isPlayerHorse(horse)) {
                return true;
            }
        }
        return false;
    }

    private String laneLabel(int laneIndex, boolean isPlayer) {
        if (isPlayer) {
            return "Tu caballo";
        }
        int rivalNumber = playerHorseInList() ? laneIndex : laneIndex + 1;
        return "Rival " + rivalNumber;
    }

    private void drawCheckeredFinish(Graphics2D g2, int x, int top, int height) {
        int rows = height / SQUARE_SIZE;
        int finishHeight = rows * SQUARE_SIZE;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < 2; col++) {
                boolean white = (row + col) % 2 == 0;
                g2.setColor(white ? Color.WHITE : Color.BLACK);
                g2.fillRect(x + col * SQUARE_SIZE, top + row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }

        g2.setColor(TRACK_BORDER);
        g2.drawRect(x, top, FINISH_WIDTH, finishHeight);
    }

    /**
     * Dibuja el caballo de ajedrez mirando hacia la meta (derecha).
     */
    private void drawHorseIcon(Graphics2D g2, int x, int centerY, Color color) {
        if (drawChessKnightGlyph(g2, x, centerY, color)) {
            return;
        }
        drawKnightShape(g2, x, centerY, color);
    }

    private boolean drawChessKnightGlyph(Graphics2D g2, int x, int centerY, Color color) {
        char knightGlyph = '\u265E';
        Font[] fonts = {
                new Font("Segoe UI Symbol", Font.PLAIN, KNIGHT_SIZE),
                new Font("DejaVu Sans", Font.PLAIN, KNIGHT_SIZE),
                new Font("Noto Sans Symbols 2", Font.PLAIN, KNIGHT_SIZE),
                new Font("SansSerif", Font.PLAIN, KNIGHT_SIZE)
        };

        for (Font font : fonts) {
            if (!font.canDisplay(knightGlyph)) {
                continue;
            }

            AffineTransform original = g2.getTransform();
            g2.translate(x + KNIGHT_SIZE, centerY + KNIGHT_SIZE / 2.0 - 4);
            g2.scale(-1, 1);
            g2.setFont(font);
            g2.setColor(color);
            g2.drawString(String.valueOf(knightGlyph), 0, 0);
            g2.setTransform(original);
            return true;
        }
        return false;
    }

    private void drawKnightShape(Graphics2D g2, int x, int centerY, Color color) {
        GeneralPath knight = buildKnightPath();
        AffineTransform transform = new AffineTransform();
        transform.translate(x + KNIGHT_SIZE, centerY);
        transform.scale(-KNIGHT_SIZE / 100.0, KNIGHT_SIZE / 100.0);
        transform.translate(0, -50);

        GeneralPath scaled = new GeneralPath(knight);
        scaled.transform(transform);

        g2.setColor(color);
        g2.fill(scaled);
        g2.setColor(color.darker());
        g2.draw(scaled);
    }

    private GeneralPath buildKnightPath() {
        GeneralPath path = new GeneralPath();

        path.moveTo(8, 92);
        path.lineTo(92, 92);
        path.lineTo(92, 82);
        path.lineTo(78, 82);
        path.curveTo(74, 78, 70, 58, 52, 56);
        path.curveTo(38, 54, 30, 42, 34, 30);
        path.curveTo(36, 22, 44, 14, 56, 12);
        path.curveTo(66, 10, 76, 14, 82, 22);
        path.curveTo(88, 28, 90, 34, 86, 38);
        path.lineTo(78, 42);
        path.curveTo(68, 48, 54, 54, 40, 58);
        path.curveTo(26, 62, 14, 72, 8, 82);
        path.closePath();

        return path;
    }

    private boolean isPlayerHorse(HorseDTO horse) {
        return playerHorseName != null
                && horse.getName() != null
                && playerHorseName.equalsIgnoreCase(horse.getName());
    }
}
