package com.oup.eac.admin.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
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
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.HtmlUtils;

import com.oup.eac.admin.beans.ActivationCodeBatchBean;
import com.oup.eac.admin.validators.ActivationCodeBatchBeanValidator;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.ActivationCodeBatch.ActivationCodeFormat;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.ConcurrentLicenceTemplate;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.InstantRegistrationActivation;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.LicenceTemplate.LicenceType;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.Product.ProductState;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegistrationActivation;
import com.oup.eac.domain.RegistrationActivation.ActivationStrategy;
import com.oup.eac.domain.RegistrationDefinition;
import com.oup.eac.domain.RegistrationDefinition.RegistrationDefinitionType;
import com.oup.eac.domain.RollingLicenceTemplate;
import com.oup.eac.domain.RollingLicenceTemplate.RollingBeginOn;
import com.oup.eac.domain.RollingLicenceTemplate.RollingUnitType;
import com.oup.eac.domain.SelfRegistrationActivation;
import com.oup.eac.domain.UsageLicenceTemplate;
import com.oup.eac.domain.ValidatedRegistrationActivation;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.ProductGroupDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.GroupNotFoundException;
import com.oup.eac.integration.facade.exceptions.LicenseNotFoundException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.service.ActivationCodeService;
import com.oup.eac.service.AsyncReportService;
import com.oup.eac.service.DivisionService;
import com.oup.eac.service.EacGroupService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationActivationService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.ServiceLayerException;

/**
 * Spring MVC Controller for creating and editing Activation Code Batches.  
 * @author David Hay 
 */
@Controller
@RequestMapping(value = {"/mvc/batch" })
public class CreateOrEditActivationCodeBatchController {

    private static final Logger LOG = Logger.getLogger(CreateOrEditActivationCodeBatchController.class);
    private static final String EDIT_PAGE = "createOrEditActivationCodeBatch";
    private static final String PRODUCT_SUMMARY = "activationCodeRegistrationDefinitionTile";
    private static final String BATCH_SUMMARY = "generatedActivationCodeBatchSummary";
    private static final String STATUS_MSG_KEY ="statusMessageKey";
    private static final String ERROR_MSG_KEY  ="errorMessageKey";
    private static final String EAC_GROUP_SUMMARY = "eacGroupActivationCodeRegistrationDefinitionTile";
    
    private static final String MSG_CREATE_SUCCESS = "status.batch.create.success";
    private static final String MSG_UPDATE_SUCCESS = "status.batch.update.success";
    private static final String MSG_DELETE_SUCCESS = "status.batch.delete.success";
    private static final String MSG_DELETE_FAILED  = "status.batch.delete.failed";    
    private static final String EDIT_NEXT_PAGE = String.format("/mvc/activationCodeBatch/search.htm?%s=%s",STATUS_MSG_KEY, MSG_UPDATE_SUCCESS);
    private static final String DELETE_BATCH_NEXT_PAGE = String.format("/mvc/activationCodeBatch/search.htm?%s=%s",STATUS_MSG_KEY, MSG_DELETE_SUCCESS);
    private static final String DELETE_BATCH_FAILED_NEXT_PAGE = String.format("/mvc/activationCodeBatch/search.htm?%s=%s",ERROR_MSG_KEY, MSG_DELETE_FAILED);
    private static final String SEARCH_PAGE = "/token/search.htm";
    private static final String MSG_CREATE_FAILED_INVALID_PRODUCT_STATE = "status.batch.create.failed.invalid.product.state";
    private static final List<ProductState> VALID_PRODUCT_STATES_FOR_CREATE_BATCH = Arrays.asList(ProductState.ACTIVE, ProductState.SUSPENDED); 

    private final AsyncReportService asyncReportService;
    private final ActivationCodeService activationCodeService;
    private final RegistrationDefinitionService registrationDefinitionService;
    private final ActivationCodeBatchBeanValidator validator;    
    private final ProductService productService;
    private final EacGroupService eacGroupService;
    private final RegistrationActivationService registrationActivationService;  
    private final DivisionService divisionService;
    
