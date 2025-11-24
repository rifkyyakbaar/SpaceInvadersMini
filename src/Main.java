import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        // 1. Bikin Jendelanya (Modul 3: GUI)
        JFrame window = new JFrame();
        
        // 2. Kasih Judul & Aturan Dasar
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Biar program stop pas di-close
        window.setResizable(false); // Biar ukuran window gak bisa diubah-ubah user
        window.setTitle("Tank-Top: Galactic Mini Shooter"); // Judul game kamu
        
        // 3. Pasang "GamePanel" ke dalam Jendela
        // (GamePanel adalah kanvas tempat Tank dan Musuh berada)
        GamePanel gamePanel = new GamePanel(null);
        window.add(gamePanel);
        
        // 4. Pasang Ukuran & Tampilkan
        window.pack(); // Menyesuaikan ukuran window dengan ukuran GamePanel
        window.setLocationRelativeTo(null); // Biar window muncul di tengah layar
        window.setVisible(true); // Munculkan window!
        
        // 5. Nyalakan Mesin Game (Modul 4: Thread)
        gamePanel.startGame();
    }
}