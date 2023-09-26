package com.cbodo.schedule.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Class for handling database connection.
 */
public class JDBC {
    /**
     * DB protocol
     */
    private static final String protocol = "jdbc";
    /**
     * DB vendor
     */
    private static final String vendor = ":mysql:";
    /**
     * DB location address
     */
    private static final String location = "//localhost:3306/";
    /**
     * DB name
     */
    private static final String databaseName = "client_schedule";
    /**
     * DB URL
     */
    private static final String url = protocol + vendor + location + databaseName + "?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false?connectionTimeZone=SERVER";
    /**
     * DB driver reference
     */
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    /**
     * DB username
     */
    private static final String userName = "sqlUser";
    /**
     * DB password
     */
    private static final String password = "Passw0rd!";
    /**
     * DB connection
     */
    public static Connection connection;

    /**
     * Opens the database connection.
     */
    public static void openConnection()
    {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, userName, password);
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /**
     * Closes the database connection.
     */
    public static void closeConnection() {
        try {
            connection.close();
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }
}
