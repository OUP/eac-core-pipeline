/**
 * 
 */
package com.oup.eac.data.impl.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.RegistrationDao;
import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductRegistration;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.RegistrationDefinition.RegistrationDefinitionType;
import com.oup.eac.dto.ReportCriteria;
import com.oup.eac.dto.ReportCriteria.RegistrationSelectionType;

/**
 * Get RegistrationHibernate instances.
 *  
 * @author harlandd
 * @author Ian Packard
 */
@Repository(value="registrationDao")
public class RegistrationHibernateDao extends HibernateBaseDao<Registration<? extends ProductRegistrationDefinition>, String> implements RegistrationDao {

    /**
     * @param sf
     *            the session factory
     */
	@Autowired
    public RegistrationHibernateDao(final SessionFactory sf) {
        super(sf);
    }
	
	/*@Override
	@SuppressWarnings("unchecked")
    public final List<Registration<ProductRegistrationDefinition>> getRegistrationByRegistrationDefinitionAndCustomer(final ProductRegistrationDefinition productRegistrationDefinition, final Customer customer) {
    	//HQL UNIONS DO NOT SEEM TO WORK SEE: https://hibernate.onjira.com/browse/HHH-1050    	
		List<Registration<ProductRegistrationDefinition>> registrations = getSession()
    													.createQuery("select distinct r from ProductRegistration r " +
										    			"join r.customer c " +
										    			"join r.registrationDefinition rd " +
										    			"where c = :customer " +
										    			"and rd = :productRegistrationDefinition")
										        		.setParameter("productRegistrationDefinition", productRegistrationDefinition)
										        		.setParameter("customer", customer)
										    			 	.list();
    	if(registrations != null && registrations.size() > 0) {
    		return registrations;
    	}
    	return  getSession().createQuery("select distinct r from ActivationCodeRegistration r " +
    											"join r.customer c " +
								    			"join r.registrationDefinition rd " +
								    			"where c = :customer " +
												"and rd = :productRegistrationDefinition")
												.setParameter("productRegistrationDefinition", productRegistrationDefinition)
												.setParameter("customer", customer)
								    			 	.list();
    }*/
 
	/*@Override
	@SuppressWarnings("unchecked")
	public final Registration<?> getRegistrationByIdInitialised(final String id) {
    	//HQL UNIONS DO NOT SEEM TO WORK SEE: https://hibernate.onjira.com/browse/HHH-1050
    	Registration<ProductRegistrationDefinition> registration = (Registration<ProductRegistrationDefinition>)getSession()
    													.createQuery("select distinct r from ProductRegistration r " +
										    			"join fetch r.customer c " +
										    			"join fetch r.registrationDefinition rd " +
										    			"join fetch rd.registrationActivation ra " +
										    			"join fetch rd.licenceTemplate lt " +
										    			"join fetch rd.product p " +
										    			"left join fetch p.linkedProducts lp " +
										    			"where r.id = :id ")
										    			 .setParameter("id", id)
										    			 	.uniqueResult();
    	if(registration != null) {
    		return registration;
    	}
    	return  (Registration<ProductRegistrationDefinition>)getSession().createQuery("select distinct r from ActivationCodeRegistration r " +
    											"join fetch r.customer c " +
								    			"join fetch r.registrationDefinition rd " +
								    			"join fetch rd.registrationActivation ra " +
								    			"join fetch rd.licenceTemplate lt " +
								    			"join fetch rd.product p " +
								    			"left join fetch r.activationCode ac " +
								    			"left join fetch ac.activationCodeBatch acb " +
								    			"left join fetch acb.licenceTemplate lt " +
								    			"left join fetch p.linkedProducts lp " +
								    			"where r.id = :id ")
								    			 .setParameter("id", id)
								    			 	.uniqueResult();
    } */
 
	@SuppressWarnings("unchecked")
	@Override
	public final Registration<ProductRegistrationDefinition> getRegistrationById(final String id) {
    	//HQL UNIONS DO NOT SEEM TO WORK SEE: https://hibernate.onjira.com/browse/HHH-1050
		Registration<ProductRegistrationDefinition> registration = (ProductRegistration)getSession()
    													.createQuery("select distinct r from ProductRegistration r " +
										    			"where r.id = :id ")
										    			 .setParameter("id", id)
										    			 	.uniqueResult();
    	if(registration != null) {
    		return registration;
    	}
    	return  (Registration<ProductRegistrationDefinition>)getSession().createQuery("select distinct r from ActivationCodeRegistration r " +
								    			"where r.id = :id ")
								    			 .setParameter("id", id)
								    			 	.uniqueResult();
    }
	
	/*@Override
	@SuppressWarnings("unchecked")
	public final Registration<ProductRegistrationDefinition> getRegistrationWithProductById(final String id) {
    	//HQL UNIONS DO NOT SEEM TO WORK SEE: https://hibernate.onjira.com/browse/HHH-1050
    	Registration<ProductRegistrationDefinition> registration = (Registration<ProductRegistrationDefinition>)getSession()
    													.createQuery("select distinct r from ProductRegistration r " +
    													"join fetch r.registrationDefinition rd " +
    													"join fetch rd.product p " +
										    			"where r.id = :id ")
										    			 .setParameter("id", id)
										    			 	.uniqueResult();
    	if(registration != null) {
    		return registration;
    	}
    	return  (Registration<ProductRegistrationDefinition>)getSession().createQuery("select distinct r from ActivationCodeRegistration r " +
												"join fetch r.registrationDefinition rd " +
												"join fetch rd.product p " +
								    			"where r.id = :id ")
								    			 .setParameter("id", id)
								    			 	.uniqueResult();
    }*/

