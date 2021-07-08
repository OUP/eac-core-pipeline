package com.oup.eac.admin.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.constants.SearchDomainFields;
import com.oup.eac.admin.beans.OrgUnitsBean;
import com.oup.eac.admin.validators.OrgUnitValidator;
import com.oup.eac.cloudSearch.search.impl.AmazonCloudSearchServiceImpl;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.search.AmazonSearchFields;
import com.oup.eac.domain.search.AmazonSearchRequest;
import com.oup.eac.domain.search.AmazonSearchResponse;
import com.oup.eac.integration.facade.exceptions.DivisionNotFoundException;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.DivisionService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.ServiceLayerException;

@Controller
@RequestMapping("/mvc/orgunits/manage.htm")
public class ManageOrgUnitsController {

    private static final String MANAGE_ORG_UNITS_VIEW = "manageOrgUnits";
    private static final String MODEL = "orgUnitsBean";
    private static final String AFTER_ORG_UNIT_UPDATE_VIEW = "redirect:/mvc/orgunits/manage.htm";

    
    private static final String MSG_KEY="statusMessageKey";
    
    private static final String MSG_UPDATE_SUCCESS   = "status.org.units.update.success";
    private static final String MSG_UPDATE_NO_CHANGE = "status.org.units.update.nothing";
    
    private static final String UPDATE_ORG_UNITS_SUCCESS_VIEW = String.format("%s?%s=%s",AFTER_ORG_UNIT_UPDATE_VIEW, MSG_KEY, MSG_UPDATE_SUCCESS);
    private static final String UPDATE_ORG_UNITS_NO_CHANGE_VIEW = String.format("%s?%s=%s",AFTER_ORG_UNIT_UPDATE_VIEW, MSG_KEY, MSG_UPDATE_NO_CHANGE);
    
    private final DivisionService divisionService;
    private final ProductService productService;
    private final Validator validator;
    private final SessionFactory sessionFactory;

    @InitBinder
    public void initBinder(final WebDataBinder webDataBinder) {
        webDataBinder.setValidator(validator);
    }

    @Autowired
    public ManageOrgUnitsController(final DivisionService divisionService, final OrgUnitValidator validator, final SessionFactory sessionFactory, final ProductService productService) {
        this.divisionService = divisionService;
        this.validator = validator;
        this.sessionFactory = sessionFactory;
        this.productService = productService ;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showOrgUnitsList() {
    	int resultsPerPage = 1000 ;
    	Set<Integer> divisionsUsedInProduct = new HashSet<Integer>() ;
    	AmazonSearchRequest request = new AmazonSearchRequest();
    	List<AmazonSearchFields> searchFieldsList = new ArrayList<AmazonSearchFields>();
		request.setResultsPerPage(resultsPerPage);
		//request.setSearchResultFieldsList(searchResultFieldsList);
    	request.setSearchFieldsList(searchFieldsList);
    	List<Division> divisions = new ArrayList<Division>();
		try {
			divisions = divisionService.getAllDivisions();
		} catch (AccessDeniedException | ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	List<Integer> divisionsId = new ArrayList<Integer>() ;
    	for (Division division: divisions) {
    		divisionsId.add(division.getErightsId()) ;
    	}
    	AmazonCloudSearchServiceImpl cloudSearch = new AmazonCloudSearchServiceImpl();
    	AmazonSearchResponse response = cloudSearch.searchProductsDivisions(request,divisionsId);
    	List<Map<String, String>> fields = response.getResultFields();
		for (Map<String , String> field: fields){
			if(field.get(SearchDomainFields.PRODUCT_DIVISIONID)!=null){
				divisionsUsedInProduct.add(Integer.parseInt(field.get(SearchDomainFields.PRODUCT_DIVISIONID).replaceAll("\\[", "").replaceAll("\\]","")));
			}
		}
		divisionService.setDivisionsUsedInProduct(divisionsUsedInProduct);
    	return new ModelAndView(MANAGE_ORG_UNITS_VIEW);
    }

    @ModelAttribute(MODEL)
    public OrgUnitsBean createModel() {
        OrgUnitsBean model = null;
		try {
			model = new OrgUnitsBean(divisionService.getAllDivisions());
		} catch (DivisionNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return model;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateOrgUnitsList(final @Valid @ModelAttribute(MODEL) OrgUnitsBean orgUnitsBean,
            final BindingResult bindingResult) throws ServiceLayerException {
        if (bindingResult.hasErrors()) {
            return MANAGE_ORG_UNITS_VIEW;
        }
        List<Division> toDelete = new ArrayList<Division>();
        List<Division> toUpdate = new ArrayList<Division>();
        Set<Integer> indexesToRemove = orgUnitsBean.getIndexesToDelete();

        for (int i = 0; i < orgUnitsBean.getOrgUnits().size(); i++) {
            Division orgUnit = orgUnitsBean.getOrgUnits().get(i);
            Assert.isTrue(StringUtils.isNotBlank(orgUnit.getErightsId().toString()));
            if (indexesToRemove.contains(i)) {
                toDelete.add(orgUnit);
            } else {
                toUpdate.add(orgUnit);
            }
        }
        List<Division> toAdd = orgUnitsBean.getNewOrgUnits();

        //sessionFactory.getCurrentSession().clear();
        boolean updated = false;
		try {
			updated = this.divisionService.updateDivisions(toDelete, toUpdate, toAdd);
		} catch (ErightsException e) {
			// TODO Auto-generated catch block
			 return UPDATE_ORG_UNITS_NO_CHANGE_VIEW;
		}
        if(updated){
            return UPDATE_ORG_UNITS_SUCCESS_VIEW;    
        }else{
            return UPDATE_ORG_UNITS_NO_CHANGE_VIEW;    
        }
        
    }

}
