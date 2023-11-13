package BankSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private final Connection connection;

    // constructor
    User(Connection con) {
        connection = con;
    }

    public boolean register(String full_name, String email, String password) {
        if (user_exist(email)) {
            System.out.println("[LOG] : User already exist for this EMAIL address !");
            return false;
        }
        String register_query = "INSERT INTO users(full_name, email, password) VALUES(?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(register_query);
            preparedStatement.setString(1, full_name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("[ERR] : " + e.getMessage());
        }
        throw new RuntimeException("Registration failed!!");
    }

    public String login(String email, String password) {
        String login_query = "SELECT * FROM Users WHERE email = ? AND password = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(login_query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return email;
            } else {
                System.out.println("[LOG] : email and password do not match !");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("[ERR] : " + e.getMessage());
        }
        throw new RuntimeException("Login failed!!");
    }

    // checker
    private boolean user_exist(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return true;
            else {
                System.out.println("[LOG] : User does not exist for this EMAIL address !");
                return false;
            }
        } catch (Exception e) {
            System.out.println("[ERR] : " + e.getMessage());
        }
        throw new RuntimeException("User Existence Check Failed!!");
    }

    // getter
    public String get_full_name(String email) {
        String query = "SELECT full_name FROM users WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("full_name");
            } else {
                System.out.println("[LOG] : Full Name not found for this EMAIL address !");
            }
        } catch (Exception e) {
            System.out.println("[ERR] : " + e.getMessage());
        }
        throw new RuntimeException("Full Name Fetch Failed!!");
    }
}
