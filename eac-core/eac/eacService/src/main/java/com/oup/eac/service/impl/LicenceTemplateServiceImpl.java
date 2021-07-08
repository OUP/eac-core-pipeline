package com.oup.eac.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.oup.eac.data.LicenceTemplateDao;
import com.oup.eac.domain.ConcurrentLicenceTemplate;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.LicenceTemplate.LicenceType;
import com.oup.eac.domain.RollingLicenceTemplate;
import com.oup.eac.domain.StandardLicenceTemplate;
import com.oup.eac.domain.UsageLicenceTemplate;
import com.oup.eac.service.LicenceTemplateService;
import com.oup.eac.service.ServiceLayerException;

@Service(value="licenceTemplateService")
public class LicenceTemplateServiceImpl implements LicenceTemplateService {

    private final LicenceTemplateDao licenceTemplateDao;
    
    @Autowired
    public LicenceTemplateServiceImpl(final LicenceTemplateDao licenceTemplateDao) {
        super();
        Assert.notNull(licenceTemplateDao);
        this.licenceTemplateDao = licenceTemplateDao;
    }


    @Override
    public LicenceTemplate saveLicenceTemplate(final LicenceTemplate licenceTemplate) throws ServiceLayerException {
        licenceTemplateDao.save(licenceTemplate);
        return licenceTemplate;
    }


    @Override
	public void saveOrUpdateLicenceTemplate(final LicenceTemplate licenceTemplate) throws ServiceLayerException {
    	licenceTemplateDao.saveOrUpdate(licenceTemplate);
	}


	@Override
    public LicenceTemplate createLicenceTemplate(LicenceType licenceType) throws ServiceLayerException {
        switch (licenceType) {
            case STANDARD: 
                return new StandardLicenceTemplate(); 
            case CONCURRENT: 
                return new ConcurrentLicenceTemplate();
            case USAGE: 
                return new UsageLicenceTemplate(); 
            case ROLLING: 
                return new RollingLicenceTemplate();
            default: 
                return null;
        }
    }

}
