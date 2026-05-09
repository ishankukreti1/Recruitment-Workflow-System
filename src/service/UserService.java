package service;  // FIX: package declaration must be the very first line

import java.sql.*;
import model.User;
import util.DBConnection;

public class UserService {

    public void register(User user) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "INSERT INTO users(name, email, password, role) VALUES(?, ?, ?, ?)");
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // FIX: always close resources in finally block to avoid leaks
            try { if (ps  != null) ps.close();  } catch (Exception ignored) {}
            try { if (con != null) con.close(); } catch (Exception ignored) {}
        }
    }

    public User login(String email, String password) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "SELECT * FROM users WHERE email = ? AND password = ?");
            ps.setString(1, email);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs  != null) rs.close();  } catch (Exception ignored) {}
            try { if (ps  != null) ps.close();  } catch (Exception ignored) {}
            try { if (con != null) con.close(); } catch (Exception ignored) {}
        }
        return null;
    }
}