    @Autowired
    public CreateOrEditActivationCodeBatchController(
            ActivationCodeService activationCodeService, 
            ActivationCodeBatchBeanValidator validator,
            ProductService productService,
            RegistrationDefinitionService registrationDefinitionService,
            EacGroupService eacGroupService,
            RegistrationActivationService registrationActivationService, 
            DivisionService divisionService,
            AsyncReportService asyncReportService){
        this.activationCodeService = activationCodeService;
        this.validator = validator;
        this.productService = productService;
        this.registrationDefinitionService = registrationDefinitionService;
        this.eacGroupService =  eacGroupService;
        this.registrationActivationService = registrationActivationService;
        this.divisionService = divisionService;
        this.asyncReportService = asyncReportService ;
    }
    
    //used in create batch only
    @ModelAttribute("activationCodeFormats")
    public List<ActivationCodeFormat> getActivationCodeFormats() {
        return Arrays.asList(ActivationCodeFormat.EAC_NUMERIC);
    }
    
    @ModelAttribute("licenceTypes")
    public List<LicenceType> getLicenceTypes() {
        return ActivationCodeBatchBean.getAllowedLicenceTypes();
    }
    
    @ModelAttribute("rollingBeginOns")
    public RollingBeginOn[] getBeginOnValues() {
        return RollingBeginOn.values();
    }
    
    @ModelAttribute("rollingUnitTypes")
    public List<RollingUnitType> getRollingUnitTypes() {
        return ActivationCodeBatchBean.getAllowedRollingUnitTypes();
    }


    @ModelAttribute("activationCodeBatchBean")
    public ActivationCodeBatchBean createModel(@RequestParam(value = "id", required = false) final String batchDbId,
            @RequestParam(value = "productId", required = false) final String productId,
            @RequestParam(value = "eacGroupId", required = false) final String eacGroupId
            ) throws ErightsException, com.amazonaws.services.cloudfront.model.AccessDeniedException, AccessDeniedException, ServiceLayerException {
        ActivationCodeBatch batch;
        String actualBatchId = null;
        if(StringUtils.isNotEmpty(batchDbId)){
        	
        	actualBatchId = HtmlUtils.htmlUnescape(batchDbId) ;
            batch = activationCodeService.getActivationCodeBatchByBatchId(actualBatchId,false);
        }else{
            batch= new ActivationCodeBatch();
        }
        ActivationCodeBatchBean result = new ActivationCodeBatchBean(batch);
        if (batch.getActivationCodeRegistrationDefinition() != null) {
        	if(batch.getActivationCodeRegistrationDefinition().getEacGroup() != null){
            	populateEacGroup(result, batch.getActivationCodeRegistrationDefinition().getEacGroup().getId());
            }else{
                populateProduct(result, batch.getActivationCodeRegistrationDefinition().getProduct().getId());
            }
        } 
        if(eacGroupId != null ){
        	populateEacGroup(result, eacGroupId);
        } else {
        	populateProduct(result, productId);
        }
        result.setAddedInPool(batch.getAddedInPool());
        return result;
    }
    
    
    private void populateProduct(ActivationCodeBatchBean activationCodeBatchBean, String productId){
        if(StringUtils.isNotBlank(productId)){
        	ActivationCodeRegistrationDefinition acrd;
            try{       
            	Product eacProduct = new RegisterableProduct(); 
            			//productService.getProductById(productId);
                EnforceableProductDto product = loadProductFromProductId(productId) ;
                eacProduct.setId(productId);
                Set extIds = new HashSet(product.getExternalIds()); 
                eacProduct.setExternalIds(extIds);
                //Division division = new Division();
                List<Division> divisions = new ArrayList<Division>();
                divisions = divisionService.getAllDivisions();
                for (Division division : divisions) {
                	if (division.getErightsId() == product.getDivisionId()) {
                		//eacProduct.setDivision(division);
                		product.setDivision(division);
                	}
                }
                if(product != null && product.getRegistrationDefinitionType().equalsIgnoreCase(RegistrationDefinitionType.ACTIVATION_CODE_REGISTRATION.toString())){
                    if (product.getActivationStrategy().equals(ActivationStrategy.INSTANT.toString())){
                    	product.setActivationStrategy(new InstantRegistrationActivation().getName());
                    } else if (product.getActivationStrategy().equals(ActivationStrategy.SELF.toString())){
                    	product.setActivationStrategy(new SelfRegistrationActivation().getName());
                    } else if (product.getActivationStrategy().equals(ActivationStrategy.VALIDATED.toString())){
                    	product.setActivationStrategy(new ValidatedRegistrationActivation().getName());
                    }
                    activationCodeBatchBean.setProduct(product);
                    //activationCodeBatchBean.setProductId(productId);
                }
                acrd = new ActivationCodeRegistrationDefinition();
				acrd.setProduct(eacProduct);
				List<RegistrationActivation> raList=registrationActivationService.getAllRegistrationActivationsOrderedByType();
	            for (RegistrationActivation registrationActivation : raList) {
	            	if (registrationActivation instanceof InstantRegistrationActivation){
	            		acrd.setRegistrationActivation(registrationActivation);
	            		break;
	            	}
	            }
	            activationCodeBatchBean.setActivationCodeRegistrationDefinition(acrd);
            }catch(ServiceLayerException | ErightsException sle){
                LOG.warn(sle);
            }
        }
    }
    
