package com.oup.eac.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.constants.SearchDomainFields;
import com.oup.eac.cloudSearch.search.impl.AmazonCloudSearchServiceImpl;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.search.AmazonSearchFields;
import com.oup.eac.domain.search.AmazonSearchRequest;
import com.oup.eac.domain.search.AmazonSearchResponse;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.Message;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.LicenseNotFoundException;
import com.oup.eac.service.LicenceService;
import com.oup.eac.service.ServiceLayerException;

@Service(value="licenceService")
public class LicenceServiceImpl implements LicenceService {
	
	private AmazonCloudSearchServiceImpl amazonCloudSearchServiceImpl = new AmazonCloudSearchServiceImpl();

	private final ErightsFacade erightsFacade;
	
	@Autowired
    public LicenceServiceImpl(final ErightsFacade erightsFacade) {
		super();
		Assert.notNull(erightsFacade);
		this.erightsFacade = erightsFacade;
	}

	/**
     * Calls the eRightsFacade to get all the licences for a user with some exception processing logic.
     * 
     * @param customer the customer to get the liceces for
     * @return the list of licence information
     * @throws ServiceLayerException if there is a problem
     */
    @Override
    public List<LicenceDto> getLicencesForCustomer(Customer customer) throws ServiceLayerException {
        try{
			/** Reverting this CloudSearch code for Britanico as no longer required
        	List<LicenceDto> licences = new ArrayList<LicenceDto>();
        	licences = checkBritanicoUser(customer.getId());
        	if (licences == null) {
        		 licences = erightsFacade.getLicensesForUser(customer.getId(), null);	
        	}
			*/
        	List<LicenceDto> licences = erightsFacade.getLicensesForUser(customer.getId(), null);
			
            Collections.sort(licences, new Comparator<LicenceDto>() {

				@Override
				public int compare(LicenceDto o1, LicenceDto o2) {
					return o2.getLicenseId().compareTo(o1.getLicenseId());
				}
				
			});
			return licences;
        } catch (LicenseNotFoundException lnfe) {
        	return new ArrayList<LicenceDto>();
        } catch(ErightsException ex){
            throw new ServiceLayerException("Problem getting licences for customer " + customer.getId(), ex);
        } catch(Exception e) {
        	throw new ServiceLayerException("Problem getting licences for customer " + customer.getId(), e);
        }
    }
    
    @Override
    public List<LicenceDto> getLicencesForCustomer(Customer customer, String licenseId) throws ServiceLayerException {
        try{
        	List<LicenceDto> licences = new ArrayList<LicenceDto>();
        	licences = checkBritanicoUser(customer.getId());
        	if (licences == null) {
        		 licences = erightsFacade.getLicensesForUser(customer.getId(), licenseId);	
        	}
        	
            Collections.sort(licences, new Comparator<LicenceDto>() {

				@Override
				public int compare(LicenceDto o1, LicenceDto o2) {
					return o2.getLicenseId().compareTo(o1.getLicenseId());
				}
				
			});
			return licences;
        } catch (LicenseNotFoundException lnfe) {
        	return new ArrayList<LicenceDto>();
        } catch(ErightsException ex){
            throw new ServiceLayerException("Problem getting licences for customer " + customer.getId(), ex);
        }
    }
    
    /**
     * Calls the eRightsFacade to get all the licences for a user with some exception processing logic.
     * 
     * @param customer the customer to get the licences for
     * @return the list of licence information
     * @throws ServiceLayerException if there is a problem
     */
    @Override
    public List<LicenceDto> getActiveLicencesForCustomer(final Customer customer) throws ServiceLayerException {
        //TODO DJH here would be a good place to filter out inactive licences
    	throw new NotImplementedException();
    }
    
    
    @Override
    public List<LicenceDto> getLicensesForUserProduct(final Customer customer, final Product product) throws ServiceLayerException {
    	try {
			List<LicenceDto> licences = erightsFacade.getLicensesForUserProduct(customer.getId(), product.getId());
			Collections.sort(licences, new Comparator<LicenceDto>() {

				@Override
				public int compare(LicenceDto o1, LicenceDto o2) {
					return o2.getLicenseId().compareTo(o1.getLicenseId());
				}
				
			});
			return licences;
		} catch (ErightsException e) {
			throw new ServiceLayerException(
                    "Erights problem getting licences for customer." + customer.getUsername(), e, new Message(
                            "error.problem.getting.customer.licences",
                            "There was a problem getting licenes from erights. Please contact the system administrator."));
		}
    }

	@Override
	public void updateLicencesForCustomer(final Customer customer, final List<LicenceDto> licences) throws ServiceLayerException {

		//TODO should consider what happens if individual licence update fails. Some updates may have been committed in erights
		//Attempt update in erights
		for (LicenceDto licenceDto : licences) {
			if(!(licenceDto.getLicenseId().contains(licenceDto.getProductIds().get(0)))){
				updateLicenceForCustomer(customer, licenceDto);
			}
		}		
	}
	
	@Override
	public void updateLicenceForCustomer(final Customer customer, final LicenceDto licence) throws ServiceLayerException {
		
		try {
			erightsFacade.updateLicence(customer.getId(), licence);
			AuditLogger.logEvent(customer, "Updated Erights Licence", licence.getLicenseId().toString());
		} catch (ErightsException e) {
			throw new ServiceLayerException(
                    "Erights problem updating licence for customer." + customer.getUsername(), e, new Message(
                            "error.problem.updating.customer.licence",
                            "There was a problem updating licence in erights for customer with id " + customer.getId() + " and licence with id " + licence.getLicenseId() + ". Please contact the system administrator."));
		}
	}
	
	public List<LicenceDto> checkBritanicoUser(String userId){		
		    	
    	AmazonCloudSearchServiceImpl cloudSearch = new AmazonCloudSearchServiceImpl();
    	
    	if (cloudSearch.searchUserGroupMembershipForBritanico(userId)) {
    		System.out.println("User Found in CloudSearch for Britanico");
    		return cloudSearch.searchAllLicensesForBritanico(userId);
    	} else {
    		return null;
    	}
    	
    	
	}
}
