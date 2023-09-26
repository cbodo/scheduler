package com.cbodo.schedule.util;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Helper class used to display date and time formats in the UI.
 */
public class DateFormatHelper {
    /**
     * Format for date and time.
     * @return DateTimeFormatter object.
     */
    public static DateTimeFormatter dateTimeFormat() {
        boolean isInUS = Locale.getDefault().getDisplayCountry().equalsIgnoreCase("united states");
        String format = isInUS ? "MM/dd/yyyy h:mm a" : "dd/MM/yyyy HH:mm";
        return DateTimeFormatter.ofPattern(format);
    }
    /**
     * Format for date.
     * @return DateTimeFormatter object.
     */
    public static DateTimeFormatter dateFormat() {
        boolean isInUS = Locale.getDefault().getDisplayCountry().equalsIgnoreCase("united states");
        String format = isInUS ? "MM/dd/yyyy" : "dd/MM/yyyy";
        return DateTimeFormatter.ofPattern(format);
    }
    /**
     * Format for time.
     * @return DateTimeFormatter object.
     */
    public static DateTimeFormatter timeFormat() {
        boolean isInUS = Locale.getDefault().getDisplayCountry().equalsIgnoreCase("united states");
        String format = isInUS ? "h:mm a" : "HH:mm";
        return DateTimeFormatter.ofPattern(format);
    }
}
