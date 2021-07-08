package com.oup.eac.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;


@Entity
@DiscriminatorValue(value = "ADMIN")
public class AdminUser extends User {

	@Override
    public UserType getUserType() {        
        return UserType.ADMIN;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "adminUser", cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<DivisionAdminUser> divisionAdminUsers = new HashSet<DivisionAdminUser>();

    /**
     * @return the divisionAdminUsers
     */
	public Set<DivisionAdminUser> getDivisionAdminUsers() {
        return divisionAdminUsers;
    }
    /**
     * @param divisionAdminUsers the divisionAdminUsers to set
     */
	public void setDivisionAdminUsers(final Set<DivisionAdminUser> divisionAdminUsers) {
        this.divisionAdminUsers = divisionAdminUsers;
    }
   
    
    @Override
    public String toString(){
    	StringBuffer sb = new StringBuffer();
    	sb.append("AdminUser[");
    	sb.append(this.getUsername());
    	sb.append("]");
    	sb.append("@");
    	sb.append(Integer.toHexString(hashCode()));
    	String result = sb.toString();
    	return result;
    }
    
    @Override
    public int hashCode() {
    	return this.getUsername().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
    	boolean is=obj instanceof AdminUser;
    	AdminUser adminUser=(AdminUser)obj;
   		return this.getUsername().equals(adminUser.getUsername());
    }
    
    /*public List<Division> getDivisions() {
    	List<Division> divisions = new ArrayList<Division>();
    	Division division=null;
    	for(DivisionAdminUser divisionAdminUser : divisionAdminUsers) {
    		division=divisionAdminUser.getDivision();
    		Hibernate.initialize(division);
    		divisions.add(division);
    	}
    	Collections.sort(divisions, new Comparator<Division>() {
    		
			@Override
			public int compare(Division o1, Division o2) {
				return o1.getDivisionType().compareTo(o2.getDivisionType());
			}
		});
    	return divisions;
    }*/
}
