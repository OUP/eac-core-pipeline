package com.oup.eac.admin.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.admin.beans.ActivationCodeSearchBean;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.paging.Paging;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.domain.paging.PagingCriteria.SortDirection;
import com.oup.eac.dto.ActivationCodeReportDto;
import com.oup.eac.dto.ActivationCodeSearchCriteria;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.GroupNotFoundException;
import com.oup.eac.integration.facade.exceptions.LicenseNotFoundException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.service.ActivationCodeService;
import com.oup.eac.service.ServiceLayerException;

@Controller
@RequestMapping("/mvc/activationCode/search.htm")
public class ActivationCodeSearchController {

    private static final String ACTIVATION_CODE_SEARCH_BEAN = "activationCodeSearchBean";
    
    @Autowired
    private ActivationCodeService activationCodeService;
    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showForm() {
        return showFormInternal();
    }

    @RequestMapping(method = RequestMethod.POST, params = "_eventId=search")  // _eventId maintaining interoperability with WebFlow
    public ModelAndView searchActivationCode(@ModelAttribute(ACTIVATION_CODE_SEARCH_BEAN) final ActivationCodeSearchBean searchBean,final BindingResult bindingResult) throws ServiceLayerException {
    	
    	if(bindingResult.hasErrors()){
    		bindingResult.reject("error.checkDBChar.invalid");
    		return  new ModelAndView("searchActivationCodes");
    	}
        return searchActivationCodesInternal(searchBean);
    }

    @RequestMapping(method = RequestMethod.POST, params = "_eventId=nextPage")
    public ModelAndView nextPage(@ModelAttribute(ACTIVATION_CODE_SEARCH_BEAN) final ActivationCodeSearchBean searchBean) throws ServiceLayerException {
        searchBean.incrementPageNumber();
        return searchActivationCodesInternal(searchBean);
    }

    @RequestMapping(method = RequestMethod.POST, params = "_eventId=previousPage")
    public ModelAndView previousPage(@ModelAttribute(ACTIVATION_CODE_SEARCH_BEAN) final ActivationCodeSearchBean searchBean) throws ServiceLayerException {
        searchBean.decrementPageNumber();
        return searchActivationCodesInternal(searchBean);
    }
    
    @RequestMapping(method = RequestMethod.GET, params="method=details")
    public ModelAndView details(@RequestParam("id")String id) throws ServiceLayerException, ProductNotFoundException, 
    UserNotFoundException, LicenseNotFoundException, AccessDeniedException, GroupNotFoundException, 
    ErightsException {
        ModelAndView modelAndView = new ModelAndView("activationCodeDetails");
        modelAndView.addObject("activationCode", activationCodeService.getActivationCodeWithDetails(id));
        return modelAndView;
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "_eventId=reset")
    public ModelAndView reset() throws ServiceLayerException {
        return showFormInternal();
    }
    
    private ModelAndView showFormInternal() {

        ModelAndView modelAndView = new ModelAndView("searchActivationCodes");
        modelAndView.addObject("fragments", "activationCodeSearchResultsTile");
        
        return modelAndView;
    }
    
    private ModelAndView searchActivationCodesInternal(final ActivationCodeSearchBean searchBean) throws ServiceLayerException {
        ActivationCodeSearchCriteria searchCriteria = searchBean.toActivationCodeSearchCriteria();
        //searchCriteria.setCode(AdminUtils.escapeSpecialChar(searchCriteria.getCode()));
        searchCriteria.setCode(searchCriteria.getCode());
        AdminUser adminUser = (AdminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Paging<ActivationCodeReportDto> resultsPage = activationCodeService.getActivationCodeSearch(
        		searchCriteria, PagingCriteria.valueOf(searchBean.getResultsPerPage(), searchBean.getPageNumber(), 
        				SortDirection.ASC, "productName"), adminUser);

        ModelAndView modelAndView = new ModelAndView("searchActivationCodes");
        modelAndView.addObject("pageInfo", resultsPage);

        return modelAndView;
    }
    
    @ModelAttribute("activationCodeSearchBean")
    public ActivationCodeSearchBean createModel(final HttpServletRequest request) {
        HttpSession session = request.getSession();
        ActivationCodeSearchBean activationCodeSearchBean = (ActivationCodeSearchBean) session.getAttribute(ACTIVATION_CODE_SEARCH_BEAN);

        if (activationCodeSearchBean == null || !isPaginationRequest(request)) {
            activationCodeSearchBean = new ActivationCodeSearchBean();
            session.setAttribute(ACTIVATION_CODE_SEARCH_BEAN, activationCodeSearchBean);
        }

        return activationCodeSearchBean;
    }
    
    private boolean isPaginationRequest(final HttpServletRequest request) {
        String method = request.getParameter("method");
        if(StringUtils.isNotBlank(method) && "details".equals(method)) {
            return true;
        }
        String eventId = request.getParameter("_eventId");
        return "nextPage".equals(eventId) || "previousPage".equals(eventId);
    }
    
    @InitBinder("activationCodeSearchBean")
	public void initBinder(final WebDataBinder binder) {	
	}
    
}
