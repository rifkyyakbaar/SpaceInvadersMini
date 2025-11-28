import java.awt.*;
import javax.swing.*;

public class WinPanel extends JPanel {
    private Main mainApp;
    private int score;

    public WinPanel(Main mainApp, int score) {
        this.mainApp = mainApp;
        this.score = score;

        setLayout(null);
        setBackground(Color.BLACK); // Latar hitam

        // === TULISAN YOU WIN ===
        JLabel title = new JLabel("MISSION ACCOMPLISHED!");
        title.setFont(new Font("Poppins", Font.BOLD, 48));
        title.setForeground(Color.GREEN); // Warna Hijau Kemenangan
        title.setBounds(0, 100, 1280, 60);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        JLabel subTitle = new JLabel("YOU WIN");
        subTitle.setFont(new Font("Poppins", Font.BOLD, 36));
        subTitle.setForeground(Color.WHITE);
        subTitle.setBounds(0, 160, 1280, 50);
        subTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(subTitle);

        // === SKOR ===
        JLabel scoreLbl = new JLabel("Final Score: " + score);
        scoreLbl.setFont(new Font("Poppins", Font.PLAIN, 28));
        scoreLbl.setForeground(Color.YELLOW);
        scoreLbl.setBounds(0, 220, 1280, 40);
        scoreLbl.setHorizontalAlignment(SwingConstants.CENTER);
        add(scoreLbl);

        // === TOMBOL ===
        JButton btnMenu = new JButton("Main Menu");
        btnMenu.setFont(new Font("Poppins", Font.BOLD, 20));
        btnMenu.setBackground(new Color(218, 185, 80));
        btnMenu.setBounds(540, 350, 200, 50);
        btnMenu.addActionListener(e -> {
            mainApp.playButtonSound();
            mainApp.showMainMenu();
        });
        add(btnMenu);

        JButton btnQuit = new JButton("Quit Game");
        btnQuit.setFont(new Font("Poppins", Font.BOLD, 20));
        btnQuit.setBackground(Color.GRAY);
        btnQuit.setForeground(Color.WHITE);
        btnQuit.setBounds(540, 420, 200, 50);
        btnQuit.addActionListener(e -> System.exit(0));
        add(btnQuit);
    }
}