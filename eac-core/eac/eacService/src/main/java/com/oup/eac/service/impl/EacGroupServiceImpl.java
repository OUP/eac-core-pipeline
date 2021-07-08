package com.oup.eac.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.amazonaws.services.cloudsearchdomain.model.Hits;
import com.constants.SearchDomainFields;
import com.oup.eac.cloudSearch.search.impl.AmazonCloudSearchServiceImpl;
import com.oup.eac.cloudSearch.util.CloudSearchUtils;
import com.oup.eac.cloudSearch.util.CloudSearchUtils.DomainType;
import com.oup.eac.data.EacGroupsDao;
import com.oup.eac.data.ProductDao;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.beans.ProductBean;
import com.oup.eac.domain.paging.Paging;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.domain.paging.PagingCriteria.SortDirection;
import com.oup.eac.domain.search.AmazonSearchFields;
import com.oup.eac.domain.search.AmazonSearchRequest;
import com.oup.eac.domain.search.AmazonSearchResponse;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.EacGroupsSearchCriteria;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.ProductGroupDto;
import com.oup.eac.dto.RegistrationDefinitionSearchCriteria;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.service.DivisionService;
import com.oup.eac.service.EacGroupService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.util.ConvertSearch;

@Service(value="eacGroupService")
public class EacGroupServiceImpl implements EacGroupService{

	private final EacGroupsDao eacGroupDao;
	private final ProductDao productDao;
	private final ErightsFacade erightsFacade;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private DivisionService divisionService;

	@Autowired
	public EacGroupServiceImpl(EacGroupsDao eacGroupDao, ProductDao productDao, ErightsFacade erightsFacade, DivisionService divisionService) {
		super();
		Assert.notNull(eacGroupDao);
		Assert.notNull(productDao);
		Assert.notNull(erightsFacade);
		this.eacGroupDao = eacGroupDao;
		this.productDao = productDao;
		this.erightsFacade=erightsFacade;
		this.divisionService = divisionService ;
	}

