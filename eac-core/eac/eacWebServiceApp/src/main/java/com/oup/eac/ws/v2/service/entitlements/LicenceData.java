package com.oup.eac.ws.v2.service.entitlements;

import com.oup.eac.ws.v2.binding.common.LicenceDetails;
import com.oup.eac.ws.v2.binding.common.LicenceInfo;

/**
 * Simple POJO for containing LicenceDetails and LicenceInfo.
 * This class cannot move to the domain project because of the dependencies on generated code.
 * @author David Hay
 *
 */
public class LicenceData {

    private LicenceDetails detail;
    private LicenceInfo info;
    
    public LicenceDetails getDetail() {
        return detail;
    }
    public void setDetail(LicenceDetails detail) {
        this.detail = detail;
    }
    public LicenceInfo getInfo() {
        return info;
    }
    public void setInfo(LicenceInfo info) {
        this.info = info;
    }
}
