package com.oup.eac.service;

import java.util.List;

import com.amazonaws.services.cloudfront.model.AccessDeniedException;
import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.ActivationCodeSearchDto;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.paging.Paging;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.ActivationCodeBatchDto;
import com.oup.eac.dto.ActivationCodeBatchSearchCriteria;
import com.oup.eac.dto.ActivationCodeReportDto;
import com.oup.eac.dto.ActivationCodeSearchCriteria;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.GuestRedeemActivationCodeDto;
import com.oup.eac.integration.erights.GetActivationCodeBatchByBatchIdResponse;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.GroupNotFoundException;
import com.oup.eac.integration.facade.exceptions.LicenseNotFoundException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;

public interface ActivationCodeService {
	
	ActivationCode getActivationCodeWithDetails(String id) throws ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, 
	org.springframework.security.access.AccessDeniedException, GroupNotFoundException, ErightsException, ServiceLayerException; 
	
    Paging<ActivationCodeReportDto> getActivationCodeSearch(ActivationCodeSearchCriteria criteria, PagingCriteria pagingCriteria, AdminUser adminUser);
    
	/**
	 * Get activation codes by code
	 * 
	 * @param code the code
	 * @param likeSearch do a like search or not
	 * @return
	 */
	List<ActivationCodeSearchDto> searchActivationCodeByCode(final String systemId, String code, boolean likeSearch);
	
	/**
	 * Get the activation code by code with associations pre fetched
	 * 
	 * @param code the code
	 * @return the activation code
	 */
	ActivationCode getActivationCodeAndDefinitionByCode(String code);
	
	ActivationCode getActivationCodeAndDefinitionByCodeForEacGroup(String code);

	/**
	 * Get the activation code by code
	 * 
	 * @param code the code
	 * @return the activation code
	 * @throws ServiceLayerException 
	 */
    ActivationCode getActivationCodeByCode(String code) throws  ServiceLayerException;
    
    /**
     * Get the activation code by code with the product by the activation code
     * 
     * @param activationCode
     * @return
     */
    ActivationCode getActivationCodeAndProductByCode(String activationCode);
	
	/**
	 * Get the activation code by code
	 * 
	 * @param code the code
	 * @return the activation code
	 */
    ActivationCodeBatchDto getActivationCodeByCode(ActivationCode activationCode);
	
    /**
     * Get the bytes for the activation code file to be exported
     * 
     * @param batchId the batch Id
     * @param format to format the codes or not
     * @return the byte array of the string to be exported
     * @throws ServiceLayerException
     */
	byte[] getActivationCodeByBatch(String batchId, boolean format) throws ServiceLayerException;
	
	/**
	 * Get the activation code batched by divion admin users
	 * 
	 * @param divisionAdminUsers the division admin users
	 * @return the activation code batches
	 */
	//List<ActivationCodeBatch> getActivationCodeBatchByDivision(List<DivisionAdminUser> divisionAdminUsers);

	/**
	 * Get the activation code batch by id
	 * 
	 * @param id the id
	 * @return the activation code batch
	 */
	ActivationCodeBatch getActivationCodeBatchById(String id);
	
    /**
     * Get the activation code batch by id
     * 
     * @param id the id
     * @return the activation code batch
     */
    ActivationCodeBatch getActivationCodeBatchByDbId(String id);

    
	
	/**
	 * Create a default activation code batch
	 * 
	 * @return the activation code batch
	 * @throws ServiceLayerException
	 */
	ActivationCodeBatch createDefaultActivationCodeBatch() throws ServiceLayerException;
	
	/**
	 * 
	 * Save an activation code batch 
	 * 
	 * @param activationCodeBatch the activation code batch
	 * @param activationCodeRegistrationDefinition the activation code registration definition
	 * @param numTokens the number of tokens
	 * @param allowedUsage the allowed usage
	 * @return the saved activation code batch
	 * @throws ServiceLayerException
	 * @throws ErightsException 
	 * @throws org.springframework.security.access.AccessDeniedException 
	 */
	ActivationCodeBatch saveActivationCodeBatch(ActivationCodeBatch activationCodeBatch, 
			ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition,
			int numTokens, 
			int allowedUsage) throws ServiceLayerException, org.springframework.security.access.AccessDeniedException, ErightsException;
	
	ActivationCodeBatch saveActivationCodeBatch(final ActivationCodeBatch activationCodeBatch,
            final ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition,
            final int allowedUsage,
            final int actualUsage,
            String activationCode) throws ServiceLayerException;
	/**
	 * Search for an activation code batch
	 * 
	 * @param searchCriteria the {@link ActivationCodeBatchSearchCriteria}.
	 * @param pagingCriteria
	 *            The {@link PagingCriteria}
	 * @return A {@link Paging} object referencing the ActivationCodeBatch found by the search.
	 */
	Paging<ActivationCodeBatch> searchActivationCodeBatches(ActivationCodeBatchSearchCriteria searchCriteria, PagingCriteria pagingCriteria);
	
	/**
	 * Get activation codes by batch id
	 * 
	 * @param batchId the batch id
	 * @return the activation codes
	 * @throws ServiceLayerException
	 */
    List<ActivationCode> getActivationCodesByBatchId(String batchId) throws ServiceLayerException;
    
