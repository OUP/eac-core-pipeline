package com.oup.eac.admin.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.admin.beans.EacGroupsBean;
import com.oup.eac.admin.validators.EacGroupBeanValidator;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.Product.ProductState;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegistrationDefinition.RegistrationDefinitionType;
import com.oup.eac.domain.beans.ProductBean;
import com.oup.eac.domain.paging.Paging;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.ProductGroupDto;
import com.oup.eac.dto.RegistrationDefinitionSearchCriteria;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.service.DivisionService;
import com.oup.eac.service.EacGroupService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.ServiceLayerException;

@Controller
@RequestMapping("/mvc/eacGroups")
public class CreateOrEditEacGroupController {
	private final String CREATE_EDIT_EAC_GROUP_VIEW= "createOrEditEacGroup";
	private static final String EAC_GROUP_DELETE_SUCCESS = "status.eacGroup.delete.success";
    private static final String EAC_GROUP_CREATE_SUCCESS = "status.eacGroup.create.success";
    private static final String EAC_GROUP_UPDATE_SUCCESS = "status.eacGroup.update.success";
    private static final String EAC_GROUP_DELETE_FAILURE = "status.eacGroup.delete.failure";
	
	private final EacGroupService eacGroupService;
	private final ProductService productService;
	private final DivisionService divisionService;
	private final RegistrationDefinitionService registrationDefinitionService;
	private final EacGroupBeanValidator validator;
	private String currentEacGroupName;
	
	
	@Autowired
	public CreateOrEditEacGroupController(
			final EacGroupService eacGroupService, 
			final ProductService productService,
			final RegistrationDefinitionService registrationDefinitionService,
			final EacGroupBeanValidator validator, final ErightsFacade erightsFacade,
			final DivisionService divisionService){
		this.eacGroupService=eacGroupService;
		this.productService=productService;
		this.registrationDefinitionService=registrationDefinitionService;
		this.validator=validator;
		this.divisionService = divisionService ;
	}
	
	@RequestMapping(value = { "/create.htm", "/edit.htm" }, method=RequestMethod.GET)
	public ModelAndView showForm() {
		return new ModelAndView(CREATE_EDIT_EAC_GROUP_VIEW);
	}
	
	@InitBinder()
	public void initBinder(final WebDataBinder binder) {
		binder.setValidator(validator);
	}
	
