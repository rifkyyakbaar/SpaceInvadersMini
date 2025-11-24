public class FastEnemy extends Enemy {

    public FastEnemy(int x, int y, int levelMultiplier) {
        this.x = x;
        this.y = y;
        this.width = 26;
        this.height = 18;
        this.speed = 3 + levelMultiplier / 2;
    }

    @Override
    public void move() {
        y += speed;
    }
}

