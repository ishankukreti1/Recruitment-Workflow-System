import util.DBConnection;
import java.sql.*;

public class TestDB {
    public static void main(String[] args) {
        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM jobs");
            rs.next();
            System.out.println("Jobs count: " + rs.getInt(1));
            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}