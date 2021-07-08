/**
 * 
 */
package com.oup.eac.dto;

import com.oup.eac.domain.Customer;

/**
 * @author harlandd
 * 
 */
public class CustomerSessionDto {

    private Customer customer;

    private String session;

    /**
     * Default constructor.
     */
    public CustomerSessionDto() {
        super();
    }

    /**
     * @param customer
     *            the customer
     * @param session
     *            the session
     */
    public CustomerSessionDto(final Customer customer, final String session) {
        super();
        this.customer = customer;
        this.session = session;
    }

    /**
     * @return the customer
     */
    public final Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer
     *            the customer to set
     */
    public final void setCustomer(final Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the session
     */
    public final String getSession() {
        return session;
    }

    /**
     * @param session
     *            the session to set
     */
    public final void setSession(final String session) {
        this.session = session;
    }

}
