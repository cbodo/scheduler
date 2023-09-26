package com.cbodo.schedule.controller;

import com.cbodo.schedule.DAO.AppointmentDAO;
import com.cbodo.schedule.DAO.CustomerDAO;
import com.cbodo.schedule.model.Appointment;
import com.cbodo.schedule.model.Customer;
import com.cbodo.schedule.util.AlertHelper;
import com.cbodo.schedule.util.DateHelper;
import com.cbodo.schedule.util.SceneHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * This class handles the logic for the main view of the application.
 */
public class MainViewController implements Initializable {
    /**
     * Label to display user's time zone in the UI.
     */
    @FXML private Label timeZoneLabel;
    /**
     * Tab pane for switching between month and week views.
     */
    @FXML private TabPane tabPane;
    /**
     * WeekViewController instance for passing data between controllers.
     */
    @FXML private WeekViewController weekViewController;
    /**
     * MonthViewController instance for passing data between controllers.
     */
    @FXML private MonthViewController monthViewController;
    /**
     * ReportsController instance for passing data between controllers.
     */
    @FXML private ReportsController reportsController;
    /**
     * TableView to display customer data from the database.
     */
    @FXML private TableView<Customer> customerTableView;
    /**
     * Customer ID column.
     */
    @FXML private TableColumn<Customer, Integer> customerIdColumn;
    /**
     * Customer name column.
     */
    @FXML private TableColumn<Customer, String> customerNameColumn;
    /**
     * Customer address column.
     */
    @FXML private TableColumn<Customer, String> addressColumn;
    /**
     * Customer phone column.
     */
    @FXML private TableColumn<Customer, String> phoneColumn;
    /**
     * HBox for appointment buttons.
     */
    @FXML private HBox appointmentButtons;
    /**
     * Button to edit selected appointment.
     */
    @FXML private Button appointmentEditButton;
    /**
     * Button to delete selected appointment.
     */
    @FXML private Button appointmentDeleteButton;
    /**
     * Button to add new appointment.
     */
    @FXML private Button appointmentAddButton;
    /**
     * Button to edit selected customer.
     */
    @FXML private Button customerEditButton;
    /**
     * Button to delete selected customer.
     */
    @FXML private Button customerDeleteButton;
    /**
     * Button to display selected customer's schedule.
     */
    @FXML private Button customerScheduleButton;
    /**
     * Label for displaying Appointment-related alert messages in the UI.
     */
    @FXML private Label appointmentTableLabel;
    /**
     * Label for displaying Customer-related alert messages in the UI.
     */
    @FXML private Label customerTableLabel;
    /**
     * List of selected customer's appointment in the database.
     */
    private final ObservableList<Appointment> customerAppointmentData = FXCollections.observableArrayList();
    /**
     * List of all appointment data in the database.
     */
    private ObservableList<Appointment> appointmentData = FXCollections.observableArrayList();
    /**
     * List of all appointment data in the database.
     */
    private ObservableList<Appointment> weekData = FXCollections.observableArrayList();
    /**
     * List of all appointment data in the database.
     */
    private ObservableList<Appointment> monthData = FXCollections.observableArrayList();
    /**
     * List of all customer data in the database.
     */
    private ObservableList<Customer> customerData = FXCollections.observableArrayList();
    /**
     * Selected appointment.
     */
    private Appointment selectedAppointment;
    /**
     * Selected customer.
     */
    private Customer selectedCustomer;
    /**
     * String containing appointment-related message to display.
     */
    private String appointmentAlertMessage;
    /**
     * String containing customer-related message to display.
     */
    private String customerAlertMessage;
    /**
     * Color of appointment-related alert message.
     */
    private Color appointmentAlertColor;
    /**
     * Color of customer-related alert message.
     */
    private Color customerAlertColor;
    /**
     * SceneHelper instance to help with switching between scenes.
     */
    private final SceneHelper sceneHelper = new SceneHelper(this);

    /**
     * Gets the current weekData list.
     * @return ObservableList of appointments.
     */
    public ObservableList<Appointment> getWeekData() { return this.weekData; }
    /**
     * Gets the current monthData list.
     * @return ObservableList of appointments.
     */
    public ObservableList<Appointment> getMonthData() { return this.monthData; }

    /**
     * Sets the WeekViewController instance passed from child controller.
     * @param weekViewController WeekViewController instance to set.
     */
    public void setWeekViewController(WeekViewController weekViewController) {
        this.weekViewController = weekViewController;
    }

