package com.cbodo.schedule.DAO;

import com.cbodo.schedule.model.Contact;
import com.cbodo.schedule.util.JDBC;
import com.cbodo.schedule.util.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class handles the interfacing between the database and Contact-related activities.
 */
public class ContactDAO {
    /**
     * Sets a new Contact object
     * @param rs A database query ResultSet.
     * @return The Contact object.
     * @throws SQLException For querying the database.
     */
    public static Contact setContact(ResultSet rs) throws SQLException {
        int contactId = rs.getInt("Contact_ID");
        String contactName = rs.getString("Contact_Name");
        String contactEmail = rs.getString("Email");
        return new Contact(contactId, contactName, contactEmail);

    }

    /**
     * RETRIEVES contact name from database.
     * @param id The contact ID.
     * @return Contact name string.
     */
    public static String getContactName(int id) throws Exception {
        JDBC.openConnection();
        String sql = "SELECT * FROM contacts WHERE Contact_ID  = '" + id + "'";
        Query.query(sql);
        ResultSet rs = Query.getResult();
        String contact = null;
        if(rs.next()) {
            contact = rs.getString("Contact_Name");
        }
        JDBC.closeConnection();
        return contact;
    }

    /**
     * RETRIEVES contact ID from database using contact name for query.
     * @param name Contact to find.
     * @return The contact ID.
     * @throws Exception For querying the database.
     */
    public static Integer getContactId(String name) throws Exception {
        JDBC.openConnection();
        String sql = "SELECT * FROM contacts WHERE Contact_Name  = '" + name + "'";
        Query.query(sql);
        ResultSet rs = Query.getResult();
        Integer id = null;
        if(rs.next()) {
            id = rs.getInt("Contact_ID");
        }
        JDBC.closeConnection();
        return id;
    }

    /**
     * RETRIEVES all contacts in the database.
     * @return An ObservableList of contacts.
     * @throws Exception For querying the database.
     */
    public static ObservableList<Contact> getAllContacts() throws Exception {
        JDBC.openConnection();
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        String sql = "SELECT * FROM contacts";
        Query.query(sql);
        ResultSet rs = Query.getResult();
        while(rs.next()){
            contacts.add(setContact(rs));
        }
        JDBC.closeConnection();
        return contacts;
    }
}
