package BankManagementSystem;

import java.sql.*;

public class CreateTable {

    public static void main(String[] args) throws Exception {

        Connection c = DBConnection.getConnection();

        String customerTable = """
                CREATE TABLE IF NOT EXISTS Customer(
                    account_no INT PRIMARY KEY,
                    customer_name VARCHAR(100),
                    phone VARCHAR(15),
                    balance DOUBLE
                )
                """;

        String transactionTable = """
                CREATE TABLE IF NOT EXISTS Transactions(
                    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
                    account_no INT,
                    transaction_type VARCHAR(20),
                    amount DOUBLE,
                    balance_after DOUBLE,
                    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY(account_no) REFERENCES Customer(account_no)
                )
                """;

        Statement s = c.createStatement();

        s.executeUpdate(customerTable);
        s.executeUpdate(transactionTable);

        System.out.println("Tables Created");

        c.close();
    }
}
