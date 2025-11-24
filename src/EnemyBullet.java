import java.awt.Color;
import java.awt.Graphics;

public class EnemyBullet extends GameObject {

    private int speed = 5;
    private boolean destroyed = false;

    public EnemyBullet(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 6;
        this.height = 12;
    }

    @Override
    public void update() {
        y += speed;
        if (y > 700) destroyed = true;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.PINK);
        g.fillRect(x, y, width, height);
    }

    public void destroy() { destroyed = true; }
    public boolean isDestroyed() { return destroyed; }
}

