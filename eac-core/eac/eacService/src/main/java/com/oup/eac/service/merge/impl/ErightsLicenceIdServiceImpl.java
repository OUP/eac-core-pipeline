package com.oup.eac.service.merge.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oup.eac.data.LinkedRegistrationDao;
import com.oup.eac.data.RegistrationDao;
import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.LinkedProduct;
import com.oup.eac.domain.LinkedRegistration;
import com.oup.eac.domain.ProductRegistration;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.service.merge.ErightsLicenceIdService;
import com.oup.eac.service.merge.RegistrationLicenceMergeInfoDto;

/**
 * Used to populate the eRightsLicenceIds fields.
 * 
 * @author David Hay
 *
 */
@Service("eRightsLicenceIdService")
public class ErightsLicenceIdServiceImpl implements ErightsLicenceIdService {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(ErightsLicenceIdServiceImpl.class);
    
    @Autowired
    private RegistrationDao registrationDao;
    
    @Autowired
    private LinkedRegistrationDao linkedRegDao;
    
    @Override
    public void saveErightsLicencesInfo(List<RegistrationLicenceMergeInfoDto> updates) {
        for(RegistrationLicenceMergeInfoDto update : updates){
            saveErightsLicencesInfo(update);
        }
    }
    
    @Override
    public void saveErightsLicencesInfo(RegistrationLicenceMergeInfoDto update) {       
        Registration<ProductRegistrationDefinition> reg = this.registrationDao.getRegistrationById(update.getRegistration().getId());
         
        //at this point we know that reg is a persistent object in the hibernate session
        //this allows us to lazy load the existing linked registrations so we can remove them
        Set<LinkedRegistration> oldLinks = reg.getLinkedRegistrations();
        for(LinkedRegistration oldLink : oldLinks){
            this.linkedRegDao.delete(oldLink);
        }
        oldLinks.clear();
        
        //update the registration
        reg.setId(update.getRegistration().getId());
        
        Map<Integer, LinkedProduct> linkData = update.getLinkedRegistrations();
        for(Map.Entry<Integer,LinkedProduct> entry : linkData.entrySet()){
            LinkedProduct lp = entry.getValue(); 
            Integer eRightsLicenceId = entry.getKey();                       
            LinkedRegistration linkReg = new LinkedRegistration();
            linkReg.setRegistration(reg);
            linkReg.setLinkedProduct(lp);
            linkReg.setErightsLicenceId(eRightsLicenceId);
            //save the new linked registration
            linkedRegDao.save(linkReg);
        }
    }
    
    @Override
    public List<Registration<? extends ProductRegistrationDefinition>> getCustomerRegistrations(Customer customer, boolean productRegistrations, boolean activationCodeRegistrations) { 
        List<Registration<? extends ProductRegistrationDefinition>> result = new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
        if(productRegistrations){
            List<ProductRegistration> prodReg1 = this.registrationDao.getProductRegistrationsForCustomer(customer);
            result.addAll(prodReg1);
        }
        if(activationCodeRegistrations){
            List<ActivationCodeRegistration> acReg1 = registrationDao.getActivationCodeRegistrationsForCustomer(customer);
            result.addAll(acReg1);
        }
        return result;
    }
    
}
