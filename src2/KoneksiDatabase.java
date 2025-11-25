import java.sql.*;
import java.util.ArrayList;

public class KoneksiDatabase {

    private static final String URL = "jdbc:mysql://localhost:3306/tanktop_db";
    private static final String USER = "root";
    private static final String PASS = "";

    private static Connection conn;

    // =============================
    // INITIALIZE CONNECTION
    // =============================
    public void initialize() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Koneksi MySQL Berhasil!");
        } catch (Exception e) {
            System.out.println("Koneksi Gagal: " + e.getMessage());
        }
    }

    // =============================
    // REGISTER USER
    // =============================
    public static boolean register(String username, String password) {
        try {
            // cek username sudah ada atau belum
            String check = "SELECT * FROM users WHERE username=?";
            PreparedStatement psCheck = conn.prepareStatement(check);
            psCheck.setString(1, username);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                return false; // username sudah dipakai
            }

            // insert user baru
            String sql = "INSERT INTO users (username, password) VALUES (?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Register Error: " + e.getMessage());
            return false;
        }
    }

    // =============================
    // LOGIN USER
    // =============================
    public static int checkLogin(String username, String password) {
        try {
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id"); // return user id
            }

        } catch (Exception e) {
            System.out.println("Login Error: " + e.getMessage());
        }
        return -1; // gagal login
    }

    // =============================
    // SIMPAN SCORE
    // =============================
    public void saveScore(int userId, int score) {
        try {
            String sql = "INSERT INTO scores (user_id, score) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setInt(2, score);

            ps.executeUpdate();
            System.out.println("Score tersimpan!");

        } catch (Exception e) {
            System.out.println("Save Score Error: " + e.getMessage());
        }
    }

    // =============================
    // AMBIL LEADERBOARD
    // =============================
    public ArrayList<String[]> getLeaderboard() {
        ArrayList<String[]> list = new ArrayList<>();

        try {
            String sql =
                "SELECT users.username, scores.score " +
                "FROM scores " +
                "JOIN users ON scores.user_id = users.id " +
                "ORDER BY scores.score DESC LIMIT 10";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new String[]{
                        rs.getString("username"),
                        String.valueOf(rs.getInt("score"))
                });
            }

        } catch (Exception e) {
            System.out.println("Leaderboard Error: " + e.getMessage());
        }

        return list;
    }
}
