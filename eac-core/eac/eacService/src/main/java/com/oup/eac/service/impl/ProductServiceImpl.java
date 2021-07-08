package com.oup.eac.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.constants.SearchDomainFields;
import com.oup.eac.cloudSearch.search.impl.AmazonCloudSearchServiceImpl;
import com.oup.eac.common.utils.url.URLUtils;
import com.oup.eac.data.ProductDao;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.LinkedProduct;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.Product.ProductState;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.beans.ProductBean;
import com.oup.eac.domain.paging.Paging;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.domain.search.AmazonSearchFields;
import com.oup.eac.domain.search.AmazonSearchRequest;
import com.oup.eac.domain.search.AmazonSearchResponse;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.EnforceableProductDto.RegisterableType;
import com.oup.eac.dto.ExternalIdDto;
import com.oup.eac.dto.LicenceDetailDto;
import com.oup.eac.dto.Message;
import com.oup.eac.dto.RegistrationDefinitionSearchCriteria;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ChildProductFoundException;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.ParentProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.service.DivisionService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.NoRegisterableProductFoundException;
import com.oup.eac.service.util.ConvertSearch;

/**
 * Product service providing product related business processes.
 * 
 * @author harlandd
 * @author Ian Packard 
 */
@Service(value="productService")
public class ProductServiceImpl implements ProductService {

	private static final Logger LOGGER = Logger.getLogger(ProductServiceImpl.class);

	private static final Set<ProductState> ACTIVE_STATE = new HashSet<ProductState>(Arrays.asList(ProductState.ACTIVE));

	private final ErightsFacade erightsFacade;

	private final ProductDao productDao;

	//private final DivisionDao divisionDao;

	
	private static final String RECORD_SEPARATOR = "\\|";
	
	private static final String FIELD_SEPARATOR = ",";
	
	private final DivisionService divisionService;
	

	/**
	 * @param erightsFacade
	 *            the erights facade
	 * @param productDao
	 *            product definition dao
	 */
	/*@Autowired
	public ProductServiceImpl(final ErightsFacade erightsFacade, final ProductDao productDao,
			final DivisionDao divisionDao) {
		super();
		Assert.notNull(erightsFacade);
		Assert.notNull(productDao);
		Assert.notNull(divisionDao);
		this.erightsFacade = erightsFacade;
		this.productDao = productDao;
		this.divisionDao = divisionDao;
	}*/
	@Autowired
	public ProductServiceImpl(final ErightsFacade erightsFacade, final ProductDao productDao,
			DivisionService divisionService) {
		super();
		Assert.notNull(erightsFacade);
		Assert.notNull(productDao);
		this.erightsFacade = erightsFacade;
		this.productDao = productDao;
		this.divisionService=divisionService;
	}
	
	/**
	 * @param url
	 *            the product url
	 * @return ProductDefinition the product 
	 * @throws ServiceLayerException
	 *             the exception
	 * @throws NoRegisterableProductFoundException 
	 * @throws ErightsException 
	 */
	@Override
	public final RegisterableProduct getRegisterableProductByUrl(final String url) throws ServiceLayerException, NoRegisterableProductFoundException, ErightsException {
		if (StringUtils.isBlank(url)) {
			throw new NoRegisterableProductFoundException("No URL specified");
		}
		/*try {*/
			String encodedUrl;
			try {
				encodedUrl = URLUtils.safeEncode(url);
			} catch (Exception e) {
				throw new ServiceLayerException("Error encoding url. URL: " + url, e, new Message("error.aquiring.product.information",
						"There was a problem with your product. Please go back to the product page and choose the item again."));
			}            
			return getRegisterableProductFromIds(erightsFacade.getProductIdsByUrl(encodedUrl), url);
		/*} catch (ErightsException e) {
			throw new NoRegisterableProductFoundException("Product not found");
		}*/
	}
	
	@Override
	public final EnforceableProductDto getEnforceableProductByErightsId(String productId) throws 
	ProductNotFoundException, ErightsException{ 
		return erightsFacade.getProduct(productId) ;
	}
	
	@Override
	public final EnforceableProductDto createRightsuitProduct(EnforceableProductDto enforceableProductDto,LicenceDetailDto licenceDetailDto) throws ParentProductNotFoundException, ErightsException {
		return erightsFacade.createProduct(enforceableProductDto, licenceDetailDto);
	}
	
