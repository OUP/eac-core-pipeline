package com.oup.eac.ws.rest.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Get All Roles Request
 * 
 * User: vaibhav.mehta Date: June 27, 2017
 *
 */
public class GetAllRolesResponse extends AbstractRoleResponse implements
        Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 12343L;

    private List<UserRolesWS> userRoles;

    /**
     * Gets the value of the userRoles property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the childIds property.
     * 
     * @return list of userRoles
     * 
     * 
     */

    public List<UserRolesWS> getUserRoles() {
        if (userRoles == null) {
            userRoles = new ArrayList<UserRolesWS>();
        }
        return userRoles;
    }

}
