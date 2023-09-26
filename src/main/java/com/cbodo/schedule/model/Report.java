package com.cbodo.schedule.model;

import java.util.Objects;

/**
 * This class represents a model for reports.
 */
public class Report {
    /**
     * Month string.
     */
    String month;
    /**
     * Type string.
     */
    String type;
    /**
     * Total integer.
     */
    int total;

    /**
     * Class constructor.
     */
    public Report(String month, String type, int total) {
        this.month = month;
        this.type = type;
        this.total = total;
    }

    /**
     * Getter for total.
     * @return Total.
     */
    public int getTotal() {
        return this.total;
    }

    /**
     * Setter for total.
     * @param total Total to set.
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * Overrides base class toString() method.
     */
    @Override
    public String toString() {
        return String.format("%-20s%-20s%20s\n", month, type, total);
    }
    /**
     * Overrides base class equals() method.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(month, report.month) && Objects.equals(type, report.type);
    }
    /**
     * Overrides base class hashCode() method.
     */
    @Override
    public int hashCode() {
        return Objects.hash(month, type, total);
    }
}
