package com.cbodo.schedule.DAO;

import com.cbodo.schedule.model.Customer;
import com.cbodo.schedule.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.Calendar;
import java.util.TimeZone;

import static com.cbodo.schedule.Main.CURRENT_USER;
import static com.cbodo.schedule.util.JDBC.connection;

/**
 * This class handles the interfacing between the database and Customer-related activities.
 */
public class CustomerDAO {
    /**
     * A Calendar instance of the UTC time zone for inserting timestamps.
     */
    static Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    /**
     * Generates an ID for a new customer, using up any empty IDs left by deleted records.
     * @return The new ID.
     * @throws Exception For querying the database.
     */
    public static int getNewId() throws Exception {
        ObservableList<Customer> customers = getAllCustomers();
        int i = 0;
        for(Customer customer : customers) {
            if(i+1 != customer.getCustomerId()) {
                return i+1;
            }
            if(i == customers.size() - 1) {
                if(i == customer.getCustomerId()) {
                    return i + 2;
                }
            }
            i += 1;
        }
        return customers.size() + 1;
    }

    /**
     * CREATES a new customer record in the database.
     * @param customer A customer object to insert into database.
     * @return Boolean result of insertion.
     */
    public static boolean createCustomer(Customer customer) {
        JDBC.openConnection();
        PreparedStatement ps;
        try {
            String sql = "INSERT INTO customers (" +
                    "Customer_ID, " +
                    "Customer_Name, " +
                    "Address, " +
                    "Postal_Code, " +
                    "Phone, " +
                    "Division_ID, " +
                    "Create_Date, " +
                    "Created_By, " +
                    "Last_Update, " +
                    "Last_Updated_By) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, customer.getCustomerId());
            ps.setString(2, customer.getCustomerName());
            ps.setString(3, customer.getAddress());
            ps.setString(4, customer.getPostalCode());
            ps.setString(5, customer.getPhone());
            ps.setInt(6, customer.getDivisionId());
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()), utc);
            ps.setString(8, CURRENT_USER.getUserName());
            ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()), utc);
            ps.setString(10, CURRENT_USER.getUserName());
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
     * UPDATES an existing customer in the database.
     * @param customer A customer object to update in the database.
     * @return Boolean result of the update.
     */
    public static boolean updateCustomer(Customer customer) {
        JDBC.openConnection();
        PreparedStatement ps;
        try {
            String sql = "UPDATE customers SET " +
                    "Customer_Name = ?, " +
                    "Address = ?, " +
                    "Postal_Code = ?, " +
                    "Phone = ?, " +
                    "Division_ID = ?, " +
                    "Last_Update = ?, " +
                    "Last_Updated_By = ? "  +
                    "WHERE Customer_ID = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, customer.getCustomerName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostalCode());
            ps.setString(4, customer.getPhone());
            ps.setInt(5, customer.getDivisionId());
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()), utc);
            ps.setString(7, CURRENT_USER.getUserName());
            ps.setInt(8, customer.getCustomerId());
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
     * DELETES a customer from the database.
     * @param id ID of customer to delete.
     * @return Boolean result of deletion.
     * @throws SQLException For querying the database.
     */
    public static boolean deleteCustomer(int id) throws SQLException {
        JDBC.openConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM customers WHERE Customer_ID = ?");
        ps.setInt(1, id);
        int rowsAffected = ps.executeUpdate();
        JDBC.closeConnection();
        return rowsAffected == 1;
    }

    /**
     * Sets a new customer object from database query results.
     * @param rs ResultSet of database query.
     * @return A customer object.
     * @throws SQLException For querying the database.
     */
    public static Customer setCustomer(ResultSet rs) throws SQLException {
        Integer customerId = rs.getInt("Customer_ID");
        String customerName = rs.getString("Customer_Name");
        String address = rs.getString("Address");
        String postalCode = rs.getString("Postal_Code");
        String phone = rs.getString("Phone");
        Integer divisionId = rs.getInt("Division_ID");
        return new Customer(customerId, customerName, address, postalCode, phone, divisionId);
    }

    /**
     * RETRIEVES customer name from database.
     * @param id The customer ID.
     * @return Customer name string.
     */
    public static String getCustomerName(int id) throws SQLException {
        JDBC.openConnection();
        String sql = "select * FROM customers WHERE Customer_ID  = '" + id + "'";
        Query.query(sql);
        ResultSet rs = Query.getResult();
        String customer = null;
        if(rs.next()) {
            customer = rs.getString("Customer_Name");
        }
        JDBC.closeConnection();
        return customer;
    }

    /**
     * RETRIEVES customer ID using customer name for query.
     * @param name The customer's name.
     * @return The customer's ID.
     * @throws SQLException For querying the database.
     */
    public static Integer getCustomerId(String name) throws SQLException {
        JDBC.openConnection();
        String sql = "select * FROM customers WHERE Customer_Name  = '" + name + "'";
        Query.query(sql);
        ResultSet rs = Query.getResult();
        Integer id = null;
        if(rs.next()) {
            id = rs.getInt("Customer_ID");
        }
        JDBC.closeConnection();
        return id;
    }

    /**
     * RETRIEVES all customers currently in the database.
     * @return An ObservableList of customers.
     * @throws SQLException For querying the database.
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        JDBC.openConnection();
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customers";
        Query.query(sql);
        ResultSet rs = Query.getResult();
        while(rs.next()){
            customers.add(setCustomer(rs));
        }
        JDBC.closeConnection();
        return customers;
    }


}
