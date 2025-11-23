package objects;

import java.awt.Graphics;

public abstract class GameObject {
    protected int x, y;
    protected int width, height;

    public abstract void update();
    public abstract void draw(Graphics g);

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
