package com.oup.eac.admin.controllers;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.domain.AccountPageDefinition;
import com.oup.eac.domain.PageDefinition;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.dto.RegistrationDto;
import com.oup.eac.service.PageDefinitionService;
import com.oup.eac.service.RegistrationService;

@Controller
@RequestMapping("/mvc/page/preview.htm")
public class PagePreviewController {

	private final PageDefinitionService pageDefinitionService;
	private final RegistrationService registrationService;

	@Autowired
	public PagePreviewController(
			final PageDefinitionService pageDefinitionService,
			final RegistrationService registrationService) {
		this.pageDefinitionService = pageDefinitionService;
		this.registrationService = registrationService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showPreview() {
		return new ModelAndView("pagePreview");
	}

	@ModelAttribute("registrationDto")
	public RegistrationDto loadRegistrationDto(
			@RequestParam("id") final String id, 
			@RequestParam(value = "locale", required = false) final Locale locale) {
		RegistrationDto registrationDto = null;
		PageDefinition pageDef = pageDefinitionService.getFullyFetchedAccountPageDefinitionById(id);

		if (pageDef != null) {
			registrationDto = registrationService.getAccountRegistrationDto((AccountPageDefinition) pageDef, locale);
		} else {
			pageDef = pageDefinitionService.getFullyFetchedProductPageDefinitionById(id);
			registrationDto = registrationService.getProductRegistrationDto((ProductPageDefinition) pageDef, locale); 
		}

		return registrationDto;
	}

}
