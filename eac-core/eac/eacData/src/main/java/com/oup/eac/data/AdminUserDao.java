package com.oup.eac.data;

import java.util.List;

import com.oup.eac.domain.AdminUser;

public interface AdminUserDao extends OUPBaseDao<AdminUser, String>{

    AdminUser getAdminUserByUsername(final String username);
    
    AdminUser getAdminUserByUsernameUnInitialised(final String username);
    
    List<AdminUser> getAllAdminUsersOrderedByName();

    boolean canAdminDeleteCustomer(String adminId, String customerId);
    
    AdminUser getAdminUserById(String id);
    
    AdminUser getAdminUserWithOutRoleAndPermission(final String id);
    
    
}
