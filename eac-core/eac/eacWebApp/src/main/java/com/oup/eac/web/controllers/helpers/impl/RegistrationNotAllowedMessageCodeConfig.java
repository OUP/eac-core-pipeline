package com.oup.eac.web.controllers.helpers.impl;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Product.ProductState;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.web.controllers.authentication.AuthenticationWorkFlow;

/**
 * 
 * @author David Hay
 *
 */
public class RegistrationNotAllowedMessageCodeConfig {

    private Customer.CustomerType customerType;
    private RegisterableProduct.RegisterableType registerableType;
    private AuthenticationWorkFlow.RegistrationNotAllowedReason notAllowedReason;
    private ProductState lifecycleState;
    
    public RegistrationNotAllowedMessageCodeConfig() {
    }
    
    public void setCustomerType(Customer.CustomerType customerType) {
        this.customerType = customerType;
    }
    public void setRegisterableType(RegisterableProduct.RegisterableType registerableType) {
        this.registerableType = registerableType;
    }
    public void setNotAllowedReason(AuthenticationWorkFlow.RegistrationNotAllowedReason notAllowedReason) {
        this.notAllowedReason = notAllowedReason;
    }
    public void setLifecycleState(ProductState lifecycleState) {
        this.lifecycleState = lifecycleState;
    }
    public Customer.CustomerType getCustomerType() {
        return customerType;
    }
    public RegisterableProduct.RegisterableType getRegisterableType() {
        return registerableType;
    }
    public AuthenticationWorkFlow.RegistrationNotAllowedReason getNotAllowedReason() {
        return notAllowedReason;
    }
    public ProductState getLifecycleState() {
        return lifecycleState;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof RegistrationNotAllowedMessageCodeConfig == false){
            return false;
        }
        RegistrationNotAllowedMessageCodeConfig other = (RegistrationNotAllowedMessageCodeConfig)o;
        boolean result = new EqualsBuilder()
            .append(other.getNotAllowedReason(), this.getNotAllowedReason())
            .append(other.getRegisterableType(), this.getRegisterableType())
            .append(other.getLifecycleState(), this.getLifecycleState())
            .append(other.getCustomerType(), this.getCustomerType())
            .isEquals();
            
        return result;
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.notAllowedReason)
            .append(this.registerableType)
            .append(this.lifecycleState)
            .append(this.customerType)
            .toHashCode();
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this).toString();
    }
}
