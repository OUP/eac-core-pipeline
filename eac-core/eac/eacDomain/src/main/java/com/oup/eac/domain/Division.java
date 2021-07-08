package com.oup.eac.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;

@Entity
public class Division extends UpdatedAudit {

    //@Column(unique = true)
    private String divisionType;
    
    @Index (name = "erights_id_idx")
	private Integer erightsId;	
    
    public Division() {
        super();
    }

    public Division(String divisionType) {
        this.divisionType = divisionType;
    }

    //@OneToMany(fetch = FetchType.LAZY, mappedBy = "division")
    @Transient
    private Set<DivisionAdminUser> divisionAdminUsers = new HashSet<DivisionAdminUser>();
   
    @Transient
    private Set<Product> products = new HashSet<Product>();

    /**
     * @return the divisionType
     */
	public String getDivisionType() {
        return divisionType;
    }

    /**
     * @param divisionType the divisionType to set
     */
	public void setDivisionType(final String divisionType) {
        this.divisionType = divisionType;
    }

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

	/**
	 * @return the products
	 */
	public Set<Product> getProducts() {
		return products;
	}

	/**
	 * @param products the products to set
	 */
	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	
	public Integer getErightsId() {
		return erightsId;
	}

	public void setErightsId(Integer erightsId) {
		this.erightsId = erightsId;
	}

	@Override
	public boolean equals(Object obj) {
	    boolean value = false; 
	    if (obj instanceof Division){
	        Division div=(Division)obj;
	        value = this.getErightsId().equals(div.getErightsId());
	    } else if(obj instanceof String){
	        value = this.getId().equals(obj);
	    }
		
		return value;
	}

}