    /**
     * Sets the MonthViewController instance passed from child controller.
     * @param monthViewController MonthViewController instance to set.
     */
    public void setMonthViewController(MonthViewController monthViewController) {
        this.monthViewController = monthViewController;
    }

    /**
     * Sets the ReportsController instance passed from child controller.
     * @param reportsController ReportsController instance to set.
     */
    public void setReportsController(ReportsController reportsController) {
        this.reportsController = reportsController;
    }

    /**
     * Updates appointment TableView with data from the database.
     */
    public void updateAppointmentData() throws Exception {
        monthData = getAppointmentsBetween(DateHelper.firstOfMonth(), DateHelper.lastOfMonth());
        monthViewController.setAppointments(monthData);
        weekData = getAppointmentsBetween(DateHelper.monday(), DateHelper.friday());
        weekViewController.setAppointments(weekData);
        reportsController.updateReports();
    }

    /**
     * Adds appointment to appointment data list.
     * @param appointment Appointment to add.
     */
    public void updateAppointmentData(Appointment appointment) throws Exception {
        if(!appointmentData.contains(appointment)) { appointmentData.add(appointment); }
        updateAppointmentData();
    }

    /**
     * Updates appointment in appointment data list.
     * @param toAdd Appointment to add to list.
     * @param toUpdate Appointment in list to update.
     */
    public void updateAppointmentData(Appointment toAdd, Appointment toUpdate) throws Exception {
        appointmentData.set(getAppointmentIdInList(toUpdate), toAdd);
        updateAppointmentData();
    }

    /**
     * Adds customer to customer data list.
     * @param customer Customer to add.
     */
    public void updateCustomerData(Customer customer) throws Exception {
        if(!customerData.contains(customer)) { customerData.add(customer); }
        reportsController.updateReports();
    }

    /**
     * Updates customer in customer data list.
     * @param toAdd Customer to add to list.
     * @param toUpdate Customer in list to update.
     */
    public void updateCustomerData(Customer toAdd, Customer toUpdate) throws Exception {
        customerData.set(getCustomerIdInList(toUpdate), toAdd);
        reportsController.updateReports();
    }

    /**
     * Sets the alert message to display in the UI.
     * @param alertFor String value for which table to display message under.
     * @param alert String containing the alert message.
     * @param color Color of alert message.
     */
    public void setAlert(String alertFor, String alert, Color color) {
        if(alertFor.equalsIgnoreCase("appointment")) {
            this.appointmentAlertMessage = alert;
            this.appointmentAlertColor = color;
        } else {
            this.customerAlertMessage = alert;
            this.customerAlertColor = color;
        }
    }

    /**
     * Sets the selected appointment.
     * @param appointment Appointment to set.
     */
    public void setSelectedAppointment(Appointment appointment) {
        this.selectedAppointment = appointment;
    }

    /**
     * Disables buttons to edit/delete appointment data if no appointment is selected.
     * @param disable Boolean value to disable buttons or not.
     */
    public void disableAppointmentButtons(boolean disable) {
        appointmentEditButton.setDisable(disable);
        appointmentDeleteButton.setDisable(disable);
    }

    /**
     * Disables buttons to edit/delete/view customer data if no customer is selected.
     * @param disable Boolean value to disable buttons or not.
     */
    public void disableCustomerButtons(boolean disable) {
        customerEditButton.setDisable(disable);
        customerDeleteButton.setDisable(disable);
        customerScheduleButton.setDisable(disable);
    }

    /**
     * Clears all alert messages in the UI.
     */
    public void clearAlertMessages() {
        appointmentTableLabel.setText("");
        appointmentTableLabel.setTextFill(null);
        customerTableLabel.setText("");
        customerTableLabel.setTextFill(null);
    }


    /**
     * Handles adding a new appointment.
     */
    @FXML public void addAppointment() throws Exception {
        clearAlertMessages();
        if(sceneHelper.showAppointmentForm("add-appointment-form")) {
            appointmentTableLabel.setText(appointmentAlertMessage);
            appointmentTableLabel.setTextFill(appointmentAlertColor);
            reportsController.updateReports();
        } else {
            appointmentTableLabel.setText("");
        }
    }

    /**
     * Handles editing the selected appointment.
     */
    @FXML public void editAppointment() throws Exception {
        clearAlertMessages();
        if(selectedAppointment != null) {
            if(sceneHelper.showAppointmentForm("update-appointment-form", selectedAppointment)) {
                appointmentTableLabel.setText(appointmentAlertMessage);
                appointmentTableLabel.setTextFill(appointmentAlertColor);
            }
        } else {
            appointmentTableLabel.setTextFill(Color.RED);
            appointmentTableLabel.setText("Please select an appointment.");
        }
    }

