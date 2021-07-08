package com.oup.eac.service;

import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.LicenceTemplate.LicenceType;

public interface LicenceTemplateService {

    LicenceTemplate saveLicenceTemplate(final LicenceTemplate licenceTemplate) throws ServiceLayerException;
    
    void saveOrUpdateLicenceTemplate(final LicenceTemplate licenceTemplate) throws ServiceLayerException;
    
    LicenceTemplate createLicenceTemplate(final LicenceType licenceType) throws ServiceLayerException;
}
