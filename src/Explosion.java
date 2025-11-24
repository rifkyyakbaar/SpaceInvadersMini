import java.awt.Color;
import java.awt.Graphics;

public class Explosion {

    private int x, y;
    private int radius = 8;
    private boolean done = false;

    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        radius += 4;
        if (radius > 48) done = true;
    }

    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillOval(x - radius/2, y - radius/2, radius, radius);
    }

    public boolean isDone() { return done; }
}
