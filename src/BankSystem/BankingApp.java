package BankSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BankingApp {
    // sql
    public static final String sqlUrl = "jdbc:mysql://localhost:3306/banksystem";
    public static final String sqlUser = "root";
    public static final String sqlPassword = "root";
    // bank
    public static User user;
    public static Account account;
    public static AccountManager accountManager;
    // vars
    public static String currEmail;
    public static long currAcNum;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            user = new User(con);
            account = new Account(con);
            accountManager = new AccountManager(con);

            UserInterface.main(new String[]{});

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean register(String full_name, String email, String password) {
        return user.register(full_name, email, password);
    }

    public static boolean login(String in_email, String password) {
        currEmail = user.login(in_email, password);
        if (hasAccount() && (currEmail != null))
            currAcNum = account.get_account_number(currEmail);
        return currEmail != null;
    }

    public static boolean hasAccount() {
        if (currEmail == null)
            return false;
        return account.account_exist(currEmail);
    }

    public static boolean openAccount(double balance, String password) {
        currAcNum = account.open_account(currEmail, balance, password);
        return currAcNum != 0;
    }

    // --- account manager

    public static int debit_money(double amount, String security_pin) {
        if (currAcNum == 0) {
            System.out.println("[ERR] : Account Number is 0");
            return 0;
        }
        return accountManager.debit_money(currAcNum, amount, security_pin);
    }

    public static int credit_money(double amount, String security_pin) {
        if (currAcNum == 0) {
            System.out.println("[ERR] : Account Number is 0");
            return 0;
        }
        return accountManager.credit_money(currAcNum, amount, security_pin);
    }

    public static int transfer_money(double amount, String security_pin, long to_account_number) {
        if (currAcNum == 0) {
            System.out.println("[ERR] : Account Number is 0");
            return 0;
        }
        return accountManager.transfer_money(currAcNum, amount, security_pin, to_account_number);
    }

    public static double getBalance() {
        if (currAcNum == 0) {
            System.out.println("[ERR] : Account Number is 0");
            return 0;
        }
        return accountManager.getBalance(currAcNum);
    }
}
