package com.oup.eac.service;

import java.util.List;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Product;
import com.oup.eac.dto.LicenceDto;

/**
 * Performs licence related functions.
 * 
 * @author Ian Packard
 *
 */
public interface LicenceService {

	/**
	 * Get the licences for a given customer.
	 * 
	 * @param customer
	 * @return
	 * @throws ServiceLayerException
	 */
	List<LicenceDto> getLicencesForCustomer(final Customer customer) throws ServiceLayerException;
	
	/**
	 * Get the licences for a given customer.
	 * 
	 * @param customer
	 * @param registrationId 
	 * @return
	 * @throws ServiceLayerException
	 */
	List<LicenceDto> getLicencesForCustomer(final Customer customer, String registrationId) throws ServiceLayerException;

	/**
	 * Get the licences for a given user filtered by product.
	 * 
	 * @param customer
	 * @param product
	 * @return
	 * @throws ServiceLayerException
	 */
	List<LicenceDto> getLicensesForUserProduct(final Customer customer, final Product product) throws ServiceLayerException;
	
	/**
	 * Get the active licences for a given customer.
	 * 
	 * @param customer
	 * @return
	 * @throws ServiceLayerException
	 */
	List<LicenceDto> getActiveLicencesForCustomer(final Customer customer) throws ServiceLayerException;
	
	/**
	 * Update licences in erights for the customer provided.
	 * 
	 * @param licences
	 * @throws ServiceLayerException
	 */
	/**
	 * Update licences in erights for the customer provided.
	 * 
	 * @param customer
	 * 			The eac customer to update in erights
	 * @param licences
	 * 			The licences to update in erights.
	 * @throws ServiceLayerException
	 */
	void updateLicencesForCustomer(final Customer customer, final List<LicenceDto> licences) throws ServiceLayerException;
	
	/**
	 * Update an individual licence attached to a customer in erights.
	 * 
	 * @param customer
	 * 			The eac customer to update in erights
	 * @param licence
	 * 			The licence to update in erights.
	 * @throws ServiceLayerException
	 */
	void updateLicenceForCustomer(final Customer customer, final LicenceDto licence) throws ServiceLayerException;
}
