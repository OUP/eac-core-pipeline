package com.oup.eac.admin.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.admin.beans.ProductSearchBean;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.Platform;
import com.oup.eac.domain.beans.ProductBean;
import com.oup.eac.domain.paging.Paging;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.domain.paging.PagingCriteria.SortDirection;
import com.oup.eac.dto.RegistrationDefinitionSearchCriteria;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.DivisionNotFoundException;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.DivisionService;
import com.oup.eac.service.PlatformService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.ServiceLayerException;

@Controller
@RequestMapping("/mvc/product/search.htm")
public class ProductSearchController {

	@Autowired
	private DivisionService divisionService;
	@Autowired
	private PlatformService platformService;
	@Autowired
	private RegistrationDefinitionService registrationDefinitionService;
	@Autowired
	private ErightsFacade erightsFacade;
	@Autowired
	private ProductService productService;
	
	private Map<String,String> adminDivisionList = new HashMap<String, String>() ;
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showForm() throws DivisionNotFoundException, AccessDeniedException, ErightsException {
		return showFormInternal();
	}

	@RequestMapping(method = RequestMethod.POST, params = "_eventId=search")  // _eventId maintaining interoperability with WebFlow
	public ModelAndView searchProducts(@ModelAttribute("productSearchBean") final ProductSearchBean productSearchBean) throws ServiceLayerException {
		return searchProductsInternal(productSearchBean);
	}

	@RequestMapping(method = RequestMethod.POST, params = "_eventId=nextPage")
	public ModelAndView nextPage(@ModelAttribute("productSearchBean") final ProductSearchBean productSearchBean) throws ServiceLayerException {
		productSearchBean.incrementPageNumber();
		return searchProductsInternal(productSearchBean);
	}

	@RequestMapping(method = RequestMethod.POST, params = "_eventId=previousPage")
	public ModelAndView previousPage(@ModelAttribute("productSearchBean") final ProductSearchBean productSearchBean) throws ServiceLayerException {
		productSearchBean.decrementPageNumber();
		return searchProductsInternal(productSearchBean);
	}
	
	@RequestMapping(method = RequestMethod.POST, params = "_eventId=reset")
	public ModelAndView reset() throws ServiceLayerException, DivisionNotFoundException, AccessDeniedException, ErightsException {
		return showFormInternal();
	}
	
