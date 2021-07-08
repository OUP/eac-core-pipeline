package com.oup.eac.service;

import java.util.List;

import com.amazonaws.services.cloudfront.model.AccessDeniedException;
import com.oup.eac.domain.AccountRegistrationDefinition;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegistrationDefinition;
import com.oup.eac.domain.paging.Paging;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.RegistrationDefinitionSearchCriteria;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.GroupNotFoundException;
import com.oup.eac.integration.facade.exceptions.LicenseNotFoundException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;

/**
 * @author harlandd 
 * RegistrationDefinition service providing product related business processes.
 */
public interface RegistrationDefinitionService {


    /**
     * Gets the product registration definition by product.
     *
     * @param registerableProduct the registerable product
     * @return the product registration definition by product
     * @throws ServiceLayerException the service layer exception
     */
    ProductRegistrationDefinition getProductRegistrationDefinitionByProduct(final Product product) throws ServiceLayerException;
    
    /**
     * Gets the product registration definition by product.
     *
     * @param registerableProduct the registerable product
     * @return the product registration definition by product
     * @throws ServiceLayerException the service layer exception
     */
    ProductRegistrationDefinition getProductRegistrationDefinitionByProductWithoutActivationCodeInfo(final RegisterableProduct registerableProduct) throws ServiceLayerException;
    
    
    /**
     * Gets the account registration definition by product.
     *
     * @param registerableProduct the registerable product
     * @return the account registration definition by product
     * @throws ServiceLayerException the service layer exception
     */
    AccountRegistrationDefinition getAccountRegistrationDefinitionByProduct(final Product product) throws ServiceLayerException;
    
    /**
     * Gets the activation code registration definitions by admin user.
     *
     * @param adminUser the admin user
     * @return the activation code registration definitions by admin user
     * @throws ServiceLayerException the service layer exception
     */
    List<ActivationCodeRegistrationDefinition> getActivationCodeRegistrationDefinitionsByAdminUser(AdminUser adminUser) throws ServiceLayerException;
    
    /**
     * Gets the registration definition by id.
     *
     * @param id the id
     * @return the registration definition by id
     * @throws ServiceLayerException the service layer exception
     */
    RegistrationDefinition getRegistrationDefinitionById(String id) throws ServiceLayerException;

    /**
     * Gets the activation code registration definition by product.
     *
     * @param product the product
     * @return the activation code registration definition by product
     * @throws ServiceLayerException the service layer exception
     */
    ActivationCodeRegistrationDefinition getActivationCodeRegistrationDefinitionByProduct(final Product product) throws ServiceLayerException;

    /**
     * Gets the product registration definition from customer and registration id.
     *
     * @param customer the customer
     * @param registrationId the registration id
     * @return the product registration definition from customer and registration id
     */
    ProductRegistrationDefinition getProductRegistrationDefinitionFromCustomerAndRegistrationId(Customer customer, String registrationId);

	/**
	 * Searches for {@link RegistrationDefinition}s based on the supplied {@link RegistrationDefinitionSearchCriteria}.
	 * The search query will be built using the properties supplied in the RegistrationDefinitionSearchCriteria. Setting
	 * a property effectively narrows the search - so if no properties are set, then all {@link RegistrationDefinition}s
	 * will be returned.
	 * 
	 * @param searchCriteria
	 *            The RegistrationDefinitionSearchCriteria.
	 * @param pagingCriteria
	 *            The {@link PagingCriteria} which governs the start point/end point and number of items that should be
	 *            returned.
	 * @return A {@link Paging} object referencing the RegistrationDefinitions found.
	 * @throws ServiceLayerException
	 *             If there was a problem performing the search.
	 */
	Paging<RegistrationDefinition> searchRegistrationDefinitions(final RegistrationDefinitionSearchCriteria searchCriteria, final PagingCriteria pagingCriteria)
			throws ServiceLayerException;
    
    /**
     * Save product registration definition.
     * @param registrationDefinition The registration definition to save.
     */
    void saveRegistrationDefinition(final RegistrationDefinition registrationDefinition);
    
    void deleteRegistrationDefinition(final RegistrationDefinition registrationDefinition);
    
    ProductRegistrationDefinition getProductRegistrationDefinitionWithLicence(String id);
        
    ActivationCodeRegistrationDefinition getActivationCodeRegistrationDefinitionForEacGroup(final String batchId) 
    		throws ServiceLayerException, ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, 
    		AccessDeniedException, GroupNotFoundException, ErightsException;

	List<RegistrationDefinition> getRegistrationDefinitionsForProduct(final Product product) throws ServiceLayerException;

	List<ProductRegistrationDefinition> getRegistrationDefinitionsForPageDefinition(
			String pageDefId) throws ServiceLayerException;

	List<AccountRegistrationDefinition> getRegistrationDefinitionsForPageDefinitionForAccount(
			String pageDefId) throws ServiceLayerException;

}
