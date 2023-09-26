package com.cbodo.schedule.DAO;

import com.cbodo.schedule.model.User;
import com.cbodo.schedule.util.JDBC;
import com.cbodo.schedule.util.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class handles the interfacing between the database and User-related activities.
 */
public class UserDAO {
    /**
     * Sets a new user object from database query results.
     * @param rs ResultSet of database query.
     * @return A user object.
     * @throws SQLException For querying the database.
     */
    public static User setUser(ResultSet rs) throws SQLException {
        String userName = rs.getString("User_Name");
        String password = rs.getString("Password");
        return new User(userName, password);
    }

    /**
     * RETRIEVES user name from database.
     * @param id The user ID.
     * @return User name string.
     */
    public static String getUserName(int id) throws Exception {
        JDBC.openConnection();
        String sql = "SELECT * FROM users WHERE User_ID  = '" + id + "'";
        Query.query(sql);
        ResultSet rs = Query.getResult();
        String user = null;
        if(rs.next()) {
            user = rs.getString("User_Name");
        }
        JDBC.closeConnection();
        return user;
    }

    /**
     * RETRIEVES user using user's name for query.
     * @param name The user's name.
     * @return A user object.
     * @throws Exception For querying the database.
     */
    public static User getUser(String name) throws Exception {
        JDBC.openConnection();
        String sql = "select * FROM users WHERE User_Name  = '" + name + "'";
        Query.query(sql);
        ResultSet rs = Query.getResult();
        User user = null;
        if(rs.next()) {
            user = setUser(rs);
        }
        JDBC.closeConnection();
        return user;
    }

    /**
     * RETRIEVES user ID using user's name for query.
     * @param name The user's name.
     * @return The user's ID.
     * @throws SQLException For querying the database.
     */
    public static Integer getUserId(String name) throws Exception {
        JDBC.openConnection();
        String sql = "select * FROM users WHERE User_Name  = '" + name + "'";
        Query.query(sql);
        ResultSet rs = Query.getResult();
        Integer id = null;
        if(rs.next()) {
            id = rs.getInt("User_ID");
        }
        JDBC.closeConnection();
        return id;
    }

    /**
     * RETRIEVES all users currently in the database.
     * @return An ObservableList of users.
     * @throws Exception For querying the database.
     */
    public static ObservableList<User> getAllUsers() throws Exception {
        ObservableList<User> allUsers = FXCollections.observableArrayList();
        JDBC.openConnection();
        String sql = "select * from users";
        Query.query(sql);
        ResultSet rs = Query.getResult();
        while(rs.next()){
            allUsers.add(setUser(rs));
        }
        JDBC.closeConnection();
        return allUsers;
    }

    public static boolean userExists(String name) throws Exception {
        ObservableList<User> users = getAllUsers();
        for(User u:users) {
            if(u.getUserName().equals(name)) { return true; }
        }
        return false;
    }
}
