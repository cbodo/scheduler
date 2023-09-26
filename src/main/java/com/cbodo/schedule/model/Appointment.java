package com.cbodo.schedule.model;

import com.cbodo.schedule.util.DateFormatHelper;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * This class represents a model for items from the 'appointments' database table.
 */
public class Appointment {
    /**
     * The appointment ID.
     */
    private int appointmentId;
    /**
     * The appointment title.
     */
    private String title;
    /**
     * The appointment description.
     */
    private String description;
    /**
     * The appointment location.
     */
    private String location;
    /**
     * The appointment type.
     */
    private String type;
    /**
     * The appointment start time.
     */
    private LocalDateTime start;
    /**
     * The appointment end time.
     */
    private LocalDateTime end;
    /**
     * The appointment's customer ID.
     */
    private int customerId;
    /**
     * The appointment's user ID.
     */
    private int userId;
    /**
     * The appointment's contact ID.
     */
    private int contactId;

    /**
     * Default Appointment constructor.
     */
    public Appointment() {
    }

    /**
     * Appointment class constructor.
     */
    public Appointment(int appointmentId, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    /**
     * Getter for appointment ID.
     * @return Appointment ID.
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Getter for title.
     * @return Title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for description.
     * @return Description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for location.
     * @return Location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Getter for type.
     * @return Type.
     */
    public String getType() {
        return type;
    }

    /**
     * Getter for start.
     * @return Start.
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Getter for end.
     * @return End.
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Getter for customer ID.
     * @return Customer ID.
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Getter for user ID.
     * @return User ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Getter for contact ID.
     * @return Contact ID.
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Overrides base class toString() method.
     */
    @Override
    public String toString() {
        return "#" +
                this.appointmentId +
                " " +
                this.title +
                " " +
                this.type +
                " " +
                this.start.format(DateFormatHelper.dateTimeFormat()) +
                " - " +
                this.end.format(DateFormatHelper.dateTimeFormat());
    }

    /**
     * Overrides base class equals() method.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return customerId == that.customerId && userId == that.userId && contactId == that.contactId && Objects.equals(title, that.title) && Objects.equals(description, that.description) && Objects.equals(location, that.location) && Objects.equals(type, that.type) && Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    /**
     * Overrides base class hashCode() method.
     */
    @Override
    public int hashCode() {
        return Objects.hash(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);
    }
}
