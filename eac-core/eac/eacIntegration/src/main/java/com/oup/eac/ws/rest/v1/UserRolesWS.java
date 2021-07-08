package com.oup.eac.ws.rest.v1;

import java.io.Serializable;

/**
 * The Class UserRolesWS.
 */
public class UserRolesWS implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 12343L;

    private String roleName;

    private String roleType;

    private Integer level;

    /**
     * 
     * @return
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 
     * @param roleName
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

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

    /**
     * 
     * @return
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * 
     * @param level
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

}