	private ModelAndView showFormInternal() throws DivisionNotFoundException, AccessDeniedException, ErightsException {
		AdminUser adminUser = (AdminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Division> adminDivisionsList = divisionService.getDivisionsByAdminUser(adminUser) ;
		List<Platform> platformList = platformService.getAllPlatforms() ;
		for (Division division : adminDivisionsList) {
			adminDivisionList.put(division.getErightsId().toString(), division.getDivisionType()) ;
		}
		ModelAndView modelAndView = new ModelAndView("searchProducts");
		modelAndView.addObject("availableDivisions", adminDivisionsList);
		modelAndView.addObject("availablePlatforms", platformList);
		modelAndView.addObject("fragments", "productSearchResultsTile");
		
		return modelAndView;
	}

	private ModelAndView searchProductsInternal(final ProductSearchBean productSearchBean) throws ServiceLayerException {
		RegistrationDefinitionSearchCriteria searchCriteria = createSearchCriteria(productSearchBean);
		Paging<ProductBean> resultsPage = productService.searchProduct(searchCriteria,
				PagingCriteria.valueOf(productSearchBean.getResultsPerPage(), productSearchBean.getPageNumber(), SortDirection.ASC, "productName" ),adminDivisionList);
		ModelAndView modelAndView = new ModelAndView("searchProducts");
		modelAndView.addObject("pageInfo", resultsPage);
		modelAndView.addObject("productBeans", resultsPage.getItems());
		return modelAndView;
	}

	private RegistrationDefinitionSearchCriteria createSearchCriteria(final ProductSearchBean productSearchBean) {
		RegistrationDefinitionSearchCriteria searchCriteria = productSearchBean.toRegistrationDefinitionSearchCriteria();
		searchCriteria.setProductId(searchCriteria.getProductId());
        searchCriteria.setProductName(searchCriteria.getProductName());
        searchCriteria.setExternalId(searchCriteria.getExternalId());
        searchCriteria.setUrl(searchCriteria.getUrl());
        searchCriteria.setPlatformCode(productSearchBean.getPlatformCode());

		AdminUser adminUser = (AdminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		searchCriteria.setAdminUser(adminUser);

		if (StringUtils.isNotBlank(productSearchBean.getDivisionType())) {
			Division division = new Division() ;
			division.setErightsId(Integer.parseInt(productSearchBean.getDivisionType()));
			searchCriteria.setDivision(division);
		}

		return searchCriteria;
	}

	@ModelAttribute("productSearchBean")
	public ProductSearchBean createModel(final HttpServletRequest request) {
		HttpSession session = request.getSession();
		ProductSearchBean productSearchBean = (ProductSearchBean) session.getAttribute("productSearchBean");

		if (productSearchBean == null || !isPaginationRequest(request)) {
			productSearchBean = new ProductSearchBean();
			session.setAttribute("productSearchBean", productSearchBean);
		}

		return productSearchBean;
	}
	
	private boolean isPaginationRequest(final HttpServletRequest request) {
		String eventId = request.getParameter("_eventId");
		return "nextPage".equals(eventId) || "previousPage".equals(eventId);
	}
	

	@RequestMapping(method = RequestMethod.POST, params = { "_eventId=match", "exampleUrl" })
	public ModelAndView matchProductsByErightsUrl(@ModelAttribute final ProductSearchBean productSearchBean) throws Exception {
		//return searchProductsInternal(productSearchBean);
		List<ProductBean> products = new ArrayList<ProductBean>();
		List<String> erightsIds = getErightsIds(productSearchBean);
		for(String erightsId : erightsIds){
			productSearchBean.setSearchBy("productId");
			productSearchBean.setSearchTerm(erightsId);
			RegistrationDefinitionSearchCriteria searchCriteria = createSearchCriteria(productSearchBean);
			Paging<ProductBean> result = productService.searchProduct(searchCriteria,
					PagingCriteria.valueOf(productSearchBean.getResultsPerPage(), productSearchBean.getPageNumber(), SortDirection.ASC, "productName" ),adminDivisionList);
			List<ProductBean> productList = result.getItems();
			if(productList!=null &&  productList.size()>0)
				products.add(productList.get(0));
		}
		Paging<ProductBean> resultsPage = Paging.valueOf(PagingCriteria.valueOf(100, 1, null, null), products, products.size());
		ModelAndView modelAndView = new ModelAndView("searchProducts");
		modelAndView.addObject("pageInfo", resultsPage);
		modelAndView.addObject("productBeans", products);
		return modelAndView;
	}
	
	//Not required as product will be directly searched from cloud
	/*private List<ProductBean> getProductsByErightsIds(final List<String> erightsIds, Map<String,String>  adminDivisionList) throws ServiceLayerException {
		List<ProductBean> products = new ArrayList<ProductBean>();

		for (String erightsId : erightsIds) {
			ProductBean product;
			try {
				product = productService.getProductByErightsId(erightsId, adminDivisionList).get(0);
			} catch (ErightsException e) {
				throw new ServiceLayerException(e.getMessage());
			}
			if (product != null) {
				products.add(product);
			}
		}
		
		return products;
	}*/
	
	private List<String> getErightsIds(final ProductSearchBean productSearchBean) throws ErightsException {
		List<String> erightsIds = new ArrayList<String>();

		if (StringUtils.isNotBlank(productSearchBean.getExampleUrl())) {
			try {
				erightsIds.addAll(erightsFacade.getProductIdsByUrl(productSearchBean.getExampleUrl()));
			} catch (Exception e) {
				//LOG.info(e, e);
			}
		}

		return erightsIds;
	}
	
}
