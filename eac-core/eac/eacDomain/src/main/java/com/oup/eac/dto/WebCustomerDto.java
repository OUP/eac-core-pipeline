package com.oup.eac.dto;

import java.io.Serializable;

import com.oup.eac.domain.Customer;

/**
 * Used to cache Customer and webUserName inside ehcache for LoginWidget where the browser client is not participating in http sessions because of 3rd Party Cookies. 
 * 
 * @author David Hay
 *
 */
public class WebCustomerDto implements Serializable {

    private final Customer customer;

    private final String webUserName;

    public WebCustomerDto(Customer customer, String webUserName) {
        super();
        this.customer = customer;
        this.webUserName = webUserName;
    }

    public final Customer getCustomer() {
        return customer;
    }

    public final String getWebUserName() {
        return webUserName;
    }

    @Override
    public final int hashCode() { 
        return this.customer.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WebCustomerDto other = (WebCustomerDto) obj;
        if (customer == null) {
            if (other.customer != null)
                return false;
        } else if (!customer.getId().equals(other.customer.getId()))
            return false;
        return true;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" ");
        if(customer == null){
            sb.append("Customer[null]");
        }else{
            sb.append("Customer username[");
            sb.append(customer.getUsername());
            sb.append("]");
        }
        sb.append(" webUserName[");
        sb.append(webUserName);
        sb.append("]");
        return sb.toString();            
    }
}
