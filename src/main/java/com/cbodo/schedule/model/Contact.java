package com.cbodo.schedule.model;

/**
 * This class represents a model for items from the 'contacts' database table.
 */
public class Contact {
    /**
     * Contact ID.
     */
    int contactId;
    /**
     * Contact name.
     */
    String contactName;
    /**
     * Contact email.
     */
    String contactEmail;

    /**
     * Contact class constructor.
     */
    public Contact(int contactId, String contactName, String contactEmail) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /**
     * Getter for contact name.
     * @return Contact name.
     */
    public String getContactName() {
        return contactName;
    }
}
