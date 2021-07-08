package com.oup.eac.admin.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

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

import com.constants.SearchDomainFields;
import com.oup.eac.admin.beans.PageDefinitionBean;
import com.oup.eac.admin.binding.ComponentTypePropertyEditor;
import com.oup.eac.admin.binding.DivisionTypePropertyEditor;
import com.oup.eac.admin.validators.PageDefinitionBeanValidator;
import com.oup.eac.cloudSearch.search.impl.AmazonCloudSearchServiceImpl;
import com.oup.eac.domain.AccountPageDefinition;
import com.oup.eac.domain.AccountRegistrationDefinition;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Component;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.PageDefinition;
import com.oup.eac.domain.PageDefinition.PageDefinitionType;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegistrationDefinition;
import com.oup.eac.domain.RegistrationDefinition.RegistrationDefinitionType;
import com.oup.eac.domain.beans.ProductBean;
import com.oup.eac.domain.search.AmazonSearchFields;
import com.oup.eac.domain.search.AmazonSearchRequest;
import com.oup.eac.domain.search.AmazonSearchResponse;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.ComponentService;
import com.oup.eac.service.DivisionService;
import com.oup.eac.service.ElementService;
import com.oup.eac.service.MessageService;
import com.oup.eac.service.PageDefinitionService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.util.ConvertSearch;

@Controller
@RequestMapping("/mvc/page/manage.htm")
public class ManagePageController {

