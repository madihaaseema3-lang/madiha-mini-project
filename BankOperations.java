package BankManagementSystem;

import java.sql.*;

public class BankOperations {

    public static final Connection c = DBConnection.getConnection();

    // Create Customer Account
    public static void createAccount(
            int accountNo,
            String customerName,
            String phone,
            double initialBalance) {

        if (initialBalance < 0) {
            System.out.println("Initial balance cannot be negative.");
            return;
        }

        String query = "INSERT INTO Customer VALUES(?,?,?,?)";

        try {
            PreparedStatement ps = c.prepareStatement(query);

            ps.setInt(1, accountNo);
            ps.setString(2, customerName);
            ps.setString(3, phone);
            ps.setDouble(4, initialBalance);

            ps.executeUpdate();

            System.out.println("Account Created Successfully");

            if (initialBalance > 0) {
                String transaction =
                        "INSERT INTO Transactions " +
                        "(account_no, transaction_type, amount, balance_after) " +
                        "VALUES(?,?,?,?)";

                PreparedStatement ts = c.prepareStatement(transaction);

                ts.setInt(1, accountNo);
                ts.setString(2, "DEPOSIT");
                ts.setDouble(3, initialBalance);
                ts.setDouble(4, initialBalance);

                ts.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Deposit Money
    public static void deposit(int accountNo, double amount) {

        if (amount <= 0) {
            System.out.println("Deposit amount must be greater than zero.");
            return;
        }

        String select = "SELECT balance FROM Customer WHERE account_no=?";
        String update = "UPDATE Customer SET balance=? WHERE account_no=?";
        String transaction =
                "INSERT INTO Transactions " +
                "(account_no, transaction_type, amount, balance_after) " +
                "VALUES(?,?,?,?)";

        try {
            PreparedStatement ps = c.prepareStatement(select);
            ps.setInt(1, accountNo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double oldBalance = rs.getDouble("balance");
                double newBalance = oldBalance + amount;

                PreparedStatement updatePs = c.prepareStatement(update);
                updatePs.setDouble(1, newBalance);
                updatePs.setInt(2, accountNo);
                updatePs.executeUpdate();

                PreparedStatement transactionPs =
                        c.prepareStatement(transaction);

                transactionPs.setInt(1, accountNo);
                transactionPs.setString(2, "DEPOSIT");
                transactionPs.setDouble(3, amount);
                transactionPs.setDouble(4, newBalance);
                transactionPs.executeUpdate();

                System.out.println("Amount Deposited Successfully");
                System.out.println("New Balance: " + newBalance);
            } else {
                System.out.println("Account Not Found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Withdraw Money
    public static void withdraw(int accountNo, double amount) {

        if (amount <= 0) {
            System.out.println("Withdrawal amount must be greater than zero.");
            return;
        }

        String select = "SELECT balance FROM Customer WHERE account_no=?";
        String update = "UPDATE Customer SET balance=? WHERE account_no=?";
        String transaction =
                "INSERT INTO Transactions " +
                "(account_no, transaction_type, amount, balance_after) " +
                "VALUES(?,?,?,?)";

        try {
            PreparedStatement ps = c.prepareStatement(select);
            ps.setInt(1, accountNo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double oldBalance = rs.getDouble("balance");

                if (amount > oldBalance) {
                    System.out.println("Insufficient Balance");
                    return;
                }

                double newBalance = oldBalance - amount;

                PreparedStatement updatePs = c.prepareStatement(update);
                updatePs.setDouble(1, newBalance);
                updatePs.setInt(2, accountNo);
                updatePs.executeUpdate();

                PreparedStatement transactionPs =
                        c.prepareStatement(transaction);

                transactionPs.setInt(1, accountNo);
                transactionPs.setString(2, "WITHDRAW");
                transactionPs.setDouble(3, amount);
                transactionPs.setDouble(4, newBalance);
                transactionPs.executeUpdate();

                System.out.println("Amount Withdrawn Successfully");
                System.out.println("New Balance: " + newBalance);
            } else {
                System.out.println("Account Not Found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Balance Enquiry
    public static void balanceEnquiry(int accountNo) {

        String query = "SELECT * FROM Customer WHERE account_no=?";

        try {
            PreparedStatement ps = c.prepareStatement(query);
            ps.setInt(1, accountNo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("-------------------------");
                System.out.println("Account Number : " + rs.getInt("account_no"));
                System.out.println("Customer Name  : " + rs.getString("customer_name"));
                System.out.println("Phone          : " + rs.getString("phone"));
                System.out.println("Balance        : " + rs.getDouble("balance"));
                System.out.println("-------------------------");
            } else {
                System.out.println("Account Not Found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Transaction History
    public static void transactionHistory(int accountNo) {

        String query =
                "SELECT * FROM Transactions " +
                "WHERE account_no=? ORDER BY transaction_id";

        try {
            PreparedStatement ps = c.prepareStatement(query);
            ps.setInt(1, accountNo);

            ResultSet rs = ps.executeQuery();

            System.out.println("--------------------------------------------------------------");
            System.out.println("ID\tTYPE\tAMOUNT\tBALANCE\tDATE");
            System.out.println("--------------------------------------------------------------");

            while (rs.next()) {
                System.out.println(
                        rs.getInt("transaction_id") + "\t" +
                        rs.getString("transaction_type") + "\t" +
                        rs.getDouble("amount") + "\t" +
                        rs.getDouble("balance_after") + "\t" +
                        rs.getTimestamp("transaction_date"));
            }

            System.out.println("--------------------------------------------------------------");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        createAccount(101, "Rahul", "9876543210", 5000);

        deposit(101, 2000);

        withdraw(101, 1000);

        balanceEnquiry(101);

        transactionHistory(101);
    }
}
