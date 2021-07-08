package com.oup.eac.service.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

import com.oup.eac.domain.Permission;
import com.oup.eac.domain.Role;

public class RBACAuthorityMapperTest {

	private GrantedAuthoritiesMapper mapper = new RBACAuthorityMapper();
	
	private Collection<Role> rolesWithPermissions;
	
	@Before
	public void setUp() throws Exception {
		rolesWithPermissions = new HashSet<Role>();
		
		Set<Permission> sysAdPerms = new HashSet<Permission>();
		sysAdPerms.add(new Permission("ONLY_ADMIN_PERM"));
		sysAdPerms.add(new Permission("PERM_1"));
		sysAdPerms.add(new Permission("PERM_2"));
		sysAdPerms.add(new Permission("PERM_3"));
		sysAdPerms.add(new Permission("PERM_4"));
		sysAdPerms.add(new Permission("PERM_5"));
		
		Set<Permission> divAdPerms = new HashSet<Permission>();
		divAdPerms.add(new Permission("PERM_1"));
		divAdPerms.add(new Permission("PERM_2"));
		divAdPerms.add(new Permission("PERM_3"));
		divAdPerms.add(new Permission("PERM_4"));
		divAdPerms.add(new Permission("PERM_5"));
		
		Set<Permission> prodAdPerms = new HashSet<Permission>();
		divAdPerms.add(new Permission("PERM_2"));
		divAdPerms.add(new Permission("PERM_3"));
		divAdPerms.add(new Permission("PERM_4"));
		divAdPerms.add(new Permission("PERM_5"));
		
		Set<Permission> fieldRepPerms = new HashSet<Permission>();
		divAdPerms.add(new Permission("PERM_3"));
		divAdPerms.add(new Permission("PERM_4"));
		divAdPerms.add(new Permission("PERM_5"));

		
		rolesWithPermissions.add(new Role("1","SYSTEM_ADMIN", sysAdPerms));
		rolesWithPermissions.add(new Role("2","DIVISION_ADMIN", divAdPerms));
		rolesWithPermissions.add(new Role("3","PRODUCTION_ADMIN", prodAdPerms));
		rolesWithPermissions.add(new Role("4","FIELD_REP", fieldRepPerms));
	}
	
	@Test
	public void testMapAuthorities() throws Exception {
		Collection<? extends GrantedAuthority> rolesAndPermissions = mapper.mapAuthorities(rolesWithPermissions);
		
		assertEquals("Check correct number of roles and permissions", 11, rolesAndPermissions.size());
		
		for (GrantedAuthority authority : rolesAndPermissions) {
			assertTrue("Check starts with prefix", authority.getAuthority().startsWith(RBACAuthorityMapper.ROLE_PREFIX));
		}
	}
	
	@Test
	public void testNoAuthorities() throws Exception {
		Collection<? extends GrantedAuthority> rolesAndPermissions = mapper.mapAuthorities(new HashSet<Role>());
		assertEquals("Check correct number of roles and permissions", 1, rolesAndPermissions.size());
		assertEquals("Check ROLE_USER", RBACAuthorityMapper.ROLE_USER, rolesAndPermissions.iterator().next().getAuthority());
	}
	
    @Test
    public void testAlwaysGetRoleUser() {
        GrantedAuthority nonRole = new SimpleGrantedAuthority("BLAH");
        List<GrantedAuthority> authorities = Arrays.asList(nonRole);
        Collection<? extends GrantedAuthority> results = mapper.mapAuthorities(authorities);
        Iterator<? extends GrantedAuthority> iter = results.iterator();
        GrantedAuthority ga1 = iter.next();
        Assert.assertEquals("ROLE_BLAH", ga1.getAuthority());

        GrantedAuthority ga2 = iter.next();
        Assert.assertEquals("ROLE_USER", ga2.getAuthority());
    }
    
    @Test
    public void testNullSet() {
        Collection<? extends GrantedAuthority> results = mapper.mapAuthorities(null);
        Iterator<? extends GrantedAuthority> iter = results.iterator();
        GrantedAuthority ga1 = iter.next();
        Assert.assertEquals("ROLE_USER", ga1.getAuthority());
    }
    
    @Test
    public void testBlankAuthority() {
        GrantedAuthority nonRole = new GrantedAuthority() {
            
            @Override
            public String getAuthority() {
                return "  ";
            }
        };
        List<GrantedAuthority> authorities = Arrays.asList(nonRole);
        Collection<? extends GrantedAuthority> results = mapper.mapAuthorities(authorities);
        Iterator<? extends GrantedAuthority> iter = results.iterator();
        GrantedAuthority ga1 = iter.next();
        Assert.assertEquals("ROLE_USER", ga1.getAuthority());
    }
    
    @Test
    public void testRoleUserNotDuplicated() {
        GrantedAuthority userRole = new SimpleGrantedAuthority("ROLE_USER");
        List<GrantedAuthority> authorities = Arrays.asList(userRole);
        Collection<? extends GrantedAuthority> results = mapper.mapAuthorities(authorities);
        Iterator<? extends GrantedAuthority> iter = results.iterator();
        GrantedAuthority ga1 = iter.next();
        Assert.assertEquals("ROLE_USER", ga1.getAuthority());
    }
    
    
    @Test
    public void testNullAuthority() {
        GrantedAuthority authority = null;
        List<GrantedAuthority> authorities = Arrays.asList(authority);
        Collection<? extends GrantedAuthority> results = mapper.mapAuthorities(authorities);
        Iterator<? extends GrantedAuthority> iter = results.iterator();
        GrantedAuthority ga1 = iter.next();
        Assert.assertEquals("ROLE_USER", ga1.getAuthority());
    }
    
    @Test
    public void testRoleWithNullPermissionSet() {
        GrantedAuthority authority = new Role("BLAH",null);
        List<GrantedAuthority> authorities = Arrays.asList(authority);
        Collection<? extends GrantedAuthority> results = mapper.mapAuthorities(authorities);
        Iterator<? extends GrantedAuthority> iter = results.iterator();
        GrantedAuthority ga1 = iter.next();
        Assert.assertEquals("ROLE_BLAH", ga1.getAuthority());

        GrantedAuthority ga2 = iter.next();
        Assert.assertEquals("ROLE_USER", ga2.getAuthority());
    }


}
