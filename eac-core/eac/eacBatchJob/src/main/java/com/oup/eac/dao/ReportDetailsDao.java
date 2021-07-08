package com.oup.eac.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import com.oup.eac.util.BatchJobProperties;
import com.oup.eac.util.ConnectionUtil;

public class ReportDetailsDao {
	
	private static final Logger LOG = Logger.getLogger(ReportDetailsDao.class);

	private static String rsDBSchema = BatchJobProperties.ER_DB_NAME;
	
	private static final Object[] EMPTY_ARGS = new Object[0];
	
	
	
	public long getRegistrationsByDateAndDivisionAndAccountCount(DateTime fromDT, DateTime toDT, String divisionType, ArrayList<String> productErightsIds) throws SQLException {
		long count = 0;
		Connection con = ConnectionUtil.getConnection();
		StringBuilder builder = new StringBuilder();
		for( String productId :productErightsIds) {
			if(productId!=null){
				LOG.info(productId);
				builder.append("?,");
			}
		}
		try{
			//LOG.info(ActivationCodeSqlQuery.ACTIVATION_BATCH_MIGRATION);
			String query = buildQuery(true, builder) ;
			
			LOG.info("Query getRegistrationsByDateAndDivisionAndAccountCount : " + query);
			int i=6;
			PreparedStatement ps = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,  ResultSet.CONCUR_READ_ONLY);
			ps.setString(1, fromDT.toLocalDateTime().toString());
			ps.setString(2, toDT.toLocalDateTime().toString());
			ps.setString(3, fromDT.toLocalDateTime().toString());
			ps.setString(4, toDT.toLocalDateTime().toString());
			ps.setString(5, divisionType);
			for( Object o : productErightsIds ) {
				if(o!=null){
				   ps.setObject(  i++, o );
				   LOG.info(i);
				}
			}
			//ps.setString(4,getListOfProductIds(productErightsIds));
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				count = rs.getLong(1);
			}
		}
		catch (SQLException e) {
			LOG.error(e.getMessage(),e);
			throw e ;
		}
		finally{
			try {
				con.close();
			} catch (SQLException e) {
				LOG.error("Error in CMDP data export dao finally : "+e);
				throw e ;
			}
		}
		return count;
	}
	
	private String buildQuery(final boolean isCountQuery, StringBuilder productIds ) {
		String selectParameters = " count(1) " ;
		if (!isCountQuery) {
			selectParameters = " DISTINCT Cast( ERSMD_PRODUCT_INFO_DATA.product_id AS VARCHAR(510))            AS PRODUCT_ID,\n" + 
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
	    			"                usages.used_count,  ers_licenses.license_id, \n" +
	    			" 				 ERS_USERS.date_modified		AS PROFILE_UPDATE_DATE \n";
		}
		String query = "SELECT " + selectParameters + 
                "FROM   " + rsDBSchema + ".ERS_LICENSES AS ers_licenses WITH (NOLOCK)\n" + 
                "       join " + rsDBSchema + ".ERSMD_LICENSE_INFO_DATA AS ersmd_license_info_data WITH (NOLOCK)\n" + 
                "         ON ERS_LICENSES.license_id = ERSMD_LICENSE_INFO_DATA.license_id\n" + 
                "       left outer join ( SELECT license_id,\n" + 
                "                                Count( ERS_LICENSE_USAGES.license_id ) AS USED_COUNT,\n" + 
                "                                Max( ERS_LICENSE_USAGES.date_used )    AS LAST_USE\n" + 
                "                         FROM   " + rsDBSchema + ".ERS_LICENSE_USAGES AS ers_license_usages WITH (NOLOCK)\n" + 
                "                         GROUP  BY ERS_LICENSE_USAGES.license_id ) AS usages\n" + 
                "                    ON usages.license_id = ERS_LICENSES.license_id\n" + 
                "       join " + rsDBSchema + ".ERS_LICENSE_PRODUCT_LINK AS ers_license_product_link WITH (NOLOCK)\n" + 
                "         ON ERS_LICENSES.license_id = ERS_LICENSE_PRODUCT_LINK.license_id\n" + 
                "       join " + rsDBSchema + ".ERS_LICENSE_PARAMETERS AS ers_license_parameters WITH (NOLOCK)\n" + 
                "         ON ERS_LICENSES.license_id = ERS_LICENSE_PARAMETERS.license_id\n" + 
                "       left join " + rsDBSchema + ".ERS_LICENSE_CUSTOMER_STATE AS ers_license_customer_state WITH (NOLOCK)\n" + 
                "              ON ERS_LICENSES.license_id = ERS_LICENSE_CUSTOMER_STATE.license_id\n" + 
                "       join " + rsDBSchema + ".ERS_PRODUCTS AS ers_products WITH (NOLOCK)\n" + 
                "         ON ERS_LICENSE_PRODUCT_LINK.product_id = ERS_PRODUCTS.product_id\n" + 
                "       join " + rsDBSchema + ".ERSMD_PRODUCT_INFO_DATA AS ersmd_product_info_data WITH (NOLOCK)\n" + 
                "         ON ERS_PRODUCTS.product_id = ERSMD_PRODUCT_INFO_DATA.product_id\n" + 
                "       left join " + rsDBSchema + ".ERSMD_DIVISION_LOOKUP AS ersmd_division_lookup WITH (NOLOCK)\n" + 
                "              ON ERSMD_PRODUCT_INFO_DATA.division_id = ERSMD_DIVISION_LOOKUP.division_id\n" + 
                "       join " + rsDBSchema + ".ERS_CUSTOMERS AS ers_customers WITH (NOLOCK)\n" + 
                "         ON ERS_CUSTOMERS.customer_id = ERS_LICENSES.customer_id AND ERS_CUSTOMERS.TARGET_TYPE = 2\n" + 
                "       join " + rsDBSchema + ".ERS_USERS AS ers_users WITH (NOLOCK)\n" + 
                "         ON ERS_CUSTOMERS.target_id = ERS_USERS.user_id\n" + 
                "       join " + rsDBSchema + ".ERSMD_USER_INFO_DATA AS ersmd_user_info_data WITH (NOLOCK)\n" + 
                "         ON ERS_USERS.user_id = ERSMD_USER_INFO_DATA.user_id\n" + 
                "       join " + rsDBSchema + ".ERS_AUTH_PROFILES AS ers_auth_profiles WITH (NOLOCK)\n" + 
                "         ON ERS_USERS.user_id = ERS_AUTH_PROFILES.user_id\n" + 
                "       join " + rsDBSchema + ".ERS_CREDENTIALS AS ers_credentials WITH (NOLOCK)\n" + 
                "         ON ERS_AUTH_PROFILES.profile_id = ERS_CREDENTIALS.profile_id\n" + 
                "       join " + rsDBSchema + ".ERS_CRED_LOGIN_PASSWORD AS ers_cred_login_password WITH (NOLOCK)\n" + 
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
                "		( Isnull(ersmd_license_info_data.modified_date, ERS_LICENSES.date_modified) BETWEEN convert(datetime, ?, 126) AND convert(datetime, ?, 126) OR\n" +
                "		Isnull(ERS_USERS.date_modified, ERS_USERS.date_created) BETWEEN convert(datetime, ?, 126) AND convert(datetime, ?, 126)) AND\n" +
                "		ERSMD_DIVISION_LOOKUP.division_type = ? AND " + 
                "		ERS_PRODUCTS.PRODUCT_ID IN ("+ productIds.deleteCharAt( productIds.length() -1 ).toString()+ " )";
		
		if (!isCountQuery) {
			query += " order by ers_licenses.license_id offset ? rows";
		}
		return query ;
	}
	private String getListOfProductIds(ArrayList<String> productErightsIds) {
		String productIds = "',";
		for(String productId : productErightsIds ){
			if(productId!=null)
				productIds+=productId + "','"; 
		}
		LOG.info(productIds);
		if(productIds.length()>0){
			productIds = productIds.substring(0, productIds.length()-1);
			LOG.info(productIds);
		}else{
			productIds=null;
		}
		return productIds;
	}

	public final List<Object[]> getRegistrationsByDateAndDivisionAndAccount(final String divisionType, final DateTime fromDT, final DateTime toDT, final int batchSize, int firstRecord, ArrayList<String> productErightsIds) throws SQLException {
		Connection con = ConnectionUtil.getConnection();
		StringBuilder builder = new StringBuilder();
		List<Object[]> result = new ArrayList<Object[]>();
		for( String productId :productErightsIds) {
			if(productId!=null)
				builder.append("?,");
		}
		String query = buildQuery(false, builder) ;
		
		LOG.info("Query getUserDataByOwnerAndDateRange : " + query);
		try {
			PreparedStatement ps = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,  ResultSet.CONCUR_READ_ONLY);
			ps.setString(1, fromDT.toLocalDateTime().toString());
			ps.setString(2, toDT.toLocalDateTime().toString());
			ps.setString(3, fromDT.toLocalDateTime().toString());
			ps.setString(4, toDT.toLocalDateTime().toString());
			ps.setString(5, divisionType);
			int i=6;
			for( Object o : productErightsIds ) {
				if(o!=null)
				   ps.setObject( i++, o );
			}
			
			//ps.setString(i++, getListOfProductIds(productErightsIds));
			ps.setInt(i, ((firstRecord - 1) * batchSize));
			ps.setMaxRows(batchSize);
			ResultSet rs = ps.executeQuery();
			Object[] objArr = null;
			while(rs.next()){
				objArr = new Object[19];
				objArr[0] = rs.getString(1);
				objArr[1] = rs.getString(2);
				objArr[2] = rs.getString(3);
				objArr[3] = rs.getString(4);
				objArr[4] = rs.getString(5);
				objArr[5] = rs.getString(6);
				objArr[6] = rs.getString(7);
				objArr[7] = rs.getString(8);
				objArr[8] = rs.getString(9);
				objArr[9] = rs.getString(10);
				objArr[10] = rs.getString(11);
				objArr[11] = rs.getString(12);
				objArr[12] = rs.getString(13);
				objArr[13] = rs.getTimestamp(14);
				objArr[14] = rs.getTimestamp(15);
				objArr[15] = rs.getTimestamp(16);
				objArr[16] = rs.getTimestamp(17);
				objArr[17] = rs.getString(18);
				objArr[18] = rs.getTimestamp(20);
				result.add(objArr);
			}
		} catch (SQLException e) {
			LOG.error("Error in CMDP data export dao : "+e);
			throw e ;
		}	
		finally{
			try {
				con.close();
			} catch (SQLException e) {
				LOG.error("Error in CMDP data export dao finally : "+e);
				throw e ;
			}
		}
		
		return result;
    }
	
	public List<Object[]> getAnswerDetailsByCustomerAndProduct(
			String customerId, String productId, boolean isCMDP) throws SQLException {
		StringBuilder sql = new StringBuilder();
		List<Object[]> result = new ArrayList<Object[]>();
		Connection con = ConnectionUtil.getConnection();
		sql.append("SELECT distinct cast(ans.answer_text as varchar(max)),\n"); 
		if(isCMDP){
			sql.append("       CASE\n" + 
					"         WHEN ( en.name IS NOT NULL AND\n" + 
					"                en.export_type = 'CMDP' ) THEN cast(en.name as varchar(max))\n" + 
					"         ELSE cast(que.description as varchar(max))\n" + 
					"       END question_desc,\n"); 
		}
		else{
			sql.append("cast(que.description as varchar(max)) question_desc,\n");
		}
		sql.append("       cast(rd.page_definition_id as varchar(max)) page_definition_id\n"); 
		sql.append("FROM   ANSWER ans\n"); 
		sql.append("       join QUESTION que\n"); 
		sql.append("         ON ans.question_id = que.id\n"); 
		if(isCMDP){
			sql.append("       left join EXPORT_NAME en\n"); 
			sql.append("              ON en.question_id = que.id\n"); 
		}
		sql.append("       join ELEMENT el\n"); 
		sql.append("         ON el.question_id = que.id\n"); 
		sql.append("       join FIELD fi\n"); 
		sql.append("         ON fi.element_id = el.id\n"); 
		sql.append("       join PAGE_COMPONENT pc\n"); 
		sql.append("         ON pc.component_id = fi.component_id\n"); 
		sql.append("       join REGISTRATION_DEFINITION rd\n"); 
		sql.append("         ON rd.page_definition_id = pc.page_definition_id\n"); 
		sql.append("WHERE  ISNULL(ans.registerable_product_id,?) = ?\n ");
		sql.append("     and rd.product_id = ? \n"); 
		sql.append("       AND ans.customer_id = ? 	");
		LOG.info("Query getUserDataByOwnerAndDateRange : " + sql);
		try {
			PreparedStatement ps = con.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,  ResultSet.CONCUR_READ_ONLY);
			ps.setString(1,productId);
			ps.setString(2,productId);
			ps.setString(3,productId);
			ps.setString(4, customerId);
			ResultSet rs = ps.executeQuery();
			Object[] objArr = null;
			while(rs.next()){
				objArr = new Object[3];
				objArr[0] = rs.getString(1);
				objArr[1] = rs.getString(2);
				objArr[2] = rs.getString(3);
				result.add(objArr);
			}
			rs.close();
		} catch (SQLException e) {
			LOG.error("Error in CMDP data export dao : "+e);
			throw e ;
		}	
		finally{
			try {
				con.close();
			} catch (SQLException e) {
				LOG.error("Error in CMDP data export dao finally : "+e);
				throw e ;
			}
		}
		
		return result;
	}
	
	public Map<String, Map<String, String>> getTextMap(final String pageDefinitionId,final MessageSource messageSource) throws SQLException {
		Connection con = ConnectionUtil.getConnection();
		Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
		String sql = "select\n" + 
				"    distinct \n" + 
				"	tag_option.value,\n" + 
				"	tag_option.label,\n" + 
				"	 CASE \n" + 
				"	   WHEN ( en.name IS NOT NULL AND\n" + 
				"	          en.export_type = 'CMDP' ) THEN cast(en.name as varchar(max))\n" + 
				"	   ELSE cast(que.description as varchar(max))\n" + 
				"	 END question_desc, msg.message \n" + 
				"from\n" + 
				"    page_definition pd \n" + 
				"inner join\n" + 
				"    page_component pc \n" + 
				"        on pd.id=pc.page_definition_id \n" + 
				"inner join\n" + 
				"    component comp \n" + 
				"        on pc.component_id=comp.id \n" + 
				"inner join\n" + 
				"    field fields \n" + 
				"        on comp.id=fields.component_id \n" + 
				"inner join\n" + 
				"    element element \n" + 
				"        on fields.element_id=element.id \n" + 
				"left outer join\n" + 
				"    element_country_restriction ecr \n" + 
				"        on element.id=ecr.element_id \n" + 
				"inner join\n" + 
				"    question que \n" + 
				"        on element.question_id=que.id \n" + 
				"inner join\n" + 
				"    tag tag \n" + 
				"        on element.id=tag.element_id \n" + 
				"Inner join \n" + 
				"	tag_option\n" + 
				"		on tag.id = tag_option.tag_id\n" + 
				"left join \n" + 
				"	export_name en\n" + 
				"		on en.question_id = que.id\n" + 
				"left join \n" + 
				"	message msg\n" + 
				"		on tag_option.label = msg.\"key\" and (language = 'en' or language is null)\n "+
				"where\n" + 
				"    pd.page_definition_type='PRODUCT_PAGE_DEFINITION' \n" + 
				"	and tag.tag_type in ('SELECT','MULTISELECT')\n" + 
				"    and pd.id=? ";
		try {
			LOG.info("getTextMap query : "+sql+" id = "+pageDefinitionId);
			PreparedStatement ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,  ResultSet.CONCUR_READ_ONLY);
			ps.setString(1,pageDefinitionId);
			ResultSet rs = ps.executeQuery();
			Map<String, String> temp = null;
			while(rs.next()){
				temp = result.get(rs.getString("question_desc"));
				if(temp == null) {
					temp = new HashMap<String, String>();
					temp.put(rs.getString("value"), rs.getString("message"));
					result.put(rs.getString("question_desc"), temp);
				}
				else{
					temp.put(rs.getString("value"), rs.getString("message"));
				}
				
			}
			rs.close();
		} catch (SQLException e) {
			LOG.error("Error in CMDP data export dao : "+e);
			throw e ;
		}	
		finally{
			try {
				con.close();
			} catch (SQLException e) {
				LOG.error("Error in CMDP data export dao finally : "+e);
				throw e ;
			}
		}
		return result;
	}
	
	 public String getString(String key, MessageSource messageSource) {
        String result;
        try{
            result = messageSource.getMessage(key, EMPTY_ARGS, Locale.getDefault());
        }catch(NoSuchMessageException ex){
            LOG.warn("No message found key " + key,ex);
            result = key;
        }
        return result;
    }
	 
	public List<Object[]> getRegistrationsByDateAndDivision(final DateTime fromDate, 
    		final DateTime toDate, final String divisionType, final String rsDBSchema) throws SQLException{

		Connection con = ConnectionUtil.getConnection();
		List<Object[]> result = new ArrayList<Object[]>();
		String query = "SELECT DISTINCT Cast( ERSMD_PRODUCT_INFO_DATA.product_id AS VARCHAR(510))            AS PRODUCT_ID,\n" + 
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
    			"                ERS_USERS.date_created            									  AS USER_CREATED_DATE,\n" + 
    			"                ERS_LICENSES.date_created                                            AS LICENSE_CREATED_DATE,\n" + 
    			"                Isnull(ERSMD_LICENSE_INFO_DATA.modified_date, ERS_LICENSES.date_modified) AS LICENSE_UPDATED_DATE,\n" + 
    			"                usages.used_count\n" + 
    			"FROM   " + rsDBSchema + ".ERS_LICENSES AS ers_licenses WITH (NOLOCK)\n" + 
    			"       join " + rsDBSchema + ".ERSMD_LICENSE_INFO_DATA AS ersmd_license_info_data WITH (NOLOCK)\n" + 
    			"         ON ERS_LICENSES.license_id = ERSMD_LICENSE_INFO_DATA.license_id\n" + 
    			"       left outer join ( SELECT license_id,\n" + 
    			"                                Count( ERS_LICENSE_USAGES.license_id ) AS USED_COUNT,\n" + 
    			"                                Max( ERS_LICENSE_USAGES.date_used )    AS LAST_USE\n" + 
    			"                         FROM   " + rsDBSchema + ".ERS_LICENSE_USAGES AS ers_license_usages WITH (NOLOCK)\n" + 
    			"                         GROUP  BY ERS_LICENSE_USAGES.license_id ) AS usages\n" + 
    			"                    ON usages.license_id = ERS_LICENSES.license_id\n" + 
    			"       join " + rsDBSchema + ".ERS_LICENSE_PRODUCT_LINK AS ers_license_product_link WITH (NOLOCK)\n" + 
    			"         ON ERS_LICENSES.license_id = ERS_LICENSE_PRODUCT_LINK.license_id\n" + 
    			"       join " + rsDBSchema + ".ERS_LICENSE_PARAMETERS AS ers_license_parameters WITH (NOLOCK)\n" + 
    			"         ON ERS_LICENSES.license_id = ERS_LICENSE_PARAMETERS.license_id\n" + 
    			"       left join " + rsDBSchema + ".ERS_LICENSE_CUSTOMER_STATE AS ers_license_customer_state WITH (NOLOCK)\n" + 
    			"              ON ERS_LICENSES.license_id = ERS_LICENSE_CUSTOMER_STATE.license_id\n" + 
    			"       join " + rsDBSchema + ".ERS_PRODUCTS AS ers_products WITH (NOLOCK)\n" + 
    			"         ON ERS_LICENSE_PRODUCT_LINK.product_id = ERS_PRODUCTS.product_id\n" + 
    			"       join " + rsDBSchema + ".ERSMD_PRODUCT_INFO_DATA AS ersmd_product_info_data WITH (NOLOCK)\n" + 
    			"         ON ERS_PRODUCTS.product_id = ERSMD_PRODUCT_INFO_DATA.product_id\n" + 
    			"       join " + rsDBSchema + ".ERSMD_DIVISION_LOOKUP AS ersmd_division_lookup WITH (NOLOCK)\n" + 
    			"         ON ERSMD_PRODUCT_INFO_DATA.division_id = ERSMD_DIVISION_LOOKUP.division_id\n" + 
    			"       join " + rsDBSchema + ".ERS_CUSTOMERS AS ers_customers WITH (NOLOCK)\n" + 
    			"         ON ERS_CUSTOMERS.customer_id = ERS_LICENSES.customer_id AND ERS_CUSTOMERS.TARGET_TYPE = 2\n" + 
    			"       join " + rsDBSchema + ".ERS_USERS AS ers_users WITH (NOLOCK)\n" + 
    			"         ON ERS_CUSTOMERS.target_id = ERS_USERS.user_id\n" + 
    			"       join " + rsDBSchema + ".ERSMD_USER_INFO_DATA AS ersmd_user_info_data WITH (NOLOCK)\n" + 
    			"         ON ERS_USERS.user_id = ERSMD_USER_INFO_DATA.user_id\n" + 
    			"       join " + rsDBSchema + ".ERS_AUTH_PROFILES AS ers_auth_profiles WITH (NOLOCK)\n" + 
    			"         ON ERS_USERS.user_id = ERS_AUTH_PROFILES.user_id\n" + 
    			"       join " + rsDBSchema + ".ERS_CREDENTIALS AS ers_credentials WITH (NOLOCK)\n" + 
    			"         ON ERS_AUTH_PROFILES.profile_id = ERS_CREDENTIALS.profile_id\n" + 
    			"       join " + rsDBSchema + ".ERS_CRED_LOGIN_PASSWORD AS ers_cred_login_password WITH (NOLOCK)\n" + 
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
    			"		Isnull(ersmd_license_info_data.modified_date, ERS_LICENSES.date_modified) BETWEEN convert(datetime, ?, 126) AND convert(datetime, ?, 126) AND\n" + 
    			"		ERSMD_DIVISION_LOOKUP.division_type = ?";
               
		
		LOG.info("Query getRegistrationsByDateAndDivision : " + query);
		try {
			PreparedStatement ps = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,  ResultSet.CONCUR_READ_ONLY);
			ps.setString(1, fromDate.toLocalDateTime().toString());
			ps.setString(2, toDate.toLocalDateTime().toString());
			ps.setString(3, divisionType);
			
			ResultSet rs = ps.executeQuery();
			Object[] objArr = null;
			
			while(rs.next()){
				objArr = new Object[18];
				objArr[0] = rs.getString(1);
				objArr[1] = rs.getString(2);
				objArr[2] = rs.getString(3);
				objArr[3] = rs.getString(4);
				objArr[4] = rs.getString(5);
				objArr[5] = rs.getString(6);
				objArr[6] = rs.getString(7);
				objArr[7] = rs.getString(8);
				objArr[8] = rs.getString(9);
				objArr[9] = rs.getString(10);
				objArr[10] = rs.getString(11);
				objArr[11] = rs.getString(12);
				objArr[12] = rs.getString(13);
				objArr[13] = rs.getTimestamp(14);
				objArr[14] = rs.getTimestamp(15);
				objArr[15] = rs.getTimestamp(16);
				objArr[16] = rs.getTimestamp(17);
				objArr[17] = rs.getString(18);
				result.add(objArr);
			}
		} catch (SQLException e) {
			LOG.error("Error in data export dao : "+e);
			throw e ;
		}	
		finally{
			try {
				con.close();
			} catch (SQLException e) {
				LOG.error("Error in data export dao finally : "+e);
				throw e ;
			}
		}
		
		return result;
	}
	
	public List<String> getAllQuestions() throws SQLException{
		Connection con = ConnectionUtil.getConnection();
		List<String> result = new ArrayList<String>();
		String query = "SELECT DISTINCT QUE.DESCRIPTION FROM QUESTION que";
               
		
		LOG.info("Query getAllQuestions : " + query);
		try {
			PreparedStatement ps = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,  ResultSet.CONCUR_READ_ONLY);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				result.add(rs.getString(1));
			}
		} catch (SQLException e) {
			LOG.error("Error in org unit data export dao : "+e);
			throw e ;
		}	
		finally{
			try {
				con.close();
			} catch (SQLException e) {
				LOG.error("Error in org unit data export dao finally : "+e);
				throw e ;
			}
		}
		
		return result;
	}
	
	public List<String[]> getOrgUnitUsage(DateTime fromTime, DateTime toTime) throws SQLException {
		Connection con = ConnectionUtil.getConnection();
		List<String[]> result = new ArrayList<String[]>();
		String ER_DB_NAME_WITH_SCHEMA = BatchJobProperties.ER_DB_NAME;
		String query = "SELECT cast(ERSMD_DIVISION_LOOKUP.division_type as varchar(max)) division, count(ers_licenses.license_id) usage_count\n" + 
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
        		"         ON ERS_CUSTOMERS.customer_id = ERS_LICENSES.customer_id  AND ERS_CUSTOMERS.TARGET_TYPE = 2\n" + 
        		"       join "+ER_DB_NAME_WITH_SCHEMA+".ERS_USERS AS ers_users\n" + 
        		"         ON ERS_CUSTOMERS.target_id = ERS_USERS.user_id\n" + 
        		"       join "+ER_DB_NAME_WITH_SCHEMA+".ERS_AUTH_PROFILES AS ers_auth_profiles\n" + 
        		"         ON ERS_USERS.user_id = ERS_AUTH_PROFILES.user_id\n" + 
        		"       join "+ER_DB_NAME_WITH_SCHEMA+".ERS_CREDENTIALS AS ers_credentials\n" + 
        		"         ON ERS_AUTH_PROFILES.profile_id = ERS_CREDENTIALS.profile_id\n" + 
        		"       join "+ER_DB_NAME_WITH_SCHEMA+".ERS_CRED_LOGIN_PASSWORD AS ers_cred_login_password\n" + 
        		"         ON ERS_CREDENTIALS.credential_id = ERS_CRED_LOGIN_PASSWORD.credential_id\n" + 
        		"WHERE  ERS_CRED_LOGIN_PASSWORD.date_login_succeeded BETWEEN convert(datetime, ?, 126) AND convert(datetime, ?, 126) \n"
        		+ "		Group by ERSMD_DIVISION_LOOKUP.division_type";
       
		LOG.info("Query getRegistrationsByDateAndDivision : " + query);
		try {
			PreparedStatement ps = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,  ResultSet.CONCUR_READ_ONLY);
			ps.setString(1, fromTime.toLocalDateTime().toString());
			ps.setString(2, toTime.toLocalDateTime().toString());
			
			ResultSet rs = ps.executeQuery();
			Integer tot = 0;
        	while (rs.next()) {
                tot += rs.getInt("usage_count");
            }
        	rs.beforeFirst();
            BigDecimal total = new BigDecimal(tot);
            BigDecimal hundred = new BigDecimal(100);
            while (rs.next()) {
                Integer val = rs.getInt("usage_count");
                BigDecimal calced;
                if (val == null || val.intValue() == 0L) {
                    calced = BigDecimal.ZERO.setScale(2);
                } else {
                    calced = new BigDecimal(val.intValue()).setScale(2).divide(total, 8, RoundingMode.HALF_EVEN)
                            .multiply(hundred).setScale(2, RoundingMode.HALF_EVEN);
                }
                String percentage = calced.toString();
                String[] res = new String[] { rs.getString("division"), String.valueOf(val), percentage };
                result.add(res);
            }
		} catch (SQLException e) {
			LOG.error("Error in data export dao : "+e);
			throw e ;
		}	
		finally{
			try {
				con.close();
			} catch (SQLException e) {
				LOG.error("Error in data export dao finally : "+e);
				throw e ;
			}
		}
		
		return result;
	
	}
	
	public List<String> getProductByDivision(
			String divisionType) throws SQLException {
		List result = new ArrayList<String>();
		Connection con = ConnectionUtil.getConnection();
		String query = "select distinct(registration_definition.product_id) from registration_definition "
				+ "join page_definition on page_definition.id = registration_definition.page_definition_id "
				+ "join "+rsDBSchema+".ersmd_division_lookup as div on div.DIVISION_ID = page_definition.division_erights_id "
				+ "where div.DIVISION_TYPE=?";
		LOG.info("Query getProductByDivision : " + query);
		try {
			PreparedStatement ps = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,  ResultSet.CONCUR_READ_ONLY);
			ps.setString(1, divisionType);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				result.add(rs.getString(1));
			}
		} catch (SQLException e) {
			LOG.error("Error in data export dao : "+e);
			throw e ;
		}	
		finally{
			try {
				con.close();
			} catch (SQLException e) {
				LOG.error("Error in data export dao finally : "+e);
				throw e ;
			}
		}
		return result;
	}
}
