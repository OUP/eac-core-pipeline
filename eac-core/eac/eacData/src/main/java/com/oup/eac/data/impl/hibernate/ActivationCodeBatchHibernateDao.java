package com.oup.eac.data.impl.hibernate;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.ActivationCodeBatchDao;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.DivisionAdminUser;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.ActivationCodeBatchReportCriteria;
import com.oup.eac.dto.ActivationCodeBatchSearchCriteria;
/**
 * Load activation code batches.
 * 
 * @author Ian Packard
 *
 */
@Repository(value="acitvationCodeBatchDao")
public class ActivationCodeBatchHibernateDao extends HibernateBaseDao<ActivationCodeBatch, String> implements ActivationCodeBatchDao {

    private Logger LOG = Logger.getLogger(ActivationCodeBatchHibernateDao.class);
    
	@Autowired
    public ActivationCodeBatchHibernateDao(final SessionFactory sf) {
        super(sf);
    }
	

	@SuppressWarnings("unchecked")
	@Override
    public List<Object[]> getActivationCodeReport(final ActivationCodeBatchReportCriteria activationCodeReportCriteria, String rsDBSchema) {
	    Query query = getSession().createSQLQuery(buildQuery(activationCodeReportCriteria, rsDBSchema, false));
	    addParameters(query, activationCodeReportCriteria);
	    return query.setMaxResults(activationCodeReportCriteria.getMaxResults()).list();
	}

    @Override
    public final Long getActivationCodeReportCount(final ActivationCodeBatchReportCriteria reportCriteria, String rsDBSchema) {
        Query query = getSession().createSQLQuery(buildQuery(reportCriteria, rsDBSchema, true));
        addParameters(query, reportCriteria);
        return (long)query.list().size();      
    }
	
