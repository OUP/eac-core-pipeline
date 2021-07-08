package com.oup.eac.ws.rest.v1;

import java.io.Serializable;

/**
 * Get All Roles Request
 * 
 * User: vaibhav.mehta Date: June 27, 2017
 *
 */
public class GetAllRolesRequest implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 12343L;

    /** Role type is person, organisation or all. */
    private String roleType;

    /**
     * 
     * @return
     */
    public String getRoleType() {
        return roleType;
    }

    /**
     * 
     * @param roleType
     */
    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

}