    /**
     * Locates the appointment in the current list of appointments.
     * @param appointment Appointment to find.
     * @return The appointment ID.
     */
    public int getAppointmentIdInList(Appointment appointment) {
        int i = 0;
        for(Appointment a : appointmentData) {
            if(a.equals(appointment)) {
                return i;
            }
            i += 1;
        }
        return i;
    }

    /**
     * Locates the customer in the current list of appointments.
     * @param customer Customer to find.
     * @return The customer ID.
     */
    public int getCustomerIdInList(Customer customer) {
        int i = 0;
        for(Customer c : customerData) {
            if(c.equals(customer)) {
                return i;
            }
            i += 1;
        }
        return i;
    }

    /**
     * Handles deleting selected appointment from the database.
     */
    @FXML public void deleteAppointment() throws Exception {
        clearAlertMessages();
        String appointment = selectedAppointment.toString();
        if(selectedAppointment != null) {
            String title = "Cancel Appointment";
            String header = "Cancelling Appointment";
            String content = "Would you like to cancel this appointment?\nAppointment #" + appointment;
            if(AlertHelper.alert(title, header, content, "Yes", "No", Alert.AlertType.WARNING)) {
                int id = getAppointmentIdInList(selectedAppointment);
                if(AppointmentDAO.deleteAppointment(selectedAppointment.getAppointmentId())) {
                    appointmentData.remove(id);
                    updateAppointmentData();
                    reportsController.updateReports();
                    appointmentTableLabel.setText("Appointment cancelled: " + appointment);
                    appointmentTableLabel.setTextFill(Color.RED);
                }
            }
        }  else {
            appointmentTableLabel.setTextFill(Color.RED);
            appointmentTableLabel.setText("Please select an appointment.");
        }
    }

    /**
     * Handles adding a new customer.
     */
    @FXML public void addCustomer() throws Exception {
        clearAlertMessages();
        if(sceneHelper.showCustomerForm("add-customer-form")) {
            customerTableLabel.setText(customerAlertMessage);
            customerTableLabel.setTextFill(customerAlertColor);
        } else {
            customerTableLabel.setText("");
        }
    }

    /**
     * Handles editing an existing customer.
     */
    @FXML public void editCustomer() throws Exception {
        clearAlertMessages();
        if(selectedCustomer != null) {
            if(sceneHelper.showCustomerForm("update-customer-form", selectedCustomer)) {
                customerTableLabel.setText(customerAlertMessage);
                customerTableLabel.setTextFill(customerAlertColor);
            }
        } else {
            customerTableLabel.setTextFill(Color.RED);
            customerTableLabel.setText("Please select a customer.");
        }
    }

    /**
     * Handles deleting selected customer from the database.
     */
    @FXML public void deleteCustomer() throws Exception {
        clearAlertMessages();
        if(selectedCustomer != null) {
            String customerString = selectedCustomer.getCustomerId() + " " + selectedCustomer.getCustomerName();
            String title = "Delete Customer";
            String header = "Deleting Customer";
            String content = "Would you like to delete this customer?\nCustomer #" + customerString;

            if(AlertHelper.alert(title, header, content, "Yes", "No", Alert.AlertType.WARNING)) {
                ObservableList<Appointment> appointments = AppointmentDAO.getCustomerAppointments(selectedCustomer.getCustomerId());

                int id = 0 , i = 0;
                for(Customer c : customerData) {
                    if(c.getCustomerName().equalsIgnoreCase(selectedCustomer.getCustomerName())) {
                        id = i;
                    }
                    i += 1;
                }
                if(!appointments.isEmpty()) {
                    title = "Cannot Delete Customer";
                    header = "Warning";
                    content = "Customer has existing appointments:\n\n"
                            + AppointmentDAO.showAppointments(appointments)
                            + "\n\nContinuing will DELETE all the data from the database.";
                    if(AlertHelper.alert(title, header, content, "Continue With Delete", "Cancel", Alert.AlertType.WARNING)) {
                        if (CustomerDAO.deleteCustomer(selectedCustomer.getCustomerId())) {
                            appointmentData.removeAll(appointments);
                            updateAppointmentData();
                            customerData.remove(id);
                            reportsController.updateReports();
                            customerTableLabel.setText("Customer deleted: " + customerString);
                            customerTableLabel.setTextFill(Color.RED);
                        }
                    }
                } else if (CustomerDAO.deleteCustomer(selectedCustomer.getCustomerId())) {
                    customerData.remove(id);
                    customerTableLabel.setText("Customer deleted: " + customerString);
                    customerTableLabel.setTextFill(Color.RED);
                    reportsController.updateReports();
                }
            }

        }  else {
            customerTableLabel.setTextFill(Color.RED);
            customerTableLabel.setText("Please select a customer.");
        }
    }

