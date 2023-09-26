package com.cbodo.schedule.util;

import java.sql.Statement;
import java.sql.ResultSet;

import static com.cbodo.schedule.util.JDBC.connection;

/**
 * This class acts as an interface between the client code and the database.
 */
public class Query {
    /**
     * ResultSet member.
     */
    private static ResultSet result;

    /**
     * Queries the database.
     * @param q sql statement to query the database with.
     */
    public static void query(String q){
        try{
            Statement statement = connection.createStatement();
            if(q.toLowerCase().startsWith("select"))
                result = statement.executeQuery(q);
            if(q.toLowerCase().startsWith("delete")|| q.toLowerCase().startsWith("insert")|| q.toLowerCase().startsWith("update"))
                statement.executeUpdate(q);
        }
        catch(Exception e){
            System.out.println("Error: "+e.getMessage());
        }
    }

    /**
     * Getter for ResultSet.
     * @return the stored ResultSet.
     */
    public static ResultSet getResult(){
        return result;
    }
}
