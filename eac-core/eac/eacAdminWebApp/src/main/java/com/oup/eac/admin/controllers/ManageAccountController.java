package com.oup.eac.admin.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.admin.beans.AccountBean;
import com.oup.eac.admin.validators.AccountBeanValidator;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.paging.Paging;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.domain.paging.PagingCriteria.SortDirection;
import com.oup.eac.dto.AdminAccountSearchBean;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.AccountSearchService;
import com.oup.eac.service.AdminService;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.DivisionService;
import com.oup.eac.service.RoleService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.UsernameExistsException;

@Controller
@RequestMapping("/mvc/account/manage.htm")
public class ManageAccountController {

	private static final String MANAGE_ACCOUNT_VIEW = "manageAccount";
	private static final String MANAGE_ACCOUNT_TABS_VIEW = "manageAccountTabs";
	private static final String MODEL = "accountBean";

	private final AdminService adminService;
	private final DivisionService divisionService;
	private final RoleService roleService;
	private final CustomerService customerService;
	private final AccountBeanValidator validator;

	private final AccountSearchService accountSearchService;
	@Autowired
	public ManageAccountController(
			final AdminService adminService, 
			final DivisionService divisionService, 
			final RoleService roleService, 
			final AccountBeanValidator validator,final AccountSearchService accountSearchService,final CustomerService customerService) {
		this.adminService = adminService;
		this.divisionService = divisionService;
		this.roleService = roleService;
		this.accountSearchService=accountSearchService;
		this.customerService=customerService;
		this.validator = validator;
	}

 
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showAdminUserList() {
		ModelAndView modelAndView=searchAccountInitial();
//		modelAndView.addObject("fragments", "searchResultsAccountTile");
		return modelAndView ;
	}

