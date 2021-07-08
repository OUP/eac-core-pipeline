/**
 * 
 */
package com.oup.eac.web.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.web.controllers.helpers.RequestHelper;
import com.oup.eac.web.controllers.helpers.SessionHelper;

/**
 * Initialises the session for the ProductRegistrationUpdateController.
 * <ul>
 * <li>the 'registrationId' from the request parameters
 * <li>
 * <li>the Customer
 * <li>
 * <li>the ProductRegistrationDefinition
 * <li>
 * </ul>
 * 
 * @author David Hay
 * @see com.oup.eac.web.controllers.registration.ProductRegistrationUpdateController
 */
public class ProductRegistrationUpdateInterceptor extends HandlerInterceptorAdapter {

    public static final String PARAM_REGISTRATION_ID = "registrationId";

    private static final String ERR_NO_REGISTRATION_ID_IN_REQUEST = "No Registration Id in Request";
    private static final String ERR_NO_CUSTOMER_IN_SESSION = "No Customer in Session";
    private static final String ERR_NO_PRODUCT_REG_DEF_IN_SESSION = "No Product Registration Definition in Session";

    //private RegistrationDefinitionService registrationDefinitionService;

    private RegistrationService registrationService;
    /**
     * Instantiates a new product registration update interceptor.
     * 
     * @param registrationDefinitionServiceP
     *            the registration definition service p
     */
    public ProductRegistrationUpdateInterceptor( 
    		final RegistrationService registrationServiceP) {
        this.registrationService =  registrationServiceP;
    }

    /**
     * @param request
     *            the request
     * @param response
     *            the response
     * @param handler
     *            the handler
     * @throws Exception
     *             any checked exception
     * @return true if the handler chain should continue
     */
    @Override
    public final boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {

        String requestMethod = request.getMethod();

        Customer customer = SessionHelper.getCustomer(request);
        if(customer == null){
            error("No Customer in Session");
        }

        if (RequestHelper.isGetRequest(request)) {
            handleGet(customer, request, response);

        } else if (RequestHelper.isPostRequest(request)) {
            handlePost(customer, request, response);

        } else {
            String msg = String.format("Unexpected Http Request Method type : %s", requestMethod);
            error(msg);
        }
        return true;
    }

    /**
     * Handle get.
     * 
     * @param customer
     *            the customer
     * @param request
     *            the request
     * @param response
     *            the response
     */
    private void handleGet(final Customer customer, final HttpServletRequest request, final HttpServletResponse response) {
        SessionHelper.setRegistrationId(request, null);
        if (customer == null) {
            error(ERR_NO_CUSTOMER_IN_SESSION);
        }
        String registrationId = request.getParameter(PARAM_REGISTRATION_ID);
        if (StringUtils.isBlank(registrationId)) {
            error(ERR_NO_REGISTRATION_ID_IN_REQUEST);
        }
        ProductRegistrationDefinition prodRegDef = getProductRegistrationDefinition(customer, registrationId);
        SessionHelper.setProductRegistrationDefinition(request, prodRegDef);
        SessionHelper.setRegistrationId(request, registrationId);
    }

    /**
     * Handle post.
     * 
     * @param customer
     *            the customer
     * @param request
     *            the request
     * @param response
     *            the response
     */
    private void handlePost(final Customer customer, final HttpServletRequest request, final HttpServletResponse response) {
        ProductRegistrationDefinition prd = SessionHelper.getProductRegistrationDefinition(request);
        if (prd == null) {
            error(ERR_NO_PRODUCT_REG_DEF_IN_SESSION);
        }
    }

    /**
     * Gets the product registration definition.
     * 
     * @param customer
     *            the customer
     * @param registrationId
     *            the registration id
     * @return the product registration definition
     */
    private ProductRegistrationDefinition getProductRegistrationDefinition(final Customer customer, final String registrationId) {
    	ProductRegistrationDefinition result = null;
    	try {
    		Registration<? extends ProductRegistrationDefinition> registration = registrationService.
    				getProductRegistration(registrationId, customer.getId());
    		result = registration.getRegistrationDefinition();

    		
    		///ProductPageDefinition productPageDefinition =null; 
    		/*if(registration.getRegistrationDefinition().getPageDefinition()!=null){
    			productPageDefinition = pageDefinitionService.getFullyFetchedProductPageDefinitionById(
    					registration.getRegistrationDefinition().getPageDefinition().getId());
    		}
*/
    		// Load dto with values
    		//ProductRegistrationDto registrationDto = new ProductRegistrationDto();
    		//initRegistrationDto(productPageDefinition, customer, registrationDto, registration.getRegistrationDefinition(), customer.getLocale());

    		// We can't trust that the productRegistrationDefinition we were passed in was loaded in this session - we have to reload it.
    		//ProductRegistrationDefinition prodRegDef = this.registrationDefinitionDao.getProductRegistrationDefinitionWithLicence(productRegistrationDefinition.getId());
    		
    		
    		//ProductRegistrationDefinition result = registrationDefinitionService.getProductRegistrationDefinitionFromCustomerAndRegistrationId(customer,registrationId);
    		if (result == null) {
    			String msg = String.format("ProductRegistrationDefinition Cannot be Found for customerId[%s] and registrationId[%s]", customer.getId(),
    					registrationId);
    			error(msg);
    		} else {
    			if (result.getPageDefinition() == null) {
    				String msg = String.format("ProductRegistrationDefinition with id [%s] does not have a registration page", result.getId());
    				error(msg);
    			}
    		}
    	} catch (ServiceLayerException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	return result;
    }

    /**
     * Warning.
     * 
     * @param msg
     *            the msg
     */
    private void error(final String msg) {
        throw new IllegalStateException(msg);
    }

    @Override
    public final void postHandle(
            final HttpServletRequest request, 
            final HttpServletResponse response, 
            final Object handler, 
            final ModelAndView modelAndView)
            throws Exception {
        RequestHelper.setShowingProductRegistrationPage(modelAndView, request);
   }

}
