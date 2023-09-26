package com.cbodo.schedule.util;

import static com.cbodo.schedule.util.FormValidator.operatingHours;

/**
 * Stores strings used in displaying error messages in the UI.
 */
public enum ValidationError {
    BUSINESS_HOURS ("Appointment must be scheduled during operating hours " + operatingHours + "."),
    CONFLICT ("Customer has the following conflicts:\n"),
    EMPTY (" is empty."),
    TIME_ORDER ("Start time is not before end time."),
    WEEKEND ("Appointment must be scheduled during the week.");
    /**
     * Error message string.
     */
    private final String message;

    /**
     * Class constructor.
     * @param message error message string.
     */
    ValidationError(String message) {
        this.message = message;
    }

    /**
     * Overrides toString() method.
     * @return String value of message member.
     */
    @Override
    public String toString() {
        return message;
    }
}
