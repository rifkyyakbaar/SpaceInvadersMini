package objects;

import java.awt.Color;
import java.awt.Graphics;

public class PlayerShip extends GameObject {

    private int speed = 6;
    private boolean left, right;
    private int health = 3;
    private long lastShot = 0;
    private int shotCooldown = 300; // ms
    private boolean doubleShot = false;
    private long doubleShotExpires = 0;

    public PlayerShip(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 50;
        this.height = 20;
    }

    public void setLeft(boolean val) { left = val; }
    public void setRight(boolean val) { right = val; }

    @Override
    public void update() {
        if (left && x > 0) x -= speed;
        if (right && x < 600 - width) x += speed;
        // check power-up timeout
        if (doubleShot && System.currentTimeMillis() > doubleShotExpires) {
            doubleShot = false;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(x, y, width, height);
    }

    public Bullet[] shoot() {
        long now = System.currentTimeMillis();
        if (now - lastShot < shotCooldown) return new Bullet[0];
        lastShot = now;
        if (!doubleShot) {
            return new Bullet[]{ new Bullet(x + width/2 - 2, y - 12) };
        } else {
            // two bullets
            return new Bullet[]{
                new Bullet(x + 8, y - 12),
                new Bullet(x + width - 12, y - 12)
            };
        }
    }

    public void hit() { health--; }
    public int getHealth() { return health; }

    public void addHealth(int v) { health += v; }

    public void enableDoubleShot(long durationMs) {
        doubleShot = true;
        doubleShotExpires = System.currentTimeMillis() + durationMs;
    }
}
