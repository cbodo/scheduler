package com.cbodo.schedule.DAO;

import com.cbodo.schedule.model.Appointment;
import com.cbodo.schedule.model.Customer;
import com.cbodo.schedule.model.Report;
import com.cbodo.schedule.util.JDBC;
import com.cbodo.schedule.util.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * This class handles the interfacing between the database and Reports-related activities.
 */
public class ReportsDAO {
    /**
     * A Calendar instance of the UTC time zone for inserting timestamps.
     */
    static Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    /**
     * Gets the average duration for a list of appointments.
     * @param appointments The list of appointments.
     * @return A string displaying the average duration.
     */
    public static String average(ObservableList<Appointment> appointments) {
        long total = 0;
        int size = 0;
        for(Appointment a : appointments) {
            total += Duration.between(a.getStart(), a.getEnd()).toMinutes();
            size += 1;
        }
        long average = 0;
        if(size != 0) {
            average = total / size;

            if (average > 60) {
                String hours;
                String minutes;
                hours = String.valueOf(average / 60);
                minutes = String.valueOf(average % 60);
                return hours + " hrs " + minutes + " mins";
            }
        }
        return average != 0 ? average + " mins" : "Not enough data.";
    }

    /**
     * Generates a report of average appointment durations for the specified customer.
     * @return The generated report.
     */
    public static String durationReport() throws Exception {
        ObservableList<Customer> customers = CustomerDAO.getAllCustomers();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%-20s%20s%20s\n\n",  "Customer", "Average", "Total"));

        for(Customer customer:customers) {
            ObservableList<Appointment> appointments = AppointmentDAO.getCustomerAppointments(customer.getCustomerId());
            stringBuilder.append(
                String.format(
                    "%-20s%20s%20s\n",
                    customer.getCustomerName(),
                    average(appointments),
                    appointments.size()
                )
            );
        }

        return stringBuilder.toString();
    }

    public static String buildReport() throws SQLException {
        JDBC.openConnection();
        String sql = "SELECT Start, Type, COUNT(appointments.Type) AS Total FROM appointments GROUP BY Start, Type;";
        Query.query(sql);
        ResultSet rs = Query.getResult();
        ObservableList<Report> reportItems = FXCollections.observableArrayList();
        while(rs.next()){
            Timestamp startDb = rs.getTimestamp("Start", utc);
            LocalDateTime start = ZonedDateTime.of(startDb.toLocalDateTime(), ZoneId.systemDefault()).toLocalDateTime();
            String month = start.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
            String type = rs.getString("Type");
            int total = rs.getInt("Total");
            Report r = new Report(month, type, total);
            if(reportItems.contains(r)) {
                r = reportItems.get(reportItems.indexOf(r));
                r.setTotal(r.getTotal() + 1);
            } else {
                reportItems.add(r);
            }
        }
        JDBC.closeConnection();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%-20s%-20s%20s\n\n",  "Month", "Type", "Total"));

        for(Report r:reportItems) {
            stringBuilder.append(r.toString());
        }
        return stringBuilder.toString();
    }
}
