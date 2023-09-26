package com.cbodo.schedule.util;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.*;
import java.util.Locale;

import static com.cbodo.schedule.util.FormHelper.*;

/**
 * This class assists in building the forms for adding/updating appointments and customers.
 */
public class FormValidator {
    /**
     * Stores the company's time zone.
     */
    public static String ZONE = "America/New_York";
    /**
     * Lambda expression used to make it easier to check if a Locale is in the United States.
     */
    public static boolean isInUS = Locale.getDefault().getDisplayCountry().equalsIgnoreCase("united states");
    /**
     * String used with displaying time related errors, when the time is outside business hours.
     */
    public static String operatingHours = isInUS ? "(weekdays 8:00am - 10:00pm EST)" : "(weekdays 8:00 - 22:00 EST)";

    /**
     * General method for error validation.
     * @param errors Current list of errors to display in the UI.
     * @param field TextField to check.
     * @param error Boolean error result.
     * @param message Error message to display.
     * @return Boolean result of validation.
     */
    public static boolean fieldHasError(ObservableList<String> errors, TextField field, boolean error, String message) {
        if(error) {
            FormHelper.addError(errors, message);
            field.setBorder(errorStyle);
            return false;
        }
        FormHelper.removeError(errors, message);
        field.setBorder(null);
        return true;
    }

    /**
     * Checks if the TextField has any input.
     * @param errors Current list of errors to display in the UI.
     * @param field TextField to check.
     * @return Boolean result of validation check.
     */
    public static boolean fieldHasInput(ObservableList<String> errors, TextField field) {
        String error = FormHelper.getFieldIdString(field.getId(), "TextField".length()) + ValidationError.EMPTY;
        if(field.getId().equalsIgnoreCase("phonetextfield")) {
            error = "Phone number" + ValidationError.EMPTY;
        }
        if(field.getId().equalsIgnoreCase("postalcodetextfield")) {
            error = "Postal code" + ValidationError.EMPTY;
        }
        if(field.getText().isEmpty()) {
            FormHelper.addError(errors, error);
            field.setBorder(errorStyle);
            return false;
        }
        FormHelper.removeError(errors, error);
        field.setBorder(null);
        return true;
    }

    /**
     * Overloaded method checks if the TextField has any input and displays passed error message.
     * @param errors Current list of errors to display in the UI.
     * @param field TextField to check.
     * @return Boolean result of validation check.
     */
    public static boolean fieldHasInput(ObservableList<String> errors, TextField field, String error) {
        if(field.getText().isEmpty()) {
            FormHelper.addError(errors, error);
            field.setBorder(errorStyle);
            return false;
        }
        FormHelper.removeError(errors, error);
        field.setBorder(null);
        return true;
    }

    /**
     * Overloaded method checks if the ComboBox has a selected value.
     * @param errors Current list of errors to display in the UI.
     * @param field ComboBox to check.
     * @return Boolean result of validation check.
     */
    public static boolean fieldHasInput(ObservableList<String> errors, ComboBox<String> field) {
        String error = FormHelper.getFieldIdString(field.getId(), "ComboBox".length()) + ValidationError.EMPTY;
        if(field.getSelectionModel().isEmpty()) {
            FormHelper.addError(errors, error);
            field.setBorder(errorStyle);
            return false;
        }
        FormHelper.removeError(errors, error);
        field.setBorder(null);
        return true;
    }
    /**
     * Overloaded method checks if the DatePicker has a selected value.
     * @param errors Current list of errors to display in the UI.
     * @param field DatePicker to check.
     * @return Boolean result of validation check.
     */
    public static boolean fieldHasInput(ObservableList<String> errors, DatePicker field) {
        String error = FormHelper.getFieldIdString(field.getId(), "DatePicker".length()) + " date" + ValidationError.EMPTY;
        if(field.getValue() == null) {
            FormHelper.addError(errors, error);
            field.setBorder(errorStyle);
            return false;
        }
        FormHelper.removeError(errors, error);
        field.setBorder(null);
        return true;
    }

