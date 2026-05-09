package util;

import java.sql.*;

public class DBConnection {
    // Change these values to match your MySQL setup
    private static final String URL      = "jdbc:mysql://localhost:3306/job_portal";
    private static final String USER     = "root";
    private static final String PASSWORD = "Ishan@543";

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