	@Override
	public final void addLinkedProductToRightsuit(String childId, String parentId) throws ProductNotFoundException, ParentProductNotFoundException, ErightsException{
		erightsFacade.addLinkedProduct(childId,parentId) ;
	}
	
	@Override
	public final void removeLinkedProductFromRightsuit(String childId, String parentId) throws ProductNotFoundException, ParentProductNotFoundException, ErightsException{
		erightsFacade.removeLinkedProduct(childId,parentId) ;
	}
	
	@Override
	public final void removeExternalProductFromRightsuit(String externalId,String productId) throws ErightsException {
		erightsFacade.removeProductExternalId(externalId,productId);
	}
	/**
	 * @param productIds
	 *            the product ids
	 * @param url
	 *            the product url
	 * @return ProductDefinition the product 
	 * @throws ServiceLayerException
	 *             the ServiceLayerException
	 * @throws NoRegisterableProductFoundException 
	 * @throws ErightsException 
	 */
	private final RegisterableProduct getRegisterableProductFromIds(final List<String> productIds, final String url) throws NoRegisterableProductFoundException, ErightsException, ServiceLayerException {
		if (CollectionUtils.isEmpty(productIds)) {
			LOGGER.warn("No product ids were returned by erights for url: " + url);
			throw new NoRegisterableProductFoundException("No product ids returned by erights for url: " + url);
		}
		if (productIds.size() > 1) {
			LOGGER.warn("More than one product id was returned for url: " + url);
		}
		RegisterableProduct registerableProduct;
		try {
			registerableProduct = getRegisterableProduct(productIds);
		} catch (ErightsException e) {
			throw new ServiceLayerException("No registerable product found for erights ids: " + Arrays.toString(productIds.toArray()));
		}/*catch (ProductNotFoundException e) {
			e.printStackTrace();
			throw new NoRegisterableProductFoundException("No registerable product found for erights ids: " + Arrays.toString(productIds.toArray()));
			
		}*/
		/*if(registerableProduct == null) {
			throw new NoRegisterableProductFoundException("No registerable product found for erights ids: " + Arrays.toString(productIds.toArray()));
		}*/
		return registerableProduct;
	}

	/**
	 * Get single registerable product favouring self-registerable over admin-registerable
	 * @param productIds
	 * @return
	 * @throws ErightsException 
	 * @throws ProductNotFoundException 
	 * @throws ServiceLayerException 
	 */
	private RegisterableProduct getRegisterableProduct(final List<String> productIds) throws ProductNotFoundException, ErightsException, ServiceLayerException {
		// check if any self-registerable products
		RegisterableProduct firstAdminProduct = null;
		for (String id : productIds) {
			 EnforceableProductDto product = erightsFacade.getProduct(id); 
			 RegisterableProduct prod = new RegisterableProduct();
			 prod.setId(product.getProductId());
			 prod.setProductName(product.getName());
			 //List<RegisterableProduct> products =		productDao.getRegisterableProductsByErightsId(id, ACTIVE_STATE);
			/*for (RegisterableProduct product : products) {*/
				if (product.getRegisterableType().toString().equals(EnforceableProductDto.RegisterableType.SELF_REGISTERABLE.toString())) {
					// use first self-registerable product
					prod.setRegisterableType(com.oup.eac.domain.RegisterableProduct.RegisterableType.SELF_REGISTERABLE);
					return prod;
				}
				prod.setRegisterableType(com.oup.eac.domain.RegisterableProduct.RegisterableType.ADMIN_REGISTERABLE);
				// set first admin-registerable if no further self-registerable products
				if (firstAdminProduct == null) {
					firstAdminProduct = prod;
				}
			/*}*/
		}
		// use first-admin registerable product if exists
		return firstAdminProduct;
	}


	/**
	 * Resolve products from a list of erights ids.
	 * 
	 * @param erightsIds
	 * @return
	 */
	@Override
	public List<Product> getProductsByErightsIds(List<Integer> erightsIds) {
		return productDao.getProductsByErightsIds(erightsIds);
	}

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
	/*@Override
	public final List<Product> getProductsByDivision(final Division division, final String productName, final Integer erightsId, 
			final int pageSize, final int page) 
					throws ServiceLayerException {
		return productDao.getProductByDivision(division.getDivisionType(), productName, erightsId, 
				pageSize, page);
	}*/

