package com.oup.eac.web.tags;

import org.apache.log4j.Logger;

import com.oup.eac.service.DivisionService;
import com.oup.eac.service.ServiceLayerException;

public class IsOrgUnitUsedTag extends BaseGetValueTag {

    private static final Logger LOG = Logger.getLogger(IsOrgUnitUsedTag.class);
    
    private String orgUnitId;
    
    public String getOrgUnitId() {
        return orgUnitId;
    }

    public void setOrgUnitId(String orgUnitId) {
        this.orgUnitId = orgUnitId;
    }


    @Override
    public Object getValue() {
        DivisionService divsionService = getService("divisionService", DivisionService.class);
        String divisionId = orgUnitId;
        Boolean value =  false;
        try {
            value = divsionService.isDivisionUsed(divisionId);
        } catch (ServiceLayerException ex) {
            LOG.error("Problem trying to see if OrgUnitUsed",ex);
        }
        return value;
    }

}
