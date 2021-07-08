package com.oup.eac.admin.controllers;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.paging.Paging;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.EacGroupsSearchCriteria;
import com.oup.eac.dto.ProductGroupDto;
import com.oup.eac.service.EacGroupService;
import com.oup.eac.service.ServiceLayerException;


@Controller
@RequestMapping("/mvc/eacGroups/finder.htm")
public class EacGroupFinderController {
	
	@Autowired
	private EacGroupService eacGroupService;
	
	@RequestMapping(params = "guid")
	public void init(
			@RequestParam(value = "guid") final String guid,
			final HttpServletResponse response) throws Exception {

		ProductGroupDto productGroupDto = eacGroupService.getProductGroupDtoByErightsId(
				guid);
		EacGroups eacGroup = eacGroupService.getEacGroupByProductGroupDto(productGroupDto);
		
		response.setContentType("application/json");
		OutputStream out = response.getOutputStream();
		IOUtils.write(toJson(eacGroup), out);
	}

	private String toJson(final EacGroups eacGroup) {
		StringBuilder builder = new StringBuilder();
		builder.append("{\"id\": \"");
		if (eacGroup != null) {
			builder.append(eacGroup.getId());
		}
		builder.append("\", \"name\": \"");
		if (eacGroup != null) {
			builder.append(eacGroup.getGroupName());
		}
		builder.append("\"}");
		return builder.toString();
	}
	
	@RequestMapping(params = { "search_term", "search_by" })
	public ModelAndView find(
			@RequestParam(value = "search_term") final String searchTerm, 
			@RequestParam(value = "search_by") final String searchBy)
			throws ServiceLayerException {

		List<EacGroups> eacGroups = findInternal(searchTerm, searchBy);

		ModelAndView modelAndView = new ModelAndView("eacGroupFinderResults");
		modelAndView.addObject("results", eacGroups);
		
		return modelAndView;
	}
	
	private List<EacGroups> findInternal(final String searchTerm, final String searchBy)
			throws ServiceLayerException {
		EacGroupsSearchCriteria searchCriteria=new EacGroupsSearchCriteria();
		
		setSearchTerm(searchCriteria, searchTerm, searchBy);
		
		Paging<EacGroups> resultsPage = eacGroupService.searchProductGroups(searchCriteria,
				PagingCriteria.valueOf(100, 1, null, null));

		return resultsPage.getItems();
	}
	
	private void setSearchTerm(final EacGroupsSearchCriteria searchCriteria, final String searchTerm, final String searchBy) {
		if (StringUtils.isNotBlank(searchTerm)) {
			if ("groupName".equals(searchBy)) {
				searchCriteria.setGroupName(searchTerm);
			} else if ("productName".equals(searchBy)) {
				searchCriteria.setProductName(searchTerm);
			} else if ("productId".equals(searchBy)) {
				searchCriteria.setProductId(searchTerm);
			} else if ("externalId".equals(searchBy)) {
				searchCriteria.setExternalId(searchTerm);
			}
		}
	}
}
