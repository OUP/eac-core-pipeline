package com.oup.eac.service;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ExternalCustomerId;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.dto.ExternalCustomerIdDto;
import com.oup.eac.dto.ExternalIdDto;
import com.oup.eac.dto.ExternalProductIdDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.exceptions.ServiceLayerValidationException;

public interface ExternalIdService {
    
    ExternalProductIdDto getExternalProductIds(List<Product> products, String systemId);
    
    ExternalProductIdDto getExternalProductIds(List<Product> products);
    
    ExternalProductIdDto getExternalProductIdsForProductsAndLinkedProducts(List<Product> products, String systemId);
    
    ExternalCustomerIdDto getExternalCustomerIds(Customer customer, String systemId);

    void saveExternalCustomerIdsForSystem(Customer customer, String systemId, List<ExternalIdDto> dtos) throws ServiceLayerException;

    void saveExternalCustomerIdsForSystemCreatingCustomer(Customer customer, String systemId, List<ExternalIdDto> dtos) throws ServiceLayerException, ErightsException;

    void saveExternalProductIdsForSystem(Product product, String systemId, List<ExternalIdDto> dtos, RegisterableProduct regProduct) throws ServiceLayerException, ErightsException;

    ExternalSystem getExternalSystemByName(String externalSystemName) throws AccessDeniedException, ErightsException;
    
    List<ExternalSystemIdType> getExternalSystemIdTypesOrderedByName(ExternalSystem externalSystem) throws ErightsException;
    
	List<ExternalSystem> getAllExternalSystemsOrderedByName() throws ErightsException;

	ExternalSystem getExternalSystemById(String externalSystemId);
	
	ExternalSystemIdType getExternalSystemIdTypeById(String externalSystemIdTypeId);

	void checkExistingExternalCustomerIds(Customer customer, List<ExternalCustomerId> externalIds) throws ServiceLayerValidationException, ErightsException;	
	
	boolean externalProductIdInUse(String productId, ExternalIdDto externalIdDto);
	
	void saveOrUpdate(ExternalSystem externalSystem);
	
	void delete(ExternalSystem externalSystem);
	
	//ExternalSystemIdType getExternalSystemIdType(ExternalSystem extSys, String type);
	
	ExternalCustomerIdDto getFullExternalCustomerIdDto(Customer customer);
	
	ExternalProductIdDto getFullExternalProductIdDto(List<Product> products);
	
	void createExternalSystem(ExternalSystem externalSystem);
	
	void updateExternalSystem(ExternalSystem externalSystem,String oldSystemName);
    
	void deleteExternalSystem(ExternalSystem externalSystem);
	
	void deleteExternalSystemTypes(ExternalSystem externalSystem,List<ExternalSystemIdType> externalSystemIdTypes);
	
}