	private String buildQuery(
			final ActivationCodeBatchReportCriteria reportCriteria,
			String rsDBSchema, boolean count) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT Cast( cf.claim_folder_id AS VARCHAR(510))                                                                                        AS CLAIM_FOLDER_ID,\n"); 
		query.append("                Cast( cf.display_name AS VARCHAR(510))                                                                                           DISPLAY_NAME,\n"); 
		query.append("                cf.date_created,\n"); 
		query.append("                ( SELECT Count( * )\n"); 
		query.append("                  FROM   " + rsDBSchema + ".ERS_CLAIM_TICKET\n"); 
		query.append("                  WHERE  cf.claim_folder_id = ERS_CLAIM_TICKET.claim_folder_id )                                                                 AS NO_OF_ACTIVATIONCODE,\n"); 
		query.append("                ( SELECT Count( * )\n"); 
		query.append("                  FROM   " + rsDBSchema + ".ERS_CLAIM_TICKET\n"); 
		query.append("                  WHERE  ERS_CLAIM_TICKET.claim_folder_id = cf.claim_folder_id AND\n"); 
		query.append("                         ( ( ERS_CLAIM_TICKET.claim_ceiling - ( SELECT Count( * )\n"); 
		query.append("                                                                FROM   " + rsDBSchema + ".ERS_CLAIMS\n"); 
		query.append("                                                                WHERE  ERS_CLAIMS.claim_ticket_id = ERS_CLAIM_TICKET.claim_ticket_id ) ) = 0 ) ) AS UNAVAILABLE_COUNT,\n"); 
		query.append("                ( SELECT Count( * )\n"); 
		query.append("                  FROM   " + rsDBSchema + ".ERS_CLAIM_TICKET\n"); 
		query.append("                  WHERE  ERS_CLAIM_TICKET.claim_folder_id = cf.claim_folder_id AND\n"); 
		query.append("                         ( ( ERS_CLAIM_TICKET.claim_ceiling - ( SELECT Count( * )\n"); 
		query.append("                                                                FROM   " + rsDBSchema + ".ERS_CLAIMS\n"); 
		query.append("                                                                WHERE  ERS_CLAIMS.claim_ticket_id = ERS_CLAIM_TICKET.claim_ticket_id ) ) > 0 ) ) AS AVAILABLE_COUNT,\n"); 
		query.append("                ( SELECT SUM(cast( ERS_CLAIM_TICKET.claim_ceiling as numeric) )\n"); 
		query.append("                  FROM   " + rsDBSchema + ".ERS_CLAIM_TICKET\n"); 
		query.append("                  WHERE  ERS_CLAIM_TICKET.claim_folder_id = cf.claim_folder_id )                                                                 AS ALLOWED_USAGES,\n"); 
		query.append("                ( SELECT Count( * )\n"); 
		query.append("                  FROM   " + rsDBSchema + ".ERS_CLAIMS\n"); 
		query.append("                         join " + rsDBSchema + ".ERS_CLAIM_TICKET ect\n"); 
		query.append("                           ON ERS_CLAIMS.claim_ticket_id = ect.claim_ticket_id\n"); 
		query.append("                  WHERE  ect.claim_folder_id = cf.claim_folder_id )                                                                              AS ACTUAL_USAGES,\n"); 
		query.append("                cf.effective_date,\n"); 
		query.append("                cf.expiration_date,\n" );
		query.append("                Cast(ERS_PRODUCTS.display_name AS VARCHAR(510))                                                                                                      AS PRODUCT_NAME,\n"); 
		query.append("                ERS_CLAIM_DIRECT_PRODUCTS.product_id,\n"); 
		query.append("                ERSMD_CLAIM_DRCT_PRD_INFO_DATA.product_group_name                                                                                AS PRODUCT_GROUP_NAME,\n"); 
		query.append("                ERS_CLAIM_DIRECT_LICENSES.license_type,\n"); 
		query.append("                ERS_CLAIM_DIRECT_LICENSES.start_date,\n"); 
		query.append("                ERS_CLAIM_DIRECT_LICENSES.end_date,\n"); 
		query.append("                ERS_CLAIM_DIRECT_LICENSES.int2                                                                                                   AS LICENSE_TOTAL_CONCURRENCY,\n"); 
		query.append("                ERS_CLAIM_DIRECT_LICENSES.int2                                                                                                   AS UNIT_TYPE,\n"); 
		query.append("                ERS_CLAIM_DIRECT_LICENSES.int3                                                                                                   AS BEGIN_ON,\n"); 
		query.append("                ERS_CLAIM_DIRECT_LICENSES.int1                                                                                                   AS TIME_PERIOD,\n"); 
		query.append("                ERS_CLAIM_DIRECT_LICENSES.int1                                                                                                   AS LICENSE_ALLOWED_USAGES\n"); 
		query.append("FROM   " + rsDBSchema + ".ERS_CLAIM_FOLDERS cf\n"); 
		query.append("        join " + rsDBSchema + ".ERS_CLAIM_DIRECT_ENTITLEMENT\n"); 
		query.append("              ON ERS_CLAIM_DIRECT_ENTITLEMENT.claim_node_id = cf.claim_node_id\n"); 
		query.append("       left join " + rsDBSchema + ".ERS_CLAIM_DIRECT_PRODUCTS\n"); 
		query.append("              ON ERS_CLAIM_DIRECT_PRODUCTS.cd_entitlement_id = ERS_CLAIM_DIRECT_ENTITLEMENT.cd_entitlement_id\n"); 
		query.append("       left join " + rsDBSchema + ".ERS_CLAIM_DIRECT_LICENSES\n"); 
		query.append("              ON ERS_CLAIM_DIRECT_LICENSES.cd_entitlement_id = ERS_CLAIM_DIRECT_ENTITLEMENT.cd_entitlement_id\n"); 
		query.append("       left join " + rsDBSchema + ".ERSMD_CLAIM_DRCT_PRD_INFO_DATA\n"); 
		query.append("              ON ERSMD_CLAIM_DRCT_PRD_INFO_DATA.claim_direct_products_id = ERS_CLAIM_DIRECT_PRODUCTS.claim_direct_products_id\n"); 
		query.append("       left join " + rsDBSchema + ".ERS_PRODUCTS\n"); 
		query.append("              ON ERS_PRODUCTS.product_id = ERS_CLAIM_DIRECT_PRODUCTS.product_id\n"); 
		query.append("       left join " + rsDBSchema + ".ERSMD_PRODUCT_INFO_DATA\n"); 
		query.append("              ON ERSMD_PRODUCT_INFO_DATA.product_id = ERS_PRODUCTS.product_id \n");
		
