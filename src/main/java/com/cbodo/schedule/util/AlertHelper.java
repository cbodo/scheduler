package com.cbodo.schedule.util;

import com.cbodo.schedule.DAO.AppointmentDAO;
import com.cbodo.schedule.model.Appointment;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class displays alert messages in the UI.
 */
public class AlertHelper {
    /**
     * Displays an alert dialog if there are any appointments within 15 minutes of login time.
     * @param rb ResourceBundle.
     */
    public static void appointmentAlert(ResourceBundle rb, ObservableList<Appointment> appointments) {
        if(!appointments.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getDialogPane().setStyle("-fx-font-family: 'sans-serif';");
            alert.setResizable(true);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.getDialogPane().setMinWidth(450);
            String alertTitle;
            String alertContent;

            alertContent = AppointmentDAO.showAppointments(appointments);
            alertTitle = rb.getString("upcoming.true.title");
            alert.setTitle(alertTitle);
            alert.setHeaderText(alertTitle);
            alert.setContentText(alertContent);
            alert.showAndWait();
        }
    }

    /**
     * Displays a customer alert with custom alert buttons in the UI.
     * @param title Alert title text.
     * @param header Alert header text.
     * @param content Alert content text.
     * @param yesButton Display text for OK_DONE button.
     * @param noButton Display text for CANCEL_CLOSE button.
     * @param type Type of alert dialog to display.
     * @return Boolean result of the alert.
     */
    public static boolean alert(String title, String header, String content, String yesButton, String noButton, Alert.AlertType type) {
        ButtonType yes = new ButtonType(yesButton, ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType(noButton, ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(type, content, yes, no);
        alert.setResizable(true);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(450);
        alert.getDialogPane().setStyle("-fx-font-family: 'sans-serif';");

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == yes;
    }
}
