package com.cbodo.schedule.controller;

import com.cbodo.schedule.model.Appointment;
import com.cbodo.schedule.util.DateFormatHelper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * This class handles the logic for the Customer Schedule view.
 */
public class AllAppointmentsController implements Initializable {
    /**
     * TableView to display customer schedule.
     */
    @FXML
    private TableView<Appointment> scheduleTableView;
    /**
     * Label to display the selected customer.
     */
    @FXML private Label headerLabel;
    /**
     * ID column.
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
     * End column.
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
    /**
     * Button to close the schedule view.
     */
    @FXML private Button closeButton;

    /**
     * Handles the close button action.
     */
    public void close() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Initializes data passed from the main controller.
     * @param appointments Current list of selected customer's appointments in database.
     * @throws SQLException For retrieving data from database.
     */
    public void init(ObservableList<Appointment> appointments) throws SQLException {
        scheduleTableView.setItems(appointments);
        headerLabel.setText("All Appointments");
    }

    /**
     * Initializes the TableView cells.
     * LAMBDA: Expression lambda used on the startColumn and endColumn cell factories.
     *         This allows use of a custom time format for displaying the start and end times in the table.
     */
    public void initializeTable() {
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
                if (start == null) {
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
                if (end == null) {
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

    /**
     * Initializes the TableView.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
    }
}
