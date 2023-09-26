package com.cbodo.schedule.DAO;

import com.cbodo.schedule.model.Appointment;
import com.cbodo.schedule.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.*;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.cbodo.schedule.Main.CURRENT_USER;
import static com.cbodo.schedule.util.FormValidator.ZONE;
import static com.cbodo.schedule.util.JDBC.connection;

/**
 * This class handles the interfacing between the database and Appointment-related activities.
 */
public class AppointmentDAO {
    /**
     * A Calendar instance of the UTC time zone for inserting timestamps.
     */
    static Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    /**
     * Generates an ID for a new appointment, using up empty IDs left by deleted records.
     * @return The new ID.
     * @throws Exception For querying the database.
     */
    public static int getNewId() throws Exception {
        ObservableList<Appointment> appointments = getAllAppointments();
        int i = 0;
        for(Appointment appointment : appointments) {
            if(i+1 != appointment.getAppointmentId()) {
                return i+1;
            }
            if(i == appointments.size() - 1) {
                if(i <= appointment.getAppointmentId()) {
                    return appointment.getAppointmentId() + 1;
                }
            }
            i += 1;
        }
        return appointments.size() + 1;
    }

    /**
     * CREATES a new appointment into the database using a PreparedStatement.
     * @param appointment An appointment object to insert into the database.
     * @return Boolean result of insertion.
     */
    public static boolean createAppointment(Appointment appointment) {
        PreparedStatement ps;
        JDBC.openConnection();
        try {
            String sql = "INSERT INTO appointments " +
                    "(" +
                    "Appointment_ID, " +
                    "Title, " +
                    "Description, " +
                    "Location, " +
                    "Type, " +
                    "Start, " +
                    "End, " +
                    "Customer_ID, " +
                    "User_ID, " +
                    "Contact_ID, " +
                    "Create_Date, " +
                    "Created_By, " +
                    "Last_Update, " +
                    "Last_Updated_By" +
                    ") "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, appointment.getAppointmentId());
            ps.setString(2, appointment.getTitle());
            ps.setString(3, appointment.getDescription());
            ps.setString(4, appointment.getLocation());
            ps.setString(5, appointment.getType());
            ps.setTimestamp(6, Timestamp.valueOf(appointment.getStart()), utc);
            ps.setTimestamp(7, Timestamp.valueOf(appointment.getEnd()), utc);
            ps.setInt(8, appointment.getCustomerId());
            ps.setInt(9, appointment.getUserId());
            ps.setInt(10, appointment.getContactId());
            ps.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()), utc);
            ps.setString(12, CURRENT_USER.getUserName());
            ps.setTimestamp(13, Timestamp.valueOf(LocalDateTime.now()), utc);
            ps.setString(14, CURRENT_USER.getUserName());
            int rowsAffected = ps.executeUpdate();
            JDBC.closeConnection();
            return rowsAffected == 1;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JDBC.closeConnection();
        return false;
    }

    /**
     * Sets a new appointment object from database query results.
     * @param rs ResultSet of database query.
     * @return An appointment object.
     * @throws SQLException For querying the database.
     */
    public static Appointment setAppointmentObject(ResultSet rs) throws SQLException {
        Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        int appointmentId = rs.getInt("Appointment_ID");
        String title = rs.getString("Title");
        String description = rs.getString("Description");
        String location = rs.getString("Location");
        String type = rs.getString("Type");
        Timestamp startDb = rs.getTimestamp("Start", utc);
        LocalDateTime start = ZonedDateTime.of(startDb.toLocalDateTime(), ZoneId.systemDefault()).toLocalDateTime();
        Timestamp endDb = rs.getTimestamp("End", utc);
        LocalDateTime end = ZonedDateTime.of(endDb.toLocalDateTime(), ZoneId.systemDefault()).toLocalDateTime();
        int customerId = rs.getInt("Customer_ID");
        int userId = rs.getInt("User_ID");
        int contactId = rs.getInt("Contact_ID");
        return new Appointment(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);
    }

    /**
     * RETRIEVES all appointments currently in the database.
     * @return An ObservableList of appointments.
     * @throws Exception For querying the database.
     */
    public static ObservableList<Appointment> getAllAppointments() throws Exception {
        JDBC.openConnection();
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments";
        Query.query(sql);
        ResultSet rs = Query.getResult();
        while(rs.next()){
            appointments.add(setAppointmentObject(rs));
        }
        JDBC.closeConnection();
        return appointments;
    }

    /**
     * RETRIEVES all appointment data related to a specified customer.
     * @param id ID of the specified customer.
     * @return An ObservableList of appointments.
     * @throws Exception For querying the database.
     */
    public static ObservableList<Appointment> getCustomerAppointments(Integer id) throws Exception {
        if(id == null) return null;
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        JDBC.openConnection();

        String sql = "SELECT * FROM appointments WHERE Customer_ID  = '" + id + "'";

        Query.query(sql);
        ResultSet rs = Query.getResult();

        while(rs.next()) {
            appointments.add(setAppointmentObject(rs));
        }
        JDBC.closeConnection();
        return appointments;
    }

    /**
     * RETRIEVES any appointments that conflict with a specified appointment.
     * @param appointment The specified appointment.
     * @return An ObservableList of appointments.
     * @throws Exception For querying the database.
     */
    public static ObservableList<Appointment> getCustomerConflicts(Appointment appointment) throws Exception {
        JDBC.openConnection();
        ObservableList<Appointment> customerAppointments = getCustomerAppointments(appointment.getCustomerId());
        ObservableList<Appointment> conflicts = FXCollections.observableArrayList();

        ZonedDateTime start = appointment.getStart().atZone(ZoneId.of(ZONE));
        ZonedDateTime end = appointment.getEnd().atZone(ZoneId.of(ZONE));

        if(customerAppointments == null) return null;

        for(Appointment conflict : customerAppointments) {
            if(conflict.getAppointmentId() != appointment.getAppointmentId()) {
                ZonedDateTime conflictStart = conflict.getStart().atZone(ZoneId.of(ZONE));
                ZonedDateTime conflictEnd = conflict.getEnd().atZone(ZoneId.of(ZONE));

                if (FormValidator.hasAConflict(start, end, conflictStart, conflictEnd)) {
                    conflicts.add(conflict);
                }
            }
        }

        return conflicts;
    }

    /**
     * UPDATES an existing appointment in the database.
     * @param appointment An appointment object to update in the database.
     * @return Boolean result of the update.
     */
    public static boolean updateAppointment(Appointment appointment) {
        PreparedStatement ps;
        JDBC.openConnection();
        try {
            String sql = "UPDATE appointments SET " +
                    "Title = ?, " +
                    "Description = ?, " +
                    "Location = ?, " +
                    "Type = ?, " +
                    "Start = ?, " +
                    "End = ?, " +
                    "Customer_ID = ?, " +
                    "User_ID = ?, " +
                    "Contact_ID = ?, " +
                    "Last_Update = ?, " +
                    "Last_Updated_By = ? " +
                    "WHERE Appointment_ID = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getType());
            ps.setTimestamp(5, Timestamp.valueOf(appointment.getStart()), utc);
            ps.setTimestamp(6, Timestamp.valueOf(appointment.getEnd()), utc);
            ps.setInt(7, appointment.getCustomerId());
            ps.setInt(8, appointment.getUserId());
            ps.setInt(9, appointment.getContactId());
            ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()), utc);
            ps.setString(11, CURRENT_USER.getUserName());
            ps.setInt(12, appointment.getAppointmentId());
            int rowsAffected = ps.executeUpdate();
            JDBC.closeConnection();
            return rowsAffected == 1;
        } catch (Exception ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        JDBC.closeConnection();
        return false;
    }

    /**
     * DELETES appointment from the database.
     * @param id ID of appointment to delete.
     * @return Boolean result of deletion.
     * @throws SQLException For querying the database.
     */
    public static boolean deleteAppointment(int id) throws SQLException {
        JDBC.openConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM appointments WHERE Appointment_ID = ?");
        ps.setInt(1, id);
        int rowsAffected = ps.executeUpdate();
        JDBC.closeConnection();
        return rowsAffected == 1;
    }

    /**
     * Gets a string of all appointments in an ObservableList of appointments.
     * @param appointments The ObservableList of appointments.
     * @return The string of appointments.
     */
    public static String showAppointments(ObservableList<Appointment> appointments) {
        ObservableList<String> list = FXCollections.observableArrayList();
        for (Appointment appointment : appointments) {
            list.add(appointment.toString());
        }
        return FormHelper.listToString(list);
    }

}
