package com.cbodo.schedule.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Helper class used to make dealing with dates easier.
 */
public class DateHelper {
    /**
     * Retrieves the LocalDateTime for monday in the current week.
     * @return LocalDateTime value of monday.
     */
    public static LocalDateTime monday() {
        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0));
        DayOfWeek day = today.getDayOfWeek();
        switch (day) {
            case MONDAY:
                return today.minusDays(0);
            case TUESDAY:
                return today.minusDays(1);
            case WEDNESDAY:
                return today.minusDays(2);
            case THURSDAY:
                return today.minusDays(3);
            case FRIDAY:
                return today.minusDays(4);
            case SATURDAY:
                return today.plusDays(2);
            case SUNDAY:
                return today.plusDays(1);
            default:
                throw new IllegalStateException("Unexpected value: " + today);
        }
    }
    /**
     * Retrieves the LocalDateTime for friday in the current week.
     * @return LocalDateTime value of friday.
     */
    public static LocalDateTime friday() {
        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59));
        DayOfWeek day = today.getDayOfWeek();
        switch (day) {
            case MONDAY:
                return today.plusDays(4);
            case TUESDAY:
                return today.plusDays(3);
            case WEDNESDAY:
                return today.plusDays(2);
            case THURSDAY:
                return today.plusDays(1);
            case FRIDAY:
                return today;
            case SATURDAY:
                return today.plusDays(7);
            case SUNDAY:
                return today.plusDays(6);
            default:
                throw new IllegalStateException("Unexpected value: " + today);
        }
    }

    /**
     * Retrieves the first day of the current month.
     * @return LocalDateTime value of the first day of month.
     */
    public static LocalDateTime firstOfMonth() {
        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0));
        return today.withDayOfMonth(1);
    }
    /**
     * Retrieves the last day of the current month.
     * @return LocalDateTime value of the last day of month.
     */
    public static LocalDateTime lastOfMonth() {
        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59));
        boolean leapYear = today.toLocalDate().isLeapYear();
        return today.withDayOfMonth(today.getMonth().length(leapYear));
    }
}
