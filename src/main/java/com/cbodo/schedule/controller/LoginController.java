package com.cbodo.schedule.controller;

import com.cbodo.schedule.DAO.UserDAO;
import com.cbodo.schedule.model.User;
import com.cbodo.schedule.util.AlertHelper;
import com.cbodo.schedule.util.FormHelper;
import com.cbodo.schedule.util.FormValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

import static com.cbodo.schedule.Main.CURRENT_USER;

/**
 * Handles the logic for the login form.
 */
public class LoginController implements Initializable {
    /**
     * Label for title
     */
    @FXML private Label headerLabel;
    /**
     * Welcome message.
     */
    @FXML private Label welcomeLabel;
    /**
     * Label for username
     */
    @FXML private Label usernameLabel;
    /**
     * Label for password
     */
    @FXML private Label passwordLabel;
    /**
     * Username TextField
     */
    @FXML private TextField usernameField;
    /**
     * Password TextField
     */
    @FXML private PasswordField passwordField;
    /**
     * Location Label
     */
    @FXML private Label locationLabel;
    /**
     * Location result Label
     */
    @FXML private Label locationResultLabel;
    /**
     * Log In Button
     */
    @FXML private Button logInButton;
    /**
     * Exit button
     */
    @FXML private Button exitButton;
    /**
     * Error header label
     */
    @FXML private Label errorHeaderLabel;
    /**
     * Error messages label
     */
    @FXML private Label errorMessagesLabel;
    /**
     * ObservableList to store all validation errors.
     */
    private final ObservableList<String> errors = FXCollections.observableArrayList();
    /**
     * Boolean passed back to parent controller shows success of login.
     */
    private boolean loggedIn;
    /**
     * Resource bundle
     */
    private ResourceBundle rb;

    public boolean getLoggedIn() {
        return this.loggedIn;
    }

    /**
     * Gets error message from the ResourceBundle.
     * @param error Error message to retrieve.
     * @return Returns the message string.
     */
    public String getErrorMessage(String error) {
        return rb.getString(error);
    }

    /**
     * Overloaded method gets error message from the ResourceBundle with the label included.
     * @param error Error message to retrieve.
     * @param label Label to include in message.
     * @return Returns the message string.
     */
    public String getErrorMessage(Label label, String error) {
        return rb.getString(error).replace("%field", label.getText());
    }

    /**
     * Removes all errors from the UI.
     */
    public void clearAllErrors() {
        errors.clear();
        usernameField.setBorder(null);
        passwordField.setBorder(null);
        errorMessagesLabel.setText("");
        errorHeaderLabel.setVisible(false);
    }

    /**
     * Validates form input.
     * @return Boolean result of validation.
     */
    public boolean formIsFilled() {
        ObservableList<Boolean> valid = FXCollections.observableArrayList();
        valid.add(FormValidator.fieldHasInput(errors, usernameField, getErrorMessage(usernameLabel, "error.empty")));
        valid.add(FormValidator.fieldHasInput(errors, passwordField, getErrorMessage(passwordLabel, "error.empty")));
        return !valid.contains(false);
    }

    /**
     * Checks if user exists in the database
     * @return Boolean result of call to UserDAO method.
     */
    public boolean userExists() throws Exception {
        return UserDAO.userExists(usernameField.getText());
    }

    /**
     * Checks if password is valid.
     * @return Boolean result of check.
     */
    public boolean passwordIsValid() {
        return CURRENT_USER != null && CURRENT_USER.getPassword().equals(passwordField.getText());
    }

    /**
     * Validates username and password combination.
     * @return Boolean result of validation.
     */
    public boolean loginIsValid() throws Exception {
        ObservableList<Boolean> valid = FXCollections.observableArrayList();
        valid.add(FormValidator.fieldHasError(errors, usernameField, !userExists(), getErrorMessage("error.invalid")));
        valid.add(FormValidator.fieldHasError(errors, usernameField, !passwordIsValid(), getErrorMessage("error.invalid")));
        return !valid.contains(false);
    }

    /**
     * Retrieves user from database.
     * @return User object.
     */
    public User user() throws Exception {
        return UserDAO.getUser(usernameField.getText());
    }

    /**
     * Gets a Timestamp string for the current time.
     * @return Timestamp string
     */
    public String timestamp() {
        return String.valueOf(Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()));
    }

    public void showErrors() {
        errorHeaderLabel.setVisible(true);
        errorMessagesLabel.setText(FormHelper.listToString(errors));
        errorMessagesLabel.setTextFill(Color.RED);
    }

    /**
     * Checks if user login is valid, then logs user in.
     * @return Boolean result of login attempt.
     */
    public boolean attemptLogin() throws Exception {
        CURRENT_USER = user();
        if(loginIsValid()) {
            logAttempt(CURRENT_USER.getUserName(), timestamp(), "successful");
            loggedIn = true;
            Stage stage = (Stage) logInButton.getScene().getWindow();
            stage.close();
            return true;
        }
        logAttempt(CURRENT_USER != null ? CURRENT_USER.getUserName() : "n/a", timestamp(), "failed");
        loggedIn = false;
        showErrors();
        return false;
    }

    /**
     * Handles Log In Button action.
     */
    public Boolean handleLogIn() throws Exception {
        CURRENT_USER = null;
        clearAllErrors();
        if(formIsFilled()) {
            return attemptLogin();
        }
        showErrors();
        logAttempt("n/a", timestamp(), "failed");
        loggedIn = false;
        return false;
    }

    /**
     * Handles exit button action.
     */
    public void handleExit() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        String title = "Exit";
        String header = "Exiting the Application";
        String content = "Would you like to exit the application?";
        if(AlertHelper.alert(title, header, content, "Exit", "Cancel", Alert.AlertType.WARNING)) {
            stage.close();
        }
    }

    /**
     * Writes the login attempt to file.
     * @param user The user attempting to log in.
     * @param success The success of the login attempt.
     * @param timestamp The timestamp attempt occurred.
     */
    public static void logAttempt(String user, String timestamp, String success) {
        String toWrite = "login attempt by user: " + user + " at " + timestamp +  " " + success + "\n";
        try(FileWriter file = new FileWriter("login_activity.txt", true);
            BufferedWriter writer = new BufferedWriter(file)
        ) {
            writer.append(toWrite);
        } catch (IOException e) {
            System.out.println("There was an error.");
            e.printStackTrace();
        }
    }

    /**
     * Initializes the form.
     * @param resourceBundle ResourceBundle passed from main.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.rb = resourceBundle;
        ZoneId zid = ZoneId.systemDefault();
        headerLabel.setText(resourceBundle.getString("app.title"));
        welcomeLabel.setText(resourceBundle.getString("login.welcome"));
        usernameLabel.setText(resourceBundle.getString("login.username"));
        passwordLabel.setText(resourceBundle.getString("login.password"));
        locationLabel.setText(resourceBundle.getString("login.location"));
        locationResultLabel.setText(zid.getId().replace("_", " "));
        logInButton.setText(resourceBundle.getString("login.button"));
        errorHeaderLabel.setText(resourceBundle.getString("error.label"));

        usernameField.focusedProperty().addListener((observableValue, aBoolean, t1) -> FormHelper.clearError(usernameField));
        passwordField.focusedProperty().addListener((observableValue, aBoolean, t1) -> FormHelper.clearError(passwordField));
    }
}