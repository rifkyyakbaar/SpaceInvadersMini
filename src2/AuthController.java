import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthController {

    // Cek jika username sudah ada
    public static boolean isUsernameExist(String username) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true jika username sudah ada
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Register user baru
    public static boolean register(String username, String password) {
        try (Connection conn = DBConnection.getConnection()) {

            if (isUsernameExist(username)) {
                return false; // Username sudah ada
            }

            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Login
    public static boolean login(String username, String password) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true jika ada kecocokan

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
