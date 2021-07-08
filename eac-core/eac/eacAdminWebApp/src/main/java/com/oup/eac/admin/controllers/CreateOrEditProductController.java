package com.oup.eac.admin.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.JavaScriptUtils;

import com.oup.eac.admin.validators.ProductBeanValidator;
import com.oup.eac.domain.AccountRegistrationDefinition;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.CircularCountryMatchException;
import com.oup.eac.domain.CountryMatchRegistrationActivation;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.LinkedProductNew;
import com.oup.eac.domain.MatchedCountryMatchRegistrationActivationVisitor;
import com.oup.eac.domain.Platform;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegistrationActivation;
import com.oup.eac.domain.RegistrationDefinition;
import com.oup.eac.domain.UnmatchedCountryMatchRegistrationActivationVisitor;
import com.oup.eac.domain.ValidatedRegistrationActivation;
import com.oup.eac.domain.beans.ProductBean;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.ExternalIdDto;
import com.oup.eac.integration.facade.exceptions.ChildProductFoundException;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.service.DivisionService;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.LicenceTemplateService;
import com.oup.eac.service.PageDefinitionService;
import com.oup.eac.service.PlatformService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationActivationService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.process.ProductUpdateProcess;

@Controller
@RequestMapping("/mvc/product")
public class CreateOrEditProductController {

	private static final String CREATE_EDIT_PRODUCT_VIEW = "createOrEditProduct";
	private static final String PRODUCT_DELETE_SUCCESS = "status.product.delete.success";
	private static final String PRODUCT_CREATE_SUCCESS = "status.product.create.success";
	private static final String PRODUCT_UPDATE_SUCCESS = "status.product.update.success";
	private static final String PRODUCT_DELETE_FAILURE = "status.product.delete.failure";
	private static final String PRODUCT_USED_DELETE_FAILURE = "status.product.used.delete";
	private static final String RECORD_SEPARATOR = "\\|";
	private static final String FIELD_SEPARATOR = ",";
	private final ProductService productService;
	private final RegistrationDefinitionService registrationDefinitionService;
	private final PageDefinitionService pageDefinitionService;
	private final DivisionService divisionService;
	private final RegistrationActivationService registrationActivationService;
	private final LicenceTemplateService licenceTemplateService;
	private final ExternalIdService externalIdService;
	private final PlatformService platformService;
	private final ProductBeanValidator validator;

	@Autowired
	public CreateOrEditProductController(
			final ProductService productService, 
			final RegistrationDefinitionService registrationDefinitionService, 
			final PageDefinitionService pageDefinitionService,
			final RegistrationActivationService registrationActivationService,
			final LicenceTemplateService licenceTemplateService,
			final ExternalIdService externalIdService,
			final ProductBeanValidator validator,
			final DivisionService divisionService,
			final PlatformService platformService) {
		this.productService = productService;
		this.registrationDefinitionService = registrationDefinitionService;
		this.pageDefinitionService = pageDefinitionService;
		this.registrationActivationService = registrationActivationService;
		this.licenceTemplateService = licenceTemplateService;
		this.externalIdService = externalIdService;
		this.validator = validator;
		this.divisionService = divisionService ;
		this.platformService = platformService ;
	}

	@RequestMapping(value = { "/create.htm", "/edit.htm" }, method=RequestMethod.GET)
	public ModelAndView showForm() {
		return new ModelAndView(CREATE_EDIT_PRODUCT_VIEW);
	}