    /**
     * Get registrations by date and owner.
     * 
     * @see com.oup.eac.data.RegistrationDao#getRegistrationsByDateAndDivision(java.util.Date, java.util.Date, java.lang.String)
     * @param fromDate
     *            start period
     * @param toDate
     *            end period
     * @param productOwner
     *            owner
     * @return list of registrations
     */
    @Override
    @SuppressWarnings("unchecked")
    public final List<Object[]> getRegistrationsByDateAndDivision(final DateTime fromDate, 
    		final DateTime toDate, final String divisionType, final String rsDBSchema) {
        /*return getSession().createQuery(
                "select distinct rg from Registration rg "
        		+ "join fetch rg.registrationDefinition rd " 
        		+ "join fetch rd.pageDefinition pd "
        		+ "join fetch rd.product pd " 
        		+ "join fetch rg.customer cs "
        		+ "left join fetch cs.answers a "
        		+ "left join fetch a.question q "
                + "where rg.updatedDate between :fromDate and :toDate " 
                + "and pd.division.divisionType = :divisionType "
                + "and rg.activated = :activated " 
                + "order by rg.createdDate ")
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate)
                .setParameter("divisionType", divisionType)
                .setParameter("activated", Boolean.TRUE)
                .list();*/
    	return getSession().createSQLQuery("SELECT DISTINCT Cast( ERSMD_PRODUCT_INFO_DATA.product_id AS VARCHAR(510))            AS PRODUCT_ID,\n" + 
    			"                cast(ERS_PRODUCTS.display_name AS VARCHAR(510))                                           AS PRODUCT_NAME,\n" + 
    			"                Cast( ERSMD_USER_INFO_DATA.user_id AS VARCHAR(510))                  AS USER_ID,\n" + 
    			"                cast(ERS_CRED_LOGIN_PASSWORD.login AS VARCHAR(510))                                       AS USERNAME,\n" + 
    			"                Cast( ERSMD_USER_INFO_DATA.firstname AS VARCHAR(510))                AS FIRST_NAME,\n" + 
    			"                ERSMD_USER_INFO_DATA.lastname                                        AS LAST_NAME,\n" + 
    			"                ERSMD_USER_INFO_DATA.email                                           AS EMAIL_ADDRESS,\n" + 
    			"                Cast( ERSMD_USER_INFO_DATA.email_verification_state AS VARCHAR(510)) AS EMAIL_VERIFICATION_STATE,\n" + 
    			"                Cast( ERSMD_USER_INFO_DATA.locale AS VARCHAR(510))                   AS LOCALE,\n" + 
    			"                ERSMD_LICENSE_INFO_DATA.denied                                       AS DENIED,\n" + 
    			"                ERSMD_LICENSE_INFO_DATA.awaiting_validation                          AS PENDING,\n" + 
    			"                ERSMD_LICENSE_INFO_DATA.completed                                    AS ACTIVATED,\n" + 
    			"                ERSMD_PRODUCT_INFO_DATA.registration_definition_type                 AS REGISTRATION_TYPE,\n" + 
    			"                ERS_CRED_LOGIN_PASSWORD.date_login_succeeded                         AS LAST_LOGIN,\n" + 
    			"                ERS_USERS.date_created                                               AS USER_CREATED_DATE,\n" + 
    			"                ERS_LICENSES.date_created                                            AS LICENSE_CREATED_DATE,\n" + 
    			"                Isnull(ERSMD_LICENSE_INFO_DATA.modified_date, ERS_LICENSES.date_modified) AS LICENSE_UPDATED_DATE,\n" + 
    			"                usages.used_count\n" + 
    			"FROM   " + rsDBSchema + ".ERS_LICENSES AS ers_licenses\n" + 
    			"       join " + rsDBSchema + ".ERSMD_LICENSE_INFO_DATA AS ersmd_license_info_data\n" + 
    			"         ON ERS_LICENSES.license_id = ERSMD_LICENSE_INFO_DATA.license_id\n" + 
    			"       left outer join ( SELECT license_id,\n" + 
    			"                                Count( ERS_LICENSE_USAGES.license_id ) AS USED_COUNT,\n" + 
    			"                                Max( ERS_LICENSE_USAGES.date_used )    AS LAST_USE\n" + 
    			"                         FROM   " + rsDBSchema + ".ERS_LICENSE_USAGES AS ers_license_usages\n" + 
    			"                         GROUP  BY ERS_LICENSE_USAGES.license_id ) AS usages\n" + 
    			"                    ON usages.license_id = ERS_LICENSES.license_id\n" + 
    			"       join " + rsDBSchema + ".ERS_LICENSE_PRODUCT_LINK AS ers_license_product_link\n" + 
    			"         ON ERS_LICENSES.license_id = ERS_LICENSE_PRODUCT_LINK.license_id\n" + 
    			"       join " + rsDBSchema + ".ERS_LICENSE_PARAMETERS AS ers_license_parameters\n" + 
    			"         ON ERS_LICENSES.license_id = ERS_LICENSE_PARAMETERS.license_id\n" + 
    			"       left join " + rsDBSchema + ".ERS_LICENSE_CUSTOMER_STATE AS ers_license_customer_state\n" + 
    			"              ON ERS_LICENSES.license_id = ERS_LICENSE_CUSTOMER_STATE.license_id\n" + 
    			"       join " + rsDBSchema + ".ERS_PRODUCTS AS ers_products\n" + 
    			"         ON ERS_LICENSE_PRODUCT_LINK.product_id = ERS_PRODUCTS.product_id\n" + 
    			"       join " + rsDBSchema + ".ERSMD_PRODUCT_INFO_DATA AS ersmd_product_info_data\n" + 
    			"         ON ERS_PRODUCTS.product_id = ERSMD_PRODUCT_INFO_DATA.product_id\n" + 
    			"       join " + rsDBSchema + ".ERSMD_DIVISION_LOOKUP AS ersmd_division_lookup\n" + 
    			"         ON ERSMD_PRODUCT_INFO_DATA.division_id = ERSMD_DIVISION_LOOKUP.division_id\n" + 
    			"       join " + rsDBSchema + ".ERS_CUSTOMERS AS ers_customers\n" + 
    			"         ON ERS_CUSTOMERS.customer_id = ERS_LICENSES.customer_id\n" + 
    			"       join " + rsDBSchema + ".ERS_USERS AS ers_users\n" + 
    			"         ON ERS_CUSTOMERS.target_id = ERS_USERS.user_id\n" + 
    			"       join " + rsDBSchema + ".ERSMD_USER_INFO_DATA AS ersmd_user_info_data\n" + 
    			"         ON ERS_USERS.user_id = ERSMD_USER_INFO_DATA.user_id\n" + 
    			"       join " + rsDBSchema + ".ERS_AUTH_PROFILES AS ers_auth_profiles\n" + 
    			"         ON ERS_USERS.user_id = ERS_AUTH_PROFILES.user_id\n" + 
    			"       join " + rsDBSchema + ".ERS_CREDENTIALS AS ers_credentials\n" + 
    			"         ON ERS_AUTH_PROFILES.profile_id = ERS_CREDENTIALS.profile_id\n" + 
    			"       join " + rsDBSchema + ".ERS_CRED_LOGIN_PASSWORD AS ers_cred_login_password\n" + 
    			"         ON ERS_CREDENTIALS.credential_id = ERS_CRED_LOGIN_PASSWORD.credential_id\n" + 
    			"WHERE  ERS_PRODUCTS.date_suspended IS NULL AND\n" + 
    			"       ERS_USERS.date_suspended IS NULL AND\n" + 
    			"       ERS_PRODUCTS.product_id IS NOT NULL AND\n" + 
    			"       Isnull( usages.used_count, ERS_LICENSE_PARAMETERS.num1 ) <= ERS_LICENSE_PARAMETERS.num1 AND\n" + 
    			"       ERSMD_LICENSE_INFO_DATA.completed = 1 AND\n" + 
    			"       ERS_LICENSE_PARAMETERS.enabled = 1 AND\n" + 
    			"       ERS_LICENSES.date_paused IS NULL AND\n" + 
    			"       Isnull( ERS_LICENSE_PARAMETERS.start_date, Getdate( ) ) <= Getdate( ) AND\n" + 
    			"       Isnull( ERS_LICENSE_PARAMETERS.end_date, Getdate( ) ) >= Getdate( ) AND\n" + 
    			"		Isnull(ersmd_license_info_data.modified_date, ERS_LICENSES.date_modified) BETWEEN :fromDate AND :toDate AND\n" + 
    			"		ERSMD_DIVISION_LOOKUP.division_type = :divisionType")
                .setParameter("fromDate", fromDate.toGregorianCalendar().getTime())
                .setParameter("toDate", toDate.toGregorianCalendar().getTime())
                .setParameter("divisionType", divisionType)
                .list();
    }
    
