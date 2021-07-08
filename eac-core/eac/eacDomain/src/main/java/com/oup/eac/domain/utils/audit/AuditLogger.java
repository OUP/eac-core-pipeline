package com.oup.eac.domain.utils.audit;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.security.core.userdetails.UserDetails;

import com.oup.eac.common.utils.security.SecurityContextUtils;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.ExternalCustomerId;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.Message;
import com.oup.eac.domain.Permission;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.Role;
import com.oup.eac.domain.UrlSkin;
import com.oup.eac.dto.ActivationCodeBatchSearchCriteria;
import com.oup.eac.dto.ActivationCodeSearchCriteria;
import com.oup.eac.dto.AdminAccountSearchBean;
import com.oup.eac.dto.CustomerSearchCriteria;
import com.oup.eac.dto.EacGroupsSearchCriteria;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.RegistrationDefinitionSearchCriteria;
import com.oup.eac.dto.ReportCriteria;

/**
 * Helper to write audit event messages to an audit log.
 * 
 */
public final class AuditLogger {

	private static final String SEPERATOR = "\t";

	/**
	 * The name of the logger.
	 */
	private static final String AUDIT = "auditLogger";
	
	private static final String SYSTEM_EVENT = "SYSTEM_EVENT";

	/**
	 * The logger used for auditing.
	 */
	protected static final Logger LOG = Logger.getLogger(AUDIT);

	/**
	 * Hidden Constructor.
	 */
	private AuditLogger() {
		super();
	}


	/**
	 * Audit system event.
	 * 
	 * @param msg
	 *            audit messages
	 */
	public static void logSystemEvent(final String... msgs) {
		logSystemEvent(null, msgs);
	}

	public static void logSystemEvent(final Customer customer, final String... msg) {
		log(true, null, customer, msg);
	}

	/**
	 * Admin event.
	 * 
	 * @param admin
	 *            the admin
	 * @param msgs
	 *            the msgs
	 */
	public static final void logEvent(String... msgs) {
		logEvent((Customer)null, msgs);
	}

	/**
	 * Admin event.
	 * 
	 * @param admin
	 *            the admin
	 * @param msgs
	 *            the msgs
	 */
	public static final void logEvent(Customer user, String... msgs) {
		AdminUser admin = getAdminUser();
		log(false, admin, user, msgs);
	}

	/**
	 * Logs to the Audit file.
	 * 
	 * @param type
	 *            event type
	 * @param msg
	 *            audit messages
	 */
	private static void log(boolean systemEvent, final AdminUser adminUser, final Customer user, final String... msgs) {
		StringBuilder sb = new StringBuilder();
		
		EacAppType appType = EacApp.getType();
		sb.append(appType.name());
		
		if(systemEvent){
			sb.append(SEPERATOR);
			sb.append(SYSTEM_EVENT);	
		}
		
		if (adminUser != null) {
			String adminDesc = String.format("Admin[%s]", adminUser.getUsername());
			sb.append(SEPERATOR);
			sb.append(adminDesc);
		}
		
		if (user != null) {
			String userDesc = String.format("User[%s]", user.getUsername());
			sb.append(SEPERATOR);
			sb.append(userDesc);
		}
		if (msgs != null) {
			for (String message : msgs) {
				sb.append(SEPERATOR);
				sb.append(message);
			}
		}
		LOG.info(sb.toString().trim());
	}

	/**
	 * Gets the admin user.
	 * 
	 * @return the admin user
	 */
	public static AdminUser getAdminUser() {
		AdminUser result = null;
		UserDetails userDetails = SecurityContextUtils.getCurrentUser();
		if (userDetails instanceof AdminUser) {
			result = (AdminUser) userDetails;
		}
		if (result == null) {
			LOG.trace("No logged in user");
		}
		return result;
	}


