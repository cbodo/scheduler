package com.cbodo.schedule.controller;

import com.cbodo.schedule.DAO.AppointmentDAO;
import com.cbodo.schedule.DAO.ContactDAO;
import com.cbodo.schedule.DAO.CustomerDAO;
import com.cbodo.schedule.DAO.UserDAO;
import com.cbodo.schedule.model.Appointment;
import com.cbodo.schedule.model.Contact;
import com.cbodo.schedule.model.Customer;
import com.cbodo.schedule.model.User;
import com.cbodo.schedule.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.time.*;
import java.util.ResourceBundle;

/**
 * This class handles the logic for the add appointment form.
 */
public class AddAppointmentController implements Initializable {
    /**
     * Label to display user's time zone.
     */
    @FXML private Label timeZoneLabel;
    /**
     * Label to display user's start time selection in EST.
     */
    @FXML private Label startTimeLabel;
    /**
     * Label to display user's end time selection in EST.
     */
    @FXML private Label endTimeLabel;
    /**
     * Label to display form validation errors.
     */
    @FXML private Label formErrorLabel;
    /**
     * TextField for appointment title input.
     */
    @FXML private TextField titleTextField;
    /**
     * TextField for appointment description input.
     */
    @FXML private TextField descriptionTextField;
    /**
     * TextField for appointment location input.
     */
    @FXML private TextField locationTextField;
    /**
     * TextField for appointment type input.
     */
    @FXML private TextField typeTextField;
    /**
     * DatePicker for appointment start date selection.
     */
    @FXML private DatePicker startDatePicker;
    /**
     * DatePicker for appointment end date selection.
     */
    @FXML private DatePicker endDatePicker;
    /**
     * ComboBox for appointment start time hour selection.
     */
    @FXML private ComboBox<String> startHourComboBox;
    /**
     * ComboBox for appointment start time minute selection.
     */
    @FXML private ComboBox<String> startMinuteComboBox;
    /**
     * ComboBox for appointment start time period selection.
     */
    @FXML private ComboBox<String> startPeriodComboBox;
    /**
     * ComboBox for appointment end time hour selection.
     */
    @FXML private ComboBox<String> endHourComboBox;
    /**
     * ComboBox for appointment end time minute selection.
     */
    @FXML private ComboBox<String> endMinuteComboBox;
    /**
     * ComboBox for appointment end time period selection.
     */
    @FXML private ComboBox<String> endPeriodComboBox;
    /**
     * ComboBox for customer selection.
     */
    @FXML private ComboBox<String> customerComboBox;
    /**
     * ComboBox for contact selection.
     */
    @FXML private ComboBox<String> contactComboBox;
    /**
     * ComboBox for user selection.
     */
    @FXML private ComboBox<String> userComboBox;
    /**
     * Save button.
     */
    @FXML private Button saveButton;
    /**
     * Cancel button.
     */
    @FXML private Button cancelButton;
    /**
     * Scroll pane to display form validation error messages.
     */
    @FXML private ScrollPane errorPane;
    /**
     * LocalTime to store selected appointment start time from ComboBoxes.
     */
    private LocalTime startTime;
    /**
     * LocalTime to store selected appointment start time from ComboBoxes.
     */
    private LocalTime endTime;
    /**
     * ObservableList to store all validation errors.
     */
    private final ObservableList<String> errors = FXCollections.observableArrayList();
    /**
     * Stores the current main controller instance.
     */
    private MainViewController mainViewController;

    /**
     * Updates startTime when a ComboBox related to appointment start time changes.
     */
    @FXML public void updateStartTime() {
        startTime = FormHelper.displayEST(startHourComboBox, startMinuteComboBox, startPeriodComboBox, startTimeLabel);
    }
    /**
     * Updates endTime when a ComboBox related to appointment end time changes.
     */
    @FXML public void updateEndTime() {
        endTime = FormHelper.displayEST(endHourComboBox, endMinuteComboBox, endPeriodComboBox, endTimeLabel);
    }
    /**
     * Updates endDate when startDate changes if startDate is empty or if startDate would be after endDate following the change.
     */
    @FXML public void updateEndDate() {
        FormHelper.clearError(endDatePicker);
        if(endDatePicker.getValue() == null || endDatePicker.getValue().isBefore(startDatePicker.getValue())) {
            endDatePicker.setValue(startDatePicker.getValue());
        }
    }

    /**
     * Updates startDate when endDate changes if endDate is empty or if endDate would be before startDate following the change.
     */
    @FXML public void updateStartDate() {
        FormHelper.clearError(startDatePicker);
        if(startDatePicker.getValue() == null || startDatePicker.getValue().isAfter(endDatePicker.getValue())) {
            startDatePicker.setValue(endDatePicker.getValue());
        }
    }

    /**
     * Displays all errors in the UI.
     */
    public void showErrors() {
        errorPane.setVisible(true);
        formErrorLabel.setText(FormHelper.listToString(errors));
        formErrorLabel.setTextFill(Color.RED);
    }