    /**
     * Get registrations by date and owner.
     * 
     * @see com.oup.eac.data.RegistrationDao#getRegistrationsByDateAndDivision(java.util.Date, java.util.Date, java.lang.String)
     * @param fromDate
     *            start period
     * @param toDate
     *            end period
     * @param productOwner
     *            owner
     * @return list of registrations
     */
    @Override
    @SuppressWarnings("unchecked")
	public List<Object[]> getRegistrationsByDateAndDivisionAndAccount(
			DateTime fromDate, DateTime toDate, final String divisionType, int batchSize, int firstRecord, final String rsDBSchema) {
        /*return getSession().createQuery(
                "select distinct rg from Registration rg "
        		+ "join fetch rg.registrationDefinition rd " 
        		+ "join fetch rd.pageDefinition pd "
        		+ "join fetch rd.product p " 
        		+ "join fetch rg.customer cs "
                + "where ((rg.updatedDate between :fromDate and :toDate) "
                + "or (cs.updatedDate between :fromDate and :toDate)) " 
                + "and pd.division.divisionType = :divisionType "
                + "and rg.activated = :activated " 
                + "order by rg.id ")
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate)
                .setParameter("divisionType", divisionType)
                .setParameter("activated", Boolean.TRUE)
                .setFirstResult((firstRecord - 1) * batchSize)
                .setMaxResults(batchSize)
                .list();*/
    	return getSession().createSQLQuery("SELECT DISTINCT Cast( ERSMD_PRODUCT_INFO_DATA.product_id AS VARCHAR(510))            AS PRODUCT_ID,\n" + 
    			"                cast(ERS_PRODUCTS.display_name AS VARCHAR(510))                                            AS PRODUCT_NAME,\n" + 
    			"                Cast( ERSMD_USER_INFO_DATA.user_id AS VARCHAR(510))                  AS USER_ID,\n" + 
    			"                cast(ERS_CRED_LOGIN_PASSWORD.login AS VARCHAR(510))                                       AS USERNAME,\n" + 
    			"                Cast( ERSMD_USER_INFO_DATA.firstname AS VARCHAR(510))                AS FIRST_NAME,\n" + 
    			"                ERSMD_USER_INFO_DATA.lastname                                        AS LAST_NAME,\n" + 
    			"                ERSMD_USER_INFO_DATA.email                                           AS EMAIL_ADDRESS,\n" + 
    			"                Cast( ERSMD_USER_INFO_DATA.email_verification_state AS VARCHAR(510)) AS EMAIL_VERIFICATION_STATE,\n" + 
    			"                Cast( ERSMD_USER_INFO_DATA.locale AS VARCHAR(510))                   AS LOCALE,\n" + 
    			"                ERSMD_LICENSE_INFO_DATA.denied                                       AS DENIED,\n" + 
    			"                ERSMD_LICENSE_INFO_DATA.awaiting_validation                          AS PENDING,\n" + 
    			"                ERSMD_LICENSE_INFO_DATA.completed                                    AS ACTIVATED,\n" + 
    			"                ERSMD_PRODUCT_INFO_DATA.registration_definition_type                 AS REGISTRATION_TYPE,\n" + 
    			"                ERS_CRED_LOGIN_PASSWORD.date_login_succeeded                         AS LAST_LOGIN,\n" + 
    			"                ERS_USERS.date_created                                               AS USER_CREATED_DATE,\n" + 
    			"                ERS_LICENSES.date_created                                            AS LICENSE_CREATED_DATE,\n" + 
    			"                Isnull(ERSMD_LICENSE_INFO_DATA.modified_date, ERS_LICENSES.date_modified) AS LICENSE_UPDATED_DATE,\n" + 
    			"                usages.used_count\n" + 
    			"FROM   " + rsDBSchema + ".ERS_LICENSES AS ers_licenses\n" + 
    			"       join " + rsDBSchema + ".ERSMD_LICENSE_INFO_DATA AS ersmd_license_info_data\n" + 
    			"         ON ERS_LICENSES.license_id = ERSMD_LICENSE_INFO_DATA.license_id\n" + 
    			"       left outer join ( SELECT license_id,\n" + 
    			"                                Count( ERS_LICENSE_USAGES.license_id ) AS USED_COUNT,\n" + 
    			"                                Max( ERS_LICENSE_USAGES.date_used )    AS LAST_USE\n" + 
    			"                         FROM   " + rsDBSchema + ".ERS_LICENSE_USAGES AS ers_license_usages\n" + 
    			"                         GROUP  BY ERS_LICENSE_USAGES.license_id ) AS usages\n" + 
    			"                    ON usages.license_id = ERS_LICENSES.license_id\n" + 
    			"       join " + rsDBSchema + ".ERS_LICENSE_PRODUCT_LINK AS ers_license_product_link\n" + 
    			"         ON ERS_LICENSES.license_id = ERS_LICENSE_PRODUCT_LINK.license_id\n" + 
    			"       join " + rsDBSchema + ".ERS_LICENSE_PARAMETERS AS ers_license_parameters\n" + 
    			"         ON ERS_LICENSES.license_id = ERS_LICENSE_PARAMETERS.license_id\n" + 
    			"       left join " + rsDBSchema + ".ERS_LICENSE_CUSTOMER_STATE AS ers_license_customer_state\n" + 
    			"              ON ERS_LICENSES.license_id = ERS_LICENSE_CUSTOMER_STATE.license_id\n" + 
    			"       join " + rsDBSchema + ".ERS_PRODUCTS AS ers_products\n" + 
    			"         ON ERS_LICENSE_PRODUCT_LINK.product_id = ERS_PRODUCTS.product_id\n" + 
    			"       join " + rsDBSchema + ".ERSMD_PRODUCT_INFO_DATA AS ersmd_product_info_data\n" + 
    			"         ON ERS_PRODUCTS.product_id = ERSMD_PRODUCT_INFO_DATA.product_id\n" + 
    			"       join " + rsDBSchema + ".ERSMD_DIVISION_LOOKUP AS ersmd_division_lookup\n" + 
    			"         ON ERSMD_PRODUCT_INFO_DATA.division_id = ERSMD_DIVISION_LOOKUP.division_id\n" + 
    			"       join " + rsDBSchema + ".ERS_CUSTOMERS AS ers_customers\n" + 
    			"         ON ERS_CUSTOMERS.customer_id = ERS_LICENSES.customer_id\n" + 
    			"       join " + rsDBSchema + ".ERS_USERS AS ers_users\n" + 
    			"         ON ERS_CUSTOMERS.target_id = ERS_USERS.user_id and ERS_CUSTOMERS.TARGET_TYPE = 2\n" + 
    			"       join " + rsDBSchema + ".ERSMD_USER_INFO_DATA AS ersmd_user_info_data\n" + 
    			"         ON ERS_USERS.user_id = ERSMD_USER_INFO_DATA.user_id\n" + 
    			"       join " + rsDBSchema + ".ERS_AUTH_PROFILES AS ers_auth_profiles\n" + 
    			"         ON ERS_USERS.user_id = ERS_AUTH_PROFILES.user_id\n" + 
    			"       join " + rsDBSchema + ".ERS_CREDENTIALS AS ers_credentials\n" + 
    			"         ON ERS_AUTH_PROFILES.profile_id = ERS_CREDENTIALS.profile_id\n" + 
    			"       join " + rsDBSchema + ".ERS_CRED_LOGIN_PASSWORD AS ers_cred_login_password\n" + 
    			"         ON ERS_CREDENTIALS.credential_id = ERS_CRED_LOGIN_PASSWORD.credential_id\n" + 
    			"WHERE  ERS_PRODUCTS.date_suspended IS NULL AND\n" + 
    			"       ERS_USERS.date_suspended IS NULL AND\n" + 
    			"       ERS_PRODUCTS.product_id IS NOT NULL AND\n" + 
    			"       Isnull( usages.used_count, ERS_LICENSE_PARAMETERS.num1 ) <= ERS_LICENSE_PARAMETERS.num1 AND\n" + 
    			"       ERSMD_LICENSE_INFO_DATA.completed = 1 AND\n" + 
    			"       ERS_LICENSE_PARAMETERS.enabled = 1 AND\n" + 
    			"       ERS_LICENSES.date_paused IS NULL AND\n" + 
    			"       Isnull( ERS_LICENSE_PARAMETERS.start_date, Getdate( ) ) <= Getdate( ) AND\n" + 
    			"       Isnull( ERS_LICENSE_PARAMETERS.end_date, Getdate( ) ) >= Getdate( ) AND\n" + 
    			"		Isnull(ersmd_license_info_data.modified_date, ERS_LICENSES.date_modified) BETWEEN :fromDate AND :toDate AND\n" + 
    			"		ERSMD_DIVISION_LOOKUP.division_type = :divisionType")
                .setParameter("fromDate", fromDate.toGregorianCalendar().getTime())
                .setParameter("toDate", toDate.toGregorianCalendar().getTime())
                .setParameter("divisionType", divisionType)
                .setFirstResult((firstRecord - 1) * batchSize)
                .setMaxResults(batchSize)
                .list();
    }
    
