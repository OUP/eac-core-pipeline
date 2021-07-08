package com.oup.eac.admin.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.admin.beans.ActivationCodeBatchSearchCriteriaBean;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.paging.Paging;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.domain.paging.PagingCriteria.SortDirection;
import com.oup.eac.dto.ActivationCodeBatchSearchCriteria;
import com.oup.eac.service.ActivationCodeService;
import com.oup.eac.service.ServiceLayerException;


@Controller
@RequestMapping("/mvc/activationCodeBatch/search.htm")
public class ActivationCodeBatchSearchController {
	
	private static final String ACTIVATION_CODE_BATCH_SEARCH_BEAN = "activationCodeBatchSearchCriteriaBean";
	
	@Autowired
	private ActivationCodeService activationCodeService;
		 
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showForm() {
		return showFormInternal();
	}
	 
	private ModelAndView showFormInternal() {
		ModelAndView modelAndView = new ModelAndView("searchActivationCodeBatches");
		modelAndView.addObject("fragments", "searchResultsTile");
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.POST, params = "_eventId=search")  // _eventId maintaining interoperability with WebFlow
	public ModelAndView searchActivationCodeBatch(@ModelAttribute(ACTIVATION_CODE_BATCH_SEARCH_BEAN) final ActivationCodeBatchSearchCriteriaBean searchBean,final BindingResult bindingResult) throws ServiceLayerException {
		if(bindingResult.hasErrors()){
			bindingResult.reject("error.checkDBChar.invalid");
			return  new ModelAndView("searchActivationCodeBatches");
		}
		return searchActivationCodeBatchInternal(searchBean);
	}

	@RequestMapping(method = RequestMethod.POST, params = "_eventId=nextPage")
	public ModelAndView nextPage(@ModelAttribute(ACTIVATION_CODE_BATCH_SEARCH_BEAN) final ActivationCodeBatchSearchCriteriaBean searchBean) throws ServiceLayerException {
		searchBean.incrementPageNumber();
		return searchActivationCodeBatchInternal(searchBean);
	}

	@RequestMapping(method = RequestMethod.POST, params = "_eventId=previousPage")
	public ModelAndView previousPage(@ModelAttribute(ACTIVATION_CODE_BATCH_SEARCH_BEAN) final ActivationCodeBatchSearchCriteriaBean searchBean) throws ServiceLayerException {
		searchBean.decrementPageNumber();
		return searchActivationCodeBatchInternal(searchBean);
	}

	@RequestMapping(method = RequestMethod.POST, params = "_eventId=reset")
	public ModelAndView reset() throws ServiceLayerException {
		return showFormInternal();
	}

	private ModelAndView searchActivationCodeBatchInternal(final ActivationCodeBatchSearchCriteriaBean searchBean) throws ServiceLayerException {
		ActivationCodeBatchSearchCriteria searchCriteria = (ActivationCodeBatchSearchCriteria) searchBean.toActivationCodeBatchSearchCriteria();
		Paging<ActivationCodeBatch> resultsPage = activationCodeService.searchActivationCodeBatches(searchCriteria, new PagingCriteria(searchBean.getResultsPerPage(), searchBean.getPageNumber(), SortDirection.ASC, "batchId"));
		ModelAndView modelAndView = new ModelAndView("searchActivationCodeBatches");
		modelAndView.addObject("pageInfo", resultsPage);
		modelAndView.addObject("activationCodeBatches", resultsPage.getItems());
		return modelAndView;
	}

	@ModelAttribute("activationCodeBatchSearchCriteriaBean")
	public ActivationCodeBatchSearchCriteriaBean createModel(final HttpServletRequest request) {
		HttpSession session = request.getSession();
		ActivationCodeBatchSearchCriteriaBean activationCodeBatchSearchCriteriaBean = (ActivationCodeBatchSearchCriteriaBean) session.getAttribute(ACTIVATION_CODE_BATCH_SEARCH_BEAN);
		if (activationCodeBatchSearchCriteriaBean == null || !isPaginationRequest(request)) {
			activationCodeBatchSearchCriteriaBean = new ActivationCodeBatchSearchCriteriaBean();
			activationCodeBatchSearchCriteriaBean.getAllowedLicenceTypes();
			session.setAttribute(ACTIVATION_CODE_BATCH_SEARCH_BEAN, activationCodeBatchSearchCriteriaBean);
		}
		return activationCodeBatchSearchCriteriaBean;
	}

	private boolean isPaginationRequest(final HttpServletRequest request) {
		String method = request.getParameter("method");
		if(StringUtils.isNotBlank(method) && "details".equals(method)) {
			return true;
		}
		String eventId = request.getParameter("_eventId");
		return "nextPage".equals(eventId) || "previousPage".equals(eventId);
	}

	@InitBinder("activationCodeBatchSearchCriteriaBean")
	public void initBinder(final WebDataBinder binder) {
	}
}