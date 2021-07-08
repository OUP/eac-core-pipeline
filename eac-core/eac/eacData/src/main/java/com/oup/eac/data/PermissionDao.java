package com.oup.eac.data;

import java.util.List;

import com.oup.eac.domain.Permission;
import com.oup.eac.domain.Role;

public interface PermissionDao extends BaseDao<Permission, String> {
    
    List<Permission> getAllPermissions();
    
    List<Permission> getPermissionsByRole(Role role);
    
    List<String> getPermissionIdsByRole(Role role);

}
