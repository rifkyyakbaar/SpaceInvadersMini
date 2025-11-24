import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class MenuPanel extends JPanel {

    private GameFrame parent;
    private JButton startBtn, settingsBtn, quitBtn;

    public MenuPanel(GameFrame parent) {
        this.parent = parent;
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);

        startBtn = new JButton("Start Game");
        settingsBtn = new JButton("Settings");
        quitBtn = new JButton("Quit");

        startBtn.addActionListener(this::onStart);
        settingsBtn.addActionListener(this::onSettings);
        quitBtn.addActionListener(e -> System.exit(0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.gridx = 0;

        gbc.gridy = 0; add(startBtn, gbc);
        gbc.gridy = 1; add(settingsBtn, gbc);
        gbc.gridy = 2; add(quitBtn, gbc);
    }

    private void onStart(ActionEvent e) {
        parent.startGame();
    }

    private void onSettings(ActionEvent e) {
        String shot = JOptionPane.showInputDialog(this,
                "Shot cooldown (ms, default 300):", "300");
        try {
            int v = Integer.parseInt(shot);
            parent.setPlayerCooldown(v);
        } catch (Exception ex) {
            // ignore invalid input
        }
    }
}

