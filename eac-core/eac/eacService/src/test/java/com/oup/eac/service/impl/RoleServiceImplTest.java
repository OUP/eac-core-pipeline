package com.oup.eac.service.impl;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oup.eac.data.AbstractDBTest;
import com.oup.eac.domain.Permission;
import com.oup.eac.domain.Role;
import com.oup.eac.service.RoleService;

public class RoleServiceImplTest extends AbstractDBTest {

    @Autowired
    private RoleService roleService;
    
    private Role role1;
    
    private Permission permission1, permission2, permission3, permission4, permission5;
    
    @Before
    public void setUp() throws Exception {
        role1 = getSampleDataCreator().createRole("ROLE_1");
        permission1 = getSampleDataCreator().createPermission("perm1");
        permission2 = getSampleDataCreator().createPermission("perm2");
        permission3 = getSampleDataCreator().createPermission("perm3");
        permission4 = getSampleDataCreator().createPermission("perm4");
        permission5 = getSampleDataCreator().createPermission("perm5");
        role1.addPermission(permission1);
        role1.addPermission(permission2);
        role1.addPermission(permission3);
        getSampleDataCreator().createRolePermissions(role1);
        loadAllDataSets();
    }
    
    @Test
    public final void testChangingPermssions() throws Exception {
        Assert.assertEquals(3, countRowsInTable("ROLE_PERMISSIONS"));
        
        Set<String> ids = new HashSet<String>();
        ids.add(permission1.getId());
        ids.add(permission3.getId());
        ids.add(permission4.getId());
        roleService.updateRolePermissions(role1.getId(), "role name", ids);
        
        Assert.assertEquals(3, countRowsInTable("ROLE_PERMISSIONS"));
        
        Role role = roleService.getRoleWithPermissionsById(role1.getId());
        Assert.assertEquals(3, role.getPermissions().size());
        Set<String> pids = getIds(role.getPermissions());
        Assert.assertTrue(pids.contains(permission1.getId()));
        Assert.assertTrue(pids.contains(permission3.getId()));
        Assert.assertTrue(pids.contains(permission4.getId()));
        Assert.assertFalse(pids.contains(permission2.getId()));
        Assert.assertFalse(pids.contains(permission5.getId()));
    }
    
    private Set<String> getIds(Set<Permission> permissions) {
        Set<String> ids = new HashSet<String>();
        for(Permission permission : permissions) {
            ids.add(permission.getId());
        }
        return ids;
    }

}