	@RequestMapping(value = { "/create.htm", "/edit.htm" }, method=RequestMethod.POST)
	public ModelAndView saveProduct(@Valid @ModelAttribute("productBean") final ProductBean productBean, final BindingResult bindingResult) throws Exception {

		if (bindingResult.hasErrors()) {
			return new ModelAndView(CREATE_EDIT_PRODUCT_VIEW);
		}
		
		/*if (!productBean.isEditMode()) {
			Product product = productService.getProductById(productBean.getProductId());
			if(product!=null){
				bindingResult.reject("error.productExistWithId");
				return new ModelAndView(CREATE_EDIT_PRODUCT_VIEW);
			}
		}*/
		
		if (isProductNameInUse(productBean)) {
			bindingResult.reject("error.productNameInUse");
			return new ModelAndView("createOrEditProduct");
		}

		EnforceableProductDto enforceableProduct = productBean.getEnforceableProduct();
		String messageKey = null;
		try {


			if (productBean.isEditMode()) {
				//erightsFacade.updateProduct(enforceableProduct);
				messageKey = PRODUCT_UPDATE_SUCCESS;
			} else {
				messageKey = PRODUCT_CREATE_SUCCESS;
			}
			saveProductInternal(productBean, enforceableProduct);
		} catch (final CircularCountryMatchException e) {
			bindingResult.reject("error.circularCountryMatch", e.getMessage());
			return new ModelAndView(CREATE_EDIT_PRODUCT_VIEW);
		} catch (final Exception e) {
			bindingResult.reject("error.circularCountryMatch", e.getMessage());
			return new ModelAndView(CREATE_EDIT_PRODUCT_VIEW);
		}

		if (messageKey.equals(PRODUCT_CREATE_SUCCESS)) {
			
			return new ModelAndView("redirect:/mvc/product/search.htm?statusMessageKey=" + messageKey + "&id=" + productBean.getProductId() );
		} else {
			return new ModelAndView("redirect:/mvc/product/search.htm?statusMessageKey=" + messageKey );
		}

		
	}

	private EnforceableProductDto createErightsProduct(final EnforceableProductDto enforceableProduct, final ProductBean productBean, ProductRegistrationDefinition productRegistrationDefinition, Product product)
			throws ErightsException, UnsupportedEncodingException, ServiceLayerException {
		//Product De-duplication
		/*LicenceDetailDto licenceDetailDto = ErightsObjectFactory.getLicenceTemplateDto(productRegistrationDefinition.getLicenceTemplate());
		if(productRegistrationDefinition.getRegistrationActivation().getName().toString().toLowerCase()
				.contains(ActivationStrategy.INSTANT.toString().toLowerCase())) {
			enforceableProduct.setActivationStrategy(ActivationStrategy.INSTANT.toString());
		} else if(productRegistrationDefinition.getRegistrationActivation().getName().toString().toLowerCase()
				.contains(ActivationStrategy.SELF.toString().toLowerCase())) {
			enforceableProduct.setActivationStrategy(ActivationStrategy.SELF.toString());
		} else {
			enforceableProduct.setActivationStrategy(ActivationStrategy.VALIDATED.toString());
		}*/
		
		RegistrationActivation registrationActivation = productRegistrationDefinition.getRegistrationActivation();
		if (registrationActivation instanceof ValidatedRegistrationActivation) {
			enforceableProduct.setValidatorEmail(((ValidatedRegistrationActivation) registrationActivation).getValidatorEmail());
		}
		//Product De-duplication
		/*enforceableProduct.setConfirmationEmailEnabled(productRegistrationDefinition.isConfirmationEmailEnabled());
		enforceableProduct.setEacId(productBean.getProductId());
		enforceableProduct.setLandingPage(productBean.getLandingPage());
		enforceableProduct.setHomePage(productBean.getHomePage());
		enforceableProduct.setSla(productBean.getSla());
		enforceableProduct.setRegisterableType(productBean.getRegisterableType().toString());
		
		Set<ExternalProductId> externalIdsToAdd = new HashSet<ExternalProductId>();
		if (StringUtils.isNotBlank(productBean.getExternalIdsToAdd())) {
			String[] records = productBean.getExternalIdsToAdd().split(RECORD_SEPARATOR);
			for (String record : records) {
				String[] fields = splitIntoFields(record);
				ExternalProductId externalProductId = new ExternalProductId();
				externalProductId.setExternalId(fields[0].trim());
				externalProductId.setProduct(product);
				ExternalSystemIdType externalSystemIdType = externalIdService.getExternalSystemIdTypeById(fields[2].trim());
				externalProductId.setExternalSystemIdType(externalSystemIdType);
				externalIdsToAdd.add(externalProductId);
			}
			product.getExternalIds().addAll(externalIdsToAdd);
		}*/
		
		//enforceableProduct.setExternalIds(product.getExternalIds());
		
		
		if(productRegistrationDefinition instanceof ActivationCodeRegistrationDefinition)
		{
			enforceableProduct.setRegistrationDefinitionType(productRegistrationDefinition.getRegistrationDefinitionType().ACTIVATION_CODE_REGISTRATION.toString());
		}
		else
		{
			enforceableProduct.setRegistrationDefinitionType(productRegistrationDefinition.getRegistrationDefinitionType().PRODUCT_REGISTRATION.toString());
		}
		EnforceableProductDto createdProduct = productService.createRightsuitProduct(enforceableProduct, enforceableProduct.getLicenceDetail().getLicenceDetail());
		String linkedProductToAdd = productBean.getLinkedProductsToAdd() ;
		if (linkedProductToAdd != null && linkedProductToAdd.trim().length() > 0 ) {
			String [] productToAdd = linkedProductToAdd.split(RECORD_SEPARATOR) ;
			for ( String productAdd : productToAdd) {
				String [] linkedProduct = splitIntoFields(productAdd) ; 
				Product prod = new RegisterableProduct();
				prod.setId(linkedProduct[0]);
						//productService.getProductById(linkedProduct[0]);
				productService.addLinkedProductToRightsuit(prod.getId(),createdProduct.getProductId());
			}
		}
		return createdProduct;
	}
	
