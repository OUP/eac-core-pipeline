package com.oup.eac.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.amazonaws.services.cloudfront.model.AccessDeniedException;
import com.oup.eac.data.RegistrationDefinitionDao;
import com.oup.eac.domain.AccountRegistrationDefinition;
import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.ActivationCodeBatch.ActivationCodeFormat;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.InstantRegistrationActivation;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegistrationActivation;
import com.oup.eac.domain.RegistrationDefinition;
import com.oup.eac.domain.SelfRegistrationActivation;
import com.oup.eac.domain.ValidatedRegistrationActivation;
import com.oup.eac.domain.paging.Paging;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.ActivationCodeBatchDto;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.LicenceDetailDto;
import com.oup.eac.dto.Message;
import com.oup.eac.dto.RegistrationDefinitionSearchCriteria;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.GroupNotFoundException;
import com.oup.eac.integration.facade.exceptions.LicenseNotFoundException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.ServiceLayerException;

/**
 * Product service providing product related business processes.
 * 
 * @author harlandd
 * @author Ian Packard
 */
@Service(value="registrationDefinitionService")
public class RegistrationDefinitionServiceImpl implements RegistrationDefinitionService {

    private final RegistrationDefinitionDao registrationDefinitionDao;
    
    private final ErightsFacade erightsFacede;
   
    /**
     * @param erightsFacade
     *            the erights facade
     * @param productDao
     *            product definition dao
     */
    @Autowired
    public RegistrationDefinitionServiceImpl(final RegistrationDefinitionDao registrationDefinitionDao, 
    		@Qualifier("erightsFacade") final ErightsFacade erightsFacede) {
        super();
        Assert.notNull(registrationDefinitionDao);
        this.registrationDefinitionDao = registrationDefinitionDao;
        this.erightsFacede = erightsFacede;
    }

    /**
     * @param url
     *            the product url
     * @return ProductDefinition the product 
     * @throws ServiceLayerException
     *             the exception
     */
    @Override
    public final ProductRegistrationDefinition getProductRegistrationDefinitionByProduct(final Product product) throws ServiceLayerException {
    	checkRegisterableProduct(product);
    	EnforceableProductDto enforceableProduct  = null;
    	try {
			enforceableProduct = erightsFacede.getProduct(product.getId());
			product.setProductName(enforceableProduct.getName());
			product.setEmail(enforceableProduct.getAdminEmail());
		} catch (ErightsException e) {
			throw new ServiceLayerException(e.getMessage());
		}
    	ProductRegistrationDefinition productRegistrationDefinition = registrationDefinitionDao.getRegistrationDefinitionByProduct(ProductRegistrationDefinition.class, product);
    	if(productRegistrationDefinition == null) {
    		throw new ServiceLayerException("Erights error during get product registration definition from product. ID: " + product.getId(), 
    				new Message("error.aquiring.product.information",
            "There was a problem trying to get your product information. Please go back to the product page and choose the item again."));
    	}
    	
    	if(enforceableProduct.getActivationStrategy() == RegistrationActivation.ActivationStrategy.INSTANT.toString()){
        	InstantRegistrationActivation instantActivation = new InstantRegistrationActivation();
        	productRegistrationDefinition.setRegistrationActivation(instantActivation);
        }
        if(enforceableProduct.getActivationStrategy() == RegistrationActivation.ActivationStrategy.SELF.toString()){
        	SelfRegistrationActivation selfActivation = new SelfRegistrationActivation();
        	productRegistrationDefinition.setRegistrationActivation(selfActivation);
        }
        if(enforceableProduct.getActivationStrategy() == RegistrationActivation.ActivationStrategy.VALIDATED.toString()){
        	ValidatedRegistrationActivation validatedActivation = new ValidatedRegistrationActivation();
        	validatedActivation.setValidatorEmail(enforceableProduct.getValidatorEmail());
        	productRegistrationDefinition.setRegistrationActivation(validatedActivation);
        }
        productRegistrationDefinition.setProduct(product);
    	if(enforceableProduct.getRegistrationDefinitionType().equals("ACTIVATION_CODE_REGISTRATION")){
            ActivationCodeRegistrationDefinition acrd = (ActivationCodeRegistrationDefinition)productRegistrationDefinition;
            return acrd;
            //Hibernate.initialize(acrd.getActivationCodeBatchs());
            /*if(acrd!=null && acrd.getActivationCodeBatchs()!=null && !acrd.getActivationCodeBatchs().isEmpty()){
                Iterator<ActivationCodeBatch> it= acrd.getActivationCodeBatchs().iterator();
                ActivationCodeBatch obj = it.next();
                System.out.println("getProductRegistrationDefinitionByProduct :: getActivationCodeBatchs first element id :: " + obj.getId());
            }*/
            //System.out.println("getProductRegistrationDefinitionByProduct :: getActivationCodeBatchs size............." + acrd.getActivationCodeBatchs().size());
        }
    	return productRegistrationDefinition;
    }