    public static String product(Product product) {
        StringBuffer sb = new StringBuffer();
        sb.append("product:");
        String name = "<unknown>";
        try {            
            if (canCallGet(product)) {
                if (product != null) {
                    name = product.getProductName();
                }
            }else if(product != null){
            	name = product.getProductName();
            }
        } catch (LazyInitializationException ex) {
            LOG.debug("failed to get product name " + ex.getMessage());
        }
        sb.append(name);
        sb.append("/");
        if (product == null) {
            sb.append(String.valueOf(product));
        } else {
            sb.append(product.getId());
        }
        String result = sb.toString();
        return result;
    }
    
    public static String product(EnforceableProductDto product) {
        StringBuffer sb = new StringBuffer();
        sb.append("product:");
        String name = "<unknown>";
        try {            
            if (canCallGet(product)) {
                if (product != null) {
                    name = product.getName();
                }
            }else if(product != null){
            	name = product.getName();
            }
        } catch (LazyInitializationException ex) {
            LOG.debug("failed to get product name " + ex.getMessage());
        }
        sb.append(name);
        sb.append("/");
        if (product == null) {
            sb.append(String.valueOf(product));
        } else {
            sb.append(product.getProductId());
        }
        String result = sb.toString();
        return result;
    }
    
    public static String eacGroup(EacGroups eacGroup) {
        StringBuffer sb = new StringBuffer();
        sb.append("eacGroup:");
        String name = "<unknown>";
        if (eacGroup == null) {
            sb.append(name);
        } else {
        	name = eacGroup.getGroupName();
        	sb.append(name);
            sb.append(eacGroup.getId());
        }
        String result = sb.toString();
        return result;
    }
     
    public static String registrationReport(ReportCriteria reportCriteria) 
    {
        StringBuffer sb = new StringBuffer();
        sb.append("Registration Report:");
        String name = "<unknown>";
        if (reportCriteria == null) {
            sb.append(name);
        } else {        	
        	sb.append(reportCriteria.getProductId());
        	sb.append("/");
        	sb.append(reportCriteria.getCustomerUsername());
        	sb.append("/");        	
            sb.append(reportCriteria.getRegistrationSelectionType());
        	sb.append("/");        	
            sb.append(reportCriteria.getLastLoginFromDate());
            sb.append("/");        	
            sb.append(reportCriteria.getLastLoginToDate());
            sb.append("/");        	
            sb.append(reportCriteria.getCustomerCreatedFromDate());
            sb.append("/");        	
            sb.append(reportCriteria.getCustomerCreatedToDate());
            sb.append("/");        	
            sb.append(reportCriteria.getRegistrationCreatedFromDate());
            sb.append("/");        	
            sb.append(reportCriteria.getRegistrationCreatedToDate());            
        }
        String result = sb.toString();
        return result;
    }
    
    public static String registrationDefinitionSearchCriteria(RegistrationDefinitionSearchCriteria searchCriteria) 
    {
        StringBuffer sb = new StringBuffer();
        sb.append("Search Product:");
        String name = "<unknown>";
        if (searchCriteria == null) {
            sb.append(name);
        } else {        	
        	sb.append(searchCriteria.getProductName());
        	sb.append("/");
        	sb.append(searchCriteria.getProductId());
        	sb.append("/");
            sb.append(searchCriteria.getExternalId());
        }
        String result = sb.toString();
        return result;
    }
    
    public static String eacGroupsSearchCriteria(EacGroupsSearchCriteria searchCriteria) 
    {
        StringBuffer sb = new StringBuffer();
        sb.append("Search Product Group:");
        String name = "<unknown>";
        if (searchCriteria == null) {
            sb.append(name);
        } else {        	
        	sb.append(searchCriteria.getGroupName());
        	sb.append("/");
        	sb.append(searchCriteria.getProductName());
        	sb.append("/");
        	sb.append(searchCriteria.getProductId());
        	sb.append("/");
            sb.append(searchCriteria.getExternalId());
        }
        String result = sb.toString();
        return result;
    }
    
    
	
