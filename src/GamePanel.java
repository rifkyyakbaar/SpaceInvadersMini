
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import javax.swing.*;


public class GamePanel extends JPanel implements Runnable {

    private Thread gameThread;
    private volatile boolean running = false;
    private volatile boolean paused = false;

    private PlayerShip player;
    private List<Bullet> bullets = Collections.synchronizedList(new ArrayList<>());
    private List<Enemy> enemies = Collections.synchronizedList(new ArrayList<>());
    private List<EnemyBullet> enemyBullets = Collections.synchronizedList(new ArrayList<>());
    private List<Explosion> explosions = Collections.synchronizedList(new ArrayList<>());
    private List<PowerUp> powerUps = Collections.synchronizedList(new ArrayList<>());

    private int score = 0;
    private int level = 1;
    private boolean bossPresent = false;
    private Random rand = new Random();

    private long lastSpawn = 0;
    private GameFrame parent;
    private long lastPowerUpDrop = 0;

    public GamePanel(GameFrame parent) {
        this.parent = parent;
        setPreferredSize(new Dimension(600, 700));
        setBackground(Color.BLACK);

        player = new PlayerShip(275, 620);

        addKeyListener(new KeyInput(this));
        setFocusable(true);
        startGame();

        // optional: play background music (if file exists)
        // SoundPlayer.loop("assets/sfx/bgm.wav");
    }

    public void setPlayerShotCooldown(int ms) {
        // hacky reflection: use PlayerShip field - simpler: add setter (not present)
        // For simplicity, we won't change PlayerShip shotCooldown here in this code block.
    }

    public void startGame() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void spawnEnemy() {
        int type = rand.nextInt(100);
        int x = rand.nextInt(560);
        if (type < 55) enemies.add(new SlowEnemy(x, -20, level));
        else enemies.add(new FastEnemy(x, -20, level));
    }

    private void spawnPowerUp(int x, int y) {
        PowerUp.Type t = rand.nextBoolean() ? PowerUp.Type.HEALTH : PowerUp.Type.DOUBLE_SHOT;
        powerUps.add(new PowerUp(x, y, t));
    }

    @Override
    public void run() {
        lastSpawn = System.currentTimeMillis();
        long spawnInterval = Math.max(400, 1000 - level * 80);

        while (running) {
            if (!paused) {
                updateGame();
                repaint();
            }
            // spawn logic
            spawnInterval = Math.max(400, 1000 - level * 80);
            if (!bossPresent && System.currentTimeMillis() - lastSpawn > spawnInterval) {
                spawnEnemy();
                lastSpawn = System.currentTimeMillis();
            }

            // spawn boss every 3 levels
            if (!bossPresent && level % 3 == 0 && score >= (level-1) * 100) {
                enemies.add(new BossEnemy(200, 30, level));
                bossPresent = true;
                SoundPlayer.play("assets/sfx/boss.wav");
            }

            // power-up occasional drop
            if (System.currentTimeMillis() - lastPowerUpDrop > 8000 && rand.nextInt(100) < 20) {
                int px = rand.nextInt(540);
                spawnPowerUp(px, -20);
                lastPowerUpDrop = System.currentTimeMillis();
            }

            try { Thread.sleep(16); } catch (InterruptedException e) { break; }
        }
    }