	@RequestMapping(method = RequestMethod.GET, params = "id")
	public ModelAndView showForm(@ModelAttribute(MODEL) final AccountBean accountBean,String id) {
		
		ModelAndView modelAndView=new ModelAndView(MANAGE_ACCOUNT_TABS_VIEW);
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView saveAccount( @ModelAttribute(MODEL) final AccountBean accountBean, final BindingResult bindingResult) {

		ModelAndView modelAndView = new ModelAndView(MANAGE_ACCOUNT_VIEW);
		String messageKey = null;
		if(!accountBean.isEditMode()){
            accountBean.setChangePassword(true);
        }
		validator.validate(accountBean, bindingResult);
		if (!bindingResult.hasErrors()) {
			AdminUser adminUser = accountBean.getUpdatedAdminUser();
			try {
				adminService.saveAdminUser(adminUser);
				accountBean.setAdminUsers(adminService.getAllAdminUsersOrderedByName());
				messageKey="status.account.save.success";
				modelAndView=searchAccountInitial();
				modelAndView.addObject("statusMessageKey", messageKey);
			} catch (final UsernameExistsException e) {
				bindingResult.reject("error.username.taken");
			}
		}else{
			return new ModelAndView(MANAGE_ACCOUNT_TABS_VIEW);
		}

		return modelAndView;
	}
	
	private ModelAndView searchAccountInitial(){
		ModelAndView modelAndView = new ModelAndView(MANAGE_ACCOUNT_VIEW);
		AdminAccountSearchBean accountSearchBean=new AdminAccountSearchBean();
		AccountBean accountBean = null;
		try {
			accountBean = new AccountBean(adminService.getAllAdminUsersOrderedByName(), divisionService.getAllDivisions(), roleService.getAllRoles());
		} catch (AccessDeniedException | ErightsException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Paging<AdminUser> resultsPage =null;
		try{
			resultsPage=accountSearchService.searchAdminCustomers(accountSearchBean,PagingCriteria.valueOf(accountBean.getResultsPerPage(), accountBean.getPageNumber(), SortDirection.DESC, "username"));
			
		}catch(ServiceLayerException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace(); 
		}
		modelAndView.addObject("pageInfo", resultsPage);
		modelAndView.addObject("customers", resultsPage.getItems());
		modelAndView.addObject("fragments", "searchResultsAccountTile");
		return modelAndView;
	}
	
	private ModelAndView searchAccount(@Valid @ModelAttribute(MODEL) final AccountBean accountBean, final BindingResult bindingResult) {
		AdminAccountSearchBean accountSearchBean=new AdminAccountSearchBean();
		ModelAndView modelAndView = new ModelAndView(MANAGE_ACCOUNT_VIEW);

		if(null!=accountBean.getUsername() && !accountBean.getUsername().equals("")){
			accountSearchBean.setUserName(accountBean.getUsername());
		}
		if(null!=accountBean.getEmail() && !accountBean.getEmail().equals("")){
			accountSearchBean.setEmailAddress(accountBean.getEmail());
		}
		if(null!=accountBean.getFamilyName() && !accountBean.getFamilyName().equals("")){
			accountSearchBean.setFamilyName(accountBean.getFamilyName());
		}
		if(null!=accountBean.getFirstName() && !accountBean.getFirstName().equals("")){
			accountSearchBean.setFirstName(accountBean.getFirstName());
		}
		if(null!=accountBean.getSelectedDivision()){
			accountSearchBean.setSelectedDivision(accountBean.getSelectedDivision());
		}
		if(null!=accountBean.getSelectedRole()){
			accountSearchBean.setSelectedRole(accountBean.getSelectedRole());
		}
		
		Paging<AdminUser> resultsPage =null;
		try{
			resultsPage=accountSearchService.searchAdminCustomers(accountSearchBean,PagingCriteria.valueOf(accountBean.getResultsPerPage(), accountBean.getPageNumber(), SortDirection.DESC, "username"));
			
		}catch(ServiceLayerException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace(); 
		}
			
		
	/*	if (!bindingResult.hasErrors()) {
			AdminUser adminUser = accountBean.getUpdatedAdminUser();
			try {
				adminService.saveAdminUser(adminUser);
				accountBean.setAdminUsers(adminService.getAllAdminUsersOrderedByName());
				modelAndView.addObject("statusMessageKey", "status.account.save.success");
			} catch (final UsernameExistsException e) {
				bindingResult.reject("error.username.taken");
			}
		}*/
		modelAndView.addObject("pageInfo", resultsPage);
		modelAndView.addObject("customers", resultsPage.getItems());
		
		return modelAndView;
	}
	
	@RequestMapping(method = RequestMethod.POST,params="_eventId=search")
	public ModelAndView searchAccounts(@Valid @ModelAttribute(MODEL) final AccountBean accountBean, final BindingResult bindingResult){
		accountBean.setPageNumber(1);
		ModelAndView modelAndView =searchAccount(accountBean,bindingResult);
		return modelAndView;
	}
	
	@RequestMapping(method = RequestMethod.POST, params = "_eventId=nextPage")
	public ModelAndView nextPage(@ModelAttribute(MODEL) final AccountBean accountBean, final BindingResult bindingResult) throws ServiceLayerException {
		accountBean.incrementPageNumber();
		return searchAccount(accountBean,bindingResult);
	}
	
	@RequestMapping(method = RequestMethod.POST, params = "_eventId=previousPage")
	public ModelAndView previousPage(@ModelAttribute(MODEL) final AccountBean accountBean, final BindingResult bindingResult) throws ServiceLayerException {
		accountBean.decrementPageNumber();
		return searchAccount(accountBean,bindingResult);
	}
	
	@RequestMapping(method = RequestMethod.POST, params = "_eventId=reset")
    public ModelAndView reset() throws ServiceLayerException {
        return searchAccountInitial();
    }
	
	@RequestMapping(method=RequestMethod.POST, params="delete")
	public ModelAndView deleteAccount(@ModelAttribute(MODEL) final AccountBean accountBean) {
		adminService.deleteAdminUser(accountBean.getSelectedAdminUser());
		accountBean.setAdminUsers(adminService.getAllAdminUsersOrderedByName());
		if (accountBean.isMyAccount()) {
			SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
		}
		accountBean.clearSelectedAdminUser();
		ModelAndView modelAndView = new ModelAndView(MANAGE_ACCOUNT_VIEW);
		modelAndView=searchAccountInitial();
		modelAndView.addObject("statusMessageKey", "status.account.delete.success");
		return modelAndView;
	}

	@ModelAttribute(MODEL)
	public AccountBean createModel(@RequestParam(value = "id", required = false) final String id,final HttpServletRequest request) {
		HttpSession session = request.getSession();
		//System.out.println("Refresh Session "+request.getParameterMap().containsKey("refreshSession"));
		if(request.getParameterMap().containsKey("refreshSession")){
			//System.out.println("Inside the if of refresh SEssion");
			session.removeAttribute("accountBean");
		}
		String adminId="";
		String [] adminIdArray=null;
		AccountBean accountBean =(AccountBean)session.getAttribute("accountBean");
		
		if(id==null || StringUtils.isBlank(id) || id.equalsIgnoreCase("null,null")){
            
            AccountBean freshAccountBean = null;
			try {
				freshAccountBean = new AccountBean(adminService.getAllAdminUsersOrderedByName(), divisionService.getAllDivisions(), roleService.getAllRoles());
			} catch (AccessDeniedException | ErightsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if(accountBean != null){
                freshAccountBean.setPageNumber(accountBean.getPageNumber());
                freshAccountBean.setResultsPerPage(accountBean.getResultsPerPage());
            }
            session.setAttribute("accountBean",freshAccountBean);
            return freshAccountBean;
        } else{
			if(null!=id && id.contains(",")){
				adminIdArray=id.split(",");
				adminId=adminIdArray[0];
			}else{
				adminId=id;
			}
			try {
				if (null!=id && StringUtils.isNotBlank(id)) {
					if(!adminId.equals("null")){ 
					    accountBean.setEditMode(true);
					    accountBean.setSelectedAdminUser(adminService.getAdminUserById(adminId));
						if(null==accountBean.getSelectedAdminUser()){
							if(null!=id && id.contains(",")){
								adminIdArray=id.split(",");
								adminId=adminIdArray[0];
							}else{
								adminId=id;
							}
							accountBean.setSelectedAdminUser(adminService.getAdminUserWithOutRoleAndPermission(adminId)); 
							session.setAttribute("accountBean",accountBean);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				//throw new ServiceLayerException("");
		    }
		}
			
		

//		if (StringUtils.isNotBlank(id)) {
//			accountBean.setSelectedAdminUser(adminService.getAdminUserById(id));
//		}

		return accountBean;
	}


}
