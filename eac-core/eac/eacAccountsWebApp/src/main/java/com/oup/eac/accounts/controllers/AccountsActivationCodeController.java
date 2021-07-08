package com.oup.eac.accounts.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.accounts.dto.CodeRedeemDto;
import com.oup.eac.accounts.validators.AccountsActivationCodeFormValidator;
import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.ActivationCodeService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.web.controllers.helpers.SessionHelper;
//import com.oup.eac.ws.v2.ex.WebServiceValidationException;

/**
 * Spring MVC Controller for redeeming Activation Code.  
 * @author Gaurav Soni
 */

@Controller
public class AccountsActivationCodeController {

    private static final Logger LOG = Logger.getLogger(AccountsActivationCodeController.class);
    private final String ACTIVATION_CODE_FORM_VIEW = "redeemActivationCodeForm";
    private final ActivationCodeService activationCodeService;
    private final RegistrationService registrationService;
    private final AccountsActivationCodeFormValidator actCodeFormValidator;
    private final String sharedUserWarning = "Unfortunately we cannot redeem code as you have a shared account.";
    
    @Autowired
    public AccountsActivationCodeController(
            ActivationCodeService activationCodeService, RegistrationService registrationService,
            AccountsActivationCodeFormValidator actCodeFormValidator) {
        this.activationCodeService = activationCodeService;
        this.registrationService = registrationService;
        this.actCodeFormValidator = actCodeFormValidator;
    }
    
    @ModelAttribute("codeRedeemDto")
    public CodeRedeemDto createModel(){
        CodeRedeemDto acDto= new CodeRedeemDto();
        return acDto;
    }
    
    @RequestMapping(value = { "/activationCode" }, method=RequestMethod.GET)
    public ModelAndView showForm(@ModelAttribute("codeRedeemDto") final CodeRedeemDto actCodeDto,
            final HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(ACTIVATION_CODE_FORM_VIEW);
        Customer customer = SessionHelper.getCustomer(request);
        if(checkSharedUser(customer)){
            LOG.debug(sharedUserWarning);
            actCodeDto.setSharedUser(true);
        }
        return modelAndView;
    }
    
    @InitBinder
    public void initBinder(final WebDataBinder webDataBinder) {
        webDataBinder.setValidator(actCodeFormValidator);
    }
    
    @RequestMapping(value = { "/activationCode" }, method=RequestMethod.POST)
    public ModelAndView redeemCode(@Valid @ModelAttribute("codeRedeemDto") final CodeRedeemDto actCodeDto,
            final BindingResult bindingResult,
            final HttpServletRequest request,
            final HttpServletResponse response) {
        
        if (bindingResult.hasErrors()) {
            return new ModelAndView(ACTIVATION_CODE_FORM_VIEW);
        }
        Customer customer = SessionHelper.getCustomer(request);
        
        //check for shared user
        if(checkSharedUser(customer)){
            LOG.debug(sharedUserWarning);
            bindingResult.reject("error.shared.user.registration.denied", sharedUserWarning);
            return new ModelAndView(ACTIVATION_CODE_FORM_VIEW);
        }
        
        String trimmedActivationCode = trimActivationCode(actCodeDto.getCode());
        //check for null
        //ActivationCode activationCode =  activationCodeService.getActivationCodeFullDetails(trimmedActivationCode);
        ActivationCode activationCode = null;
		try {
			activationCode = activationCodeService.getActivationCodeWithDetails(trimmedActivationCode);
		} catch (AccessDeniedException | ErightsException
				| ServiceLayerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			LOG.debug(e1.getMessage());
			}
        if (activationCode == null) {
        	LOG.debug("Activation Code is invalid.");
            bindingResult.reject("error.regularexpression", new Object[] { "label.redeemcode" }, "Activation Code is invalid.");
            return new ModelAndView(ACTIVATION_CODE_FORM_VIEW);
        }
        ActivationCodeRegistrationDefinition acrd = activationCode.getActivationCodeBatch().getActivationCodeRegistrationDefinition();
		ProductPageDefinition pageDef = acrd.getPageDefinition();
		if (pageDef != null) {
			LOG.debug("There was a problem registering the product. Please contact the system administrator.");
            bindingResult.reject("error.problem.registering.product", "There was a problem registering the product. Please contact the system administrator.");
            return new ModelAndView(ACTIVATION_CODE_FORM_VIEW);
//			throw new WebServiceValidationException(
//					"You cannot redeem this type of activation code : it requires the capture of product registration information");
		}
        EacGroups eacGroup = activationCode.getActivationCodeBatch().getActivationCodeRegistrationDefinition().getEacGroup();
        Product product = activationCode.getActivationCodeBatch().getActivationCodeRegistrationDefinition().getProduct();
        
        if(eacGroup != null && product == null){
            LOG.warn("Activation Code is for Product Group. Unfortunately We cannot redeem it now.");
            bindingResult.reject("error.code.for.group", "Activation Code is for Product Group. Unfortunately We cannot redeem it now.");
            return new ModelAndView(ACTIVATION_CODE_FORM_VIEW);
        }else if(eacGroup == null && product != null){
        	
        	//removed for de-duplication
        	
           /* try {
            activationCode = this.registrationService.incrementActivationCodeUsage(activationCode);
            } catch (ServiceLayerException e) {
                LOG.debug(e.getMessage());
                AccountsBindingErrorController.convertMessages(e, bindingResult);
                return new ModelAndView(ACTIVATION_CODE_FORM_VIEW);
            } catch(Exception e){
                LOG.error(e.getMessage());
                bindingResult.reject("error.unknown", "Unexpected error.");
                return new ModelAndView(ACTIVATION_CODE_FORM_VIEW);
            }*/
            
            try{
                this.registrationService.createRegistrationAndAddLicence(customer, activationCode, SessionHelper.getLocale(request));
            } catch(ServiceLayerException e) {
                LOG.debug(e.getMessage());
                //decrement code usage if any service layer exception occurs
               // this.registrationService.decrementUsage(activationCode);//removed for de-duplication
                AccountsBindingErrorController.convertMessages(e, bindingResult);
                return new ModelAndView(ACTIVATION_CODE_FORM_VIEW);
            } catch(ErightsException e) {
//                LOG.debug(e.getMessage());
                //decrement code usage if any service layer exception occurs
               // this.registrationService.decrementUsage(activationCode);//removed for de-duplication
                LOG.error("ErightsException");
                bindingResult.reject("error.problem.activating.token");
                return new ModelAndView(ACTIVATION_CODE_FORM_VIEW);
            } catch(Exception e){
                LOG.error(e.getMessage());
                bindingResult.reject("error.unknown", "Unexpected error.");
                return new ModelAndView(ACTIVATION_CODE_FORM_VIEW);
            }
        }else{
            LOG.debug("The activation code details are not valid.");
            bindingResult.reject("error.code.details.invalid", "The activation code details are not valid.");
            return new ModelAndView(ACTIVATION_CODE_FORM_VIEW);
        }
       
        SessionHelper.setActivationCode(request, activationCode);
        SessionHelper.setRegisterableProduct(request, (RegisterableProduct)product);
        return new ModelAndView("redirect:/downloadApp.htm");
    }
    
    
    private String trimActivationCode(final String activationCode) {
        StringTrimmerEditor editor = new StringTrimmerEditor("- ", true);
        editor.setAsText(activationCode);
        String trimmedActivationCode = editor.getAsText();
        return trimmedActivationCode;
    }
    
    private boolean checkSharedUser(Customer customer){
        if(customer != null && customer.getCustomerType() == CustomerType.SHARED){
            return true;
        }
        return false;
    }
}