	private String[] splitIntoFields(final String record) throws UnsupportedEncodingException {
		String[] fields = record.trim().split(FIELD_SEPARATOR);
		for (int i = 0; i < fields.length; i++) {
			fields[i] = URLDecoder.decode(fields[i], "UTF-8");
		}
		return fields;
	}

	@RequestMapping(value = { "/create.htm", "/edit.htm" }, method=RequestMethod.GET, params = {"id", "externalId", "externalSystem", "externalSystemType"})
	public void checkExternalProductIdInUse(
			@RequestParam("id") final String productId,
			@RequestParam("externalId") final String externalId, 
			@RequestParam("externalSystem") final String externalSystem,
			@RequestParam("externalSystemType") final String externalSystemType,
			final HttpServletResponse response) throws IOException {

		response.setContentType("application/json");
		OutputStream out = response.getOutputStream();
		//Product De-duplication
		IOUtils.write("{\"inUse\": \"" + externalIdService.externalProductIdInUse(productId, new ExternalIdDto(externalSystem, externalSystemType, externalId)) + "\"}", out);
		//IOUtils.write("{\"inUse\": \"false\"}", out);
	}

	private void saveProductInternal(final ProductBean productBean, EnforceableProductDto enforceableProduct) throws CircularCountryMatchException, Exception {
		ProductUpdateProcess updateProcess = new ProductUpdateProcess(productService);

		updateProcess.process(productBean);

		ProductRegistrationDefinition productRegistrationDefinition = updateProcess.getUpdatedProductRegistrationDefinition();
		AccountRegistrationDefinition accountRegistrationDefinition = updateProcess.getUpdatedAccountRegistrationDefinition();
		//circularReferenceCheck(productRegistrationDefinition.getRegistrationActivation());
		//Product De-duplication
		/*if (StringUtils.isNotBlank(productBean.getLinkedProductsToRemove())) {
			String[] records = productBean.getLinkedProductsToRemove().split("\\|");
			ArrayList<Integer> removeList=new ArrayList<Integer>();
			for (String record : records) 
			{
				removeList.add(productService.getProductById(record).getErightsId());
			}
			productBean.setLinkedProductsToRemoveErightsId(removeList);
		}*/

		

		Product product = updateProcess.getProduct();
		//start : update data in Atypon
		//Product De-duplication
		//Product Product =  updateProcess.getProduct(); 
		//productService.updateRegisterableProduct(registerableProduct, productRegistrationDefinition); //Anushka
		//productService.updateLinkedProducts(productBean, registerableProduct);
		//end : update data in Atypon
		
		//Product De-duplication
		//registrationActivationService.saveOrUpdateRegistrationActivation(productRegistrationDefinition.getRegistrationActivation());

		if (productBean.isEditMode()) {
			//Product De-duplication
			//productService.updateLinkedProducts(productBean, registerableProduct);
			
			productService.updateProduct(enforceableProduct,productBean);
			//Product De-duplication
			//productService.updateProduct(product);
		} else {
			EnforceableProductDto erightsProduct = createErightsProduct(enforceableProduct, productBean, productRegistrationDefinition, product);
			if(erightsProduct.getProductId() != null){
				product.setId(erightsProduct.getProductId());
				productBean.setProductId(erightsProduct.getProductId());
				//Product De-duplication
				//productService.updateLinkedProducts(productBean, Product);
				//productService.saveProductWithExirestingGuid(product); // Saves the product definition
				
				//productService.updateProduct(product); // Saves child objects
			}
		}
		productRegistrationDefinition.setProductId(product.getId());
		accountRegistrationDefinition.setProductId(product.getId());
		registrationDefinitionService.saveRegistrationDefinition(accountRegistrationDefinition);
		//Product De-duplication
		//licenceTemplateService.saveOrUpdateLicenceTemplate(productRegistrationDefinition.getLicenceTemplate());
		registrationDefinitionService.saveRegistrationDefinition(productRegistrationDefinition);
	}