    public static String customerSearchCriteria(CustomerSearchCriteria searchCriteria) 
    {
        StringBuffer sb = new StringBuffer();
        sb.append("Search Customer:");
        String name = "<unknown>";
        if (searchCriteria == null) {
            sb.append(name);
        } else {        	
        	sb.append(searchCriteria.getUsername());        	
        	sb.append(searchCriteria.getFirstName());        	
            sb.append(searchCriteria.getFamilyName());
            sb.append(searchCriteria.getEmail());
        }
        String result = sb.toString();
        return result;
    }
    
    /**
     * activationCodeSearchCriteria
     * @param searchCriteria
     * @return
     * String
     * @author Developed by TCS
     */ 
    public static String activationCodeSearchCriteria(ActivationCodeSearchCriteria searchCriteria) 
    {
        StringBuffer sb = new StringBuffer();
        sb.append("Search ActivationCode:");
        String name = "<unknown>";
        if (searchCriteria == null) {
            sb.append(name);
        } else {        	
        	sb.append(searchCriteria.getCode());        	
        	sb.append(searchCriteria.getProductId());        	
            sb.append(searchCriteria.getEacGroupId());
            sb.append(searchCriteria.getActivationCodeState());
        }
        String result = sb.toString();
        return result;
    }
    
    /**
     * activationCodeBatchSearchCriteria
     * @param searchCriteria
     * @return
     * String
     * @author Developed by TCS
     */ 
    public static String activationCodeBatchSearchCriteria(ActivationCodeBatchSearchCriteria searchCriteria) 
    {
        StringBuffer sb = new StringBuffer();
        sb.append("Search ActivationCodeBatch:");
        String name = "<unknown>";
        if (searchCriteria == null) {
            sb.append(name);
        } else {     
        	sb.append(searchCriteria.getBatchId());
        	if (searchCriteria.getRegisterableProduct() != null) {
        		sb.append(searchCriteria.getRegisterableProduct().getErightsId().toString());
        	}
        	if (searchCriteria.getEacGroupId() != null) {
        		sb.append(searchCriteria.getEacGroupId());
        	}
        	if (searchCriteria.getCode() != null) {
        		sb.append(searchCriteria.getCode());
        	}
        	if (searchCriteria.getBatchDate() != null) {
        		sb.append(searchCriteria.getBatchDate());
        	}
        	if (searchCriteria.getEacGroupId() != null) {
        		sb.append(searchCriteria.getEacGroupId());
        	}
        	if (searchCriteria.getLicenceTemplate() != null) {
        		sb.append(searchCriteria.getLicenceTemplate().getName());
        	}
        	
        }
        String result = sb.toString();
        return result;
    }
    
    
    public static String accountSearchCriteria(AdminAccountSearchBean customerSearchCriteria) {
        
    	StringBuffer sb = new StringBuffer();
    	sb.append("Account Search:");
        String name = "<unknown>";
        
		if (customerSearchCriteria != null) {
			sb.append(customerSearchCriteria.getUserName());
			sb.append(SEPERATOR);
			sb.append(customerSearchCriteria.getFirstName());
			sb.append(SEPERATOR);
			sb.append(customerSearchCriteria.getFamilyName());
			sb.append(SEPERATOR);
			sb.append(customerSearchCriteria.getEmailAddress());
			sb.append(SEPERATOR);
			sb.append(customerSearchCriteria.getSelectedRole().getName());
		}
		else
		{
			sb.append(name);
		}

		return sb.toString();        
    }
    
    public static String adminUser(AdminUser adminUser) {
    
    	StringBuffer sb = new StringBuffer();
    	sb.append("adminUser:");
        String name = "<unknown>";
        
		if (adminUser != null) {
			String adminDesc = String.format("Admin[%s]", adminUser.getUsername());
			sb.append(SEPERATOR);
			sb.append(adminDesc);
		}
		else
		{
			sb.append(name);
		}

		return sb.toString();        
    }
    
    public static String message(Message message) {
        
    	StringBuffer sb = new StringBuffer();
    	sb.append("Message:");
        String name = "<unknown>";
        
		if (message != null) {
			sb.append(message.getMessage());
			sb.append(SEPERATOR);			
			sb.append(message.getCountry());
			sb.append(SEPERATOR);
			sb.append(message.getLanguage());
		}
		else
		{
			sb.append(name);
		}

		return sb.toString();        
    }
    

