import java.awt.*;
import javax.swing.*;

public class LoginPanel extends JPanel {

    private Image backgroundImage;
    private Image welcomeImage;
    private Image logoImage;

    public JTextField usernameField;
    public JPasswordField passwordField;
    public JButton btnLogin, btnRegister;

    private Main mainApp;

    public LoginPanel(Main mainApp) {

        this.mainApp = mainApp;

        backgroundImage = new ImageIcon("assets/background.png").getImage();
        welcomeImage = new ImageIcon("assets/welcometo.png").getImage();
        logoImage = new ImageIcon("assets/tanktop.png").getImage();

        setLayout(null);

        JLabel userLbl = new JLabel("Username");
        userLbl.setFont(new Font("Poppins", Font.BOLD, 18));
        userLbl.setBounds(455, 470, 150, 30);
        add(userLbl);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Poppins", Font.PLAIN, 18));
        usernameField.setBounds(580, 470, 220, 30);
        add(usernameField);

        JLabel passLbl = new JLabel("Password");
        passLbl.setFont(new Font("Poppins", Font.BOLD, 18));
        passLbl.setBounds(455, 520, 150, 30);
        add(passLbl);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Poppins", Font.PLAIN, 18));
        passwordField.setBounds(580, 520, 220, 30);
        add(passwordField);

        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Poppins", Font.BOLD, 18));
        btnLogin.setBackground(new Color(218, 185, 80));
        btnLogin.setBounds(535, 568, 200, 30);
        add(btnLogin);

        btnRegister = new JButton("Register");
        btnRegister.setFont(new Font("Poppins", Font.BOLD, 18));    
        btnRegister.setBackground(new Color(218, 185, 80));
        btnRegister.setBounds(535, 615, 200, 30);
        add(btnRegister);

        btnLogin.addActionListener(e -> {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        if (AuthController.login(user, pass)) {
            JOptionPane.showMessageDialog(this, "Login Berhasil!");
            mainApp.showMainMenu();  // langsung pindah ke menu
        } else {
            JOptionPane.showMessageDialog(this, "Username atau Password salah!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        } );

        btnRegister.addActionListener(e -> {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        if (AuthController.isUsernameExist(user)) {
            JOptionPane.showMessageDialog(this, "Username sudah dipakai!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (AuthController.register(user, pass)) {
            JOptionPane.showMessageDialog(this, "Registrasi berhasil! Silakan login.");
        } else {
            JOptionPane.showMessageDialog(this, "Registrasi gagal!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage, 0, 0, 1280, 720, this);
        g.drawImage(welcomeImage, 140, 60, 1000, 100, this);
        g.drawImage(logoImage, 260, 170, 745, 160, this);

        g.setColor(new Color(255, 255, 255, 200));
        g.fillRoundRect(430, 450, 400, 217, 40, 40);
    }

    
    

}
