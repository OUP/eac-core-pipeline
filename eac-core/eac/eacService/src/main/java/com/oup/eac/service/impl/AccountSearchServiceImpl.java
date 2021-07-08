package com.oup.eac.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oup.eac.data.AdminAccountSearchDao;
import com.oup.eac.data.RoleDao;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.Role;
import com.oup.eac.domain.paging.Paging;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.AdminAccountSearchBean;
import com.oup.eac.dto.Message;
import com.oup.eac.service.AccountSearchService;
import com.oup.eac.service.DivisionService;
import com.oup.eac.service.ServiceLayerException;

@Service("adminAccountSearchService")
public class AccountSearchServiceImpl implements AccountSearchService {
	
	private final AdminAccountSearchDao adminSearchDao;
	private final RoleDao roleDao;
	private final DivisionService divisionService;
	
	
	@Autowired
	public AccountSearchServiceImpl(final AdminAccountSearchDao adminSearchDao,final RoleDao roleDao,
			final DivisionService divisionService) {
		this.adminSearchDao=adminSearchDao;
		this.roleDao=roleDao;
		this.divisionService=divisionService;
	}
	 
	@Override
	public Paging<AdminUser> searchAdminCustomers(final AdminAccountSearchBean customerSearchCriteria, final PagingCriteria pagingCriteria)
			throws ServiceLayerException {
		if(customerSearchCriteria.getUserName() != null && !customerSearchCriteria.getUserName().isEmpty())
			AuditLogger.logEvent("Search Account", AuditLogger.accountSearchCriteria(customerSearchCriteria));
		
		List<AdminUser> customers = adminSearchDao.searchCustomers(customerSearchCriteria, pagingCriteria);
		int count = adminSearchDao.countSearchCustomers(customerSearchCriteria);
		Division selectDivision=customerSearchCriteria.getSelectedDivision();
		Role selectRole=null;
		if(null!=customerSearchCriteria.getSelectedRole()){
		selectRole=roleDao.getById(customerSearchCriteria.getSelectedRole().getName(),false);
		}
		//Set<DivisionAdminUser> setOfDivAdminUsr=new HashSet<DivisionAdminUser>(); 
		Set<AdminUser> setOfAdminUserWithDiv=new HashSet<AdminUser>();
		List<AdminUser> lstOfCustWithDiv=new ArrayList<AdminUser>();
		String selectedDivType=null;		
		String selectedRoleName=null;
		if(null!=selectDivision && null!=selectDivision.getDivisionType()){
			selectedDivType=selectDivision.getDivisionType();	
		}
		Boolean searchFlagDivOrRole=false;
		setOfAdminUserWithDiv.addAll(customers);
		int initialVal=0;
		int fromIndex =0;
		int toIndex =0 ;
		int totalNoRecordToBeDisplayed=0;
		if(null!=selectRole){
			selectedRoleName=selectRole.getName();
		}
		if(selectDivision!=null && !selectedDivType.equals("pleaseSelect")){
			System.out.println("Selected division :: " + selectDivision.getDivisionType());
			searchFlagDivOrRole=true;
			//Need to take Set instead of List
			setOfAdminUserWithDiv.removeAll(customers);
			if(customers!=null){
				for (Iterator iterator = customers.iterator(); iterator.hasNext();) {
					AdminUser adminUser = (AdminUser) iterator.next();
					//System.out.println("Admin User Name "+adminUser.getUsername());
					//setOfDivAdminUsr=adminUser.getDivisionAdminUsers();
					List<Division> divisionList = null;
					try {
						divisionList = divisionService.getDivisionsByAdminUser(adminUser);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						throw new ServiceLayerException("Unexpected error.", e, new  Message( "error.unknown", "Unexpected error."));
					}
					/*for (Iterator iterator2 = setOfDivAdminUsr.iterator(); iterator2
							.hasNext();) {
						DivisionAdminUser divisionAdminUser = (DivisionAdminUser) iterator2
								.next();*/
						//System.out.println("The Division id in divadminuser is "+divisionAdminUser.getDivision().getId()+" the selected division is "+selectDivision.getDivisionType()+"  "+selectDivision.getId());
						if (divisionList != null && divisionList.size() > 0) {
							for(Division division : divisionList) {
								System.out.println(adminUser.getId() + " :: " + division.getErightsId());
								if (division.getErightsId().intValue() == Integer.parseInt((selectDivision.getDivisionType()))) {
									System.out.println("Div Matched.");
									setOfAdminUserWithDiv.add(adminUser);
									break;
								}
							}
						//}
						/*if(divisionAdminUser.getDivision().getId().equals(selectDivision.getDivisionType())){
							setOfAdminUserWithDiv.add(adminUser);
//							lstOfCustWithDiv.add(adminUser);
						}*/ 
					}
				}
			}
			
			//System.out.println("The Size of the List is "+setOfAdminUserWithDiv.size());
			count=setOfAdminUserWithDiv.size();
		}
		if (selectRole!=null && !selectedRoleName.equals("pleaseSelect")){
			Set<Role> setOfRoleAlreadyHave=null;
			
			List<AdminUser> lstOfAdminUser=new ArrayList<AdminUser>();
			if(searchFlagDivOrRole){
				lstOfAdminUser.addAll(setOfAdminUserWithDiv);
				setOfAdminUserWithDiv=new HashSet<AdminUser>();
				for (Iterator iterator = lstOfAdminUser.iterator(); iterator.hasNext();) {
					AdminUser adminUser = (AdminUser) iterator.next();
					setOfRoleAlreadyHave=adminUser.getRoles();
					//System.out.println("The Role already have "+adminUser.getUsername());
					if(setOfRoleAlreadyHave.contains(selectRole)){
						//System.out.println("^^^^^^");
						setOfAdminUserWithDiv.add(adminUser);
					}
				}
			}else{
				setOfAdminUserWithDiv.removeAll(customers);	
				for (Iterator iterator = customers.iterator(); iterator.hasNext();) {
					AdminUser adminUser = (AdminUser) iterator.next();
					setOfRoleAlreadyHave=adminUser.getRoles();
					//System.out.println("The Role already have "+adminUser.getUsername());
					if(setOfRoleAlreadyHave.contains(selectRole)){
						//System.out.println("^^^^^^");
						setOfAdminUserWithDiv.add(adminUser);
					}
				}
			}
			
			
			count=setOfAdminUserWithDiv.size();
		}else{
			
		}
		count=setOfAdminUserWithDiv.size();
		lstOfCustWithDiv=new ArrayList<AdminUser>();
		List<AdminUser> lstOfUserToBeDisplayed=new ArrayList<AdminUser>();
		lstOfCustWithDiv.addAll(setOfAdminUserWithDiv);
		totalNoRecordToBeDisplayed=pagingCriteria.getItemsPerPage();
		initialVal=pagingCriteria.getRequestedPage();
		fromIndex=initialVal*totalNoRecordToBeDisplayed-totalNoRecordToBeDisplayed;
		toIndex=fromIndex + totalNoRecordToBeDisplayed;
		if(toIndex>=count){
			toIndex=count;
		}
		//System.out.println("Total records found in ServiceImpl is "+count+" From Index "+fromIndex+" To index "+toIndex+" the size of the sublist  "+lstOfUserToBeDisplayed.size());
		if(!lstOfCustWithDiv.isEmpty()){
		lstOfUserToBeDisplayed = lstOfCustWithDiv.subList(fromIndex, toIndex);
		}
		
		
		return Paging.valueOf(pagingCriteria, lstOfUserToBeDisplayed, count);
		
		
//		return Paging.valueOf(pagingCriteria, new ArrayList<AdminUser>(), count);
    }
}
