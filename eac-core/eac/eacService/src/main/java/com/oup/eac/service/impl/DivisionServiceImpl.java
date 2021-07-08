package com.oup.eac.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.oup.eac.data.AdminUserDao;
import com.oup.eac.data.DivisionAdminUserDao;
import com.oup.eac.data.DivisionDao;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.DivisionAdminUser;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.DivisionDto;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.DivisionAlreadyExistsException;
import com.oup.eac.integration.facade.exceptions.DivisionNotFoundException;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.DivisionService;
import com.oup.eac.service.ServiceLayerException;

@Service("divisionService")
public class DivisionServiceImpl implements DivisionService {

	private final DivisionDao divisionDao;
	private final AdminUserDao adminUserDao;
	private final DivisionAdminUserDao divisionAdminUserDao;
	private final ErightsFacade erightsFacade;
    private static final Logger LOG = Logger.getLogger(DivisionServiceImpl.class);
    private static Set<Integer> divisionsUsedInProduct = new HashSet<Integer>() ;
    
	@Autowired
	public DivisionServiceImpl(ErightsFacade erightsFacade, DivisionDao divisionDao, AdminUserDao adminUserDao, DivisionAdminUserDao divisionAdminUserDao) {
		this.erightsFacade = erightsFacade ;
		this.divisionDao = divisionDao;
		this.adminUserDao = adminUserDao;
		this.divisionAdminUserDao = divisionAdminUserDao;
	}
	
	@Override
	public Division getDivisionById(String id) {
		return this.divisionDao.getEntity(id);
	}
	
	@Override
	public List<Division> getAllDivisions() throws ErightsException, DivisionNotFoundException, AccessDeniedException {
		return convertDtoToDivision( erightsFacade.getAllDivisions()) ;
		//return divisionDao.getDivisions();
	}

	@Override
	public List<Division> getDivisionsByAdminUser(AdminUser adminUser) throws ErightsException, DivisionNotFoundException, AccessDeniedException {
		//Set<DivisionAdminUser> divisionAdminUsers = adminUser.getDivisionAdminUsers() ;
		List<DivisionAdminUser> divisionAdminUsers = divisionAdminUserDao.getDivisionAdminUserByAdmin(adminUser);
		
		List<Division> allDivision = convertDtoToDivision( erightsFacade.getAllDivisions()) ;
		List<Division> returnDivision = new ArrayList<Division>() ;
		
		for (DivisionAdminUser divisionAdminUser : divisionAdminUsers) {
			for (Division division : allDivision) {
				if ( division.getErightsId().equals(divisionAdminUser.getDivisionErightsId())) {
					returnDivision.add(division) ;
				}
			}
		}
		Collections.sort(returnDivision, new Comparator<Division>() {
    		
			@Override
			public int compare(Division o1, Division o2) {
				return o1.getDivisionType().compareTo(o2.getDivisionType());
			}
		});
		return returnDivision;
	}