    @Override
    public final List<Object[]> getReportRegistrationsCount(final ReportCriteria reportCriteria, String rsDBSchema) {
        SQLQuery query = getSession().createSQLQuery(buildQueryStr(reportCriteria, rsDBSchema, true));
        addParameters(query, reportCriteria);
        return query.list();     
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public final List<Object[]> getReportRegistrations(final ReportCriteria reportCriteria, String rsDBSchema) {
        SQLQuery query = getSession().createSQLQuery(buildQueryStr(reportCriteria, rsDBSchema,false));
        addParameters(query, reportCriteria);        
        //return query.setMaxResults(reportCriteria.getMaxResults()).list();
        /*query.setFirstResult(reportCriteria.getStartIndex()-1);
        query.setMaxResults(reportCriteria.getMaxResults());*/
        return query.list();
        
    }
    
    private String addParameters(final Query query, final ReportCriteria reportCriteria) {
        /*query.setParameter("completed_1", Boolean.TRUE);*/
        if(StringUtils.isNotBlank(reportCriteria.getDivisionId())) {
            query.setParameter("divisionId", reportCriteria.getDivisionId());
        }
        if(StringUtils.isNotBlank(reportCriteria.getPlatformCode())) {
            query.setParameter("platformCode", reportCriteria.getPlatformCode());
        }
        if(StringUtils.isNotBlank(reportCriteria.getProductId())) {
            query.setParameter("productId", reportCriteria.getProductId());
        }
        if(StringUtils.isNotBlank(reportCriteria.getCustomerUsername())) {
            query.setParameter("username", reportCriteria.getCustomerUsername());
        }
        if(reportCriteria.getLocale() != null) {
            query.setParameter("locale", reportCriteria.getLocale().toString());
        }
        if(StringUtils.isNotBlank(reportCriteria.getTimeZone())) {
            query.setParameter("timeZone", reportCriteria.getTimeZone());
        }
        if(reportCriteria.geteMailVarificationState() != null) {
            query.setParameter("emailVerificationState", reportCriteria.geteMailVarificationState().toString().toUpperCase());
        }
        if(reportCriteria.getExternalProductId() != null) {
            query.setParameter("externalCustomerId", reportCriteria.getExternalProductId());
        }
        if(reportCriteria.getExternalProductId() != null && StringUtils.isNotBlank(reportCriteria.getProductExternalSystemIdType())) {
        	query.setParameter("productExternalSystemIdType", reportCriteria.getProductExternalSystemIdType());
        }
        //for last login date
        if(reportCriteria.getLastLoginFromDate() == null) {
            if(reportCriteria.getLastLoginToDate() != null) {
                query.setParameter("lastLoginToDate", reportCriteria.getLastLoginToDate().toGregorianCalendar().getTime());
            }
        } else {
            query.setParameter("lastLoginFromDate", reportCriteria.getLastLoginFromDate().toGregorianCalendar().getTime());
            if(reportCriteria.getLastLoginToDate() == null) {
                query.setParameter("lastLoginFromDate", reportCriteria.getLastLoginFromDate().toGregorianCalendar().getTime());
            } else {
                query.setParameter("lastLoginToDate", reportCriteria.getLastLoginToDate().toGregorianCalendar().getTime());
            } 
        }
        
        // for customer created date
        if(reportCriteria.getCustomerCreatedFromDate() == null) {
            if(reportCriteria.getCustomerCreatedToDate() != null) {
                query.setParameter("customerCreatedToDate", reportCriteria.getCustomerCreatedToDate().toGregorianCalendar().getTime());
            }
        } else {
            query.setParameter("customerCreatedFromDate", reportCriteria.getCustomerCreatedFromDate().toGregorianCalendar().getTime());
            if(reportCriteria.getCustomerCreatedToDate() == null) {
                query.setParameter("customerCreatedFromDate", reportCriteria.getCustomerCreatedFromDate().toGregorianCalendar().getTime());
            } else {
                query.setParameter("customerCreatedToDate", reportCriteria.getCustomerCreatedToDate().toGregorianCalendar().getTime());
            } 
        }
        
      //for reg. created date
        if(reportCriteria.getRegistrationCreatedFromDate() == null) {
            if(reportCriteria.getRegistrationCreatedToDate() != null) {
                query.setParameter("RegistrationCreatedToDate", reportCriteria.getRegistrationCreatedToDate().toGregorianCalendar().getTime());
            }
        } else {
            query.setParameter("RegistrationCreatedFromDate", reportCriteria.getRegistrationCreatedFromDate().toGregorianCalendar().getTime());
            if(reportCriteria.getRegistrationCreatedToDate() == null) {
                query.setParameter("RegistrationCreatedFromDate", reportCriteria.getRegistrationCreatedFromDate().toGregorianCalendar().getTime());
            } else {
                query.setParameter("RegistrationCreatedToDate", reportCriteria.getRegistrationCreatedToDate().toGregorianCalendar().getTime());
            } 
        }
        
        //for reg. updated date
        if(reportCriteria.getFromDate() == null) {
            if(reportCriteria.getToDate() != null) {
                query.setParameter("toDate", reportCriteria.getToDate().toGregorianCalendar().getTime());
            }
        } else {
            query.setParameter("fromDate", reportCriteria.getFromDate().toGregorianCalendar().getTime());
            if(reportCriteria.getToDate() == null) {
                query.setParameter("fromDate", reportCriteria.getFromDate().toGregorianCalendar().getTime());
            } else {
                query.setParameter("toDate", reportCriteria.getToDate().toGregorianCalendar().getTime());
            } 
        }
        /*if(reportCriteria.isDenied()) {
            query.setParameter("denied_1", Boolean.TRUE);
        }
        if(reportCriteria.isActivated()) {
            query.setParameter("denied_2", Boolean.FALSE);
            query.setParameter("activated_2", Boolean.TRUE);
        }
        if(reportCriteria.isPending()) {
            query.setParameter("denied_3", Boolean.FALSE);
            query.setParameter("activated_3", Boolean.FALSE);
        }*/
        return query.toString();
    }
     
    private String buildQueryStr(final ReportCriteria reportCriteria, String rsDBSchema, boolean count) {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(buildQuery(reportCriteria,rsDBSchema ,count));
        queryStr.append(buildWhereClause(reportCriteria,rsDBSchema));
        if(!count) {
        	queryStr.append(buildOrderBy(reportCriteria, count));
        }
        return queryStr.toString();
    }
    
    private String buildQuery(final ReportCriteria reportCriteria,String rsDBSchema, boolean count) {
        StringBuilder query = new StringBuilder();
	query.append("SELECT DISTINCT ERS_LICENSES.LICENSE_ID,"
			+ " CAST(ERSMD_PRODUCT_INFO_DATA.PRODUCT_ID AS VARCHAR(510))AS PRODUCT_ID,"
			+ " CAST(ERS_PRODUCTS.DISPLAY_NAME AS VARCHAR(510)) AS PRODUCT_NAME,"
			+ " CAST(ERSMD_USER_INFO_DATA.USER_ID AS VARCHAR(510)) AS USER_ID,"
			+ " CAST(ERS_CRED_LOGIN_PASSWORD.LOGIN AS VARCHAR(510)) AS USERNAME," 
			+ " CAST(ERSMD_USER_INFO_DATA.FIRSTNAME AS VARCHAR(510)) AS FIRST_NAME,"
			+ " ERSMD_USER_INFO_DATA.LASTNAME AS LAST_NAME,"
			+ " ERSMD_USER_INFO_DATA.EMAIL AS EMAIL_ADDRESS,"
			+ " CAST(ERSMD_USER_INFO_DATA.EMAIL_VERIFICATION_STATE AS VARCHAR(510)) AS EMAIL_VERIFICATION_STATE,"
			+ " CAST(ERSMD_USER_INFO_DATA.LOCALE AS VARCHAR(510))AS LOCALE,"
	/*10*/	+ " CAST(ERSMD_USER_INFO_DATA.TIME_ZONE AS VARCHAR(510)) AS TIME_ZONE,"
			+ " CAST(ERS_CLAIM_FOLDERS.DISPLAY_NAME  AS VARCHAR(510)) AS BATCH_NAME,"
			+ " CAST(ERS_CLAIM_TICKET.CLAIM_CODE  AS VARCHAR(510)) AS ACTIVATION_CODE,"
			+ " ERSMD_LICENSE_INFO_DATA.DENIED AS DENIED,"
			+ " ERSMD_LICENSE_INFO_DATA.AWAITING_VALIDATION AS PENDING,"
			+ " ERSMD_LICENSE_INFO_DATA.COMPLETED AS ACTIVATED,"
			+ " ERSMD_PRODUCT_INFO_DATA.REGISTRATION_DEFINITION_TYPE AS REGISTRATION_TYPE,"
			+ " ERS_CRED_LOGIN_PASSWORD.DATE_LOGIN_SUCCEEDED AS LAST_LOGIN,"
			+ " ERS_USERS.DATE_CREATED AS USER_CREATED_DATE,"
			+ " ERS_LICENSES.DATE_CREATED AS LICENSE_CREATED_DATE," 
			+ " ERS_LICENSES.DATE_MODIFIED AS LICENSE_UPDATED_DATE,"
			+ " ERS_LICENSES.LICENSE_TYPE AS LICENSE_TYPE,"
	/*	22*/+ " ERS_LICENSES.DATE_PAUSED AS LICENSE_PAUSED,"
			+ " ERS_LICENSE_PARAMETERS.START_DATE AS LICENSE_START_DATE,"
			+ " ERS_LICENSE_PARAMETERS.END_DATE AS LICENSE_END_DATE,"
			+ " ERS_LICENSE_PARAMETERS.ENABLED AS LICENSE_ENABLED,"
			+ " ERS_LICENSE_PARAMETERS.NUM1 AS LICENSE_NUM1,"
			+ " ERS_LICENSE_PARAMETERS.NUM2 AS LICENSE_NUM2,"
			+ " ERS_LICENSE_PARAMETERS.NUM3 AS LICENSE_NUM3,"
			+ " ERS_PRODUCTS.DATE_SUSPENDED AS PRODUCT_DATE_SUSPENDED,"
			+ " ERS_USERS.DATE_SUSPENDED AS USER_DATE_SUSPENDED,"
			+ " ERS_LICENSE_CUSTOMER_STATE.FIRST_USE AS LICENSE_FIRST_USE ,"
			+ " USAGES.USED_COUNT AS LICENSE_USED_COUNT,"
			+ " USAGES.LAST_USE AS LICENSE_LAST_USE,"
			+ " CAST(ERS_PRODUCTS.DISPLAY_NAME  AS VARCHAR(510)) AS DUMMY_FIELD "
			+ " FROM " + rsDBSchema + ".ERS_LICENSES AS ERS_LICENSES ");
        query.append("JOIN " + rsDBSchema + ".ERSMD_LICENSE_INFO_DATA AS ERSMD_LICENSE_INFO_DATA"
			+ " ON ERS_LICENSES.LICENSE_ID = ERSMD_LICENSE_INFO_DATA.LICENSE_ID"
			+ " LEFT OUTER JOIN ("
			+ " SELECT LICENSE_ID,COUNT(ERS_LICENSE_USAGES.LICENSE_ID) AS USED_COUNT," 
			+ " MAX(ERS_LICENSE_USAGES.DATE_USED) AS LAST_USE "
			+ " FROM "+ rsDBSchema + ".ERS_LICENSE_USAGES AS ERS_LICENSE_USAGES"
			+ " GROUP BY ERS_LICENSE_USAGES.LICENSE_ID" 
			+ " ) AS USAGES "
			+ " ON USAGES.LICENSE_ID = ERS_LICENSES.LICENSE_ID"
			+ " JOIN " + rsDBSchema + ".ERS_LICENSE_PRODUCT_LINK AS ERS_LICENSE_PRODUCT_LINK"
			+ " ON ERS_LICENSES.LICENSE_ID = ERS_LICENSE_PRODUCT_LINK.LICENSE_ID"
			+ " JOIN " + rsDBSchema + ".ERS_LICENSE_PARAMETERS AS ERS_LICENSE_PARAMETERS"
			+ " ON ERS_LICENSES.LICENSE_ID = ERS_LICENSE_PARAMETERS.LICENSE_ID"
			+ " LEFT JOIN " + rsDBSchema + ".ERS_LICENSE_CUSTOMER_STATE AS ERS_LICENSE_CUSTOMER_STATE"
			+ " ON ERS_LICENSES.LICENSE_ID = ERS_LICENSE_CUSTOMER_STATE.LICENSE_ID"
			+ " JOIN " + rsDBSchema + ".ERS_PRODUCTS AS ERS_PRODUCTS"
			+ " ON ERS_LICENSE_PRODUCT_LINK.PRODUCT_ID = ERS_PRODUCTS.PRODUCT_ID"
			+ " JOIN " + rsDBSchema + ".ERSMD_PRODUCT_INFO_DATA AS ERSMD_PRODUCT_INFO_DATA"
			+ " ON ERS_PRODUCTS.PRODUCT_ID = ERSMD_PRODUCT_INFO_DATA.PRODUCT_ID"
			+ " LEFT JOIN " + rsDBSchema + ".ERSMD_DIVISION_LOOKUP AS ERSMD_DIVISION_LOOKUP"
			+ " ON ERSMD_PRODUCT_INFO_DATA.DIVISION_ID = ERSMD_DIVISION_LOOKUP.DIVISION_ID"
			+ " JOIN " + rsDBSchema + ".ERS_CUSTOMERS AS ERS_CUSTOMERS"
			+ " ON ERS_CUSTOMERS.CUSTOMER_ID = ERS_LICENSES.CUSTOMER_ID AND ERS_CUSTOMERS.TARGET_TYPE = 2"
			+ " JOIN " + rsDBSchema + ".ERS_USERS AS ERS_USERS"
			+ " ON ERS_CUSTOMERS.TARGET_ID = ERS_USERS.USER_ID"
			+ " JOIN " + rsDBSchema + ".ERSMD_USER_INFO_DATA AS ERSMD_USER_INFO_DATA"
			+ " ON ERS_USERS.USER_ID = ERSMD_USER_INFO_DATA.USER_ID"
			+ " JOIN " + rsDBSchema + ".ERS_AUTH_PROFILES AS ERS_AUTH_PROFILES"
			+ " ON ERS_USERS.USER_ID = ERS_AUTH_PROFILES.USER_ID"
			+ " JOIN " + rsDBSchema + ".ERS_CREDENTIALS AS ERS_CREDENTIALS"
			+ " ON ERS_AUTH_PROFILES.PROFILE_ID = ERS_CREDENTIALS.PROFILE_ID"
			+ " JOIN " + rsDBSchema + ".ERS_CRED_LOGIN_PASSWORD AS ERS_CRED_LOGIN_PASSWORD"
			+ " ON ERS_CREDENTIALS.CREDENTIAL_ID = ERS_CRED_LOGIN_PASSWORD.CREDENTIAL_ID"
			+ " LEFT JOIN " + rsDBSchema + ".ERSMD_PRODUCT_EXTERNAL_IDENTIFIER_INFO_DATA AS ERSMD_PRODUCT_EXTERNAL_IDENTIFIER_INFO_DATA"
			+ " ON ERS_PRODUCTS.PRODUCT_ID = ERSMD_PRODUCT_EXTERNAL_IDENTIFIER_INFO_DATA.PRODUCT_ID"
			+ " LEFT JOIN " + rsDBSchema + ".ERSMD_EXTERNAL_SYSTEM_ID_TYPE_LOOKUP AS ERSMD_EXTERNAL_SYSTEM_ID_TYPE_LOOKUP"
			+ " ON ERSMD_PRODUCT_EXTERNAL_IDENTIFIER_INFO_DATA.EXTERNAL_SYSTEM_ID_TYPE_ID = ERSMD_EXTERNAL_SYSTEM_ID_TYPE_LOOKUP.ID"
			+ " LEFT JOIN " + rsDBSchema + ".ERSMD_EXTERNAL_SYSTEM_LOOKUP AS ERSMD_EXTERNAL_SYSTEM_LOOKUP"
			+ " ON ERSMD_EXTERNAL_SYSTEM_ID_TYPE_LOOKUP.EXTERNAL_SYSTEM_ID = ERSMD_EXTERNAL_SYSTEM_LOOKUP.ID"
			+ " LEFT JOIN " + rsDBSchema + ".ERS_CLAIM_LICENSE_LINK AS ERS_CLAIM_LICENSE_LINK"
			+ " ON ERS_LICENSES.LICENSE_ID = ERS_CLAIM_LICENSE_LINK.LICENSE_ID"
			+ " LEFT JOIN " + rsDBSchema + ".ERS_CLAIMS AS ERS_CLAIMS"
			+ " ON ERS_CLAIM_LICENSE_LINK.CLAIM_ID = ERS_CLAIMS.CLAIM_ID"
			+ " LEFT JOIN " + rsDBSchema + ".ERS_CLAIM_TICKET AS ERS_CLAIM_TICKET"
			+ " ON ERS_CLAIMS.CLAIM_TICKET_ID = ERS_CLAIM_TICKET.CLAIM_TICKET_ID"
			+ " LEFT JOIN " + rsDBSchema + ".ERS_CLAIM_FOLDERS AS ERS_CLAIM_FOLDERS"
			+ " ON ERS_CLAIM_TICKET.CLAIM_FOLDER_ID = ERS_CLAIM_FOLDERS.CLAIM_FOLDER_ID ") ;
        return query.toString();
    }
    
    private void addJoin(StringBuilder selectBuilder, String select, boolean count, String joinStr) {
        selectBuilder.append(joinStr).append(" ");
        if(!count) {
            selectBuilder.append("fetch ");
        }
        selectBuilder.append(select);
    }

    private String buildOrderBy(final ReportCriteria reportCriteria, boolean count) {
        StringBuilder orderByClause = new StringBuilder();
        if(!count) {
            orderByClause.append("ORDER BY ERS_LICENSES.DATE_MODIFIED DESC ");
        }
        return orderByClause.toString();
    }
    
    private String buildWhereClause(final ReportCriteria reportCriteria, final String rsDBSchema) {
        
        StringBuilder whereClause = new StringBuilder();
        addWhereClause(whereClause, " ERS_LICENSES.date_deleted is null ");
        if(StringUtils.isNotBlank(reportCriteria.getDivisionId())) {
            addWhereClause(whereClause, "ERSMD_DIVISION_LOOKUP.DIVISION_ID = :divisionId ");
        }
        
        if(StringUtils.isNotBlank(reportCriteria.getPlatformCode())) {
        	addWhereClause(whereClause, "ERS_PRODUCTS.PRODUCT_ID IN ( SELECT ERSMD_PLATFORM_PRODUCT_MAPPING.PRODUCT_ID "
        			+ "FROM " + rsDBSchema + ".ERSMD_PLATFORM_PRODUCT_MAPPING AS ERSMD_PLATFORM_PRODUCT_MAPPING "
        			+ "JOIN " + rsDBSchema + ".ERSMD_PLATFORM_MASTER_LOOKUP AS ERSMD_PLATFORM_MASTER_LOOKUP "
        			+ "ON ERSMD_PLATFORM_PRODUCT_MAPPING.PLATFORM_ID = ERSMD_PLATFORM_MASTER_LOOKUP.PLATFORM_ID "
        			+ "AND ERSMD_PLATFORM_MASTER_LOOKUP.PLATFORM_CODE = :platformCode ) ");
        }
        
        if(StringUtils.isNotBlank(reportCriteria.getProductId())) {
            addWhereClause(whereClause, "ERSMD_PRODUCT_INFO_DATA.PRODUCT_ID = :productId ");
        }
        if(StringUtils.isNotBlank(reportCriteria.getCustomerUsername())) {
            addWhereClause(whereClause, "ERS_CRED_LOGIN_PASSWORD.LOGIN = :username ");
        }        
        if(reportCriteria.getLocale() != null) {
            addWhereClause(whereClause, "ERSMD_USER_INFO_DATA.LOCALE = :locale ");
        }
        if(StringUtils.isNotBlank(reportCriteria.getTimeZone())) {
            addWhereClause(whereClause, "ERSMD_USER_INFO_DATA.TIME_ZONE = :timeZone ");
        }
        if(reportCriteria.geteMailVarificationState() != null) {
            addWhereClause(whereClause, "ERSMD_USER_INFO_DATA.EMAIL_VERIFICATION_STATE = :emailVerificationState ");
        }       
        if( reportCriteria.getExternalProductId() != null) {
            addWhereClause(whereClause, "ERSMD_EXTERNAL_SYSTEM_LOOKUP.NAME = :externalCustomerId ");
        }
        // filter for externalSystemIdType **externalsystem should not be null
        if(reportCriteria.getExternalProductId() != null && StringUtils.isNotBlank(reportCriteria.getProductExternalSystemIdType())) {
            addWhereClause(whereClause, "ERSMD_EXTERNAL_SYSTEM_ID_TYPE_LOOKUP.NAME = :productExternalSystemIdType ");
        }
        // filter for last login dates 
        if(reportCriteria.getLastLoginFromDate() == null) {
            if(reportCriteria.getLastLoginToDate() != null) {
                addWhereClause(whereClause, "ERS_CRED_LOGIN_PASSWORD.DATE_LOGIN_SUCCEEDED <= :lastLoginToDate ");
            }
        } else {
            if(reportCriteria.getLastLoginToDate() == null) {
                addWhereClause(whereClause, "ERS_CRED_LOGIN_PASSWORD.DATE_LOGIN_SUCCEEDED > :lastLoginFromDate ");
            } else {
                addWhereClause(whereClause, "ERS_CRED_LOGIN_PASSWORD.DATE_LOGIN_SUCCEEDED between "
                		+ ":lastLoginFromDate and :lastLoginToDate ");
            } 
        }
        
        // filter for customer created date
        if(reportCriteria.getCustomerCreatedFromDate() == null) {
            if(reportCriteria.getCustomerCreatedToDate() != null) {
                addWhereClause(whereClause, "ERS_USERS.DATE_CREATED <= :customerCreatedToDate ");
            }
        } else {
            if(reportCriteria.getCustomerCreatedToDate() == null) {
                addWhereClause(whereClause, "ERS_USERS.DATE_CREATED > :customerCreatedFromDate ");
            } else {
                addWhereClause(whereClause, "ERS_USERS.DATE_CREATED between :customerCreatedFromDate "
                		+ "and :customerCreatedToDate ");
            } 
        }
        
      //filter for registration  created date
        if(reportCriteria.getRegistrationCreatedFromDate() == null) {
            if(reportCriteria.getRegistrationCreatedToDate() != null) {
                addWhereClause(whereClause, "ERS_LICENSES.DATE_CREATED <= :RegistrationCreatedToDate ");
            }
        } else {
            if(reportCriteria.getRegistrationCreatedToDate() == null) {
                addWhereClause(whereClause, "ERS_LICENSES.DATE_CREATED > :RegistrationCreatedFromDate ");
            } else {
                addWhereClause(whereClause, "ERS_LICENSES.DATE_CREATED between :RegistrationCreatedFromDate and :RegistrationCreatedToDate ");
            } 
        }
        
        //filter for registration updated date
        if(reportCriteria.getFromDate() == null) {
            if(reportCriteria.getToDate() != null) {
                addWhereClause(whereClause, "ERS_LICENSES.DATE_MODIFIED <= :toDate ");
            }
        } else {
            if(reportCriteria.getToDate() == null) {
                addWhereClause(whereClause, "ERS_LICENSES.DATE_MODIFIED > :fromDate ");
            } else {
                addWhereClause(whereClause, "ERS_LICENSES.DATE_MODIFIED between :fromDate and :toDate ");
            } 
        }
        /*List<String> orClauses = new ArrayList<String>();
        if(reportCriteria.isDenied()) {
            orClauses.add("ERSMD_LICENSE_INFO_DATA.DENIED = :denied_1 ");
        }
        if(reportCriteria.isActivated()) {
            orClauses.add("(ERSMD_LICENSE_INFO_DATA.COMPLETED = :activated_2 and ERSMD_LICENSE_INFO_DATA.DENIED = :denied_2) ");
        }
        if(reportCriteria.isPending()) {
            orClauses.add("(ERSMD_LICENSE_INFO_DATA.COMPLETED = :activated_3 and ERSMD_LICENSE_INFO_DATA.DENIED = :denied_3) ");
        }
        addOrClauses(whereClause, orClauses.toArray(new String[orClauses.size()]));*/
        if(reportCriteria.getRegistrationSelectionType() != RegistrationSelectionType.ALL) {
        	String registrationDefinitionType = new String() ;
            if(reportCriteria.getRegistrationSelectionType() == RegistrationSelectionType.ACTIVATION_CODE) {
            	registrationDefinitionType = RegistrationDefinitionType.ACTIVATION_CODE_REGISTRATION.toString() ;
            } else {
            	registrationDefinitionType = RegistrationDefinitionType.PRODUCT_REGISTRATION.toString() ;
            }
            addWhereClause(whereClause, "ERSMD_PRODUCT_INFO_DATA.REGISTRATION_DEFINITION_TYPE = '" + registrationDefinitionType+"'");
        }
        return whereClause.toString();
        
    }
    
    private void addOrClauses(StringBuilder whereClauseBuilder, String... whereClauses) {
        if(whereClauses == null || whereClauses.length == 0) {
            return;
        }
        StringBuilder orClause = new StringBuilder();
        for(String whereClause : whereClauses) {
            if(orClause.length() == 0) {
                orClause.append("(");
            } else {
                orClause.append("or ");
            }
            orClause.append(whereClause);
        }
        orClause.append(")");
        addWhereClause(whereClauseBuilder, orClause.toString());
    }

    private void addWhereClause(StringBuilder whereClauseBuilder, String whereClause) {
        if(whereClauseBuilder.length() > 0) {
            whereClauseBuilder.append("and ");
        } else {
            whereClauseBuilder.append("where ");
        }
        whereClauseBuilder.append(whereClause);
    }
    
    public final Long getRegistrationsByDateAndDivisionAndAccountCount(final DateTime fromDate, final DateTime toDate, final String divisionType, final String rsDBSchema) {
        
        return ((Integer)getSession().createSQLQuery(
                "SELECT count(1) " + 
                "FROM   " + rsDBSchema + ".ERS_LICENSES AS ers_licenses\n" + 
                "       join " + rsDBSchema + ".ERSMD_LICENSE_INFO_DATA AS ersmd_license_info_data\n" + 
                "         ON ERS_LICENSES.license_id = ERSMD_LICENSE_INFO_DATA.license_id\n" + 
                "       left outer join ( SELECT license_id,\n" + 
                "                                Count( ERS_LICENSE_USAGES.license_id ) AS USED_COUNT,\n" + 
                "                                Max( ERS_LICENSE_USAGES.date_used )    AS LAST_USE\n" + 
                "                         FROM   " + rsDBSchema + ".ERS_LICENSE_USAGES AS ers_license_usages\n" + 
                "                         GROUP  BY ERS_LICENSE_USAGES.license_id ) AS usages\n" + 
                "                    ON usages.license_id = ERS_LICENSES.license_id\n" + 
                "       join " + rsDBSchema + ".ERS_LICENSE_PRODUCT_LINK AS ers_license_product_link\n" + 
                "         ON ERS_LICENSES.license_id = ERS_LICENSE_PRODUCT_LINK.license_id\n" + 
                "       join " + rsDBSchema + ".ERS_LICENSE_PARAMETERS AS ers_license_parameters\n" + 
                "         ON ERS_LICENSES.license_id = ERS_LICENSE_PARAMETERS.license_id\n" + 
                "       left join " + rsDBSchema + ".ERS_LICENSE_CUSTOMER_STATE AS ers_license_customer_state\n" + 
                "              ON ERS_LICENSES.license_id = ERS_LICENSE_CUSTOMER_STATE.license_id\n" + 
                "       join " + rsDBSchema + ".ERS_PRODUCTS AS ers_products\n" + 
                "         ON ERS_LICENSE_PRODUCT_LINK.product_id = ERS_PRODUCTS.product_id\n" + 
                "       join " + rsDBSchema + ".ERSMD_PRODUCT_INFO_DATA AS ersmd_product_info_data\n" + 
                "         ON ERS_PRODUCTS.product_id = ERSMD_PRODUCT_INFO_DATA.product_id\n" + 
                "       left join " + rsDBSchema + ".ERSMD_DIVISION_LOOKUP AS ersmd_division_lookup\n" + 
                "              ON ERSMD_PRODUCT_INFO_DATA.division_id = ERSMD_DIVISION_LOOKUP.division_id\n" + 
                "       join " + rsDBSchema + ".ERS_CUSTOMERS AS ers_customers\n" + 
                "         ON ERS_CUSTOMERS.customer_id = ERS_LICENSES.customer_id\n" + 
                "       join " + rsDBSchema + ".ERS_USERS AS ers_users\n" + 
                "         ON ERS_CUSTOMERS.target_id = ERS_USERS.user_id\n" + 
                "       join " + rsDBSchema + ".ERSMD_USER_INFO_DATA AS ersmd_user_info_data\n" + 
                "         ON ERS_USERS.user_id = ERSMD_USER_INFO_DATA.user_id\n" + 
                "       join " + rsDBSchema + ".ERS_AUTH_PROFILES AS ers_auth_profiles\n" + 
                "         ON ERS_USERS.user_id = ERS_AUTH_PROFILES.user_id\n" + 
                "       join " + rsDBSchema + ".ERS_CREDENTIALS AS ers_credentials\n" + 
                "         ON ERS_AUTH_PROFILES.profile_id = ERS_CREDENTIALS.profile_id\n" + 
                "       join " + rsDBSchema + ".ERS_CRED_LOGIN_PASSWORD AS ers_cred_login_password\n" + 
                "         ON ERS_CREDENTIALS.credential_id = ERS_CRED_LOGIN_PASSWORD.credential_id\n" + 
                "WHERE  ERS_PRODUCTS.date_suspended IS NULL AND\n" + 
                "       ERS_USERS.date_suspended IS NULL AND\n" + 
                "       ERS_PRODUCTS.product_id IS NOT NULL AND\n" + 
                "       Isnull( usages.used_count, ERS_LICENSE_PARAMETERS.num1 ) <= ERS_LICENSE_PARAMETERS.num1 AND\n" + 
                "       ERSMD_LICENSE_INFO_DATA.completed = 1 AND\n" + 
                "       ERS_LICENSE_PARAMETERS.enabled = 1 AND\n" + 
                "       ERS_LICENSES.date_paused IS NULL AND\n" + 
                "       Isnull( ERS_LICENSE_PARAMETERS.start_date, Getdate( ) ) <= Getdate( ) AND\n" + 
                "       Isnull( ERS_LICENSE_PARAMETERS.end_date, Getdate( ) ) >= Getdate( ) AND\n" + 
                "		Isnull(ersmd_license_info_data.modified_date, ERS_LICENSES.date_modified) BETWEEN :fromDate AND :toDate AND\n" + 
                "		ERSMD_DIVISION_LOOKUP.division_type = :divisionType")
                .setParameter("fromDate", fromDate.toGregorianCalendar().getTime())
                .setParameter("toDate", toDate.toGregorianCalendar().getTime())
                .setParameter("divisionType", divisionType)
                .uniqueResult()).longValue();
    }
    
	@Override
	@SuppressWarnings("unchecked")
	public List<ActivationCodeRegistration> getActivationCodeRegistrationByCode(final ActivationCode activationCode) {
		return getSession().createQuery("select acr from ActivationCodeRegistration acr " +
				"join fetch acr.customer c " +
				/*"left join fetch c.externalIds ei " +*/
				"where acr.activationCode = :activationCode")
				.setParameter("activationCode", activationCode)
				.list();
	}
    
    /**
     * {@inheritDoc}
     */
	@Override
	@SuppressWarnings("unchecked")
    public final List<ProductRegistration> getProductRegistrationsForCustomer(final Customer customer) {
        List<ProductRegistration> result;
        result = getSession().createQuery(BASE_PROD_ENTITLEMENTS_SQL
                 + " where preg.customer = :cust "
                 )
        		 .setParameter("cust", customer)
        		 .list();
        return result;
    }
    
  
    private static final String BASE_AC_ENTITLEMENTS_SQL =
	          " select distinct acreg from ActivationCodeRegistration acreg"
	        + " left outer join fetch acreg.activationCode ac"
	        + " join fetch acreg.registrationDefinition rd "
	        + " join fetch rd.licenceTemplate lt "
	        + " join fetch rd.product pd "
	        + " left outer join fetch acreg.linkedRegistrations" //eagerly fetch linked registrations
	        + " left outer join fetch pd.linkedProducts as linked "; // eagerly fetch linked products

  
  private static final String BASE_PROD_ENTITLEMENTS_SQL =
        " select distinct preg from ProductRegistration preg" +
        " join fetch preg.registrationDefinition rd "
      + " join fetch rd.licenceTemplate lt "
      + " join fetch rd.product pd "
      + " left outer join fetch preg.linkedRegistrations" //eagerly fetch linked registrations
      + " left outer join fetch pd.linkedProducts as linked "; // eagerly fetch linked products
    

    @Override
    @SuppressWarnings("unchecked")
    public final List<ActivationCodeRegistration> getActivationCodeRegistrationsForCustomer(final Customer customer) {
        List<ActivationCodeRegistration> result;
        result = getSession().createQuery(BASE_AC_ENTITLEMENTS_SQL
                + " where acreg.customer = :cust "
                )
                .setParameter("cust",customer)
                .list();
        return result;
    }

    private ActivationCodeRegistration fetchAcRegistrationPopulatedWithEntitlements(ActivationCodeRegistration acRegistration) {
        ActivationCodeRegistration result;
        result = (ActivationCodeRegistration) getSession().createQuery(BASE_AC_ENTITLEMENTS_SQL
                + " where acreg.id = :id "
                )
                .setParameter("id", acRegistration.getId())
                .uniqueResult();
        return result;
    }

    private ProductRegistration fetchProductRegistrationPopulatedWithEntitlements(ProductRegistration prRegistration) {
        ProductRegistration result;
        result = (ProductRegistration) getSession().createQuery(BASE_PROD_ENTITLEMENTS_SQL
                + " where preg.id = :id "
            ).setParameter("id", prRegistration.getId())
            .uniqueResult();
        return result;
    }

    @Override
    public Registration<? extends ProductRegistrationDefinition> getRegistrationPopulatedWithEntitlements(Registration<? extends ProductRegistrationDefinition> registration) {
        final Registration<? extends ProductRegistrationDefinition> result;
        switch (registration.getRegistrationType()) {
        case PRODUCT:
            ProductRegistration pr = (ProductRegistration) registration; 
            result = this.fetchProductRegistrationPopulatedWithEntitlements(pr);
            break;
        case ACTIVATION_CODE:        
            ActivationCodeRegistration acr = (ActivationCodeRegistration) registration;
            result = this.fetchAcRegistrationPopulatedWithEntitlements(acr);
            break;
        default:
            throw new IllegalArgumentException("Unknown RegistrationType : " + registration.getRegistrationType());
        }
        return result;
    }

    /*
	 * Gaurav Soni : Performance improvements CR
	 * */
	@Override
	@SuppressWarnings("unchecked")
	public List<ActivationCodeRegistration> getActivationCodeRegistrationsForCustomerFiltered( Customer customer, Set<String> productOrgUnitSet, Set<String> productSystemIdSet) {
		List<ActivationCodeRegistration> result;
		Query query = getQueryForRegFiltered(BASE_AC_ENTITLEMENTS_SQL, customer, productSystemIdSet,productOrgUnitSet, true);
        result = query.list();
        return result;
	}

	/*
	 * Gaurav Soni : Performance improvements CR
	 * */
	@Override
	@SuppressWarnings("unchecked")
	public List<ProductRegistration> getProductRegistrationsForCustomerFiltered( Customer customer, Set<String> productOrgUnitSet, Set<String> productSystemIdSet) {
		List<ProductRegistration> result;
		Query query = getQueryForRegFiltered(BASE_PROD_ENTITLEMENTS_SQL, customer, productSystemIdSet,productOrgUnitSet,  false);
        result = query.list();
        return result;
	}
	
	
	/*
	 * Gaurav Soni : Performance improvements CR
	 * Method includes filtration and dynamic joins
	 * */
	private Query getQueryForRegFiltered(String baseQuery, Customer customer, Set<String> productSystemIdSet,Set<String> productOrgUnitSet, boolean isActCodeReg){
		StringBuilder builder = new StringBuilder(baseQuery);
		
		if(null != productSystemIdSet && CollectionUtils.isNotEmpty(productSystemIdSet)){
			builder.append(" left outer join fetch pd.externalIds extid");
		}
		
		if(isActCodeReg){
			builder.append(" where acreg.customer = :cust ");
		}else{
			builder.append(" where preg.customer = :cust ");
		}
				
		if(null != productOrgUnitSet && CollectionUtils.isNotEmpty(productOrgUnitSet)){
			builder.append(" and a.division.divisionType in (:productOrgUnitSet )");
		}
		
		if(null != productSystemIdSet && CollectionUtils.isNotEmpty(productSystemIdSet)){
			builder.append(" and extid.externalSystemIdType.externalSystem.name in (:productSystemIdSet )");
		}
		
		Query query = getSession().createQuery(builder.toString());
		//set cust param
		query.setParameter("cust",customer);
		//set productOrgUnit list if not empty
		if(null != productOrgUnitSet && CollectionUtils.isNotEmpty(productOrgUnitSet)){
			List<String> divisionTypes = new ArrayList<String>(productOrgUnitSet);
			query.setParameterList("productOrgUnitSet", divisionTypes);
		}
		
		if(null != productSystemIdSet && CollectionUtils.isNotEmpty(productSystemIdSet)){
			List<String> productSystemIdList = new ArrayList<String>(productSystemIdSet);
			query.setParameterList("productSystemIdSet", productSystemIdList);
		}
		return query;
	}

}