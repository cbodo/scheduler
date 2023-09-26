package com.cbodo.schedule.model;

import java.util.Objects;

/**
 * This class represents a model for items from the 'customers' database table.
 */
public class Customer {
    /**
     * Customer ID.
     */
    private Integer customerId;
    /**
     * Customer name.
     */
    private String customerName;
    /**
     * Customer address.
     */
    private String address;
    /**
     * Customer postal code.
     */
    private String postalCode;
    /**
     * Customer phone.
     */
    private String phone;
    /**
     * Customer's first-level division id.
     */
    private Integer divisionId;

    /**
     * Default Customer constructor.
     */
    public Customer() {
    }

    /**
     * Customer class constructor.
     */
    public Customer(Integer customerId, String customerName, String address, String postalCode, String phone, Integer divisionId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
    }

    /**
     * Getter for customer ID
     * @return Customer ID.
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * Getter for customer name.
     * @return Customer name.
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Getter for address.
     * @return Address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Getter for postal code.
     * @return Postal code.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Getter for phone.
     * @return Phone.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Getter for division ID.
     * @return Division ID.
     */
    public Integer getDivisionId() {
        return divisionId;
    }

    /**
     * Overrides base class toString() method.
     */
    @Override
    public String toString() {
        return this.getCustomerId() + " " + this.getCustomerName();
    }

    /**
     * Overrides base class equals() method.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerName, customer.customerName) && Objects.equals(address, customer.address) && Objects.equals(postalCode, customer.postalCode) && Objects.equals(phone, customer.phone) && Objects.equals(divisionId, customer.divisionId);
    }

    /**
     * Overrides base class hashCode() method.
     */
    @Override
    public int hashCode() {
        return Objects.hash(customerName, address, postalCode, phone, divisionId);
    }
}
