package com.oup.eac.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;

@Entity
public class Role extends BaseDomainObject implements GrantedAuthority {

	@Column(unique=true)
	private String name;
	
	@ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "role_permissions",
        joinColumns = { @JoinColumn(name = "role_id") }, 
    	inverseJoinColumns = { @JoinColumn(name = "permission_id") })
	private Set<Permission> permissions = new HashSet<Permission>();

	public Role() {
		super();
	}

	public Role(String name) {
        super();
        this.name = name;
    }

    public Role(String name, Set<Permission> permissions) {
		super();
		this.name = name;
		this.permissions = permissions;
	}
    
    public Role(String id,String name, Set<Permission> permissions) {
		super();
		this.setId(id);
		this.name = name;
		this.permissions = permissions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@Override
	public String getAuthority() {
		return name;
	}
	
	public void addPermission(Permission permission) {
	    permissions.add(permission);
	}

    public void removePermission(Permission permission) {
        permissions.remove(permission);
    }
	
    @Override
    public boolean equals(Object obj) {
    	Role role=(Role)obj;
    	return this.getId().equals(role.getId());
    }
    
    @Override
    public int hashCode() {
    	if(null==this.getId()){
    		this.setId("");
    	}
    	return this.getId().hashCode();
    }
	@Override
	public String toString() {
	    return getClass().getName() + "[" + name + "]" + "@" + Integer.toHexString(hashCode());
	}
}
