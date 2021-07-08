package com.oup.eac.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.MessageTextSource;
import com.oup.eac.common.utils.email.EmailUtils;
import com.oup.eac.common.utils.email.VelocityUtils;
import com.oup.eac.common.utils.token.TokenConverter;
import com.oup.eac.data.AdminUserDao;
import com.oup.eac.data.CustomerDao;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.MailCriteria;
import com.oup.eac.domain.User;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.AdminChangePasswordTokenDto;
import com.oup.eac.service.AdminService;
import com.oup.eac.service.EmailService;
import com.oup.eac.service.exceptions.UsernameExistsException;

@Component(value="adminService")
public class AdminServiceImpl implements AdminService, UserDetailsService {

    private final AdminUserDao adminUserDao;
    private final CustomerDao customerDao; 
    private final VelocityEngine velocityEngine;
    private final EmailService emailService;
    private final MessageSource messageSource;
    
    @Autowired
    public AdminServiceImpl(
    		final AdminUserDao adminUserDao, 
    		final CustomerDao customerDao,
    		final VelocityEngine velocityEngine, 
    		final EmailService emailService, 
    		final MessageSource messageSource) {
        this.adminUserDao = adminUserDao;
		this.customerDao = customerDao;
        this.velocityEngine = velocityEngine;
		this.emailService = emailService;
		this.messageSource = messageSource;
    }
    
    @Override
	public AdminUser getAdminUserById(final String id) {
		return adminUserDao.getAdminUserById(id);
	}

    
    @Override
   	public AdminUser getAdminUserWithOutRoleAndPermission(final String id) {
   		return adminUserDao.getAdminUserWithOutRoleAndPermission(id);
   	}
	@Override
    public AdminUser getAdminUserByUsername(final String username) {
        return adminUserDao.getAdminUserByUsername(username);
    }
    
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException, DataAccessException {
        UserDetails details = getAdminUserByUsername(username);
        if (details != null) return details;
        
        throw new UsernameNotFoundException("AdminUser " + username + " was not found.");
    }
    
    public void resetLockedUserAccount(final User user) {
        user.setLocked(false);        
    }
    
    @Override
    public void saveAdminUser(final AdminUser adminUser) throws UsernameExistsException {
    	AdminUser existingUser = adminUserDao.getAdminUserByUsername(adminUser.getUsername());
    	if(existingUser == null){
            existingUser = getAdminUserByUsernameUnInitialised(adminUser.getUsername());
        }
    	Customer customer = customerDao.getCustomerByUsername(adminUser.getUsername());
    	if (isUsernameInUseByExistingAdminUser(existingUser, adminUser) || isUsernameInUseByCustomer(customer, adminUser)) {
    		throw new UsernameExistsException(adminUser.getUsername());
    	}
    	if(adminUser.getId()!=null)
    	{
    	    adminUserDao.update(adminUser);
    	}
    	else
    	{
    		UUID uuid = UUID.randomUUID();
    		adminUser.setId(uuid.toString());
    		adminUser.setCreatedDate(new DateTime());
    		adminUserDao.saveOrUpdate(adminUser);
    	}
    	AuditLogger.logEvent("Saved Account", "Id:"+adminUser.getId(), AuditLogger.adminUser(adminUser));
    }

	private boolean isUsernameInUseByExistingAdminUser(final AdminUser existingUser, final AdminUser adminUser) {
		return existingUser != null && !StringUtils.equals(existingUser.getId(), adminUser.getId());
	}
	
	private boolean isUsernameInUseByCustomer(final Customer customer, final AdminUser adminUser) {
		return customer != null && !StringUtils.equals(customer.getId(), adminUser.getId());
	}

	@Override
	public void updateAdminUserLastLoginTime(final String username) {
		AdminUser adminUser = adminUserDao.getAdminUserByUsernameUnInitialised(username);
		adminUser.setLastLoginDateTime(new DateTime());
		adminUserDao.update(adminUser);
		
		AuditLogger.logEvent("Updated Account", "Id:"+adminUser.getId()+" LastLogin:"+adminUser.getLastLoginDateTime(), AuditLogger.adminUser(adminUser));
	}

	@Override
	public List<AdminUser> getAllAdminUsersOrderedByName() {
		return adminUserDao.getAllAdminUsersOrderedByName();
	}

	@Override
	public void deleteAdminUser(final AdminUser adminUser) {
		adminUserDao.delete(adminUser);
		AuditLogger.logEvent("Deleted Account", "Id:"+adminUser.getId(), AuditLogger.adminUser(adminUser));
	}

    @Override
    public boolean canAdminDeleteCustomer(String adminId, String customerId) {
        boolean result = this.adminUserDao.canAdminDeleteCustomer(adminId, customerId);
        return result;
    }

	@Override
	public void resetPassword(final String username) throws Exception {
		AdminUser adminUser = adminUserDao.getAdminUserByUsername(username);
		if (adminUser != null) {
			MailCriteria mailCriteria = new MailCriteria();
			mailCriteria.setReplyTo(EmailUtils.getInternetAddressReplyTo());
			mailCriteria.setSubject(EACSettings.getProperty("admin.password.email.subject"));
			mailCriteria.setFrom(EmailUtils.getInternetAddressFrom());
			mailCriteria.addToAddress(adminUser.getEmailInternetAddress());
			String emailContent = prepareResetPasswordEmail(username);
			mailCriteria.setText(emailContent);
			emailService.sendMail(mailCriteria);
		}
	}

	@Override
	public String prepareResetPasswordEmail(final String username) throws Exception {
		MessageTextSource resource = getResource(Locale.ENGLISH);
		Map<String, Object> templateVariables = new HashMap<String, Object>();
		templateVariables.put("resource", resource);
		templateVariables.put("username", username);
		templateVariables.put("link", EACSettings.getProperty("admin.password.change.url") + TokenConverter.encrypt(new AdminChangePasswordTokenDto(username)));
		String emailContent = VelocityUtils.mergeTemplateIntoString(velocityEngine, "com/oup/eac/service/velocity/adminPasswordReset.vm", templateVariables);
		return emailContent;
	}
    
	private MessageTextSource getResource(final Locale locale) {
		MessageTextSource result = new MessageTextSource(this.messageSource, locale);
		return result;
	}
	
	@Override
    public AdminUser getAdminUserByUsernameUnInitialised(String username) {
        return adminUserDao.getAdminUserByUsernameUnInitialised(username);
    }
}