		query.append(buildWhereClause(reportCriteria, rsDBSchema));
		query.append(buildOrderBy(reportCriteria, count));
		return query.toString();
	}
    
    private void addParameters(final Query query, final ActivationCodeBatchReportCriteria reportCriteria) {
        if(StringUtils.isNotBlank(reportCriteria.getDivisionId())) {
            query.setParameter("divisionId", reportCriteria.getDivisionId());
        }
        if(StringUtils.isNotBlank(reportCriteria.getPlatformCode())) {
            query.setParameter("platformCode", reportCriteria.getPlatformCode());
        }
        if(StringUtils.isNotBlank(reportCriteria.getProductId())) {
            query.setParameter("productId", reportCriteria.getProductId());
        }
        if(StringUtils.isNotBlank(reportCriteria.getEacGroupId())) {
            query.setParameter("eacGroupId", reportCriteria.getEacGroupId());
        }
        if(StringUtils.isNotBlank(reportCriteria.getBatchId())) {
            query.setParameter("batchId", reportCriteria.getBatchId());
        }        
    }
	
    private String buildWhereClause(final ActivationCodeBatchReportCriteria reportCriteria, final String rsDBSchema) {
        StringBuilder whereClause = new StringBuilder();
       if(StringUtils.isNotBlank(reportCriteria.getDivisionId())) {
            //addWhereClause(whereClause, "p.division.id = :divisionId ");
            addWhereClause(whereClause, "COALESCE(ersmd_product_info_data.division_id, :divisionId) = :divisionId ");
        }
       if(StringUtils.isNotBlank(reportCriteria.getPlatformCode())) {
       	addWhereClause(whereClause, "ERS_PRODUCTS.PRODUCT_ID IN ( SELECT ERSMD_PLATFORM_PRODUCT_MAPPING.PRODUCT_ID "
       			+ "FROM " + rsDBSchema + ".ERSMD_PLATFORM_PRODUCT_MAPPING AS ERSMD_PLATFORM_PRODUCT_MAPPING "
       			+ "JOIN " + rsDBSchema + ".ERSMD_PLATFORM_MASTER_LOOKUP AS ERSMD_PLATFORM_MASTER_LOOKUP "
       			+ "ON ERSMD_PLATFORM_PRODUCT_MAPPING.PLATFORM_ID = ERSMD_PLATFORM_MASTER_LOOKUP.PLATFORM_ID "
       			+ "AND ERSMD_PLATFORM_MASTER_LOOKUP.PLATFORM_CODE = :platformCode ) ");
       }
        if(StringUtils.isNotBlank(reportCriteria.getProductId())) {
            addWhereClause(whereClause, "ers_products.product_id = :productId ");
        }
        if(StringUtils.isNotBlank(reportCriteria.getEacGroupId())) {
            addWhereClause(whereClause, "ersmd_claim_drct_prd_info_data.product_group_id = :eacGroupId ");
        }
        if(StringUtils.isNotBlank(reportCriteria.getBatchId())) {
            addWhereClause(whereClause, "cf.display_name = :batchId ");
        }
        return whereClause.toString();
    }
    
    private void addWhereClause(StringBuilder whereClauseBuilder, String whereClause) {
        if(whereClauseBuilder.length() > 0) {
            whereClauseBuilder.append("and ");
        } else {
            whereClauseBuilder.append("where ");
        }
        whereClauseBuilder.append(whereClause);
    }
    
    private String buildGroupByClause(final ActivationCodeBatchReportCriteria reportCriteria, boolean count) {
        StringBuilder groupByClause = new StringBuilder();
        if(!count) {
            groupByClause.append("group by acb.batchId, acb.id, acb.createdDate, acb.startDate, acb.endDate, p.productName, p.id, eg.groupName,");
            groupByClause.append("lt.class, lt.startDate, lt.endDate, lt.totalConcurrency, lt.unitType, lt.beginOn,");
            groupByClause.append("lt.timePeriod, lt.allowedUsages ");
        }
        return groupByClause.toString();
    }
    
    private String buildOrderBy(final ActivationCodeBatchReportCriteria reportCriteria, boolean count) {
        StringBuilder orderByClause = new StringBuilder();
        if(!count) {
            orderByClause.append(" order by CLAIM_FOLDER_ID, CAST(cf.DISPLAY_NAME AS VARCHAR(510)) ");
        }
        return orderByClause.toString();
    }
	
	@Override
    public final ActivationCodeBatch getActivationCodeBatchByBatchId(final String batchId) {
    	return (ActivationCodeBatch)getSession().createQuery("select acb from ActivationCodeBatch acb " +
    			"where acb.batchId = :batchId")
    			.setParameter("batchId", batchId)
    			.uniqueResult();
    }

    @Override
    public final ActivationCodeBatch getActivationCodeBatchByBatchDbId(final String batchDbId) {
        return (ActivationCodeBatch)getSession().createQuery("select acb from ActivationCodeBatch acb " +
                "where acb.id = :dbId")
                .setParameter("dbId", batchDbId)
                .uniqueResult();
    }

    @SuppressWarnings("unchecked")
	public final List<ActivationCodeBatch> getActivationCodeBatchByDivision(final List<DivisionAdminUser> divisionAdminUsers) {
    	return getSession().createQuery("select acb from ActivationCodeBatch acb " +
    									"join acb.activationCodeRegistrationDefinition rd " +
    									"join rd.product pd " +
    									"join p.division d " +
    									"join d.divisionAdminUsers dau " +
    									"where dau in(:divisionAdminUsers)")
    									.setParameterList("divisionAdminUsers", divisionAdminUsers)
    									.list();
    }

	@Override
	public int countSearchActivationCodeBatches(final ActivationCodeBatchSearchCriteria searchCriteria, final PagingCriteria pagingCriteria) {
		
    	Query query = getQueryForCountSearchActivationCodeBatches(searchCriteria);
    	setQueryParamsForSearch(query, searchCriteria);
    	
    	Long count = (Long) query.uniqueResult();

    	return count.intValue();
	}
	
    @SuppressWarnings("unchecked")
    public List<ActivationCodeBatch> searchActivationCodeBatches(final ActivationCodeBatchSearchCriteria searchCriteria, final PagingCriteria pagingCriteria) {

        Query query = getQueryForSearchActivationCodeBatches(searchCriteria, pagingCriteria);
        
        setQueryParamsForSearch(query, searchCriteria);
        query.setFirstResult((pagingCriteria.getRequestedPage() - 1) * pagingCriteria.getItemsPerPage());
        query.setMaxResults(pagingCriteria.getItemsPerPage());
        
        return query.list();
    }
   
	private Query getQueryForSearchActivationCodeBatches(final ActivationCodeBatchSearchCriteria searchCriteria, final PagingCriteria pagingCriteria) {
		StringBuilder sb = new StringBuilder();
        
        sb.append("select distinct acb");
        sb.append(getBaseQueryStringForSearchActivationCodeBatches(searchCriteria));
		sb.append(" order by acb.");
		
		if (pagingCriteria.getSortColumn() != null) {
			sb.append(pagingCriteria.getSortColumn());
		} else {
			sb.append("batchId");
		}
		
		sb.append(" ");
		sb.append(pagingCriteria.getSortDirection());
		sb.append(", acb.createdDate desc");
		
        return getSession().createQuery(sb.toString());
	}
	
