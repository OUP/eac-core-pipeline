package com.oup.eac.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.oup.eac.service.DataExportService;
import com.oup.eac.service.ServiceLayerException;

public class DummyDataExportServiceImpl implements DataExportService {

    
    private static final Logger LOG = org.apache.log4j.Logger.getLogger(DummyDataExportServiceImpl.class);
    
    @Override
    public void eMailDataExport(String divisionType, DateTime fromDT, DateTime toDT, String emailTo, String eacEmailBcc) throws ServiceLayerException {
        String msg = String.format("**** eMailDataExport(%s,%s,%s,%s)",fromDT,toDT, emailTo, eacEmailBcc);
        LOG.debug(msg);
    }

    @Override
    public List<String[]> getUserDataByOwnerAndDateRange(String divisionType, DateTime fromDT, DateTime toDT) {
        String msg = String.format("**** getUserDataByOwnerAndDateRange(%s,%s)",fromDT,toDT);
        LOG.debug(msg);
        return null;
    }
    
    @PostConstruct
    public void init(){
        LOG.debug("The DummyDataExportService has been created");
    }

    
}