    /* Gaurav Soni
     * # EAC Performance
     * Created to avoid lazy initialization which is done in above method
     * */
    @Override
	public ProductRegistrationDefinition getProductRegistrationDefinitionByProductWithoutActivationCodeInfo(RegisterableProduct registerableProduct)
			throws ServiceLayerException {
    	checkRegisterableProduct(registerableProduct);
    	ProductRegistrationDefinition productRegistrationDefinition = registrationDefinitionDao.getRegistrationDefinitionByProduct(ProductRegistrationDefinition.class, registerableProduct);
    	if(productRegistrationDefinition == null) {
    		throw new ServiceLayerException("Erights error during get product registration definition from product. ID: " + registerableProduct.getId(), 
    				new Message("error.aquiring.product.information",
            "There was a problem trying to get your product information. Please go back to the product page and choose the item again."));
    	}
    	return productRegistrationDefinition;
	}
    
	private void checkRegisterableProduct(final Product product) throws ServiceLayerException {
		if(product == null) {
    		throw new ServiceLayerException("The registerable product can not be null.", 
    				new Message("error.aquiring.product.information",
            "The registerable product can not be null."));
    	}
	}

	@Override
	public final AccountRegistrationDefinition getAccountRegistrationDefinitionByProduct(final Product product) throws ServiceLayerException {
    	checkRegisterableProduct(product);
		AccountRegistrationDefinition accountRegistrationDefinition = registrationDefinitionDao.getRegistrationDefinitionByProduct(AccountRegistrationDefinition.class, product);
    	if(accountRegistrationDefinition == null) {
    		throw new ServiceLayerException("Account registration definition does not exist for product. ID: " + product.getId(), 
    				new Message("error.aquiring.product.information",
            "There was a problem trying to get your product information. Please go back to the product page and choose the item again."));
    	}
    	return accountRegistrationDefinition;
	}
    
	@Override
	public final List<ActivationCodeRegistrationDefinition> getActivationCodeRegistrationDefinitionsByAdminUser(AdminUser adminUser) throws ServiceLayerException {
		return registrationDefinitionDao.getActivationCodeRegistrationDefinitionsByAdminUser(adminUser);
	}

	@Override
    public RegistrationDefinition getRegistrationDefinitionById(final String id) throws ServiceLayerException {
        return registrationDefinitionDao.getEntity(id);
    }
	
	@Override
	public final ActivationCodeRegistrationDefinition getActivationCodeRegistrationDefinitionByProduct(final Product product) throws ServiceLayerException {
	    return registrationDefinitionDao.getActivationCodeRegistrationDefinitionByProduct(product);
	}
	
    @Override
	public final ProductRegistrationDefinition getProductRegistrationDefinitionFromCustomerAndRegistrationId(
	        final Customer customer, final String registrationId) {
        ProductRegistrationDefinition result =
                registrationDefinitionDao.getProductRegistrationDefinitionFromCustomerAndRegistrationId(customer, registrationId);
	    return result;
	}

