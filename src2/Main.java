import java.awt.*;
import javax.swing.*;

public class Main extends JFrame {

    // ===========================================
    // Selected Vehicle (hanya 1 deklarasi!)
    // ===========================================
    private String selectedVehicle = "Tank_Default";

    public String getSelectedVehicle() {
        return selectedVehicle;
    }

    public void setSelectedVehicle(String v) {
        selectedVehicle = v;
    }

    // ===========================================
    // Constructor (Fullscreen Borderless)
    // ===========================================
    public Main() {

        setTitle("TANK-TOP");

        // =============== FULLSCREEN BORDERLESS ===============
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setUndecorated(true);         // hilangkan border window
        setSize(screen.width, screen.height);
        setLocation(0, 0);            // pastikan di pojok kiri atas
        setExtendedState(JFrame.MAXIMIZED_BOTH); // pastikan fill screen

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        showLoginPanel(); // Start aplikasi
        setVisible(true);

    // ===========================================
    // Tambahkan ESC untuk keluar aplikasi
    // ===========================================
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("ESCAPE"), "EXIT_APP");

        getRootPane().getActionMap().put("EXIT_APP", new AbstractAction() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Apakah Anda yakin ingin keluar?",
                    "Keluar Game",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
        });
    }

    // ===========================================
    // PANEL NAVIGATION
    // ===========================================
    public void showLoginPanel() {
        setContentPane(new LoginPanel(this));
        revalidate();
        repaint();
    }

    public void showMainMenu() {
        setContentPane(new MainMenuPanel(this));
        revalidate();
        repaint();
    }

    public void showGamePanel() {
        setContentPane(new GamePanel(this));
        revalidate();
        repaint();
    }

    public void showVehicleSelect() {
        setContentPane(new VehicleSelectPanel(this));
        revalidate();
        repaint();
    }

    public void showLeaderboard() {
        setContentPane(new LeaderboardPanel(this));
        revalidate();
        repaint();
    }

    public void showGameOver(int score) {
        setContentPane(new GameOverPanel(this, score));
        revalidate();
        repaint();
    }

    // ===========================================
    // MAIN ENTRY POINT
    // ===========================================
    public static void main(String[] args) {
        new Main();
    }
}
