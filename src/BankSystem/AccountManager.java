package BankSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountManager {
    private final Connection connection;

    // constructor
    AccountManager(Connection con) {
        connection = con;
    }

    public int debit_money(long account_number, double amount, String security_pin) {
        final int FAILURE = 0;
        final int SUCCESS = 1;
        final int INSUFFICIENT_BALANCE = 2;
        final int INVALID_PIN = 3;

        try {
            connection.setAutoCommit(false);
            if (account_number != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? and security_pin = ? ");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if (amount <= current_balance) {
                        String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2, account_number);
                        int rowsAffected = preparedStatement1.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("[LOG] : " + "Rs." + amount + " debited Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return SUCCESS;
                        } else {
                            System.out.println("[ERR] : Transaction Failed!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                            return FAILURE;
                        }
                    } else {
                        System.out.println("[LOG] : " + "Insufficient Balance!");
                        return INSUFFICIENT_BALANCE;
                    }
                } else {
                    System.out.println("[LOG] : " + "Invalid Pin!");
                    return INVALID_PIN;
                }
            }
        } catch (SQLException e) {
            System.out.println("[ERR] : " + e.getMessage());
        }
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Transaction Failed!");
    }

    public int credit_money(long account_number, double amount, String security_pin) {
        final int FAILURE = 0;
        final int SUCCESS = 1;
        final int LIMIT_REACHED = 2;
        final int INVALID_PIN = 3;

        try {
            connection.setAutoCommit(false);
            if (account_number != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? and security_pin = ? ");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if ((current_balance + amount) < 99999999.99d) {
                        String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2, account_number);
                        int rowsAffected = preparedStatement1.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("[LOG] : " + "Rs." + amount + " credited Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return SUCCESS;
                        } else {
                            System.out.println("[ERR] : Transaction Failed!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                            return FAILURE;
                        }
                    } else {
                        System.out.println("[LOG] : Account will reach max amount");
                        return LIMIT_REACHED;
                    }
                } else {
                    System.out.println("[LOG] : " + "Invalid Security Pin!");
                    return INVALID_PIN;
                }
            }
        } catch (SQLException e) {
            System.out.println("[ERR] : " + e.getMessage());
        }

        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Transaction Failed");
    }

    public int transfer_money(long sender_account_number, double amount, String security_pin, long receiver_account_number) {
        final int FAILURE = 0;
        final int SUCCESS = 1;
        final int INSUFFICIENT_BALANCE = 2;
        final int LIMIT_REACHED = 3;
        final int INVALID_PIN = 4;
        final int INVALID_AC_NUM = 5;

        try {
            connection.setAutoCommit(false);

            // Check if either account number is 0
            if (sender_account_number == 0 || receiver_account_number == 0) {
                System.out.println("[LOG] : " + "Invalid Account Number!");
                return INVALID_AC_NUM;
            }

            // query
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ? ");
            preparedStatement.setLong(1, sender_account_number);
            preparedStatement.setString(2, security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if sender account number exists
            if (!resultSet.next()) {
                System.out.println("[LOG] : " + "Invalid Security Pin!");
                return INVALID_PIN;
            }
            double current_balance = resultSet.getDouble("balance");

            // Check if amount is less than current balance
            if (!(amount <= current_balance)) {
                System.out.println("[LOG] : " + "Insufficient Balance!");
                return INSUFFICIENT_BALANCE;
            }

            // Check if amount is greater than max amount
            if ((current_balance + amount) > 99999999.99d) {
                System.out.println("[LOG] : Account will reach max amount");
                return LIMIT_REACHED;
            }

            // Write debit and credit queries
            String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
            String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";

            // Debit and Credit prepared Statements
            PreparedStatement creditPreparedStatement = connection.prepareStatement(credit_query);
            PreparedStatement debitPreparedStatement = connection.prepareStatement(debit_query);

            // Set Values for debit and credit prepared statements
            creditPreparedStatement.setDouble(1, amount);
            creditPreparedStatement.setLong(2, receiver_account_number);
            debitPreparedStatement.setDouble(1, amount);
            debitPreparedStatement.setLong(2, sender_account_number);
            int rowsAffected1 = debitPreparedStatement.executeUpdate();
            int rowsAffected2 = creditPreparedStatement.executeUpdate();
            if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                System.out.println("Transaction Successful!");
                System.out.println("Rs." + amount + " Transferred Successfully");
                connection.commit();
                connection.setAutoCommit(true);
                return SUCCESS;
            } else {
                System.out.println("Transaction Failed");
                connection.rollback();
                connection.setAutoCommit(true);
                return FAILURE;
            }

        } catch (SQLException e) {
            System.out.println("[ERR] : " + e.getMessage());
        }

        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Transaction Failed");
    }


    public double getBalance(long account_number) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM Accounts WHERE account_number = ?");
            preparedStatement.setLong(1, account_number);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double balance = resultSet.getDouble("balance");
                return balance;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("[ERR] : " + e.getMessage());
        }
        throw new RuntimeException("Account Balance Fetch Failed!!");
    }
}
