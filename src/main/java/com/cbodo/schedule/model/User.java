package com.cbodo.schedule.model;

/**
 * This class represents a model for items from the 'users' database table.
 */
public class User {
    /**
     * User name.
     */
    private final String userName;
    /**
     * User password.
     */
    private final String password;

    /**
     * User class constructor.
     */
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    /**
     * Getter for user name.
     * @return User name.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Getter for user password.
     * @return User password.
     */
    public String getPassword() {
        return password;
    }
}
