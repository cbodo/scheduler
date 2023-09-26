package com.cbodo.schedule.model;

/**
 * This class represents a model for items from the 'countries' database table.
 */
public class Country {
    /**
     * Country ID
     */
    Integer countryId;
    /**
     * Country name.
     */
    String country;

    /**
     * Country class constructor.
     */
    public Country(Integer countryId, String country) {
        this.countryId = countryId;
        this.country = country;
    }

    /**
     * Getter for country name.
     * @return Country name.
     */
    public String getCountry() {
        return country;
    }
}