    /**
     * Checks if the ComboBoxes that comprise the time input field have selected values.
     * @param errors Current list of errors to display in the UI.
     * @param hour ComboBox for hour selection.
     * @param minute ComboBox for minute selection
     * @param period ComboBox for time period selection.
     * @return Boolean result of validation check.
     */
    public static boolean timeHasInput(ObservableList<String> errors, ComboBox<String> hour, ComboBox<String> minute, ComboBox<String> period) {
        String error = FormHelper.getFieldIdString(hour.getId(), "HourComboBox".length()) + " time" + ValidationError.EMPTY;
        if(hour.getSelectionModel().isEmpty() || minute.getSelectionModel().isEmpty()) {
            FormHelper.addError(errors, error);
            hour.setBorder(errorStyle);
            minute.setBorder(errorStyle);
            if(isInUS) {
                if(period.getSelectionModel().isEmpty()) {
                    period.setBorder(errorStyle);
                }
            }
            return false;
        }
        FormHelper.removeError(errors, error);
        hour.setBorder(null);
        minute.setBorder(null);
        if(isInUS) period.setBorder(null);
        return true;
    }

    /**
     * Checks if start time is before end time.
     * @param errors Current list of errors to display in the UI.
     * @param start Start time.
     * @param end End time.
     * @return Boolean result of validation check.
     */
    public static boolean timeInOrder(ObservableList<String> errors, LocalDateTime start, LocalDateTime end) {
        if(start.equals(end) || !start.isBefore(end)) {
            FormHelper.addError(errors, ValidationError.TIME_ORDER.toString());
            return false;
        }
        errors.remove(ValidationError.TIME_ORDER.toString());
        return true;
    }

    /**
     * Checks if selected time is within business hours.
     * @param errors Current list of errors to display in the UI.
     * @param time Time to check.
     * @return Boolean result of validation check.
     */
    public static boolean isDuringOperatingHours(ObservableList<String> errors, ZonedDateTime time) {
        ZonedDateTime open = ZonedDateTime.of(time.toLocalDate(), LocalTime.of(8, 0), ZoneId.of(ZONE));
        ZonedDateTime close = ZonedDateTime.of(time.toLocalDate(), LocalTime.of(22, 0), ZoneId.of(ZONE));
        if(time.withZoneSameInstant(ZoneId.of(ZONE)).isBefore(open) || time.withZoneSameInstant(ZoneId.of(ZONE)).isAfter(close)) {
            FormHelper.addError(errors, ValidationError.BUSINESS_HOURS.toString());
            return false;
        }
        errors.remove(ValidationError.BUSINESS_HOURS.toString());
        return true;
    }

    public static boolean isDuringOperatingHours(ZonedDateTime time) {
        ZonedDateTime open = ZonedDateTime.of(time.toLocalDate(), LocalTime.of(8, 0), ZoneId.of(ZONE));
        ZonedDateTime close = ZonedDateTime.of(time.toLocalDate(), LocalTime.of(22, 0), ZoneId.of(ZONE));
        return !time.withZoneSameInstant(ZoneId.of(ZONE)).isBefore(open) && !time.withZoneSameInstant(ZoneId.of(ZONE)).isAfter(close);
    }

    /**
     * Checks if selected time is on a weekday.
     * @param errors Current list of errors to display in the UI.
     * @param date Date to check.
     * @return Boolean result of validation check.
     */
    public static boolean isOnWeekday(ObservableList<String> errors, ZonedDateTime date) {
        ZonedDateTime zdt = date.withZoneSameInstant(ZoneId.of(ZONE));
        if(zdt.getDayOfWeek().equals(DayOfWeek.SATURDAY) || zdt.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            FormHelper.addError(errors, ValidationError.WEEKEND.toString());
            return false;
        }
        errors.remove(ValidationError.WEEKEND.toString());
        return true;
    }

    /**
     * Checks if appointment has conflicts for the customer in the database.
     * @param newStart Start time of new appointment.
     * @param newEnd End time of new appointment.
     * @param oldStart Start time of existing appointment.
     * @param oldEnd End time of existing appointment.
     * @return Boolean result of check.
     */
    public static boolean hasAConflict(ZonedDateTime newStart, ZonedDateTime newEnd, ZonedDateTime oldStart, ZonedDateTime oldEnd) {
        if(newStart.equals(oldStart) || newEnd.equals(oldEnd)) return true;
        if(newStart.isAfter(oldStart) && newStart.isBefore(oldEnd)) return true;
        if(newEnd.isAfter(oldStart) && newEnd.isBefore(oldEnd)) return true;
        return newStart.isBefore(oldStart) && newEnd.isAfter(oldEnd);
    }

}

