package com.oup.eac.accounts.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.Product;
import com.oup.eac.dto.ExternalProductIdDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.ProductService;
import com.oup.eac.web.controllers.helpers.SessionHelper;

/**
 * Spring MVC Controller for App download link.
 * @author Gaurav Soni
 */

@Controller
public class AccountsAppDownloadController {

    private static final Logger LOG = Logger.getLogger(AccountsAppDownloadController.class);
    private final String APP_DOWNLOAD_VIEW = "appDownload";
    private final String ACTIVATION_CODE_FORM_VIEW = "redeemActivationCodeForm";
    private final ExternalIdService externalIdService;
    private final ProductService productService;
    private String appleAppId = "appleAppID";
    private String googleAppId = "googleAppId";
    
    
    @Autowired
    public AccountsAppDownloadController(ExternalIdService externalIdService,ProductService productService){
        this.externalIdService = externalIdService;
        this.productService=productService;
    }

    @RequestMapping(value = { "/downloadApp" }, method=RequestMethod.GET)
    public ModelAndView showForm(final HttpServletRequest request, final HttpServletResponse response) {
        Product product = (Product)SessionHelper.getRegisterableProduct(request);
        Map<String, String> appIDMap = new HashMap<String, String>();
        
        try{
            appIDMap = getAppIds(product);
        }
        catch(ProductNotFoundException e){
            LOG.error(e.getMessage());
            SessionHelper.removeActivationCode(request);
            request.getSession().removeAttribute("PRODUCT");
            return new ModelAndView(ACTIVATION_CODE_FORM_VIEW);
        }
        catch(ErightsException e){
        	  LOG.error(e.getMessage());
              SessionHelper.removeActivationCode(request);
              request.getSession().removeAttribute("PRODUCT");
              return new ModelAndView(ACTIVATION_CODE_FORM_VIEW);
        }
        catch(Exception e){
            LOG.error(e.getMessage());
            SessionHelper.removeActivationCode(request);
            request.getSession().removeAttribute("PRODUCT");
            return new ModelAndView(ACTIVATION_CODE_FORM_VIEW);
        }
        
        //remove activation code and product once app ids has been extracted. This would stop user to again submit the request with same parameter.
        SessionHelper.removeActivationCode(request);
        request.getSession().removeAttribute("PRODUCT");
        ModelAndView  modelAndView  = new ModelAndView(APP_DOWNLOAD_VIEW);
        
        // add the id map to model to be rendered on jsp
        modelAndView.addObject("appIDs", appIDMap);
        modelAndView.addObject("productName", product.getProductName());
        return modelAndView;
    }
    
    //get the app id from external ids of product
    private Map<String, String> getAppIds(Product product) throws ProductNotFoundException, ErightsException{
        Map<String, String> appIDs = new HashMap<String, String>();
        //ExternalProductIdDto externalProductIdDto = externalIdService.getExternalProductIds(Arrays.asList(product));
        //List<ExternalProductId> extProductIDList = externalProductIdDto.getExternalProductIds(product);
        
        List<ExternalProductId> extProductIDList=productService.getEnforceableProductByErightsId(product.getId()).getExternalIds();
        for (ExternalProductId externalProductId : extProductIDList) {
            if(externalProductId.getExternalSystemIdType().getName().equalsIgnoreCase(appleAppId) || externalProductId.getExternalSystemIdType().getName().equalsIgnoreCase(googleAppId)){
                appIDs.put(externalProductId.getExternalSystemIdType().getName(), externalProductId.getExternalId());
            }
        }
        return appIDs;
    }
}
