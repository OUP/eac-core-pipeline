package com.oup.eac.service.impl;

import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Field;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.RegistrationDefinition.RegistrationDefinitionType;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.RegistrationActivationDto;
import com.oup.eac.dto.RegistrationDto;
import com.oup.eac.service.ProductRegistrationService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.ServiceLayerValidationException;
import com.oup.eac.service.util.ProductRegistrationHelper;

@Service("productRegistrationService")
public class ProductRegistrationServiceImpl implements ProductRegistrationService {

    private static final Logger LOG = Logger.getLogger(ProductRegistrationServiceImpl.class);

    private final RegistrationService registrationService;

    private final RegistrationDefinitionService registrationDefinitionService;
    
    private final ProductRegistrationHelper productRegistratonHelper;
    
    private final ProductService productService ;
    
    /**
     * Instantiates a new product registration service impl.
     *
     * @param registrationService the registration service
     * @param registrationDefinitionService the registration definition service
     */
    @Autowired
    public ProductRegistrationServiceImpl(final RegistrationService registrationService, final RegistrationDefinitionService registrationDefinitionService,
    		final ProductRegistrationHelper productRegistratonHelper, final ProductService productService ) {
        Assert.notNull(registrationService);
        Assert.notNull(registrationDefinitionService);
        Assert.notNull(productRegistratonHelper);
        Assert.notNull(productService);
        this.registrationService = registrationService;
        this.registrationDefinitionService = registrationDefinitionService;
        this.productRegistratonHelper = productRegistratonHelper;
        this.productService = productService ;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Registration<?> createProductRegistration(
            final EnforceableProductDto enfoProduct,
            final Customer customer,
            final Map<String, String> registrationPageData) throws ServiceLayerException {
        // not all products have a registration page
    	RegisterableProduct product = new RegisterableProduct();
    	product.setId(enfoProduct.getProductId());
        ProductRegistrationDefinition prodRegDef = getProductRegistrationDefinition(product);
        prodRegDef.setProduct(product);
/*        ProductRegistrationDefinition prodRegDef =new ProductRegistrationDefinition();

          prodRegDef.setConfirmationEmailEnabled(null);
          prodRegDef.setEacGroup(null);
          prodRegDef.setId(null);
*/          

        boolean registrationPageExists = prodRegDef.getPageDefinition() != null;

        RegistrationDto registrationDto = null;
        if (registrationPageExists) {
            // get the fields for this registration
            registrationDto = registrationService.getProductPageDefinitionByRegistrationDefinition(prodRegDef, customer, customer.getLocale());

            Map<String, Field> registrationPageConfig = this.productRegistratonHelper.getRegistrationPageConfig(registrationDto);

            // get the validated registration data
            Map<Field, String> validatedRegData = this.productRegistratonHelper.getValidatedRegistrationPageData(registrationPageConfig, registrationPageData, true);

            // add the validated registration data to the registration dto
            this.productRegistratonHelper.populateRegistrationDtoWithValidatedData(validatedRegData, registrationDto);

        } else if (registrationPageData.size() > 0) {
            // we don't ignore information which is supplied but not required
            throw new ServiceLayerValidationException(ERR_NO_REGISTRATION_INFORMATION_REQUIRED);
        }
		
        // create the registration in the db
        final Registration<?> registration;
        
        if(enfoProduct.getRegistrationDefinitionType().equals(RegistrationDefinitionType.ACTIVATION_CODE_REGISTRATION.name())) {
        	registration = registrationService.saveActivationCodeRegistrationWithoutActivationCode(customer, (ActivationCodeRegistrationDefinition)prodRegDef);
        } else {
        	registration = registrationService.saveProductRegistration(customer, prodRegDef);
        }

        if (registrationPageExists) {
            // add the product registration information to the registration in the db
            registrationService.saveCompleteRegsitration(registrationDto, customer, registration.getRegistrationDefinition().getProduct().getId());
        }

        // Activate the Registration
        Locale locale = customer.getLocale();
		
		String originalUrl = enfoProduct.getLandingPage();
		registrationService.saveRegistrationActivation(new RegistrationActivationDto(customer, locale, originalUrl, registration, prodRegDef),enfoProduct);

        if (LOG.isDebugEnabled()) {
            String msg = String.format("Created Product Registration for Customer [%s] and Product [%s] with id [%s]", customer.getUsername(), prodRegDef
                    .getProduct().getProductName(), registration.getId());
            LOG.debug(msg);
        }
        
        
        AuditLogger.logEvent(customer, "Created Product Registration", AuditLogger.product(product));
        return registration;
    }

    /**
     * Gets the product registration definition from product id.
     *
     * @param product the product
     * @return the product registration definition 
     * @throws ServiceLayerException the service layer exception
     */
    @Override
    public ProductRegistrationDefinition getProductRegistrationDefinition(final RegisterableProduct product)
            throws ServiceLayerException {

        // lookup product registration definition
        RegisterableProduct rp = (RegisterableProduct) product;
        try {
        	/* Gaurav Soni:
        	 * # EAC Performance
        	 * 
        	 * Previous code was using "getProductRegistrationDefinitionByProduct" method.
        	 * Changed the method because there is a lazy loading in previous method.
        	 * */
            ProductRegistrationDefinition result = registrationDefinitionService.getProductRegistrationDefinitionByProductWithoutActivationCodeInfo(rp);
            return result;
        } catch (ServiceLayerException sle) {
            throw new ServiceLayerValidationException(ERR_INVALID_REGISTRATION_DEFINITION_TYPE, sle);
        }
    }
}
