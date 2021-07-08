package com.oup.eac.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oup.eac.data.PermissionDao;
import com.oup.eac.data.RoleDao;
import com.oup.eac.domain.Permission;
import com.oup.eac.domain.Role;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.service.RoleService;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

	private final RoleDao roleDao;
	
	private final PermissionDao permissionDao;
	
	@Autowired
	public RoleServiceImpl(final RoleDao roleDao, final PermissionDao permissionDao) {
		this.roleDao = roleDao;
		this.permissionDao = permissionDao;
	}
	
	@Override
	public final boolean isRoleDeleteable(final String id) {
	    return roleDao.getCountUsersWithRole(id) == 0;
	}
	
	@Override
	public void deleteRole(final String id) {
	    if(!isRoleDeleteable(id)) {
	        throw new IllegalStateException("There are 1 more or more users using this role.");
	    }
	    Role role = roleDao.loadEntity(id);
	    AuditLogger.logEvent("Deleted Role", "Id:"+role.getId(),AuditLogger.role(role));
	    roleDao.delete(role);
	    roleDao.flush();
	    roleDao.clear();
	    
	}
	
    @Override
    public Role getRoleById(String id) {
        return roleDao.getEntity(id);
    }
    
    @Override
    public final Role getRoleWithPermissionsById(final String id) {
        return roleDao.getRoleWithPermissions(id);
    }

	@Override
	public final List<Role> getAllRoles() {
		return roleDao.getRolesOrderedByNameWithFullyFetchedPermissions();
	}

	@Override
	public List<Permission> getAllPermissions() {
	    return permissionDao.getAllPermissions();
	}
	
	@Override
	public final List<Permission> getPermissionsByRole(final String roleId) {
	    Role role = roleDao.loadEntity(roleId);
	    AuditLogger.logEvent("Search Permission For Role", "Id:"+role.getId(),AuditLogger.role(role));
	    return permissionDao.getPermissionsByRole(role);
	}
	
	public final String saveRole(final String roleName, final Set<String> permissionIds) {
	    Role role = new Role(roleName);
	    for(String permissionId : permissionIds) {
	        role.addPermission(permissionDao.loadEntity(permissionId));
	    }
	    roleDao.save(role);
	    String roleId = role.getId();
	    roleDao.flush();
	    roleDao.clear();
	    AuditLogger.logEvent("Saved Role", "Id:"+role.getId(),AuditLogger.role(role));
	    return roleId;
	}
	
	@Override
	public final void updateRolePermissions(final String roleId, final String roleName, final Set<String> permissionIds) {
	    Role role = roleDao.loadEntity(roleId);
	    Long count = roleDao.getRoleCountByNameAndNotId(roleName, roleId);
	    if(count > 0) {
	        throw new IllegalArgumentException("There is already a role with this name");
	    }
	    role.setName(roleName);
	    Set<String> existingPermissionIds = getIds(role.getPermissions());
	    Set<String> removePermissions = getRemovePermission(existingPermissionIds, permissionIds);
	    Set<String> addPermissions = getAddPermission(existingPermissionIds, permissionIds);
	    for(String permissionId : removePermissions) {
	        Permission permission = permissionDao.loadEntity(permissionId);
	        role.removePermission(permission);
	    }
        for(String permissionId : addPermissions) {
            Permission permission = permissionDao.loadEntity(permissionId);
            role.addPermission(permission);
        }
	    roleDao.update(role);
	    roleDao.flush();
	    roleDao.clear();
	    AuditLogger.logEvent("Updated Role", "Id:"+role.getId(),AuditLogger.role(role));
	} 
	
	private Set<String> getRemovePermission(Set<String> existingPermissionIds, Set<String> permissionIds) {
	    Set<String> removePermissions = new HashSet<String>();
        for(String existingPermissionId : existingPermissionIds) {
            if(!permissionIds.contains(existingPermissionId)) {
                removePermissions.add(existingPermissionId);
            }
        }
	    return removePermissions;
	}
	
    private Set<String> getAddPermission(Set<String> existingPermissionIds, Set<String> permissionIds) {
        Set<String> addPermissions = new HashSet<String>();
        for(String permissionId : permissionIds) {
            if(!existingPermissionIds.contains(permissionId)) {
                addPermissions.add(permissionId);
            }
        }
        return addPermissions;
    }
    
    private Set<String> getIds(Set<Permission> permissions) {
        Set<String> ids = new HashSet<String>();
        for(Permission permission : permissions) {
            ids.add(permission.getId());
        }
        return ids;
    }

}
