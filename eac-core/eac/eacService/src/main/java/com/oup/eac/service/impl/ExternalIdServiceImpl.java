package com.oup.eac.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.oup.eac.data.ExternalIdDao;
import com.oup.eac.data.ExternalSystemDao;
import com.oup.eac.data.ExternalSystemIdTypeDao;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ExternalCustomerId;
import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.ExternalCustomerIdDto;
import com.oup.eac.dto.ExternalIdDto;
import com.oup.eac.dto.ExternalProductIdDto;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.ExternalIdTakenException;
import com.oup.eac.service.exceptions.ServiceLayerValidationException;

/**
 * Service for ExternalCustomerIds and ExteralProductIds
 * 
 * @author David Hay 
 */
@Service(value="externalIdService")
public class ExternalIdServiceImpl implements ExternalIdService {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ExternalIdServiceImpl.class);

	private final ExternalSystemDao externalSystemDao;

	private final ExternalSystemIdTypeDao externalSystemTypeDao;

	private final ExternalIdDao externalIdDao;

	private final CustomerService customerService;

	//Added for Product External ID
	private final ProductService productService;
	
	private final ErightsFacade erightsFacade;
	
	
 
	/**
	 * Instantiates a new customer service impl.
	 * 
	 * @param extSysDao
	 *            the ext sys dao
	 * @param externalIdDao
	 *            the external id dao
	 * @param extSysIdType
	 *            the ext sys id type
	 * @param customerService
	 *            The {@link CustomerService}
	 */
	@Autowired
	public ExternalIdServiceImpl(final ExternalSystemDao extSysDao, final ExternalSystemIdTypeDao externalSystemTypeDao, final ExternalIdDao externalIdDao,
			final CustomerService customerService, ProductService productService,final ErightsFacade erightsFacade) {
		super();
		Assert.notNull(extSysDao);
		Assert.notNull(externalSystemTypeDao);
		Assert.notNull(externalIdDao);
		Assert.notNull(productService);
		Assert.notNull(erightsFacade);
		this.externalSystemDao = extSysDao;
		this.externalSystemTypeDao = externalSystemTypeDao;
		this.externalIdDao = externalIdDao;
		this.customerService = customerService;
		this.productService= productService;
		this.erightsFacade = erightsFacade;
	}



	@Override
	public ExternalCustomerIdDto getExternalCustomerIds(Customer customer, String systemId) {
		ExternalCustomerIdDto result = new ExternalCustomerIdDto(customer);
		if (StringUtils.isBlank(systemId)) {
			result = new ExternalCustomerIdDto(customer);
		} else {
			List<ExternalCustomerId> externalCustomerIdList = new ArrayList<ExternalCustomerId>();
			for (ExternalCustomerId extId:customer.getExternalIds()){
				if(systemId.equals(extId.getExternalSystemIdType().getExternalSystem().getName()));
				externalCustomerIdList.add(extId);
			}
			result.setExternalCustomerIds(externalCustomerIdList );
			//result = externalIdDao.getExternalIdsForCustomer(customer, systemId);
		}
		return result;
	}

	@Override
	public ExternalProductIdDto getExternalProductIds(List<Product> products, String systemId) {
		ExternalProductIdDto result;
		 Map<String, List<ExternalProductId>> externalPro = new HashMap<String, List<ExternalProductId>>();
		if (StringUtils.isBlank(systemId)) {
			result = new ExternalProductIdDto(products);
		} else {
			//Uncomment when product is completed
			for(Product product : products){
				List<ExternalProductId> extIds = new ArrayList<ExternalProductId>();
				if(product.getExternalIds()!=null && product.getExternalIds().size()>0)
					for (ExternalProductId extId : product.getExternalIds()){
						if(extId.getExternalSystemIdType()!=null && extId.getExternalSystemIdType().getExternalSystem()!=null
								&& extId.getExternalSystemIdType().getExternalSystem().getName()!=null){
							if(extId.getExternalSystemIdType().getExternalSystem().getName().equals(systemId)){
								extIds.add(extId);
							}
						}
					}
				externalPro.put(product.getId(), extIds);
			}
			result = new ExternalProductIdDto(products, externalPro);
			//result = externalIdDao.getExternalIdsForProducts(products, systemId);
		}
		return result;
	}

	@Override
	public ExternalProductIdDto getExternalProductIds(List<Product> products) {
		ExternalProductIdDto result;
		result = externalIdDao.getExternalIdsForProducts(products, null);
		return result;
	}

	@Override
	public ExternalProductIdDto getExternalProductIdsForProductsAndLinkedProducts(List<Product> products, String systemId) {
		ExternalProductIdDto result;
		if (StringUtils.isBlank(systemId)) {
			result = new ExternalProductIdDto(products);
		} else {
			result = externalIdDao.getExternalIdsForProductsAndLinkedProducts(products, systemId);
		}
		return result;
	}

	/**
	 * This method does not create external system ids where the specified
	 * external system does not exist.
	 */
	 @Override
	 public void saveExternalCustomerIdsForSystem(final Customer customer, final String systemId, List<ExternalIdDto> dtos) throws ServiceLayerException {
		 try {
			setExternalCustomerIdsForSystem(false, customer, systemId, dtos);
		} catch (ErightsException e) {
			throw new ServiceLayerException(e.getMessage()); 
		}
	 }

	 private void setExternalCustomerIdsForSystem(final boolean isSave, final Customer customer, final String systemId, List<ExternalIdDto> dtos) throws ServiceLayerException, ErightsException {
		// boolean updated = false;

		 checkExternalCustomerIdsAreNotTaken(customer, dtos);

		 ExternalSystem extSys = new ExternalSystem();
		 extSys = getExternalSystem(systemId);
		 Map<String, ExternalSystemIdType> typeMap = getTypeMap(extSys, dtos);

		 /*List<ExternalCustomerId> ids = this.externalIdDao.findExternalCustomerIdByCustomerAndSystem(customer, extSys);       
		 for(ExternalCustomerId id :ids){            
			 externalIdDao.delete(id);
			 updated = true;
		 }
		 externalIdDao.flush();*/

		 Set<ExternalSystemIdType> types = new HashSet<ExternalSystemIdType>();
		 int count = 0;
		 /*Customer cust = new Customer();
		 cust= customerService.getCustomerByUsername(customer.getUsername());*/
		
			 List<ExternalCustomerId> extSysList = new ArrayList<ExternalCustomerId>();
			 for(ExternalCustomerId extId : customer.getExternalIds()){
				 if(extId.getExternalSystemIdType().getExternalSystem().getName().equals(extSys.getName()))
					 extSysList.add(extId);
			 }
			 customer.getExternalIds().removeAll(extSysList);
		 
		 
		 for(ExternalIdDto dto : dtos){
			 ExternalCustomerId id = new ExternalCustomerId();
			 id.setCustomer(customer);
			 id.setExternalId(dto.getId());
			 ExternalSystemIdType typeId = typeMap.get(dto.getType());
				 if(types.contains(typeId.getExternalSystem()) && types.contains(typeId.getExternalSystem().getExternalSystemIdTypes())){
					 throw new ServiceLayerValidationException("The external system id type [" + typeId.getName() + "] can only be associated with a single external id value");
				 }
			 types.add(typeId);
			 id.setExternalSystemIdType(typeId);
			// externalIdDao.save(id);//save the new external system id
			 AuditLogger.logAddExternalCustomerId(isSave, customer, id);
			 customer.getExternalIds().add(id);
			// updated = true;
			 count++;
		 }
		
			//Added to set ExtId in atypon
			
			 
			 customerService.updateCustomer(customer, false);
		 
		/* if (updated) {
			 customerService.markUpdated(customer);
		 }*/
		 if(count == 0){
			 AuditLogger.logEvent(customer, "Cleared External Ids for system:"+systemId.toLowerCase());
		 }
	 }


	 /**
	  * {@inheritDoc}
	  */
	 @Override
	 public final void saveExternalCustomerIdsForSystemCreatingCustomer(
			 final Customer customer, final String systemId, final List<ExternalIdDto> dtos) throws ServiceLayerException, ErightsException {

		 /**************************************************************************************************
		  * We must do all database work BEFORE creating user in atypon.
		  * If we did get a problem with database work after creating user in atypon - we can't roll atypon back. 
		  **************************************************************************************************/

		 Customer eacCustomer = new Customer();
		 //save the customer record to db
		 //customerService.saveCustomerPart1(eacCustomer, false);

		 //save the external ids to db - with validation check too.
		 //setExternalCustomerIdsForSystem(true, customer, systemId, dtos);
		 //checkExternalCustomerIdsAreNotTaken(customer, dtos);
		 /*ExternalSystem extSys = null;
		 try{
			 extSys = getExternalSystem(systemId);
		 }catch(ErightsException e){
			 throw new ServiceLayerValidationException("No system id found for " + systemId); 
		 }*/
		 List<ExternalSystemIdType> listType = new ArrayList<ExternalSystemIdType>();
		 //Map<String, ExternalSystemIdType> typeMap = getTypeMap(extSys, dtos);
		 
		 for(ExternalIdDto dto : dtos){
			 ExternalCustomerId id = new ExternalCustomerId();
			 id.setCustomer(customer);
			 id.setExternalId(dto.getId());
			 ExternalSystemIdType typeId = new ExternalSystemIdType() ;
			 typeId.setId(dto.getType());
			 typeId.setName(dto.getType()) ;
			 ExternalSystem extSystem = new ExternalSystem() ;
			 extSystem.setId(dto.getSystemId());
			 extSystem.setName(dto.getSystemId());
			 typeId.setExternalSystem(extSystem);
			 if(listType.contains(typeId)){
				 throw new ServiceLayerValidationException("The external system id type [" + typeId.getName() + "] can only be associated with a single external id value");
			 }
			 listType.add(typeId);
			 id.setExternalSystemIdType(typeId);
			 customer.getExternalIds().add(id);
		 }	
		 
		 
		 //create the customer in erights (after we know that db operations aren't going to fail)
		 customerService.saveCustomerPart2(eacCustomer, customer, false);
		 AuditLogger.customerSaved(customer);
	 }


	 private void checkExternalCustomerIdsAreNotTaken(Customer customer, List<ExternalIdDto> dtos) throws ServiceLayerValidationException, ErightsException {
		 for (ExternalIdDto dto : dtos) {
			 String systemId = dto.getSystemId();
			 String systemTypeId = dto.getType();
			 String externalId = dto.getId();

			 Customer cust = customerService.getCustomerByExternalCustomerId(systemId, systemTypeId, externalId);
			 if (cust == null) {
				 continue;
			 }
			 Set<ExternalCustomerId> extIds = cust.getExternalIds();

			 //ExternalCustomerId extId = externalIdDao.findExternalCustomerIdBySystemAndTypeAndExternalId(systemId, systemTypeId, extrlId.getExternalId());
			 for(ExternalCustomerId extId:extIds){
				 if (extId != null && (!cust.getId().equals(customer.getId()))) {
					 // a different customer is associated with this external customer id
					 String msg = String.format("The externalCustomerId : Id[%s] systemId[%s] typeId[%s] has been associated with another customer", externalId,
							 systemId, systemTypeId);
					 throw new ExternalIdTakenException(msg,dto);
				 }
			 }
		 }
	 }

	 @Override
	 public void checkExistingExternalCustomerIds(Customer customer, List<ExternalCustomerId> ids) throws ServiceLayerValidationException, ErightsException {    	
		 List<ExternalIdDto> dtos = new ArrayList<ExternalIdDto>();    	
		 if(ids != null){
			 for(ExternalCustomerId id : ids){
				 ExternalSystemIdType type = id.getExternalSystemIdType();
				 String typeId = type.getName();
				 String systemId = type.getExternalSystem().getName();
				 String externalId = id.getExternalId();
				 ExternalIdDto dto = new ExternalIdDto(systemId,typeId,externalId);
				 dtos.add(dto);
			 }
		 }
		 checkExternalCustomerIdsAreNotTaken(customer, dtos);
	 }

	 /**
	  * This method fails does not create external system ids where the specified external system does not exist. 
	  * @throws ErightsException 
	  */
	 @Override
	 public void saveExternalProductIdsForSystem(final Product product, final String systemId, List<ExternalIdDto> dtos, RegisterableProduct regProduct) throws ServiceLayerException, ErightsException {

		 // check in rs
//		 checkExternalProductIdsAreNotTaken(product, dtos);
		 
		 for (ExternalIdDto extIdDto : dtos) {	
			 boolean flag = externalProductIdInUse(product.getId(), extIdDto); 
			 if(flag) {

				 String msg = String.format("The externalProductId : Id[%s] systemId[%s] typeId[%s] has been associated with another product", extIdDto.getId(),
						 systemId, extIdDto.getType());
				 throw new ExternalIdTakenException(msg,extIdDto);
			 }
		 }

		 //  get from erights
		 ExternalSystem extSys = getExternalSystem(systemId);
		 Map<String, ExternalSystemIdType> typeMap = getTypeMap(extSys, dtos);

		 /*List<ExternalProductId> ids = this.externalIdDao.findExternalProductIdByProductAndSystem(product, extSys);
		 if(ids != null){
			 for(ExternalProductId id :ids){            
				 externalIdDao.delete(id);
			 }
		 }*/
		 
		 //comment eac db save
//		 externalIdDao.flush();

		 Set<ExternalSystemIdType> types = new HashSet<ExternalSystemIdType>();
		 int count = 0;
		 ExternalProductId id = new ExternalProductId();
		 for(ExternalIdDto dto : dtos){
			 id.setProduct(product);
			 id.setExternalId(dto.getId());
			 ExternalSystemIdType typeId = typeMap.get(dto.getType());
			 if(types.contains(typeId.getExternalSystem()) && types.contains(typeId.getExternalSystem().getExternalSystemIdTypes())){
				 throw new ServiceLayerValidationException("The external system id type [" + typeId.getName() + "] can only be associated with a single external id value");
			 }
			 types.add(typeId);
			 id.setExternalSystemIdType(typeId);
			 
			 //comment 
//			 externalIdDao.save(id);//save the new external system id
			 auditExtProductId(product, dto);
			 count++;
		 }
		 //regProduct.getExternalIds().add(id);
		 if(regProduct!=null){
			 this.productService.updateRegisterableProduct(regProduct);
		 }
		 
		 // for audit log
		 if(count == 0){
			 auditExtProductIdClear(product, systemId);
		 }
	 }

	 private void checkExternalProductIdsAreNotTaken(Product product, List<ExternalIdDto> dtos) throws ServiceLayerValidationException{
		 for (ExternalIdDto dto : dtos) {
			 String systemId = dto.getSystemId();
			 String systemTypeId = dto.getType();
			 String externalId = dto.getId();
			 ExternalProductId extId = externalIdDao.findExternalProductIdBySystemAndTypeAndExternalId(systemId, systemTypeId, externalId);
			 if (extId != null && (!extId.getProduct().getId().equals(product.getId()))) {
				 // a different product is associated with this external product id
				 String msg = String.format("The externalProductId : Id[%s] systemId[%s] typeId[%s] has been associated with another product", externalId,
						 systemId, systemTypeId);
				 throw new ExternalIdTakenException(msg,dto);
			 }
		 }
	 }



	 private void auditExtProductIdClear(final Product product, final String systemId) {
		 String msg = String.format("Cleared ExternalProductIds for %s and systemId:%s", AuditLogger.product(product), systemId.toLowerCase());
		 AuditLogger.logEvent(msg);
	 }

	 private void auditExtProductId(final Product product, final ExternalIdDto dto) {
		 String msg = String.format("Added ExternalProductId[%s, sys:%s, typeId:%s, extId:%s]",AuditLogger.product(product), dto.getSystemId(), dto.getType(), dto.getId());
		 AuditLogger.logEvent(msg);
	 }

	 private Map<String, ExternalSystemIdType> getTypeMap(final ExternalSystem extSys, List<ExternalIdDto> dtos) throws ServiceLayerValidationException{
		 //create a lookup for external system id types
		 Map<String, ExternalSystemIdType> typeMap = new HashMap<String,ExternalSystemIdType>();
		 for(ExternalIdDto dto : dtos){
			 String type = dto.getType();
			 for(ExternalSystemIdType typeId: extSys.getExternalSystemIdTypes()){
				 if(typeId!=null && type.equals(typeId.getName()))
					 typeMap.put(type,typeId);
			 }
			 if(typeMap.get(type) == null){
				 throw new ServiceLayerValidationException("Type ID = '"+type+"' not found for System ID = '"+dto.getSystemId()+"'");
			 }
			 //ExternalSystemIdType typeId = getExternalSystemIdType(extSys,type);
			 
		 }
		 return typeMap;

	 }
	 /*public ExternalSystemIdType getExternalSystemIdType(ExternalSystem extSys, String type){
		 extSys.getExternalSystemIdTypes().contains(type);
		 ExternalSystemIdType typeId = this.externalSystemTypeDao.findByExternalSystemAndName(extSys, type);
		 if(typeId == null){
			 //create a new external system id type
			 typeId = new ExternalSystemIdType();
			 typeId.setName(type);
			 typeId.setDescription("desc for " + type);
			 typeId.setExternalSystem(extSys);
			 this.externalSystemTypeDao.save(typeId);
		 }
		 return typeId;

	 }*/

	 private ExternalSystem getExternalSystem(String systemId) throws ErightsException{
		 ExternalSystem extSys;
		try {
			extSys = erightsFacade.getExternalSystem(systemId);
		} catch (AccessDeniedException | ErightsException e) {
			throw new ErightsException(e.getMessage());
		}
		/* if(extSys == null){
			 extSys = new ExternalSystem();
			 extSys.setName(systemId);
			 extSys.setDescription("desc for " + systemId);
			 //this.externalSystemDao.save(extSys);
			 //this.externalSystemDao.flush();//this is required to be in db for next query
		 }*/
		 return extSys;
	 }

	 @Override
	 public ExternalSystem getExternalSystemByName(String externalSystemName) throws AccessDeniedException, ErightsException {
		 ExternalSystem externalSystem = null;
		 
			externalSystem = erightsFacade.getExternalSystem(externalSystemName);
		// ExternalSystem es = this.externalSystemDao.findByName(externalSystemName);        
		 return externalSystem;
	 }

	 /**
	  * Return a list of external system types filtered by external system.
	  * 
	  * @param externalSystem
	  * @return
	 * @throws ErightsException 
	  */
	 /* @Override
    public List<ExternalSystemIdType> getExternalSystemIdTypesOrderedByName(final ExternalSystem externalSystem) {
    	return this.externalSystemTypeDao.findByExternalSystemOrderedByName(externalSystem);
    }*/

	 @Override
	 public List<ExternalSystemIdType> getExternalSystemIdTypesOrderedByName(final ExternalSystem externalSystem) throws ErightsException {
		 List<ExternalSystem> externalSystems = erightsFacade.getAllExternalSystems() ;
		 List<ExternalSystemIdType> retList = new ArrayList<ExternalSystemIdType>() ;
		 for (ExternalSystem externalSys: externalSystems) {
			 if(externalSys.getName().equals(externalSystem.getName())){
				 retList.addAll(externalSys.getExternalSystemIdTypes());
			 }
		 }
		 return retList;
	 }

	 /**
	  * Return a list of external systems ordered by name.
	  * 
	  * @return
	  * 		List of external systems ordered by name
	 * @throws ErightsException 
	  */
	 @Override
	 public List<ExternalSystem> getAllExternalSystemsOrderedByName() throws ErightsException {
		 return erightsFacade.getAllExternalSystems() ;
		 //product de-duplication
		//return this.externalSystemDao.findAllExternalSystemsOrderedByName();
	 }

	 @Override
	 public ExternalSystem getExternalSystemById(String externalSystemId) {
		 return this.externalSystemDao.getById(externalSystemId, false);
	 }

	 @Override
	 public ExternalSystemIdType getExternalSystemIdTypeById(String externalSystemIdTypeId) {
		 return this.externalSystemTypeDao.getById(externalSystemIdTypeId, false);
	 }

	 @Override
	 public boolean externalProductIdInUse(final String productId, final ExternalIdDto externalIdDto) {
		 EnforceableProductDto product = null ;
		 
			try {
				product = erightsFacade.getProductByExternalId(externalIdDto) ;
			} catch (ProductNotFoundException e) {
				LOGGER.error(e);
			} catch (ErightsException e) {
				LOGGER.error(e);
			}
		
		 return product != null && (productId == null || !productId.equals(product.getProductId()));
		 //product -de-duplication
		 /*ExternalProductId externalId = externalIdDao.findExternalProductIdBySystemAndTypeAndExternalId(externalIdDto.getSystemId(), externalIdDto.getType(), externalIdDto.getId());
		 return externalId != null && !externalId.getProduct().getId().equals(productId);*/
	 }

	 @Override
	 public void saveOrUpdate(final ExternalSystem externalSystem) {
		 externalSystemDao.saveOrUpdate(externalSystem);
	 }

	 @Override
	 public void delete(final ExternalSystem externalSystem) {
		 externalSystemDao.delete(externalSystem);
	 }


	 @Override
	 public ExternalCustomerIdDto getFullExternalCustomerIdDto( Customer customer) {
		 // sending system id as null, so that we can get all externalCustomerIds.
		 ExternalCustomerIdDto result = new ExternalCustomerIdDto(customer);
		 List<ExternalCustomerId> externalCustomerIdList = new ArrayList<ExternalCustomerId>();
			for (ExternalCustomerId extId:customer.getExternalIds()){
				externalCustomerIdList.add(extId);
			}
			result.setExternalCustomerIds(externalCustomerIdList );
		 //ExternalCustomerIdDto result = externalIdDao.getExternalIdsForCustomer(customer, null);
		 return result;
	 }

	 @Override
	 public ExternalProductIdDto getFullExternalProductIdDto(List<Product> products) {        
		 //ExternalProductIdDto result = externalIdDao.getExternalIdsForProducts(products, null);
		 ExternalProductIdDto result = externalIdDao.getExternalIdsForProductsAndLinkedProducts(products, null); // here
		 return result;
	 }
	 @Override
	 public void createExternalSystem(ExternalSystem externalSystem) {
		try {
			erightsFacade.createExternalSystem(externalSystem);
			AuditLogger.logEvent("Created ExternalSystem", "ExternalSystemId:"+externalSystem.getId(),AuditLogger.externalSystem(externalSystem));
		} catch (ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	 }
	 @Override
	 public void updateExternalSystem(ExternalSystem externalSystem,String oldSystemName) {
		try {
			erightsFacade.updateExternalSystem(externalSystem,oldSystemName);
			AuditLogger.logEvent("Updated ExternalSystem", "ExternalSystemId:"+externalSystem.getId(),AuditLogger.externalSystem(externalSystem));
		} catch (ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public void deleteExternalSystem(ExternalSystem externalSystem) {
		try {
			erightsFacade.deleteExternalSystem(externalSystem);
			AuditLogger.logEvent("Deleted ExternalSystem", "ExternalSystemId:"+externalSystem.getId(),AuditLogger.externalSystem(externalSystem));
		} catch (ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public void deleteExternalSystemTypes(ExternalSystem externalSystem,
			List<ExternalSystemIdType> externalSystemIdTypes) {
		try {
			erightsFacade.deleteExternalSystemTypes(externalSystem, externalSystemIdTypes);
		} catch (ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
