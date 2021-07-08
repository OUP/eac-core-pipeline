/**
 * 
 */
package com.oup.eac.data;

import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductRegistration;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.dto.ReportCriteria;

/**
 * @author harlandd
 * @author David Hay
 * 
 */
public interface RegistrationDao extends BaseDao<Registration<? extends ProductRegistrationDefinition>, String> {
	
	/**
	 * Get a registration by id and pre fetch associations
	 * 
	 * @param id the id
	 * @return The registration
	 */
	//Registration<?> getRegistrationByIdInitialised(final String id);	
	
	/**
	 * Get a registration by id
	 * 
	 * @param id
	 * @return
	 */
	Registration<ProductRegistrationDefinition> getRegistrationById(final String id);
	
	/**
	 * Get a registration with its product by id
	 * 
	 * @param id
	 * @return
	 */
	//Registration<ProductRegistrationDefinition> getRegistrationWithProductById(final String id);
	
	
	
	/**
     * Get a registration by product and user.
     * 
     * @param product
     *            the product definition
     * @param customer
     *            the customer
     * @return The registration matching the product and user
     */
	//List<Registration<ProductRegistrationDefinition>> getRegistrationByRegistrationDefinitionAndCustomer(final ProductRegistrationDefinition productRegistrationDefinition, Customer customer);

    /**
     * Get registrations by date and owner.
     * 
     * @param fromDate
     *            start period
     * @param toDate
     *            end period
     * @param productOwner
     *            owner
     * @return products
     */
    List<Object[]> getRegistrationsByDateAndDivision(final DateTime fromDate, final DateTime toDate, final String divisionType, final String rsDBSchema);
    
    Long getRegistrationsByDateAndDivisionAndAccountCount(final DateTime fromDate, final DateTime toDate, final String divisionType, final String rsDBSchema);
    
    List<Object[]> getRegistrationsByDateAndDivisionAndAccount(final DateTime fromDate, final DateTime toDate, final String divisionType, final int batchSize, int firstRecord, final String rsDBSchema);

    
    /**
     * Gets the product registrations for customer.
     *
     * @param customer the customer
     * @return the product registrations for customer
     */
    List<ProductRegistration> getProductRegistrationsForCustomer(Customer customer);
    
    /**
     * Gets the activation code registrations for customer.
     *
     * @param customer the customer
     * @return the activation code registrations for customer
     */
    List<ActivationCodeRegistration> getActivationCodeRegistrationsForCustomer(Customer customer);

    /**
     * Gets the registration populated with entitlements.
     *
     * @param registration the registration
     * @return the registration populated with entitlements
     */
    Registration<? extends ProductRegistrationDefinition> getRegistrationPopulatedWithEntitlements(Registration<? extends ProductRegistrationDefinition> registration);
    
    /**
     * Get a list of ActivationCodeRegistrations by activation code
     * 
     * @param activationCode
     * @return
     */
    List<ActivationCodeRegistration> getActivationCodeRegistrationByCode(final ActivationCode activationCode);
    
    /**
     * Gaurav Soni : Performance improvements CR
     * Gets the activation code registrations for customer.
     *
     * @param customer the customer
     * @param productSystemIdSet the list of product external IDs
     * @return the activation code registrations for customer
     */
    List<ActivationCodeRegistration> getActivationCodeRegistrationsForCustomerFiltered(Customer customer, Set<String> productOrgUnitSet, Set<String> productSystemIdSet);
    
    
    /**
     * Gaurav Soni : Performance improvements CR
     * Gets the product registrations for customer.
     *
     * @param customer the customer
     * @param productSystemIdSet the list of product external IDs
     * @return the product registrations for customer
     */
    List<ProductRegistration> getProductRegistrationsForCustomerFiltered( Customer customer, Set<String> productOrgUnitSet, Set<String> productSystemIdSet);

	

	/**
	 * 
	 * Get the number of registrations based on the report criteria
	 * 
	 * @param reportCriteria
	 * @param rsDBSchema
	 * @return
	 */
	List<Object[]> getReportRegistrations(ReportCriteria reportCriteria,
			String rsDBSchema);

	/**
	 * Get the number of registrations found based on the report criteria
	 * 
	 * @param reportCriteria
	 * @return the number of registrations found
	 */
	List<Object[]> getReportRegistrationsCount(ReportCriteria reportCriteria,
			String rsDBSchema);

}