	@Override
	public Paging<RegistrationDefinition> searchRegistrationDefinitions(final RegistrationDefinitionSearchCriteria searchCriteria, final PagingCriteria pagingCriteria)
			throws ServiceLayerException {
		List<RegistrationDefinition> registrationDefinitions = registrationDefinitionDao.searchRegistrationDefinitions(searchCriteria, pagingCriteria);
		int count = registrationDefinitionDao.countSearchRegistrationDefinitions(searchCriteria);
		AuditLogger.logEvent("Search Product", "Product Id:"+searchCriteria.getProductId(),AuditLogger.registrationDefinitionSearchCriteria(searchCriteria));
		return Paging.valueOf(pagingCriteria, registrationDefinitions, count);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_CREATE_REGISTRATION_DEFINITION')")
	public void saveRegistrationDefinition(
			final RegistrationDefinition registrationDefinition) {
		registrationDefinitionDao.saveOrUpdate(registrationDefinition);
	}

    @Override
    public ProductRegistrationDefinition getProductRegistrationDefinitionWithLicence(String id) {
        ProductRegistrationDefinition result = this.registrationDefinitionDao.getProductRegistrationDefinitionWithLicence(id);
        return result;
    }

    
    @Override
    public List<RegistrationDefinition> getRegistrationDefinitionsForProduct(Product product) throws ServiceLayerException {
        return this.registrationDefinitionDao.getRegistrationDefinitionsForProduct(product);
    }
    
    @Override
    public List<ProductRegistrationDefinition> getRegistrationDefinitionsForPageDefinition(String pageDefId) 
    		throws ServiceLayerException {
        return this.registrationDefinitionDao.getRegistrationDefinitionsForPageDefinition(pageDefId);
    }
    
    @Override
    public List<AccountRegistrationDefinition> getRegistrationDefinitionsForPageDefinitionForAccount(String pageDefId) 
    		throws ServiceLayerException {
        return this.registrationDefinitionDao.getRegistrationDefinitionsForPageDefinitionForAccount(pageDefId);
    }

	@Override
	public ActivationCodeRegistrationDefinition getActivationCodeRegistrationDefinitionForEacGroup(String batchId) 
			throws ServiceLayerException, ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, 
			AccessDeniedException, GroupNotFoundException, ErightsException {
		 ActivationCodeBatchDto activationCodeBatchDto = new ActivationCodeBatchDto();
		try {
			 activationCodeBatchDto = erightsFacede.getActivationCodeBatch(batchId,false); // erightsFacade.getActivationCodeBatch(batchId);
		} catch (Exception e) {
			e.printStackTrace();
		}   
		
	    ActivationCodeRegistrationDefinition acrd = new ActivationCodeRegistrationDefinition();
	    Set<ActivationCodeBatch> acbs = new HashSet<ActivationCodeBatch>();
	    ActivationCodeBatch acb = new ActivationCodeBatch();
	    List<ActivationCode> codes = new ArrayList<ActivationCode>();
	    ActivationCode code = new ActivationCode();
	    ArrayList<String> acs = (ArrayList<String>) activationCodeBatchDto.getActivationCodes();
	    for (String ac : acs) {
	    	code.setCode(ac);
	    	codes.add(code);
	    }
	    
	    LicenceDetailDto licenseDto = activationCodeBatchDto.getLicenceDetailDto();
	    LicenceTemplate lt = null;
	    lt.setStartDate(licenseDto.getStartDate());
        lt.setEndDate(licenseDto.getEndDate());        
        
        ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition = 
	    		new ActivationCodeRegistrationDefinition();
	    activationCodeRegistrationDefinition.setActivationCodeBatchs(acbs);
	   
	    if (activationCodeBatchDto.getActivationCodeRegistration().getRegistrationDefinition().getEacGroup() != null) {
	    	 activationCodeRegistrationDefinition.setEacGroup(
	    			 activationCodeBatchDto.getActivationCodeRegistration().getRegistrationDefinition().getEacGroup());
	    }	   
	    if (activationCodeBatchDto.getActivationCodeRegistration().getRegistrationDefinition().getProduct() != null) {
	    	activationCodeRegistrationDefinition.setProduct(
	    			activationCodeBatchDto.getActivationCodeRegistration().getRegistrationDefinition().getProduct());
	    }
	    activationCodeRegistrationDefinition.setId(activationCodeBatchDto.getBatchId());
	    activationCodeRegistrationDefinition.setLicenceTemplate(lt);
	    activationCodeRegistrationDefinition.setVersion(activationCodeBatchDto.getVersion());	   
	    //activationCodeRegistrationDefinition.setPageDefinition(activationCodeBatchDto);
	    //activationCodeRegistrationDefinition.setRegistrationActivation(registrationActivation);
	    //activationCodeRegistrationDefinition.setConfirmationEmailEnabled(activationCodeBatchDto.get);
	   
	    DateTime createdDate = new DateTime(activationCodeBatchDto.getCreatedDate().toDate());
	    DateTime updatedDate = new DateTime(activationCodeBatchDto.getUpdatedDate().toDate());
	    
	    if (activationCodeBatchDto.getCodeFormat() == "EAC_NUMERIC") {
	    	acb.setActivationCodeFormat(ActivationCodeFormat.EAC_NUMERIC);
	    }
	    
	    acb.setActivationCodeRegistrationDefinition(activationCodeRegistrationDefinition);
	    acb.setActivationCodes(codes);
	    acb.setBatchId(batchId);
	    //acb.setCodePrefix(codePrefix);
	    acb.setCreatedDate(createdDate);
	    acb.setId(activationCodeBatchDto.getBatchId());
	    acb.setLicenceTemplate(lt);
	    
	    if (activationCodeBatchDto.getValidFrom() != null) {
	    	DateTime startDate = new DateTime(activationCodeBatchDto.getValidFrom().toDate());
	    	acb.setStartDate(startDate);
	    }
	    if (activationCodeBatchDto.getValidTo() != null) {
	    	DateTime endDate = new DateTime(activationCodeBatchDto.getValidTo().toDate());
	    	acb.setEndDate(endDate);
	    }
	    acb.setUpdatedDate(updatedDate);
	    acb.setVersion(activationCodeBatchDto.getVersion());
	    
	    acbs.add(acb);
	    
	    acrd.setActivationCodeBatchs(acbs);
	    //acrd.setConfirmationEmailEnabled(confirmationEmailEnabled);
	    if (activationCodeBatchDto.getActivationCodeRegistration().getRegistrationDefinition().getEacGroup() != null) {
	    	acrd.setEacGroup(activationCodeBatchDto.getActivationCodeRegistration().getRegistrationDefinition().getEacGroup());
	    }
	    acrd.setId(activationCodeBatchDto.getBatchId());
	    acrd.setLicenceTemplate(lt);
	    //acrd.setPageDefinition(pageDefinition);
	    if (activationCodeBatchDto.getActivationCodeRegistration().getRegistrationDefinition().getProduct() != null) {
	    	acrd.setProduct(activationCodeBatchDto.getActivationCodeRegistration().getRegistrationDefinition().getProduct());	
	    }	  
	    /*if (activationCodeBatchDto.getActivationCodeRegistration() != null) {
	    	acrd.setRegistrationActivation(registrationActivation);	
	    }*/	    
	    acrd.setVersion(activationCodeBatchDto.getVersion());
		
		//De-duplication
		//ActivationCodeRegistrationDefinition acrd = this.registrationDefinitionDao.getActivationCodeRegistrationDefinitionForEacGroup(eacGroup);
	    Hibernate.initialize(acrd.getActivationCodeBatchs());
	    /*if(acrd!=null && acrd.getActivationCodeBatchs()!=null && !acrd.getActivationCodeBatchs().isEmpty()){
            Iterator<ActivationCodeBatch> it= acrd.getActivationCodeBatchs().iterator();
            ActivationCodeBatch obj = it.next();
            System.out.println("getActivationCodeRegistrationDefinitionForEacGroup :: getActivationCodeBatchs first element id :: " + obj.getId());
        }*/
	    //System.out.println("getActivationCodeRegistrationDefinitionForEacGroup :: getActivationCodeBatchs size..." + acrd.getActivationCodeBatchs().size());
		return acrd;
	}

	@Override
	public void deleteRegistrationDefinition(
			RegistrationDefinition registrationDefinition) {
		registrationDefinitionDao.delete(registrationDefinition);
		
	}
}