	@Override
	public Paging<EacGroups> searchProductGroups(EacGroupsSearchCriteria searchCriteria, PagingCriteria pagingCriteria) {

		AmazonSearchRequest request = new AmazonSearchRequest();
    	request.setResultsPerPage(pagingCriteria.getItemsPerPage());
		List<AmazonSearchFields> searchFieldsList = new ArrayList<AmazonSearchFields>();
		RegistrationDefinitionSearchCriteria productSearchCriteria = new RegistrationDefinitionSearchCriteria();
		
    	if(searchCriteria.getGroupName()!=null){
    		AmazonSearchFields searchField = new AmazonSearchFields();
    		searchField.setName(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPNAME);
    		String groupname = searchCriteria.getGroupName().replace("'", "");
    		searchField.setValue(groupname);
    		searchField.setType("String");
    		searchFieldsList.add(searchField);
    	}
    	boolean productSearch = false;
    	if(searchCriteria.getProductName()!=null){
    		productSearchCriteria.setProductName(searchCriteria.getProductName());
    		productSearch = true;
    	}
    	if(searchCriteria.getProductId()!=null){
    		productSearchCriteria.setProductId(searchCriteria.getProductId());
    		productSearch = true;
    	}
    	if(searchCriteria.getExternalId()!=null){
    		productSearchCriteria.setExternalId(searchCriteria.getExternalId());
    		productSearch = true;
    	}
    	
    	
    	Paging<ProductBean> products = null;
    	List<Division> divisions = new ArrayList<Division>();
		try {
			divisions = divisionService.getAllDivisions();
		} catch (AccessDeniedException | ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String,String> divisionList = new HashMap<String, String>() ;
		
		
		for (Division division : divisions) {
			divisionList.put(division.getErightsId().toString(), division.getDivisionType()) ;
		}
    	if(productSearch){
    		products = productService.searchProduct(productSearchCriteria, PagingCriteria.valueOf(10000, 1, SortDirection.ASC, "productName"),divisionList);
    	}
    	Set<String> productGroupIds = new HashSet<>();
    	if(products != null){
	    	for(ProductBean product :products.getItems()){
	    		if (product.getProductGroupIds() != null) {
	    			productGroupIds.addAll(product.getProductGroupIds());
	    		}
	    	}
    	}
    	if(productGroupIds.size()>0){
    		AmazonSearchFields searchField = new AmazonSearchFields();
    		searchField.setName(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPID);
    		searchField.setValue(Arrays.toString(productGroupIds.toArray()).replace("[", "").replace("]", ""));
    		searchField.setType("String");
    		searchFieldsList.add(searchField);
    	}
    	List<String> searchResultFieldsList = new ArrayList<String>();
		searchResultFieldsList.add(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPID);
		searchResultFieldsList.add(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPNAME);
		
		HashMap<String, String> hMap1 = new HashMap<String, String>();
		hMap1.put(pagingCriteria.getSortColumn(), "asc");
		
		request.setSortFields(hMap1);
		request.setSearchResultFieldsList(searchResultFieldsList);
    	request.setSearchFieldsList(searchFieldsList);
    	request.setStartAt((pagingCriteria.getRequestedPage()-1)*10);
    	
    	AmazonCloudSearchServiceImpl cloudSearch = new AmazonCloudSearchServiceImpl();
    	AmazonSearchResponse response = null;
    	int count = 0;
    	List<EacGroups> productGroups = null;
    	if(products==null || (products!=null && productGroupIds.size()>0)){
    		response = cloudSearch.searchProductGroups(request);
    		ConvertSearch convert = new ConvertSearch();
    		productGroups = convert.convertProductGroups(response);
        	count = response.getResultsFound();
    	} else {
    		productGroups = new ArrayList<EacGroups>();
    	}
    	
//		List<EacGroups> productGroups = eacGroupDao.searchProductGroups(searchCriteria, pagingCriteria);
//		int count = eacGroupDao.countSearchProductGroups(searchCriteria);		
		AuditLogger.logEvent("Search Product Group", "Product Id:"+searchCriteria.getProductId(),AuditLogger.eacGroupsSearchCriteria(searchCriteria));
		return Paging.valueOf(pagingCriteria, productGroups, count);
	}

	@Override
	public EacGroups getEacGroupById(final String eacGroupId) {
		/*
		Fetch From cloud
		EacGroups eacGroups = new EacGroups();
		CloudSearchUtils client = new CloudSearchUtils(DomainType.PRODUCTGROUP);
		Hits searchResult = client.searchDocument(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPID, eacGroupId);
		if (searchResult.getFound() != 0) {
			Map<String, List<String>> fields = searchResult.getHit().get(0).getFields();
			eacGroups.setId(fields.get(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPID).get(0));
			eacGroups.setGroupName(fields.get(SearchDomainFields.PRODUCT_GRP_PRODUCTGROUPNAME).get(0));
		}
		return eacGroups;*/
		//TODO : remove below eac call
		return eacGroupDao.getEacGroupById(eacGroupId);
	}
	
	@Override
	public ProductGroupDto getProductGroupDtoByErightsId(String groupId) throws ErightsException{ 
		return erightsFacade.getProductGroup(groupId, null) ;
	}

	@Override
	@PreAuthorize("hasRole('ROLE_UPDATE_PRODUCT_GROUP')")
	public void updateEacGroup(final EacGroups eacGroup, Set<String> productIdsToAdd, Set<String> productIdsToRemove, String currentEacGroupName) throws ServiceLayerException, ProductNotFoundException, ErightsException {

		ProductGroupDto productGroupDto = new ProductGroupDto();
		//List<Integer> productIds= productGroupDto.getProductIds();

		for (String productId : productIdsToAdd) {
			Product product = new RegisterableProduct() ;
			product.setId(productId);
			eacGroup.addProduct(product);
		}
		for (String productId : productIdsToRemove) {
			
			for (Product product : eacGroup.getProducts()) {
				if (product.getId().equals(productId)) {
					eacGroup.removeProduct(product);
					break ;
				}
			}
			//De-duplication
			/*Product product = productService.getProductById(productId);
			eacGroup.removeProduct(product);*/
		}
		for(Product prod:eacGroup.getProducts()){
			productGroupDto.getProductIds().add(prod.getId());
		}

		productGroupDto.setProductGroupName(eacGroup.getGroupName());
		productGroupDto.setCurrentGroupName(currentEacGroupName);
		erightsFacade.updateProductGroup(productGroupDto);
		//De-duplication
		//eacGroupDao.update(eacGroup);
		AuditLogger.logEvent("Update Product Group", "Product Id:"+productGroupDto.getProductGroupId(),AuditLogger.eacGroup(eacGroup));
	}

	@Override
	@PreAuthorize("hasRole('ROLE_CREATE_PRODUCT_GROUP')")
	public void createEacGroup(final EacGroups eacGroup) throws ServiceLayerException, ProductNotFoundException, ErightsException {
		ProductGroupDto productGroupDto = new ProductGroupDto();
		List<String> productIds = new ArrayList<String>();
		for(Product prod:eacGroup.getProducts()){
			productIds.add(prod.getId());
		}
		productGroupDto.setProductIds(productIds);
		productGroupDto.setProductGroupName(eacGroup.getGroupName());
		erightsFacade.createProductGroup(productGroupDto);
		//De-duplication
		//eacGroupDao.save(eacGroup);
		AuditLogger.logEvent("Create Product Group", "Product Id:"+productGroupDto.getProductGroupId(),AuditLogger.eacGroup(eacGroup));
	}

	@Override
	@PreAuthorize("hasRole('ROLE_DELETE_PRODUCT_GROUP')")
	public boolean deleteUnUsedEacGroup(String eacGroupId) throws ServiceLayerException, ProductNotFoundException, ErightsException {
		boolean result = false;
		boolean used = this.isEacGroupUsed(eacGroupId);
		if(!used){
			//De-duplication
			//boolean regDefDeleted = eacGroupDao.deleteRegistrationDefinitionOfEacGroup(eacGroupId);
			//if(regDefDeleted){
			ProductGroupDto productGroupDto = this.getProductGroupDtoByErightsId(eacGroupId);
			EacGroups eacGroup = this.getEacGroupByProductGroupDto(productGroupDto);	
			//De-duplication
			//this.getEacGroupById(eacGroupId);
				if(productGroupDto != null)
					erightsFacade.deleteProductGroup(productGroupDto.getProductGroupName());
				//De-duplication
				//eacGroupDao.delete(eacGroup);
				result = true;
				if(productGroupDto != null)
					AuditLogger.logEvent("Delete Product Group", "Product Id:" + productGroupDto.getProductGroupId(),AuditLogger.eacGroup(eacGroup));
			//}
		}
		
		return result;
	}

	@Override
	public boolean isEacGroupUsed(String eacGroupId) throws ServiceLayerException {
		
		boolean used = false;
		CloudSearchUtils client = new CloudSearchUtils(DomainType.ACTIVATIONCODE);
		eacGroupId = "'"+eacGroupId+"'" ;
		Hits searchResult = client.searchDocument(SearchDomainFields.CLAIM_TICKET_PRODUCTGROUPID, eacGroupId);
		if (searchResult.getFound() != 0) {
			used = true;
		}
		return used;
		//De-duplication
		//return eacGroupDao.isEacGroupUsed(eacGroupId);
	}

	@Override
	public EacGroups getEacGroupByProductGroupDto(ProductGroupDto productGroupDto) throws ServiceLayerException {
		EacGroups eacGroups = new EacGroups();
		EnforceableProductDto enforceableProductDto = new EnforceableProductDto();
		List<String> productIds = productGroupDto.getProductIds();
		Set<Product> productSet = new HashSet<Product>();
		if (productGroupDto.getProductGroupId() != null) {
			eacGroups.setId(productGroupDto.getProductGroupId().toString());
		}
		eacGroups.setGroupName(productGroupDto.getProductGroupName());
		
		for (String productId : productIds) {
			List<Product> products = new ArrayList<Product>();
			Product prod = new RegisterableProduct();
			//products = productService.getProductByErightsId(productId);
				try {
					enforceableProductDto = productService.getEnforceableProductByErightsId(productId);
				} catch (ErightsException e) {
					e.printStackTrace();
				}
				prod.setId(enforceableProductDto.getProductId());
				prod.setProductName(enforceableProductDto.getName());
				products.add(prod);
			productSet.addAll(products);
			eacGroups.setProducts(productSet);
		}	
		return eacGroups;	
	}
	
	@Override
	public EacGroups getEacGroupByName(String name) throws ServiceLayerException {
		ProductGroupDto productGroupDto = null;
		EacGroups eacGroups = new EacGroups(); 
		try {
			productGroupDto = erightsFacade.getProductGroup(null, name);
		} catch (ErightsException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		if (productGroupDto != null) {
			eacGroups = getEacGroupByProductGroupDto(productGroupDto);
		}
		return eacGroups;
		
		//De-duplication
		//return eacGroupDao.getEacGroupByName(name);
	}

	@Override
	public void updateIsEditable(EacGroups eacGroup) throws ServiceLayerException {
		//updateEacGroup(eacGroup, productIdsToAdd, productIdsToRemove, currentEacGroupName);
		eacGroupDao.update(eacGroup);
	}

	@Override
	public String getEacGroupIdByName(String name) throws ServiceLayerException {
		ProductGroupDto productGroupDto = new ProductGroupDto();
		String productGroupId = null;
		try {
			productGroupDto = erightsFacade.getProductGroup(null, name);
		} catch (ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (productGroupDto != null) {
			productGroupId = productGroupDto.getProductGroupId().toString();
		}
		
		return productGroupId;
		
		//De-duplication
		//return eacGroupDao.getEacGroupIdByName(name);
	}
}
