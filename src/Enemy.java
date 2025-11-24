import java.awt.Color;
import java.awt.Graphics;

public abstract class Enemy extends GameObject implements Movable, Destroyable {

    protected int speed;
    protected boolean destroyed = false;

    @Override
    public void update() {
        move();
        // if enemy reaches bottom it will be handled by game (game over or player hit)
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    public void destroy() {
        destroyed = true;
    }
}
