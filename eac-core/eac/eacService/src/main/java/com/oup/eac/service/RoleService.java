package com.oup.eac.service;

import java.util.List;
import java.util.Set;

import com.oup.eac.domain.Permission;
import com.oup.eac.domain.Role;

public interface RoleService {

	/**
	 * Gets a List of all {@link Role}s in the system ordered alphabetically by role name.
	 * 
	 * @return The list of roles.
	 * 
	 */
    Role getRoleById(String id);
    
    boolean isRoleDeleteable(String id);
    
    void deleteRole(String id);
    
    List<Permission> getAllPermissions();
    
    Role getRoleWithPermissionsById(String id);
    
	List<Role> getAllRoles();
	
	List<Permission> getPermissionsByRole(String roleId);
	
	void updateRolePermissions(String roleId, String roleName, Set<String> permissionIds);
	
	String saveRole(String roleName, Set<String> permissionIds);
}