    @Override
    public Boolean isDivisionUsed(String divisionId) throws ServiceLayerException {
        AdminUser currentAdmin = AuditLogger.getAdminUser();
        //Division d = this.divisionDao.getEntity(divisionId);
        boolean flag = false;
        List<DivisionDto> divisions = null;
		try {
			divisions = erightsFacade.getAllDivisions();
		} catch (AccessDeniedException | ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (divisions != null) {
			for (DivisionDto division : divisions) {
				if (division.getId().toString().equals(divisionId)) {
					flag = true;
				}
			}
		}
		System.out.println("flag" + flag);
		if (flag == false) {
			throw new ServiceLayerException("No Division/OrgUnit with id " + divisionId);
		}
		
		/*System.out.println("!d.contains(divisionId)" + !d.contains(divisionId));*/
        if(divisions == null ){
            throw new ServiceLayerException("No Division/OrgUnit with id " + divisionId);
        }
        if (divisionsUsedInProduct.contains(Integer.parseInt(divisionId))) {
        	return true ;
        }
        return divisionDao.isDivisionUsed(divisionId, currentAdmin);
    }

    @Override
    public boolean updateDivisions(final List<Division> toDelete, final List<Division> toUpdate, List<Division> toAdd) throws ServiceLayerException, ErightsException {

        AdminUser adminUser = AuditLogger.getAdminUser();
        List<Division> prevDivisions = getAllDivisions();
        boolean isSuccess = insertData(toDelete, toUpdate, toAdd) ;
        boolean updated = false;
        if(isSuccess)
        {
	        if (toDelete.size() > 0) {
	            for (Division div : toDelete) {
	            	Division liveDivision = null;
	                List<Division> divisions = getAllDivisions();	//this.divisionDao.getById(div.getId(), false);
	                for (Division division : divisions) {
	                	if (div.getErightsId() == division.getErightsId()) {
	                		liveDivision = division;
	                	}
	                }
	                		
	                if(liveDivision != null){
	                    DivisionAdminUser dau = this.divisionAdminUserDao.getDivisionAdminUserByDivisionAndAdmin(liveDivision, adminUser);
	                    if (dau != null) {
	                        this.divisionAdminUserDao.delete(dau);
	                    }
	                    //this.divisionDao.delete(live);
	                }
	                AuditLogger.logEvent("Deleted OrgUnit", div.getDivisionType());
	                updated = true;
	            }
	        }
	
	        if (toUpdate != null) {
	        	Division previous = null;
	            for (Division div : toUpdate) {
	                for (Division division : prevDivisions) {
	                	if (div.getErightsId().equals(division.getErightsId())) {
	                		previous = division;
	                		if (previous != null && previous.getDivisionType().equals(div.getDivisionType()) == false) {
			                    AuditLogger.logEvent("Updated OrgUnit From", previous.getDivisionType(), "To",
			                            div.getDivisionType());
			                    updated = true;
			                    break;
			                }
	                	}
	                }
	                if (updated) {
            			break;
            		}
	            }
	        }
	
	        if (toAdd.size() > 0) {
	            for (Division newDiv : toAdd) {
	                //this.divisionDao.save(newDiv);
	            	Division newDivision = new Division();
	                List<Division> divisions = getAllDivisions();	//this.divisionDao.getById(div.getId(), false);
	                for (Division division : divisions) {
	                	if (newDiv.getErightsId().equals(division.getErightsId())) {
	                		newDivision = division;
	                		break;
	                	}
	                }
	                
	                if (adminUser == null) {
	                    LOG.warn("No Logged In AdminUser when managing OrgUnits!");
	                } else {
	                    AdminUser live = this.adminUserDao.loadEntity(adminUser.getId());
	                    DivisionAdminUser dau = new DivisionAdminUser(live, newDivision);
	                    dau.setDivisionErightsId(newDivision.getErightsId());
	                    divisionAdminUserDao.save(dau);
	                    live.getDivisionAdminUsers().add(dau);
	                    newDivision.getDivisionAdminUsers().add(dau);
	                }
	                AuditLogger.logEvent("Added OrgUnit", newDivision.getDivisionType());
	                if (adminUser != null) {
	                    AuditLogger.logEvent("Can now manage OrgUnit", newDivision.getDivisionType());
	                }
	                updated = true;
	            }
	        }
        } else {
        	return false ;
        }
        return updated;
    }       
    
    private boolean insertData(List<Division> toDelete, List<Division> toUpdate, List<Division> toAdd) throws ErightsException{
    	try {
			
	    	if ( toDelete.size() > 0 && toDelete != null){
	    		List<DivisionDto> divisionDtos = convertDivisionToDto(toDelete) ;
	    		
	    		
	    		AdminUser adminUser = AuditLogger.getAdminUser();
	            for (Division div : toDelete) {
	            	Division liveDivision = null;
	                List<Division> divisions = getAllDivisions();	//this.divisionDao.getById(div.getId(), false);
	                for (Division division : divisions) {
	                	if (div.getErightsId() == division.getErightsId()) {
	                		liveDivision = division;
	                	}
	                }
	                		
	                if(liveDivision != null){
	                    DivisionAdminUser dau = this.divisionAdminUserDao.getDivisionAdminUserByDivisionAndAdmin(liveDivision, adminUser);
	                    if (dau != null) {
	                        this.divisionAdminUserDao.delete(dau);
	                    }
	                    //this.divisionDao.delete(live);
	                }
	                AuditLogger.logEvent("Deleted OrgUnit", div.getDivisionType());
	            }
	        
	    		
	    		
	    		
	    		erightsFacade.deleteDivision(divisionDtos);
	    	}
			if ( toUpdate.size() > 0 && toUpdate != null){
				List<DivisionDto> divisionDtos = convertDivisionToDto(toUpdate) ;
				toUpdate = convertDtoToDivision(erightsFacade.updateDivision(divisionDtos));
	    	}
			if ( toAdd.size() > 0 && toAdd != null){
				List<DivisionDto> divisionDtos = convertDivisionToDto(toAdd) ;
				List<DivisionDto> divisionDtoToAdd = divisionDtos ;
				divisionDtos = erightsFacade.createDivision(divisionDtos) ;
				divisionDtos.retainAll(divisionDtoToAdd) ;
				List<Division> divisionUpdatedList = convertDtoToDivision(divisionDtos) ; 
				toAdd.clear() ;
				toAdd.addAll(divisionUpdatedList) ;
			}
    	} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false ;
		} catch (DivisionNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false ;
		} catch (DivisionAlreadyExistsException e){
			e.printStackTrace();
			return false ;
		}
    	return true ;
    }

    private List<DivisionDto> convertDivisionToDto(List<Division> divisions){
    	List<DivisionDto> divisionDtos = new ArrayList<DivisionDto>() ;
    	for(Division division : divisions){
			DivisionDto divisionDto = new DivisionDto() ;
			if(division.getErightsId() != null)
				divisionDto.setId(division.getErightsId());
			divisionDto.setDivisionType(division.getDivisionType());
			divisionDtos.add(divisionDto);
		}
    	return divisionDtos ;
    }
    private List<Division> convertDtoToDivision(List<DivisionDto> divisionDtos){
    	List<Division> divisions = new ArrayList<Division>() ;
    	for(DivisionDto divisionDto : divisionDtos){
			Division division = new Division() ;
			if(divisionDto.getId() != null)
				division.setErightsId(divisionDto.getId());
			division.setDivisionType(divisionDto.getDivisionType());
			divisions.add(division);
		}
    	return divisions ;
    }
    
    @Override
    public Set<Integer> getDivisionsUsedInProduct() {
		return divisionsUsedInProduct;
	}
    
    @Override
	public void setDivisionsUsedInProduct(Set<Integer> divisionsUsedInProduct) {
		DivisionServiceImpl.divisionsUsedInProduct = divisionsUsedInProduct;
	}
}

