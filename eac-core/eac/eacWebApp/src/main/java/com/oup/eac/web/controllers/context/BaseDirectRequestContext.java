package com.oup.eac.web.controllers.context;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.oup.eac.domain.Product;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.EnforceableProductDto.RegisterableType;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.service.DomainSkinResolverService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.web.controllers.authentication.AuthenticationWorkFlow;
import com.oup.eac.web.controllers.helpers.SessionHelper;

public abstract class BaseDirectRequestContext implements DirectRequestContext {

	private static final Logger LOG = Logger.getLogger(BaseDirectRequestContext.class);

	private final ProductService productService;
	private final DomainSkinResolverService domainSkinResolverService;
	private final RegistrationDefinitionService registrationDefinitionService;
	private final ErightsFacade erightsFacade;
	
	protected BaseDirectRequestContext(final ProductService productService, final DomainSkinResolverService domainSkinResolverService,
			final RegistrationDefinitionService registrationDefinitionService, final ErightsFacade erightsFacade) {
		this.productService = productService;
		this.domainSkinResolverService = domainSkinResolverService;
		this.registrationDefinitionService = registrationDefinitionService;
		this.erightsFacade = erightsFacade;
	}
    
    /**
     * Process product.
     *
     * @param product the product
     * @param request the request
     * @param requestContext the request context
     * @throws ErightsException 
     * @throws ProductNotFoundException 
     */
    protected final void processProduct(final Product product, EnforceableProductDto enfoProductDto, final HttpServletRequest request, final RequestContext requestContext) throws ProductNotFoundException, ErightsException {
    	EnforceableProductDto enforceableProduct;
    	if (product instanceof RegisterableProduct) {
        	if(enfoProductDto==null){
        		enforceableProduct = erightsFacade.getProduct(product.getId());
        	}
        	else {
        		enforceableProduct = enfoProductDto;
        	}
        	if(enforceableProduct.getRegisterableType().toString().equals(RegisterableType.ADMIN_REGISTERABLE.toString())){
        		((RegisterableProduct) product).setRegisterableType(com.oup.eac.domain.RegisterableProduct.RegisterableType.ADMIN_REGISTERABLE);
        	}
        	else{
        		((RegisterableProduct) product).setRegisterableType(com.oup.eac.domain.RegisterableProduct.RegisterableType.SELF_REGISTERABLE);
        	}
        	if (enforceableProduct.getLandingPage() != null ){
        		product.setLandingPage(enforceableProduct.getLandingPage());
        	}
        	if (enforceableProduct.getHomePage() != null ) {
        		product.setHomePage(enforceableProduct.getHomePage());
        	}
            RegisterableProduct rp = (RegisterableProduct) product;
            SessionHelper.setRegisterableProduct(request, rp);
            requestContext.setRegisterableProduct(rp);
            String productLandingPage = enforceableProduct.getLandingPage();
            Assert.notNull(productLandingPage);
            requestContext.setUserRequestedUrl(productLandingPage);
            SessionHelper.setForwardUrl(request.getSession(), productLandingPage);
            AuthenticationWorkFlow.initRequestContext(request, domainSkinResolverService, requestContext);
        }
    }
    
    /**
     * Checks if is no landing page.
     *
     * @param requestContext the request context
     * @return true, if is no landing page
     */
    protected final boolean isNoLandingPage(final RequestContext requestContext) {
        RegisterableProduct rp = requestContext.getRegisterableProduct();
        boolean noLandingPage =  StringUtils.isBlank(rp.getLandingPage());
        if (noLandingPage) {
            String msg = String.format(
                    "Cannot redirect to Product Registration Page for RegisterableProduct id[%s] name[%s] because it has no Landing Page",
                    rp.getId(), rp.getProductName()); 
            LOG.error(msg);
        }
        return noLandingPage;
    }

	protected ProductService getProductService() {
		return productService;
	}

	protected DomainSkinResolverService getDomainSkinResolverService() {
		return domainSkinResolverService;
	}

	protected RegistrationDefinitionService getRegistrationDefinitionService() {
		return registrationDefinitionService;
	}
}
