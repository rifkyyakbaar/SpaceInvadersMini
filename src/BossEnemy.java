package enemies;

import java.awt.Color;
import java.awt.Graphics;

public class BossEnemy extends Enemy {

    private int hp;
    private boolean moveRight = true;
    private int moveRangeRight = 420;
    private int moveRangeLeft = 0;
    private int shootCooldownMs = 1800;
    private long lastShot = 0;

    public BossEnemy(int x, int y, int level) {
        this.x = x;
        this.y = y;
        this.width = 150;
        this.height = 60;
        this.speed = 2 + level/2;
        this.hp = 20 + level * 5;
    }

    @Override
    public void move() {
        if (moveRight) {
            x += speed;
            if (x > moveRangeRight) moveRight = false;
        } else {
            x -= speed;
            if (x < moveRangeLeft) moveRight = true;
        }
        // slight downward movement to pressure player
        y += 0.05;
    }

    public boolean canShoot() {
        long now = System.currentTimeMillis();
        if (now - lastShot > shootCooldownMs) {
            lastShot = now;
            return true;
        }
        return false;
    }

    public void hit() {
        hp--;
        if (hp <= 0) destroyed = true;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect(x, y, width, height);
        // HP bar
        g.setColor(Color.RED);
        int barW = (int)((double)hp / (20) * width);
        if (barW < 0) barW = 0;
        g.fillRect(x, y - 8, barW, 6);
    }
}

