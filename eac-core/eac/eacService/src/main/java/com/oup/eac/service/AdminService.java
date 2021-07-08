package com.oup.eac.service;

import java.util.List;

import com.oup.eac.domain.AdminUser;
import com.oup.eac.service.exceptions.UsernameExistsException;

public interface AdminService {
	
	AdminUser getAdminUserById(String id);

    AdminUser getAdminUserByUsername(String username);
    
    List<AdminUser> getAllAdminUsersOrderedByName();
    
    void saveAdminUser(AdminUser adminUser) throws UsernameExistsException;
    
    void updateAdminUserLastLoginTime(String username);
    
    void deleteAdminUser(AdminUser adminUser); 
    
    /**
     * Can admin delete customer.
     *
     * @param adminId the admin id
     * @param customerId the customer id
     * @return true, if the admin can delete the Customer.
     */
    boolean canAdminDeleteCustomer(String adminId, String customerId);
    
	/**
	 * Performs the necessary reset password logic for the supplied username. If no admin account exists for the
	 * supplied username, does nothing.
	 * 
	 * @param username
	 *            The username of the account to reset the password for.
	 */
	void resetPassword(final String username) throws Exception;
	
	String prepareResetPasswordEmail(final String username) throws Exception;
	
	AdminUser getAdminUserWithOutRoleAndPermission(final String id); 
	
	AdminUser getAdminUserByUsernameUnInitialised(String username);
}
