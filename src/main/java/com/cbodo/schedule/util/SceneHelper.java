package com.cbodo.schedule.util;

import com.cbodo.schedule.controller.*;
import com.cbodo.schedule.model.Appointment;
import com.cbodo.schedule.model.Customer;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This class is designed to assist in displaying the various scenes throughout the application.
 */
public class SceneHelper {
    /**
     * Stores the current MainViewController instance.
     */
    private final MainViewController mainViewController;

    /**
     * SceneHelper constructor.
     * @param controller MainViewController instance.
     */
    public SceneHelper(MainViewController controller) {
        this.mainViewController = controller;
    }

    /**
     * Displays the login form in the UI.
     * @param rb ResourceBundle passed by parent controller.
     * @return Boolean result of the form being displayed.
     * @throws IOException For FXMLLoader.
     */
    public static boolean displayLogin(ResourceBundle rb) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/com/cbodo/schedule/view/login-view.fxml"), rb);
        Scene scene = new Scene(loader.load());

        scene.getStylesheets().add(
                Objects.requireNonNull(
                        LoginController.class.getResource("/com/cbodo/schedule/stylesheet.css")
                ).toExternalForm()
        );

        LoginController controller = loader.getController();

        stage.setTitle(rb.getString("login.title"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);

        stage.setScene(scene);
        stage.showAndWait();
        return controller.getLoggedIn();
    }

    /**
     * Displays the customer form for adding a new customer.
     * @param filename Name of the FXML file to load.
     * @return Boolean result of the scene loading.
     * @throws IOException FXMLLoader exception.
     */
    public boolean showCustomerForm(String filename) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(AddCustomerController.class.getResource("/com/cbodo/schedule/view/" + filename + ".fxml"));
        Scene scene = new Scene(loader.load());

        AddCustomerController controller = loader.getController();
        controller.init(this.mainViewController);

        scene.getStylesheets().add(setStylesheet());

        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setScene(scene);
        stage.showAndWait();

        return true;
    }

    /**
     * Overloaded method for displaying customer edit form.
     * @param filename Name of the FXML file to load.
     * @param customer Customer to update.
     * @return Boolean result of scene loading.
     * @throws IOException FXMLLoader exception.
     */
    public boolean showCustomerForm(String filename, Customer customer) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(UpdateCustomerController.class.getResource("/com/cbodo/schedule/view/" + filename + ".fxml"));
        Scene scene = new Scene(loader.load());

        UpdateCustomerController controller = loader.getController();
        controller.init(this.mainViewController, customer);

        scene.getStylesheets().add(setStylesheet());

        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setScene(scene);
        stage.showAndWait();

        return true;
    }
    /**
     * Method for displaying the add appointment form.
     * @param filename Name of the FXML file to load.
     * @return Boolean result of the scene loading.
     * @throws IOException FXMLLoader exception.
     */
    public boolean showAppointmentForm(String filename) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(AddAppointmentController.class.getResource("/com/cbodo/schedule/view/" + filename + ".fxml"));
        Scene scene = new Scene(loader.load());

        AddAppointmentController controller = loader.getController();
        controller.init(this.mainViewController);

        scene.getStylesheets().add(setStylesheet());

        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setScene(scene);
        stage.showAndWait();

        return true;
    }
    /**
     * Overloaded method for displaying the edit appointment form.
     * @param filename Name of the FXML file to load.
     * @param appointment Appointment to edit.
     * @return Boolean result of the scene loading.
     * @throws IOException FXMLLoader exception.
     */
    public boolean showAppointmentForm(String filename, Appointment appointment) throws Exception {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(UpdateCustomerController.class.getResource("/com/cbodo/schedule/view/" + filename + ".fxml"));
        Scene scene = new Scene(loader.load());

        UpdateAppointmentController controller = loader.getController();
        controller.init(this.mainViewController, appointment);

        scene.getStylesheets().add(setStylesheet());

        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setScene(scene);
        stage.showAndWait();

        return true;
    }

    /**
     * Method for displaying the current customer's schedule.
     * @param filename FXML file to load.
     * @param appointments List of appointments in the database.
     * @param customer The current customer.
     * @throws IOException FXMLLoader exception.
     * @throws SQLException Database query exception.
     */
    public void showSchedule(String filename, ObservableList<Appointment> appointments, Customer customer) throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(CustomerScheduleController.class.getResource("/com/cbodo/schedule/view/" + filename + ".fxml"));
        Scene scene = new Scene(loader.load());

        CustomerScheduleController controller = loader.getController();
        controller.init(appointments, customer);

        scene.getStylesheets().add(setStylesheet());

        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * Method for displaying all appointments.
     * @param filename FXML file to load.
     * @param appointments List of appointments in the database.
     * @throws IOException FXMLLoader exception.
     * @throws SQLException Database query exception.
     */
    public void showAll(String filename, ObservableList<Appointment> appointments) throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(AllAppointmentsController.class.getResource("/com/cbodo/schedule/view/" + filename + ".fxml"));
        Scene scene = new Scene(loader.load());

        AllAppointmentsController controller = loader.getController();
        controller.init(appointments);

        scene.getStylesheets().add(setStylesheet());

        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * Sets the stylesheet for the scene.
     * @return The stylesheet.
     */
    public String setStylesheet() {
        return Objects.requireNonNull(
                CustomerScheduleController.class.getResource("/com/cbodo/schedule/stylesheet.css")
        ).toExternalForm();
    }
}
