package BankManagementSystem;

import java.sql.*;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/Bank";
    private static final String User = "root";
    private static final String password = "YOUR_PASSWORD";

    public static Connection getConnection() {
        Connection con = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, User, password);
            System.out.println("Database Connected");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return con;
    }
}