	private static final String NEW_ACCOUNT_PAGE = "newAccountPage";
	private static final String NEW_PRODUCT_PAGE = "newProductPage";
	private static final String MANAGE_PAGE_VIEW = "managePage";
	private static final String MANAGE_PAGE_FORM_VIEW = "managePageForm";
	private static final String MODEL = "pageDefinitionBean";
	private static final String PAGE_DEFINITION_TYPES = "pageDefinitionTypes";
	private static final String SUPPORTED_LOCALES = "supportedLocales";
	private final DivisionService divisionService;
	private final PageDefinitionService pageDefinitionService;
	private final ComponentService componentService;
	private final ElementService elementService;
	private final MessageService messageService;
	private final PageDefinitionBeanValidator validator;
	private final RegistrationDefinitionService registartionDefinitionService;
	private final ProductService productService;
	@Autowired
	public ManagePageController(
			final PageDefinitionService pageDefinitionService, 
			final ComponentService componentService,
			final ElementService elementService,
			final MessageService messageService,
			final PageDefinitionBeanValidator validator,
			final DivisionService divisionService,
			final RegistrationDefinitionService registartionDefinitionService,
			final ProductService productService) {
		this.pageDefinitionService = pageDefinitionService;
		this.componentService = componentService;
		this.elementService = elementService;
		this.messageService = messageService;
		this.validator = validator;
		this.divisionService = divisionService;
		this.registartionDefinitionService = registartionDefinitionService;
		this.productService = productService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showPageList() {
		return new ModelAndView(MANAGE_PAGE_VIEW);
	}

	@RequestMapping(method = RequestMethod.GET, params = { "id", "deep" })
	public ModelAndView showAll() {
		return new ModelAndView(MANAGE_PAGE_VIEW);
	}

	@RequestMapping(method = RequestMethod.GET, params = "id")
	public ModelAndView showForm() {
		return new ModelAndView(MANAGE_PAGE_FORM_VIEW);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView savePageDefinition(@Valid @ModelAttribute(MODEL) final PageDefinitionBean pageDefinitionBean, final BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			return new ModelAndView(MANAGE_PAGE_VIEW);
		}
		
		PageDefinition pageDefinition = pageDefinitionBean.getUpdatedPageDefinition();
		pageDefinition.setDivisionErightsId(pageDefinition.getDivision().getErightsId());
		pageDefinitionService.savePageDefinition(pageDefinition);
		if (pageDefinitionBean.isNewPageDefinition()) {
			AuditLogger.logEvent("New page '" + pageDefinition.getName() + "' created");
		} else {
			AuditLogger.logEvent("Page '" + pageDefinition.getName() + "' updated");
		}
		return new ModelAndView("redirect:/mvc/page/manage.htm?statusMessageKey=status.page.save.success&deep=1&id=" + pageDefinition.getId());
	}
	
	@RequestMapping(method=RequestMethod.POST, params="delete")
	public ModelAndView deletePageDefinition(@ModelAttribute(MODEL) final PageDefinitionBean pageDefinitionBean) {
		PageDefinition pageDefinition = pageDefinitionBean.getSelectedPageDefinition();
		pageDefinitionService.deletePageDefinition(pageDefinition);
		AuditLogger.logEvent("Page '" + pageDefinition.getName() + "' deleted");
		return new ModelAndView("redirect:/mvc/page/manage.htm?statusMessageKey=status.page.delete.success");
	}
	
	@ModelAttribute(PAGE_DEFINITION_TYPES)
	public PageDefinitionType[] getPageDefinitionTypes() {
		return PageDefinitionType.values();
	}
	
	@ModelAttribute(SUPPORTED_LOCALES)
	public List<Locale> getSupportedLocales() {
		Set<Locale> locales = new HashSet<Locale>();
		locales.addAll(elementService.getOrderedElementCountryRestrictionLocales());
		locales.addAll(messageService.getSupportedLocalesOrderedByLanguageAndCountry());
		List<Locale> orderedLocales = new ArrayList<Locale>(locales);
		Collections.sort(orderedLocales, new Comparator<Locale>() {
			@Override
			public int compare(Locale o1, Locale o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});
		return orderedLocales;
	}

	@ModelAttribute(MODEL)
	public PageDefinitionBean createModel(@RequestParam(value = "id", required = false) final String id) {
		AdminUser adminUser = (AdminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Component> components = componentService.getComponentsOrderedByName();
		List<PageDefinition> pageDefinitions = new ArrayList<PageDefinition>();
		List<Division> divisions = new ArrayList<Division>();
		try {
			divisions = divisionService.getDivisionsByAdminUser(adminUser);
		} catch (AccessDeniedException | ErightsException e) {
			e.printStackTrace();
		}
		pageDefinitions.addAll(pageDefinitionService.getAvailableAccountPageDefinitions(adminUser));
		pageDefinitions.addAll(pageDefinitionService.getAvailableProductPageDefinitions(adminUser));
		
		PageDefinitionBean pageDefinitionBean = new PageDefinitionBean(pageDefinitions, components, divisions);
		
		if (StringUtils.isNotBlank(id)) {
			if (NEW_ACCOUNT_PAGE.equals(id)) {
				pageDefinitionBean.setSelectedPageDefinition(new AccountPageDefinition());
				pageDefinitionBean.setNewAccountPageDefinition(true);
			} else if (NEW_PRODUCT_PAGE.equals(id)) {
				pageDefinitionBean.setSelectedPageDefinition(new ProductPageDefinition());
				pageDefinitionBean.setNewProductPageDefinition(true);
			} else {
				PageDefinition pageDef = null;
				for (PageDefinition pageDefinition : pageDefinitions) {
					if (pageDefinition.getId().equals(id)) {
						List<Division> divs = null;
						try {
							divs = divisionService.getAllDivisions();
						} catch (AccessDeniedException | ErightsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						for (Division div : divs) {
							if (pageDefinition.getDivisionErightsId().equals(div.getErightsId())) {
								pageDefinition.setDivision(div);
							}
							
						}		
						pageDef = pageDefinition;
						
						List<ProductRegistrationDefinition> productRegistrationDefinitions = null;
						List<AccountRegistrationDefinition> accountRegistrationDefinitions = null;
						//EnforceableProductDto enforceableProductDto = null;
						List<RegistrationDefinition> registrationDefinitions = new ArrayList<RegistrationDefinition>() ;
						try {
							productRegistrationDefinitions = 
									registartionDefinitionService.getRegistrationDefinitionsForPageDefinition(id);
							registrationDefinitions.addAll(productRegistrationDefinitions);
							
						} catch (ServiceLayerException e1) {
							e1.printStackTrace();
						}
						
						try {
							accountRegistrationDefinitions = 
									registartionDefinitionService.getRegistrationDefinitionsForPageDefinitionForAccount(id);
							registrationDefinitions.addAll(accountRegistrationDefinitions);
							
						} catch (ServiceLayerException e1) {
							e1.printStackTrace();
						}
						
						List<String> productIds = new ArrayList<String>();
						if (registrationDefinitions != null && registrationDefinitions.size()>0) {
							for (RegistrationDefinition registrationDefinition : registrationDefinitions) {
								if(registrationDefinition!=null){
									productIds.add(registrationDefinition.getProductId());
								}
							}
							
							
							String[] prodIds = new String[10];
							int i = 0;
							List<ProductBean> products = new ArrayList<ProductBean>();
							for (String prodId : productIds) {
								if (!prodId.equals("")) {
									prodIds[i++] = prodId;
								}
							
								if(i ==10){
									AmazonSearchRequest request = new AmazonSearchRequest();
									List<AmazonSearchFields> searchFieldsList = new ArrayList<AmazonSearchFields>();
									AmazonSearchFields searchField = new AmazonSearchFields();
									searchField.setName(SearchDomainFields.PRODUCT_PRODUCTID);
									searchField.setValue(Arrays.toString(prodIds).replace("[", "").replace("]", "").replace(" ", ""));
									searchField.setType("String");
									searchFieldsList.add(searchField);

									List<String> searchResultFieldsList = new ArrayList<String>();
									searchResultFieldsList.add(SearchDomainFields.PRODUCT_PRODUCTID);
									searchResultFieldsList.add(SearchDomainFields.PRODUCT_PRODUCTNAME);

									request.setSearchFieldsList(searchFieldsList);
									//request.setResultsPerPage(9999);
									//System.out.println(request);
									request.setSearchResultFieldsList(searchResultFieldsList);
									AmazonCloudSearchServiceImpl cloudSearch = new AmazonCloudSearchServiceImpl();
									AmazonSearchResponse response = cloudSearch.searchProduct(request);
									ConvertSearch convert = new ConvertSearch();
									final Map<String,String> divisionList = new HashMap<String,String>();
									products.addAll(convert.convertProduct(response,divisionList));
									prodIds = new String[10];
									i=0;
								}
								
							}
							
							if(i>0 && prodIds!=null){
								AmazonSearchRequest request = new AmazonSearchRequest();
								List<AmazonSearchFields> searchFieldsList = new ArrayList<AmazonSearchFields>();
								AmazonSearchFields searchField = new AmazonSearchFields();
								searchField.setName(SearchDomainFields.PRODUCT_PRODUCTID);
								searchField.setValue(Arrays.toString(prodIds).replace("[", "").replace("]", "").replace(" ", ""));
								searchField.setType("String");
								searchFieldsList.add(searchField);

								List<String> searchResultFieldsList = new ArrayList<String>();
								searchResultFieldsList.add(SearchDomainFields.PRODUCT_PRODUCTID);
								searchResultFieldsList.add(SearchDomainFields.PRODUCT_PRODUCTNAME);

								request.setSearchFieldsList(searchFieldsList);
								//request.setResultsPerPage(9999);
								//System.out.println(request);
								request.setSearchResultFieldsList(searchResultFieldsList);
								AmazonCloudSearchServiceImpl cloudSearch = new AmazonCloudSearchServiceImpl();
								AmazonSearchResponse response = cloudSearch.searchProduct(request);
								ConvertSearch convert = new ConvertSearch();
								final Map<String,String> divisionList = new HashMap<String,String>();
								products.addAll(convert.convertProduct(response,divisionList));
							}
							
							/*enforceableProductDto = productService.getEnforceableProductByErightsId(
									productRegistrationDefinition.getProduct().getId());*/
							for(ProductBean productNew : products){
								for(RegistrationDefinition regDef : registrationDefinitions){
									if(regDef.getProductId().equals(productNew.getProductId())){
										Product product = new RegisterableProduct();
										product.setProductName(productNew.getProductName());
										product.setId(productNew.getProductId());
										regDef.setProduct(product);
										if (regDef.getRegistrationDefinitionType() == RegistrationDefinitionType.ACCOUNT_REGISTRATION) {
											AccountRegistrationDefinition actRegDef = (AccountRegistrationDefinition)regDef ;
											actRegDef.setPageDefinition((AccountPageDefinition)pageDefinition);
										} else {
											ProductRegistrationDefinition proRegDef = (ProductRegistrationDefinition)regDef ;
											proRegDef.setPageDefinition((ProductPageDefinition)pageDefinition);
										}
									}
								}
							}
							
						}
						break;
					}
				}
				pageDefinitionBean.setSelectedPageDefinition(pageDef);
			}
		}
		return pageDefinitionBean;
	}
	
	@InitBinder
	public void initBinder(final WebDataBinder webDataBinder) {
		webDataBinder.setValidator(validator);
		webDataBinder.registerCustomEditor(Division.class, new DivisionTypePropertyEditor(divisionService));
		webDataBinder.registerCustomEditor(Component.class, new ComponentTypePropertyEditor(componentService));
	}
}
