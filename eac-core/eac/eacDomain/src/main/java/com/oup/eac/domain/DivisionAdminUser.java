package com.oup.eac.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Index;


@Entity
public class DivisionAdminUser extends BaseDomainObject {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_user_id")
    private AdminUser adminUser;
    
    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id")
    private Division division;*/

    @Index (name = "division_erights_id_idx")
	private Integer divisionErightsId;	
    
    
    
    public Integer getDivisionErightsId() {
		return divisionErightsId;
	}

	public void setDivisionErightsId(Integer divisionErightsId) {
		this.divisionErightsId = divisionErightsId;
	}

	public DivisionAdminUser() {
        super();
    }

    public DivisionAdminUser(AdminUser adminUser, Division division) {
        super();
        this.adminUser = adminUser;
        this.divisionErightsId = division.getErightsId();
    }

	public DivisionAdminUser(AdminUser adminUser) {
        super();
        this.adminUser = adminUser;
    }
    /**
     * @return the adminUser
     */
	public AdminUser getAdminUser() {
        return adminUser;
    }

    /**
     * @param adminUser the adminUser to set
     */
	public void setAdminUser(final AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    /**
     * @return the division
     */
	/*public Division getDivision() {
        return division;
    }*/

    /**
     * @param division the division to set
     */
	/*public void setDivision(final Division division) {
        this.division = division;
    }*/
    
}
