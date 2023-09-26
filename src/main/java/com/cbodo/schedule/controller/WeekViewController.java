package com.cbodo.schedule.controller;

import com.cbodo.schedule.model.Appointment;
import com.cbodo.schedule.util.DateFormatHelper;
import com.cbodo.schedule.util.DateHelper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * This class handles the logic for displaying the current week's appointments in the UI.
 */
public class WeekViewController implements Initializable {
    /**
     * TableView for this week's appointments.
     */
    @FXML private TableView<Appointment> appointmentTableView;
    /**
     * Label to display the current week in the UI
     */
    @FXML private Label scheduleLabel;
    /**
     * Appointment ID column.
     */
    @FXML private TableColumn<Appointment, Integer> appointmentIdColumn;
    /**
     * Title column.
     */
    @FXML private TableColumn<Appointment, String> titleColumn;
    /**
     * Description column.
     */
    @FXML private TableColumn<Appointment, String> descriptionColumn;
    /**
     * Location column.
     */
    @FXML private TableColumn<Appointment, String> locationColumn;
    /**
     * Type column.
     */
    @FXML private TableColumn<Appointment, String> typeColumn;
    /**
     * Start time column.
     */
    @FXML private TableColumn<Appointment, LocalDateTime> startColumn;
    /**
     * End time column.
     */
    @FXML private TableColumn<Appointment, LocalDateTime> endColumn;
    /**
     * Customer ID column.
     */
    @FXML private TableColumn<Appointment, Integer> customerIdColumn;

    /**
     * User ID column.
     */
    @FXML private TableColumn<Appointment, Integer> userIdColumn;
    /**
     * Contact ID column.
     */
    @FXML private TableColumn<Appointment, Integer> contactIdColumn;
    private ObservableList<Appointment> appointments;

    public void setAppointments(ObservableList<Appointment> appointments) {
        this.appointments = appointments;
        appointmentTableView.setItems(this.appointments);
    }

    /**
     * Clears the current selection.
     */
    public void clearTableSelection() {
        appointmentTableView.getSelectionModel().clearSelection();
    }

    /**
     * Initializes the data passed by parent controller.
     * LAMBDA: Expression lambda used to set a listener on the TableView.
     *         This makes it easier to pass the selected appointment back to the parent controller.
     * @param controller MainViewController instance.
     * @throws SQLException For querying the database.
     */
    public void init(MainViewController controller) throws SQLException {
        this.appointments = controller.getWeekData();
        appointmentTableView.setItems(this.appointments);
        controller.setWeekViewController(this);
        scheduleLabel.setText(DateHelper.monday().format(DateFormatHelper.dateFormat())
                + " - "
                + DateHelper.friday().format(DateFormatHelper.dateFormat()));

        appointmentTableView.setRowFactory(table -> {
            TableRow<Appointment> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(!row.isEmpty()) {
                    controller.setSelectedAppointment(row.getItem());
                    controller.disableAppointmentButtons(false);
                }
                if(event.getClickCount() == 2 && !row.isEmpty()) {
                    try {
                        controller.editAppointment();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
    }

    /**
     * Initializes the TableView.
     * LAMBDA: Expression lambda used on the startColumn and endColumn cell factories.
     *         This allows use of a custom time format for displaying the start and end times in the table.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        startColumn.setCellFactory(cell -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime start, boolean empty) {
                super.updateItem(start, empty);
                if(start == null) {
                    setText(null);
                } else {
                    setText(start.format(DateFormatHelper.dateTimeFormat()));
                }
            }
        });
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        endColumn.setCellFactory(cell -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime end, boolean empty) {
                super.updateItem(end, empty);
                if(end == null) {
                    setText(null);
                } else {
                    setText(end.format(DateFormatHelper.dateTimeFormat()));
                }
            }
        });
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        contactIdColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));
    }
}