	/*@Override
	public final List<Division> getDivisions() {
		return divisionDao.getDivisions();
	}*/

	@Override
	@PreAuthorize("hasRole('ROLE_CREATE_PRODUCT')")
	public void saveProduct(final Product pd) throws ServiceLayerException {
		productDao.save(pd);
	}

	@Override
	public void saveProductWithExistingGuid(final Product pd) throws ServiceLayerException {
		productDao.saveProduct(pd);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_UPDATE_PRODUCT')")
	public void updateProduct(final Product pd) throws ServiceLayerException {
		productDao.update(pd);
    	AuditLogger.logEvent("Saved Product", "productId:"+pd.getId(), AuditLogger.product(pd));
	}

	//Throws erights exception since we want error message of exception
	@Override
	@PreAuthorize("hasRole('ROLE_UPDATE_PRODUCT')")
	public void updateRegisterableProduct(final RegisterableProduct pd) throws ServiceLayerException, ErightsException {
		EnforceableProductDto enforceableProduct;
		try {
			enforceableProduct = erightsFacade.getProduct(pd.getId());

			//Added to remove Product External Id
			Set<ExternalProductId> extProductId = pd.getExternalIds();
			
			//temprory change add HashSet 
			List<ExternalSystemIdType> productExtSystemIdTypeLst = new ArrayList<ExternalSystemIdType>();
			for (ExternalProductId externalProductId : enforceableProduct.getExternalIds()) {
				if (externalProductId != null) {
					productExtSystemIdTypeLst.add(externalProductId.getExternalSystemIdType());
				}
			}
			Set<ExternalSystemIdType> extProductdto= new HashSet(productExtSystemIdTypeLst);
			boolean isDelete = false ;
			for(ExternalSystemIdType exId: extProductdto){
				isDelete = false ;
				for(ExternalProductId extId2 : extProductId){
					if(extId2.getExternalSystemIdType().getExternalSystem().getName().equals(exId.getExternalSystem().getName())){
						if (extId2.getExternalSystemIdType().getName().equals(exId.getName())) {
							isDelete = true ;
							break ;
						}
					}
				}
				if (isDelete) {
					erightsFacade.removeProductExternalId(exId.getExternalSystem().getId(), pd.getId());
				}
				
			}
//				if(!flag){
//					//product de-duplication
//					erightsFacade.removeProductExternalId(exId.getExternalIds());
//				}
			/*}*/
			enforceableProduct.mergeProductChanges(pd);
			//updated parent id for linked product in atypon
			
			
			erightsFacade.updateProduct(enforceableProduct);
			//product de-duplication(add comments to remove compilation error)
			Set<LinkedProduct> linkedProducts =null ;//=pd.getLinkedProducts();
			if(linkedProducts!= null && linkedProducts.size()>0)
			{
				for(Product product:linkedProducts)
				{
					erightsFacade.addLinkedProduct(product.getId(), pd.getId());
				}
			}
		} catch (ProductNotFoundException e) {
			// TODO Auto-generated catch block
			throw new ServiceLayerException(e.getMessage());
		}
	}
	
	@Override
	@PreAuthorize("hasRole('ROLE_UPDATE_PRODUCT')")
	public void updateProduct(final EnforceableProductDto enforceableProductDto, final ProductBean productBean) 
			throws ServiceLayerException, ErightsException, UnsupportedEncodingException {
		//product de-duplication	
		/*EnforceableProductDto enforceableProduct;
			enforceableProduct = erightsFacade.getProduct(pd.getErightsId());*/
			/*if(urls!=null)
				enforceableProduct.setUrls(urls);
			//Added to remove Product External Id
			Set<ExternalProductId> extProductId = pd.getExternalIds();
			//Set<ExternalProductId> extProductdto= enforceableProduct.getExternalIds();
			for(ExternalProductId exId: extProductdto){
				boolean flag= false;
				for(ExternalProductId extId2 : extProductId){
					if(extId2.getExternalSystemIdType().getName().equals(exId.getExternalSystemIdType().getName() )&& extId2.getExternalSystemIdType().getExternalSystem().getName().equals(exId.getExternalSystemIdType().getExternalSystem().getName())){
						flag=true;
					}
				}
				if(!flag){
					erightsFacade.removeProductExternalId(exId.getExternalId());
				}
			}
			//enforceableProduct.mergeProductChanges(pd);
			LicenceDetailDto licenceDetailDto = ErightsObjectFactory.getLicenceTemplateDto(prodRegDef.getLicenceTemplate());
//			List<Integer> erightsProductIds = Arrays.asList(prodRegDef.getProduct().getErightsId());
			
			if(prodRegDef.getRegistrationActivation().getName().toString().toLowerCase()
					.contains(ActivationStrategy.INSTANT.toString().toLowerCase())) {
				enforceableProduct.setActivationStrategy(ActivationStrategy.INSTANT.toString());
			} else if(prodRegDef.getRegistrationActivation().getName().toString().toLowerCase()
					.contains(ActivationStrategy.SELF.toString().toLowerCase())) {
				enforceableProduct.setActivationStrategy(ActivationStrategy.SELF.toString());
			} else {
				enforceableProduct.setActivationStrategy(ActivationStrategy.VALIDATED.toString());
			}
			enforceableProduct.getLicenceDetail().setLicenceDetail(licenceDetailDto);
			RegistrationActivation registrationActivation = prodRegDef.getRegistrationActivation();
			if (registrationActivation instanceof ValidatedRegistrationActivation) {
				enforceableProduct.setValidatorEmail(((ValidatedRegistrationActivation) registrationActivation).getValidatorEmail());
			}
			
			enforceableProduct.setConfirmationEmailEnabled(prodRegDef.isConfirmationEmailEnabled());
			
			
			if(prodRegDef instanceof ActivationCodeRegistrationDefinition)
			{
				enforceableProduct.setRegistrationDefinitionType(prodRegDef.getRegistrationDefinitionType().ACTIVATION_CODE_REGISTRATION.toString());
			}
			else
			{
				enforceableProduct.setRegistrationDefinitionType(prodRegDef.getRegistrationDefinitionType().PRODUCT_REGISTRATION.toString());
			}*/
		if ( productBean != null ) {	
			String linkedProductToUpdate = productBean.getLinkedProductsToUpdate() ;
			String linkedProductToAdd = productBean.getLinkedProductsToAdd() ;
			String linkedProductToRemove = productBean.getLinkedProductsToRemove() ;
			if (linkedProductToUpdate != null && linkedProductToUpdate.trim().length() > 0 ) {
				String [] productToUpdate = linkedProductToUpdate.split(RECORD_SEPARATOR) ;
				for ( String product : productToUpdate) {
					String [] linkedProduct = splitIntoFields(product) ; 
					//Product prod = getProductById(linkedProduct[0]);
					if(!linkedProduct[0].equals(linkedProduct[1])){
						linkedProductToAdd = (linkedProductToAdd==null ||linkedProductToAdd.trim().length() == 0) ? (linkedProduct[0]) : (linkedProductToAdd + linkedProduct[0]);
						linkedProductToRemove = (linkedProductToRemove==null ||linkedProductToRemove.trim().length() == 0) ? (linkedProduct[1]) : (linkedProductToRemove + linkedProduct[1]);
						//addLinkedProductToRightsuit(linkedProduct[0],enforceableProductDto.getProductId());
					}
				}
			}
			
			
			if (linkedProductToAdd != null && linkedProductToAdd.trim().length() > 0 ) {
				String [] productToAdd = linkedProductToAdd.split(RECORD_SEPARATOR) ;
				for ( String product : productToAdd) {
					//String [] linkedProduct = splitIntoFields(product) ; 
					//Product prod = getProductById(linkedProduct[0]);
					addLinkedProductToRightsuit(product,enforceableProductDto.getProductId());
				}
			}
			
			
			if (linkedProductToRemove != null && linkedProductToRemove.trim().length() > 0 ) {
				String [] productToRemove = linkedProductToRemove.split(RECORD_SEPARATOR) ;
				for ( String product : productToRemove) {
					//String [] linkedProduct = splitIntoFields(product) ; 
					removeLinkedProductFromRightsuit(product, enforceableProductDto.getProductId());//addLinkedProduct(enforceableProductDto.getErightsId(),Integer.parseInt(linkedProduct[0]));
				}
			}
			
			String externalIdsToRemove = productBean.getExternalIdsToRemove() ;
			if (externalIdsToRemove != null && externalIdsToRemove.trim().length() > 0 ) {
				String [] externalIdRemove = externalIdsToRemove.split(RECORD_SEPARATOR) ;
				for ( String external : externalIdRemove) {
					String [] externalProduct = splitIntoFields(external) ; 
					
					removeExternalProductFromRightsuit(externalProduct[0], productBean.getProductId());//addLinkedProduct(enforceableProductDto.getErightsId(),Integer.parseInt(linkedProduct[0]));
				}
			}
		}
		erightsFacade.updateProduct(enforceableProductDto);
						
	}
	
	@Override
	@PreAuthorize("hasRole('ROLE_UPDATE_PRODUCT')")
	public void updateLinkedProducts(final ProductBean productBean,final RegisterableProduct pd) throws ServiceLayerException, ErightsException, UnsupportedEncodingException	
	{
		//product de-duplication(add comments to remove compilation error)
		Set<LinkedProduct> linkedProductsToAdd=null ; //pd.getLinkedProducts();
		if(linkedProductsToAdd!= null && linkedProductsToAdd.size()>0)
		{
			for(Product product:linkedProductsToAdd)
			{ 
				erightsFacade.addLinkedProduct(product.getLinkedProduct().getId(),pd.getId());
				//erightsFacade.addLinkedProduct(product.getErightsId(), pd.getErightsId());
						
			}
		}
		
		if(productBean.getLinkedProductsToRemoveErightsId()!= null && productBean.getLinkedProductsToRemoveErightsId().size()>0)
		{
			for(String erightsId:productBean.getLinkedProductsToRemoveErightsId())
			{
				erightsFacade.removeLinkedProduct(erightsId, pd.getId());
			}
		}
	
	}
	
	
	private String[] splitIntoFields(final String record) throws UnsupportedEncodingException {
		String[] fields = record.trim().split(FIELD_SEPARATOR);
		for (int i = 0; i < fields.length; i++) {
			fields[i] = URLDecoder.decode(fields[i], "UTF-8");
		}
		return fields;
	}

	/**
	 * Save or update a product.
	 * 
	 * @param pd
	 *      The product  to save or update.
	 * @return
	 *      The updated product.
	 */
	@Override
	@PreAuthorize("hasRole('ROLE_CREATE_PRODUCT')")
	public final void saveOrUpdateProduct(final Product pd) throws ServiceLayerException {
		productDao.saveOrUpdate(pd);
	}

	/**
	 * Get a product by id.
	 * 
	 * @param id
	 *      The id of the product to get
	 * @return
	 *      The product matching the id
	 */
	public final Product getProductById(final String id) throws ServiceLayerException {
		return productDao.getProductById(id);
	}

	@Override
	public EnforceableProductDto getProductByExternalProductId(String systemId, String typeId, String externalId) throws ServiceLayerException {
		EnforceableProductDto product = null ;
		 
		try {
			ExternalIdDto externalIdDto = new ExternalIdDto(systemId, typeId, externalId ) ;
			product = erightsFacade.getProductByExternalId(externalIdDto) ;
		} catch (ProductNotFoundException e) {
			throw new ServiceLayerException(e.getMessage()) ;
		} catch (ErightsException e) {
			// TODO Auto-generated catch block
			throw new ServiceLayerException(e.getMessage()) ;
		}
	
	 return product ;
		//return productDao.getProductByExternalProductId(systemId, typeId, externalId);
	}

	@Override
	public List<ProductBean> getProductByErightsId(String eRightsId, Map<String,String> adminDivisionList) throws ProductNotFoundException, ErightsException {
		EnforceableProductDto erightsProduct = erightsFacade.getProduct(eRightsId);
		ProductBean product = new ProductBean(erightsProduct.getProductId());
		
		if(erightsProduct.getRegisterableType().toString().equals(RegisterableType.SELF_REGISTERABLE)){
			product.setRegistrationType(RegisterableType.SELF_REGISTERABLE.name());
		}else
			product.setRegistrationType(RegisterableType.ADMIN_REGISTERABLE.name());
		product.setProductName(erightsProduct.getName());
		product.setEmail(erightsProduct.getAdminEmail());
		product.setActivationStrategy(erightsProduct.getActivationStrategy());
		product.setDivisionType(adminDivisionList.get(erightsProduct.getDivisionId()));
		List<ProductBean> retList = new ArrayList<ProductBean>();
		retList.add(product);
	/*	List<Product> retList=this.productDao.getProductByErightsId(eRightsId);
		if(retList!=null && !retList.isEmpty()){
			Set<ExternalProductId> extSet=retList.get(0).getExternalIds();
			if(extSet!=null && !extSet.isEmpty()){
				for (ExternalProductId externalProductId : extSet) {
					if(externalProductId!=null){
						externalProductId.getExternalSystemIdType().getExternalSystem().getId();
					}
				}
			}
		}*/
		return retList;
	}
	
	@Override
	public List<Product> getProductByErightsId(String eRightsId) {
		List<Product> retList=this.productDao.getProductByErightsId(eRightsId);
		/* Unnecessary call. Removed. -Abhi
		 * if(retList!=null && !retList.isEmpty()){
			Set<ExternalProductId> extSet=retList.get(0).getExternalIds();
			if(extSet!=null && !extSet.isEmpty()){
				for (ExternalProductId externalProductId : extSet) {
					if(externalProductId!=null){
						externalProductId.getExternalSystemIdType().getExternalSystem().getId();
					}
				}
			}
		}*/
		return retList;
	}

	@Override
	public List<Product> getProductAndExternalIdsByErightsId(String eRightsProductId) {
		return this.productDao.getProductAndExternalIdsByErightsId(eRightsProductId);
	}

	@Override
	public List<Product> getProductsByErightsIdsAndAdminUser(final List<String> erightsIds, final AdminUser adminUser) {
		return this.productDao.getProductsByErightsIdsAndAdminUser(erightsIds, adminUser);
	}
	
	
	@Override
	public boolean isProductUsed(String productId) {
		//product de-duplication
		//boolean result = this.productDao.isProductUsed(productId);
		int productErightsId ;
		boolean result = false ; 
		if ( !result ) {
			AmazonSearchRequest request = new AmazonSearchRequest();
	    	List<AmazonSearchFields> searchFieldsList = new ArrayList<AmazonSearchFields>();
	    	
	    	
	    	if(productId != null ){
	    		AmazonSearchFields searchField = new AmazonSearchFields();
	    		searchField.setName(SearchDomainFields.PRODUCT_PRODUCTID);
	    		searchField.setValue(productId);
	    		searchField.setType("String");
	    		searchFieldsList.add(searchField);
	    	}
	    	
	    	List<String> searchResultFieldsList = new ArrayList<String>();
			searchResultFieldsList.add(SearchDomainFields.PRODUCT_PRODUCTID);
			searchResultFieldsList.add(SearchDomainFields.PRODUCT_PRODUCTGROUPIDS);
			request.setResultsPerPage(10);
			//request.setSearchResultFieldsList(searchResultFieldsList);
	    	request.setSearchFieldsList(searchFieldsList);
	    	
	    	AmazonCloudSearchServiceImpl cloudSearch = new AmazonCloudSearchServiceImpl();
	    	AmazonSearchResponse response = cloudSearch.searchProduct(request);
	    	List<Map<String, String>> fields = response.getResultFields();
			for (Map<String , String> field: fields){
				if(field.get(SearchDomainFields.PRODUCT_PRODUCTID)!=null){
					productId = String.valueOf(field.get(SearchDomainFields.PRODUCT_PRODUCTID).replaceAll("\\[", "").replaceAll("\\]","")) ;
				}
				if(field.get(SearchDomainFields.PRODUCT_PRODUCTGROUPIDS)!=null){
					result = true ;
				}
			}
	    	
		}
		if(!result) {
			AmazonSearchRequest request = new AmazonSearchRequest();
	    	List<AmazonSearchFields> searchFieldsList = new ArrayList<AmazonSearchFields>();
	    	
	    	
	    	if(productId != null ){
	    		AmazonSearchFields searchField = new AmazonSearchFields();
	    		searchField.setName(SearchDomainFields.LICENSE_PRODUCT_ID);
	    		searchField.setValue(productId);
	    		searchField.setType("String");
	    		searchFieldsList.add(searchField);
	    	}
	    	List<String> searchResultFieldsList = new ArrayList<String>();
			searchResultFieldsList.add(SearchDomainFields.LICENSE_LICENSE_ID);
			request.setSearchResultFieldsList(searchResultFieldsList);
	    	request.setSearchFieldsList(searchFieldsList);
	    	
	    	AmazonCloudSearchServiceImpl cloudSearch = new AmazonCloudSearchServiceImpl();
	    	AmazonSearchResponse response = cloudSearch.searchLicense(request);
	    	result = response.getResultsFound() > 0 ;
		}
		if ( !result ) {
			AmazonSearchRequest request = new AmazonSearchRequest();
	    	List<AmazonSearchFields> searchFieldsList = new ArrayList<AmazonSearchFields>();
	    	
	    	
	    	if(productId != null ){
	    		AmazonSearchFields searchField = new AmazonSearchFields();
	    		searchField.setName(SearchDomainFields.CLAIM_TICKET_PRODUCTID);
	    		searchField.setValue(productId);
	    		searchField.setType("String");
	    		searchFieldsList.add(searchField);
	    	}
	    	
	    	List<String> searchResultFieldsList = new ArrayList<String>();
			searchResultFieldsList.add(SearchDomainFields.CLAIM_TICKET_CLAIMTICKETID);
			
			request.setSearchResultFieldsList(searchResultFieldsList);
	    	request.setSearchFieldsList(searchFieldsList);
	    	
	    	AmazonCloudSearchServiceImpl cloudSearch = new AmazonCloudSearchServiceImpl();
	    	AmazonSearchResponse response = cloudSearch.searchActivationCode(request);
	    	result = response.getResultsFound() > 0 ;
		}
		
		
		
		return result;
	}

	@Override
	@PreAuthorize("hasRole('ROLE_DELETE_PRODUCT')")
	public boolean deleteUnUsedProduct(String productId) throws ServiceLayerException, ProductNotFoundException, ChildProductFoundException, ErightsException {
		boolean result = false;
		/*boolean used = isProductUsed(productId);*/
		Product product= new RegisterableProduct();
		//String failedMessage = String.format("Failed to delete product with id[%s]", productId);
			product.setId(productId);
			if (null != product ){
				//productErightsId = product.getErightsId() ;
				//product de-duplication
				/*if (!used) {
					boolean deleted = this.productDao.deleteUnusedProduct(productId);
					if(deleted){
						this.erightsFacade.deleteProduct(productErightsId);
					}
					result = deleted;
					this.erightsFacade.deleteProduct(productErightsId);
					result = true;
				}*/
				this.erightsFacade.deleteProduct(productId);
				result = true;
			}
		
		return result;
	}
	
	@Override
	public List<LinkedProduct> getProductsLinkedDirectToProduct(Product product) throws ServiceLayerException {
		return this.productDao.getProductsLinkedDirectToProduct(product);
	}

	@Override
	public EnforceableProductDto getProductByName(String name) throws ErightsException {
		return erightsFacade.getProductByName(name) ;
		//return this.productDao.getProductByName(name);
	}
	@Override
	public Paging<ProductBean> searchProduct(final RegistrationDefinitionSearchCriteria productSearchCriteria, final PagingCriteria pagingCriteria, final Map<String,String> adminDivisionList) {
		AmazonSearchRequest request = new AmazonSearchRequest();
		request.setResultsPerPage(pagingCriteria.getItemsPerPage());
		//request.setStartAt(pagingCriteria.getFirstResult());
	
		List<AmazonSearchFields> searchFieldsList = new ArrayList<AmazonSearchFields>();
	
	
		if(productSearchCriteria.getProductId() != null ){
			AmazonSearchFields searchField = new AmazonSearchFields();
			searchField.setName(SearchDomainFields.PRODUCT_PRODUCTID);
			searchField.setValue(productSearchCriteria.getProductId());
			searchField.setType("String");
			searchFieldsList.add(searchField);
		}
		if(productSearchCriteria.getExternalId() !=null){
			AmazonSearchFields searchField = new AmazonSearchFields();
			searchField.setName(SearchDomainFields.PRODUCT_EXTERNALIDS);
			searchField.setValue(productSearchCriteria.getExternalId());
			searchField.setType("String");
			searchFieldsList.add(searchField);
		}
		if(productSearchCriteria.getProductName() !=null){
			AmazonSearchFields searchField = new AmazonSearchFields();
			searchField.setName(SearchDomainFields.PRODUCT_PRODUCTNAME);
			String productname = productSearchCriteria.getProductName().replace("'", "");
			searchField.setValue(productname);
			searchField.setType("String");
			searchFieldsList.add(searchField);
		}
	
		if (productSearchCriteria.getProductStates() != null && productSearchCriteria.getProductStates().size() > 0) {
			AmazonSearchFields searchField = new AmazonSearchFields();
			searchField.setName(SearchDomainFields.PRODUCT_STATE);
			searchField.setValue(productSearchCriteria.getProductStates().toString());
			searchField.setType("String");
			searchFieldsList.add(searchField);
			System.out.println("state");
		}
	
		if (productSearchCriteria.getRegistrationDefinitionType() != null ) {
			AmazonSearchFields searchField = new AmazonSearchFields();
			searchField.setName(SearchDomainFields.PRODUCT_REGISTRATION_DEFINITION_TYPE);
			searchField.setValue(productSearchCriteria.getRegistrationDefinitionType().toString());
			searchField.setType("String");
			searchFieldsList.add(searchField);
			System.out.println("definition");
		}
	
		if (productSearchCriteria.getPlatformCode() != null ) {
			AmazonSearchFields searchField = new AmazonSearchFields();
			searchField.setName(SearchDomainFields.PRODUCT_PLATFORMS);
			searchField.setValue(productSearchCriteria.getPlatformCode().toString());
			searchField.setType("String");
			searchFieldsList.add(searchField);
		}
		
		if(productSearchCriteria.getDivision()!=null){
			AmazonSearchFields searchField = new AmazonSearchFields();
			searchField.setName(SearchDomainFields.PRODUCT_DIVISIONID);
			searchField.setValue(productSearchCriteria.getDivision().getErightsId().toString());
			searchField.setType("String");
			searchFieldsList.add(searchField);
		} else if(productSearchCriteria.getAdminUser() != null){
			List<Integer> divisionIds = new ArrayList<Integer>() ;
			List<Division> divisionList = null;
			try {
				divisionList = divisionService.getDivisionsByAdminUser(productSearchCriteria.getAdminUser());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(Division division : divisionList) {
				divisionIds.add(division.getErightsId());
			}
			/*for (DivisionAdminUser divisionAdminUser : productSearchCriteria.getAdminUser().getDivisionAdminUsers()) {
    			divisionTypes.add(divisionAdminUser.getDivision().getDivisionType()) ;
			}*/
			AmazonSearchFields searchField = new AmazonSearchFields();
			searchField.setName(SearchDomainFields.PRODUCT_DIVISIONID);
			searchField.setValue(divisionIds.toString());
			searchField.setType("String");
			searchFieldsList.add(searchField);
		}
	
	
	
		List<String> searchResultFieldsList = new ArrayList<String>();
		searchResultFieldsList.add(SearchDomainFields.PRODUCT_DIVISIONID);
		searchResultFieldsList.add(SearchDomainFields.PRODUCT_PRODUCTID);
		searchResultFieldsList.add(SearchDomainFields.PRODUCT_EXTERNALIDS);
		searchResultFieldsList.add(SearchDomainFields.PRODUCT_PRODUCTNAME);
		
		//searchResultFieldsList.add("Createddateto");
	
		HashMap<String, String> hMap1 = new HashMap<String, String>();
		hMap1.put(pagingCriteria.getSortColumn(), "asc");
		//hMap1.put("lastname", "desc");
	
		request.setSortFields(hMap1);
		request.setSearchResultFieldsList(searchResultFieldsList);
	
		request.setSearchFieldsList(searchFieldsList);
		request.setStartAt((pagingCriteria.getRequestedPage()-1)*pagingCriteria.getItemsPerPage());
		System.out.println(request);
		//request.setSortFields(pagingCriteria.getSortColumn());
		//request.setSearchFieldsList(pagingCriteria.get);
	
		AmazonCloudSearchServiceImpl cloudSearch = new AmazonCloudSearchServiceImpl();
		AmazonSearchResponse response = cloudSearch.searchProduct(request);
		ConvertSearch convert = new ConvertSearch();
		List<ProductBean> products = convert.convertProduct(response,adminDivisionList);
		
		//AuditLogger.logEvent("Search products", "Product:"+customerSearchCriteria.getUsername(), AuditLogger.customerSearchCriteria(customerSearchCriteria));
		//return Paging.valueOf(pagingCriteria, customers, count);
		return Paging.valueOf(pagingCriteria, products, response.getResultsFound());}
}