    private void populateEacGroup(ActivationCodeBatchBean activationCodeBatchBean, String eacGroupId) 
    		throws ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, AccessDeniedException, 
    		GroupNotFoundException, ErightsException{
        if(StringUtils.isNotBlank(eacGroupId)){
        	ActivationCodeRegistrationDefinition acrd;
			try {
				EacGroups eacGroup = new EacGroups();
				ProductGroupDto productGroupDto = new ProductGroupDto();
				productGroupDto = eacGroupService.getProductGroupDtoByErightsId(eacGroupId);
				eacGroup = eacGroupService.getEacGroupByProductGroupDto(productGroupDto);
				Boolean usedEacGroup = eacGroupService.isEacGroupUsed(eacGroupId);
				/*if(usedEacGroup){
					acrd = registrationDefinitionService.getActivationCodeRegistrationDefinitionForEacGroup(activationCodeBatchBean.getCurrentBatchId());
				}else{*/
					acrd = new ActivationCodeRegistrationDefinition();
					acrd.setEacGroup(eacGroup);
					List<RegistrationActivation> raList=registrationActivationService.getAllRegistrationActivationsOrderedByType();
		            for (RegistrationActivation registrationActivation : raList) {
		            	if (registrationActivation instanceof InstantRegistrationActivation){
		            		acrd.setRegistrationActivation(registrationActivation);
		            		break;
		            	}
		           // }
				}
				activationCodeBatchBean.setEacGroupId(eacGroupId);
	        	activationCodeBatchBean.setActivationCodeRegistrationDefinition(acrd);
			} catch (ServiceLayerException sle) {
				 LOG.warn(sle);
			}
        	
        }
    }
    
    @InitBinder
    public void initBinder(final WebDataBinder webDataBinder) {
        webDataBinder.setValidator(validator);
    }
    
    @RequestMapping(value = "/create.htm",  method = RequestMethod.GET)
    public ModelAndView showFormCreate(@ModelAttribute("activationCodeBatchBean") ActivationCodeBatchBean activationCodeBatchBean) {
        if (activationCodeBatchBean.isEditMode()) {
            ModelAndView result = new ModelAndView(SEARCH_PAGE);
            return result;
        }
        ModelAndView result = new ModelAndView(EDIT_PAGE);
        return result;
    }

