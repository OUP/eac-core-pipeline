package com.oup.eac.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.CustomerSearchCriteria;

/**
 * @author harlandd The user dao interface
 */
public interface CustomerDao extends OUPBaseDao<Customer, String> {
    
    boolean hasCustomerEmailChanged(String username, String emailAddress);

    /**
     * 
     * @param erightsId
     *            the erights id
     * @param customerTypes
     *            the customer types
     * @return the user by erights id
     */
    Customer getCustomerByErightsId(final Integer erightsId, final List<CustomerType> customerTypes);
    
    /**
     * 
     * @param erightsId
     *            the erights id
     * @return the user by erights id
     */
    Customer getCustomerWithAnswersByErightsId(final Integer erightsId);

    Set<Answer> getAnswersByCustomerId(final String customerId);
    
    /**
     * 
     * @param emailAddress
     *            the email address
     * @return the user by email address
     */
    Customer getCustomerByEmailAddress(final String emailAddress);

    /**
     * 
     * @param username
     *            the username
     * @return the user by username
     */
    Customer getCustomerByUsername(final String username);    
        
	List<Customer> searchCustomers(final CustomerSearchCriteria customerSearchCriteria, final PagingCriteria pagingCriteria);

	Set<String> getCustomerIdsByRegistrationData(final CustomerSearchCriteria customerSearchCriteria, final PagingCriteria pagingCriteria); 
	
	//As part of customer de-duplication
	int countSearchCustomers(final CustomerSearchCriteria customerSearchCriteria, final PagingCriteria pagingCriteria);

    Customer getCustomerByExternalCustomerId(String systemId, String typeId, String externalId);
    
    void markCustomerAsUpdated(Customer customer);
    
    void deleteCustomer(Customer customer);
    
    List<Customer> getCustomerInvalidEmailAddress();

	List<Customer> getCustomersByProductOwner(List<String> divisionTypes);

	List<String> getCustomerIdsByRegistrationDataSearch(
			CustomerSearchCriteria customerSearchCriteria,
			PagingCriteria pagingCriteria);

	List<Integer> getCustomerIdListByCustomerIdListInSortedOrderOfDateCreated(
			ArrayList<String> customerErightsId);

}
