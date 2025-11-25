import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class LeaderboardPanel extends JPanel {

    private Main mainApp;
    private KoneksiDatabase db = new KoneksiDatabase();

    private Image bgImage;

    public LeaderboardPanel(Main mainApp) {
        this.mainApp = mainApp;
        setLayout(null);

        bgImage = new ImageIcon("assets/mainmenu_bg.png").getImage();

        JLabel title = new JLabel("LEADERBOARD");
        title.setFont(Theme.FONT_BOLD_48);
        title.setForeground(Color.WHITE);
        title.setBounds(430, 60, 500, 60);
        add(title);

        // ========== TABLE ==========  
        String[] columns = {"Username", "Score"};
        ArrayList<String[]> raw = db.getLeaderboard();

        String[][] data = new String[raw.size()][2];
        for (int i = 0; i < raw.size(); i++) {
            data[i] = raw.get(i);
        }

        JTable table = new JTable(data, columns);
        table.setFont(new Font("Poppins", Font.PLAIN, 18));
        table.setRowHeight(30);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(400, 170, 500, 350);
        add(scroll);

        JButton btnBack = new JButton("Kembali");
        btnBack.setFont(Theme.FONT_BOLD_24);
        btnBack.setBackground(Theme.GOLD);
        btnBack.setBounds(520, 550, 250, 50);
        btnBack.addActionListener(e -> mainApp.showMainMenu());
        add(btnBack);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (bgImage != null)
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);

        g.setColor(new Color(255, 255, 255, 180));
        g.fillRoundRect(350, 140, 600, 480, 35, 35);
    }
}