    /**
     * Checks all fields for input.
     * @return True if all fields have input, false if any field is empty.
     */
    public boolean formIsFilled() {
        ObservableList<Boolean> valid = FXCollections.observableArrayList();
        
        valid.add(FormValidator.fieldHasInput(errors, titleTextField));
        valid.add(FormValidator.fieldHasInput(errors, descriptionTextField));
        valid.add(FormValidator.fieldHasInput(errors, locationTextField));
        valid.add(FormValidator.fieldHasInput(errors, typeTextField));
        valid.add(FormValidator.fieldHasInput(errors, startDatePicker));
        valid.add(FormValidator.fieldHasInput(errors, endDatePicker));
        valid.add(FormValidator.timeHasInput(errors, startHourComboBox, startMinuteComboBox, startPeriodComboBox));
        valid.add(FormValidator.timeHasInput(errors, endHourComboBox, endMinuteComboBox, endPeriodComboBox));
        valid.add(FormValidator.fieldHasInput(errors, contactComboBox));
        valid.add(FormValidator.fieldHasInput(errors, customerComboBox));
        valid.add(FormValidator.fieldHasInput(errors, userComboBox));

        return !valid.contains(false);
    }
    /**
     * Validates form by checking for input and making sure selected times are valid.
     * @param startLDT Appointment start date and time.
     * @param endLDT Appointment end date and time.
     * @return True if the selected times are valid, otherwise false.
     */
    public boolean formIsValid(LocalDateTime startLDT, LocalDateTime endLDT) {
        if(startTime == null || endTime == null) { return false; }
        ObservableList<Boolean> valid = FXCollections.observableArrayList();

        ZonedDateTime startZDT = startLDT.atZone(ZoneId.systemDefault());
        ZonedDateTime endZDT = endLDT.atZone(ZoneId.systemDefault());

        valid.add(FormValidator.timeInOrder(errors, startLDT, endLDT));
        valid.add(FormValidator.isDuringOperatingHours(errors, startZDT));
        valid.add(FormValidator.isDuringOperatingHours(errors, endZDT));
        valid.add(FormValidator.isOnWeekday(errors, startZDT));
        valid.add(FormValidator.isOnWeekday(errors, endZDT));

        return !valid.contains(false);
    }

    /**
     * Creates a new appointment object.
     * @param start Appointment start LocalDateTime.
     * @param end Appointment end LocalDateTime.
     * @return New appointment object.
     * @throws Exception For retrieving the customer, user, and contact data from database.
     */
    public Appointment createAppointmentObject(LocalDateTime start, LocalDateTime end) throws Exception {
        return new Appointment(
            AppointmentDAO.getNewId(),
            FormHelper.capitalize(titleTextField.getText()),
            FormHelper.capitalize(descriptionTextField.getText()),
            FormHelper.capitalize(locationTextField.getText()),
            FormHelper.capitalize(typeTextField.getText()),
            start,
            end,
            CustomerDAO.getCustomerId(customerComboBox.getValue()),
            UserDAO.getUserId(userComboBox.getValue()),
            ContactDAO.getContactId(contactComboBox.getValue())
        );
    }

    /**
     * Handles saving the new appointment to the database when the save button is pressed.
     */
    @FXML public void handleSave() throws Exception {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        errorPane.setVisible(false);
        errors.removeAll();

        if(formIsFilled()) {
            LocalDateTime start = LocalDateTime.of(startDatePicker.getValue(), startTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime end = LocalDateTime.of(endDatePicker.getValue(), endTime).atZone(ZoneId.systemDefault()).toLocalDateTime();

            if (formIsValid(start, end)) {
                Appointment appointment = createAppointmentObject(start, end);
                ObservableList<Appointment> conflicts = AppointmentDAO.getCustomerConflicts(appointment);
                if (conflicts == null || conflicts.isEmpty()) {
                    String alert;
                    if (AppointmentDAO.createAppointment(appointment)) {
                        mainViewController.updateAppointmentData(appointment);
                        alert = "Appointment added: " + appointment;
                        mainViewController.setAlert("appointment", alert, Color.GREEN);
                    } else {
                        alert = "There was a problem adding the appointment.";
                        mainViewController.setAlert("appointment", alert, Color.RED);
                    }
                    stage.close();
                } else {
                    errorPane.setVisible(true);
                    formErrorLabel.setText(ValidationError.CONFLICT + AppointmentDAO.showAppointments(conflicts));
                    formErrorLabel.setTextFill(Color.RED);
                }
            } else {
                showErrors();
            }
        } else {
            showErrors();
        }
    }

    /**
     * Handles cancel button action.
     */
    @FXML public void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        String title = "Cancel";
        String header = "Cancel Adding Appointment?";
        String content = "Would you like to cancel? Changes will not be saved.";
        if(AlertHelper.alert(title, header, content, "Yes", "No", Alert.AlertType.CONFIRMATION)) {
            stage.close();
        }
    }

    /**
     * Passes controller and appointments instances from main controller.
     * @param controller Current MainViewController instance.
     */
    public void init(MainViewController controller) {
        this.mainViewController = controller;
    }

