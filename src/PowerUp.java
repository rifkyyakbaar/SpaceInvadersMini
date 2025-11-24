import java.awt.Color;
import java.awt.Graphics;

public class PowerUp extends GameObject {

    public enum Type { HEALTH, DOUBLE_SHOT }

    private Type type;
    private int speed = 2;
    private boolean collected = false;

    public PowerUp(int x, int y, Type type) {
        this.x = x;
        this.y = y;
        this.width = 18;
        this.height = 18;
        this.type = type;
    }

    @Override
    public void update() {
        y += speed;
        if (y > 800) collected = true; // safe guard: off-screen -> mark collected
    }

    @Override
    public void draw(Graphics g) {
        if (type == Type.HEALTH) g.setColor(Color.GREEN);
        else g.setColor(Color.MAGENTA);
        g.fillOval(x, y, width, height);
    }

    public Type getType() { return type; }
    public boolean isCollected() { return collected; }
    public void collect() { collected = true; }
}
