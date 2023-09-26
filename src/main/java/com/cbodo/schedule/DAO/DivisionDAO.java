package com.cbodo.schedule.DAO;

import com.cbodo.schedule.model.Division;
import com.cbodo.schedule.util.JDBC;
import com.cbodo.schedule.util.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class handles the interfacing between the database and Division-related activities.
 */
public class DivisionDAO {
    /**
     * Sets a new division object from database query results.
     * @param rs ResultSet of database query.
     * @return A division object.
     * @throws SQLException For querying the database.
     */
    public static Division setDivision(ResultSet rs) throws SQLException {
        int divisionId = rs.getInt("Division_ID");
        String division = rs.getString("Division");
        int countryId = rs.getInt("Country_ID");
        return new Division(divisionId, division, countryId);
    }

    /**
     * RETRIEVES division using division's ID for query.
     * @param id The division's ID.
     * @return A division object.
     * @throws Exception For querying the database.
     */
    public static Division getDivision(int id) throws Exception {
        JDBC.openConnection();
        String sql = "select * FROM first_level_divisions WHERE Division_ID  = '" + id + "'";
        Query.query(sql);
        ResultSet rs = Query.getResult();
        Division division = null;
        if(rs.next()) {
            division = setDivision(rs);
        }
        JDBC.closeConnection();
        return division;
    }

    /**
     * RETRIEVES division ID using division name for query.
     * @param name The division's name.
     * @return The division's ID.
     * @throws SQLException For querying the database.
     */
    public static Integer getDivisionId(String name) throws Exception {
        JDBC.openConnection();
        String sql = "select * FROM first_level_divisions WHERE Division = '" + name + "'";
        Query.query(sql);
        ResultSet rs = Query.getResult();
        Integer id = null;
        if(rs.next()) {
            id = rs.getInt("Division_ID");
        }
        JDBC.closeConnection();
        return id;
    }

    /**
     * RETRIEVES all divisions currently in the database.
     * @return An ObservableList of divisions.
     * @throws Exception For querying the database.
     */
    public static ObservableList<Division> getAllDivisions() throws Exception {
        ObservableList<Division> allDivisions = FXCollections.observableArrayList();
        JDBC.openConnection();
        String sql = "select * from first_level_divisions";
        Query.query(sql);
        ResultSet rs = Query.getResult();
        while(rs.next()){
            allDivisions.add(setDivision(rs));
        }
        JDBC.closeConnection();
        return allDivisions;
    }

    /**
     * RETRIEVES all divisions located in the indicated country.
     * @return An ObservableList of divisions.
     * @throws SQLException For querying the database.
     */
    public static ObservableList<Division> getDivisionsInCountry(int countryId) throws SQLException {
        ObservableList<Division> divisions = FXCollections.observableArrayList();
        JDBC.openConnection();
        String sql = "select * FROM first_level_divisions WHERE Country_ID = '" + countryId + "'";
        Query.query(sql);
        ResultSet rs = Query.getResult();
        while(rs.next()) {
            divisions.add(setDivision(rs));
        }
        JDBC.closeConnection();
        return divisions;
    }
}
