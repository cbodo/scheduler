package com.cbodo.schedule.util;

import com.cbodo.schedule.DAO.CountryDAO;
import com.cbodo.schedule.DAO.DivisionDAO;
import com.cbodo.schedule.model.Division;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.cbodo.schedule.util.FormValidator.ZONE;

/**
 * Helper class assists in populating the form UI
 */
public class FormHelper {
    /**
     * Boolean that tells whether Locale is in the US
     */
    public static boolean isInUS = Locale.getDefault().getDisplayCountry().equalsIgnoreCase("united states");
    /**
     * Error style for fields
     */
    public static Border errorStyle = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2),
            new BorderWidths(1.5)));

    /**
     * Sets time ComboBoxes in UI
     * @param comboBox ComboBox to set.
     * @param type String type tells method which ComboBox is being set.
     */
    public static void setComboBox(ComboBox<String> comboBox, String type) {
        if(type.equalsIgnoreCase("hour")) {
            setHourComboBox(comboBox);
        } else if(type.equalsIgnoreCase("minute")) {
            setMinuteComboBox(comboBox);
        } else {
            if(isInUS) {
                comboBox.setVisible(true);
                setPeriodComboBox(comboBox);
            } else {
                comboBox.setVisible(false);
            }
        }
    }

    /**
     * Sets the hour ComboBox.
     * @param hourCB the hour ComboBox
     */
    public static void setHourComboBox(ComboBox<String> hourCB) {
        int length = isInUS ? 12 : 23;
        String[] hours = new String[length];

        for(int i = 0; i < hours.length; ++i) {
            hours[i] = String.valueOf(i+1);
        }
        for (String hour : hours) {
            hourCB.getItems().add(hour);
        }
    }
    /**
     * Sets the minute ComboBox.
     * @param minuteCB the minute ComboBox
     */
    public static void setMinuteComboBox(ComboBox<String> minuteCB) {
        String[] minutes = new String[12];
        for(int j = 0; j < minutes.length; ++j) {
            if(j == 0 || j == 1) {
                minutes[j] = "0" + (j * 5);
            } else {
                minutes[j] = String.valueOf(j * 5);
            }
        }
        for (String minute : minutes) {
            minuteCB.getItems().add(minute);
        }
    }
    /**
     * Sets the period ComboBox.
     * @param periodCB the period ComboBox
     */
    public static void setPeriodComboBox(ComboBox<String> periodCB) {
        String[] periods = {"AM", "PM"};
        for(String period : periods) {
            periodCB.getItems().add(period);
        }
    }

    /**
     * Initializes the date picker format.
     * @param datePicker DatePicker to format.
     */
    public static void setDateConverter(DatePicker datePicker) {
        datePicker.setConverter(new StringConverter<>() {
            final String pattern = "yyyy-MM-dd";
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                datePicker.setPromptText(pattern.toLowerCase());
            }

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }

    /**
     * Sets the label that shows current time input in EST.
     * @param label Label to set.
     * @param time Time to display.
     */
    public static void setTimeLabel(LocalTime time, Label label, Color color) {
        if(!TimeZone.getDefault().equals(TimeZone.getTimeZone(ZONE))) {
            LocalDateTime ldt = LocalDateTime.of(LocalDate.now(), time).atZone(ZoneId.of(ZONE)).toLocalDateTime();
            label.setText("Time selection in America/New York: " + ldt.format(DateFormatHelper.timeFormat()));
            label.setTextFill(color);
        }
    }

    /**
     * Converts list to string.
     * @param list List to convert.
     * @return String result.
     */
    public static String listToString(List<String> list) {
        return ! list.isEmpty() ? "  • " + String.join("\n  • ", list) : "";
    }

    /**
     * Capitalizes string input.
     * @param input String to capitalize.
     * @return String result.
     */
    public static String capitalize(String input) {
        input = input.trim();
        String[] words = input.split("\\s+");

        for (int i = 0; i < words.length; ++i) {
            words[i] = words[i].substring(0,1).toUpperCase() + words[i].substring(1).toLowerCase();
        }

        return String.join(" ", words);
    }

    /**
     * Builds field name string from UI control ID.
     * @param fieldId String value of ID
     * @param endIndex Index to remove unneeded control ID string.
     * @return String result
     */
    public static String getFieldIdString(String fieldId, int endIndex) {
        String temp = fieldId.substring(0, fieldId.length() - endIndex);
        return capitalize(temp);
    }

    /**
     * Adds error to errors list.
     * @param errors List of errors.
     * @param error Error to add.
     */
    public static void addError(ObservableList<String> errors, String error) {
        if(!errors.contains(error)) errors.add(error);
    }

    /**
     * Removes error from errors list.
     * @param errors List of errors.
     * @param error Error to add.
     */
    public static void removeError(ObservableList<String> errors, String error) {
        errors.remove(error);
    }

    /**
     * Removes error style for TextFields.
     */
    public static void clearError(TextField textField) {
            textField.setBorder(null);
    }

    /**
     * Removes error style for ComboBoxes.
     */
    public static void clearError(ComboBox<String> comboBox) { comboBox.setBorder(null); }

    /**
     * Removes error style for DatePickers.
     */
    public static void clearError(DatePicker datePicker) {
            datePicker.setBorder(null);
    }

    /**
     * Converts time input to EST time for display in UI.
     */
    public static LocalTime displayEST(ComboBox<String> hourBox, ComboBox<String> minBox, ComboBox<String> perBox, Label zoneLabel) {
        if(!hourBox.getSelectionModel().isEmpty()) {
            int hour = Integer.parseInt(hourBox.getSelectionModel().getSelectedItem());
            int minute = minBox.getSelectionModel().getSelectedItem() == null ? 0 : Integer.parseInt(minBox.getSelectionModel().getSelectedItem());

            if(isInUS) {
                if (!perBox.getSelectionModel().isEmpty()
                        && perBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("pm")) {
                    if(!hourBox.getValue().equals("12")) {
                        hour = hour + 12;
                    }
                } else {
                    if (hourBox.getValue().equals("12")) {
                        hour = 0;
                    }
                }
            }
            ZonedDateTime zdt = ZonedDateTime.of(LocalDate.now(), LocalTime.of(hour, minute), ZoneId.systemDefault());
            LocalTime est = zdt.withZoneSameInstant(ZoneId.of(ZONE)).toLocalTime();
            Color color = FormValidator.isDuringOperatingHours(zdt) ? Color.BLACK : Color.RED;
            setTimeLabel(est, zoneLabel, color);
            return zdt.toLocalTime();
        }
        return null;
    }

    /**
     * Fills time ComboBoxes with existing appointment's time input.
     */
    public static void initializeTimeComboBox(LocalDateTime time, ComboBox<String> hourBox, ComboBox<String> minuteBox, ComboBox<String> periodBox) {
        if(time != null) {
            int hour = time.getHour();
            int minute = time.getMinute();
            String period;
            if (isInUS) {
                period = hour >= 12 ? "PM" : "AM";
                periodBox.getSelectionModel().select(period);

                hour = hour > 12 ? hour - 12 : hour;
            }
            hourBox.getSelectionModel().select(String.valueOf(hour));
            minuteBox.getSelectionModel().select(minute < 10 ? "0" + minute : String.valueOf(minute));
        }
    }

    /**
     * Returns a simplified string to check country.
     */
    public static String checkCountry(String country) {
        switch (country) {
            case "United States":
            case "US":
            case "U.S":
            case "U.S.":
                return "us";
            case "Canada":
                return "ca";
            case "United Kingdom":
            case "UK":
            case "U.K":
            case "U.K.":
                return "uk";
        }
        return country.substring(0,2).toLowerCase();
    }

    /**
     * Adjusts the first-level division element in the UI depending on location.
     */
    public static String getDivisionLabel(String country) {
        String c = checkCountry(country);
        switch (c) {
            case "us":
                return "State";
            case "ca":
                return "Province";
            case "uk":
                return "Region";
        }
        return null;
    }

    /**
     * Populates first-level division ComboBox based on current country selection.
     */
    public static void updateDivisions(ComboBox<String> countryComboBox, ComboBox<String> divisionComboBox, Label divisionLabel) {
        if(countryComboBox.getValue() != null) {
            divisionLabel.setText(FormHelper.getDivisionLabel(countryComboBox.getValue()));
            divisionComboBox.setPromptText(FormHelper.getDivisionLabel(countryComboBox.getValue()));
            divisionComboBox.getItems().clear();
            try {
                Integer countryId = CountryDAO.getCountryId(countryComboBox.getValue());
                ObservableList<Division> divisionsInCountry = DivisionDAO.getDivisionsInCountry(countryId);
                for (Division division : divisionsInCountry) {
                    divisionComboBox.getItems().add(division.getDivision());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            divisionLabel.setText(FormHelper.getDivisionLabel(Locale.getDefault().getDisplayCountry()));
            divisionComboBox.setPromptText(FormHelper.getDivisionLabel(Locale.getDefault().getDisplayCountry()));
            divisionComboBox.getItems().clear();
        }
    }

    /**
     * Displays address example based on current country selection.
     */
    public static String getAddressLabelText(String country) {
        String c;
        if(country == null) {
            c = Locale.getDefault().getDisplayCountry();
        } else {
            c = country;
        }
        switch (c) {
            case "us":
                return "e.g. 123 ABC Street, White Plains";
            case "uk":
                return "e.g. 123 ABC Street, Greenwich, London";
            case "ca":
                return "e.g. 123 ABC Street, Newmarket";
            default:
                return "e.g. 123 ABC Street, City";
        }
    }

}
