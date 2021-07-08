package com.oup.eac.dto;

import java.io.Serializable;
import java.util.List;

import com.oup.eac.domain.Permission;

public class RoleCriteria implements Serializable {
    
    private boolean deletable;
    private String roleId;
    private String roleName;
    private List<PermissionSelection> permissionSelections;

    public RoleCriteria() {
        super();
    }

    public RoleCriteria(String roleId) {
        super();
        this.roleId = roleId;
    }

    /**
     * @return the roleId
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * @param roleId the roleId to set
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    
    /**
     * @return the permissionSelections
     */
    public List<PermissionSelection> getPermissionSelections() {
        return permissionSelections;
    }

    /**
     * @param permissionSelections the permissionSelections to set
     */
    public void setPermissionSelections(List<PermissionSelection> permissionSelections) {
        this.permissionSelections = permissionSelections;
    }

    /**
     * @return the deletable
     */
    public boolean isDeletable() {
        return deletable;
    }

    /**
     * @param deletable the deletable to set
     */
    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    /**
     * @return the roleName
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * @param roleName the roleName to set
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public static class PermissionSelection {
        private Permission permission;
        private boolean access;

        public PermissionSelection() {
            super();
        }

        public PermissionSelection(final Permission permission) {
            this.permission = permission;
        }

        public Permission getPermission() {
            return permission;
        }
        
        public void setPermission(Permission permission) {
            this.permission = permission;
        }

        public boolean isAccess() {
            return access;
        }

        public void setAccess(boolean access) {
            this.access = access;
        }

    }

}
