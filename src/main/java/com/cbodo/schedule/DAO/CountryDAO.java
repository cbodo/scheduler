package com.cbodo.schedule.DAO;

import com.cbodo.schedule.model.Country;
import com.cbodo.schedule.util.JDBC;
import com.cbodo.schedule.util.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class handles the interfacing between the database and Country-related activities.
 */
public class CountryDAO {
    /**
     * Sets a new Country object.
     * @param rs A database query ResultSet.
     * @return A country object.
     * @throws SQLException For querying the database.
     */
    public static Country setCountry(ResultSet rs) throws SQLException {
        int countryId = rs.getInt("Country_ID");
        String country = rs.getString("Country");
        return new Country(countryId, country);
    }

    /**
     * RETRIEVES country from database using country's ID for query.
     * @param id The country's ID.
     * @return A country object.
     * @throws Exception For querying the database.
     */
    public static Country getCountry(int id) throws Exception {
        JDBC.openConnection();
        String sql = "select * FROM countries WHERE Country_ID = '" + id + "'";
        Query.query(sql);
        ResultSet rs = Query.getResult();
        Country country = null;
        if(rs.next()) {
            country = setCountry(rs);
        }
        JDBC.closeConnection();
        return country;
    }

    /**
     * RETRIEVES country ID using the country's name for query.
     * @param name The country's name.
     * @return The country's ID.
     * @throws Exception For querying the database.
     */
    public static Integer getCountryId(String name) throws Exception {
        JDBC.openConnection();
        String sql = "select * FROM countries WHERE Country = '" + name + "'";
        Query.query(sql);
        ResultSet rs = Query.getResult();
        Integer id = null;
        if(rs.next()) {
            id = rs.getInt("Country_ID");
        }
        JDBC.closeConnection();
        return id;
    }

    /**
     * RETRIEVES all countries in the database.
     * @return An ObservableList of countries.
     * @throws Exception For querying the database.
     */
    public static ObservableList<Country> getAllCountries() throws Exception {
        ObservableList<Country> allCountries = FXCollections.observableArrayList();
        JDBC.openConnection();
        String sql = "select * from countries";
        Query.query(sql);
        ResultSet rs = Query.getResult();
        while(rs.next()){
            allCountries.add(setCountry(rs));
        }
        JDBC.closeConnection();
        return allCountries;
    }



}
