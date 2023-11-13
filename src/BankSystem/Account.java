package BankSystem;

import java.sql.*;

public class Account {
    private final Connection connection;

    // constructor
    Account(Connection con) {
        connection = con;
    }

    public long open_account(String email, Double balance, String security_pin) {
        if (!account_exist(email)) {
            String open_account_query = "INSERT INTO Accounts(account_number, full_name, email, balance, security_pin) VALUES(?, ?, ?, ?, ?)";
            String full_name = new User(connection).get_full_name(email);
            try {
                long account_number = generateAccountNumber();
                PreparedStatement preparedStatement = connection.prepareStatement(open_account_query);
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, full_name);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setString(5, security_pin);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    return account_number;
                } else {
                    System.out.println("[LOG] : Account cant be created");
                    return 0;
                }
            } catch (SQLException e) {
                System.out.println("[ERR] : " + e.getMessage());
            }
        } else {
            System.out.println("[ERR] : " + "Account Already Exist");
            return 0;
        }
        throw new RuntimeException("Account Creation Failed!!");
    }

    private long generateAccountNumber() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement
                    .executeQuery("SELECT account_number from Accounts ORDER BY account_number DESC LIMIT 1");
            if (resultSet.next()) {
                long last_account_number = resultSet.getLong("account_number");
                return last_account_number + 1;
            } else {
                return 10000100;
            }
        } catch (SQLException e) {
            System.out.println("[ERR] : " + e.getMessage());
        }
        throw new RuntimeException("Account Number Generation Failed!!");
    }

    public long get_account_number(String email) {
        String query = "SELECT account_number from Accounts WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("account_number");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Account Number Fetch Failed!!");
    }

    public boolean account_exist(String email) {
        String query = "SELECT account_number from Accounts WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Account Existence Check Failed!!");
    }
}