    public static String urlSkin(UrlSkin urlSkin) {
    
    	StringBuffer sb = new StringBuffer();
    	sb.append("UrlSkin:");
        String name = "<unknown>";
        
		if (urlSkin != null) {			
			sb.append(urlSkin.getSiteName());
			sb.append(SEPERATOR);
			sb.append(urlSkin.getSkinPath());
			sb.append(SEPERATOR);
			sb.append(urlSkin.getUrl());
		}
		else
		{
			sb.append(name);
		}

		return sb.toString();        
    }
    

    public static String externalSystem(ExternalSystem externalSystem) 
    {    
    	StringBuffer sb = new StringBuffer();
    	sb.append("ExternalSystem:");
        String name = "<unknown>";
        
		if (externalSystem != null) {			
			sb.append(externalSystem.getName());
			sb.append(SEPERATOR);
			sb.append(externalSystem.getDescription());
			sb.append(SEPERATOR);
			sb.append(externalSystem.getVersion());
		}
		else
		{
			sb.append(name);
		}

		return sb.toString();        
    }
    
    public static String role(Role role) {
        
    	StringBuffer sb = new StringBuffer();
    	sb.append("Role:");
        String name = "<unknown>";
        
		if (role != null) {			
			sb.append(role.getName());
			sb.append(SEPERATOR);
			Set<Permission> permissionSet = role.getPermissions();
			
			if(permissionSet != null)
			{
				for(Permission perm : permissionSet)
				{
					sb.append(perm.getName());
					sb.append(SEPERATOR);
				}
			}
		}
		else
		{
			sb.append(name);
		}

		return sb.toString();        
    }   
    
	public static String activationCode(String activationCodeValue){
		StringBuffer sb = new StringBuffer();
		sb.append("code:");
		if(StringUtils.isBlank(activationCodeValue)){
			sb.append("<NO_CODE>");
		}else{
			sb.append(activationCodeValue);
		}
		String result = sb.toString();
		return result;
	}

	public static void customerUpdated(Customer customer){
		logEvent(customer,"Customer Updated");
		if(EacApp.isAdmin()){
		    logExternalCustomerIds(false, customer);
		}
    }

    private static void logExternalCustomerIds(boolean isSave, Customer customer) {
        try{
            if(canCallGet(customer) && canCallGet(customer.getExternalIds())) {
               logAddExternalCustomerIds(isSave, customer, customer.getExternalIds());
            }
        } catch (LazyInitializationException ex) {
            LOG.debug("failed to get external Ids for customer" + ex.getMessage());
        }
    }

	public static void logAddExternalCustomerId(Customer customer, ExternalCustomerId extCustId) {
		logAddExternalCustomerId(true, customer, extCustId);
	}
	
	public static void logAddExternalCustomerId(boolean isSave, Customer customer, ExternalCustomerId extCustId){
		String prefix = isSave ? "Save" : "Set";
		AuditLogger.logEvent(customer, prefix, ExternalCustomerId.getStringValue(extCustId));
	}
	
    public static void logAddExternalCustomerIds(boolean isSave, Customer customer, Set<ExternalCustomerId> extCustIds){
        if(customer != null && (extCustIds.isEmpty() == false)) {
        	for(ExternalCustomerId extCustId : extCustIds){
        		logAddExternalCustomerId(isSave, customer, extCustId);
        	}
        }
    }

	public static void customerSaved(Customer customer){
		logEvent(customer, "Customer Saved In EAC and erights");
		logExternalCustomerIds(true, customer);
    }

    private static boolean canCallGet(Object o) {
        boolean result = false;
        if (o != null) {
            if (o instanceof HibernateProxy) {
                HibernateProxy hp = (HibernateProxy) o;
                if (hp.getHibernateLazyInitializer().getSession() != null) {
                    result = true;
                }
            } else {
                result = true;
            }
        }
        return result;
    }
}
