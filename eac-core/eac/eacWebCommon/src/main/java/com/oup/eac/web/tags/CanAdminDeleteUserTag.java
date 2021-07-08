package com.oup.eac.web.tags;

import org.springframework.security.core.userdetails.UserDetails;

import com.oup.eac.common.utils.security.SecurityContextUtils;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.service.AdminService;

public class CanAdminDeleteUserTag extends BaseGetValueTag {

    private String userId;
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private AdminUser getAdmin() {
        AdminUser result = null;
        UserDetails userDetails = SecurityContextUtils.getCurrentUser();
        if (userDetails instanceof AdminUser) {
            result = (AdminUser) userDetails;
        }
        return result;
    }
    
    @Override
    public Object getValue() {
        AdminService adminService = getService("adminService", AdminService.class);
        AdminUser admin  = getAdmin();
        if(admin == null){
            return false;
        }else{
            String adminId = admin.getId();
            Boolean value = adminService.canAdminDeleteCustomer(adminId, userId);
            return value;    
        }
    }

}
