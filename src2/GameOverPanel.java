import java.awt.*;
import javax.swing.*;

public class GameOverPanel extends JPanel {

    private Main mainApp;
    private int score;

    public GameOverPanel(Main mainApp, int score) {
        this.mainApp = mainApp;
        this.score = score;

        setLayout(null);
        setBackground(Theme.DARK_BG);

        JLabel title = new JLabel("GAME OVER");
        title.setFont(Theme.FONT_BOLD_48);
        title.setForeground(Color.WHITE);
        title.setBounds(470, 100, 400, 60);
        add(title);

        JLabel scoreLbl = new JLabel("Score: " + score);
        scoreLbl.setFont(Theme.FONT_BOLD_32);
        scoreLbl.setForeground(Color.WHITE);
        scoreLbl.setBounds(550, 180, 300, 40);
        add(scoreLbl);

        JButton retryBtn = new JButton("Retry");
        retryBtn.setFont(Theme.FONT_BOLD_24);
        retryBtn.setBounds(520, 280, 250, 45);
        retryBtn.setBackground(Theme.GOLD);
        add(retryBtn);

        JButton menuBtn = new JButton("Main Menu");
        menuBtn.setFont(Theme.FONT_BOLD_24);
        menuBtn.setBounds(520, 350, 250, 45);
        menuBtn.setBackground(Theme.GOLD);
        add(menuBtn);

        JButton quitBtn = new JButton("Quit Game");
        quitBtn.setFont(Theme.FONT_BOLD_24);
        quitBtn.setBounds(520, 420, 250, 45);
        quitBtn.setBackground(Color.GRAY);
        add(quitBtn);

        retryBtn.addActionListener(e -> mainApp.showGamePanel());
        menuBtn.addActionListener(e -> mainApp.showMainMenu());
        quitBtn.addActionListener(e -> System.exit(0));
    }
}