    public void updateGame() {
        player.update();

        // bullets
        synchronized (bullets) {
            for (Bullet b : new ArrayList<>(bullets)) b.update();
        }
        // enemies
        synchronized (enemies) {
            for (Enemy e : new ArrayList<>(enemies)) e.update();
        }
        // enemy bullets
        synchronized (enemyBullets) {
            for (EnemyBullet eb : new ArrayList<>(enemyBullets)) eb.update();
        }
        // explosions
        synchronized (explosions) {
            for (Explosion ex : new ArrayList<>(explosions)) ex.update();
        }
        // powerups
        synchronized (powerUps) {
            for (PowerUp pu : new ArrayList<>(powerUps)) pu.update();
        }

        // boss shooting
        for (Enemy e : new ArrayList<>(enemies)) {
            if (e instanceof BossEnemy) {
                BossEnemy b = (BossEnemy)e;
                if (b.canShoot()) {
                    // shoot multiple bullets across width
                    int step = b.getWidth() / 5;
                    for (int i = 1; i <= 3; i++) {
                        int bx = b.getX() + i * step;
                        enemyBullets.add(new EnemyBullet(bx, b.getY() + b.getHeight()));
                    }
                    SoundPlayer.play("assets/sfx/shoot.wav");
                }
            }
        }

        // collisions: bullets -> enemies
        synchronized (enemies) {
            synchronized (bullets) {
                for (Enemy e : new ArrayList<>(enemies)) {
                    for (Bullet b : new ArrayList<>(bullets)) {
                        if (!e.isDestroyed() && !b.isDestroyed()) {
                            if (checkCollision(e, b)) {
                                b.destroy();
                                if (e instanceof BossEnemy) {
                                    ((BossEnemy)e).hit();
                                    SoundPlayer.play("assets/sfx/explosion.wav");
                                    explosions.add(new Explosion(b.getX(), b.getY()));
                                    if (e.isDestroyed()) {
                                        score += 200;
                                        bossPresent = false;
                                        if (rand.nextInt(100) < 60) spawnPowerUp(e.getX(), e.getY());
                                    }
                                } else {
                                    e.destroy();
                                    score += 10;
                                    explosions.add(new Explosion(e.getX(), e.getY()));
                                    SoundPlayer.play("assets/sfx/explosion.wav");
                                    if (rand.nextInt(100) < 25) spawnPowerUp(e.getX(), e.getY());
                                }
                                // level up every 100 pts
                                if (score / 100 + 1 > level) level = score / 100 + 1;
                            }
                        }
                    }
                }
            }
        }

        // enemy bullets -> player
        synchronized (enemyBullets) {
            for (EnemyBullet eb : new ArrayList<>(enemyBullets)) {
                if (!eb.isDestroyed() && checkCollision(player, eb)) {
                    eb.destroy();
                    player.hit();
                    SoundPlayer.play("assets/sfx/explosion.wav");
                    explosions.add(new Explosion(player.getX() + player.getWidth()/2, player.getY() + player.getHeight()/2));
                    if (player.getHealth() <= 0) {
                        gameOver();
                    }
                }
            }
        }

        // enemies -> player (touch)
        synchronized (enemies) {
            for (Enemy e : new ArrayList<>(enemies)) {
                if (!e.isDestroyed() && checkCollision(player, e)) {
                    if (e instanceof BossEnemy) {
                        // boss collision hurts more
                        player.hit();
                        player.hit();
                    } else {
                        player.hit();
                    }
                    e.destroy();
                    explosions.add(new Explosion(e.getX(), e.getY()));
                    SoundPlayer.play("assets/sfx/explosion.wav");
                    if (player.getHealth() <= 0) {
                        gameOver();
                    }
                }
                // if enemy reached bottom area
                if (e.getY() + e.getHeight() >= 660) {
                    // damages player or game over
                    player.hit();
                    e.destroy();
                    if (player.getHealth() <= 0) {
                        gameOver();
                    }
                }
            }
        }

        // powerup collection
        synchronized (powerUps) {
            for (PowerUp p : new ArrayList<>(powerUps)) {
                if (!p.isCollected() && checkCollision(player, p)) {
                    p.collect();
                    if (p.getType() == PowerUp.Type.HEALTH) {
                        player.addHealth(1);
                    } else if (p.getType() == PowerUp.Type.DOUBLE_SHOT) {
                        player.enableDoubleShot(8000); // 8 seconds
                    }
                    SoundPlayer.play("assets/sfx/powerup.wav");
                }
            }
        }

        // cleanups
        bullets.removeIf(Bullet::isDestroyed);
        enemies.removeIf(Enemy::isDestroyed);
        enemyBullets.removeIf(EnemyBullet::isDestroyed);
        explosions.removeIf(Explosion::isDone);
        powerUps.removeIf(PowerUp::isCollected);
    }

    private boolean checkCollision(GameObject a, GameObject b) {
        return a.getX() < b.getX() + b.getWidth() &&
               a.getX() + a.getWidth() > b.getX() &&
               a.getY() < b.getY() + b.getHeight() &&
               a.getY() + a.getHeight() > b.getY();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // background
        g.setColor(Color.BLACK);
        g.fillRect(0,0,getWidth(),getHeight());

        // draw player
        player.draw(g);

        // draw bullets
        synchronized (bullets) { for (Bullet b : bullets) b.draw(g); }

        // draw enemies
        synchronized (enemies) { for (Enemy e : enemies) e.draw(g); }

        // draw enemy bullets
        synchronized (enemyBullets) { for (EnemyBullet eb : enemyBullets) eb.draw(g); }

        // explosions
        synchronized (explosions) { for (Explosion ex : explosions) ex.draw(g); }

        // powerups
        synchronized (powerUps) { for (PowerUp pu : powerUps) pu.draw(g); }

        // HUD
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Level: " + level, 10, 40);
        g.drawString("HP: " + player.getHealth(), 10, 60);

        if (paused) {
            g.setColor(new Color(255,255,255,120));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.BLACK);
            g.drawString("PAUSED", getWidth()/2 - 20, getHeight()/2);
        }
    }

    public void keyPressed(java.awt.event.KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) player.setLeft(true);
        if (code == KeyEvent.VK_RIGHT) player.setRight(true);
        if (code == KeyEvent.VK_SPACE) {
            Bullet[] shots = player.shoot();
            if (shots.length > 0) {
                for (Bullet s : shots) bullets.add(s);
                SoundPlayer.play("assets/sfx/shoot.wav");
            }
        }
        if (code == KeyEvent.VK_P) paused = !paused;
    }

    public void keyReleased(java.awt.event.KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) player.setLeft(false);
        if (code == KeyEvent.VK_RIGHT) player.setRight(false);
    }

    private void gameOver() {
        running = false;
        SoundPlayer.play("assets/sfx/gameover.wav");
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, "GAME OVER!\nScore: " + score + "\nLevel: " + level);
            System.exit(0);
        });
    }
}
