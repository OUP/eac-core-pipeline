package com.oup.eac.service.migrationtool;

import java.util.List;
import java.util.Map;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.migrationtool.CustomerMigration;
import com.oup.eac.domain.migrationtool.CustomerMigrationData;
import com.oup.eac.domain.migrationtool.CustomerMigrationState;
import com.oup.eac.domain.migrationtool.RegistrationMigration;
import com.oup.eac.domain.migrationtool.RegistrationMigrationData;
import com.oup.eac.domain.migrationtool.RegistrationMigrationState;
import com.oup.eac.service.ServiceLayerException;


/**
 * @author Chirag Joshi
 */
public interface CustomerMigrationService {

    //CUSTOMERS
    CustomerMigration getCustomerMigrationToProcess(CustomerMigrationState state, int fetchSize);
    void saveCustomerMigrationState(String customerMigrationId, CustomerMigrationState state);

    //CREATE CUSTOMER
    Customer createCustomer(String customerMigrationId)  throws ServiceLayerException;
                
    //For stopping
    boolean isMigrationFinished(boolean registrationMigration);
    
    //USED BY CSV FILE -> DB LOADER
    void saveCustomerMigrationData(CustomerMigrationData data);
    
    void saveCustomerMigrationData(List<CustomerMigrationData> data);
    
    List<CustomerMigrationData> findCustomerMigrationData(String user_id) throws ServiceLayerException;
    
    CustomerMigration getCustomerMigration(final String id);
    
    
    void saveRegistrationMigrationData(RegistrationMigrationData data);
    
    Map<String, String> getProductDtls(List<String> productNameToMigrate);
    
    void updateProductAndDefinitionId(Map<String,String> mapOfProdDtls);
    
    RegistrationMigration getRegistrationMigrationToProcess(RegistrationMigrationState state, int fetchSize);

    void saveRegistrationMigrationState(String registrationMigrationId, RegistrationMigrationState state);
    
    Registration<ProductRegistrationDefinition> createRegistration(String registrationMigrationId) throws ServiceLayerException;
    
    RegistrationMigration getRegistrationMigration(final String id);
    
    void createRegirstionMigrationRecords(String customerMigrationId);
	
}
