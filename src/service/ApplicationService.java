package service;

import java.sql.*;
import java.util.*;
import util.DBConnection;

public class ApplicationService {

    
    public String apply(int userId, int jobId) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBConnection.getConnection();

            // Duplicate check
            PreparedStatement check = con.prepareStatement(
                "SELECT id FROM applications WHERE user_id = ? AND job_id = ?");
            check.setInt(1, userId);
            check.setInt(2, jobId);
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                rs.close(); check.close();
                return "DUPLICATE";
            }
            rs.close(); check.close();

            ps = con.prepareStatement(
                "INSERT INTO applications(user_id, job_id, status) VALUES(?, ?, 'APPLIED')");
            ps.setInt(1, userId);
            ps.setInt(2, jobId);
            ps.executeUpdate();
            return "OK";

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        } finally {
            try { if (ps  != null) ps.close();  } catch (Exception ignored) {}
            try { if (con != null) con.close(); } catch (Exception ignored) {}
        }
    }

    // ── Get all applications (for recruiter) ─────────────────────────────────
    public List<String[]> getAllApplications() {
        List<String[]> list = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "SELECT a.id, u.name, u.email, j.title, a.status " +
                "FROM applications a " +
                "JOIN users u ON a.user_id = u.id " +
                "JOIN jobs  j ON a.job_id  = j.id " +
                "ORDER BY a.id");
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("title"),
                    rs.getString("status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs  != null) rs.close();  } catch (Exception ignored) {}
            try { if (ps  != null) ps.close();  } catch (Exception ignored) {}
            try { if (con != null) con.close(); } catch (Exception ignored) {}
        }
        return list;
    }

    // ── Get applications for one candidate ───────────────────────────────────
    public List<String[]> getMyApplications(int userId) {
        List<String[]> list = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "SELECT a.id, j.title, a.status " +
                "FROM applications a " +
                "JOIN jobs j ON a.job_id = j.id " +
                "WHERE a.user_id = ? " +
                "ORDER BY a.id");
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("id"),
                    rs.getString("title"),
                    rs.getString("status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs  != null) rs.close();  } catch (Exception ignored) {}
            try { if (ps  != null) ps.close();  } catch (Exception ignored) {}
            try { if (con != null) con.close(); } catch (Exception ignored) {}
        }
        return list;
    }

    // ── Update status (with valid transition check) ───────────────────────────
    public String updateStatus(int appId, String newStatus) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBConnection.getConnection();

            // Get current status
            PreparedStatement get = con.prepareStatement(
                "SELECT status FROM applications WHERE id = ?");
            get.setInt(1, appId);
            rs = get.executeQuery();
            if (!rs.next()) {
                rs.close(); get.close();
                return "NOT_FOUND";
            }
            String current = rs.getString("status");
            rs.close(); get.close();

            // Validate transition
            if (!validTransition(current, newStatus)) {
                return "INVALID_TRANSITION";
            }

            ps = con.prepareStatement(
                "UPDATE applications SET status = ? WHERE id = ?");
            ps.setString(1, newStatus);
            ps.setInt(2, appId);
            ps.executeUpdate();
            return "OK";

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        } finally {
            try { if (ps  != null) ps.close();  } catch (Exception ignored) {}
            try { if (con != null) con.close(); } catch (Exception ignored) {}
        }
    }

    // ── Valid transition logic (mirrors console app) ──────────────────────────
    private boolean validTransition(String current, String next) {
        switch (current) {
            case "APPLIED":      return next.equals("SHORTLISTED");
            case "SHORTLISTED":  return next.equals("INTERVIEW");
            case "INTERVIEW":    return next.equals("SELECTED") || next.equals("REJECTED");
            default:             return false;
        }
    }
}