    /**
     * Updates Customer's list of appointments.
     */
    public void updateCustomerAppointments() {
        for(Appointment a : appointmentData) {
            if(a.getCustomerId() == selectedCustomer.getCustomerId()) {
                customerAppointmentData.add(a);
            }
        }
    }

    /**
     * Handles displaying selected customer's schedule in the UI.
     */
    @FXML public void showCustomerSchedule() {
        try {
            updateCustomerAppointments();
            sceneHelper.showSchedule("customer-schedule-view", customerAppointmentData, selectedCustomer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles displaying all appointments in the UI.
     */
    @FXML public void showAll() {
        try {
            updateAppointmentData();
            sceneHelper.showAll("all-appointments", appointmentData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Filters ObservableArray list of appointments to include appointments within a specified time range.
     * LAMBDA: Statement lambda used to filter ObservableList stream to include appointments within the time range.
     * @param start Start of time range.
     * @param end End of time range.
     * @return ObservableArray of appointments.
     */
    public ObservableList<Appointment> getAppointmentsBetween(LocalDateTime start, LocalDateTime end) {
        if(appointmentData == null) return null;
        return appointmentData.stream()
                .filter(appointment -> appointment.getStart().isAfter(start) && appointment.getStart().isBefore(end))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    /**
     * Checks for upcoming appointments and displays an alert in the UI.
     * @param rb ResourceBundle.
     */
    public void checkUpcomingAppointments(ResourceBundle rb) {
        if(!appointmentData.isEmpty()) {
            AlertHelper.appointmentAlert(rb, getAppointmentsBetween(LocalDateTime.now(), LocalDateTime.now().plusMinutes(15)));
        } else {
            appointmentTableLabel.setText(rb.getString("upcoming.false.message"));
            appointmentTableLabel.setTextFill(Color.BLUE);
        }
    }

    /**
     * Loads appointment and customer data from database.
     */
    public void initializeData() throws Exception {
        appointmentData = AppointmentDAO.getAllAppointments();
        customerData = CustomerDAO.getAllCustomers();
        weekData = getAppointmentsBetween(DateHelper.monday(), DateHelper.friday());
        monthData = getAppointmentsBetween(DateHelper.firstOfMonth(), DateHelper.lastOfMonth());
    }

    /**
     * Initializes child controller instances to pass data from parent.
     */
    public void initializeControllers() throws Exception {
        reportsController.init(this);
        weekViewController.init(this);
        monthViewController.init(this);
    }

    /**
     * Initializes Customer TableView
     */
    public void initializeCustomerTable() {
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        customerTableView.setItems(customerData);
    }

    /**
     * Sets the user's time zone in the UI
     */
    public void initializeTimeZoneLabel() {
        ZoneId zid = ZoneId.systemDefault();
        timeZoneLabel.setText("Current time zone: " + zid.getId().replace("_", " "));
    }

    /**
     * Initializes the main view UI.
     * LAMBDA: Expression lambda used for adding a listener to the TabPane.
     *         This makes it easier to changing UI elements when the scene changes.
     * LAMBDA: Expression lambda used for setting a listener to the customerTableView.
     *         This is useful for capturing the selected customer, as well as changing
     *         UI elements when the selection changes.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initializeData();
            checkUpcomingAppointments(resourceBundle);
            initializeControllers();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        initializeTimeZoneLabel();
        initializeCustomerTable();
        disableAppointmentButtons(true);
        disableCustomerButtons(true);

        tabPane.getSelectionModel().selectedItemProperty().addListener((observableValue, oldTab, newTab) -> {
            boolean reportsTab = newTab.getText().equalsIgnoreCase("reports");
            appointmentAddButton.setDisable(reportsTab);
            appointmentButtons.setVisible(!reportsTab);
            appointmentTableLabel.setVisible(!reportsTab);
            weekViewController.clearTableSelection();
            monthViewController.clearTableSelection();
            disableAppointmentButtons(true);
            clearAlertMessages();
        });

        customerTableView.setRowFactory(table -> {
            TableRow<Customer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(!row.isEmpty()) {
                    selectedCustomer = row.getItem();
                    disableCustomerButtons(false);
                    clearAlertMessages();
                }
                if(event.getClickCount() == 2 && !row.isEmpty()) {
                    try {
                        editCustomer();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            return row;
        });
    }

}
