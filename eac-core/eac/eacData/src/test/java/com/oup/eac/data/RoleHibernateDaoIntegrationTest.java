/*package com.oup.eac.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oup.eac.domain.Permission;
import com.oup.eac.domain.Role;
import com.oup.eac.domain.util.SampleDataFactory;

public class RoleHibernateDaoIntegrationTest extends AbstractDBTest {

	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private PermissionDao permissionDao;
	
	private Role role1;
	
	private Permission permission1, permission2;
	
	@Before
	public void setUp() throws Exception {
		getSampleDataCreator().createRole("ROLE_9");
		getSampleDataCreator().createRole("ROLE_5");
		getSampleDataCreator().createRole("ROLE_7");
		
		role1 = getSampleDataCreator().createRole("ROLE_1");
		permission1 = getSampleDataCreator().createPermission("perm1");
		permission2 = getSampleDataCreator().createPermission("perm2");
		role1.addPermission(permission1);
		role1.addPermission(permission2);
		getSampleDataCreator().createRolePermissions(role1);
		loadAllDataSets();
	}
	
	@Test
	public void shouldGetOrderedRoles() {
		List<Role> roles = roleDao.getRolesOrderedByNameWithFullyFetchedPermissions();
		assertThat(roles.size(), equalTo(4));
		assertThat(roles.get(0).getName(), equalTo("ROLE_1"));
		assertThat(roles.get(1).getName(), equalTo("ROLE_5"));
		assertThat(roles.get(2).getName(), equalTo("ROLE_7"));
		assertThat(roles.get(3).getName(), equalTo("ROLE_9"));
	}
	
	@Test
	public void testDeletePermission() {
	    Role r1 = roleDao.loadEntity(role1.getId());
	    Permission p1 = permissionDao.loadEntity(permission1.getId());
	    
	    Assert.assertEquals(2, r1.getPermissions().size());
	    r1.removePermission(p1);
	    roleDao.update(r1);
	    roleDao.flush();
	    roleDao.clear();
	    
	    Role r2 = roleDao.loadEntity(role1.getId());
	    Assert.assertEquals(1, r2.getPermissions().size());
	}
	
    @Test
    public void testAddPermission() {
        Role r1 = roleDao.loadEntity(role1.getId());
        Permission p1 = SampleDataFactory.createPermission("ROLE_111");
        permissionDao.save(p1);
        
        Assert.assertEquals(2, r1.getPermissions().size());
        r1.addPermission(p1);
        roleDao.update(r1);
        roleDao.flush();
        roleDao.clear();
        
        Role r2 = roleDao.loadEntity(role1.getId());
        Assert.assertEquals(3, r2.getPermissions().size());
    }
    
    @Test
    public void testDeleteRole() {
        Role r1 = roleDao.loadEntity(role1.getId());
        roleDao.delete(r1);
        roleDao.flush();
        roleDao.clear();
        
        Permission permission = permissionDao.getEntity(permission1.getId());
        Assert.assertNotNull(permission);
    }
}
*/