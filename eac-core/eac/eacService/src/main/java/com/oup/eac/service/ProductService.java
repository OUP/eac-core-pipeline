package com.oup.eac.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.LinkedProduct;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.beans.ProductBean;
import com.oup.eac.domain.paging.Paging;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.LicenceDetailDto;
import com.oup.eac.dto.RegistrationDefinitionSearchCriteria;
import com.oup.eac.integration.facade.exceptions.ChildProductFoundException;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.ParentProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.service.exceptions.NoRegisterableProductFoundException;

/**
 * @author harlandd Product service providing product related business processes.
 */
public interface ProductService {

    /**
     * @param url
     *            the product url
     * @return the ProductDefinition
     * @throws ServiceLayerException
     *             the exception
     * @throws NoRegisterableProductFoundException 
     * @throws ErightsException 
     */
    RegisterableProduct getRegisterableProductByUrl(final String url) throws ServiceLayerException, NoRegisterableProductFoundException, ErightsException;
    
	/**
	 * Get a list of products for a specific division. These can optionally be filtered by product name and erights
	 * id.
	 * 
	 * @param division
	 *            The  division to get the products for.
	 * @param Optional
	 *            product name to filter the products by.
	 * @param Optional
	 *            erights id to filter the products by.
	 * @param pageSize
	 *            the number of items on the page.
	 * @param page
	 *            the page number.
	 * 
	 * @return The list of filtered product .
	 * @throws ServiceLayerException
	 */
    
    //division removal
    /*List<Product> getProductsByDivision(final Division division, 
    												final String productName, final Integer erightsId,
    												final int pageSize, final int page) 
    												throws ServiceLayerException;*/
	
    /**
	 * Saves a new product.
	 * 
	 * @param pd
	 *            The product to save.
	 * @throws ServiceLayerException
	 */
    void saveProduct(final Product pd) throws ServiceLayerException;
    
	/**
	 * Saves a new product where the primary key (GUID) of the product has been populated externally.
	 * 
	 * @param pd
	 *            The product to save.
	 * @throws ServiceLayerException
	 */
    void saveProductWithExistingGuid(final Product pd) throws ServiceLayerException;
    
	/**
	 * Updates a product.
	 * 
	 * @param pd
	 *            The product to update.
	 * @throws ServiceLayerException
	 */
    void updateProduct(final Product pd) throws ServiceLayerException;
    
    
    void updateRegisterableProduct(final RegisterableProduct pd) throws ServiceLayerException, ErightsException;
    void updateProduct(final EnforceableProductDto enforceableProductDto, final ProductBean productBean) throws ServiceLayerException, ErightsException, UnsupportedEncodingException;
    void updateLinkedProducts(final ProductBean pb,final RegisterableProduct pd) throws ServiceLayerException, ErightsException,UnsupportedEncodingException;
    /**
     * Save or update a product definition.
     * 
     * @param pd
     *      The product definition to save or update.
     * @return
     *      The updated product definition.
     */
    void saveOrUpdateProduct(final Product pd) throws ServiceLayerException;
    
    /**
     * Get a product definition by id.
     * 
     * @param id
     *      The id of the product definition to get
     * @return
     *      The product definition matching the id
     */
    Product getProductById(final String id) throws ServiceLayerException;
    
    /**
     * Gets the product by external product id.
     *
     * @param systemId the system id
     * @param typeId the type id
     * @param externalId the external id
     * @return the product by external product id
     * @throws ServiceLayerException 
     */
    EnforceableProductDto getProductByExternalProductId(String systemId, String typeId, String externalId) throws ServiceLayerException;

    /**
     * Resolve products from a list of erights ids.
     * 
     * @param erightsIds
     * @return
     */
    List<Product> getProductsByErightsIds(List<Integer> erightsIds);
    
    /**
     * Gets the product by erights id.
     *
     * @param eRightsId the erights id
     * @param adminDivisionList 
     * @return the product by erights id
     * @throws ErightsException 
     * @throws ProductNotFoundException 
     */
    List<ProductBean> getProductByErightsId(final String eRightsId, Map<String, String> adminDivisionList) throws ProductNotFoundException, ErightsException;
    
    
    /**
     * @param eRightsId
     * @return
     */
    List<Product> getProductByErightsId(final String eRightsId);
    
    /**
     * Gets the product and external ids by erights id.
     *
     * @param eRightsProductId the e rights product id
     * @return the product and external ids by erights id
     */
    List<Product> getProductAndExternalIdsByErightsId(final String eRightsProductId);
    
    
    /**
     * Gets a list of products for the list of erights ids filtered by admin user.
     * 
     * @param erightsIds
     * 			List of erights ids to resolve products for		
     * @param adminUser
     * 			Admin user to filter results
     * @return
     */
    List<Product> getProductsByErightsIdsAndAdminUser(final List<String> erightsIds, final AdminUser adminUser);

    /**
     * Checks if is product used.
     *
     * @param productId the product id
     * @return true, if is product used
     */
    boolean isProductUsed(String productId);

    boolean deleteUnUsedProduct(String productId) throws ServiceLayerException, ProductNotFoundException, ChildProductFoundException, ErightsException;
    
    /**
     * Gets the products linked direct
     
     * @return the products linked direct
     * @throws ErightsException 
     */
   
    EnforceableProductDto getProductByName(final String name) throws ErightsException;

	List<LinkedProduct> getProductsLinkedDirectToProduct(Product product) throws ServiceLayerException;

	Paging<ProductBean> searchProduct(
			RegistrationDefinitionSearchCriteria productSearchCriteria,
			PagingCriteria pagingCriteria,
			final Map<String,String> adminDivisionList);

	EnforceableProductDto getEnforceableProductByErightsId(String productId)
			throws ProductNotFoundException, ErightsException;

	EnforceableProductDto createRightsuitProduct(
			EnforceableProductDto enforceableProductDto,
			LicenceDetailDto licenceDetailDto)
			throws ParentProductNotFoundException, ErightsException;


	void removeLinkedProductFromRightsuit(String childId, String parentId)
			throws ProductNotFoundException, ParentProductNotFoundException,
			ErightsException;

	void removeExternalProductFromRightsuit(String externalId, String productId)
			throws ErightsException;

	void addLinkedProductToRightsuit(String childId, String parentId)
			throws ProductNotFoundException, ParentProductNotFoundException,
			ErightsException;
}
