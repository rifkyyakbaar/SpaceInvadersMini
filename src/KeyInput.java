import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

    private GamePanel gp;

    public KeyInput(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        gp.keyPressed(e);     // panggil event di GamePanel
    }

    @Override
    public void keyReleased(KeyEvent e) {
        gp.keyReleased(e);   // panggil event di GamePanel
    }
}