	private void circularReferenceCheck(final RegistrationActivation registrationActivation) throws CircularCountryMatchException {
		if (registrationActivation instanceof CountryMatchRegistrationActivation) {
			CountryMatchRegistrationActivation countryMatchRegistrationActivation = (CountryMatchRegistrationActivation) registrationActivation;
			countryMatchRegistrationActivation.checkCircularReferences(new UnmatchedCountryMatchRegistrationActivationVisitor());
			countryMatchRegistrationActivation.checkCircularReferences(new MatchedCountryMatchRegistrationActivationVisitor());
		}
	}


	@ModelAttribute("productBean")
	public ProductBean createModel(@RequestParam(value = "id") final String productId) throws ServiceLayerException {
		ProductBean productBean = null;

		Product product = null ;
		/*if (productId != null && !productId.isEmpty()) {
			product = productService.getProductById(productId);
		}*/
		EnforceableProductDto enforceableProduct;
		try {
			AdminUser adminUser = (AdminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (productId != null) {
				product = new RegisterableProduct();
				product.setId(productId);
				//Product De-duplication
				/*
				 * de-duplication
				 * RegisterableProduct registerableProduct = (RegisterableProduct) product;

				enforceableProduct = erightsFacade.getProduct(product.getErightsId());
				ProductRegistrationDefinition productRegistrationDefinition = registrationDefinitionService.getProductRegistrationDefinitionByProduct(registerableProduct);
				List<RegistrationDefinition> regDefs = getRegistrationDefinitionsForProduct(product);
				List<LinkedProduct> linked = getLinkedProducts(product);
				AccountRegistrationDefinition accountRegistrationDefinition = registrationDefinitionService.getAccountRegistrationDefinitionByProduct(registerableProduct);
				productBean = new ProductBean(registerableProduct, accountRegistrationDefinition, productRegistrationDefinition, enforceableProduct, regDefs, linked);
*/			
				enforceableProduct = productService.getEnforceableProductByErightsId(productId);
				List<LinkedProductNew> newLinkedProductList = new ArrayList<LinkedProductNew>() ;
				for (LinkedProductNew linkedProducts: enforceableProduct.getLinkedProducts()) {
					EnforceableProductDto linkedProduct = productService.getEnforceableProductByErightsId(linkedProducts.getProductId()) ;
					linkedProducts.setName(linkedProduct.getName());
					newLinkedProductList.add(linkedProducts) ;
				}
				if (newLinkedProductList.size() > 0 ) {
					enforceableProduct.getLinkedProducts().clear();
					enforceableProduct.setLinkedProducts(newLinkedProductList);
				}
				
				ProductRegistrationDefinition productRegistrationDefinition = registrationDefinitionService.getProductRegistrationDefinitionByProduct(product);
				AccountRegistrationDefinition accountRegistrationDefinition = registrationDefinitionService.getAccountRegistrationDefinitionByProduct(product);
				productBean = new ProductBean(accountRegistrationDefinition, productRegistrationDefinition, enforceableProduct);
			} else {
				// We've been passed a GUID that is unknown to EAC - i.e. we're in create mode
				productBean = new ProductBean();
			}
			
			productBean.setDivisions(divisionService.getDivisionsByAdminUser(adminUser));
		}catch (ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			setModelPrerequisites(productBean);
		} catch (ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return productBean;
	}
	//Product De-duplication
	/*
	 * de-duplication
	 * private List<LinkedProductNew> getLinkedProducts(EnforceableProductDto enforceableProduct) {
		
		 * de-duplication
		 * List<LinkedProduct> result;
		try{
			List<LinkedProduct> linked = this.productService.getProductsLinkedDirectToProduct(product);
			result = linked;
		}catch(ServiceLayerException sle){
			// LOG.warn("problem getting LinkedProducts for asset with id " + product.getId(), sle);
			result = new ArrayList<LinkedProduct>();
		}
		
		if (enforceableProduct.getLinkedProducts().size() > 0) {
			return enforceableProduct.getLinkedProducts() ;
		}
		return null;
	}*/

	private List<RegistrationDefinition> getRegistrationDefinitionsForProduct(final Product product){
		List<RegistrationDefinition> result;
		try{
			List<RegistrationDefinition> prodRegDefs = this.registrationDefinitionService.getRegistrationDefinitionsForProduct(product);
			result = prodRegDefs;
		}catch(ServiceLayerException sle){
			//LOG.warn("problem getting RegistrationDefinitions for asset with id " + asset.getId(), sle);
			result = new ArrayList<RegistrationDefinition>();
		}
		return result;
	}
	private boolean isProductNameInUse(final ProductBean productBean) {
		EnforceableProductDto existingProduct = null;
		try {
			existingProduct = productService.getProductByName(productBean.getProductName());
		} catch (ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return existingProduct != null && !StringUtils.equals(productBean.getProductId(), existingProduct.getProductId());
	}

	private void setModelPrerequisites(final ProductBean productBean) throws ErightsException {
		AdminUser adminUser = (AdminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		productBean.setAccountPageDefinitions(pageDefinitionService.getAvailableAccountPageDefinitions(adminUser));
		productBean.setProductPageDefinitions(pageDefinitionService.getAvailableProductPageDefinitions(adminUser));
		productBean.setRegistrationActivations(registrationActivationService.getAllRegistrationActivationsOrderedByType());
		if ( !productBean.isEditMode() ){
			productBean.setRegistrationActivationId(productBean.getRegistrationActivations().get(1).getId());
		}
		//productBean.setDivisionId(divisionId);
		List<ExternalSystem> externalSystems = externalIdService.getAllExternalSystemsOrderedByName();
		int i = 0 ;
		for (ExternalSystem externalSystem : externalSystems) {
			externalSystem.setId(i++ +"");
			List<ExternalSystemIdType> externalSystemIdTypes = new ArrayList<ExternalSystemIdType>(externalSystem.getExternalSystemIdTypes());
			for(ExternalSystemIdType externalSystemIdType : externalSystemIdTypes){
				externalSystemIdType.setExternalSystem(externalSystem);
				externalSystemIdType.setId(i++  +"");
			}
			
			productBean.addToExternalSystemMap(externalSystem, externalSystemIdTypes);
		}

		List<Platform> platformList = platformService.getAllPlatforms() ;
		for (Platform platform : platformList) {
			platform.setCode(JavaScriptUtils.javaScriptEscape(HtmlUtils.htmlEscape(platform.getCode())));
			platform.setName(JavaScriptUtils.javaScriptEscape(HtmlUtils.htmlEscape(platform.getName()))); 
			productBean.addToPlatformMap(platform.getPlatformId(), platform);
		}
	}

	@InitBinder()
	public void initBinder(final WebDataBinder binder) {
		binder.setValidator(validator);
	}

	@RequestMapping(value = { "/delete.htm" }, method = RequestMethod.GET)
	public ModelAndView deleteUnusedProduct(@RequestParam(value = "id", required=true) final String productId) {
		String baseUrl = "redirect:/mvc/product/search.htm?";
		String params = null;
		Product product = new RegisterableProduct();
		product.setId(productId);
		boolean deleted=false;
		try {
			deleted = productService.deleteUnUsedProduct(productId);
			ProductRegistrationDefinition productRegistrationDefinition = registrationDefinitionService.getProductRegistrationDefinitionByProduct(product);
			AccountRegistrationDefinition accountRegistrationDefinition= registrationDefinitionService.getAccountRegistrationDefinitionByProduct(product);
			registrationDefinitionService.deleteRegistrationDefinition(productRegistrationDefinition);
			registrationDefinitionService.deleteRegistrationDefinition(accountRegistrationDefinition);
		}catch (ServiceLayerException e) {
			params = "errorMessageKey=" + PRODUCT_DELETE_FAILURE;
		} catch (ProductNotFoundException e) {
			params = "errorMessageKey=" + PRODUCT_DELETE_FAILURE;
		} catch (ChildProductFoundException e) {
			params = "errorMessageKey=" + PRODUCT_USED_DELETE_FAILURE;
		} catch (ErightsException e) {
			if (e.getErrorCode() != null && (e.getErrorCode() == 3033 || e.getErrorCode() == 3114 || e.getErrorCode() == 2030)) {
				params = "errorMessageKey=" + PRODUCT_USED_DELETE_FAILURE;
			} else {
				params = "errorMessageKey=" + PRODUCT_DELETE_FAILURE;
			}
		}      
		if(deleted){
			params = "statusMessageKey=" + PRODUCT_DELETE_SUCCESS;
		}
		ModelAndView result = new ModelAndView(baseUrl + params);
		return result;
	}    

}
