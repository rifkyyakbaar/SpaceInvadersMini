import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private Main mainApp;
    private KoneksiDatabase db = new KoneksiDatabase(); 

    // === PLAYER EFEK SUARA ===
    private SoundPlayer sfxPlayer = new SoundPlayer(); 

    private Timer timer;
    private Random rand = new Random();

    // === Tank player ===
    private int tankX = 600;
    private int tankY = 600;
    private int tankSpeed = 10;
    private int hp = 100;

    private boolean moveLeft = false;
    private boolean moveRight = false;

    // === GAMBAR ASET ===
    private Image pHull, pGun, pTrack;
    private Image eHull, eGun, eTrack;
    private Image bHull, bGun, bTrack;
    private Image expImg1, expImg2;
    private Image bulletImg1, bulletImg2, bossBulletImg; 

    // === Peluru ===
    private ArrayList<Rectangle> bullets = new ArrayList<>();
    private int bulletSpeed = 12;
    private ArrayList<Rectangle> bossBullets = new ArrayList<>(); 
    private int bossBulletSpeed = 8; 
    private int bossXSpeed = 3; 

    // === List Ledakan ===
    private ArrayList<Explosion> explosions = new ArrayList<>();

    // === Musuh & Item ===
    private ArrayList<Rectangle> enemies = new ArrayList<>();
    private int enemySpeed = 2; 
    private int spawnRate = 60; 
    private ArrayList<Rectangle> hpItems = new ArrayList<>();
    private ArrayList<Rectangle> powerItems = new ArrayList<>();
    private int itemSpeed = 3;

    // === Level & Boss ===
    private int level = 1;
    private int score = 0;
    private int frameCount = 0;
    private Rectangle boss;
    private int bossHp = 200;
    private int maxBossHp = 200; 
    private boolean bossActive = false;

    // === Back button ===
    private JButton btnBack;

    public GamePanel(Main mainApp) {
        this.mainApp = mainApp;
        db.initialize(); 

        // === LOAD SEMUA GAMBAR ===
        bulletImg1 = new ImageIcon("assets/Plasma.png").getImage();
        bulletImg2 = new ImageIcon("assets/Flame_D.png").getImage();
        bossBulletImg = new ImageIcon("assets/Laser.png").getImage();

        pHull  = new ImageIcon("assets/Hull_01.png").getImage();
        pGun   = new ImageIcon("assets/Gun_01.png").getImage();
        pTrack = new ImageIcon("assets/Track_01.png").getImage();

        eHull  = new ImageIcon("assets/Hull_02.png").getImage();
        eGun   = new ImageIcon("assets/Gun_02.png").getImage();
        eTrack = new ImageIcon("assets/Track_02.png").getImage();

        bHull  = new ImageIcon("assets/Hull_03.png").getImage();
        bGun   = new ImageIcon("assets/Gun_03.png").getImage();
        bTrack = new ImageIcon("assets/Track_03.png").getImage();

        expImg1 = new ImageIcon("assets/Explosion_01.png").getImage();
        expImg2 = new ImageIcon("assets/Explosion_02.png").getImage();

        setLayout(null);
        setFocusable(true);
        addKeyListener(this);

        btnBack = new JButton("Back");
        btnBack.setBounds(20, 20, 120, 40);
        btnBack.setFocusable(false); 
        btnBack.addActionListener(e -> {
            mainApp.playButtonSound();
            mainApp.showMainMenu();
        });
        add(btnBack);

        timer = new Timer(16, this); 
        timer.start();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frameCount++;

        // === Movement Player ===
        if (moveLeft && tankX > 20) tankX -= tankSpeed;
        if (moveRight && tankX < 1150) tankX += tankSpeed;

        // === Spawn Musuh Kecil ===
        if (!bossActive && frameCount % spawnRate == 0) {
            enemies.add(new Rectangle(rand.nextInt(1200), -40, 40, 40));
        }

        // === Spawn Item ===
        if (frameCount % 500 == 0) hpItems.add(new Rectangle(rand.nextInt(1200), -40, 30, 30));
        if (frameCount % 700 == 0) powerItems.add(new Rectangle(rand.nextInt(1200), -40, 30, 30));

        // === Update Ledakan ===
        for (int i = explosions.size() - 1; i >= 0; i--) {
            Explosion exp = explosions.get(i);
            exp.update(); 
            if (!exp.isAlive()) explosions.remove(i); 
        }

        // === Update Peluru Player ===
        for (int i = 0; i < bullets.size(); i++) {
            Rectangle b = bullets.get(i);
            b.y -= bulletSpeed;
            if (b.y < -20) bullets.remove(i);
        }

        // === Update Peluru Boss ===
        for (int i = 0; i < bossBullets.size(); i++) {
            Rectangle bb = bossBullets.get(i);
            bb.y += bossBulletSpeed; 

            // Boss Nembak Player
            if (bb.intersects(new Rectangle(tankX, tankY, 80, 80))) {
                hp -= 15; 
                sfxPlayer.playSFX("assets/tabraksound.wav"); 
                explosions.add(new Explosion(tankX, tankY, expImg2));
                bossBullets.remove(i);
                if (hp <= 0) gameOver();
            }
            else if (bb.y > 720) {
                bossBullets.remove(i);
            }
        }

        // === Update Musuh Kecil ===
        for (int i = 0; i < enemies.size(); i++) {
            Rectangle en = enemies.get(i);
            en.y += enemySpeed;

            // Musuh Nabrak Player
            if (en.intersects(new Rectangle(tankX, tankY, 80, 80))) {
                hp -= 10;
                sfxPlayer.playSFX("assets/tabraksound.wav");
                explosions.add(new Explosion(tankX, tankY, expImg2));
                enemies.remove(i);
                if (hp <= 0) gameOver();
            } else if (en.y > 720) {   
                score = Math.max(0, score - 5); 
                enemies.remove(i);   
            }
        }

        // === Collision Peluru Player -> Musuh ===
        for (int i = 0; i < bullets.size(); i++) {
            Rectangle b = bullets.get(i);
            for (int j = 0; j < enemies.size(); j++) {
                Rectangle en = enemies.get(j);
                if (b.intersects(en)) {
                    sfxPlayer.playSFX("assets/tembaksound.wav");
                    explosions.add(new Explosion(en.x, en.y, expImg1));
                    bullets.remove(i);
                    enemies.remove(j);
                    score += 10;
                    break;
                }
            }
        }

        // ===============================================
        // === LOGIKA ITEM HP (PINK) -> Tambah +15 HP ===
        // ===============================================
        for (int i = 0; i < hpItems.size(); i++) {
            Rectangle it = hpItems.get(i);
            it.y += itemSpeed;
            if (it.intersects(new Rectangle(tankX, tankY, 80, 80))) {
                
                sfxPlayer.playSFX("assets/itemsound.wav"); 

                hp = Math.min(100, hp + 15); // TAMBAH 15 HP
                hpItems.remove(i);
            }
        }

        // ==================================================
        // === LOGIKA ITEM POWER (BIRU) -> Tambah +10 Score ===
        // ==================================================
        for (int i = 0; i < powerItems.size(); i++) {
            Rectangle it = powerItems.get(i);
            it.y += itemSpeed;
            if (it.intersects(new Rectangle(tankX, tankY, 80, 80))) {
                
                sfxPlayer.playSFX("assets/itemsound.wav");

                score += 10; // TAMBAH 10 SCORE
                powerItems.remove(i);
            }
        }

        // === Level Up ===
        if (score > 100 && level == 1) {
            level = 2;
            enemySpeed = 4;
        }
        if (score > 250 && level == 2) {
            level = 3;
            enemySpeed = 5;
            startBoss();
        }

        // === Boss Logic ===
        if (bossActive && boss != null) {
            if (boss.y < 50) boss.y += 2; 
            
            boss.x += bossXSpeed;
            if (boss.x >= 1280 - boss.width || boss.x <= 0) {
                bossXSpeed = -bossXSpeed; 
            }

            if (frameCount % 50 == 0) {
                bossBullets.add(new Rectangle(boss.x + (boss.width / 2) - 15, boss.y + 100, 30, 60));
            }

            // Player Nembak Boss
            for (int i = 0; i < bullets.size(); i++) {
                if (bullets.get(i).intersects(boss)) {
                    sfxPlayer.playSFX("assets/tembaksound.wav");
                    explosions.add(new Explosion(bullets.get(i).x - 20, bullets.get(i).y, expImg1));
                    bullets.remove(i);
                    bossHp -= 5;
                    if (bossHp <= 0) {
                        score += 500;
                        explosions.add(new Explosion(boss.x, boss.y, expImg1));
                        bossActive = false;
                        boss = null;
                        bossBullets.clear(); 
                        winGame();
                    }
                }
            }
        }

        repaint();
    }

    private void startBoss() {
        bossActive = true;
        maxBossHp = 300; 
        bossHp = maxBossHp; 
        boss = new Rectangle(400, -200, 400, 200); 
    }

    private void gameOver() {
        timer.stop();
        if (mainApp.getCurrentUserId() != -1) {
            db.saveScore(mainApp.getCurrentUserId(), level, score); 
        }
        mainApp.showGameOver(score);
    }

    private void winGame() {
        timer.stop();
        if (mainApp.getCurrentUserId() != -1) {
            db.saveScore(mainApp.getCurrentUserId(), level, score);
        }
        mainApp.showWinPanel(score);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // 1. TANK PLAYER
        if (pTrack != null) g2d.drawImage(pTrack, tankX, tankY, 80, 80, null);
        if (pHull != null) g2d.drawImage(pHull, tankX, tankY, 80, 80, null);
        else { g2d.setColor(Color.GREEN); g2d.fillRect(tankX, tankY, 80, 80); }
        if (pGun != null) g2d.drawImage(pGun, tankX, tankY, 80, 80, null);

        // 2. MUSUH
        for (Rectangle en : enemies) {
            AffineTransform old = g2d.getTransform();
            g2d.rotate(Math.toRadians(180), en.x + en.width/2, en.y + en.height/2);

            if (eTrack != null) g2d.drawImage(eTrack, en.x, en.y, en.width, en.height, null);
            if (eHull != null) g2d.drawImage(eHull, en.x, en.y, en.width, en.height, null);
            else { g2d.setColor(Color.RED); g2d.fillRect(en.x, en.y, en.width, en.height); }
            if (eGun != null) g2d.drawImage(eGun, en.x, en.y, en.width, en.height, null);

            g2d.setTransform(old);
        }

        // 3. BOSS
        if (bossActive && boss != null) {
            AffineTransform old = g2d.getTransform();
            g2d.rotate(Math.toRadians(180), boss.x + boss.width/2, boss.y + boss.height/2);

            if (bTrack != null) g2d.drawImage(bTrack, boss.x, boss.y, boss.width, boss.height, null);
            if (bHull != null) g2d.drawImage(bHull, boss.x, boss.y, boss.width, boss.height, null);
            else { g2d.setColor(Color.MAGENTA); g2d.fillRect(boss.x, boss.y, boss.width, boss.height); }
            if (bGun != null) g2d.drawImage(bGun, boss.x, boss.y, boss.width, boss.height, null);

            g2d.setTransform(old);

            // HP Bar
            g2d.setColor(Color.RED);
            g2d.fillRect(boss.x, boss.y - 30, boss.width, 20);
            g2d.setColor(Color.GREEN);
            int barWidth = (int) (((double) bossHp / maxBossHp) * boss.width);
            g2d.fillRect(boss.x, boss.y - 30, barWidth, 20);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            String hpText = "BOSS HP: " + bossHp + " / " + maxBossHp;
            int textWidth = g2d.getFontMetrics().stringWidth(hpText);
            g2d.drawString(hpText, boss.x + (boss.width - textWidth) / 2, boss.y - 15);
        }

        // 4. PELURU
        for (Rectangle b : bullets) {
            if (level == 1 && bulletImg1 != null) g2d.drawImage(bulletImg1, b.x, b.y, b.width, b.height, null);
            else if (level >= 2 && bulletImg2 != null) g2d.drawImage(bulletImg2, b.x, b.y, b.width, b.height, null);
            else { g2d.setColor(Color.YELLOW); g2d.fillRect(b.x, b.y, b.width, b.height); }
        }
        for (Rectangle bb : bossBullets) {
            if (bossBulletImg != null) g2d.drawImage(bossBulletImg, bb.x, bb.y, bb.width, bb.height, null);
            else { g2d.setColor(Color.ORANGE); g2d.fillRect(bb.x, bb.y, bb.width, bb.height); }
        }

        // 5. ITEMS
        g2d.setColor(Color.PINK);
        for (Rectangle it : hpItems) g2d.fillRect(it.x, it.y, it.width, it.height);
        g2d.setColor(Color.CYAN);
        for (Rectangle it : powerItems) g2d.fillRect(it.x, it.y, it.width, it.height);

        // 6. DRAW EXPLOSIONS
        for (Explosion exp : explosions) {
            if (exp.img != null) g2d.drawImage(exp.img, exp.x, exp.y, 60, 60, null);
        }

        // 7. HUD
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Poppins", Font.BOLD, 22));
        g2d.drawString("HP: " + hp, 20, 100);
        g2d.drawString("Score: " + score, 20, 130);
        g2d.drawString("Level: " + level, 20, 160);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) moveLeft = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) moveRight = true;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            bullets.add(new Rectangle(tankX + 25, tankY - 30, 30, 50)); 
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) moveLeft = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) moveRight = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    // CLASS EXPLOSION
    class Explosion {
        int x, y;
        Image img;
        int duration = 20; 

        public Explosion(int x, int y, Image img) {
            this.x = x;
            this.y = y;
            this.img = img;
        }

        public void update() {
            duration--;
        }

        public boolean isAlive() {
            return duration > 0;
        }
    }
}