package com.oup.eac.accounts.dto;

import java.io.Serializable;

/**
 * Form DTO for redeeming Activation Code.  
 * @author Gaurav Soni
 */

public class CodeRedeemDto implements Serializable {

    private String code;
    private boolean sharedUser;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSharedUser() {
        return sharedUser;
    }

    public void setSharedUser(boolean sharedUser) {
        this.sharedUser = sharedUser;
    }
}
