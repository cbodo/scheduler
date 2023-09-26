package com.cbodo.schedule.controller;

import com.cbodo.schedule.DAO.*;
import com.cbodo.schedule.model.Country;
import com.cbodo.schedule.model.Customer;
import com.cbodo.schedule.model.Division;
import com.cbodo.schedule.util.AlertHelper;
import com.cbodo.schedule.util.FormHelper;
import com.cbodo.schedule.util.FormValidator;
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
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class handles the logic for the add customer form.
 */
public class AddCustomerController implements Initializable {
    /**
     * Label to display form validation errors.
     */
    @FXML private Label formErrorLabel;
    /**
     * Label for first-level division.
     */
    @FXML private Label divisionLabel;
    /**
     * Label for address example.
     */
    @FXML private Label addressLabel;
    /**
     * TextField for customer name input.
     */
    @FXML private TextField nameTextField;
    /**
     * TextField for customer address input.
     */
    @FXML private TextField addressTextField;
    /**
     * TextField for customer postal code input.
     */
    @FXML private TextField postalCodeTextField;
    /**
     * TextField for customer phone number input.
     */
    @FXML private TextField phoneTextField;
    /**
     * ComboBox for customer country selection.
     */
    @FXML private ComboBox<String> countryComboBox;
    /**
     * ComboBox for customer first-level division selection.
     */
    @FXML private ComboBox<String> divisionComboBox;
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
     * ObservableList to store all validation errors.
     */
    public static ObservableList<String> errors = FXCollections.observableArrayList();
    /**
     * Current MainViewController instance.
     */
    private MainViewController mainViewController;

    /**
     * Updates divisionComboBox to include only divisions in the selected country.
     * Sets the address example label to match the current country.
     */
    @FXML public void updateDivisions() {
        FormHelper.updateDivisions(countryComboBox, divisionComboBox, divisionLabel);
        setAddressLabel();
    }

    /**
     * Checks all fields for input.
     * @return True if all fields have input, false if any field is empty.
     */
    public boolean formIsFilled() {
        ObservableList<Boolean> valid = FXCollections.observableArrayList();

        valid.add(FormValidator.fieldHasInput(errors, nameTextField));
        valid.add(FormValidator.fieldHasInput(errors, addressTextField));
        valid.add(FormValidator.fieldHasInput(errors, postalCodeTextField));
        valid.add(FormValidator.fieldHasInput(errors, phoneTextField));
        valid.add(FormValidator.fieldHasInput(errors, countryComboBox));
        valid.add(FormValidator.fieldHasInput(errors, divisionComboBox));

        return !valid.contains(false);
    }

    /**
     * Creates a new customer object.
     * @return The new customer object.
     * @throws Exception for retrieving new customer ID and division ID from database.
     */
    public Customer createCustomerObject() throws Exception {
        return new Customer(
                CustomerDAO.getNewId(),
                FormHelper.capitalize(nameTextField.getText()),
                FormHelper.capitalize(addressTextField.getText()),
                postalCodeTextField.getText(),
                phoneTextField.getText(),
                DivisionDAO.getDivisionId(divisionComboBox.getValue())
        );
    }

    /**
     * Handles saving the new customer to the database when the save button is pressed.
     */
    @FXML public void handleSave() throws Exception {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        errorPane.setVisible(false);
        errors.removeAll();

        if(formIsFilled()) {
            Customer customer = createCustomerObject();
            String alert;
            if (CustomerDAO.createCustomer(customer)) {
                mainViewController.updateCustomerData(customer);
                alert = "Customer added: " + customer.toString();
                mainViewController.setAlert("customer", alert, Color.GREEN);
                stage.close();
            } else {
                alert = "There was a problem adding the customer.";
                mainViewController.setAlert("customer", alert, Color.RED);
            }
        } else {
            errorPane.setVisible(true);
            formErrorLabel.setText(FormHelper.listToString(errors));
            formErrorLabel.setTextFill(Color.RED);
        }

    }

    /**
     * Handles cancel button action.
     */
    @FXML void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        String title = "Cancel";
        String header = "Cancel add customer?";
        String content = "Would you like to cancel? Changes will not be saved.";
        if(AlertHelper.alert(title, header, content, "Yes", "No", Alert.AlertType.CONFIRMATION)) {
            stage.close();
        }
    }

    /**
     * Initializes data passed from the main controller.
     * @param controller MainViewController instance.
     *
     */
    public void init(MainViewController controller) {
        this.mainViewController = controller;
        setAddressLabel();
    }

    /**
     * Initializes the division type base on user's current location and displays it in the UI.
     */
    public void setDivisionType() {
        divisionLabel.setText(FormHelper.getDivisionLabel(Locale.getDefault().getDisplayCountry()));
        divisionComboBox.setPromptText(FormHelper.getDivisionLabel(Locale.getDefault().getDisplayCountry()));
    }

    /**
     * Initializes the country and division ComboBoxes with data from the database.
     */
    public void initializeComboBoxes() {
        ObservableList<Country> allCountries = FXCollections.observableArrayList();
        ObservableList<Division> allDivisions = FXCollections.observableArrayList();

        try {
            allCountries = CountryDAO.getAllCountries();
            allDivisions = DivisionDAO.getAllDivisions();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(Country country : allCountries) {
            countryComboBox.getItems().add(country.getCountry());
        }
        for(Division division : allDivisions) {
            divisionComboBox.getItems().add(division.getDivision());
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
     * Sets the address example label to match the current country.
     */
    public void setAddressLabel() {
        addressLabel.setText(FormHelper.getAddressLabelText(countryComboBox.getValue()));
    }

    /**
     * Adds event listeners to the focused property of all fields
     * to remove errors when the field is selected.
     * LAMBDA: Statement lambdas used to call the clearError() method when field is focused.
     */
    public void addListenersToFields() {
        nameTextField.focusedProperty().addListener((observableValue, aBoolean, t1) -> FormHelper.clearError(nameTextField));
        addressTextField.focusedProperty().addListener((observableValue, aBoolean, t1) -> FormHelper.clearError(addressTextField));
        phoneTextField.focusedProperty().addListener((observableValue, aBoolean, t1) -> FormHelper.clearError(phoneTextField));
        postalCodeTextField.focusedProperty().addListener((observableValue, aBoolean, t1) -> FormHelper.clearError(postalCodeTextField));
        countryComboBox.focusedProperty().addListener((observableValue, aBoolean, t1) -> FormHelper.clearError(countryComboBox));
        divisionComboBox.focusedProperty().addListener((observableValue, aBoolean, t1) -> FormHelper.clearError(divisionComboBox));
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
        setDivisionType();
        initializeComboBoxes();
        initializeErrorPane();
        addListenersToFields();
    }
}