private String getBaseQueryStringForSearchActivationCodeBatches(final ActivationCodeBatchSearchCriteria searchCriteria) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(" from ActivationCodeBatch acb " +
		        "join acb.licenceTemplate lt " +
		        "join acb.activationCodeRegistrationDefinition rd " +
		        "left join rd.eacGroup eg "+		        
		        "left join rd.product p  " +
				"left join p.division div " +
				"left join div.divisionAdminUsers dad "+
				"left join dad.adminUser duser "+
				"where 1 = 1 and  coalesce(duser.id,:adminUser) = :adminUser ");
		
		
		if (searchCriteria.getCode() != null) {
		    sb.append( "and acb.id in (select ac.activationCodeBatch.id from ActivationCode ac where ac.code = :code) ");
		}
		
		if (searchCriteria.getLicenceTemplate() != null) {
			sb.append("and lt.class = " + searchCriteria.getLicenceTemplate().getSimpleName() + " ");
		}
		
		if (searchCriteria.getBatchDate() != null) {
			sb.append("and (acb.startDate <= :batchDate or acb.startDate is null) and (acb.endDate >= :batchDate or acb.endDate is null)");
		}
		
		if (searchCriteria.getBatchId() != null) {
			sb.append("and acb.batchId like :batchId ");
		}
		
		if (searchCriteria.getRegisterableProduct() != null) {
			sb.append("and p = :registerableProduct ");
		}
		
		if(searchCriteria.getEacGroupId() != null){
			sb.append("and eg.id = :eacGroup ");
		}
		
		return sb.toString();
	}
	
	
	private void setQueryParamsForSearch(final Query query, final ActivationCodeBatchSearchCriteria searchCriteria) {
		if (searchCriteria.getCode() != null) {
            query.setParameter("code", searchCriteria.getCode());
        }
        if (searchCriteria.getBatchDate() != null) {
            query.setParameter("batchDate", searchCriteria.getBatchDate());
        }
        if (searchCriteria.getBatchId() != null) {
            query.setParameter("batchId", "%"+searchCriteria.getBatchId()+"%");
        }
        if (searchCriteria.getRegisterableProduct() != null) {
            query.setParameter("registerableProduct", searchCriteria.getRegisterableProduct());
        }
        
       if(searchCriteria.getEacGroupId() != null){
        	query.setParameter("eacGroup", searchCriteria.getEacGroupId());
        }
        
       query.setParameter("adminUser", searchCriteria.getAdminUser().getId());
	}
	

	private Query getQueryForCountSearchActivationCodeBatches(final ActivationCodeBatchSearchCriteria searchCriteria) {
		StringBuilder sb = new StringBuilder();
        
        sb.append("select count(distinct acb)");
        sb.append(getBaseQueryStringForSearchActivationCodeBatches(searchCriteria));
		
        return getSession().createQuery(sb.toString());
	}

    @Override
    public boolean isBatchUsed(String batchDbId) {
        long start = System.currentTimeMillis();
        long usedCodes = (Long)getSession().createQuery(
        " select count(ac) from ActivationCode ac " +
        " join ac.activationCodeBatch acb " +
        " where acb.id = :acbid and exists (select acr from ActivationCodeRegistration acr where acr.activationCode = ac) "
        )
        .setParameter("acbid", batchDbId)
        .uniqueResult();
        long diff = System.currentTimeMillis() - start;
        boolean used =  usedCodes > 0;
        String msg = String.format("Took [%d] ms to figure out if batch with db id[%s] is used [%b]", diff, batchDbId, used);
        LOG.info(msg);
        return used;
    }
    
    @Override
    public boolean archiveBatch(ActivationCodeBatch batch) {
        boolean result = false;
        try {
            Session session = getSession();

            @SuppressWarnings("deprecation")
            Connection con = session.connection();
            CallableStatement statement = con.prepareCall("{ call EAC_ARCHIVE_BATCH (?) }");
            statement.setString(1, batch.getId());
            statement.executeUpdate();

            /* we don't want any changes from the hibernate 'batch' being sent to db as it's been deleted! */
            session.evict(batch);
            result = true;
            String msg = String.format("Activation Code Batch [%s] archived.", batch.getBatchId());
            LOG.info(msg);
            AuditLogger.logEvent(msg);
        } catch (Exception ex) {
            LOG.error("unexpected exception attempting to archive batch " + batch.getBatchId(), ex);
        }
        LOG.debug("archiveBatch " + batch.getBatchId() + " result="+result);
        return result;
    }

    @Override
    public int getNumberOfTokensInBatch(String batchDbId) {
        long tokens = (Long)getSession().createQuery(
        " select count(ac) from ActivationCode ac " +
        " join ac.activationCodeBatch acb " +
        " where acb.id = :acbid")
        .setParameter("acbid", batchDbId)
        .uniqueResult();
        return (int)tokens;
    }

    @Override
    public boolean doesArchivedBatchExist(String batchName) {
        boolean result = false;
        try {
            Session session = getSession();

            @SuppressWarnings("deprecation")
            Connection con = session.connection();
            PreparedStatement statement = con.prepareStatement("select count(*) from arc_activation_code_batch where batch_id = ?");
            statement.setString(1, batchName);
            ResultSet res = statement.executeQuery();
            if(res.next()){
                result = res.getInt(1) > 0;
            }
        } catch (SQLException sqle) {
            LOG.error("unexpected exception attempting to find if archived batch with this name exists." + batchName, sqle);
        }
        return result;
    }
}
