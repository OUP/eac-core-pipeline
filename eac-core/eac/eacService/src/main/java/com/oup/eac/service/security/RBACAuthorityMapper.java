package com.oup.eac.service.security;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

import com.oup.eac.domain.Permission;
import com.oup.eac.domain.Role;

public class RBACAuthorityMapper implements GrantedAuthoritiesMapper {

    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ROLE_USER = ROLE_PREFIX + "USER";

    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {

        Set<String> roleNames = new LinkedHashSet<String>();

        if(authorities != null){
            for (GrantedAuthority authority : authorities) {
                if(authority == null){
                    continue;
                }
                addAuthority(roleNames, authority.getAuthority());
                if (authority.getClass().isAssignableFrom(Role.class)) {
                    Role role = (Role) authority;
                    Set<Permission> permissions = role.getPermissions();
                    if (permissions != null) {
                        for (Permission permission : permissions) {
                            addAuthority(roleNames, permission.getAuthority());
                        }
                    }
                }
            }
        }
        // Everyone who is authenticated has ROLE_USER
        if (roleNames.contains(ROLE_USER) == false) {
            roleNames.add(ROLE_USER);
        }
        Set<GrantedAuthority> rolesAndPermissions = new LinkedHashSet<GrantedAuthority>();

        for (String role : roleNames) {
            rolesAndPermissions.add(new SimpleGrantedAuthority(role));
        }
        return rolesAndPermissions;
    }

    private void addAuthority(Set<String> authorities, String authority) {
        if (StringUtils.isBlank(authority)) {
            return;
        }
        String roleName;
        if (authority.startsWith(ROLE_PREFIX)) {
            roleName = authority;
        } else {
            roleName = ROLE_PREFIX + authority;
        }
        authorities.add(roleName);
    }

}
