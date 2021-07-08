package com.oup.eac.admin.controllers;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.Product.ProductState;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegistrationDefinition.RegistrationDefinitionType;
import com.oup.eac.domain.beans.ProductBean;
import com.oup.eac.domain.paging.Paging;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.RegistrationDefinitionSearchCriteria;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.DivisionService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.ServiceLayerException;

@Controller
@RequestMapping("/mvc/product/finder.htm")
public class ProductFinderController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private DivisionService divisionService;
	
	@Autowired
	private RegistrationDefinitionService registrationDefinitionService;
	
	private Map<String,String> divisionList = new HashMap<String, String>() ;

	@RequestMapping(params = { "search_term", "search_by" })
	public ModelAndView find(
			@RequestParam(value = "search_term") final String searchTerm, 
			@RequestParam(value = "search_by") final String searchBy,
			@RequestParam(value = "type", required = false) final String type,
			@RequestParam(value = "product_state", required=false) final String[] productStateNames)
			throws ServiceLayerException {

		List<ProductBean> productBeans = findInternal(searchTerm, searchBy, type, productStateNames);

		ModelAndView modelAndView = new ModelAndView("productFinderResults");
		modelAndView.addObject("results", productBeans);
		
		return modelAndView;
	}
	
	@RequestMapping(params = "guid")
	public void init(
			@RequestParam(value = "guid") final String guid,
			final HttpServletResponse response) throws Exception {

		Product product = new RegisterableProduct();
		//ACB De-duplication
		//productService.getProductById(guid);
		
		//ACB De-duplication Update
		String regex = "\\d+";
		
		
		if (guid != null) {
			if ( !guid.matches(regex) ) {
				//product = productService.getProductById(guid) ;
				EnforceableProductDto enforceableProductDto = productService.getEnforceableProductByErightsId(guid);
				product.setId(enforceableProductDto.getProductId());
				product.setProductName(enforceableProductDto.getName());
			} else {
				EnforceableProductDto enforceableProductDto = productService.getEnforceableProductByErightsId(
						guid);
				product.setId(enforceableProductDto.getProductId());
				//product = productService.getProductById(enforceableProductDto.getProductId());
				product.setProductName(enforceableProductDto.getName());
			}
		}
		
		/*String[] states = new String[10];
		states[0] = "ACTIVE";
		//product.setExternalIds(enforceableProductDto.getExternalIds());
		List<ProductBean> productBeans = findInternal(product.getErightsId().toString(), "productId", 
				RegistrationDefinitionType.ACTIVATION_CODE_REGISTRATION.toString(), states);*/
		
		response.setContentType("application/json");
		OutputStream out = response.getOutputStream();
		IOUtils.write(toJson(product), out);
	}
	
	private String toJson(final Product product) {
		StringBuilder builder = new StringBuilder();
		builder.append("{\"id\": \"");
		if (product != null) {
			builder.append(product.getId());
		}
		builder.append("\", \"name\": \"");
		if (product != null) {
			builder.append(product.getProductName());
		}
		builder.append("\"}");
		return builder.toString();
	}

	private List<ProductBean> findInternal(final String searchTerm, final String searchBy, final String type, final String[] productStateNames)
			throws ServiceLayerException {
		
		RegistrationDefinitionSearchCriteria searchCriteria = new RegistrationDefinitionSearchCriteria();
		
		setSearchTerm(searchCriteria, searchTerm, searchBy);
		setProductStates(searchCriteria, productStateNames);
		setAdminUser(searchCriteria);
		setType(searchCriteria, type);

		Paging<ProductBean> resultsPage = productService.searchProduct(searchCriteria,
				PagingCriteria.valueOf(100, 1, null, null),divisionList);

		return resultsPage.getItems();
	}

	private void setSearchTerm(final RegistrationDefinitionSearchCriteria searchCriteria, final String searchTerm, final String searchBy) {
		if (StringUtils.isNotBlank(searchTerm)) {
			if ("productName".equals(searchBy)) {
				searchCriteria.setProductName(searchTerm);
			} else if ("productId".equals(searchBy)) {
				searchCriteria.setProductId(searchTerm);
			} else if ("externalId".equals(searchBy)) {
				searchCriteria.setExternalId(searchTerm);
			}
		}
	}

	private void setProductStates(final RegistrationDefinitionSearchCriteria searchCriteria, final String[] productStateNames) {
		if (ArrayUtils.isNotEmpty(productStateNames)) {
			Set<ProductState> states = new HashSet<ProductState>();
			for (String productStateName : productStateNames) {
				ProductState state = ProductState.valueOf(productStateName);
				if (state != null) {
					states.add(state);
				}
			}
			searchCriteria.setProductStates(states);;
		}
	}
	
	private void setType(final RegistrationDefinitionSearchCriteria searchCriteria, final String type) {
		if (StringUtils.isNotBlank(type)) {
			searchCriteria.setRegistrationDefinitionType(RegistrationDefinitionType.valueOf(type));
		}
	}
	private void setAdminUser(final RegistrationDefinitionSearchCriteria searchCriteria) {
		AdminUser adminUser = (AdminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Division> divisions = new ArrayList<Division>();
		try {
			divisions = divisionService.getDivisionsByAdminUser(adminUser);
		} catch (AccessDeniedException | ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Division division : divisions) {
			divisionList.put(division.getErightsId().toString(), division.getDivisionType()) ;
		}
		searchCriteria.setAdminUser(adminUser);
	}
}