	@RequestMapping(value = { "/searchProductsForGrouping.htm" }, method=RequestMethod.GET, params = {"id", "searchTerm", "searchBy"})
	public @ResponseBody Map<String, String> searchProduct(
			@RequestParam("id") final String groupId,
			@RequestParam("searchTerm") final String searchTerm,
			@RequestParam("searchBy") final String searchBy) throws IOException,ServiceLayerException {

		Map<String , String> productSearchResult = new HashMap<String, String>();
		RegistrationDefinitionSearchCriteria searchCriteria = new RegistrationDefinitionSearchCriteria();
		setSearchTerm(searchCriteria, searchTerm, searchBy);
		setType(searchCriteria);
		List<Division> divisions = new ArrayList<Division>();
		try {
			divisions = divisionService.getAllDivisions();
		} catch (AccessDeniedException | ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String,String> divisionList = new HashMap<String, String>() ;
		for (Division division : divisions) {
			divisionList.put(division.getErightsId().toString(), division.getDivisionType()) ;
		}
		// Products which has Active product state will be in search result
		setProductState(searchCriteria);
		Paging<ProductBean> resultsPage = productService.searchProduct(searchCriteria,
				PagingCriteria.valueOf(100, 1, null, null),divisionList);
		List<ProductBean> products=resultsPage.getItems();
		for (ProductBean product : products) {
			//ProductPageDefinition pd = ((ProductRegistrationDefinition)registrationDefinition).getPageDefinition();
			//if(pd==null){
			productSearchResult.put(product.getProductId(), product.getProductName());
			//}
		}
		return productSearchResult;
	}
	
	//create eacGroup bean
	@ModelAttribute("eacGroupsBean")
	public EacGroupsBean createModel(@RequestParam(value = "id") final String groupId) throws ServiceLayerException{
		EacGroupsBean eacGroupsBean=null;
		ProductGroupDto productGroupDto = new ProductGroupDto();
		AdminUser adminUser = (AdminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EacGroups eacGroup = null;
		if (groupId != null) {
			try {
				productGroupDto = eacGroupService.getProductGroupDtoByErightsId(groupId);
				eacGroup = eacGroupService.getEacGroupByProductGroupDto(productGroupDto); 
			} catch (NumberFormatException | ErightsException e) {
				//Create EACGroup
			}
		}
		
		if(eacGroup !=null){
			eacGroupsBean=new EacGroupsBean(eacGroup);
			eacGroupsBean.setUpdatedBy(adminUser);
			currentEacGroupName = eacGroup.getGroupName();
		}else{
			eacGroupsBean=new EacGroupsBean();
			eacGroupsBean.setCreatedBy(adminUser);
		}
		
		return eacGroupsBean;
	}
	
	@RequestMapping(value = { "/create.htm", "/edit.htm" }, method=RequestMethod.POST)
	public ModelAndView saveEacGroup(@Valid @ModelAttribute("eacGroupsBean") final EacGroupsBean eacGroupsBean, final BindingResult bindingResult) throws Exception {
	
		if (bindingResult.hasErrors()) {
			return new ModelAndView(CREATE_EDIT_EAC_GROUP_VIEW);
		}
		EacGroups eacGroup = getUpdatedEacGroup(eacGroupsBean);
		
		if (isEacGroupNameInUse(eacGroup)) {
        	bindingResult.reject("error.eacGroupNameInUse");
        	return new ModelAndView(CREATE_EDIT_EAC_GROUP_VIEW);
        }
		
		if(eacGroupsBean.isEditMode()){
			/*if(eacGroup.isEditable()){*/
				eacGroupService.updateEacGroup(eacGroup, eacGroupsBean.getProductIdsToAdd(), eacGroupsBean.getProductIdsToRemove(), currentEacGroupName);
			/*}else{
				bindingResult.reject("error.eacGroupNameIsNotEditable");
	        	return new ModelAndView(CREATE_EDIT_EAC_GROUP_VIEW);
			}*/
		}else{
			/*eacGroup.setEditable(eacGroupsBean.isEditable());*/
			
			eacGroupService.createEacGroup(eacGroup);
		}
		
		String messageKey = null;
		if (eacGroupsBean.isEditMode()) {
			messageKey = EAC_GROUP_UPDATE_SUCCESS;
		} else {
			messageKey = EAC_GROUP_CREATE_SUCCESS;
		}
		
		return new ModelAndView("redirect:/mvc/eacGroups/search.htm?statusMessageKey=" + messageKey);
		
	}
	

	@RequestMapping(value = { "/updateIsEditable.htm" }, method = RequestMethod.POST)
    public ModelAndView updateIsEditable(@Valid @ModelAttribute("eacGroupsBean") final EacGroupsBean eacGroupsBean, final BindingResult bindingResult) throws Exception {
		if(eacGroupsBean.isEditMode()){
			EacGroups eacGroup = eacGroupService.getEacGroupById(eacGroupsBean.getGroupId());
			/*eacGroup.setEditable(eacGroupsBean.isEditable());*/
			/*eacGroupService.updateIsEditable(eacGroup);*/
		}else{
			bindingResult.reject("error.eacGroup.isEditableInEditMode");
        	return new ModelAndView(CREATE_EDIT_EAC_GROUP_VIEW);
		}
       return new ModelAndView("redirect:/mvc/eacGroups/edit.htm?id="+eacGroupsBean.getGroupId());
    }
	
	private EacGroups getUpdatedEacGroup(final EacGroupsBean eacGroupsBean) throws ServiceLayerException{
		EacGroups eacGroup;
		
		
		if(eacGroupsBean.isEditMode()){
			ProductGroupDto productGroupDto = new ProductGroupDto();
			try {
				//Get product group dto object by OUP id
				productGroupDto = eacGroupService.getProductGroupDtoByErightsId(eacGroupsBean.getGroupId());
			} catch (NumberFormatException | ErightsException e) {
				e.printStackTrace();
			}
			eacGroup = eacGroupService.getEacGroupByProductGroupDto(productGroupDto);
			eacGroup.setUpdatedBy(eacGroupsBean.getUpdatedBy());
			eacGroup.setGroupName(eacGroupsBean.getGroupName());
		}else{
			eacGroup=new EacGroups();
			eacGroup.setId(eacGroupsBean.getGroupId());
			eacGroup.setCreatedBy(eacGroupsBean.getCreatedBy());
			Set<Product> productSet = new HashSet<Product>();
			//Not used now. -Abhi
			//Set<EnforceableProductDto> enforceableProducts = new HashSet<EnforceableProductDto>();
			//EnforceableProductDto enforceableProductDto = new EnforceableProductDto();
			for (String productId : eacGroupsBean.getProductIdsToAdd()) {
				/* Not Not used now. -Abhi
				 * try {
					enforceableProductDto = productService.getEnforceableProductByErightsId(productId);
					enforceableProducts.add(enforceableProductDto);					
				} catch (NumberFormatException | ErightsException e) {
					e.printStackTrace();
				}	*/			
				Product product = new RegisterableProduct();
				product.setId(productId);
				productSet.add(product);
			}
			//eacGroup.setEnforceableProductDto(enforceableProducts);
			eacGroup.setProducts(productSet);
		}
		eacGroup.setGroupName(eacGroupsBean.getGroupName());
		return eacGroup;
	}

		
	@RequestMapping(value = { "/delete.htm" }, method = RequestMethod.GET)
    public ModelAndView deleteUnusedEacGroup(@RequestParam(value = "id", required=true) final String eacGroupId) throws ServiceLayerException, ProductNotFoundException, ErightsException {
        String baseUrl = "redirect:/mvc/eacGroups/search.htm?";
        String params;

        boolean deleted = eacGroupService.deleteUnUsedEacGroup(eacGroupId);       
        if(deleted){
            params = "statusMessageKey=" + EAC_GROUP_DELETE_SUCCESS;
        }else{
            params = "errorMessageKey=" + EAC_GROUP_DELETE_FAILURE;
        }
        ModelAndView result = new ModelAndView(baseUrl + params);
        return result;
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
	
	private void setType(final RegistrationDefinitionSearchCriteria searchCriteria) {
		searchCriteria.setRegistrationDefinitionType(RegistrationDefinitionType.ACTIVATION_CODE_REGISTRATION);
	}
	
	private void setProductState(final RegistrationDefinitionSearchCriteria searchCriteria) {
		Set<ProductState> states = new HashSet<ProductState>();
		states.add(ProductState.ACTIVE);
		searchCriteria.setProductStates(states);
	}
	
	private boolean isEacGroupNameInUse(EacGroups eacGroup) throws ServiceLayerException{
		EacGroups existingEacGroup = eacGroupService.getEacGroupByName(eacGroup.getGroupName());
		if (existingEacGroup != null && !StringUtils.equals(eacGroup.getId(), existingEacGroup.getId()) 
				&& StringUtils.equals(eacGroup.getGroupName(), existingEacGroup.getGroupName())) {
			return true;
		}
		else {
			return false;
		}
		//return existingEacGroup != null && !StringUtils.equals(eacGroup.getId(), existingEacGroup.getId());
	}
	
}
