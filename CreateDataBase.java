package BankManagementSystem;

import java.sql.*;

public class CreateDataBase {

    public static void main(String[] args) throws Exception {

        String URL = "jdbc:mysql://localhost:3306/";
        String User = "root";
        String password = "YOUR_PASSWORD";

        Connection c = DriverManager.getConnection(URL, User, password);

        String database = "CREATE DATABASE IF NOT EXISTS Bank";

        Statement s = c.createStatement();
        s.executeUpdate(database);

        System.out.println("Bank Database Created");

        c.close();
    }
}
