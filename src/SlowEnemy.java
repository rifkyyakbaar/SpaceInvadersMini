package enemies;

public class SlowEnemy extends Enemy {

    public SlowEnemy(int x, int y, int levelMultiplier) {
        this.x = x;
        this.y = y;
        this.width = 32;
        this.height = 22;
        this.speed = 1 + levelMultiplier / 3; // slightly faster with level
    }

    @Override
    public void move() {
        y += speed;
    }
}
