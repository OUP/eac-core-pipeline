package com.oup.eac.domain.migrationtool;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.UpdatedAudit;

/**
 * @author Chirag Joshi
 */
@Entity
public class CustomerMigration extends UpdatedAudit implements BaseMigration<CustomerMigrationState>{

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id")
    private CustomerMigrationData data;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable=false, name="migration_state")
    private CustomerMigrationState state;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eac_customer_id", nullable = true)    
    private Customer customer;
    
    public CustomerMigrationState getState() {
        return state;
    }

    public void setState(CustomerMigrationState state) {
        this.state = state;
    }

    public void setNextState() {
        this.state = this.state.next();
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CustomerMigrationData getData() {
        return data;
    }

    public void setData(CustomerMigrationData data) {
        this.data = data;
    }

}