    @RequestMapping(value = "/checkOverlap.htm", method = RequestMethod.POST)
    public void checkOverlap(@ModelAttribute("activationCodeBatchBean") ActivationCodeBatchBean activationCodeBatchBean, 
    		HttpServletRequest request, HttpServletResponse response, BindingResult errors) throws ServletException, IOException {        
         
        response.setContentType("application/json");
        OutputStream out = response.getOutputStream();
        
        boolean overlapProblem =false;
        if(errors.hasErrors()){
            overlapProblem = false;
        } else {
            LocalDate licStart = activationCodeBatchBean.getLicenceStartDate();
            LocalDate licEnd = activationCodeBatchBean.getLicenceEndDate();
            DateTime batchStartDateTime = activationCodeBatchBean.getActivationCodeBatch().getStartDate();
            DateTime batchEndDateTime = activationCodeBatchBean.getActivationCodeBatch().getEndDate();
            LocalDate batchStart = batchStartDateTime == null ? null : batchStartDateTime.toLocalDate();
            LocalDate batchEnd = batchEndDateTime   == null ? null : batchEndDateTime.toLocalDate();            
            overlapProblem = ActivationCodeBatchBeanValidator.inOrderButNoOverlap(licStart, licEnd, batchStart, batchEnd);
        }
        
        String json = String.format("{\"overlapProblem\": %s}", overlapProblem);
        overlapProblem = !overlapProblem;
        IOUtils.write(json, out);
    }


    @RequestMapping(value = "/edit.htm", method = RequestMethod.GET)
    public ModelAndView showFormEdit(@ModelAttribute("activationCodeBatchBean") ActivationCodeBatchBean activationCodeBatchBean) { 
        if (!activationCodeBatchBean.isEditMode()) {
            ModelAndView result = new ModelAndView(SEARCH_PAGE);
            return result;
        }
        ModelAndView result = new ModelAndView(EDIT_PAGE);
        return result;
    }
    
    @RequestMapping(value="/selectProduct.htm")
    public ModelAndView selectProduct (@ModelAttribute("activationCodeBatchBean")  ActivationCodeBatchBean activationCodeBatchBean){
        ModelAndView result = new ModelAndView(PRODUCT_SUMMARY);
        return result;
    }
    
    @RequestMapping(value="/selectEacGroup.htm")
    public ModelAndView selectEacGroup (@ModelAttribute("activationCodeBatchBean")  ActivationCodeBatchBean activationCodeBatchBean){
        ModelAndView result = new ModelAndView(EAC_GROUP_SUMMARY);
        return result;
    }
    
