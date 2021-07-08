package com.oup.eac.admin.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.admin.beans.EacGroupsSearchBean;
import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.paging.Paging;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.domain.paging.PagingCriteria.SortDirection;
import com.oup.eac.dto.EacGroupsSearchCriteria;
import com.oup.eac.service.EacGroupService;
import com.oup.eac.service.ServiceLayerException;

@Controller
@RequestMapping("/mvc/eacGroups/search.htm")
public class EacGroupSearchController {
	
	@Autowired
	private EacGroupService eacGroupService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showForm() {
		return showFormInternal();
	}

	private ModelAndView showFormInternal() {
		//AdminUser adminUser = (AdminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		ModelAndView modelAndView = new ModelAndView("searchEacGroups");
		modelAndView.addObject("fragments", "eacGroupSearchResultsTile");
		
		return modelAndView;
	}
	
	@RequestMapping(method = RequestMethod.POST, params = "_eventId=search")  // _eventId maintaining interoperability with WebFlow
	public ModelAndView searchEacGroups(@ModelAttribute("eacGroupSearchBean") final EacGroupsSearchBean eacGroupSearchBean) throws ServiceLayerException {
		return searchEacGroupsInternal(eacGroupSearchBean);
	}

	@RequestMapping(method = RequestMethod.POST, params = "_eventId=nextPage")
	public ModelAndView nextPage(@ModelAttribute("eacGroupSearchBean") final EacGroupsSearchBean eacGroupSearchBean) throws ServiceLayerException {
		eacGroupSearchBean.incrementPageNumber();
		return searchEacGroupsInternal(eacGroupSearchBean);
	}

	@RequestMapping(method = RequestMethod.POST, params = "_eventId=previousPage")
	public ModelAndView previousPage(@ModelAttribute("eacGroupSearchBean") final EacGroupsSearchBean eacGroupSearchBean) throws ServiceLayerException {
		eacGroupSearchBean.decrementPageNumber();
		return searchEacGroupsInternal(eacGroupSearchBean);
	}
	
	@RequestMapping(method = RequestMethod.POST, params = "_eventId=reset")
	public ModelAndView reset() throws ServiceLayerException {
		return showFormInternal();
	}
	
	private ModelAndView searchEacGroupsInternal(final EacGroupsSearchBean eacGroupSearchBean) throws ServiceLayerException {
		EacGroupsSearchCriteria searchCriteria = createSearchCriteria(eacGroupSearchBean);
		Paging<EacGroups> resultsPage = eacGroupService.searchProductGroups(searchCriteria,
				PagingCriteria.valueOf(eacGroupSearchBean.getResultsPerPage(), eacGroupSearchBean.getPageNumber(), SortDirection.ASC, "groupName"));

		ModelAndView modelAndView = new ModelAndView("searchEacGroups");
		modelAndView.addObject("pageInfo", resultsPage);
		modelAndView.addObject("eacGroups", resultsPage.getItems());

		return modelAndView;
	}
	
	private EacGroupsSearchCriteria createSearchCriteria(final EacGroupsSearchBean eacGroupSearchBean) {
		EacGroupsSearchCriteria searchCriteria = eacGroupSearchBean.toProductGroupSearchCriteria();
		searchCriteria.setGroupName(searchCriteria.getGroupName());
		searchCriteria.setProductId(searchCriteria.getProductId());
        searchCriteria.setProductName(searchCriteria.getProductName());
        searchCriteria.setExternalId(searchCriteria.getExternalId());

		//AdminUser adminUser = (AdminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		return searchCriteria;
	}
	
	@ModelAttribute("eacGroupSearchBean")
	public EacGroupsSearchBean createModel(final HttpServletRequest request) {
		HttpSession session = request.getSession();
		EacGroupsSearchBean eacGroupSearchBean = (EacGroupsSearchBean) session.getAttribute("eacGroupSearchBean");

		if (eacGroupSearchBean == null || !isPaginationRequest(request)) {
			eacGroupSearchBean = new EacGroupsSearchBean();
			session.setAttribute("eacGroupSearchBean", eacGroupSearchBean);
		}

		return eacGroupSearchBean;
	}
	
	private boolean isPaginationRequest(final HttpServletRequest request) {
		String eventId = request.getParameter("_eventId");
		return "nextPage".equals(eventId) || "previousPage".equals(eventId);
	}
}
