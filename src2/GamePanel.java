import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private Main mainApp;

    private Timer timer;
    private Random rand = new Random();

    // === Tank player ===
    private int tankX = 600;
    private int tankY = 600;
    private int tankSpeed = 10;
    private int hp = 100;

    private boolean moveLeft = false;
    private boolean moveRight = false;

    // === Peluru ===
    private ArrayList<Rectangle> bullets = new ArrayList<>();
    private int bulletSpeed = 12;

    // === Musuh ===
    private ArrayList<Rectangle> enemies = new ArrayList<>();
    private int enemySpeed = 4;
    private int spawnRate = 60; // frame

    // === Item ===
    private ArrayList<Rectangle> hpItems = new ArrayList<>();
    private ArrayList<Rectangle> powerItems = new ArrayList<>();
    private int itemSpeed = 3;

    // === Level system ===
    private int level = 1;
    private int score = 0;
    private int frameCount = 0;

    // === Boss ===
    private Rectangle boss;
    private int bossHp = 150;
    private boolean bossActive = false;

    // === Back button ===
    private JButton btnBack;

    public GamePanel(Main mainApp) {
        this.mainApp = mainApp;

        setLayout(null);
        setFocusable(true);
        addKeyListener(this);

        btnBack = new JButton("Back");
        btnBack.setBounds(20, 20, 120, 40);
        btnBack.addActionListener(e -> mainApp.showMainMenu());
        add(btnBack);

        timer = new Timer(16, this); // 60 FPS
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        frameCount++;

        // === Movement ===
        if (moveLeft && tankX > 20) tankX -= tankSpeed;
        if (moveRight && tankX < 1150) tankX += tankSpeed;

        // === Spawn Musuh ===
        if (!bossActive && frameCount % spawnRate == 0) {
            enemies.add(new Rectangle(rand.nextInt(1200), -40, 40, 40));
        }

        // === Spawn Item ===
        if (frameCount % 500 == 0) {
            hpItems.add(new Rectangle(rand.nextInt(1200), -40, 30, 30));
        }
        if (frameCount % 700 == 0) {
            powerItems.add(new Rectangle(rand.nextInt(1200), -40, 30, 30));
        }

        // === Update Peluru ===
        for (int i = 0; i < bullets.size(); i++) {
            Rectangle b = bullets.get(i);
            b.y -= bulletSpeed;
            if (b.y < -20) bullets.remove(i);
        }

        // === Update Musuh ===
        for (int i = 0; i < enemies.size(); i++) {
            Rectangle en = enemies.get(i);
            en.y += enemySpeed;

            // Kena tank
            if (en.intersects(new Rectangle(tankX, tankY, 80, 80))) {
                hp -= 20;
                enemies.remove(i);
                if (hp <= 0) gameOver();
            }

            // Keluar layar
            if (en.y > 720) enemies.remove(i);
        }

        // === Collision Peluru → Musuh ===
        for (int i = 0; i < bullets.size(); i++) {
            Rectangle b = bullets.get(i);
            for (int j = 0; j < enemies.size(); j++) {
                Rectangle en = enemies.get(j);

                if (b.intersects(en)) {
                    bullets.remove(i);
                    enemies.remove(j);
                    score += 10;
                    break;
                }
            }
        }

        // === Item HP ===
        for (int i = 0; i < hpItems.size(); i++) {
            Rectangle it = hpItems.get(i);
            it.y += itemSpeed;

            if (it.intersects(new Rectangle(tankX, tankY, 80, 80))) {
                hp = Math.min(100, hp + 30);
                hpItems.remove(i);
            }
        }

        // === Item Power ===
        for (int i = 0; i < powerItems.size(); i++) {
            Rectangle it = powerItems.get(i);
            it.y += itemSpeed;

            if (it.intersects(new Rectangle(tankX, tankY, 80, 80))) {
                bulletSpeed = 20;
                powerItems.remove(i);
            }
        }

        // === Level Up ===
        if (score > 100 && level == 1) {
            level = 2;
            enemySpeed = 6;
        }
        if (score > 250 && level == 2) {
            level = 3;
            enemySpeed = 7;
            startBoss();
        }

        // === Boss Logic ===
        if (bossActive && boss != null) {
            boss.y += 2;

            if (boss.y > 150) boss.y = 150;

            for (int i = 0; i < bullets.size(); i++) {
                if (bullets.get(i).intersects(boss)) {
                    bullets.remove(i);
                    bossHp -= 5;
                    if (bossHp <= 0) {
                        score += 200;
                        bossActive = false;
                        boss = null;
                        winGame();
                    }
                }
            }
        }

        repaint();
    }

    private void startBoss() {
        bossActive = true;
        bossHp = 150;
        boss = new Rectangle(400, -200, 400, 150);
    }

    private void gameOver() {
        timer.stop();
        mainApp.showGameOver(score);
    }

    private void winGame() {
        timer.stop();
        mainApp.showGameOver(score); // Bisa kamu ganti ke panel "You Win"
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // === Tank ===
        g.setColor(Color.GREEN);
        g.fillRect(tankX, tankY, 80, 80);

        // === Bullets ===
        g.setColor(Color.YELLOW);
        for (Rectangle b : bullets) {
            g.fillRect(b.x, b.y, b.width, b.height);
        }

        // === Enemies ===
        g.setColor(Color.RED);
        for (Rectangle en : enemies) {
            g.fillRect(en.x, en.y, en.width, en.height);
        }

        // === Item HP ===
        g.setColor(Color.PINK);
        for (Rectangle it : hpItems) {
            g.fillRect(it.x, it.y, it.width, it.height);
        }

        // === Item Power ===
        g.setColor(Color.CYAN);
        for (Rectangle it : powerItems) {
            g.fillRect(it.x, it.y, it.width, it.height);
        }

        // === Boss ===
        if (bossActive && boss != null) {
            g.setColor(Color.MAGENTA);
            g.fillRect(boss.x, boss.y, boss.width, boss.height);
        }

        // === HUD ===
        g.setColor(Color.WHITE);
        g.setFont(new Font("Poppins", Font.BOLD, 22));
        g.drawString("HP: " + hp, 20, 100);
        g.drawString("Score: " + score, 20, 130);
        g.drawString("Level: " + level, 20, 160);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) moveLeft = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) moveRight = true;

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            bullets.add(new Rectangle(tankX + 35, tankY - 10, 10, 20));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) moveLeft = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) moveRight = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
