package com.oup.eac.data;

import java.util.List;

import com.oup.eac.domain.Permission;
import com.oup.eac.domain.Role;

public interface RoleDao extends BaseDao<Role, String> {
    
    Long getRoleCountByNameAndNotId(String name, String id);
    
    Long getCountUsersWithRole(String roleId);
    
    Role getRoleWithPermissions(String id);

	List<Role> getRolesWithPermission(Permission permission);
	
	List<Role> getRolesOrderedByNameWithFullyFetchedPermissions();
}
