package com.oup.eac.admin.controllers;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.admin.beans.WhiteListUrlBean;
import com.oup.eac.admin.validators.UrlValidator;
import com.oup.eac.domain.WhiteListUrl;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.WhiteListUrlService;

@Controller
@RequestMapping("/mvc/WhiteListUrls")
public class WhiteListUrlsController {

	private static final String WHITE_LIST_URLS_VIEW = "manageUrls";
	private static final String MODEL = "whiteListUrlBean";
	private static final String MSG_UPDATE_SUCCESS   = "status.url.update.success";
	private static final String MSG_UPDATE_NO_CHANGE = "status.url.update.nothing";
	private static final String MSG_KEY="statusMessageKey";
	private static final String MANAGE_ORG_UNITS_VIEW = "manageUrls";
    private static final String AFTER_URL_UPDATE_VIEW = "redirect:/mvc/WhiteListUrls/whiteListUrls.htm";
	private static final String UPDATE_URL_SUCCESS_VIEW = String.format("%s?%s=%s",AFTER_URL_UPDATE_VIEW, MSG_KEY, MSG_UPDATE_SUCCESS);
	private static final String UPDATE_URL_NO_CHANGE_VIEW = String.format("%s?%s=%s",AFTER_URL_UPDATE_VIEW, MSG_KEY, MSG_UPDATE_NO_CHANGE);
	   
	
	private final WhiteListUrlService whiteListService;
	private final Validator validator;
	@InitBinder
    public void initBinder(final WebDataBinder webDataBinder) {
        webDataBinder.setValidator(validator);
    }
	@Autowired
    public WhiteListUrlsController(final WhiteListUrlService whiteListService, final UrlValidator validator) {
        this.whiteListService = whiteListService;
        this.validator = validator;
    }

	 @RequestMapping(value = { "/whiteListUrls.htm" }, method = RequestMethod.GET)
	 public ModelAndView showForm() {
		 ModelAndView modelAndView = new ModelAndView(MANAGE_ORG_UNITS_VIEW);
	        return modelAndView;
	 }	
	 
	 @ModelAttribute(MODEL)
	    public WhiteListUrlBean createModel() {
		 WhiteListUrlBean model = null;
			try {
				List<WhiteListUrl> allUrls = whiteListService.getAllUrls();
				if(allUrls != null && !allUrls.isEmpty()) {
					model = new WhiteListUrlBean(allUrls);
				} else {
					model = new WhiteListUrlBean(new ArrayList<WhiteListUrl>());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return model;
	    }
	 
	 @InitBinder
	 public void initBinder(){
		 
	 }
	 
	 @RequestMapping(value = { "/whiteListUrls.htm" }, method = RequestMethod.POST)
	    public String updateList(final @Valid @ModelAttribute(MODEL) WhiteListUrlBean urlBean,
	            final BindingResult bindingResult) throws ServiceLayerException, ErightsException {
	        if (bindingResult.hasErrors()) {
	            return MANAGE_ORG_UNITS_VIEW;
	        }
	        List<WhiteListUrl> toDelete = new ArrayList<WhiteListUrl>();
	        List<WhiteListUrl> toUpdate = new ArrayList<WhiteListUrl>();
	        Set<Integer> indexesToRemove = urlBean.getIndexesToDelete();

	        for (int i = 0; i < urlBean.getUrlList().size(); i++) {
	            WhiteListUrl url = urlBean.getUrlList().get(i);
	            Assert.isTrue(StringUtils.isNotBlank(url.getId()));
	            
	            if (indexesToRemove.contains(i)) {
	                toDelete.add(url);
	            } else {
	                toUpdate.add(url);
	            }
	        }
	        List<WhiteListUrl> toAdd = urlBean.getNewUrl();

	        //sessionFactory.getCurrentSession().clear();
	        boolean updated = false;
			updated = whiteListService.updateUrls(toDelete, toUpdate, toAdd);
	        if(updated){
	            return UPDATE_URL_SUCCESS_VIEW;    
	        }else{
	            return UPDATE_URL_NO_CHANGE_VIEW;    
	        }
	        
	    }

	}