    @RequestMapping(value= "/create.htm" , method = RequestMethod.POST)
	public ModelAndView crateBatch(
			@Valid @ModelAttribute("activationCodeBatchBean") final ActivationCodeBatchBean activationCodeBatchBean,
			final BindingResult bindingResult) throws ServiceLayerException,
			DatatypeConfigurationException {

		if (bindingResult.hasErrors()) {
			return new ModelAndView(EDIT_PAGE);
		}

		boolean isEditMode = activationCodeBatchBean.isEditMode();
		if (isEditMode) {
			return new ModelAndView(EDIT_PAGE);
		}

		ActivationCodeBatch activationCodeBatch = activationCodeBatchBean
				.getActivationCodeBatch();
		LicenceTemplate lt = activationCodeBatchBean.getLicenceTemplate();
		activationCodeBatch.setLicenceTemplate(lt);

		ActivationCodeRegistrationDefinition regDef = new ActivationCodeRegistrationDefinition();
		if (null != activationCodeBatchBean
				.getActivationCodeRegistrationDefinition()) {
			regDef = activationCodeBatchBean
					.getActivationCodeRegistrationDefinition();
		}
		regDef.setLicenceTemplate(lt);

		if (null != activationCodeBatchBean.getEacGroupId()) {
			ProductGroupDto productGroupDto = null;
			try {
				productGroupDto = eacGroupService
						.getProductGroupDtoByErightsId(activationCodeBatchBean
								.getEacGroupId());
			} catch (NumberFormatException | ErightsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			EacGroups eacGroup = eacGroupService
					.getEacGroupByProductGroupDto(productGroupDto);
			regDef.setEacGroup(eacGroup);
		}

		/*
		 * if(null != regDef.getEacGroup()){
		 * ActivationCodeRegistrationDefinition
		 * activationCodeRegistrationDefinition = null ; try{
		 * activationCodeRegistrationDefinition = registrationDefinitionService.
		 * getActivationCodeRegistrationDefinitionForEacGroup
		 * (activationCodeBatchBean.getCurrentBatchId()); } catch(Exception e){
		 * } if(activationCodeRegistrationDefinition != null) { regDef =
		 * activationCodeRegistrationDefinition ; } else {
		 * regDef.setLicenceTemplate(lt); } }
		 */

		EnforceableProductDto product = activationCodeBatchBean.getProduct();

		if (product != null) {
			ProductState productState = null;
			if (product.getState().equals(ProductState.ACTIVE.toString())) {
				productState = ProductState.ACTIVE;
			} else if (product.getState().equals(
					ProductState.SUSPENDED.toString())) {
				productState = ProductState.SUSPENDED;
			}

			if (VALID_PRODUCT_STATES_FOR_CREATE_BATCH.contains(productState) == false) {
				bindingResult.reject(MSG_CREATE_FAILED_INVALID_PRODUCT_STATE);
				String msg = String
						.format("Attempt made to create activation code batch for product (id=%s) (name=%s) when state is (%s)",
								product.getProductId(), product.getName(),
								productState);
				LOG.warn(msg);
				return new ModelAndView(EDIT_PAGE);
			}

			Product eacProduct = new RegisterableProduct();
			eacProduct.setId(product.getProductId());
					//productService.getProductById(product.getProductId());
			regDef.setProduct(eacProduct);
		}

		DateTime createdDate = new DateTime();
		activationCodeBatch.setCreatedDate(createdDate);
		ActivationCodeBatch batchIdUsed = null;
		try {
			batchIdUsed = activationCodeService
					.getActivationCodeBatchByBatchId(activationCodeBatch
							.getBatchId(),false);
		} catch (AccessDeniedException | ErightsException e) {
			// messages.add(new EacError("error.batch.duplicateBatchId",
			// "Batch name is already in use"));
		}
		if (batchIdUsed != null) {
			bindingResult.reject("error.batch.duplicateBatchId");
			return new ModelAndView(EDIT_PAGE);
		}
		String productGroupId = null ;
		if (regDef.getEacGroup() != null && regDef.getEacGroup().getId() != null ){
			productGroupId = regDef.getEacGroup().getId() ;
		}
		AdminUser adminUser = (AdminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		asyncReportService.createActivationCodeBatch(activationCodeBatch,
				regDef, activationCodeBatchBean.getNumberOfTokensAsInt(),
				activationCodeBatchBean.getTokenAllowedUsagesAsInt(), productGroupId, adminUser.getEmailAddress(),adminUser.getUsername());
		/*
		 * try {
		 * activationCodeService.saveActivationCodeBatch(activationCodeBatch,
		 * regDef, activationCodeBatchBean.getNumberOfTokensAsInt(),
		 * activationCodeBatchBean.getTokenAllowedUsagesAsInt()); } catch
		 * (AccessDeniedException | ErightsException e) { //messages.add(new
		 * EacError("error.batch.duplicateBatchId",
		 * "Batch name is already in use"));
		 * bindingResult.reject("error.batch.duplicateBatchId"); return new
		 * ModelAndView(EDIT_PAGE); }
		 */

		ModelAndView result = new ModelAndView(EDIT_PAGE);
		result.getModelMap().addAttribute(STATUS_MSG_KEY, MSG_CREATE_SUCCESS);
		result.getModelMap().addAttribute("adminEmail", adminUser.getEmailAddress());
		return result;
	}

    @RequestMapping(value="/delete.htm", method = RequestMethod.GET)
    public ModelAndView deleteBatch(@RequestParam(value = "id", required = true) final String batchDbId)
            throws ServiceLayerException, ProductNotFoundException, UserNotFoundException, 
            LicenseNotFoundException, AccessDeniedException, GroupNotFoundException, ErightsException {

        View view;
        String batchId = HtmlUtils.htmlUnescape(batchDbId) ;
        boolean isUsed = activationCodeService.hasActivationCodeBatchBeenUsed(batchId);
        if (isUsed) {
            view = new RedirectView(DELETE_BATCH_FAILED_NEXT_PAGE, true, true, false);
        } else {
            // delete batch
            try {
            	
                this.activationCodeService.deleteUnusedActivationCodeBatch(batchId);
                view = new RedirectView(DELETE_BATCH_NEXT_PAGE, true, true, false);
            } catch (ServiceLayerException sle) {
                view = new RedirectView(DELETE_BATCH_FAILED_NEXT_PAGE, true, true, false);
            } catch (RuntimeException rte) {
                LOG.error("unexpected error deleting activation code batch " + batchId, rte);
                view = new RedirectView(DELETE_BATCH_FAILED_NEXT_PAGE, true, true, false);
            }
        }
        ModelAndView result = new ModelAndView(view);
        return result;
    }

    @RequestMapping(value="/edit.htm", method = RequestMethod.POST)
    public ModelAndView editBatch (
                @Valid @ModelAttribute("activationCodeBatchBean") ActivationCodeBatchBean activationCodeBatchBean, 
                final BindingResult bindingResult) throws ServiceLayerException, ProductNotFoundException, 
                UserNotFoundException, LicenseNotFoundException, AccessDeniedException, GroupNotFoundException, 
                ErightsException {
        
        if (bindingResult.hasErrors()) {
            return new ModelAndView(EDIT_PAGE);
        }

        boolean isEditMode = activationCodeBatchBean.isEditMode();
        if(!isEditMode) {
            return new ModelAndView(EDIT_PAGE);
        }
        
        ActivationCodeBatch activationCodeBatch = activationCodeBatchBean.getActivationCodeBatch();
        LicenceTemplate lt = activationCodeBatch.getLicenceTemplate();
        
        ActivationCodeRegistrationDefinition regDef = activationCodeBatchBean.getActivationCodeRegistrationDefinition();
        if(regDef.getEacGroup()!= null){
        	ActivationCodeBatch codeBatch = activationCodeService.getActivationCodeBatchByBatchId(
        			activationCodeBatchBean.getCurrentBatchId(),false);
        	
        	ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition = 
        			codeBatch.getActivationCodeRegistrationDefinition();       	
        	
        	
        	
        	if (activationCodeBatchBean.getEacGroupId() != null) {
        		ProductGroupDto productGroupDto = eacGroupService.getProductGroupDtoByErightsId(
        				activationCodeBatchBean.getEacGroupId());
        		EacGroups eacGroup = eacGroupService.getEacGroupByProductGroupDto(productGroupDto);
        		activationCodeRegistrationDefinition.setProduct(null);
        		activationCodeRegistrationDefinition.setEacGroup(eacGroup); 
        	} 
        	if (activationCodeBatchBean.getProductId() != null) {
        		EnforceableProductDto enforceableProductDto = productService.getEnforceableProductByErightsId(
        				activationCodeBatchBean.getProductId()); 
        		Product product = new RegisterableProduct();
        		product.setProductName(enforceableProductDto.getName());        		
        		activationCodeRegistrationDefinition.setProduct(product);
        		activationCodeRegistrationDefinition.setEacGroup(null);
        	}
        	//de-duplication
        	/*registrationDefinitionService.getActivationCodeRegistrationDefinitionForEacGroup(
        					activationCodeBatch.getBatchId());*/
        	
        	if(activationCodeRegistrationDefinition != null)
        	{
        		regDef = activationCodeRegistrationDefinition ;
        	} else {
        		regDef.setLicenceTemplate(lt);
        	}
        }
        activationCodeBatchBean.setActivationCodeRegistrationDefinition(regDef);
        updateLicenceTemplate(lt, activationCodeBatchBean);
        activationCodeBatch.setActivationCodeRegistrationDefinition(activationCodeBatchBean.getActivationCodeRegistrationDefinition());
            
        try {
        	activationCodeService.updateActivationCodeBatch(activationCodeBatch,activationCodeBatchBean.getCurrentBatchId());
        } catch (AccessDeniedException | ErightsException e) {
     	   //messages.add(new EacError("error.batch.duplicateBatchId", "Batch name is already in use"));
        //activationCodeBatchBean.setCurrentBatchId(currentBatchId);
     	   bindingResult.reject("error.batch.duplicateBatchId");
     	  activationCodeBatch.setBatchId(activationCodeBatchBean.getCurrentBatchId());
     	  activationCodeBatchBean = new ActivationCodeBatchBean(activationCodeBatch);
     	   return new ModelAndView(EDIT_PAGE);
        }
        
            
        RedirectView view = new RedirectView(EDIT_NEXT_PAGE, true, true, false);
        ModelAndView result = new ModelAndView(view);
        return result;
    }
    
    private void updateLicenceTemplate(LicenceTemplate lt, ActivationCodeBatchBean activationCodeBatchBean) {
        lt.setStartDate(activationCodeBatchBean.getLicenceStartDate());
        lt.setEndDate(activationCodeBatchBean.getLicenceEndDate());
        switch (lt.getLicenceType()) {
        case ROLLING:
            RollingLicenceTemplate rolling = (RollingLicenceTemplate) lt;
            rolling.setBeginOn(activationCodeBatchBean.getRollingBeginOn());
            rolling.setTimePeriod(activationCodeBatchBean.getTimePeriodAsInt());
            rolling.setUnitType(activationCodeBatchBean.getRollingUnitType());
            break;
        case CONCURRENT:
            ConcurrentLicenceTemplate con = (ConcurrentLicenceTemplate) lt;
            con.setTotalConcurrency(activationCodeBatchBean.getTotalConcurrencyAsInt());
            con.setUserConcurrency(activationCodeBatchBean.getUserConcurrencyAsInt());
            break;
        case USAGE:
            UsageLicenceTemplate usage = (UsageLicenceTemplate) lt;
            usage.setAllowedUsages(activationCodeBatchBean.getLicenceAllowedUsagesAsInt());
            break;
        }
    }
    
    public EnforceableProductDto loadProductFromProductId(final String productId) throws ProductNotFoundException, ErightsException, ServiceLayerException {
    	EnforceableProductDto enforceableProductDto = null ;
    	if (StringUtils.isNotBlank(productId)) {
            //Product product = productService.getProductById(productId);
            enforceableProductDto = productService.getEnforceableProductByErightsId(productId) ;
        }
    	return enforceableProductDto ;
    }
    public RegistrationDefinition loadRegistrationDefinitionFromProductId(final String productId) throws ServiceLayerException {
        RegistrationDefinition registrationDefinition = null;
        if (StringUtils.isNotBlank(productId)) {
           // Product product = productService.getProductById(productId);
        	Product product = new RegisterableProduct();
        	product.setId(productId);
            registrationDefinition = registrationDefinitionService.getProductRegistrationDefinitionByProduct((RegisterableProduct) product);
        }
        return registrationDefinition;
    }

}