package objects;

import java.awt.Color;
import java.awt.Graphics;

public class Bullet extends GameObject {

    private int speed = 8;
    private boolean destroyed = false;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 4;
        this.height = 10;
    }

    @Override
    public void update() {
        y -= speed;
        if (y + height < 0) destroyed = true;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, width, height);
    }

    public void destroy() { destroyed = true; }
    public boolean isDestroyed() { return destroyed; }
}
