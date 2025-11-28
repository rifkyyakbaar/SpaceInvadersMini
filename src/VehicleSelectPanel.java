import javax.swing.*;
import java.awt.*;

public class VehicleSelectPanel extends JPanel {

    private Main mainApp;

    private Image bgImage;

    public VehicleSelectPanel(Main mainApp) {
        this.mainApp = mainApp;

        setLayout(null);

        bgImage = new ImageIcon("assets/vehicle_bg.png").getImage();

        JLabel title = new JLabel("PILIH KENDARAAN");
        title.setFont(Theme.FONT_BOLD_48);
        title.setForeground(Color.WHITE);
        title.setBounds(420, 80, 500, 60);
        add(title);

        JButton btnTank1 = createVehicleButton("Tank Default", 260);
        JButton btnTank2 = createVehicleButton("Tank Berat", 340);
        JButton btnTank3 = createVehicleButton("Tank Cepat", 420);

        add(btnTank1);
        add(btnTank2);
        add(btnTank3);

        JButton btnBack = new JButton("Kembali");
        btnBack.setFont(Theme.FONT_BOLD_24);
        btnBack.setBackground(Color.GRAY);
        btnBack.setBounds(500, 520, 280, 50);
        btnBack.addActionListener(e -> mainApp.showMainMenu());
        add(btnBack);

        // ========== PILIH KENDARAAN ==========
        btnTank1.addActionListener(e -> {
            mainApp.setSelectedVehicle("Tank_Default");
            JOptionPane.showMessageDialog(this, "Tank Default Dipilih!");
        });

        btnTank2.addActionListener(e -> {
            mainApp.setSelectedVehicle("Tank_Heavy");
            JOptionPane.showMessageDialog(this, "Tank Berat Dipilih!");
        });

        btnTank3.addActionListener(e -> {
            mainApp.setSelectedVehicle("Tank_Fast");
            JOptionPane.showMessageDialog(this, "Tank Cepat Dipilih!");
        });
    }

    private JButton createVehicleButton(String name, int y) {
        JButton btn = new JButton(name);
        btn.setFont(Theme.FONT_BOLD_24);
        btn.setBackground(Theme.GOLD);
        btn.setBounds(500, y, 280, 50);
        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null)
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);

        g.setColor(new Color(255, 255, 255, 170));
        g.fillRoundRect(450, 230, 380, 380, 35, 35);
    }
}
