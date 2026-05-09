package service;

import java.sql.*;
import java.util.*;
import model.Job;
import util.DBConnection;

public class JobService {

    public void postJob(int userId, String title) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "INSERT INTO jobs(title, recruiter_id) VALUES(?, ?)");
            ps.setString(1, title);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // FIX: close PreparedStatement and Connection in finally block
            try { if (ps  != null) ps.close();  } catch (Exception ignored) {}
            try { if (con != null) con.close(); } catch (Exception ignored) {}
        }
    }

    public List<Job> getJobs() {
        System.out.println("JobService.getJobs() called");
        List<Job> list = new ArrayList<>();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = DBConnection.getConnection();
            System.out.println("Database connection established");
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM jobs");
            System.out.println("Query executed");
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                System.out.println("Found job: " + id + " - " + title);
                list.add(new Job(id, title));
            }
            System.out.println("Total jobs found: " + list.size());
        } catch (Exception e) {
            System.out.println("Error in getJobs(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            // FIX: close all three resources
            try { if (rs  != null) rs.close();  } catch (Exception ignored) {}
            try { if (st  != null) st.close();  } catch (Exception ignored) {}
            try { if (con != null) con.close(); } catch (Exception ignored) {}
        }
        return list;
    }
}
