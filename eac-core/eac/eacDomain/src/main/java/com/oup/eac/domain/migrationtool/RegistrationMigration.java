package com.oup.eac.domain.migrationtool;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.UpdatedAudit;

@Entity
public class RegistrationMigration extends UpdatedAudit implements BaseMigration<RegistrationMigrationState>{

	
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="registration_migration_data_id")
    private RegistrationMigrationData data;
    
     
    
    @Enumerated(EnumType.STRING)
    @Column(nullable=false, name="migration_state")
    private RegistrationMigrationState state;
    
   
    
    @ManyToOne(fetch = FetchType.LAZY)    
    @JoinColumn(name = "eac_registration_id", nullable = true)
    private Registration<ProductRegistrationDefinition> registration;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_migration_id", nullable = false)    
    private CustomerMigration customerMigration;
    
    public RegistrationMigrationState getState() {
        return state;
    }

    public void setState(RegistrationMigrationState state) {
        this.state = state;
    }

    public void setNextState() {
        this.state = this.state.next();
    }

  
    public RegistrationMigrationData getData() {
        return data;
    }

    public void setData(RegistrationMigrationData data) {
        this.data = data;
    }

	public CustomerMigration getCustomerMigration() {
		return customerMigration;
	}

	public void setCustomerMigration(CustomerMigration customerMigration) {
		this.customerMigration = customerMigration;
	}

	
	public Registration<ProductRegistrationDefinition> getRegistration() {
		return registration;
	}

	public void setRegistration(
			Registration<ProductRegistrationDefinition> registration) {
		this.registration = registration;
	}
	
    public RegistrationMigration(){
        super();
    }

	
	public RegistrationMigration(CustomerMigration cm, RegistrationMigrationData data){        
        setState(RegistrationMigrationState.INITIAL);
        setData(data);
        setCustomerMigration(cm);
    }

	
}
