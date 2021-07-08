package com.oup.eac.data.impl.hibernate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.data.DivisionDao;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Division;


/**
 * The Answer dao hibernate implementation.
 * 
 * @author harlandd
 * @author Ian Packard
 */
@Repository(value="divisionDao")
public class DivisionHibernateDao extends HibernateBaseDao<Division, String> implements DivisionDao {

    private static final String _100 = "100.00";
    private static final String TO = "to";
    private static final String FROM = "from";

    /**
     * @param sf
     *            the session factory
     */
	@Autowired
    public DivisionHibernateDao(final SessionFactory sf) {
        super(sf);
    }  

	@Override
    @SuppressWarnings("unchecked")
    public final List<Division> getDivisions() {
        return getSession().createQuery("select d from Division d " +
        		                        "order by d.divisionType")
        		                        .list();
    }

    @Override
    public final Division getDivisionByDivisionType(final String divisionType) {
        return (Division)getSession().createQuery("select d from Division d where d.divisionType = :divisionType")
                                        .setParameter("divisionType", divisionType)
                                        .uniqueResult();
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<Division> getDivisionsByAdminUser(final AdminUser adminUser) {
		return getSession().createQuery("select d from Division d " +
										"join d.divisionAdminUsers dau " +
										"where dau.adminUser = :adminUser " +
        								"order by d.divisionType")
        								.setParameter("adminUser", adminUser)
        								.list();
	}
	
    @Override
    public Boolean isDivisionUsed(String divisionId) {
        Long usedDivisionCount =  (Long) getSession()
                .createQuery(   " select count(d) from Division d " +
                                " where  d.id in (select dau.division.id     from DivisionAdminUser dau where dau.division.id = :divisionId) " +
                                " or     d.id in (select pd.division.id      from PageDefinition pd     where  pd.division.id = :divisionId) " +
                                " or     d.id in (select prod.division.id     from Product prod             where prod.division.id = :divisionId) " )                                
                .setString("divisionId", divisionId)                
                .uniqueResult();
        boolean isUsed =  usedDivisionCount > 0;
        return isUsed;
    }

    @Override
    public Boolean isDivisionUsed(String divisionId, AdminUser administrator) {
        Long usedDivisionCount =  (Long) getSession()
                .createQuery("select count(dau) from DivisionAdminUser dau where dau.divisionErightsId = :divisionId and dau.adminUser != :adminUser" )                  		        
                .setString("divisionId", divisionId)
                .setParameter("adminUser", administrator)
                .uniqueResult();
        boolean isUsed =  usedDivisionCount > 0;
        if (!isUsed) {
        	Long usedDivisionCountPg =  (Long) getSession()
            .createQuery("select count(pd)     from PageDefinition pd     where  pd.divisionErightsId = :divisionId")
        	.setString("divisionId", divisionId)
            .uniqueResult();
        	isUsed =  usedDivisionCountPg > 0;
        }
        return isUsed;
    }

    @Override
    public List<String[]> getOrgUnitUsage(DateTime fromTime, DateTime toTime) throws SQLException {
        //Because we only keep the last login time, in PRODUCTION this report must be run at time 00.00.01 on 1st of each month. 
        /*String sql  =   " select DIV.divisionType, "
        		      + "   ( select count(R) from Registration R "
        		      + "     where R.customer.lastLoginDateTime between :from and :to "
                      + "     and   R.registrationDefinition.product.division = DIV "
                      + "     and   R.erightsLicenceId is not null "  //we only count registrations that have licences!
                      + "   ) from Division DIV order by DIV.divisionType asc ";*/
    	String ER_DB_NAME_WITH_SCHEMA = EACSettings.getProperty("rs.db.name") + "." + EACSettings.getProperty("rs.db.schema")  ;
    	String sql = "SELECT cast(ERSMD_DIVISION_LOOKUP.division_type as varchar(max)), count(ers_licenses.license_id) usage_count\n" + 
        		"FROM   "+ER_DB_NAME_WITH_SCHEMA+".ERS_LICENSES AS ers_licenses\n" + 
        		"       join "+ER_DB_NAME_WITH_SCHEMA+".ERS_LICENSE_PRODUCT_LINK AS ers_license_product_link\n" + 
        		"         ON ERS_LICENSES.license_id = ERS_LICENSE_PRODUCT_LINK.license_id\n" + 
        		"       join "+ER_DB_NAME_WITH_SCHEMA+".ERS_PRODUCTS AS ers_products\n" + 
        		"         ON ERS_LICENSE_PRODUCT_LINK.product_id = ERS_PRODUCTS.product_id\n" + 
        		"       join "+ER_DB_NAME_WITH_SCHEMA+".ERSMD_PRODUCT_INFO_DATA AS ersmd_product_info_data\n" + 
        		"         ON ERS_PRODUCTS.product_id = ERSMD_PRODUCT_INFO_DATA.product_id\n" + 
        		"       left join "+ER_DB_NAME_WITH_SCHEMA+".ERSMD_DIVISION_LOOKUP AS ersmd_division_lookup\n" + 
        		"              ON ERSMD_PRODUCT_INFO_DATA.division_id = ERSMD_DIVISION_LOOKUP.division_id\n" + 
        		"       join "+ER_DB_NAME_WITH_SCHEMA+".ERS_CUSTOMERS AS ers_customers\n" + 
        		"         ON ERS_CUSTOMERS.customer_id = ERS_LICENSES.customer_id\n" + 
        		"       join "+ER_DB_NAME_WITH_SCHEMA+".ERS_USERS AS ers_users\n" + 
        		"         ON ERS_CUSTOMERS.target_id = ERS_USERS.user_id\n" + 
        		"       join "+ER_DB_NAME_WITH_SCHEMA+".ERS_AUTH_PROFILES AS ers_auth_profiles\n" + 
        		"         ON ERS_USERS.user_id = ERS_AUTH_PROFILES.user_id\n" + 
        		"       join "+ER_DB_NAME_WITH_SCHEMA+".ERS_CREDENTIALS AS ers_credentials\n" + 
        		"         ON ERS_AUTH_PROFILES.profile_id = ERS_CREDENTIALS.profile_id\n" + 
        		"       join "+ER_DB_NAME_WITH_SCHEMA+".ERS_CRED_LOGIN_PASSWORD AS ers_cred_login_password\n" + 
        		"         ON ERS_CREDENTIALS.credential_id = ERS_CRED_LOGIN_PASSWORD.credential_id\n" + 
        		"WHERE  ERS_CRED_LOGIN_PASSWORD.date_login_succeeded BETWEEN :from AND :to \n"
        		+ "		Group by ERSMD_DIVISION_LOOKUP.division_type";
        java.sql.Date tsFrom = new java.sql.Date(fromTime.toDate().getTime());
        java.sql.Date tsTo   = new java.sql.Date(toTime.toDate().getTime());
        
        List<String[]> result = new ArrayList<String[]>();
        
        @SuppressWarnings("unchecked")
        List<Object[]> items = (List<Object[]>)getSession()
            .createSQLQuery(sql)
            .setTimestamp(FROM, tsFrom)
            .setTimestamp(TO, tsTo)
            .list();
        
        Integer tot = 0;
        if (items != null) {
            for (Object[] item : items) {
                tot += (Integer) item[1];
            }
            BigDecimal total = new BigDecimal(tot);
            BigDecimal hundred = new BigDecimal(_100);
            for (Object[] item : items) {
                Integer val = (Integer) item[1];
                BigDecimal calced;
                if (val == null || val.intValue() == 0L) {
                    calced = BigDecimal.ZERO.setScale(2);
                } else {
                    calced = new BigDecimal(val.intValue()).setScale(2).divide(total, 8, RoundingMode.HALF_EVEN)
                            .multiply(hundred).setScale(2, RoundingMode.HALF_EVEN);
                }
                String percentage = calced.toString();
                String[] res = new String[] { String.valueOf(item[0]), String.valueOf(item[1]), percentage };
                result.add(res);
            }
        }
        return result;
    }
    
}

