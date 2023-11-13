package BankSystem;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Optional;

public class UserInterface extends Application {

    // fonts
    private final Font headFont = Font.font("Montserrat", FontWeight.BOLD, FontPosture.REGULAR, 24);
    private final Font bodyFont = Font.font("Montserrat", FontWeight.BOLD, FontPosture.REGULAR, 14);
    // javafx stage
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("SBI");
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:src/resources/sbi.png"));
        showWelcomePage();
        stage.show();
    }

    public static void showInfoPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null); // No header text
        alert.setContentText(message);

        // Set the icon for the Alert
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        Image icon = new Image("file:src/resources/sbi.png");
        alertStage.getIcons().add(icon);

        // Customize the OK button
        ButtonType okButton = new ButtonType("OK", ButtonType.OK.getButtonData());
        alert.getButtonTypes().setAll(okButton);

        // Show the Alert and wait for the OK button to be clicked
        alert.showAndWait();

    }

    // -------------- Welcome Page --------------

    private void showWelcomePage() {
        GridPane welcomePage = getWelcomePane();

        // items
        Label title = new Label("Welcome to SBI  ");
        Button registerButton = new Button(" Register  ");
        Button loginButton = new Button("   Log In   ");
        Button exitButton = new Button("     Exit     ");

        // fonts
        title.setFont(headFont);
        registerButton.setFont(bodyFont);
        loginButton.setFont(bodyFont);
        exitButton.setFont(bodyFont);

        // actions
        registerButton.setOnAction(e -> showRegistrationPage());
        loginButton.setOnAction(e -> showLoginPage());
        exitButton.setOnAction(e -> stage.close());

        // add them to grid pane
        welcomePage.add(title, 0, 1, 2, 1);
        welcomePage.add(registerButton, 0, 2, 2, 1);
        welcomePage.add(loginButton, 0, 3, 2, 1);
        welcomePage.add(exitButton, 0, 4, 2, 1);

        // alignment
        GridPane.setValignment(registerButton, VPos.CENTER);
        GridPane.setValignment(loginButton, VPos.CENTER);
        GridPane.setValignment(exitButton, VPos.CENTER);
        GridPane.setHalignment(registerButton, HPos.CENTER);
        GridPane.setHalignment(loginButton, HPos.CENTER);
        GridPane.setHalignment(exitButton, HPos.CENTER);

        // display using scene
        Scene scene = new Scene(welcomePage, 600, 400);
        stage.setScene(scene);
    }

    private static GridPane getWelcomePane() {
        GridPane welcomePage = new GridPane();
        welcomePage.setAlignment(Pos.TOP_RIGHT);
        welcomePage.setHgap(10);
        welcomePage.setVgap(10);
        Image backgroundImage = new Image("file:src/resources/sbiBackground.jpg");
        welcomePage.setStyle(
                "-fx-background-image: url('" + backgroundImage.getUrl() + "'); " +
                        "-fx-background-size: cover;" // Adjust as needed
        );
        return welcomePage;
    }

    // -------------- Registration Page --------------

    private void showRegistrationPage() {
        GridPane registrationPage = getBackgroundPage();

        // items
        Label title = new Label("  Registration");
        TextField fullNameField = new TextField();
        Label fullnameLabel = new Label("      Full Name");
        TextField emailField = new TextField();
        Label emailLabel = new Label("      Email");
        PasswordField passwordField = new PasswordField();
        Label passwordLabel = new Label("      Password");
        Button registerButton = new Button("Register");
        Button backButton = new Button("Back");

        // fonts
        title.setFont(headFont);
        fullNameField.setPromptText("Enter your full name");
        fullnameLabel.setFont(bodyFont);
        emailField.setPromptText("Enter your email");
        emailLabel.setFont(bodyFont);
        passwordField.setPromptText("Enter your password");
        passwordLabel.setFont(bodyFont);
        registerButton.setFont(bodyFont);
        backButton.setFont(bodyFont);

        // actions
        registerButton.setOnAction(e -> {

            registrationPage.getChildren().removeIf(node -> {
                if (node instanceof Label label) {
                    return label.getText().equals("❗ Empty");
                    // You can adjust this condition based on your specific criteria
                }
                return false;
            });

            // Perform registration (you can add validation here)
            String full_name = fullNameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            if (full_name.isEmpty()) {
                Label msg = new Label("❗ Empty");
                msg.setTextFill(Color.rgb(243, 99, 100));
                registrationPage.add(msg, 2, 2);
            }
            if (email.isEmpty()) {
                Label msg = new Label("❗ Empty");
                msg.setTextFill(Color.rgb(243, 99, 100));
                registrationPage.add(msg, 2, 3);
            }
            if (password.isEmpty()) {
                Label msg = new Label("❗ Empty");
                msg.setTextFill(Color.rgb(243, 99, 100));
                registrationPage.add(msg, 2, 4);
            }
            if (!full_name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                if (showConfirmationPopup()) {
                    if (BankingApp.register(full_name, email, password)) {
                        showInfoPopup("Registration Successful!");
                        showLoginPage();
                    } else {
                        Label temp = new Label("❗ email exists");
                        temp.setTextFill(Color.rgb(243, 99, 100));
                        registrationPage.add(temp, 2, 3);
                    }
                }
            }


        });
        backButton.setOnAction(e -> showWelcomePage());

        // add them to grid pane
        registrationPage.add(title, 0, 1, 2, 1);
        registrationPage.add(fullnameLabel, 0, 2);
        registrationPage.add(fullNameField, 1, 2);
        registrationPage.add(emailLabel, 0, 3);
        registrationPage.add(emailField, 1, 3);
        registrationPage.add(passwordLabel, 0, 4);
        registrationPage.add(passwordField, 1, 4);
        registrationPage.add(registerButton, 0, 5, 2, 1);
        registrationPage.add(backButton, 0, 7, 2, 1);

        // Set alignment for specific elements
        GridPane.setValignment(registerButton, VPos.CENTER);
        GridPane.setHalignment(registerButton, HPos.RIGHT);
        GridPane.setValignment(title, VPos.CENTER);
        GridPane.setHalignment(title, HPos.LEFT);
        GridPane.setValignment(fullNameField, VPos.CENTER);
        GridPane.setHalignment(fullNameField, HPos.RIGHT);
        GridPane.setValignment(emailField, VPos.CENTER);
        GridPane.setHalignment(emailField, HPos.RIGHT);
        GridPane.setValignment(passwordField, VPos.CENTER);
        GridPane.setHalignment(passwordField, HPos.RIGHT);
        GridPane.setValignment(backButton, VPos.CENTER);
        GridPane.setHalignment(backButton, HPos.RIGHT);

        // display using scene
        Scene scene = new Scene(registrationPage, 600, 400);
        stage.setScene(scene);
    }

    private GridPane getBackgroundPage() {
        GridPane bankingAppPage = new GridPane();
        bankingAppPage.setAlignment(Pos.TOP_LEFT);
        bankingAppPage.setHgap(10);
        bankingAppPage.setVgap(10);
        Image backgroundImage = new Image("file:src/resources/SbiRevBackground.jpg");
        bankingAppPage.setStyle(
                "-fx-background-image: url('" + backgroundImage.getUrl() + "'); " +
                        "-fx-background-size: cover;" // Adjust as needed
        );
        return bankingAppPage;
    }

    private boolean showConfirmationPopup() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION) {
            // Customize the icon with a local image
            final Image iconImage = new Image("file:src/resources/check.png");
            final ImageView iconImageView = new ImageView(iconImage);

            {
                setGraphic(iconImageView);
            }
        };
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Confirm?");
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(yesButton, noButton);

        // set window icon
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        Image icon = new Image("file:src/resources/check.png");
        alertStage.getIcons().add(icon);

        // Show the confirmation dialog and wait for user input
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesButton;
    }

    // -------------- Login Page --------------

    private void showLoginPage() {
        GridPane loginPage = getBackgroundPage();

        // items
        Label title = new Label("  Log In");
        TextField emailField = new TextField();
        Label emailLabel = new Label("      Email");
        PasswordField passwordField = new PasswordField();
        Label passwordLabel = new Label("      Password");
        Button loginButton = new Button("Log In");
        Button backButton = new Button("Back");

        // fonts
        title.setFont(headFont);
        emailField.setPromptText("Enter your email");
        passwordField.setPromptText("Enter your password");
        emailLabel.setFont(bodyFont);
        passwordLabel.setFont(bodyFont);
        loginButton.setFont(bodyFont);
        backButton.setFont(bodyFont);

        // actions
        loginButton.setOnAction(e -> {
            loginPage.getChildren().removeIf(node -> {
                if (node instanceof Label label) {
                    return label.getText().equals("❗ Empty");
                    // You can adjust this condition based on your specific criteria
                }
                return false;
            });

            // Perform registration (you can add validation here)
            String email = emailField.getText();
            String password = passwordField.getText();

            if (email.isEmpty()) {
                Label msg = new Label("❗ Empty");
                msg.setTextFill(Color.rgb(243, 99, 100));
                loginPage.add(msg, 2, 2);
            }
            if (password.isEmpty()) {
                Label msg = new Label("❗ Empty");
                msg.setTextFill(Color.rgb(243, 99, 100));
                loginPage.add(msg, 2, 3);
            }
            if (!email.isEmpty() && !password.isEmpty()) {
                if (showConfirmationPopup()) {
                    if (BankingApp.login(email, password)) {
                        showInfoPopup("Login Successful!");
                        afterLogin();
                    } else {
                        Label temp = new Label("❗ check email/password");
                        temp.setTextFill(Color.rgb(243, 99, 100));
                        loginPage.add(temp, 2, 3);
                    }
                }
            }
        });
        backButton.setOnAction(e -> showWelcomePage());


        // add them to grid pane
        loginPage.add(title, 0, 1, 2, 1);
        loginPage.add(emailLabel, 0, 2);
        loginPage.add(emailField, 1, 2);
        loginPage.add(passwordLabel, 0, 3);
        loginPage.add(passwordField, 1, 3);
        loginPage.add(loginButton, 0, 4, 2, 1);
        loginPage.add(backButton, 0, 6, 2, 1);

        // alignment
        GridPane.setValignment(title, VPos.CENTER);
        GridPane.setHalignment(title, HPos.LEFT);
        GridPane.setValignment(emailLabel, VPos.CENTER);
        GridPane.setHalignment(emailLabel, HPos.LEFT);
        GridPane.setValignment(passwordLabel, VPos.CENTER);
        GridPane.setHalignment(passwordLabel, HPos.LEFT);
        GridPane.setValignment(loginButton, VPos.CENTER);
        GridPane.setHalignment(loginButton, HPos.RIGHT);
        GridPane.setValignment(backButton, VPos.CENTER);
        GridPane.setHalignment(backButton, HPos.RIGHT);
        GridPane.setValignment(emailField, VPos.CENTER);
        GridPane.setHalignment(emailField, HPos.RIGHT);
        GridPane.setValignment(passwordField, VPos.CENTER);

        // scene
        Scene scene = new Scene(loginPage, 600, 400);
        stage.setScene(scene);
    }

    // -------------- After Login Page --------------

    private void afterLogin() {
        if (BankingApp.hasAccount())
            showLoginMenu();
        else
            showCreateAccountPage();
    }

    private void showCreateAccountPage() {
        GridPane createAccountPage = getBackgroundPage();

        // items
        Label title = new Label("  Create Bank Account");
        Label initialBalanceLabel = new Label("      Initial Balance");
        TextField initialBalanceField = new TextField();
        Label securityPinLabel = new Label("      Security Pin");
        PasswordField securityPinField = new PasswordField();
        Button createAccountButton = new Button("Create Account");
        Button backButton = new Button("Back");

        // fonts
        title.setFont(headFont);
        initialBalanceLabel.setFont(bodyFont);
        initialBalanceField.setPromptText("Enter Initial Balance");
        securityPinLabel.setFont(bodyFont);
        securityPinField.setPromptText("Enter Security Pin");
        createAccountButton.setFont(bodyFont);
        backButton.setFont(bodyFont);

        // actions
        createAccountButton.setOnAction(e -> {
            createAccountPage.getChildren().removeIf(node -> {
                if (node instanceof Label label) {
                    return label.getText().equals("❗ Too Short") || label.getText().equals("❗ Should not be 0/-ve") || label.getText().equals("❗ Invalid initial balance");
                    // You can adjust this condition based on your specific criteria
                }
                return false;
            });

            // Perform account creation
            try {
                double initialBalance = Double.parseDouble(initialBalanceField.getText());

                String securityPin = securityPinField.getText();

                if (initialBalance <= 0) {
                    Label msg = new Label("❗ Should not be 0/-ve");
                    msg.setTextFill(Color.rgb(243, 99, 100));
                    createAccountPage.add(msg, 2, 2);
                }
                if (securityPin.length() < 4) {
                    Label msg = new Label("❗ Too Short");
                    msg.setTextFill(Color.rgb(243, 99, 100));
                    createAccountPage.add(msg, 2, 3);
                }
                if (!(initialBalance <= 0) && !(securityPin.length() < 4)) {
                    if (showConfirmationPopup()) {
                        if (BankingApp.openAccount(initialBalance, securityPin)) {
                            showInfoPopup("Account Creation Successful!");
                            showLoginMenu();
                        } else {
                            Label temp = new Label("❗ check balance/pin");
                            temp.setTextFill(Color.rgb(243, 99, 100));
                            createAccountPage.add(temp, 2, 3);
                        }
                    }
                }
            } catch (NumberFormatException numberFormatException) {
                createAccountPage.getChildren().removeIf(node -> {
                    if (node instanceof Label label) {
                        return label.getText().equals("❗ Should not be 0/-ve");
                        // You can adjust this condition based on your specific criteria
                    }
                    return false;
                });
                Label msg = new Label("❗ Invalid initial balance");
                msg.setTextFill(Color.rgb(243, 99, 100));
                createAccountPage.add(msg, 2, 2);
            }
        });
        backButton.setOnAction(e -> showLoginPage());


        // add them to grid pane
        createAccountPage.add(title, 0, 1, 2, 1);
        createAccountPage.add(initialBalanceLabel, 0, 2);
        createAccountPage.add(initialBalanceField, 1, 2);
        createAccountPage.add(securityPinLabel, 0, 3);
        createAccountPage.add(securityPinField, 1, 3);
        createAccountPage.add(createAccountButton, 0, 4, 2, 1);
        createAccountPage.add(backButton, 0, 6, 2, 1);

        // alignment
        GridPane.setValignment(title, VPos.CENTER);
        GridPane.setHalignment(title, HPos.LEFT);
        GridPane.setValignment(initialBalanceLabel, VPos.CENTER);
        GridPane.setHalignment(initialBalanceLabel, HPos.LEFT);
        GridPane.setValignment(initialBalanceField, VPos.CENTER);
        GridPane.setHalignment(initialBalanceField, HPos.RIGHT);
        GridPane.setValignment(securityPinLabel, VPos.CENTER);
        GridPane.setHalignment(securityPinLabel, HPos.LEFT);
        GridPane.setValignment(securityPinField, VPos.CENTER);
        GridPane.setHalignment(securityPinField, HPos.LEFT);
        GridPane.setValignment(createAccountButton, VPos.CENTER);
        GridPane.setHalignment(createAccountButton, HPos.RIGHT);
        GridPane.setValignment(backButton, VPos.CENTER);
        GridPane.setHalignment(backButton, HPos.RIGHT);

        // scene
        Scene scene = new Scene(createAccountPage, 600, 400);
        stage.setScene(scene);
    }

    private void showLoginMenu() {
        GridPane loginMenuPage = getBackgroundPage();

        // items
        Label title = new Label("  Menu");
        Button debitButton = new Button("   Debit Money  ");
        Button creditButton = new Button("  Credit Money  ");
        Button transferButton = new Button("Transfer Money");
        Button checkBalanceButton = new Button("  Check Balance ");
        Button logoutButton = new Button("Log Out");
        Button backButton = new Button("Back");

        // fonts
        title.setFont(headFont);
        debitButton.setFont(bodyFont);
        creditButton.setFont(bodyFont);
        transferButton.setFont(bodyFont);
        checkBalanceButton.setFont(bodyFont);
        logoutButton.setFont(bodyFont);
        backButton.setFont(bodyFont);

        // actions
        backButton.setOnAction(e -> showLoginPage());
        debitButton.setOnAction(e -> showDebitPage());
        creditButton.setOnAction(e -> showCreditPage());
        transferButton.setOnAction(e -> showTransferPage());
        checkBalanceButton.setOnAction(e -> showInfoPopup("YOU CURRENT BALANCE : ₹" + BankingApp.getBalance()));
        logoutButton.setOnAction(e -> handleLogout(stage));

        // add them to grid pane
        loginMenuPage.add(title, 0, 1, 2, 1);
        loginMenuPage.add(debitButton, 1, 2);
        loginMenuPage.add(creditButton, 1, 3);
        loginMenuPage.add(transferButton, 1, 4);
        loginMenuPage.add(checkBalanceButton, 1, 5);
        loginMenuPage.add(logoutButton, 1, 6);
        loginMenuPage.add(backButton, 3, 8);

        // alignment
        GridPane.setValignment(backButton, VPos.CENTER);
        GridPane.setHalignment(backButton, HPos.RIGHT);

        // scene
        Scene scene = new Scene(loginMenuPage, 600, 400);
        stage.setScene(scene);
    }

    // menus
    
    public void showDebitPage() {
        GridPane debitPage = getBackgroundPage();

        // items
        Label title = new Label("  Debit Cash");
        Label amountLabel = new Label("      Amount");
        TextField amountField = new TextField();
        Label securityPinLabel = new Label("      Security Pin");
        PasswordField securityPinField = new PasswordField();
        Button debitButton = new Button("Debit");
        Button backButton = new Button("Back");

        // fonts
        title.setFont(headFont);
        amountLabel.setFont(bodyFont);
        amountField.setPromptText("Enter Initial Balance");
        securityPinLabel.setFont(bodyFont);
        securityPinField.setPromptText("Enter Security Pin");
        debitButton.setFont(bodyFont);
        backButton.setFont(bodyFont);

        // actions
        debitButton.setOnAction(e ->
        {
            debitPage.getChildren().removeIf(node -> {
                if (node instanceof Label label) {
                    return label.getText().equals("❗ Too Short") || label.getText().equals("❗ Should not be 0/-ve") || label.getText().equals("❗ Invalid amount");
                    // You can adjust this condition based on your specific criteria
                }
                return false;
            });

            // Perform debit
            try {
                double amount = Double.parseDouble(amountField.getText());

                String securityPin = securityPinField.getText();

                if (amount <= 0) {
                    Label msg = new Label("❗ Should not be 0/-ve");
                    msg.setTextFill(Color.rgb(243, 99, 100));
                    debitPage.add(msg, 2, 2);
                }
                if (securityPin.length() < 4) {
                    Label msg = new Label("❗ Too Short");
                    msg.setTextFill(Color.rgb(243, 99, 100));
                    debitPage.add(msg, 2, 3);
                }
                if (!(amount <= 0) && !(securityPin.length() < 4)) {
                    if (showConfirmationPopup()) {
                        int result = BankingApp.debit_money(amount, securityPin);
                        if (result == 1) {
                            showInfoPopup("Amount Debited Successfully!");
                        } else if (result == 2) {
                            showInfoPopup("Insufficient Balance!");
                        } else if (result == 3) {
                            showInfoPopup("Invalid Pin");
                        } else {
                            showInfoPopup("Transaction Failed");
                            Label temp = new Label("❗ Try Again Later");
                            temp.setTextFill(Color.rgb(243, 99, 100));
                            debitPage.add(temp, 2, 3);
                        }
                    }
                }
            } catch (NumberFormatException numberFormatException) {
                debitPage.getChildren().removeIf(node -> {
                    if (node instanceof Label label) {
                        return label.getText().equals("❗ Should not be 0/-ve");
                        // You can adjust this condition based on your specific criteria
                    }
                    return false;
                });
                Label msg = new Label("❗ Invalid amount");
                msg.setTextFill(Color.rgb(243, 99, 100));
                debitPage.add(msg, 2, 2);
            }
        });
        backButton.setOnAction(e -> showLoginMenu());

        // add them to grid pane
        debitPage.add(title, 0, 1, 2, 1);
        debitPage.add(amountLabel, 0, 2);
        debitPage.add(amountField, 1, 2);
        debitPage.add(securityPinLabel, 0, 3);
        debitPage.add(securityPinField, 1, 3);
        debitPage.add(debitButton, 0, 4, 2, 1);
        debitPage.add(backButton, 0, 6, 2, 1);

        // alignment
        GridPane.setValignment(title, VPos.CENTER);
        GridPane.setHalignment(title, HPos.LEFT);
        GridPane.setValignment(amountLabel, VPos.CENTER);
        GridPane.setHalignment(amountLabel, HPos.LEFT);
        GridPane.setValignment(amountField, VPos.CENTER);
        GridPane.setHalignment(amountField, HPos.RIGHT);
        GridPane.setValignment(securityPinLabel, VPos.CENTER);
        GridPane.setHalignment(securityPinLabel, HPos.LEFT);
        GridPane.setValignment(securityPinField, VPos.CENTER);
        GridPane.setHalignment(securityPinField, HPos.LEFT);
        GridPane.setValignment(debitButton, VPos.CENTER);
        GridPane.setHalignment(debitButton, HPos.RIGHT);
        GridPane.setValignment(backButton, VPos.CENTER);
        GridPane.setHalignment(backButton, HPos.RIGHT);

        // scene
        Scene scene = new Scene(debitPage, 600, 400);
        stage.setScene(scene);
    }

    public void showCreditPage() {
        GridPane creditPage = getBackgroundPage();

        // items
        Label title = new Label("  Credit Cash");
        Label amountLabel = new Label("      Amount");
        TextField amountField = new TextField();
        Label securityPinLabel = new Label("      Security Pin");
        PasswordField securityPinField = new PasswordField();
        Button creditButton = new Button("Credit");
        Button backButton = new Button("Back");

        // fonts
        title.setFont(headFont);
        amountLabel.setFont(bodyFont);
        amountField.setPromptText("Enter Initial Balance");
        securityPinLabel.setFont(bodyFont);
        securityPinField.setPromptText("Enter Security Pin");
        creditButton.setFont(bodyFont);
        backButton.setFont(bodyFont);

        // actions
        creditButton.setOnAction(e ->
        {
            creditPage.getChildren().removeIf(node -> {
                if (node instanceof Label label) {
                    return label.getText().equals("❗ Too Short") || label.getText().equals("❗ Should not be 0/-ve") || label.getText().equals("❗ Invalid amount");
                    // You can adjust this condition based on your specific criteria
                }
                return false;
            });

            // Perform credit
            try {
                double amount = Double.parseDouble(amountField.getText());

                String securityPin = securityPinField.getText();

                if (amount <= 0) {
                    Label msg = new Label("❗ Should not be 0/-ve");
                    msg.setTextFill(Color.rgb(243, 99, 100));
                    creditPage.add(msg, 2, 2);
                }
                if (securityPin.length() < 4) {
                    Label msg = new Label("❗ Too Short");
                    msg.setTextFill(Color.rgb(243, 99, 100));
                    creditPage.add(msg, 2, 3);
                }
                if (!(amount <= 0) && !(securityPin.length() < 4)) {
                    if (showConfirmationPopup()) {
                        int result = BankingApp.credit_money(amount, securityPin);
                        if (result == 1) {
                            showInfoPopup("Amount Credited Successfully!");
                        } else if (result == 2) {
                            showInfoPopup("Limit Reached!");
                        } else if (result == 3) {
                            showInfoPopup("Invalid Pin");
                        } else {
                            showInfoPopup("Transaction Failed");
                            Label temp = new Label("❗ Try Again Later");
                            temp.setTextFill(Color.rgb(243, 99, 100));
                            creditPage.add(temp, 2, 3);
                        }
                    }
                }
            } catch (NumberFormatException numberFormatException) {
                creditPage.getChildren().removeIf(node -> {
                    if (node instanceof Label label) {
                        return label.getText().equals("❗ Should not be 0/-ve");
                        // You can adjust this condition based on your specific criteria
                    }
                    return false;
                });
                Label msg = new Label("❗ Invalid amount");
                msg.setTextFill(Color.rgb(243, 99, 100));
                creditPage.add(msg, 2, 2);
            }
        });
        backButton.setOnAction(e -> showLoginMenu());

        // add them to grid pane
        creditPage.add(title, 0, 1, 2, 1);
        creditPage.add(amountLabel, 0, 2);
        creditPage.add(amountField, 1, 2);
        creditPage.add(securityPinLabel, 0, 3);
        creditPage.add(securityPinField, 1, 3);
        creditPage.add(creditButton, 0, 4, 2, 1);
        creditPage.add(backButton, 0, 6, 2, 1);

        // alignment
        GridPane.setValignment(title, VPos.CENTER);
        GridPane.setHalignment(title, HPos.LEFT);
        GridPane.setValignment(amountLabel, VPos.CENTER);
        GridPane.setHalignment(amountLabel, HPos.LEFT);
        GridPane.setValignment(amountField, VPos.CENTER);
        GridPane.setHalignment(amountField, HPos.RIGHT);
        GridPane.setValignment(securityPinLabel, VPos.CENTER);
        GridPane.setHalignment(securityPinLabel, HPos.LEFT);
        GridPane.setValignment(securityPinField, VPos.CENTER);
        GridPane.setHalignment(securityPinField, HPos.LEFT);
        GridPane.setValignment(creditButton, VPos.CENTER);
        GridPane.setHalignment(creditButton, HPos.RIGHT);
        GridPane.setValignment(backButton, VPos.CENTER);
        GridPane.setHalignment(backButton, HPos.RIGHT);

        // scene
        Scene scene = new Scene(creditPage, 600, 400);
        stage.setScene(scene);
    }

    public void showTransferPage() {
        GridPane transferPage = getBackgroundPage();

        // items
        Label title = new Label("  Transfer Cash");
        Label amountLabel = new Label("      Amount");
        TextField amountField = new TextField();
        Label receiverAcNumLabel = new Label("      Receiver Acc No ");
        TextField receiverAcNumField = new TextField();
        Label securityPinLabel = new Label("      Security Pin");
        PasswordField securityPinField = new PasswordField();
        Button transferButton = new Button("Transfer");
        Button backButton = new Button("Back");

        // fonts
        title.setFont(headFont);
        amountLabel.setFont(bodyFont);
        amountField.setPromptText("Enter Initial Balance");
        receiverAcNumLabel.setFont(bodyFont);
        receiverAcNumField.setPromptText("Enter Receiver Acc No");
        securityPinLabel.setFont(bodyFont);
        securityPinField.setPromptText("Enter Security Pin");
        transferButton.setFont(bodyFont);
        backButton.setFont(bodyFont);

        // actions
        transferButton.setOnAction(e ->
        {
            transferPage.getChildren().removeIf(node -> {
                if (node instanceof Label label) {
                    return label.getText().equals("❗ Too Short") || label.getText().equals("❗ Should not be 0/-ve") || label.getText().equals("❗ Invalid amount") || label.getText().equals("❗ Invalid Receiver Acc No");
                    // You can adjust this condition based on your specific criteria
                }
                return false;
            });

            // Perform transfer
            try {
                double amount = Double.parseDouble(amountField.getText());
                long receiverAcNum = Long.parseLong(receiverAcNumField.getText());
                String securityPin = securityPinField.getText();

                if (amount <= 0) {
                    Label msg = new Label("❗ Should not be 0/-ve");
                    msg.setTextFill(Color.rgb(243, 99, 100));
                    transferPage.add(msg, 2, 2);
                }
                if (Math.log10(receiverAcNum) + 1 < 8) {
                    Label msg = new Label("❗ Invalid Receiver Acc No");
                    msg.setTextFill(Color.rgb(243, 99, 100));
                    transferPage.add(msg, 2, 3);
                }
                if (securityPin.length() < 4) {
                    Label msg = new Label("❗ Too Short");
                    msg.setTextFill(Color.rgb(243, 99, 100));
                    transferPage.add(msg, 2, 4);
                }
                if (!(amount <= 0) && !(securityPin.length() < 4) && ((int) (Math.log10(receiverAcNum) + 1) >= 8)) {
                    if (showConfirmationPopup()) {
                        int result = BankingApp.transfer_money(amount, securityPin, receiverAcNum);
                        if (result == 1) {
                            showInfoPopup("Amount Transferred Successfully!");
                        } else if (result == 2) {
                            showInfoPopup("Insufficient Balance!");
                        } else if (result == 3) {
                            showInfoPopup("Limit Reached");
                        } else if (result == 4) {
                            showInfoPopup("Invalid Pin");
                        } else if (result == 5) {
                            showInfoPopup("Invalid AC num");
                        } else {
                            showInfoPopup("Transaction Failed");
                            Label temp = new Label("❗ Try Again Later");
                            temp.setTextFill(Color.rgb(243, 99, 100));
                            transferPage.add(temp, 2, 5);
                        }
                    }
                }
            } catch (NumberFormatException numberFormatException) {
                transferPage.getChildren().removeIf(node -> {
                    if (node instanceof Label label) {
                        return label.getText().equals("❗ Should not be 0/-ve");
                        // You can adjust this condition based on your specific criteria
                    }
                    return false;
                });
                Label msg = new Label("❗ Invalid amount");
                msg.setTextFill(Color.rgb(243, 99, 100));
                transferPage.add(msg, 2, 2);
            }
        });
        backButton.setOnAction(e -> showLoginMenu());

        // add them to grid pane
        transferPage.add(title, 0, 1, 2, 1);
        transferPage.add(amountLabel, 0, 2);
        transferPage.add(amountField, 1, 2);
        transferPage.add(receiverAcNumLabel, 0, 3);
        transferPage.add(receiverAcNumField, 1, 3);
        transferPage.add(securityPinLabel, 0, 4);
        transferPage.add(securityPinField, 1, 4);
        transferPage.add(transferButton, 0, 5, 2, 1);
        transferPage.add(backButton, 0, 7, 2, 1);

        // alignment
        GridPane.setValignment(title, VPos.CENTER);
        GridPane.setHalignment(title, HPos.LEFT);
        GridPane.setValignment(amountLabel, VPos.CENTER);
        GridPane.setHalignment(amountLabel, HPos.LEFT);
        GridPane.setValignment(amountField, VPos.CENTER);
        GridPane.setHalignment(amountField, HPos.RIGHT);
        GridPane.setValignment(securityPinLabel, VPos.CENTER);
        GridPane.setHalignment(securityPinLabel, HPos.LEFT);
        GridPane.setValignment(securityPinField, VPos.CENTER);
        GridPane.setHalignment(securityPinField, HPos.LEFT);
        GridPane.setValignment(transferButton, VPos.CENTER);
        GridPane.setHalignment(transferButton, HPos.RIGHT);
        GridPane.setValignment(backButton, VPos.CENTER);
        GridPane.setHalignment(backButton, HPos.RIGHT);

        // scene
        Scene scene = new Scene(transferPage, 600, 400);
        stage.setScene(scene);
    }

    private void handleLogout(Stage primaryStage) {
        // Display a confirmation dialog for logout
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Logout Confirmation");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Are you sure you want to log out?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Perform logout actions here (e.g., show login screen)
            System.out.println("User logged out");
            // Close the application or navigate to the login screen
            primaryStage.close();
        }

    }
}