    /**
     * Displays user's time zone in the UI.
     */
    public void setTimeZoneLabel() {
        ZoneId zid = ZoneId.systemDefault();
        timeZoneLabel.setText(zid.getId().replace("_", " "));
    }

    /**
     * Sets the DateConverter for the form's DatePickers.
     * LAMBDA: Expression lambda to add listener to DatePickers to enable use of space bar to open DatePicker box
     *         for faster entry when testing (key must be held down).
     */
    public void initializeDatePickers() {
        FormHelper.setDateConverter(startDatePicker);
        FormHelper.setDateConverter(endDatePicker);
        startDatePicker.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if(event.getCode() == KeyCode.SPACE) {
                startDatePicker.show();
            }
        });
        endDatePicker.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if(event.getCode() == KeyCode.SPACE) {
                endDatePicker.show();
            }
        });
    }

    /**
     * Populates ComboBoxes for time selection.
     */
    public void initializeTimeSelection() {
        FormHelper.setComboBox(startHourComboBox, "hour");
        FormHelper.setComboBox(startMinuteComboBox, "minute");
        FormHelper.setComboBox(startPeriodComboBox, "period");
        FormHelper.setComboBox(endHourComboBox, "hour");
        FormHelper.setComboBox(endMinuteComboBox, "minute");
        FormHelper.setComboBox(endPeriodComboBox, "period");
    }

    /**
     * Populates ComboBoxes with data from the database.
     */
    public void initializeComboBoxData() {
        ObservableList<Contact> allContacts = FXCollections.observableArrayList();
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        ObservableList<User> allUsers = FXCollections.observableArrayList();

        try {
            allContacts = ContactDAO.getAllContacts();
            allCustomers = CustomerDAO.getAllCustomers();
            allUsers = UserDAO.getAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(Contact contact : allContacts) {
            contactComboBox.getItems().add(contact.getContactName());
        }
        for(Customer customer : allCustomers) {
            customerComboBox.getItems().add(customer.getCustomerName());
        }
        for(User user : allUsers) {
            userComboBox.getItems().add(user.getUserName());
        }
    }

    /**
     * Initializes the error pane for displaying validation errors in the UI.
     */
    public void initializeErrorPane() {
        errorPane.setPrefHeight(75);
        errorPane.setVisible(false);
    }

    /**
     * Adds event listeners to the focused property of all fields
     * to remove errors when the field is selected.
     * LAMBDA: Statement lambdas used to call the clearError() method when field is focused.
     */
    public void addListenersToFields() {
        titleTextField.focusedProperty().addListener((observableValue, aBoolean, t1) -> FormHelper.clearError(titleTextField));
        descriptionTextField.focusedProperty().addListener((observableValue, aBoolean, t1) -> FormHelper.clearError(descriptionTextField));
        typeTextField.focusedProperty().addListener((observableValue, aBoolean, t1) -> FormHelper.clearError(typeTextField));
        locationTextField.focusedProperty().addListener((observableValue, aBoolean, t1) -> FormHelper.clearError(locationTextField));
        startDatePicker.focusedProperty().addListener((observableValue, aBoolean, t1) -> FormHelper.clearError(startDatePicker));
        endDatePicker.focusedProperty().addListener((observableValue, aBoolean, t1) -> FormHelper.clearError(endDatePicker));
        startHourComboBox.focusedProperty().addListener((observableValue, aBoolean, t1) -> FormHelper.clearError(startHourComboBox));
        startMinuteComboBox.focusedProperty().addListener((observableValue, aBoolean, t1) -> FormHelper.clearError(startMinuteComboBox));
        startPeriodComboBox.focusedProperty().addListener((observableValue, aBoolean, t1) -> FormHelper.clearError(startPeriodComboBox));
        endHourComboBox.focusedProperty().addListener((observableValue, aBoolean, t1) -> FormHelper.clearError(endHourComboBox));
        endMinuteComboBox.focusedProperty().addListener((observableValue, aBoolean, t1) -> FormHelper.clearError(endMinuteComboBox));
        endPeriodComboBox.focusedProperty().addListener((observableValue, aBoolean, t1) -> FormHelper.clearError(endPeriodComboBox));
        contactComboBox.focusedProperty().addListener((observableValue, aBoolean, t1) -> FormHelper.clearError(contactComboBox));
        customerComboBox.focusedProperty().addListener((observableValue, aBoolean, t1) -> FormHelper.clearError(customerComboBox));
        userComboBox.focusedProperty().addListener((observableValue, aBoolean, t1) -> FormHelper.clearError(userComboBox));
    }

    /**
     * Adds listener to enable submitting form by pressing the enter key.
     */
    @FXML public void handleEnter(KeyEvent key) throws Exception {
        if(key.getCode() == KeyCode.ENTER) {
            handleSave();
        }
    }

    /**
     * Initializes the form.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTimeZoneLabel();
        initializeDatePickers();
        initializeTimeSelection();
        initializeComboBoxData();
        initializeErrorPane();
        addListenersToFields();
    }
}
