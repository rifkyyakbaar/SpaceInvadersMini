package main;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private MenuPanel menu;
    private GamePanel gamePanel;

    public GameFrame() {
        setTitle("Space Invaders Mini");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        menu = new MenuPanel(this);
        add(menu);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void startGame() {
        getContentPane().removeAll();
        gamePanel = new GamePanel(this);
        add(gamePanel);
        pack();
        revalidate();
        repaint();
        gamePanel.requestFocusInWindow();
    }

    // allow menu to set player shoot cooldown
    public void setPlayerCooldown(int ms) {
        if (gamePanel != null) gamePanel.setPlayerShotCooldown(ms);
    }
}
