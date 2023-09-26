package com.cbodo.schedule.model;

/**
 * This class represents a model for items from the 'first_level_divisions' database table.
 */
public class Division {
    /**
     * Division ID.
     */
    Integer divisionId;
    /**
     * Division name.
     */
    String division;
    /**
     * Country ID.
     */
    Integer countryId;

    /**
     * Division class constructor.
     */
    public Division(Integer divisionId, String division, Integer countryId) {
        this.divisionId = divisionId;
        this.division = division;
        this.countryId = countryId;
    }

    /**
     * Getter for division name.
     * @return Division name.
     */
    public String getDivision() {
        return division;
    }

    /**
     * Getter for country ID.
     * @return Country ID.
     */
    public Integer getCountryId() {
        return countryId;
    }

}