    /**
     * Save an activation code batch 
     * 
     * @param acBatch the activation code batch
     * @param product the product
     * @param licTemplate the licence template
     * @param numTokens the number of tokens
     * @param allowedUsage the allowed usage
     * @throws ServiceLayerException
     * @throws ErightsException 
     * @throws ProductNotFoundException 
     */
    ActivationCodeBatch saveActivationCodeBatchWithTemplate(ActivationCodeBatch acBatch, EnforceableProductDto enforceableProductDto, LicenceTemplate licTemplate, int numTokens, int allowedUsage) throws ServiceLayerException, ProductNotFoundException, ErightsException;
    
    /**
     * Validate an activation code
     * 
     * @param activationCode the activation code
     * @throws ServiceLayerException
     */
    void validateActivationCode(ActivationCode activationCode) throws ServiceLayerException;
    
    /**
     * Update an activation code
     * 
     * @param activationCode
     */
    void updateActivationCode(ActivationCode activationCode);

    /**
     * Update activation code batch.
     *
     * @param activationCodeBatch the activation code batch
     * @throws ServiceLayerException the service layer exception
     * @throws ErightsException 
     */
    void updateActivationCodeBatch(ActivationCodeBatch activationCodeBatch, String currentBatchId) throws ServiceLayerException, ErightsException;

    
    /**
     * Checks if is activation code batch has been used.
     *
     * @param activationCodeBatchDbId the activation code batch db id
     * @return true, if is activation code batch used
     * @throws ServiceLayerException the service layer exception
     * @throws ErightsException 
     * @throws GroupNotFoundException 
     * @throws AccessDeniedException 
     * @throws LicenseNotFoundException 
     * @throws UserNotFoundException 
     * @throws ProductNotFoundException 
     */
    boolean hasActivationCodeBatchBeenUsed(String activationCodeBatchDbId) throws ServiceLayerException, 
    ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, AccessDeniedException, 
    GroupNotFoundException, ErightsException;
    
    /**
     * Delete unused activation code batch.
     *
     * @param activationCodeBatchId the activation code batch id
     * @throws ServiceLayerException the service layer exception
     * @throws ErightsException 
     * @throws GroupNotFoundException 
     * @throws AccessDeniedException 
     * @throws LicenseNotFoundException 
     * @throws UserNotFoundException 
     * @throws ProductNotFoundException 
     */
    void deleteUnusedActivationCodeBatch(String activationCodeBatchId) throws ServiceLayerException, ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, AccessDeniedException, GroupNotFoundException, ErightsException;
    
    /**
     * Gets the number of tokens in batch.
     *
     * @param activationCodeBatchId the activation code batch id
     * @return the number of tokens in batch
     * @throws ServiceLayerException the service layer exception
     */
    int getNumberOfTokensInBatch(String activationCodeBatchId) throws ServiceLayerException;

    boolean doesArchivedBatchExist(String batchName);
    
    /**
     * Get the activation code by code with the product by the activation code
     * 
     * @param activationCode
     * @return
     */
    boolean isActivationCodeExists(final String code);
    
    ActivationCode getActivationCodeFullDetails(String activationCode);

	GetActivationCodeBatchByBatchIdResponse checkActivationCodeBatchExistsByBatchId(String batchId)
			throws ProductNotFoundException, UserNotFoundException,
			LicenseNotFoundException,
			org.springframework.security.access.AccessDeniedException,
			GroupNotFoundException, ErightsException, ServiceLayerException;

	List<GuestRedeemActivationCodeDto> guestRedeemActivationCode(
			String activationCode) throws ErightsException;

	List<ActivationCodeRegistration> getRedeemActivationCodeInfo(
			ActivationCodeSearchDto activationCodeSearchDto, String systemId);


	ActivationCodeBatch saveActivationCodeBatchAsync(
			ActivationCodeBatch activationCodeBatch,
			ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition,
			int numTokens, int allowedUsage, String productGroupId,
			String adminEmail, String adminName) throws ServiceLayerException,
			org.springframework.security.access.AccessDeniedException,
			ErightsException;

	/**
	 * Get the activation code batch by batch id
	 * 
	 * @param batchId the batch id
	 * @return the activation code batch
     * @throws ErightsException 
     * @throws GroupNotFoundException 
     * @throws AccessDeniedException 
     * @throws LicenseNotFoundException 
     * @throws UserNotFoundException 
     * @throws ProductNotFoundException 
     * @throws org.springframework.security.access.AccessDeniedException 
     * @throws ServiceLayerException 
	 */
	ActivationCodeBatch getActivationCodeBatchByBatchId(String batchId,
			boolean activationCodeRequired) throws ProductNotFoundException,
			UserNotFoundException, LicenseNotFoundException,
			org.springframework.security.access.AccessDeniedException,
			GroupNotFoundException, ErightsException, ServiceLayerException;

	ActivationCode getActivationCodeWithDetailsWS(String trimmedActivationCode) throws ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, org.springframework.security.access.AccessDeniedException, GroupNotFoundException, ErightsException, ServiceLayerException;
	
	
}
