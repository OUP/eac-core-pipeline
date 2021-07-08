package com.oup.eac.admin.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import com.oup.eac.common.utils.spring.ApplicationContextSupport;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.DivisionAdminUser;
import com.oup.eac.domain.Password;
import com.oup.eac.domain.Role;
import com.oup.eac.service.DivisionService;

public class AccountBean extends PageTrackingBean implements Serializable {

	private List<AdminUser> adminUsers;
	private final List<DivisionAccess> divisionAccessList;
	private final List<RoleAccess> roleAccessList;
	private final List<Role> roles;
	private List<Division> division=new ArrayList<Division>();
	private AdminUser selectedAdminUser;
	private Role selectedRole;
	private Division selectedDivision;
	private boolean changePassword;
	private String password;
	private String confirmPassword;
	private String username;
	private String email;
	private String firstName;
	private String familyName;
	private boolean editMode;	
	
	
	public AccountBean(
			final List<AdminUser> adminUsers, 
			final List<Division> divisions, 
			final List<Role> roles) {
		this.adminUsers = adminUsers;
		this.divisionAccessList = new ArrayList<AccountBean.DivisionAccess>();
		this.roleAccessList = new ArrayList<AccountBean.RoleAccess>();
		this.roles = roles;
		this.division.addAll(divisions);
		for (Division division : divisions) {
			divisionAccessList.add(new DivisionAccess(division));
		}
		for (Role role : roles) {
			roleAccessList.add(new RoleAccess(role));
		}
	}

	public List<AdminUser> getAdminUsers() {
		return adminUsers;
	}

	public void setAdminUsers(final List<AdminUser> adminUsers) {
		this.adminUsers = adminUsers;
	}

	public AdminUser getUpdatedAdminUser() {
		if (changePassword) {
			selectedAdminUser.setPassword(new Password(password, false));
		}
		
		selectedAdminUser.getDivisionAdminUsers().clear();
		
		for (DivisionAccess divisionAccess : divisionAccessList) {
			if (divisionAccess.access) {
				selectedAdminUser.getDivisionAdminUsers().add(new DivisionAdminUser(selectedAdminUser,  divisionAccess.division));
			}
		}
		
		selectedAdminUser.getRoles().clear();
		
		for (RoleAccess roleAccess: roleAccessList) {
			if (roleAccess.access) {
				selectedAdminUser.getRoles().add(roleAccess.role);
			}
		}
		
		return selectedAdminUser;
	}

	public AdminUser getSelectedAdminUser() {
		return selectedAdminUser;
	}

	public void setSelectedAdminUser(final AdminUser selectedAdminUser) {
		this.selectedAdminUser = selectedAdminUser;		
		List<Division> accessibleDivisions = null;
		try {
		if (selectedAdminUser.getId() != null) {
			DivisionService divisionService = (DivisionService) ApplicationContextSupport.getBean("divisionService");
			accessibleDivisions = divisionService.getDivisionsByAdminUser(selectedAdminUser);
			if(null!=selectedAdminUser && null != accessibleDivisions){
				//accessibleDivisions=selectedAdminUser.getDivisions();	
				for (DivisionAccess divisionAccess : divisionAccessList) {
					if (accessibleDivisions.contains(divisionAccess.getDivision())) {
						divisionAccess.access = true;
					}else{
						divisionAccess.access=false;
					}
				}
			}
		}
		Set<Role> roles = null;
		if(null!=selectedAdminUser && null!=selectedAdminUser.getRoles()){
			roles=selectedAdminUser.getRoles();
			for (RoleAccess roleAccess : roleAccessList) {
				if (roles.contains(roleAccess.getRole())) {
					roleAccess.access = true;
				}else{
					roleAccess.access=false;
				}
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void clearSelectedAdminUser() {
		this.selectedAdminUser = null;
	}

	public boolean isChangePassword() {
		return changePassword;
	}

	public void setChangePassword(final boolean changePassword) {
		this.changePassword = changePassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(final String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	public List<DivisionAccess> getDivisionAccessList() {
		return divisionAccessList;
	}
	
	public List<RoleAccess> getRoleAccessList() {
		return roleAccessList;
	}

	public List<Role> getRoles() {
		return roles;
	}
	
	public String getMyGuid() {
		return ((AdminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
	}
	
	public boolean isMyAccount() {
		boolean myAccount = false;
		if (selectedAdminUser != null) {
			myAccount = StringUtils.equals(selectedAdminUser.getId(), ((AdminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
		}
		return myAccount;
	}

	public static class DivisionAccess implements Serializable{
		private final Division division;
		private boolean access;

		public DivisionAccess(final Division division) {
			this.division = division;
		}

		public Division getDivision() {
			return division;
		}

		public boolean isAccess() {
			return access;
		}
		
		public void setAccess(final boolean access) {
			this.access = access;
		}
	}

	public static class RoleAccess implements Serializable{
		private final Role role;
		private boolean access;

		public RoleAccess(final Role role) {
			this.role = role;
		}

		public Role getRole() {
			return role;
		}

		public boolean isAccess() {
			return access;
		}

		public void setAccess(boolean access) {
			this.access = access;
		}

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public Role getSelectedRole() {
		return selectedRole;
	}

	public void setSelectedRole(Role selectedRole) {
		this.selectedRole = selectedRole;
	}

	public List<Division> getDivision() {
		return division;
	}

	public void setDivision(List<Division> division) {
		this.division = division;
	}

	public Division getSelectedDivision() {
		return selectedDivision;
	}

	public void setSelectedDivision(Division selectedDivision) {
		this.selectedDivision = selectedDivision;
	}

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }
    
    
}
